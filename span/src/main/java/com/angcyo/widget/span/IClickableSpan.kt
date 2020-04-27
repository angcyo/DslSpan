package com.angcyo.widget.span

import android.view.MotionEvent
import android.view.View

/**
 *
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/01/11
 */
interface IClickableSpan {

    fun isCanClick(): Boolean {
        return true
    }

    fun onClickSpan(view: View, span: IClickableSpan) {
    }

    fun onTouchEvent(view: View, span: IClickableSpan, event: MotionEvent) {
    }
}