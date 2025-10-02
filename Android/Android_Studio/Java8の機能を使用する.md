<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [Java8の機能を使用する](#java8の機能を使用する)
	- [参考にしたサイト](#参考にしたサイト)
	- [設定した内容](#設定した内容)

<!-- /TOC -->


# Java8の機能を使用する

## 参考にしたサイト

[Java 8 の言語機能を使う](https://developer.android.com/guide/platform/j8-jack?hl=ja#configuration)

## 設定した内容

compileOptionsの4行分だけ追記しました。

jackOptionsの3行分も追記したところ、コンパイルエラーになったため、削除しました。

```
android {
  ...
  defaultConfig {
    ...

    // 今回jackOptionsは指定しなかった。    
    jackOptions {
      enabled true
    }
  }

  compileOptions {
    sourceCompatibility JavaVersion.VERSION_1_8
    targetCompatibility JavaVersion.VERSION_1_8
  }
}
```


