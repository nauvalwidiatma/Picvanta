package com.gassticker

import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

enum class BrushMode {
    None,
    Add,
    Subtract,
}

object SelectionEditor {
    fun copy(mask: SegmentMask): SegmentMask =
        mask.copy(bytes = mask.bytes.copyOf())

    fun applyBrush(
        mask: SegmentMask,
        normalizedX: Float,
        normalizedY: Float,
        mode: BrushMode,
        radiusFraction: Float,
    ): SegmentMask {
        if (mode != BrushMode.Add && mode != BrushMode.Subtract) return mask

        val bytes = mask.bytes.copyOf()
        applyBrushCircle(bytes, mask, normalizedX, normalizedY, mode, radiusFraction)
        return mask.copy(bytes = bytes)
    }

    fun applyBrushLine(
        mask: SegmentMask,
        fromX: Float,
        fromY: Float,
        toX: Float,
        toY: Float,
        mode: BrushMode,
        radiusFraction: Float,
    ): SegmentMask {
        if (mode != BrushMode.Add && mode != BrushMode.Subtract) return mask

        val bytes = mask.bytes.copyOf()
        val radius = brushRadius(mask, radiusFraction)
        val dx = (toX - fromX) * mask.width
        val dy = (toY - fromY) * mask.height
        val distance = sqrt(dx * dx + dy * dy)
        val steps = max(1, (distance / max(1f, radius * 0.42f)).toInt())

        for (i in 0..steps) {
            val t = i / steps.toFloat()
            applyBrushCircle(
                bytes = bytes,
                mask = mask,
                normalizedX = fromX + (toX - fromX) * t,
                normalizedY = fromY + (toY - fromY) * t,
                mode = mode,
                radiusFraction = radiusFraction,
            )
        }

        return mask.copy(bytes = bytes)
    }

    private fun applyBrushCircle(
        bytes: ByteArray,
        mask: SegmentMask,
        normalizedX: Float,
        normalizedY: Float,
        mode: BrushMode,
        radiusFraction: Float,
    ) {
        val centerX = (normalizedX.coerceIn(0f, 1f) * (mask.width - 1)).toInt()
        val centerY = (normalizedY.coerceIn(0f, 1f) * (mask.height - 1)).toInt()
        val radius = brushRadius(mask, radiusFraction)
        val radiusSquared = radius * radius
        val value = if (mode == BrushMode.Add) 0.toByte() else 1.toByte()

        val left = max(0, centerX - radius)
        val right = min(mask.width - 1, centerX + radius)
        val top = max(0, centerY - radius)
        val bottom = min(mask.height - 1, centerY + radius)

        for (y in top..bottom) {
            val dy = y - centerY
            for (x in left..right) {
                val dx = x - centerX
                if (dx * dx + dy * dy <= radiusSquared) {
                    bytes[y * mask.width + x] = value
                }
            }
        }
    }

    private fun brushRadius(mask: SegmentMask, radiusFraction: Float): Int {
        val shortestSide = min(mask.width, mask.height)
        return (shortestSide * radiusFraction.coerceIn(0.015f, 0.12f)).toInt().coerceAtLeast(3)
    }
}
