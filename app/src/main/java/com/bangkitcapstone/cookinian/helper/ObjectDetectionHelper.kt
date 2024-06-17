package com.bangkitcapstone.cookinian.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.SystemClock
import android.provider.MediaStore
import android.util.Log
import com.bangkitcapstone.cookinian.R
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.CastOp
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.detector.Detection
import org.tensorflow.lite.task.vision.detector.ObjectDetector

class ObjectDetectionHelper(
    private var threshold: Float = 0.4f,
    private var maxResults: Int = 10,
    private val modelName: String = "object_detection.tflite",
    val context: Context,
    val detectionListener: DetectionListener?
) {

    private var objectDetector: ObjectDetector? = null

    init {
        setupObjectDetector()
    }

    private fun setupObjectDetector() {
        val optionsBuilder = ObjectDetector.ObjectDetectorOptions.builder()
            .setScoreThreshold(threshold)
            .setMaxResults(maxResults)
        val baseOptionsBuilder = BaseOptions.builder()
            .setNumThreads(4)
        optionsBuilder.setBaseOptions(baseOptionsBuilder.build())

        try {
            objectDetector = ObjectDetector.createFromFileAndOptions(
                context,
                modelName,
                optionsBuilder.build()
            )
        } catch (e: IllegalStateException) {
            detectionListener?.onError(context.getString(R.string.object_detection_failed))
            Log.e(TAG, e.message.toString())
        }
    }

    fun detectObjects(imageUri: Uri) {
        if (objectDetector == null) {
            setupObjectDetector()
        }

        val originalBitmap = toBitmap(imageUri)
        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(300, 300, ResizeOp.ResizeMethod.BILINEAR))
            .add(NormalizeOp(-1f, 1f))
            .add(CastOp(DataType.FLOAT32))
            .build()

        val tensorImage = TensorImage(DataType.FLOAT32)
        tensorImage.load(originalBitmap)
        val processedImage = imageProcessor.process(tensorImage)

        var inferenceTime = SystemClock.uptimeMillis()
        val results = objectDetector?.detect(processedImage)
        inferenceTime = SystemClock.uptimeMillis() - inferenceTime

        results?.forEach { detection ->
            detection.boundingBox.apply {
                left *= originalBitmap.width / 300f
                top *= originalBitmap.height / 300f
                right *= originalBitmap.width / 300f
                bottom *= originalBitmap.height / 300f
            }
        }
        detectionListener?.onResults(results, inferenceTime)
    }


    private fun toBitmap(imageUri: Uri): Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, imageUri)
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
        }.copy(Bitmap.Config.ARGB_8888, true)
    }

    interface DetectionListener {
        fun onError(error: String)
        fun onResults(
            results: List<Detection>?,
            inferenceTime: Long
        )
    }

    companion object {
        private const val TAG = "ObjectDetectionHelper"
    }
}