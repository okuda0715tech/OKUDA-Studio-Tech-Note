<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [MY_PACKAGE_REPLACED が受信できずに苦戦した話](#my_package_replaced-が受信できずに苦戦した話)
  - [アプリが STOP 状態の場合は受信できない](#アプリが-stop-状態の場合は受信できない)
  - [Android Studio の Run App ボタンでインストールすると受信しないときがある](#android-studio-の-run-app-ボタンでインストールすると受信しないときがある)
<!-- TOC END -->


# MY_PACKAGE_REPLACED が受信できずに苦戦した話

## アプリが STOP 状態の場合は受信できない

STOP 状態については、別紙 「アプリの STOP 状態について」 を参照してください。  
上記の別紙は、 Intent フォルダ内にあります。


## Android Studio の Run App ボタンでインストールすると受信しないときがある

Android Studio の Run App ボタンでアプリをインストールすると受信しないときがあります。  
受信する場合もあるため、詳細は不明です。

しかし、受信しない時でも、 APK ファイルを端末に保存して、そこからアプリをアップデートしたら  
確実に受信することができたため、テストでは、 APK ファイルからアップデートをするようにしましょう。
