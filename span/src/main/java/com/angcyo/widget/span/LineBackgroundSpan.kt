package com.angcyo.widget.span

import android.graphics.Canvas
import android.graphics.Paint
import android.text.style.LineBackgroundSpan
import androidx.annotation.ColorInt
import androidx.annotation.Px

/**
 * 一行的背景
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/01/08
 */
class LineBackgroundSpan(@ColorInt var color: Int) : LineBackgroundSpan {
    override fun drawBackground(
        canvas: Canvas, paint: Paint,
        @Px left: Int, @Px right: Int,
        @Px top: Int, @Px baseline: Int, @Px bottom: Int,
        text: CharSequence, start: Int, end: Int,
        lineNumber: Int
    ) {
        val originColor = paint.color
        paint.color = color
        canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), paint)
        paint.color = originColor
    }

}