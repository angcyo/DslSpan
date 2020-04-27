package com.angcyo.widget.span

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.FontMetricsInt
import android.text.style.ReplacementSpan
import androidx.annotation.IntRange

/**
 * 色块, 空隙
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/01/08
 */
class SpaceSpan(
    val width: Int,
    val color: Int = Color.TRANSPARENT
) : ReplacementSpan() {
    override fun getSize(
        paint: Paint, text: CharSequence,
        @IntRange(from = 0) start: Int,
        @IntRange(from = 0) end: Int,
        fm: FontMetricsInt?
    ): Int {
        return width
    }

    override fun draw(
        canvas: Canvas, text: CharSequence,
        @IntRange(from = 0) start: Int,
        @IntRange(from = 0) end: Int,
        x: Float, top: Int, y: Int, bottom: Int,
        paint: Paint
    ) {
        if (color != Color.TRANSPARENT) {
            val style = paint.style
            val color = paint.color
            paint.style = Paint.Style.FILL
            paint.color = this.color
            canvas.drawRect(x, top.toFloat(), x + width, bottom.toFloat(), paint)
            paint.style = style
            paint.color = color
        }
    }

}