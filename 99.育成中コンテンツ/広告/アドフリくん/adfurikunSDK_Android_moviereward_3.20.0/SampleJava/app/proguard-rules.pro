# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/PC-269/dev/android-sdk/tools/proguard/proguard-android.txt
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


-dontskipnonpubliclibraryclassmembers

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference

-keepattributes SourceFile,LineNumberTable
-keepattributes Signature

# Needed to keep generic types and @Key annotations accessed via reflection
-keepattributes Signature,RuntimeVisibleAnnotations,AnnotationDefault

-keepclassmembers class * {
  @com.google.api.client.util.Key <fields>;
}

# Needed by google-http-client-android when linking against an older platform version

-dontwarn com.google.api.client.extensions.android.**

# Needed by google-api-client-android when linking against an older platform version

-dontwarn com.google.api.client.googleapis.extensions.android.**

# Needed by google-play-services when linking against an older platform version
# -dontwarn com.google.android.gms.**

# com.google.client.util.IOUtils references java.nio.file.Files when on Java 7+
-dontnote java.nio.file.Files, java.nio.file.Path

# Suppress notes on LicensingServices
-dontnote **.ILicensingService

# <!-- 動画リワード ProGuard設定 ->
# Support for Adfurikun Movie Reward
-keep interface jp.tjkapp.adfurikunsdk.moviereward.**
-keep class jp.tjkapp.adfurikunsdk.moviereward.** {
	public *;
}
-dontwarn jp.tjkapp.adfurikunsdk.moviereward.**
-dontwarn android.webkit.**

## Support for Adfurikun Movie Reward Unity
-keep interface com.unity3d.player.**
-keep class com.unity3d.player.**
-dontwarn com.unity3d.player.**
-dontnote

## Applovin
-keep interface com.applovin.sdk.**
-keep class com.applovin.** { *; }
-dontwarn com.applovin.**

## Adcolony
-keep class com.adcolony.sdk.** {*;}
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
 # targetSdkVersionが24以下の設定にしている場合は、以下の記述が必要です。
-dontwarn android.app.Activity

## UnityAds
## AARに含まれます。

## Maio
-keep class jp.maio.** { *; }
-keep interface jp.maio.** { *; }
-dontwarn jp.maio.**

##Five
-keep class com.five_corp.ad.** { *; }

## nend
-keep class net.nend.android.** { *; }
-dontwarn net.nend.android.**

## Afio
-keep class com.amoad.** { *; }
-keep interface com.amoad.** { *; }
-dontwarn com.amoad.**

# Vungle
-keep class com.vungle.warren.** { *; }
-dontwarn com.vungle.warren.error.VungleError$ErrorCode

# Moat SDK
-keep class com.moat.** { *; }

# AdMob
-keep class com.google.ads.** { *; }
-keep class com.google.android.gms.ads.** { *; }

# Google Android Advertising ID
-keep class com.google.android.gms.internal.** { *; }
-dontwarn com.google.android.gms.ads.identifier.**
-keep class com.google.android.gms.common.GooglePlayServicesUtil { *; }
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient { *; }
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info { *; }

# Pangle
-keep class com.bytedance.sdk.** { *; }
-keep class com.pgl.sys.ces.* {*;}