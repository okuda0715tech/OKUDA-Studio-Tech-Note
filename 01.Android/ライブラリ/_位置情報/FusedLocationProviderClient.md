<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [FusedLocationProviderClient](#fusedlocationproviderclient)
	- [サンプルアプリ](#サンプルアプリ)
	- [build.gradleの追加](#buildgradleの追加)
	- [Google Play Services APK の有効性チェック](#google-play-services-apk-の有効性チェック)
	- [パーミッション宣言](#パーミッション宣言)
	- [ビルドバージョンがAndroid6.0(SDK 23)以上の場合実行時にユーザーからパーミッションの許可を得る](#ビルドバージョンがandroid60sdk-23以上の場合実行時にユーザーからパーミッションの許可を得る)

<!-- /TOC -->

# FusedLocationProviderClient

## サンプルアプリ

一定感覚ごとに位置情報を取得してアプリに緯度経度を表示してくれるアプリ。
動作確認済み。結構出来がいいので参考になる。

[sakurabird/Android-Fused-location-provider-example](https://github.com/sakurabird/Android-Fused-location-provider-example)

上記サンプルアプリ作成時に参考にしたアプリが以下のようである。

[googlesamples/android-play-location](https://github.com/googlesamples/android-play-location)


## build.gradleの追加

```
dependencies {
    implementation 'com.google.android.gms:play-services-location:17.0.0'
}
```

`Google Play services(Google Play開発者サービス)`には、たくさんのAPIが格納されているので、上記のようにして`location`のみを取り込みます。不要な機能を取り込みすぎると、1アプリ内のメソッド定義最大数(65536)を超えてしまい、コンパイルできなくなります。


## Google Play Services APK の有効性チェック

次のように記述すると、ユーザーの端末のGoogle Play Service apkが有効かどうかチェックして、エラー内容に合わせたダイアログを表示してくれます。

```Java
int resultCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
if (resultCode != ConnectionResult.SUCCESS) {
    GoogleApiAvailability.getInstance().getErrorDialog((Activity) this, resultCode, request_code).show();
}
```


## パーミッション宣言

ACCESS_FINE_LOCATIONはGPS_PROVIDERとNETWORK_PROVIDERを使用する場合に指定します。
ACCESS_COARSE_LOCATIONはNETWORK_PROVIDERのみを使用する場合に指定します。
ACCESS_FINE_LOCATIONを指定したら、ACCESS_COARSE_LOCATIONの指定は必要ありません。

```Xml
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
```


## ビルドバージョンがAndroid6.0(SDK 23)以上の場合実行時にユーザーからパーミッションの許可を得る


TODO




