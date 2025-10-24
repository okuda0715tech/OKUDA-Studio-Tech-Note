<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [RemoteConfig 実装編](#remoteconfig-実装編)
  - [Gradle への追加](#gradle-への追加)
  - [パラメータを取得する](#パラメータを取得する)
  - [サーバー側のパラメータを設定する](#サーバー側のパラメータを設定する)
    - [Firebase コンソールを使用して設定する](#firebase-コンソールを使用して設定する)
<!-- TOC END -->


# RemoteConfig 実装編

## Gradle への追加

```java
dependencies {
    // BoM を指定することで、各ライブラリごとにバージョンを指定する必要がない。
    implementation platform('com.google.firebase:firebase-bom:29.3.0')

    // firebase Remote Config
    implementation 'com.google.firebase:firebase-config'
    // Google アナリティクス
    // ユーザープロパティとオーディエンスを対象としたアプリインスタンスの条件付き
    // ターゲティングを行うには、 Google アナリティクスが必要です。
    implementation 'com.google.firebase:firebase-analytics'
}
```


## パラメータを取得する

```java
String key1 = "key1";
String key2 = "key2";
String key3 = "key3";
String key4 = "key4";
mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
boolean val1 = mFirebaseRemoteConfig.getBoolean(key1);
double val2 = mFirebaseRemoteConfig.getDouble(key2);
long val3 = mFirebaseRemoteConfig.getLong(key3);
String val4 = mFirebaseRemoteConfig.getString(key4);
```


## サーバー側のパラメータを設定する

### Firebase コンソールを使用して設定する

Firebase コンソールのメニューから 「エンゲージメント」 -> 「Remote Config」 を選択します。
