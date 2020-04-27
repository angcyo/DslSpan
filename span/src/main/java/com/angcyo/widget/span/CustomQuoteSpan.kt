package com.angcyo.widget.span

import android.graphics.Canvas
import android.graphics.Paint
import android.text.Layout
import android.text.style.LeadingMarginSpan
import androidx.annotation.ColorInt
import androidx.annotation.Px

/**
 * 支持宽度, 偏移的. 系统的需要API 28才支持
 * [android.text.style.QuoteSpan]
 * Email:angcyo@126.com
 *
 * @author angcyo
 * @date 2020/01/08
 */
class CustomQuoteSpan(
    @ColorInt val color: Int,
    @Px val stripeWidth: Int,
    @Px val gapLeftWidth: Int,
    @Px val gapRightWidth: Int
) : LeadingMarginSpan {

    override fun getLeadingMargin(first: Boolean): Int {
        return stripeWidth + gapLeftWidth + gapRightWidth
    }

    override fun drawLeadingMargin(
        c: Canvas, p: Paint, x: Int, dir: Int,
        top: Int, baseline: Int, bottom: Int,
        text: CharSequence, start: Int, end: Int,
        first: Boolean, layout: Layout
    ) {
        val style = p.style
        val color = p.color
        p.style = Paint.Style.FILL
        p.color = this.color
        c.drawRect(
            (x + gapLeftWidth).toFloat(),
            top.toFloat(),
            x + gapLeftWidth + dir * stripeWidth.toFloat(),
            bottom.toFloat(),
            p
        )
        p.style = style
        p.color = color
    }
}