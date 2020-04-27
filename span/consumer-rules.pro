# 保持成员变量不被混淆
-keepclassmembers class androidx.fragment.app.Fragment{
    String mTag;
}

-keepclassmembers class android.app.Fragment{
    String mTag;
}

-keepclassmembers class android.support.v4.app.Fragment{
    String mTag;
}