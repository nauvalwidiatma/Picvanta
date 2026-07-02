package com.gassticker

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.BlurMaskFilter
import android.graphics.LinearGradient
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface
import kotlin.math.max
import kotlin.math.min

data class StickerStyle(
    val label: String,
    val outlineColor: Int,
    val fontColor: Int = Color.WHITE,
    val textStyleColor: Int = Color.BLACK,
    val typeface: Typeface = Typeface.DEFAULT_BOLD,
    val textTreatment: StickerTextTreatment = StickerTextTreatment.Stroke,
    val labelOffsetX: Float = 0f,
    val labelOffsetY: Float = 0f,
    val outlineRadiusPx: Int = 12,
)

enum class StickerTextTreatment {
    Stroke,
    Shadow,
    Bubble,
    Pop,
    Neon,
    Candy,
    Comic,
    Gold,
    Stamp,
}

object StickerComposer {
    private const val STICKER_SIZE = 512
    private const val HORIZONTAL_PADDING = 36f
    private const val TOP_PADDING = 24f
    private const val LABEL_TOP = 386f

    fun createSticker(
        source: Bitmap,
        mask: SegmentMask,
        style: StickerStyle,
    ): Bitmap {
        val cutout = createCutout(source, mask)
        val bounds = findOpaqueBounds(cutout) ?: Rect(0, 0, cutout.width, cutout.height)
        val cropped = Bitmap.createBitmap(cutout, bounds.left, bounds.top, bounds.width(), bounds.height())
        val sticker = Bitmap.createBitmap(STICKER_SIZE, STICKER_SIZE, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(sticker)
        val objectLayer = Bitmap.createBitmap(STICKER_SIZE, STICKER_SIZE, Bitmap.Config.ARGB_8888)
        val objectCanvas = Canvas(objectLayer)

        val maxObjectWidth = STICKER_SIZE - HORIZONTAL_PADDING * 2
        val maxObjectHeight = if (style.label.isBlank()) 448f else 346f
        val scale = min(maxObjectWidth / cropped.width, maxObjectHeight / cropped.height)
        val drawWidth = cropped.width * scale
        val drawHeight = cropped.height * scale
        val left = (STICKER_SIZE - drawWidth) / 2f
        val top = TOP_PADDING + (maxObjectHeight - drawHeight) / 2f
        val target = RectF(left, top, left + drawWidth, top + drawHeight)

        val imagePaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
        objectCanvas.drawBitmap(cropped, null, target, imagePaint)

        val alpha = objectLayer.extractAlpha()
        val outlinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = style.outlineColor
            isFilterBitmap = true
        }
        drawOuterStickerOutline(canvas, alpha, style.outlineRadiusPx.toFloat(), outlinePaint)
        canvas.drawBitmap(objectLayer, 0f, 0f, null)
        drawLabel(
            canvas = canvas,
            rawLabel = style.label.trim(),
            typeface = style.typeface,
            treatment = style.textTreatment,
            fontColor = style.fontColor,
            styleColor = style.textStyleColor,
            offsetX = style.labelOffsetX,
            offsetY = style.labelOffsetY,
        )
        return sticker
    }

    private fun createCutout(source: Bitmap, mask: SegmentMask): Bitmap {
        val output = Bitmap.createBitmap(source.width, source.height, Bitmap.Config.ARGB_8888)
        val sourcePixels = IntArray(source.width * source.height)
        source.getPixels(sourcePixels, 0, source.width, 0, 0, source.width, source.height)
        val outputPixels = IntArray(sourcePixels.size)

        for (y in 0 until source.height) {
            val maskY = (y * mask.height / source.height).coerceIn(0, mask.height - 1)
            for (x in 0 until source.width) {
                val maskX = (x * mask.width / source.width).coerceIn(0, mask.width - 1)
                val maskIndex = maskY * mask.width + maskX
                val segmentId = mask.bytes[maskIndex].toInt() and 0xFF
                val pixelIndex = y * source.width + x
                outputPixels[pixelIndex] = if (segmentId == 0) {
                    sourcePixels[pixelIndex]
                } else {
                    Color.TRANSPARENT
                }
            }
        }

        output.setPixels(outputPixels, 0, source.width, 0, 0, source.width, source.height)
        return output
    }

    private fun findOpaqueBounds(bitmap: Bitmap): Rect? {
        var minX = bitmap.width
        var minY = bitmap.height
        var maxX = -1
        var maxY = -1
        val row = IntArray(bitmap.width)

        for (y in 0 until bitmap.height) {
            bitmap.getPixels(row, 0, bitmap.width, 0, y, bitmap.width, 1)
            for (x in row.indices) {
                if (Color.alpha(row[x]) > 0) {
                    minX = min(minX, x)
                    minY = min(minY, y)
                    maxX = max(maxX, x)
                    maxY = max(maxY, y)
                }
            }
        }

        if (maxX < minX || maxY < minY) return null
        return Rect(minX, minY, maxX + 1, maxY + 1)
    }

    private fun drawOuterStickerOutline(
        canvas: Canvas,
        alpha: Bitmap,
        radius: Float,
        paint: Paint,
    ) {
        val outlineLayer = Bitmap.createBitmap(STICKER_SIZE, STICKER_SIZE, Bitmap.Config.ARGB_8888)
        val outlineCanvas = Canvas(outlineLayer)
        val steps = 32
        for (i in 0 until steps) {
            val angle = Math.PI * 2.0 * i / steps
            val dx = kotlin.math.cos(angle).toFloat() * radius
            val dy = kotlin.math.sin(angle).toFloat() * radius
            outlineCanvas.drawBitmap(alpha, dx, dy, paint)
        }

        val clearPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        }
        outlineCanvas.drawBitmap(alpha, 0f, 0f, clearPaint)
        clearPaint.xfermode = null
        canvas.drawBitmap(outlineLayer, 0f, 0f, null)
    }

    private fun drawLabel(
        canvas: Canvas,
        rawLabel: String,
        typeface: Typeface,
        treatment: StickerTextTreatment,
        fontColor: Int,
        styleColor: Int,
        offsetX: Float,
        offsetY: Float,
    ) {
        if (rawLabel.isBlank()) return
        val label = rawLabel.take(18)
        val centerX = STICKER_SIZE / 2f + offsetX
        var textSize = 58f
        val fill = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = fontColor
            textAlign = Paint.Align.CENTER
            this.typeface = typeface
        }
        val stroke = Paint(fill).apply {
            color = styleColor
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeWidth = 12f
        }

        while (textSize > 28f) {
            fill.textSize = textSize
            stroke.textSize = textSize
            if (fill.measureText(label) <= STICKER_SIZE - HORIZONTAL_PADDING * 2) break
            textSize -= 2f
        }

        val y = LABEL_TOP + textSize + offsetY
        when (treatment) {
            StickerTextTreatment.Stroke -> {
                canvas.drawText(label, centerX, y, stroke)
                canvas.drawText(label, centerX, y, fill)
            }

            StickerTextTreatment.Shadow -> {
                val shadow = Paint(fill).apply {
                    color = withAlpha(styleColor, 190)
                }
                canvas.drawText(label, centerX + 6f, y + 8f, shadow)
                canvas.drawText(label, centerX, y, fill)
            }

            StickerTextTreatment.Bubble -> {
                val textWidth = fill.measureText(label)
                val metrics = fill.fontMetrics
                val bubble = RectF(
                    centerX - textWidth / 2f - 26f,
                    y + metrics.ascent - 16f,
                    centerX + textWidth / 2f + 26f,
                    y + metrics.descent + 16f,
                )
                val bubblePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = styleColor
                }
                val bubbleStroke = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = fontColor
                    style = Paint.Style.STROKE
                    strokeWidth = 8f
                }
                canvas.drawRoundRect(bubble, 28f, 28f, bubbleStroke)
                canvas.drawRoundRect(bubble, 28f, 28f, bubblePaint)
                canvas.drawText(label, centerX, y, fill)
            }

            StickerTextTreatment.Pop -> {
                val popFill = Paint(fill).apply {
                    color = fontColor
                }
                val popStroke = Paint(stroke).apply {
                    color = styleColor
                    strokeWidth = 14f
                }
                canvas.drawText(label, centerX + 5f, y + 5f, popStroke)
                canvas.drawText(label, centerX, y, popStroke)
                canvas.drawText(label, centerX, y, popFill)
            }

            StickerTextTreatment.Neon -> {
                val glow = Paint(fill).apply {
                    color = styleColor
                    maskFilter = BlurMaskFilter(12f, BlurMaskFilter.Blur.NORMAL)
                }
                val neonFill = Paint(fill).apply {
                    color = fontColor
                }
                val neonStroke = Paint(stroke).apply {
                    color = styleColor
                    strokeWidth = 8f
                }
                canvas.drawText(label, centerX, y, glow)
                canvas.drawText(label, centerX, y, neonStroke)
                canvas.drawText(label, centerX, y, neonFill)
            }

            StickerTextTreatment.Candy -> {
                val candyFill = Paint(fill).apply {
                    color = fontColor
                }
                val candyStroke = Paint(stroke).apply {
                    color = styleColor
                    strokeWidth = 14f
                }
                val candyShadow = Paint(fill).apply {
                    color = withAlpha(styleColor, 205)
                }
                canvas.drawText(label, centerX + 5f, y + 7f, candyShadow)
                canvas.drawText(label, centerX, y, candyStroke)
                canvas.drawText(label, centerX, y, candyFill)
            }

            StickerTextTreatment.Comic -> {
                val backLayer = Paint(fill).apply {
                    color = withAlpha(styleColor, 150)
                }
                val comicFill = Paint(fill).apply {
                    color = fontColor
                }
                val comicStroke = Paint(stroke).apply {
                    color = styleColor
                    strokeWidth = 10f
                }
                canvas.drawText(label, centerX + 8f, y + 8f, backLayer)
                canvas.drawText(label, centerX, y, comicStroke)
                canvas.drawText(label, centerX, y, comicFill)
            }

            StickerTextTreatment.Gold -> {
                val goldFill = Paint(fill).apply {
                    shader = LinearGradient(
                        0f,
                        y - textSize,
                        0f,
                        y + 8f,
                        fontColor,
                        styleColor,
                        Shader.TileMode.CLAMP,
                    )
                }
                val goldStroke = Paint(stroke).apply {
                    color = styleColor
                    strokeWidth = 10f
                }
                canvas.drawText(label, centerX + 4f, y + 5f, goldStroke)
                canvas.drawText(label, centerX, y, goldStroke)
                canvas.drawText(label, centerX, y, goldFill)
            }

            StickerTextTreatment.Stamp -> {
                canvas.save()
                canvas.rotate(-5f, centerX, y - textSize / 2f)
                val stampStroke = Paint(stroke).apply {
                    color = styleColor
                    strokeWidth = 7f
                }
                val stampFill = Paint(fill).apply {
                    color = withAlpha(fontColor, 90)
                }
                canvas.drawText(label, centerX, y, stampStroke)
                canvas.drawText(label, centerX, y, stampFill)
                canvas.restore()
            }
        }
    }

    private fun withAlpha(color: Int, alpha: Int): Int =
        Color.argb(
            alpha.coerceIn(0, 255),
            Color.red(color),
            Color.green(color),
            Color.blue(color),
        )
}
