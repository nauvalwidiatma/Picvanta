package com.gassticker

import android.content.Context
import android.graphics.Bitmap
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.framework.image.ByteBufferExtractor
import com.google.mediapipe.tasks.components.containers.NormalizedKeypoint
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.interactivesegmenter.InteractiveSegmenter
import com.google.mediapipe.tasks.vision.interactivesegmenter.InteractiveSegmenter.RegionOfInterest
import java.io.Closeable

data class SegmentMask(
    val bytes: ByteArray,
    val width: Int,
    val height: Int,
)

class InteractiveSegmenterClient(context: Context) : Closeable {
    private val segmenter: InteractiveSegmenter

    init {
        val baseOptions = BaseOptions.builder()
            .setModelAssetPath(MODEL_ASSET)
            .build()

        val options = InteractiveSegmenter.InteractiveSegmenterOptions.builder()
            .setBaseOptions(baseOptions)
            .setOutputCategoryMask(true)
            .setOutputConfidenceMasks(false)
            .build()

        segmenter = InteractiveSegmenter.createFromOptions(context, options)
    }

    fun segment(bitmap: Bitmap, normalizedX: Float, normalizedY: Float): SegmentMask {
        val roi = RegionOfInterest.create(
            NormalizedKeypoint.create(
                normalizedX.coerceIn(0f, 1f) * bitmap.width,
                normalizedY.coerceIn(0f, 1f) * bitmap.height,
            ),
        )
        val mpImage = BitmapImageBuilder(bitmap).build()
        val result = segmenter.segment(mpImage, roi)
        val categoryMask = result.categoryMask().orElseThrow {
            IllegalStateException("Segmentation mask tidak tersedia.")
        }
        val byteBuffer = ByteBufferExtractor.extract(categoryMask)
        val bytes = ByteArray(byteBuffer.capacity())
        byteBuffer.rewind()
        byteBuffer.get(bytes)
        return SegmentMask(
            bytes = bytes,
            width = categoryMask.width,
            height = categoryMask.height,
        )
    }

    override fun close() {
        segmenter.close()
    }

    companion object {
        private const val MODEL_ASSET = "interactive_segmentation_model.tflite"
    }
}
