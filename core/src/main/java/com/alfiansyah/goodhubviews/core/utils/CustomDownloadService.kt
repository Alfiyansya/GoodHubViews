package com.alfiansyah.goodhubviews.core.utils

import android.Manifest
import android.R
import android.app.*
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.IBinder
import android.provider.MediaStore
import android.util.Log
import android.webkit.URLUtil
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.alfiansyah.goodhubviews.core.utils.Constant.NOTIFICATION_DOWNLOAD_CHANNEL_ID
import com.alfiansyah.goodhubviews.core.utils.Constant.NOTIFICATION_DOWNLOAD_CHANNEl_NAME
import com.alfiansyah.goodhubviews.core.utils.Constant.NOTIFICATION_ID
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class CustomDownloadService : Service() {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    @Inject
    lateinit var baseNotificationBuilder: NotificationCompat.Builder

    companion object {
        const val TAG = "DownloadDebug"
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "[FLOW] onStartCommand dipanggil.")

        val url = intent?.getStringExtra("URL") ?: return START_NOT_STICKY
        Log.d(TAG, "[FLOW] URL diterima: $url")

        if (!isNetworkAvailable()) {
            Log.e(TAG, "Tidak ada koneksi internet")
            updateNotificationError("Error: Tidak ada koneksi internet")
            stopSelf()
            return START_NOT_STICKY
        }

        if (!hasStoragePermission()) {
            Log.e(TAG, "Tidak ada izin penyimpanan")
            updateNotificationError("Error: Tidak ada izin penyimpanan")
            stopSelf()
            return START_NOT_STICKY
        }

        try {
            Log.d(TAG, "[FLOW] Memanggil createNotificationChannel...")
            createNotificationChannel()
            Log.d(TAG, "[FLOW] Selesai memanggil createNotificationChannel.")

            val initialNotification = createNotificationService()
            Log.d(TAG, "[FLOW] Notifikasi awal berhasil dibuat.")

            Log.d(TAG, "[FLOW] Memanggil startForeground...")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                startForeground(
                    NOTIFICATION_ID,
                    initialNotification,
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
                )
            } else {
                startForeground(NOTIFICATION_ID, initialNotification)
            }
            Log.d(
                TAG,
                "[FLOW] Selesai memanggil startForeground. Notifikasi seharusnya sudah muncul."
            )

        } catch (e: Exception) {
            Log.e(TAG, "[FLOW] CRASH saat memulai service di onStartCommand: ${e.message}", e)
            stopSelf()
            return START_NOT_STICKY
        }

        scope.launch {
            try {
                downloadFileWithProgress(url)
            } catch (e: Exception) {
                Log.e(TAG, "Terjadi error saat download: ${e.message}", e)
                updateNotificationProgress(0, "Error: ${e.localizedMessage}")
                stopSelf()
            }
        }
        return START_STICKY
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        } else {
            @Suppress("DEPRECATION")
            connectivityManager.activeNetworkInfo?.isConnected == true
        }
    }

    private fun hasStoragePermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            true
        } else {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun downloadFileWithProgress(url: String) {
        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        val requestBuilder = Request.Builder().url(url)

        val request = requestBuilder.build()

        try {
            val response = client.newCall(request).execute()

            if (!response.isSuccessful) {
                Log.e(TAG, "Request Gagal. Kode: ${response.code}, Pesan: ${response.message}")
                updateNotificationProgress(0, "Gagal: Error Server ${response.code}")
                stopSelf()
                return
            }

            val body = response.body
            if (body == null) {
                Log.e(TAG, "Response body is null")
                updateNotificationProgress(0, "Error: Response kosong")
                stopSelf()
                return
            }

            val totalBytes = body.contentLength()
            Log.d(TAG, "Total bytes to download: $totalBytes")

            val fileName = URLUtil.guessFileName(
                url,
                response.header("Content-Disposition"),
                body.contentType()?.toString()
            )

            val resolver = applicationContext.contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, body.contentType()?.toString() ?: "application/octet-stream")

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/GoodHub")
                }
            }

            val uri = try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                } else {
                    val downloadsDir = File(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                        "GoodHub"
                    )
                    if (!downloadsDir.exists()) {
                        downloadsDir.mkdirs()
                    }
                    val file = File(downloadsDir, fileName)
                    Uri.fromFile(file)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error creating file URI: ${e.message}", e)
                null
            }

            if (uri == null) {
                Log.e(TAG, "Gagal membuat entri MediaStore.")
                updateNotificationProgress(0, "Error: Gagal membuat file")
                stopSelf()
                return
            }

            Log.d(TAG, "File akan disimpan di URI: $uri")

            resolver.openOutputStream(uri)?.use { output ->
                var bytesCopied: Long = 0
                val buffer = ByteArray(8 * 1024)
                var lastUpdateTime: Long = 0

                body.byteStream().use { input ->
                    var bytes: Int
                    while (input.read(buffer).also { bytes = it } != -1) {
                        output.write(buffer, 0, bytes)
                        bytesCopied += bytes

                        if (totalBytes > 0) {
                            val progress = ((bytesCopied * 100) / totalBytes).toInt()
                            if (System.currentTimeMillis() - lastUpdateTime > 500) {
                                updateNotificationProgress(progress)
                                lastUpdateTime = System.currentTimeMillis()
                            }
                        }
                    }
                }
            } ?: run {
                Log.e(TAG, "Gagal membuka output stream")
                updateNotificationProgress(0, "Error: Gagal menulis file")
                stopSelf()
                return
            }

            val openFileIntent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, resolver.getType(uri))
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            val pendingIntent = PendingIntent.getActivity(
                this@CustomDownloadService,
                0,
                openFileIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            updateNotificationProgress(100, "Unduhan Selesai. Klik untuk membuka.", pendingIntent)
            Log.d(TAG, "Download Selesai, file disimpan di publik: $fileName")

        } catch (e: IOException) {
            Log.e(TAG, "Network error: ${e.message}", e)
            updateNotificationProgress(0, "Error: Masalah jaringan")
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error: ${e.message}", e)
            updateNotificationProgress(0, "Error: ${e.localizedMessage}")
        } finally {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                stopForeground(STOP_FOREGROUND_DETACH)
            } else {
                @Suppress("DEPRECATION")
                stopForeground(false)
            }
            stopSelf()
        }
    }

    private fun updateNotificationProgress(
        progress: Int,
        contentText: String? = null,
        pendingIntent: PendingIntent? = null
    ) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val builder = baseNotificationBuilder
            .setContentTitle("Mengunduh file...")
            .setContentText(contentText ?: "Progress : $progress%")
            .setSmallIcon(R.drawable.stat_sys_download)
            .setOngoing(progress < 100)

        if (progress < 100) {
            builder.setProgress(100, progress, false)
        } else {
            builder.setProgress(0, 0, false)
                .setSmallIcon(R.drawable.stat_sys_download_done)
                .setContentTitle("Download Selesai")
        }

        if (pendingIntent != null) {
            builder.setContentIntent(pendingIntent)
                .setAutoCancel(true)
        }

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    private fun updateNotificationError(errorMessage: String) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val builder = baseNotificationBuilder
            .setContentTitle("Download Gagal")
            .setContentText(errorMessage)
            .setSmallIcon(R.drawable.stat_notify_error)
            .setAutoCancel(true)
            .setOngoing(false)

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    private fun createNotificationService(): Notification {
        return baseNotificationBuilder
            .setContentTitle("Downloading files")
            .setContentText("Starting download...")
            .setSmallIcon(R.drawable.stat_sys_download)
            .setOngoing(true)
            .setProgress(100, 0, false)
            .build()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            NOTIFICATION_DOWNLOAD_CHANNEL_ID,
            NOTIFICATION_DOWNLOAD_CHANNEl_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Service di-destroy, job dibatalkan.")
        job.cancel()
    }
}