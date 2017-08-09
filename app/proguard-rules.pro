# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/Jinsen/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}


-optimizations !class/unboxing/enum
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-ignorewarning

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider

-keep public class com.android.vending.licensing.ILicensingService
-keep public class com.vanpro.zitech125.ui.activity.**
-keep public class com.vanpro.zitech125.ui.fragment.**


-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclassmembers class ** {
   public void onEvent*(**);
}

##--Begin: Umeng-------------------------
-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}
-keep class com.umeng.** { *; }
-keep public class com.umeng.fb.ui.ThreadView {
}
-dontwarn com.umeng.socialize.**
##--END: Umeng----------------------------

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-dontwarn com.squareup.picasso.**
-dontwarn com.sina.sso.**
-dontwarn rx.internal.**

##---Begin: Javascript Interface---
-keepattributes *Annotation*
-keepattributes *JavascriptInterface*
##---End: Javascript Interface---

##---Begin: Weixin ----------------
-keep class com.tencent.android.**{
    *;
}
-keep class com.tencent.mm.sdk.** {
   *;
}
##---End: Weixin -------------------


##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.vanpro.zitech125.entity.** { *; }
-keep class com.vanpro.zitech125.dao.** { *; }
-keep class com.vanpro.zitech125.dao.**$* { *; }
-keep class com.vanpro.zitech125.controller.** { *; }
-keep class com.vanpro.zitech125.event.** { *; }

##---------------End: proguard configuration for Gson  ----------

##---------------Begin: QY -------------------------------------
-keep class com.vanpro.zitech125.ui.activity.**
-keep class com.vanpro.zitech125.ui.fragment.**


-keep public class com.vanpro.zitech125.R$*{
    public static final int *;
}
##---------------End: QY ---------------------------------------


-dontwarn java.awt.**
-dontwarn javax.swing.**
-dontwarn demo.Pinyin4jAppletDemo
-dontwarn net.sourceforge.pinyin4j.**

##----------------START: okhttp --------------
-dontwarn java.nio.**
-dontwarn org.codehaus.mojo.**
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**

-dontwarn rx.**
-dontwarn retrofit.**
-dontwarn okio.**
-keep class retrofit.** { *; }
-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}

##----------------END: okhttp --------------

##----------------START: alipay ------------
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}
-keep public class * extends android.os.IInterface
-keep class com.tencent.mm.**{*;}
-dontwarn com.alipay.mobile.**
-dontwarn com.taobao.**
-dontwarn com.alipay.android.phone.mrpc.core.**
-keep class com.alibaba.fastjson.**{*;}
-dontwarn com.alibaba.fastjson.**
##----------------END: alipay --------------

##---------------START: baidu APM & map -----------
-dontwarn com.baidu.uaq.com.google.gson.**
-keep class com.baidu.uaq.com.google.gson.** { *;}
-dontwarn com.google.gson.**
-keep class com.google.gson.** { *;}
-dontwarn com.baidu.uaq.agent.android.**
-keep class com.baidu.uaq.agent.android.** { *;}
-keepattributes Exceptions,InnerClasses,Signature

-keep class com.baidu.** {*;}
-keep class vi.com.** {*;}
-dontwarn com.baidu.**
##--------------END: baidu APM & map --------------


##---------------START: OneAPM--------------
-dontwarn org.apache.commons.**
-keep class org.apache.http.impl.client.**
-dontwarn org.apache.commons.**
-keep class com.blueware.** { *; }
-dontwarn com.blueware.**
-keepattributes Exceptions, Signature, InnerClasses
##---------------END: OneAPM----------------

-dontwarn android.app.Notification.**
-dontwarn android.content.Context

##---------------START: greendao----------
-dontwarn de.greenrobot.daogenerator.**
-keepclassmembers class * extends de.greenrobot.dao.AbstractDao {
    public static java.lang.String TABLENAME;
}
-keep class **$Properties
##---------------END: greendao-------------