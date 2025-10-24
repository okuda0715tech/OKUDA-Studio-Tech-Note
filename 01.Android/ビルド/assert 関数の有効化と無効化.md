- [assert 関数の有効化と無効化](#assert-関数の有効化と無効化)
  - [デフォルトの設定](#デフォルトの設定)
  - [release ビルドで assert 文を有効化する方法](#release-ビルドで-assert-文を有効化する方法)
    - [特定のメソッドだけを有効化する場合](#特定のメソッドだけを有効化する場合)


# assert 関数の有効化と無効化

## デフォルトの設定

assert 関数は、Android Studio でのアプリ開発において、デフォルトでは debug ビルドで有効 であり、release ビルドでは無効 です。これにより、assert 関数は開発中のデバッグ目的で使用でき、リリース時にはパフォーマンスに影響を与えないようにすることができます。

具体的には、assert 文は ART や Dalvik VM において、android:debuggable が true に設定されている場合にのみ有効です。このフラグは通常、debug ビルドでは true、release ビルドでは false です。


## release ビルドで assert 文を有効化する方法

release ビルドでも assert 文を有効にしたい場合は、build.gradle の設定で proguard-rules.pro に以下のようなルールを追加して、assert を有効にできますが、通常は非推奨です。

```pro
-assumenosideeffects class java.lang.AssertionError {
    // これはこのまま <methods>; と記述してください。
    // そうすることで、 AssertionError を投げる可能性のある
    // 全てのメソッドを有効化します。
    <methods>;
}
```

通常は、assert をデバッグ目的で使用し、リリースビルドでは削除された状態にしておくのが一般的です。


### 特定のメソッドだけを有効化する場合

```pro
-assumenosideeffects class java.lang.AssertionError {
    void printStackTrace();
    void getMessage();
}
```

このようにすると、AssertionError クラス内の printStackTrace() や getMessage() のみが有効になります。


