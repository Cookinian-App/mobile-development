package com.bangkitcapstone.cookinian.helper

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.bangkitcapstone.cookinian.R
import org.tensorflow.lite.task.vision.detector.Detection
import java.text.NumberFormat

class OverlayView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private val boundingBoxPaint = Paint()
    private var textBackgroundPaint = Paint()
    private val textPaint = Paint()
    private var bounds = Rect()

    private var detections: List<Detection>? = null
    private var imageView: ImageView? = null

    init {
        boundingBoxPaint.style = Paint.Style.STROKE
        boundingBoxPaint.color = ContextCompat.getColor(context, R.color.green)
        boundingBoxPaint.strokeWidth = 4f

        textBackgroundPaint.color = Color.BLACK
        textBackgroundPaint.style = Paint.Style.FILL
        textBackgroundPaint.textSize = 30f

        textPaint.color = Color.WHITE
        textPaint.textSize = 30f
        textPaint.typeface = ResourcesCompat.getFont(context, R.font.satoshi_bold)
    }

    fun setDetections(detections: List<Detection>?, imageView: ImageView) {
        this.detections = detections
        this.imageView = imageView
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        detections?.let { detections ->
            imageView?.let { imageView ->
                val drawable = imageView.drawable ?: return
                val imageBitmapWidth = drawable.intrinsicWidth
                val imageBitmapHeight = drawable.intrinsicHeight

                val scaleX = imageView.width.toFloat() / imageBitmapWidth
                val scaleY = imageView.height.toFloat() / imageBitmapHeight

                detections.forEach { detection ->
                    val boundingBox = detection.boundingBox
                    val left = boundingBox.left * scaleX
                    val top = boundingBox.top * scaleY
                    val right = boundingBox.right * scaleX
                    val bottom = boundingBox.bottom * scaleY

                    val drawableRect = RectF(left, top, right, bottom)
                    canvas.drawRect(drawableRect, boundingBoxPaint)

                    val drawableText = "${detection.categories[0].label} " +
                            NumberFormat.getPercentInstance().format(detection.categories[0].score)

                    textBackgroundPaint.getTextBounds(drawableText, 0, drawableText.length, bounds)
                    val textWidth = bounds.width()
                    val textHeight = bounds.height()
                    canvas.drawRect(
                        left,
                        top,
                        left + textWidth + Companion.BOUNDING_RECT_TEXT_PADDING,
                        top + textHeight + Companion.BOUNDING_RECT_TEXT_PADDING,
                        textBackgroundPaint
                    )

                    canvas.drawText(drawableText, left, top + bounds.height(), textPaint)
                }
            }
        }
    }

    companion object {
        private const val BOUNDING_RECT_TEXT_PADDING = 8
    }
}
