======================================
Adfurikun Movie Reward SDK for Android
======================================

【SDKのバージョン】
  Ver. 3.20.0

【Androidのバージョン】
  Android 4.0 (API 14) 以上が必要です。

【必須ライブラリ】
  Google Play Services Ads

【構成】
  ・SampleJavaフォルダ
  　　･･･サンプルプロジェクトです。
  ・libsフォルダ
  　　･･･SDKと利用するAdNetworkライブラリ群です。  
  ・README.txt
  　　･･･本ファイルです。

【実装方法】
  1. libsフォルダーをアプリの<root>/app直下にコピーします

  2. <root>/app/build.gradleを設定

    dependencies {
      implementation files('libs/adfurikunMovieRewardSDK-3.20.0.aar')
    }

    apply from: 'libs/adfurikunsdk-adnw-local.gradle'
    apply from: 'libs/adfurikunsdk-adnw-maven.gradle'
    apply from: 'libs/adfurikunsdk-support-legacy.gradle'

  3. adfurikunsdk-support-legacy.gradle中の
    support-libraryやKotlinなどのバージョンを変更し
    御社のプロジェクト設定と合わせる

  4. サンプルプロジェクトのAndroidManifest.xmlの中
    「AdNetwork 設定開始」から「AdNetwork 設定終了」までの内容を
    御社のプロジェクト設定にコピー
     
  詳しくはマニュアルをご参照ください。
  https://docs.adfurikun.jp/movie/android/3.20.0/

【利用規約】
  AppLovin: https://www.applovin.com/eula
  AdColony: https://github.com/AdColony/AdColony-Android-SDK#legal-requirements
  UnityAds: https://unityads.unity3d.com/help/Legal/TOS
  Tapjoy: https://home.tapjoy.com/info/legal/
  Vungle: http://vungle.com/privacy/
  Five: https://www.five-corp.com/privacypolicy/
  SmaAD: https://gmotech.jp/privacy/
  AMoAd: http://www.amoad.com/privacy/
  Maio: https://maio.jp/privacy.html
  Nend: https://nend.net/privacy/sdkpolicy
  FAN: https://developers.facebook.com/docs/audience-network/policy/
