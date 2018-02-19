# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\gaurav.garg\AppData\Local\Android\sdk/tools/proguard/proguard-android.txt
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

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-dontpreverify
-allowaccessmodification
-optimizations !code/simplification/arithmetic
-keepattributes *Annotation*
-flattenpackagehierarchy 'app'
-repackageclasses 'googleadv'


-optimizationpasses 12
-overloadaggressively

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider


-dontwarn com.facebook.**
-keep class com.facebook.** {*;}

-dontwarn dagger.**
-keep class dagger.** {*;}

-dontwarn com.tapjoy.**
-keep class com.tapjoy.** {*;}

-dontwarn com.google.**
-keep class com.google.** {*;}

-dontwarn butterknife.**
-keep class butterknife.** {*;}


-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.** {*;}

-dontwarn com.acpl.access_computech_fm220_sdk.**
-keep class com.acpl.access_computech_fm220_sdk.** {*;}

-dontwarn com.mantra.mfs100.**
-keep class com.mantra.mfs100.** {*;}

-dontwarn com.chartboost.**
-keep class com.chartboost.** {*;}

-dontwarn com.jakewharton.**
-keep class com.jakewharton.** {*;}


-dontwarn com.millennialmedia.**
-keep class com.millennialmedia.** {*;}

-dontwarn com.android.volley.**
-keep class com.android.volley.** {*;}

-dontwarn org.jivesoftware.smack.**
-keep class org.jivesoftware.smack.** {*;}

-dontwarn org.acra.**
-keep class org.acra.** {*;}
-dontwarn okio.**

-dontwarn javax.**
-keep class javax.** {*;}

-dontwarn org.simpleframework.**
-keep class org.simpleframework.** {*;}

-dontwarn com.vungle.**
-keep class com.vungle.** {*;}

-keepattributes *Annotation*
-dontoptimize

-dontwarn android.support.**
-dontwarn checkpoint.forms.**
-dontwarn com.applovin.**
-dontwarn com.google.**

-keep class com.flurry.** { *; }
-dontwarn com.flurry.**

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.content.Context {
   public void *(android.view.View);
   public void *(android.view.MenuItem);
}

-keepclassmembers class * implements android.os.Parcelable {
    static android.os.Parcelable$Creator CREATOR;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}