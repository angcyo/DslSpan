package com.angcyo.widget.span

import android.text.Selection
import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.text.method.Touch
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.TextView
import java.lang.ref.WeakReference
import kotlin.math.absoluteValue

/**
 * [IClickableSpan]点击事件的支持.
 *
 * 注意:当使用了[LinkMovementMethod]后, [TextView]的事件同样有效.并不会被取代!
 *
 * Email:angcyo@126.com
 *
 * @author angcyo
 * @date 2020/01/11
 */
class SpanClickMethod : LinkMovementMethod() {

    companion object {
        var _touchDownSpan: WeakReference<IClickableSpan>? = null

        val instance: SpanClickMethod by lazy {
            SpanClickMethod()
        }

        /**安装在[TextView]中
         * [LinkMovementMethod.getInstance()]*/
        fun install(textView: TextView?) {
            textView?.movementMethod = instance
        }
    }

    var _downX: Float = 0f
    var _downY: Float = 0f

    override fun onTouchEvent(widget: TextView, buffer: Spannable, event: MotionEvent): Boolean {
        val action = event.action

        if (action == MotionEvent.ACTION_DOWN) {
            _downX = event.x
            _downY = event.y
        }

        var cancel = action == MotionEvent.ACTION_CANCEL

        if (action == MotionEvent.ACTION_MOVE) {
            val moveX = event.x
            val moveY = event.y

            val scaledTouchSlop = ViewConfiguration.get(widget.context).scaledTouchSlop
            if ((moveX - _downX).absoluteValue > scaledTouchSlop ||
                (moveY - _downY).absoluteValue > scaledTouchSlop
            ) {
                cancel = true
            }
        }

        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {
            var x = event.x.toInt()
            var y = event.y.toInt()
            x -= widget.totalPaddingLeft
            y -= widget.totalPaddingTop
            x += widget.scrollX
            y += widget.scrollY
            val layout = widget.layout
            val line = layout.getLineForVertical(y)
            val off = layout.getOffsetForHorizontal(line, x.toFloat())
            val links = buffer.getSpans(off, off, IClickableSpan::class.java)

            if (links.isNotEmpty()) {
                for (link in links) {
                    if (link.isCanClick()) {
                        link.onTouchEvent(widget, link, event)

                        if (action == MotionEvent.ACTION_UP) {
                            if (_touchDownSpan?.get() == link) {
                                link.onClickSpan(widget, link)
                            }
                        } else if (action == MotionEvent.ACTION_DOWN) {
                            _touchDownSpan = WeakReference(link)
//                    Selection.setSelection(
//                        buffer,
//                        buffer.getSpanStart(link),
//                        buffer.getSpanEnd(link)
//                    )
                        }
                        return true
                    }
                }
                return false
            } else {
                Selection.removeSelection(buffer)
            }
        }

        if (cancel) {
            _touchDownSpan?.get()?.run {
                val cancelEvent = MotionEvent.obtain(event)
                cancelEvent.action = MotionEvent.ACTION_CANCEL
                onTouchEvent(widget, this, cancelEvent)
                cancelEvent.recycle()
            }
            _touchDownSpan = null
        }

        Touch.onTouchEvent(widget, buffer, event)
        return false
    }
}