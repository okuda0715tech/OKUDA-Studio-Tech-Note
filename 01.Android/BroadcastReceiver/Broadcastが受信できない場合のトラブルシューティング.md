<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [Broadcast が受信できない場合のトラブルシューティング](#broadcast-が受信できない場合のトラブルシューティング)
  - [Manifest ファイルに receiver タグが設定されているか](#manifest-ファイルに-receiver-タグが設定されているか)
  - [マニフェストに適切なパーミッションが設定されているか](#マニフェストに適切なパーミッションが設定されているか)
  - [アプリが STOP 状態になっていないか](#アプリが-stop-状態になっていないか)
    - [暗黙的 Intent の送信側のアプリを自分が作成している場合](#暗黙的-intent-の送信側のアプリを自分が作成している場合)
    - [暗黙的 Intent の送信側のアプリを自分が作成していない場合](#暗黙的-intent-の送信側のアプリを自分が作成していない場合)
  - [APK ファイルからインストールしてみる](#apk-ファイルからインストールしてみる)
<!-- TOC END -->


# Broadcast が受信できない場合のトラブルシューティング

Broadcast が受信できない場合は、以下のポイントを確認してください。


## Manifest ファイルに receiver タグが設定されているか

`<receiver>` タグが設定されていない場合は、受信することができません。

以下はサンプルです。

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

  <application>

    <receiver
        android:name=".receiver.alarm.AutoTweetExecuteReceiver"
        android:enabled="true"
        android:exported="true" />

  </application>

</manifest>
```


## マニフェストに適切なパーミッションが設定されているか

例えば、デバイスが起動したことを知らせるブロードキャストを受信する場合には、  
以下のパーミッションが必要です。

```xml
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
```


## アプリが STOP 状態になっていないか

アプリが STOP 状態になっている場合は、暗黙的 Intent を受信することができません。

### 暗黙的 Intent の送信側のアプリを自分が作成している場合

暗黙的 Intent の送信側のアプリを自分が作成している場合には、 Intent に  
`FLAG_INCLUDE_STOPPED_PACKAGES` フラグを設定することで、受信が可能になります。

または、明示的 Intent を使用するように変更します。


### 暗黙的 Intent の送信側のアプリを自分が作成していない場合

暗黙的 Intent の送信側のアプリを自分が作成していない場合には、 STOP 状態を解除するしか  
ありません。 STOP 状態を解除する契機や STOP 状態になる契機は、別紙  
「アプリの STOP 状態について」 を参照してください。  
別紙は、 Intent フォルダ内に格納されています。


## APK ファイルからインストールしてみる

原因は不明ですが、かつて、 `MY_PACKAGE_REPLACED` の Broadcast を受信しようとした時に、  
Android Studio の `Run 'app'` ボタンからアプリをインストールしたときは、  
該当の Broadcast を受信することができませんでした。  
しかし、 APK ファイルからインストールすると受信することができました。

APK ファイルからのインストールも試してみる価値がありそうです。
