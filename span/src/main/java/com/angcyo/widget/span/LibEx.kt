package com.angcyo.widget.span

import android.content.res.Resources
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.text.Spanned
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.graphics.drawable.DrawableCompat

/**
 *
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/04/27
 * Copyright (c) 2020 ShenZhen Wayto Ltd. All rights reserved.
 */

val undefined_color = -32_768

/**第4位的最高字节  0x8000 = 32,768, 未定义的资源*/
val undefined_res = -32_768

/**未定义的整数*/
val undefined_int = -1

/**未定义的大小*/
val undefined_size = Int.MIN_VALUE

/**未定义的浮点数*/
val undefined_float = -1f

val density: Float get() = Resources.getSystem()?.displayMetrics?.density ?: 0f
val dp: Float get() = Resources.getSystem()?.displayMetrics?.density ?: 0f
val dpi: Int get() = Resources.getSystem()?.displayMetrics?.density?.toInt() ?: 0

fun Int.toDp(): Float {
    return this * dp
}

fun Int.toDpi(): Int {
    return this * dpi
}

fun Float.toDp(): Float {
    return this * dp
}

fun Float.toDpi(): Int {
    return (this * dpi).toInt()
}

/**
 * 颜色过滤
 */
fun Drawable?.colorFilter(@ColorInt color: Int): Drawable? {
    if (this == null) {
        return null
    }
    val wrappedDrawable = DrawableCompat.wrap(this).mutate()
    wrappedDrawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    return wrappedDrawable
}

@ColorInt
fun String.toColorInt(): Int = Color.parseColor(this)

/**枚举所有[span]*/
fun TextView.spans(action: (index: Int, span: Any) -> Unit) {
    val text = text
    if (text is Spanned) {
        val spans = text.getSpans(0, text.length, Any::class.java)
        spans.forEachIndexed(action)
    }
}