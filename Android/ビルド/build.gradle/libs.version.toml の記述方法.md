- [libs.version.toml の書き方](#libsversiontoml-の書き方)
  - [サンプルファイル](#サンプルファイル)
  - [\[versions\] セクション](#versions-セクション)
  - [\[libraries\] セクション](#libraries-セクション)
    - [パラメータ](#パラメータ)
    - [参照方法](#参照方法)
  - [\[bundles\] セクション](#bundles-セクション)
  - [\[plugins\] セクション](#plugins-セクション)
    - [参照方法](#参照方法-1)


# libs.version.toml の書き方

`libs.version.toml` は、プロジェクトの依存ライブラリのバージョンを管理するためのファイルです。このファイルを使用したバージョン管理は、 [バージョンカタログを使用した方法](https://developer.android.com/build/dependencies?hl=ja#add-dependency) と呼ばれます。

このファイルは、 TOML（Tom's Obvious, Minimal Language）形式を使用して書かれています。このファイルは、通常 Android プロジェクトで、`build.gradle.kts` ファイルと一緒に使われます。

バージョンカタログを使用した方法では、セントラルリポジトリにアップロードされているモジュールしか取り込めません。ローカルにしか存在しないモジュールは、 `libs.version.toml` は使用せず、 build.gradle に直接記述して取り込む必要があります。


## サンプルファイル

```toml
[versions]
kotlin = "1.8.0"
coroutines = "1.6.1"
room = "2.4.1"
retrofit = "2.9.0"

[libraries]
kotlin-stdlib = { module = "org.jetbrains.kotlin:kotlin-stdlib", version.ref = "kotlin" }
kotlin-coroutines = { module = "org.jetbrains.kotlinx:kotlinx-coroutines-core", version.ref = "coroutines" }
room-runtime = { module = "androidx.room:room-runtime", version.ref = "room" }
room-ktx = { module = "androidx.room:room-ktx", version.ref = "room" }
retrofit2 = { module = "com.squareup.retrofit2:retrofit", version.ref = "retrofit" }

[bundles]
room = ["room-runtime", "room-ktx"]

[plugins]
android-app = "com.android.application"
kotlin-android = "org.jetbrains.kotlin.android"
```


## [versions] セクション

ここでは、依存ライブラリのバージョンを一元管理します。

例えば、`kotlin = "1.8.0"` は、 [libraries] セクションや [plugins] セクションで `version.ref = "kotlin"` を指定した際に、そのライブラリやプラグインのバージョンが、 1.8.0 となります。


## [libraries] セクション

ここでは、各依存ライブラリの詳細を指定します。


### パラメータ

- `module` キー
  - ライブラリのグループ名とアーティファクト ID を指定します。
  - `group:name` という形式で、グループ名（名前空間）とアーティファクト ID をコロン `:` で区切って記述します。
- `group` キー
  - module キーを group と name に分けて記述する場合に使用します。
- `name` キー
  - module キーを group と name に分けて記述する場合に使用します。
- `version.ref` キー
  - [libraries] セクションに定義したバージョンを参照します。
- `version` キー
  - `version.ref` キーを使用しない場合は、 `version` キーに直接バージョンを記述します。

以下の 2 つの記述方法は、使用するキーが異なるだけで、同じ意味となります。

```toml
retrofit2 = { module = "com.squareup.retrofit2:retrofit", version.ref = "retrofit" }

retrofit2 = { group = "com.squareup.retrofit2", name = "retrofit", version.ref = "retrofit" }
```


### 参照方法

```toml
// libs.version.toml

[libraries]
kotlin-stdlib = { module = "org.jetbrains.kotlin:kotlin-stdlib", version.ref = "kotlin" }
```

上記の toml ファイルに記述された kotlin-stdlib の参照方法は、以下のようになります。

```kotlin
// build.gradle.kts

dependencies {
    implementation(libs.kotlin.stdlib)
}
```


## [bundles] セクション

複数のライブラリをグループ化します。例えば、 `room` バンドルは `room-runtime` と `room-ktx` をまとめたものです。


## [plugins] セクション

ここでは、プロジェクトで使用するプラグインを定義します。

- `id` キー
  - プラグイン ID を指定します。
- `version.ref` キー
  - [libraries] セクションに定義したバージョンを参照します。
- `version` キー
  - `version.ref` キーを使用しない場合は、 `version` キーに直接バージョンを記述します。


### 参照方法

```toml
// libs.version.toml

[versions]
agp = "8.5.2"

[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
```

上記の toml ファイルに記述された android-application の参照方法は、次のようになります。

```kotlin
// build.gradle.kts

plugins {
    alias(libs.plugins.android.application)
}
```

これは、 libs.version.toml ファイルを参照しなかった場合は、次のように記述するのと同じ意味になります。

```kotlin
plugins {
    id("com.android.application") version "8.5.2"
}
```


