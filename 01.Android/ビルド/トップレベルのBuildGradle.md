- [トップレベルの BuildGradle](#トップレベルの-buildgradle)
  - [トップレベルの build.gradle の役割](#トップレベルの-buildgradle-の役割)
  - [plugins{}](#plugins)
    - [概要](#概要)
    - [よく使用するプラグイン](#よく使用するプラグイン)
    - [具体例](#具体例)
  - [extra 配列プロパティ](#extra-配列プロパティ)
  - [subprojects {} 関数](#subprojects--関数)
  - [allprojects {} 関数](#allprojects--関数)
  - [参考資料](#参考資料)


# トップレベルの BuildGradle

## トップレベルの build.gradle の役割

プロジェクトのルートディレクトリにあるトップレベルの build.gradle により、プロジェクトのすべてのモジュールに適用されるビルド設定が定義されます。

これからトップレベルの build.gradle でよく使用される関数やプロパティについて解説していきます。

## plugins{}

### 概要

plugins{} ブロックは、プロジェクトで使用する [Gradle プラグイン](./Gradleプラグインとは.md) を宣言するブロックです。


### よく使用するプラグイン

Android アプリ開発では、以下のプラグインをよく使用します。

- `com.android.application` （アプリモジュール用）
- `com.android.library` （ライブラリモジュール用）
- `org.jetbrains.kotlin.android` （Kotlin + Android 用）


### 具体例

話を簡単にするために、まずは、古い記述方法を解説します。

```kotlin
plugins {
    id("com.android.application") version "8.2.0" apply false
    kotlin("android") version "1.9.10" apply false
}
```

`apply false` を指定することにより、トップレベルのモジュールではプラグインは使えません。サブモジュールで使う場合に、トップレベルで統一されたバージョンを定義するために使用します。

最新の記述方法では、以下のようになります。

```kotlin
// トップレベル build.gradle.kts
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
}
```

```kotlin
// libs.versions.toml（バージョンカタログ）
[versions]
agp = "8.2.0"
kotlin = "1.9.10"

[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }
```

こうすることによって、複数のモジュールで同じプラグインを使用する場合に、バージョンがバラバラになることを防ぐことができます。

また、 Dependabot によって、依存関係の監視・更新を自動化することができます。







## extra 配列プロパティ

extra 配列プロパティを使用すると、トップレベルの build.gradle で定義したプロパティをモジュールレベルの build.gradle から参照することができます。

```kotlin
// トップレベル build.gradle.kts
extra["composeVersion"] = "1.6.0"
extra["compileSdk"] = 34
```

```kotlin
// モジュールレベル build.gradle.kts
android {
    compileSdk = rootProject.extra["compileSdk"] as Int

    composeOptions {
        kotlinCompilerExtensionVersion =
            rootProject.extra["composeVersion"] as String
    }
}
```

**注意** : Extra Properties は Any 型なので、使用時にキャストが必要です

Groovy の名残で `ext{}` ブロックが使用されることもあります。

```kotlin
ext {
    set("composeVersion", "1.6.0")
    set("kotlinVersion", "1.9.10")
}
```

基本的には、 Kotlin DSL に移行しているのであれば、 extra 配列プロパティを使用するようにしましょう。


## subprojects {} 関数



## allprojects {} 関数



## 参考資料

- ChatGPT


