- [トップレベルの BuildGradle](#トップレベルの-buildgradle)
  - [トップレベルの build.gradle の役割](#トップレベルの-buildgradle-の役割)
  - [plugins {} 関数](#plugins--関数)
    - [概要](#概要)
    - [よく使用するプラグイン](#よく使用するプラグイン)
    - [具体例](#具体例)
  - [buildscript {} 関数](#buildscript--関数)
  - [allprojects {} 関数](#allprojects--関数)
  - [プロジェクトとは](#プロジェクトとは)
  - [subprojects {} 関数](#subprojects--関数)
  - [tasks{} 関数](#tasks-関数)
  - [extra 配列プロパティ](#extra-配列プロパティ)
  - [dependencyResolutionManagement {} 関数](#dependencyresolutionmanagement--関数)
  - [gradle プロパティ](#gradle-プロパティ)
    - [taskGraph](#taskgraph)
    - [startParameter](#startparameter)
    - [](#)
    - [](#-1)
    - [](#-2)
    - [](#-3)
    - [](#-4)


# トップレベルの BuildGradle

## トップレベルの build.gradle の役割

プロジェクトのルートディレクトリにあるトップレベルの build.gradle により、プロジェクトのすべてのモジュールに適用されるビルド設定が定義されます。

これからトップレベルの build.gradle でよく使用される関数やプロパティについて解説していきます。

## plugins {} 関数

### 概要

`plugins{}` 関数は、プロジェクトで使用する [Gradle プラグイン](./Gradleプラグインとは.md) を宣言するブロックです。


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

また、 [Dependabot](../../10.Github/Dependabot/Dependabot.md) によって、依存関係の監視・更新を自動化することができます。


## buildscript {} 関数

**注意** : 現在は、 `buildscript{}` 関数を使用することはほとんどありません。代わりに、 settings.gradle.kts の [pluginManagement](./SettingsGradle.md/#pluginmanagement) と build.gradle.kts の [plugins と libs.versions.toml](#plugins-ブロック) を使用するのが主流です。

`buildscript{}` 関数は、 Gradle プラグインなどの 「アプリをビルドするために必要なツール」 を指定する場所です。それれのツールの取得先となるリモートリポジトリ、ツールの名前、バージョンを指定します。（アプリの中で使用するライブラリを指定する場所ではありません。）

具体例を以下に示します。

```kotlin
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.6.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.0.0")
    }
}
```

現在、 buildscript ブロックを使用する必要があるケースはかなり限定的です。例えば、以下の場合が該当します。

- ビルドスクリプト内で動的にクラスパスを構築する必要がある。
- 自前で Gradle プラグインを開発しており、まだ、リモートリポジトリに公開していない。




## allprojects {} 関数

**注意** : 最近では、 `allprojects{}` の代わりに、 settings.gradle.kts を使用することが一般的であるため、あまり使用されません。

`allprojects{}` 関数を使用すると、すべての [プロジェクト](#プロジェクトとは) (ルートプロジェクト + 全てのサブプロジェクト) に対して有効な設定を行うことができます。

後述する `subprojects{}` との違いは、ルートプロジェクトが含まれるかどうかです。

使用例を以下に示します。

```kotlin
allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
```


## プロジェクトとは

Gradle では、プロジェクトと呼ばれる単位で開発対象を扱います。

Gradle のプロジェクトとは、 build.gradle.kts ファイルを持つ単位です。例えば、以下の構成の場合は、 3 つのプロジェクトが存在することになります。

```
MyProject/
 ├── build.gradle.kts        ← トップレベルプロジェクト（ルートプロジェクト）
 ├── settings.gradle.kts
 ├── app/                    ← サブプロジェクト1
 │    └── build.gradle.kts
 └── library/                ← サブプロジェクト2
      └── build.gradle.kts
```

さて、上記の構成の場合、 `app` や `library` は、「 [モジュール](../Android%20Developers%20公式ドキュメントの翻訳と補足/設計とプランニング/アプリアーキテクチャ/2.モジュール化/モジュールとライブラリ.md/#モジュールとは) 」 という単位ではないか？と思うはずです。実は、 `app` や `library` は、モジュールでもあり、プロジェクトでもあります。 Android では、モジュールと呼び、 Gradle では、プロジェクトと呼びます。ただし、ルートプロジェクトについては、モジュールと呼ぶことはありません。 Android では、ビルドの単位をモジュールと呼びますが、ルートプロジェクト自身をビルドすることはできないためです。 (【参考】 `.apk` や `.aab` は、 app モジュールをビルドして生成されるものです。)

まとめると、以下のようになります。

- 「プロジェクト」は Gradle における単位、「モジュール」は Android における単位です。
- 「プロジェクト」と「モジュール」は、ほぼ同じ意味で扱われます。
- ただし、ルートプロジェクトをモジュールと呼ぶことはありません。

先ほどの例では、以下のようになります。

```
MyProject/
 ├── build.gradle.kts        ← トップレベルプロジェクト（モジュールとは呼ばない）
 ├── settings.gradle.kts
 ├── app/                    ← サブプロジェクト1（モジュールと呼ぶ）
 │    └── build.gradle.kts
 └── library/                ← サブプロジェクト2（モジュールと呼ぶ）
      └── build.gradle.kts
```


## subprojects {} 関数

**注意** : 最近では、 `subprojects{}` の代わりに、 settings.gradle.kts を使用することが一般的であるため、あまり使用されません。

`subprojects{}` 関数を使用すると、ルートプロジェクトを除く全てのサブ [プロジェクト](#プロジェクトとは) に対して有効な設定を行うことができます。

前述の `allprojects{}` との違いは、ルートプロジェクトが含まれるかどうかです。

使用例は、 [allprojects{} 関数](#allprojects--関数) の場合と同様のため、省略します。


## tasks{} 関数

Gradle のタスクを定義したり、変更したりできます。詳細は、 [タスク.md](./タスク.md) を参照してください。


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

**注意** : `extra` 配列プロパティは `Any` 型であるため、使用時にキャストが必要です。

Groovy の名残で `ext{}` ブロックが使用されることもあります。

```kotlin
ext {
    set("composeVersion", "1.6.0")
    set("kotlinVersion", "1.9.10")
}
```

基本的には、 Kotlin DSL に移行しているのであれば、 extra 配列プロパティを使用するようにしましょう。


## dependencyResolutionManagement {} 関数

**注意** : 最新の Gradle では、 build.gradle.kts ではなく、 settings.gradle.kts で dependencyResolutionManagement を定義することが推奨されます。

dependencyResolutionManagement については、 [SettingsGradle.md](./SettingsGradle.md) を参照してください。


## gradle プロパティ

`gradle` は、現在のビルド全体を表す Gradle オブジェクト（Gradle インスタンス） への参照です。つまり、「この Gradle 実行プロセス全体で共通の設定」を行うときに使います。

### taskGraph

`gradle.taskGraph` は、タスクの依存関係グラフに関する情報を保持します。

実行対象のタスクを確認したり、タスクの実行前後に処理を追加できます。

```kotlin
gradle.taskGraph.whenReady { graph ->
    println("タスクグラフが構築されました。実行予定タスクは以下になります。")
    graph.allTasks.forEach { task ->
        println(" - ${task.path}")
    }
}

gradle.taskGraph.beforeTask { task ->
    println("タスクを開始: ${task.path}")
}

gradle.taskGraph.afterTask { task, state ->
    if (state.failure != null) {
        println("× タスク失敗: ${task.path}")
    } else {
        println("○ タスク成功: ${task.path}")
    }
}
```

タスクの流れや実行結果をデバッグしたいときなどに役立ちます。


### startParameter








### 
### 
### 
### 
### 






