<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [buildgradleの値を参照する](#buildgradleの値を参照する)
  - [例](#例)
  - [キーバリュー形式で定義しなくても良いもの](#キーバリュー形式で定義しなくても良いもの)
<!-- TOC END -->


# buildgradleの値を参照する

AndroidManifest.xml から、 build.gradle 内で定義した値を参照することができます。

## 例

build.gradle には、 `manifestPlaceholders` という名前の連想配列が定義されています。  
この連想配列に対して、 build.gradle 内で、キーとバリューのペアをセットします。

**build.gradle**

```java
android {
  defaultConfig {
    // キー ---> hostName
    // バリュー ---> "www.example.com"
    manifestPlaceholders = [hostName:"www.example.com"]
  }
}
```

マニフェストファイルからは、 `${キー}` で参照することができます。

**AndroidManifest.xml**

```java
<intent-filter>
    <data android:scheme="https" android:host="${hostName}" />
</intent-filter>
```


## キーバリュー形式で定義しなくても良いもの

なかには、最初から build.gradle 内のプロパティとプレースホルダーのキーが紐づいているものがあります。

例えば、アプリケーション ID は、

```java
android {
  buildTypes {
    manifestPlaceholders = [applicationId:"www.example.com"]
  }
}
```

とする必要はありません。  
以下のように、 build.gradle のプロパティに値をセットするだけで、

```java
android {
  buildTypes {
    debug {
      applicationId "aaa.bbb.debug"
    }
  }
}
```

マニフェストファイルからは、あたかもプレースフォルダに値をセットしたかのように参照することができます。

```
<intent-filter ... >
    <action android:name="${applicationId}" />
    ...
</intent-filter>
```

なお、上記の `${applicationId}` は、ビルドタイプ、プロダクトフレーバーの設定を適用した後の  
最終的なアプリケーション ID を得ることができます。
