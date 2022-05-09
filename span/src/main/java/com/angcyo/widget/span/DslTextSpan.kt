package com.angcyo.widget.span

import android.graphics.Canvas
import android.graphics.Paint
import android.text.Layout
import android.text.TextPaint
import android.text.style.LeadingMarginSpan
import android.text.style.MetricAffectingSpan
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.Px

/**
 * 系统文本Span样式集合体.
 *
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/01/11
 */
open class DslTextSpan : MetricAffectingSpan(), LeadingMarginSpan,
    LeadingMarginSpan.LeadingMarginSpan2, IWeightSpan, IClickableSpan {

    /**文本颜色*/
    @ColorInt
    var textColor: Int = undefined_color

    /**背景颜色*/
    @ColorInt
    var bgColor: Int = undefined_color

    /**字体大小*/
    @Px
    var textSize: Float = undefined_float

    /**字体缩放*/
    var relativeSizeScale: Float = undefined_float

    /**删除线*/
    var deleteLine: Boolean = false

    /**下划线*/
    var underline: Boolean = false

    /**粗体*/
    var textBold: Boolean = false

    /**斜体*/
    var textItalic: Boolean = false

    /**x轴缩放*/
    var scaleX: Float = undefined_float

    /**文本基线偏移, 可以实现[isSuperscript] [isSubscript] 上下标的效果*/
    var textBaselineShift: Int = 0

    /**上标*/
    var isSuperscript: Boolean = false

    /**下标*/
    var isSubscript: Boolean = false

    /**首行缩进*/
    var leadingFirst: Int = 0

    /**其他行缩进*/
    var leadingRest: Int = 0

    /**多少行算首行*/
    var leadingFirstLineCount: Int = 1

    /**需要[DslTextView]支持*/
    var leadingFirstWeight: Float = undefined_float
    var leadingRestWeight: Float = undefined_float

    /**单击事件回调, 需要[SpanClickMethod]支持*/
    var onClickSpan: ((view: View, span: DslTextSpan) -> Unit)? = null

    override fun updateDrawState(textPaint: TextPaint) {

        //
        if (textColor != undefined_color) {
            textPaint.color = textColor
        }
        if (bgColor != undefined_color) {
            textPaint.bgColor = bgColor
        }
        if (textSize != undefined_float) {
            textPaint.textSize = textSize
        }
        if (relativeSizeScale != undefined_float) {
            textPaint.textSize = relativeSizeScale * textPaint.textSize
        }

        //
        if (deleteLine) {
            textPaint.isStrikeThruText = true
        }
        if (underline) {
            textPaint.isUnderlineText = true
        }
        if (textBold) {
            textPaint.isFakeBoldText = textBold
        }
        if (textItalic) {
            textPaint.textSkewX = -0.25f
        }
        if (scaleX != undefined_float) {
            textPaint.textScaleX = textPaint.textScaleX * scaleX
        }

        //
        if (isSuperscript) {
            textPaint.baselineShift += (textPaint.ascent() / 2).toInt()
        }
        if (isSubscript) {
            textPaint.baselineShift -= (textPaint.ascent() / 2).toInt()
        }
        if (textBaselineShift != 0) {
            textPaint.baselineShift = textBaselineShift
        }
    }

    override fun updateMeasureState(textPaint: TextPaint) {
        if (textSize != undefined_float) {
            textPaint.textSize = textSize
        }
        if (relativeSizeScale != undefined_float) {
            textPaint.textSize = relativeSizeScale * textPaint.textSize
        }

        if (textBold) {
            textPaint.isFakeBoldText = textBold
        }
        if (textItalic) {
            textPaint.textSkewX = -0.25f
        }
        if (scaleX != undefined_float) {
            textPaint.textScaleX = textPaint.textScaleX * scaleX
        }

        if (isSuperscript) {
            textPaint.baselineShift += (textPaint.ascent() / 2).toInt()
        }
        if (isSubscript) {
            textPaint.baselineShift -= (textPaint.ascent() / 2).toInt()
        }
        if (textBaselineShift != 0) {
            textPaint.baselineShift = textBaselineShift
        }
    }

    override fun getLeadingMarginLineCount(): Int {
        return leadingFirstLineCount
    }

    override fun drawLeadingMargin(
        c: Canvas?,
        p: Paint?,
        x: Int,
        dir: Int,
        top: Int,
        baseline: Int,
        bottom: Int,
        text: CharSequence?,
        start: Int,
        end: Int,
        first: Boolean,
        layout: Layout?
    ) {
        //no op
    }

    override fun getLeadingMargin(first: Boolean): Int {
        return if (first) leadingFirst else leadingRest
    }

    override fun onMeasure(widthSize: Int, heightSize: Int) {
        if (leadingFirstWeight != undefined_float) {
            leadingFirst = (widthSize * leadingFirstWeight).toInt()
        }
        if (leadingRestWeight != undefined_float) {
            leadingRest = (widthSize * leadingRestWeight).toInt()
        }
    }

    fun leading(size: Int) {
        leadingFirst = size
        leadingRest = size
    }

    fun leadingWeight(weight: Float) {
        leadingFirstWeight = weight
        leadingRestWeight = weight
    }

    override fun isCanClick(): Boolean {
        return onClickSpan != null
    }

    override fun onClickSpan(view: View, span: IClickableSpan) {
        onClickSpan?.run { this(view, this@DslTextSpan) } ?: super.onClickSpan(view, span)
    }

    override fun onTouchEvent(view: View, span: IClickableSpan, event: MotionEvent) {
        super.onTouchEvent(view, span, event)
    }

}