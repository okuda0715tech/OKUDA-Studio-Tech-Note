<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [自身のアプリアップデートの Broadcast を受け取る](#自身のアプリアップデートの-broadcast-を受け取る)
  - [Manifest.xml](#manifestxml)
  - [build.gradle の versionName が更新されたときに Broadcast を受信する](#buildgradle-の-versionname-が更新されたときに-broadcast-を受信する)
  - [テスト時は、 APK ファイルからアプリアップデートすること](#テスト時は-apk-ファイルからアプリアップデートすること)
<!-- TOC END -->


# 自身のアプリアップデートの Broadcast を受け取る

## Manifest.xml

```xml
<receiver android:name=".AppUpdateReceiver"
    android:exported="true">
    <intent-filter>
        <action android:name="android.intent.action.MY_PACKAGE_REPLACED" />
    </intent-filter>
</receiver>
```


## build.gradle の versionName が更新されたときに Broadcast を受信する

`build.gradle` の `versionName` が更新されたときにのみ `Broadcast` を受信します。

`versionCode` が更新されたときや、 `versionName` が更新されていない場合は、  
`Broadcast` を受信しません。


## テスト時は、 APK ファイルからアプリアップデートすること

原因は不明ですが、 Android Studio の `Run 'app'` ボタンからアプリをインストールしたときは、  
Broadcast を受信することができないことが多々ありました。  
しかし、 APK ファイルからインストールすると確実に受信することができました。
