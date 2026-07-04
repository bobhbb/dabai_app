package com.dabai.app.service.camera

import android.content.Context
import android.net.Uri
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.dabai.app.data.local.entity.PersonEntity
import com.dabai.app.service.face.FaceRecognitionService
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors
import kotlin.coroutines.resume

class CameraService(
    private val context: Context,
    private val faceRecognitionService: FaceRecognitionService
) {

    private var imageCapture: ImageCapture? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private val cameraExecutor = Executors.newSingleThreadExecutor()

    data class CaptureResult(
        val photoPath: String,
        val faceToken: String,
        val faceFeature: ByteArray,
        val isKnownPerson: Boolean,
        val matchedPerson: PersonEntity?
    )

    fun startCamera(
        previewView: PreviewView,
        lifecycleOwner: LifecycleOwner,
        onFaceDetected: (Boolean) -> Unit = {}
    ) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()
            bindCameraUseCases(previewView, lifecycleOwner)
        }, ContextCompat.getMainExecutor(context))
    }

    private fun bindCameraUseCases(previewView: PreviewView, lifecycleOwner: LifecycleOwner) {
        val provider = cameraProvider ?: return
        val rotation = previewView.display?.rotation ?: Surface.ROTATION_0

        val preview = Preview.Builder()
            .setTargetAspectRatio(AspectRatio.RATIO_4_3)
            .setTargetRotation(rotation)
            .build()
            .also { it.setSurfaceProvider(previewView.surfaceProvider) }

        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
            .setTargetAspectRatio(AspectRatio.RATIO_4_3)
            .setTargetRotation(rotation)
            .setFlashMode(ImageCapture.FLASH_MODE_AUTO)
            .build()

        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
            .build()

        provider.unbindAll()
        try {
            provider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, imageCapture)
        } catch (e: Exception) {
            android.util.Log.e("CameraService", "绑定相机失败", e)
        }
    }

    suspend fun takePhotoAndDetectFace(): CaptureResult = suspendCancellableCoroutine { continuation ->
        val capture = imageCapture ?: run {
            continuation.resumeWith(Result.failure(Exception("相机未初始化")))
            return@suspendCancellableCoroutine
        }

        val photoDir = File(context.filesDir, "photos")
        if (!photoDir.exists()) photoDir.mkdirs()

        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(Date())
        val photoFile = File(photoDir, "IMG_$timestamp.jpg")

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        capture.takePicture(
            outputOptions,
            cameraExecutor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = output.savedUri ?: Uri.fromFile(photoFile)
                    // 异步进行人脸检测
                    cameraExecutor.execute {
                        try {
                            val result = faceRecognitionService.analyzeFace(photoFile.absolutePath)
                            if (continuation.isActive) {
                                continuation.resume(
                                    CaptureResult(
                                        photoPath = photoFile.absolutePath,
                                        faceToken = result.faceToken,
                                        faceFeature = result.faceFeature,
                                        isKnownPerson = result.isKnown,
                                        matchedPerson = result.matchedPerson
                                    )
                                )
                            }
                        } catch (e: Exception) {
                            if (continuation.isActive) {
                                continuation.resumeWith(Result.failure(e))
                            }
                        }
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    if (continuation.isActive) {
                        continuation.resumeWith(Result.failure(exception))
                    }
                }
            }
        )
    }

    fun release() {
        cameraExecutor.shutdown()
        cameraProvider?.unbindAll()
    }
}
