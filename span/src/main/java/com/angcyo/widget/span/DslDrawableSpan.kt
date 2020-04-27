package com.angcyo.widget.span

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.text.style.ReplacementSpan
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.Px
import androidx.core.view.GravityCompat
import kotlin.math.min

/**
 * 强大的自绘文本, Drawable的span. 超多属性支持.
 *
 * 支持 weight 属性, 需要[DslSpanTextView]的支持.
 *
 * 不支持span内自动换行
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/01/08
 */

open class DslDrawableSpan : ReplacementSpan(), IWeightSpan, IClickableSpan, IDrawableSpan {

    val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)

    @Px
    var textSize: Float = -1f

    @ColorInt
    var textColor: Int = undefined_color

    var textGravity: Int = Gravity.LEFT or Gravity.BOTTOM

    /**需要替换显示的文本*/
    var showText: CharSequence? = null

    /**强制指定宽度*/
    var spanWidth: Int = undefined_int
    var spanHeight: Int = undefined_int
    var spanMaxWidth: Int = undefined_int

    /**span相对于[TextView]的比例, 不支持平分. 需要[DslSpanTextView]支持*/
    var spanWeight: Float = undefined_float
    var spanMaxWeight: Float = undefined_float

    /**宽度等于高度*/
    var widthSameHeight = false

    /**填充背景[Drawable], 在最下面绘制*/
    var fillBackgroundDrawable: Drawable? = null
        set(value) {
            field = value
            field?.apply {
                if (bounds.isEmpty) {
                    setBounds(0, 0, minimumWidth, minimumHeight)
                }
            }
        }

    /**背景[Drawable], 在文本下面绘制*/
    var backgroundDrawable: Drawable? = null
        set(value) {
            field = value
            field?.apply {
                if (bounds.isEmpty) {
                    setBounds(0, 0, minimumWidth, minimumHeight)
                }
            }
        }

    /**前景[Drawable], 在文本上面绘制*/
    var foregroundDrawable: Drawable? = null
        set(value) {
            field = value
            field?.apply {
                if (bounds.isEmpty) {
                    setBounds(0, 0, minimumWidth, minimumHeight)
                }
            }
        }

    /**填充的颜色*/
    var gradientSolidColor = undefined_color

    /**边框的颜色*/
    var gradientStrokeColor = undefined_color

    /**边框的宽度*/
    var gradientStrokeWidth = 1 * dp

    /**圆角大小*/
    var gradientRadius = 25 * dp

    //影响宽度, 背景偏移, 文本偏移
    var marginLeft: Int = 0
    var marginRight: Int = 0
    var marginTop: Int = 0
    var marginBottom: Int = 0

    //影响宽度, 影响文本与背景的距离
    var paddingLeft: Int = 0
    var paddingRight: Int = 0
    var paddingTop: Int = 0
    var paddingBottom: Int = 0

    //整体偏移
    var offsetX: Float = 0f
    var offsetY: Float = 0f

    //单独文本偏移
    var textOffsetX: Float = 0f
    var textOffsetY: Float = 0f

    val _gradientRectF = RectF()

    /**单击事件回调, 需要[SpanClickMethod]支持*/
    var spanClickAction: ((view: View, span: DslDrawableSpan) -> Unit)? = null

    fun _initPaint(paint: Paint) {
        textPaint.set(paint)
        if (textSize > 0) {
            textPaint.textSize = textSize
        }
        if (textColor != undefined_color) {
            textPaint.color = textColor
        }
    }

    fun _targetText(
        text: CharSequence?,
        start: Int,
        end: Int
    ): CharSequence {
        return showText?.run { this } ?: text?.subSequence(start, end) ?: ""
    }

    fun _drawableWidth(drawable: Drawable?): Int {
        return drawable?.run { if (bounds.isEmpty) if (bounds.left == -1) -1 else minimumWidth else bounds.width() }
            ?: 0
    }

    fun _drawableHeight(drawable: Drawable?): Int {
        return drawable?.run { if (bounds.isEmpty) if (bounds.top == -1) -1 else minimumHeight else bounds.height() }
            ?: 0
    }

    /**高度包含 marigin padding , 宽度不包含*/
    fun _measureSize(
        paint: Paint,
        text: CharSequence?,
        start: Int,
        end: Int,
        fm: Paint.FontMetricsInt? = null
    ): IntArray {
        _initPaint(paint)

        val targetText = _targetText(text, start, end)

        val textWidth = textPaint.measureText(targetText, 0, targetText.length).toInt()

        val bgWidth = _drawableWidth(backgroundDrawable)
        val fgWidth = _drawableWidth(foregroundDrawable)

        val bgHeight = _drawableHeight(backgroundDrawable)
        val fgHeight = _drawableHeight(foregroundDrawable)

        val height: Int

        if (fm != null) {
            fm.ascent =
                if (spanHeight > 0) -spanHeight else minOf(
                    textPaint.ascent().toInt(),
                    -bgHeight,
                    -fgHeight
                ) - paddingTop - paddingBottom - marginTop - marginBottom
            fm.descent = textPaint.descent().toInt()

            //决定高度
            fm.top = fm.ascent
            //基线下距离
            fm.bottom = fm.descent

            height = fm.descent - fm.ascent
        } else {
            height = if (spanHeight > 0) spanHeight else maxOf(
                (textPaint.descent() - textPaint.ascent()).toInt(),
                bgHeight,
                fgHeight
            ) + paddingTop + paddingBottom + marginTop + marginBottom
        }

        var width = when {
            widthSameHeight -> height
            spanWidth > 0 -> spanWidth
            else -> maxOf(textWidth, bgWidth, fgWidth)
        }

        if (spanMaxWidth != undefined_int) {
            width = min(width, spanMaxWidth)
        }

        return intArrayOf(width, height)
    }

    override fun getSize(
        paint: Paint,
        text: CharSequence?,
        start: Int,
        end: Int,
        fm: Paint.FontMetricsInt?
    ): Int {
        val measureSize = _measureSize(paint, text, start, end, fm)
        return measureSize[0] + marginLeft + marginRight + paddingLeft + paddingRight
    }

    override fun draw(
        canvas: Canvas,
        text: CharSequence?,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,//基线位置
        bottom: Int,//底部位置
        paint: Paint
    ) {
        val measureSize = _measureSize(paint, text, start, end)
        val measureWidth = measureSize[0]
        val measureHeight = measureSize[1] - marginTop - marginBottom
        val drawHeight = bottom - top
        val targetText = _targetText(text, start, end)

        fillBackgroundDrawable?.apply {
            setBounds(
                (x + marginLeft).toInt(),
                top + marginTop,
                (x + measureWidth - marginRight).toInt(),
                y - marginBottom
            )
            draw(canvas)
        }

        canvas.save()

        //绘制文本
        val textWidth = textPaint.measureText(targetText, 0, targetText.length).toInt()
        val textHeight = textPaint.descent() - textPaint.ascent()

        //偏移画布
        canvas.translate(marginLeft + offsetX, marginTop + offsetY)

        val layoutDirection = 0
        val absoluteGravity = GravityCompat.getAbsoluteGravity(textGravity, layoutDirection)
        val verticalGravity = textGravity and Gravity.VERTICAL_GRAVITY_MASK
        val horizontalGravity = absoluteGravity and Gravity.HORIZONTAL_GRAVITY_MASK

        val textX: Float = when (horizontalGravity) {
            Gravity.CENTER_HORIZONTAL -> x + measureWidth / 2 - textWidth / 2 + paddingLeft
            Gravity.RIGHT -> x + measureWidth - textWidth - paddingRight
            else -> x + paddingLeft
        }

        val textY: Float = when (verticalGravity) {
            Gravity.CENTER_VERTICAL -> top + (drawHeight - textHeight) / 2 - textPaint.ascent()
            Gravity.BOTTOM -> (y - marginBottom - paddingBottom).toFloat()
            else -> top - textPaint.ascent()
        }

        fun choiceHeight(): Int {
            //空白文本, drawable将采用measure的size当做wrap_content
            val blankText = targetText.isBlank()
            return if (blankText) measureHeight else textHeight.toInt()
        }

        fun choiceWidth(): Int {
            //空白文本, drawable将采用measure的size当做wrap_content
            val blankText = targetText.isBlank()
            return when {
                widthSameHeight -> choiceHeight()
                blankText -> measureWidth
                else -> textWidth
            }
        }

        fun drawDrawable(drawable: Drawable?) {

            drawable?.let {
                val height = _drawableHeight(it).other(measureHeight, choiceHeight())
                val width = _drawableWidth(it).other(measureWidth, choiceWidth())

                val textCenterX = textX + textWidth / 2
                val textCenterY = textY + textPaint.ascent() / 2

                val l: Int = (textCenterX - width / 2).toInt()
                val t: Int = (textCenterY - height / 2 + textPaint.descent() / 2).toInt()

                it.setBounds(
                    l - paddingLeft,
                    t - paddingTop,
                    l + width + paddingRight,
                    t + height + paddingBottom
                )
                it.draw(canvas)
            }
        }

        //绘制背景
        drawDrawable(backgroundDrawable)

        if (gradientSolidColor != undefined_color || gradientStrokeColor != undefined_color) {
            val height = choiceHeight()
            val width = choiceWidth()
            val textCenterX = textX + textWidth / 2
            val textCenterY = textY + textPaint.ascent() / 2
            val l: Int = (textCenterX - width / 2).toInt()
            val t: Int = (textCenterY - height / 2 + textPaint.descent() / 2).toInt()

            _gradientRectF.set(
                (l - paddingLeft).toFloat(),
                (t - paddingTop).toFloat(),
                (l + width + paddingRight).toFloat(),
                (t + height + paddingBottom).toFloat()
            )

            if (gradientSolidColor != undefined_color) {
                textPaint.style = Paint.Style.FILL
                textPaint.color = gradientSolidColor
                canvas.drawRoundRect(_gradientRectF, gradientRadius, gradientRadius, textPaint)
            }
            if (gradientStrokeColor != undefined_color) {
                textPaint.style = Paint.Style.STROKE
                textPaint.strokeWidth = gradientStrokeWidth
                textPaint.color = gradientStrokeColor
                _gradientRectF.inset(gradientStrokeWidth / 2, gradientStrokeWidth / 2)
                canvas.drawRoundRect(_gradientRectF, gradientRadius, gradientRadius, textPaint)
            }

            _initPaint(paint)
        }

        canvas.drawText(
            targetText,
            0,
            targetText.length,
            textX + textOffsetX,
            textY + textOffsetY,
            textPaint
        )

        //绘制前景
        drawDrawable(foregroundDrawable)

        canvas.restore()
    }

    fun Int.other(max: Int, min: Int): Int {
        return when {
            //MATCH_PARENT
            this == -1 -> max
            //WRAP_CONTENT
            this <= 0 -> min
            //EXACTLY
            else -> this
        }
    }

    fun padding(padding: Int) {
        paddingHorizontal(padding)
        paddingVertical(padding)
    }

    fun paddingHorizontal(padding: Int) {
        paddingLeft = padding
        paddingRight = padding
    }

    fun paddingVertical(padding: Int) {
        paddingTop = padding
        paddingBottom = padding
    }

    fun marginHorizontal(margin: Int) {
        marginLeft = margin
        marginRight = margin
    }

    fun marginVertical(margin: Int) {
        marginTop = margin
        marginBottom = margin
    }

    override fun onMeasure(widthSize: Int, heightSize: Int) {
        if (spanWeight != undefined_float) {
            spanWidth = (spanWeight * widthSize).toInt()
        }
        if (spanMaxWeight != undefined_float) {
            spanMaxWidth = (spanMaxWeight * widthSize).toInt()
        }
    }

    override fun isCanClick(): Boolean {
        return spanClickAction != null
    }

    override fun onClickSpan(view: View, span: IClickableSpan) {
        spanClickAction?.run { this(view, this@DslDrawableSpan) } ?: super.onClickSpan(view, span)
    }

    override fun onTouchEvent(view: View, span: IClickableSpan, event: MotionEvent) {
        super.onTouchEvent(view, span, event)
    }

    override fun setDrawableState(state: IntArray) {
        backgroundDrawable?.let {
            it.setState(state)
        }
        foregroundDrawable?.let {
            it.setState(state)
        }
    }

    override fun setDrawableColor(color: Int) {
        backgroundDrawable = backgroundDrawable?.colorFilter(color)
        foregroundDrawable = foregroundDrawable?.colorFilter(color)
    }
}