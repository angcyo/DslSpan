package com.angcyo.widget.text

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView
import com.angcyo.widget.span.IDrawableSpan
import com.angcyo.widget.span.IWeightSpan
import com.angcyo.widget.span.spans

/**
 * 自定义Span支持类
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/01/08
 */
open class DslSpanTextView : AppCompatTextView {

    //drawable 额外的状态
    val _extraState = mutableListOf<Int>()

    var isInitExtraState: Boolean = false

    constructor(context: Context) : super(context) {
        initAttribute(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initAttribute(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initAttribute(context, attrs)
    }

    private fun initAttribute(context: Context, attributeSet: AttributeSet?) {

    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        if (!isInitExtraState) {
            return super.onCreateDrawableState(extraSpace)
        }

        val state = super.onCreateDrawableState(extraSpace + _extraState.size)

        if (_extraState.isNotEmpty()) {
            View.mergeDrawableStates(state, _extraState.toIntArray())
        }

        return state
    }

    override fun drawableStateChanged() {
        super.drawableStateChanged()
        val state = onCreateDrawableState(0)

        //设置内置span的状态
        spans { _, span ->
            if (span is IDrawableSpan) {
                span.setDrawableState(state)
            }
        }
    }

    fun setDrawableColor(@ColorInt color: Int) {
        //设置内置span的颜色
        spans { _, span ->
            if (span is IDrawableSpan) {
                span.setDrawableColor(color)
            }
        }
    }

    /**添加额外的状态*/
    fun addDrawableState(state: Int) {
        isInitExtraState = true
        _extraState.add(state)
        refreshDrawableState()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)

        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        _measureWeightSpan(widthSize, heightSize)

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    fun _measureWeightSpan(widthSize: Int, heightSize: Int) {
        //设置内置span的weight支持
        spans { _, span ->
            if (span is IWeightSpan) {
                span.onMeasure(widthSize, heightSize)
            }
        }
    }

}