package com.dabai.app.service.face

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.dabai.app.data.local.entity.PersonEntity
import com.dabai.app.data.local.dao.FaceDataDao
import com.dabai.app.data.local.dao.PersonDao
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.MessageDigest
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

data class FaceAnalysisResult(
    val faceToken: String,
    val faceFeature: ByteArray,
    val isKnown: Boolean = false,
    val matchedPerson: PersonEntity? = null,
    val confidence: Float = 0f,
    val landmarksJson: String = "{}"
)

@Singleton
class FaceRecognitionService @Inject constructor(
    @ApplicationContext private val context: Context,
    private val faceDataDao: FaceDataDao,
    private val personDao: PersonDao
) {

    private val faceDetector by lazy {
        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
            .setMinFaceSize(0.15f)
            .build()
        FaceDetection.getClient(options)
    }

    /**
     * 分析照片中的人脸
     * 1. 检测人脸区域
     * 2. 提取面部特征（通过局部像素哈希生成faceToken）
     * 3. 与已有数据比对，判断是否已知人物
     */
    suspend fun analyzeFace(imagePath: String): FaceAnalysisResult = withContext(Dispatchers.IO) {
        val bitmap = BitmapFactory.decodeFile(imagePath)
            ?: throw Exception("无法加载图片: $imagePath")

        val image = InputImage.fromFilePath(context, android.net.Uri.fromFile(java.io.File(imagePath)))

        // ML Kit 人脸检测
        val faces = faceDetector.process(image)
            .addOnSuccessListener { result ->
                android.util.Log.d("FaceRecognition", "检测到 ${result.size} 张人脸")
            }
            .await()

        if (faces.isEmpty()) {
            throw Exception("未检测到人脸，请重新拍照")
        }

        val face = faces.first()

        // 提取人脸区域的像素特征用于生成唯一标识
        val faceToken = generateFaceToken(bitmap, face)

        // 生成面部特征向量（简化版：提取人脸区域像素特征）
        val faceFeature = extractFaceFeature(bitmap, face)

        // 与已有数据比对
        val existingPerson = personDao.getPersonByFaceToken(faceToken)

        val isKnown = existingPerson != null

        FaceAnalysisResult(
            faceToken = faceToken,
            faceFeature = faceFeature,
            isKnown = isKnown,
            matchedPerson = existingPerson,
            confidence = if (isKnown) 0.95f else 0f
        )
    }

    /**
     * 生成人脸唯一Token
     * 结合人脸区域的关键像素特征和landmark位置做哈希
     */
    private fun generateFaceToken(bitmap: Bitmap, face: com.google.mlkit.vision.face.Face): String {
        val boundingBox = face.boundingBox
        val x = maxOf(0, boundingBox.left)
        val y = maxOf(0, boundingBox.top)
        val w = minOf(boundingBox.width(), bitmap.width - x)
        val h = minOf(boundingBox.height(), bitmap.height - y)

        // 采样人脸区域的像素用于生成哈希
        val sampleSize = 32
        val sampledPixels = mutableListOf<Int>()
        for (i in 0 until sampleSize) {
            for (j in 0 until sampleSize) {
                val px = x + (w * i / sampleSize)
                val py = y + (h * j / sampleSize)
                if (px in 0 until bitmap.width && py in 0 until bitmap.height) {
                    sampledPixels.add(bitmap.getPixel(px, py))
                }
            }
        }

        val digest = MessageDigest.getInstance("SHA-256")
        sampledPixels.forEach { pixel ->
            digest.update((pixel shr 24).toByte())
            digest.update((pixel shr 16).toByte())
            digest.update((pixel shr 8).toByte())
            digest.update(pixel.toByte())
        }

        // 加入面部特征点位置增强唯一性
        face.landmarks.forEach { landmark ->
            digest.update(landmark.position.x.toByte())
            digest.update(landmark.position.y.toByte())
        }

        return digest.digest().joinToString("") { "%02x".format(it) }
    }

    /**
     * 提取人脸特征向量（简化为像素灰度值序列）
     * 生产环境中应使用 FaceNet / ArcFace 等深度学习模型
     */
    private fun extractFaceFeature(bitmap: Bitmap, face: com.google.mlkit.vision.face.Face): ByteArray {
        val bb = face.boundingBox
        val x = maxOf(0, bb.left)
        val y = maxOf(0, bb.top)
        val w = minOf(bb.width(), bitmap.width - x)
        val h = minOf(bb.height(), bitmap.height - y)

        val featureSize = 64
        val features = ByteArray(featureSize * featureSize)
        val faceBitmap = Bitmap.createScaledBitmap(
            Bitmap.createBitmap(bitmap, x, y, w, h),
            featureSize, featureSize, true
        )

        for (i in 0 until featureSize) {
            for (j in 0 until featureSize) {
                val pixel = faceBitmap.getPixel(j, i)
                val gray = ((pixel shr 16 and 0xFF) * 0.299f +
                        (pixel shr 8 and 0xFF) * 0.587f +
                        (pixel and 0xFF) * 0.114f).toInt()
                features[i * featureSize + j] = (gray - 128).toByte()
            }
        }

        return features
    }

    fun release() {
        faceDetector.close()
    }
}

/** 简化版的 await 扩展, 实际项目中建议用 Tasks.await() 或回调转协程 */
private suspend fun <T> com.google.android.gms.tasks.Task<T>.await(): T {
    return kotlinx.coroutines.suspendCancellableCoroutine { continuation ->
        addOnSuccessListener { result ->
            if (continuation.isActive) continuation.resumeWith(Result.success(result))
        }
        addOnFailureListener { e ->
            if (continuation.isActive) continuation.resumeWith(Result.failure(e))
        }
    }
}

private fun Int.toByte(): Byte = (this and 0xFF).toByte()
