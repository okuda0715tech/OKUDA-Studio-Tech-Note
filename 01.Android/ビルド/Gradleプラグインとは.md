- [Gradle プラグインとは](#gradle-プラグインとは)
  - [例](#例)
  - [よく使用されるプラグイン](#よく使用されるプラグイン)
  - [引用元資料](#引用元資料)


# Gradle プラグインとは

Gradle プラグインは、 Gradle に新しい機能を追加する拡張モジュールです。

Gradle 自体は「ビルドを自動化するためのツール」ですが、 Android アプリをビルドしたり、 Kotlin をコンパイルしたりする機能は、プラグインによって追加されています。

つまり、 「 Gradle 自体は何でもビルドできる汎用ツールであり、何をビルドしたいかはプラグインが決める」 という関係になります。


## 例

```kotlin
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}
```

| プラグイン ID                  | 追加される機能                         |
| ------------------------------ | -------------------------------------- |
| `com.android.application`      | Android アプリをビルドできるようになる |
| `org.jetbrains.kotlin.android` | Kotlin を Android で使えるようにする   |

もし、プラグインがなければ

- Android Studio でアプリを作れない
- Kotlin ファイル (.kt) がコンパイルできない
- build.gradle.kts の 「 `android{}` 」 や 「 `dependencies{}` 」 などが使えない

ということになります。


## よく使用されるプラグイン

Android アプリ開発でよく使用されるプラグインには、例えば、以下のものがあります。

| プラグイン例                   | 用途                  |
| ------------------------------ | --------------------- |
| com.android.application        | Android アプリ        |
| com.android.library            | Android ライブラリ    |
| org.jetbrains.kotlin.jvm       | JVM Kotlin アプリ     |
| com.google.gms.google-services | Firebase 設定         |
| kotlin-kapt                    | Annotation Processing |


## 引用元資料

- ChatGPT

