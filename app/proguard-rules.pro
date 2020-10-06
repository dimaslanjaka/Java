# android support
-keep class android.support.customtabs.* { *; }
-dontwarn android.support.v4.**
-keep class android.support.v4.* { *; }
-dontwarn android.support.v7.**
-keep class android.support.v7.* { *; }
-dontwarn android.support.annotation.**
-keep class android.support.annotation.* { *; }

# Firebase
-keepattributes Signature

# material design
-dontwarn com.google.android.material.**
-keep class com.google.android.material.* { *; }

# androidx
-dontwarn androidx.**
-keep class androidx.* { *; }
-keep interface androidx.* { *; }

# glide
-keep class com.bumptech.glide.* { *; }
-keep class com.airbnb.lottie.* { *; }
-keep class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder.InternalRewinder { *** rewind(); }

# butterknife
-keep class **$$ViewBinder { *; }
# Retain generated class which implement Unbinder.
-keep public class * implements butterknife.Unbinder { public <init>(**, android.view.View); }

# Prevent obfuscation of types which use ButterKnife annotations since the simple name
# is used to reflectively look up the generated ViewBinding.
-keep class butterknife.*
-keepclasseswithmembernames class * { @butterknife.* <methods>; }
-keepclasseswithmembernames class * { @butterknife.* <fields>; }

# OKHttp
# JSR 305 annotations are for embedding nullability information.
-dontwarn javax.annotation.**

# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*

# OkHttp platform used only on JVM and when Conscrypt dependency is available.
-dontwarn okhttp3.internal.platform.ConscryptPlatform

-keep class cz.msebera.android.httpclient.* { *; }
-keep class com.loopj.android.http.* { *; }