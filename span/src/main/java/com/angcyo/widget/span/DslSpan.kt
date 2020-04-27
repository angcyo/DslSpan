package com.angcyo.widget.span

import android.graphics.Color
import android.graphics.MaskFilter
import android.graphics.drawable.Drawable
import android.text.SpannableStringBuilder
import android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
import android.text.style.*
import android.view.Gravity
import androidx.annotation.ColorInt
import androidx.annotation.Px
import kotlin.math.max

/**
 * span 操作类
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/01/08
 */

class DslSpan {

    val _builder = SpannableStringBuilder()

    /**是否忽略null或者empty的text*/
    var ignoreNullOrEmpty = true

    var flag: Int = SPAN_EXCLUSIVE_EXCLUSIVE

    /**根据[DslSpanConfig]创建[span]*/
    var spanFactory: (DslSpanConfig) -> List<Any> = { config ->
        val list = mutableListOf<Any>()

        config.apply {

            if (foregroundColor != undefined_color) {
                list.add(ForegroundColorSpan(foregroundColor))
            }

            if (backgroundColor != undefined_color) {
                list.add(BackgroundColorSpan(backgroundColor))
            }

            if (lineBackgroundColor != undefined_color) {
                list.add(LineBackgroundSpan(lineBackgroundColor))
            }

            if (underline) {
                list.add(UnderlineSpan())
            }

            if (deleteLine) {
                list.add(StrikethroughSpan())
            }

            if (!typefaceFamily.isNullOrEmpty()) {
                list.add(TypefaceSpan(typefaceFamily))
            }

            if (style != undefined_int) {
                list.add(StyleSpan(style))
            }

            if (tabStopOffset != undefined_int) {
                list.add(TabStopSpan.Standard(tabStopOffset))
            }

            if (isSuperscript) {
                list.add(SuperscriptSpan())
            }

            if (isSubscript) {
                list.add(SubscriptSpan())
            }

            if (scaleX > 0) {
                list.add(ScaleXSpan(scaleX))
            }

            if (relativeSizeScale > 0) {
                list.add(RelativeSizeSpan(relativeSizeScale))
            }

            if (quoteColor != undefined_color) {
                list.add(
                    CustomQuoteSpan(
                        quoteColor,
                        quoteStripeWidth,
                        quoteGapLeftWidth,
                        quoteGapRightWidth
                    )
                )
            }

            if (maskFilter != null) {
                list.add(MaskFilterSpan(maskFilter))
            }

            if (leadingMarginFirst != undefined_int || leadingMarginRest != undefined_int) {
                list.add(
                    LeadingMarginSpan.Standard(
                        max(leadingMarginFirst, 0),
                        max(leadingMarginRest, 0)
                    )
                )
            }

            if (fontSize != undefined_int) {
                list.add(AbsoluteSizeSpan(fontSize))
            }
        }

        list
    }

    fun doIt(): SpannableStringBuilder {
        return _builder
    }

    fun _ignore(text: CharSequence?, action: () -> Unit) {
        if (ignoreNullOrEmpty && text.isNullOrEmpty()) {
            //ignore
        } else {
            action()
            _reset()
        }
    }

    fun _reset() {
        ignoreNullOrEmpty = true
        flag = SPAN_EXCLUSIVE_EXCLUSIVE
    }

    fun appendln(): DslSpan {
        _builder.appendln()
        return this
    }

    /**追加指定[span]*/
    fun append(text: CharSequence?, vararg spans: Any): DslSpan {
        _ignore(text) {
            val start = _builder.length
            _builder.append(text)
            val end = _builder.length
            for (span in spans) {
                _builder.setSpan(span, start, end, flag)
            }
        }
        return this
    }

    /**通过配置指定[span]*/
    fun append(text: CharSequence?, action: DslSpanConfig.() -> Unit): DslSpan {
        _ignore(text) {
            val start = _builder.length
            _builder.append(text)
            val end = _builder.length

            val config = DslSpanConfig()
            config.action()
            for (span in spanFactory(config)) {
                _builder.setSpan(span, start, end, config.flag)
            }
        }
        return this
    }

    /**追加空隙*/
    fun appendSpace(@Px size: Int, @ColorInt color: Int = Color.TRANSPARENT): DslSpan {
        append("<space>", SpaceSpan(size, color))
        return this
    }

    fun set(start: Int, end: Int, vararg spans: Any): DslSpan {
        for (span in spans) {
            _builder.setSpan(span, start, end, flag)
        }
        return this
    }

    /**追加图片*/
    fun appendImage(
        drawable: Drawable?,
        alignment: Int = DynamicDrawableSpan.ALIGN_BASELINE
    ): DslSpan {
        if (drawable != null) {
            if (drawable.bounds.isEmpty) {
                drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
            }
            append("<img>", ImageSpan(drawable, alignment))
        }
        return this
    }

    /**快速追加[DslDrawableSpan]*/
    fun drawable(text: CharSequence? = null, action: DslDrawableSpan.() -> Unit = {}): DslSpan {
        if (text.isNullOrEmpty()) {
            //智能调整
            append("<draw>", DslDrawableSpan().apply {
                showText = ""
                textGravity = Gravity.CENTER
                this.action()
            })
        } else {
            append(text, DslDrawableSpan().apply(action))
        }
        return this
    }

    /**快速追加[DslTextSpan]*/
    fun text(text: CharSequence?, action: DslTextSpan.() -> Unit = {}): DslSpan {
        append(text, DslTextSpan().apply(action))
        return this
    }

    override fun toString(): String {
        return _builder.toString()
    }
}

data class DslSpanConfig(
    var flag: Int = SPAN_EXCLUSIVE_EXCLUSIVE,

    /**[ForegroundColorSpan]*/
    @ColorInt var foregroundColor: Int = undefined_color,
    /**[BackgroundColorSpan]*/
    @ColorInt var backgroundColor: Int = undefined_color,
    /**[LineBackgroundSpan]*/
    @ColorInt var lineBackgroundColor: Int = undefined_color,

    /**下划线 [UnderlineSpan]*/
    var underline: Boolean = false,

    /**删除线 [StrikethroughSpan]*/
    var deleteLine: Boolean = false,

    /**字体 "monospace", "serif", and "sans-serif" [TypefaceSpan]*/
    var typefaceFamily: String? = null,

    /**tab 首行偏移量 [TabStopSpan], 需要`\t`的支持*/
    var tabStopOffset: Int = undefined_int,

    /**上标 [SuperscriptSpan]*/
    var isSuperscript: Boolean = false,
    /**下标 [SubscriptSpan]*/
    var isSubscript: Boolean = false,

    /**缩放x轴 [ScaleXSpan]*/
    var scaleX: Float = -1f,

    /**[StyleSpan] [android.graphics.Typeface.NORMAL] [BOLD] [ITALIC] [BOLD_ITALIC]*/
    var style: Int = undefined_int,

    /**缩放字体撒小 [RelativeSizeSpan]*/
    var relativeSizeScale: Float = -1f,

    /**[AbsoluteSizeSpan]*/
    @Px var fontSize: Int = undefined_int,

    /**只在行首有效 [CustomQuoteSpan]*/
    @ColorInt var quoteColor: Int = undefined_color,
    @Px var quoteStripeWidth: Int = 2,
    @Px var quoteGapLeftWidth: Int = 0,
    @Px var quoteGapRightWidth: Int = 2,

    /**[MaskFilterSpan]*/
    var maskFilter: MaskFilter? = null,

    /**[LeadingMarginSpan]*/
    var leadingMarginFirst: Int = undefined_int,
    var leadingMarginRest: Int = undefined_int
)

/**快速绘制一个轻提示, 边框*/
fun DslSpan.drawableTipBorder(
    text: CharSequence? = null,
    borderColor: Int = "#0EC300".toColorInt(),
    action: DslDrawableSpan.() -> Unit = {}
): DslSpan {
    return drawable(text) {
        gradientSolidColor = Color.WHITE
        gradientRadius = 10 * dp
        textSize = 9 * dp
        textGravity = Gravity.CENTER
        textColor = borderColor
        gradientStrokeColor = textColor
        paddingHorizontal(4 * dpi)
        paddingVertical(2 * dpi)
        action()
    }
}

/**快速绘制一个轻提示, 填充*/
fun DslSpan.drawableTipFill(
    text: CharSequence? = null,
    solidColor: Int = Color.RED,
    action: DslDrawableSpan.() -> Unit = {}
): DslSpan {
    return drawable(text) {
        gradientSolidColor = solidColor
        gradientRadius = 10 * dp
        paddingHorizontal(4 * dpi)
        paddingVertical(2 * dpi)
        textColor = Color.WHITE
        action()
    }
}

fun span(action: DslSpan.() -> Unit): SpannableStringBuilder {
    return DslSpan().apply {
        action()
    }.doIt()
}