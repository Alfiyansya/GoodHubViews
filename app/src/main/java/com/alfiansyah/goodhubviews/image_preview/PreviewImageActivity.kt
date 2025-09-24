package com.alfiansyah.goodhubviews.image_preview

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.alfiansyah.goodhubviews.core.utils.avoidEdgeToEdge
import com.alfiansyah.goodhubviews.core.utils.imageTarget
import com.alfiansyah.goodhubviews.core.utils.isPermissionDenied
import com.alfiansyah.goodhubviews.core.utils.isPermissionEnabled
import com.alfiansyah.goodhubviews.core.utils.loadAndShowImage
import com.alfiansyah.goodhubviews.core.utils.showPermissionRequiredDialog
import com.alfiansyah.goodhubviews.core.utils.startDownload
import com.alfiansyah.goodhubviews.databinding.ActivityPreviewImageBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.max
import kotlin.math.min

@AndroidEntryPoint
class PreviewImageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPreviewImageBinding
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private var scaleFactor = 1.0f
    private val requiredPermission =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreviewImageBinding.inflate(layoutInflater)

        val dataName = intent.getStringExtra(DATA_NAME) as String
        initUI(dataName)

        scaleGestureDetector = ScaleGestureDetector(this, ScaleListener())
        avoidEdgeToEdge(binding)
        setContentView(binding.root)
    }

    private fun initUI(path: String = "") {
        val previewImageTarget = imageTarget(this) {
            binding.ivPreview.setImageDrawable(it)
        }
        loadAndShowImage(
            this@PreviewImageActivity,
            path,
            previewImageTarget
        )

        binding.layoutBtnDownload.setOnClickListener {
            if (isPermissionEnabled(requiredPermission)) {
                startDownload(path)
            } else {
                if (isPermissionDenied(requiredPermission)) {
                    showPermissionRequiredDialog()
                } else {
                    requestPermissionLauncher.launch(requiredPermission)
                }
            }
        }

        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { permission ->
            if (permission) {
                binding.layoutBtnDownload.callOnClick()
            } else {
                showPermissionRequiredDialog()
            }
        }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        scaleGestureDetector.onTouchEvent(event)
        return true
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            scaleFactor *= scaleGestureDetector.scaleFactor
            scaleFactor = max(0.1f, min(scaleFactor, 10.0f))
            binding.ivPreview.scaleX = scaleFactor
            binding.ivPreview.scaleY = scaleFactor
            return true
        }
    }

    companion object {
        const val DATA_NAME = "dataName"
    }
}