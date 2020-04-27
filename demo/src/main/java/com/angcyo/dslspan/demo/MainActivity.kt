package com.angcyo.dslspan.demo

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.angcyo.widget.span.SpanClickMethod
import com.angcyo.widget.span.dp
import com.angcyo.widget.span.dpi
import com.angcyo.widget.span.span

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv(R.id.text1).text = span {
            append("默认无样式的标准文本")
        }

        tv(R.id.text2).text = span {
            append("系统")
            append("Span") {
                fontSize = 30 * dpi
                foregroundColor = Color.RED
            }
            append("样式") {
                backgroundColor = Color.GREEN
                scaleX = 2f
            }
        }

        tv(R.id.text3).text = span {
            append("带Drawable")
            drawable {
                backgroundDrawable = resources.getDrawable(R.mipmap.ic_launcher)
            }
            text("自绘Text") {
                textColor = Color.WHITE
                bgColor = Color.BLACK
            }
            drawable {
                showText = "自绘Text, 支持Gravity"
                textColor = Color.WHITE
                spanWidth = ViewGroup.LayoutParams.WRAP_CONTENT
                marginLeft = 20 * dpi
                paddingLeft = 20 * dpi
                paddingRight = 20 * dpi
                textGravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
                backgroundDrawable = ColorDrawable(Color.DKGRAY)
            }
        }

        tv(R.id.text4).text = span {
            drawable {
                showText = "支持Weight"
                spanWeight = 0.33333333f
                spanHeight = 30 * dpi
                fillBackgroundDrawable = ColorDrawable(Color.RED)
                textGravity = Gravity.CENTER
            }
            drawable {
                showText = "各占三分之一"
                spanWeight = 0.33333333f
                spanHeight = 40 * dpi
                fillBackgroundDrawable = ColorDrawable(Color.GREEN)
                textGravity = Gravity.CENTER
            }
            drawable {
                showText = "1/3"
                spanWeight = 0.33333333f
                spanHeight = 50 * dpi
                fillBackgroundDrawable = ColorDrawable(Color.BLUE)
                textColor = Color.WHITE
                textGravity = Gravity.CENTER
            }
        }

        tv(R.id.text5).apply {
            SpanClickMethod.install(this)
            setOnClickListener {
                Toast.makeText(this@MainActivity, "点击View", Toast.LENGTH_SHORT).show()
            }

            text = span {
                drawable {
                    showText = "支持点击"
                    spanWeight = 0.5f
                    backgroundDrawable = ColorDrawable(Color.RED)
                    textGravity = Gravity.CENTER
                }
                drawable {
                    showText = "点我试试?"
                    spanWeight = 0.5f
                    textSize = 40 * dp
                    backgroundDrawable = ColorDrawable(Color.GREEN)
                    textGravity = Gravity.CENTER
                    textColor = Color.WHITE
                    spanClickAction = { view, span ->
                        Toast.makeText(this@MainActivity, "点击Span", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        tv(R.id.text6).text = span {
            append("自绘Drawable")
            drawable {
                backgroundDrawable = resources.getDrawable(R.mipmap.ic_launcher)
            }
            drawable {
                foregroundDrawable = resources.getDrawable(R.mipmap.ic_launcher_round)
            }
            drawable {
                backgroundDrawable = resources.getDrawable(R.mipmap.ic_launcher)
                foregroundDrawable = resources.getDrawable(R.mipmap.ic_launcher_round)
            }
        }

        tv(R.id.text7).text = span {
            append("支持空隙")
            appendSpace(10 * dpi)
            append("空隙大小")
            appendSpace(20 * dpi, Color.RED)
            append("空隙颜色")
            appendSpace(30 * dpi, Color.GREEN)
            append("支持空隙")
            appendSpace(40 * dpi, Color.BLUE)
        }

        tv(R.id.text8).text = span {
            drawable {
                showText = "支持Margin"
                marginLeft = 20 * dpi
                marginRight = 20 * dpi
                backgroundDrawable = ColorDrawable(Color.RED)
            }
            drawable {
                showText = "支持Padding"
                paddingLeft = 20 * dpi
                paddingRight = 20 * dpi
                backgroundDrawable = ColorDrawable(Color.GREEN)
            }
        }
    }

    fun tv(id: Int): TextView = findViewById(id)
}
