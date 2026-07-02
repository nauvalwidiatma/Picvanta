package com.gassticker

object SelectionBoundary {
    fun from(mask: SegmentMask): IntArray {
        val points = ArrayList<Int>()
        for (y in 0 until mask.height) {
            for (x in 0 until mask.width) {
                if (isSelected(mask, x, y) && hasOutsideNeighbor(mask, x, y)) {
                    points += (y shl 16) or x
                }
            }
        }
        return points.toIntArray()
    }

    private fun hasOutsideNeighbor(mask: SegmentMask, x: Int, y: Int): Boolean =
        !isSelected(mask, x - 1, y) ||
            !isSelected(mask, x + 1, y) ||
            !isSelected(mask, x, y - 1) ||
            !isSelected(mask, x, y + 1)

    private fun isSelected(mask: SegmentMask, x: Int, y: Int): Boolean {
        if (x !in 0 until mask.width || y !in 0 until mask.height) return false
        return (mask.bytes[y * mask.width + x].toInt() and 0xFF) == 0
    }
}
