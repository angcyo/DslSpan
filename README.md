# DslSpan
Android一个强大的自定义span(`DslDrawableSpan`), 以及一个`SpannableStringBuilder`使用工具.


![](https://img.shields.io/badge/License-MIT-3A66AC) ![](https://img.shields.io/badge/Api-8+-4A773C) ![](https://img.shields.io/badge/AndroidX-yes-AA883C)
![](https://img.shields.io/badge/Kotlin-yes-3AC9A9)


本库提供2个自定义`span`(`DslTextSpan` `DslDrawableSpan`) 和一个`SpannableStringBuilder`工具类`DslSpan`

![](https://raw.githubusercontent.com/angcyo/DslSpan/master/png/span.png)


# DslDrawableSpan

这个类完全自定义继承`ReplacementSpan`, 功能强大, 使用简便:

- 支持`绘制` `替换` 原有的`文本内容`
- 支持`Drawable`绘制
- 支持文本的`Gravity`属性(类似于`TextView`中的`gravity`)
- 支持宽度的`Weight`属性(类似于`LinearLayout`中的`weight`)
- 支持`margin`属性
- 支持`padding`属性
- 支持`click`点击事件(不影响`TextView`原有的`OnClickListener`事件)

**支持的属性如下:**

```kotlin
/**绘制文本时的文本大小*/
@Px
var textSize: Float = -1f
/**绘制文本时的文本颜色*/
@ColorInt
var textColor: Int = undefined_color
/**文本绘制的重力*/
var textGravity: Int = Gravity.LEFT or Gravity.BOTTOM
/**需要替换显示的文本, 为空则绘制默认append的文本*/
var showText: CharSequence? = null
/**强制指定span的宽度*/
var spanWidth: Int = undefined_int
/**强制指定span的高度*/
var spanHeight: Int = undefined_int
/**限制span的最大宽度*/
var spanMaxWidth: Int = undefined_int
/**span相对于[TextView]的比例, 不支持平分. 需要[DslSpanTextView]支持*/
var spanWeight: Float = undefined_float
/**使用weight计算最大宽度的限制*/
var spanMaxWeight: Float = undefined_float
/**宽度等于高度*/
var widthSameHeight = false
/**填充背景[Drawable], 在最下面绘制*/
var fillBackgroundDrawable: Drawable? = null
/**背景[Drawable], 在文本下面绘制*/
var backgroundDrawable: Drawable? = null
/**前景[Drawable], 在文本上面绘制*/
var foregroundDrawable: Drawable? = null
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
/**单击事件回调, 需要[SpanClickMethod]支持*/
var spanClickAction: ((view: View, span: DslDrawableSpan) -> Unit)? = null
```

# DslTextSpan

这个类组合了系统各个`CharacterStyle`的样式.

**支持的属性如下:**

```kotlin
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
/**文本基线偏移*/
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
```

> 注意: 
如果需要`span`的点击事件, 请调用`SpanClickMethod.install(textView)`, 方可生效.
如果需要`weight`的属性的支持,请使用`DslSpanTextView`替换原来的`TextView`

# DslSpan

这个类是`SpannableStringBuilder`的工具类, 用于方便添加`span`.

**实战使用如下:**

```kotlin
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
```


# 使用`JitPack`的方式, 引入库.

## 根目录中的 `build.gradle`

```kotlin
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

## APP目录中的 `build.gradle`

```kotlin
dependencies {
    implementation 'com.github.angcyo:DslSpan:1.0.0'
}
```

---
**群内有`各(pian)种(ni)各(jin)样(qun)`的大佬,等你来撩.**

# 联系作者

[点此QQ对话](http://wpa.qq.com/msgrd?v=3&uin=664738095&site=qq&menu=yes)  `该死的空格`    [点此快速加群](https://shang.qq.com/wpa/qunwpa?idkey=cbcf9a42faf2fe730b51004d33ac70863617e6999fce7daf43231f3cf2997460)

[开源地址](https://github.com/angcyo/DslAdapter)

![](https://gitee.com/angcyo/res/raw/master/code/all_in1.jpg)

![](https://gitee.com/angcyo/res/raw/master/code/all_in2.jpg)
