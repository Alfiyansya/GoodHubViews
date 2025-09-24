package com.alfiansyah.goodhubviews.core.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Parcelable
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import androidx.viewbinding.ViewBinding
import com.alfiansyah.goodhubviews.core.R
import com.alfiansyah.goodhubviews.core.databinding.LayoutPermissionDialogBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

// Fungsi untuk target gambar
fun imageTarget(
    context: Context,
    onResources: (Drawable) -> Unit,
) = object : CustomTarget<Drawable>() {
    override fun onResourceReady(
        resource: Drawable,
        transition: Transition<in Drawable>?
    ) {

//                                                binding.pos1.progressLayout.visibility = View.GONE
        onResources(resource)
//                                                binding.pos1.posPhotoName.text = image2File.name
    }

    override fun onLoadStarted(placeholder: Drawable?) {

        val progressBar = CircularProgressDrawable(context)
        progressBar.setColorSchemeColors(
            ContextCompat.getColor(context, R.color.grey),
            ContextCompat.getColor(context, R.color.black)
        )
        progressBar.setCenterRadius(50f)
        progressBar.setStrokeWidth(20f)
        // Set bounds to prevent zero-dimension bitmap creation
        val size = (progressBar.centerRadius * 2 + progressBar.strokeWidth).toInt()
        progressBar.setBounds(0, 0, size, size)
        progressBar.start()
        onResources(progressBar)
    }

    override fun onLoadFailed(errorDrawable: Drawable?) {
        onResources(AppCompatResources.getDrawable(context, R.drawable.image_failed)!!)
    }

    override fun onLoadCleared(placeholder: Drawable?) {
        //Nothing
    }
}

// Mengunduh dan menampilkan gambar menggunakan glide
fun loadAndShowImage(
    context: Context,
    data: String?,
    imageTarget: CustomTarget<Drawable>
) {
    Glide.with(context)
        .load(data)
        .into(imageTarget)

}
fun Fragment.showShortToast(message: String?) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}
inline fun <reified T : Parcelable> Intent.parcelableExtra(key: String): T? = when {
    Build.VERSION.SDK_INT >= 33 -> getParcelableExtra(key, T::class.java) as T
    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
}
//Fungsi untuk beradaptasi dengan android 15
fun Activity.avoidEdgeToEdge(binding: ViewBinding) {
    val windowInsetsController =
        WindowCompat.getInsetsController(window, window.decorView)
    windowInsetsController.show(WindowInsetsCompat.Type.statusBars())
    windowInsetsController.isAppearanceLightStatusBars = true
    ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->
        val bars = windowInsets.getInsets(
            WindowInsetsCompat.Type.systemBars()
                    or WindowInsetsCompat.Type.displayCutout()
        )

        view.updatePadding(
            left = bars.left,
            top = bars.top,
            right = bars.right,
            bottom = bars.bottom,
        )
        WindowInsetsCompat.CONSUMED
    }
}
fun Activity.showAlertDialogWithPositiveBtnAction(
    message: String,
    positiveBtnText: String,
    positiveBtnAction: () -> Unit
) {
    val alertDialog = AlertDialog.Builder(this).create()
    val alertDialogBinding = LayoutPermissionDialogBinding.inflate(layoutInflater)
    with(alertDialog) {
        setCancelable(true)
        setView(alertDialogBinding.root)
    }
    alertDialogBinding.errorTitleTv.text = message
    alertDialogBinding.btnYes.visibility = View.VISIBLE
    alertDialogBinding.btnYes.text = positiveBtnText
    alertDialogBinding.btnYes.setOnClickListener {
        alertDialog.dismiss()
        positiveBtnAction()
    }
    alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    alertDialog.show()
}
fun Activity.openApplicationDetailSetting() {
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
    val uri = Uri.fromParts("package", packageName, null)
    intent.data = uri
    startActivity(intent)
}

// izin ditolak
fun Activity.isPermissionDenied(permission: String): Boolean {
    return ActivityCompat.shouldShowRequestPermissionRationale(this, permission)
}
fun Activity.showShortToast(message: String?) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

// izin diterima
fun Activity.isPermissionEnabled(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        permission
    ) == PackageManager.PERMISSION_GRANTED
}

fun Activity.showPermissionRequiredDialog() {
    showAlertDialogWithPositiveBtnAction(
        getString(R.string.all_permission_access_required),
        getString(R.string.ok)
    ) {
        openApplicationDetailSetting()  // Buka pengaturan aplikasi untuk meminta izin secara manual
    }
}
fun Activity.startDownload(url: String) {
    val intent = Intent(this, CustomDownloadService::class.java).apply {
        putExtra("URL", url)
    }
    ContextCompat.startForegroundService(this, intent)
    showShortToast("Starting download...")
}
