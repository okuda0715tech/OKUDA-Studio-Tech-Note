- [モジュールレベルの BuildGradle](#モジュールレベルの-buildgradle)
  - [モジュールレベルの build.gradle の役割](#モジュールレベルの-buildgradle-の役割)
  - [モジュールレベルの build.gradle でよく使用される関数やプロパティ](#モジュールレベルの-buildgradle-でよく使用される関数やプロパティ)
  - [project](#project)
  - [plugins](#plugins)
  - [android](#android)
  - [dependencies](#dependencies)
  - [repositories](#repositories)
  - [](#)
  - [](#-1)
  - [](#-2)
  - [モジュールレベルよりもトップレベルの内容を優先したい場合](#モジュールレベルよりもトップレベルの内容を優先したい場合)


# モジュールレベルの BuildGradle

## モジュールレベルの build.gradle の役割

各 `project_root/module_name/` ディレクトリに作成されるモジュールレベルの build.gradle ファイルを使用すると、そのモジュール固有のビルド設定を行うことができます。

自分自身のモジュール内のマニフェストファイルをビルド時に動的にオーバーライドすることが可能です。

トップレベルの build.gradle ファイルとモジュールレベルの build.gradle ファイルで異なる設定がされている場合には、基本的には、モジュールレベルの内容が優先されます。トップレベルの内容を優先する方法は後述します。


## モジュールレベルの build.gradle でよく使用される関数やプロパティ

これからモジュールレベルの build.gradle でよく使用される関数やプロパティについて解説していきます。


## project

`project` プロパティは、現在のプロジェクト（編集中の build.gradle.kts のプロジェクト）を表す Project インスタンスを表します。

詳細は [Projectオブジェクト.md](./Projectオブジェクト/Projectオブジェクト.md) を参照してください。


## plugins

`plugins{}` 関数は、プラグインを適用するブロックです。

```kotlin
plugins {
    id("com.android.application")
    kotlin("android")
}
```


## android

`android{}` 関数は、 Android プラグインを適用したときに使える設定ブロックです。 Android アプリのビルドに必要な設定を行います。

```kotlin
android {
    namespace = "com.example.app"
    compileSdk = 34
}
```


## dependencies

`dependencies{}` 関数は、使用するライブラリの依存関係を定義するブロックです。

```kotlin
dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    testImplementation("junit:junit:4.13.2")
}
```


## repositories

`repositories{}` 関数は、記述する場所によって、意味が変わります。

- `pluginManagement{}` ブロック内で記述した場合
  - Gradle プラグインの取得先のリモートリポジトリを定義します。
- build.gradle.kts ファイルのトップレベルで記述した場合
  - ライブラリの取得先のリモートリポジトリを定義します。

`pluginManagement{}` ブロックは、 settings.gradle.kts かルートプロジェクトの build.gradle.kts の内部でのみ使用できます。ただし、近年の思想では、 settings.gradle.kts 内部で使用することが推奨されます。

使用例を以下に示します。

```kotlin
// app/build.gradle.kts

// ライブラリの取得先を定義
repositories {
    google()
    mavenCentral()
}
```

```kotlin
// settings.gradle.kts

pluginManagement {
    // Gradle プラグインの取得先を定義
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
```


## 


```kotlin
```


## 


```kotlin
```


## 



```kotlin
```






## モジュールレベルよりもトップレベルの内容を優先したい場合






