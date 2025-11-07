- [トップレベルの BuildGradle](#トップレベルの-buildgradle)
  - [トップレベルの build.gradle の役割](#トップレベルの-buildgradle-の役割)
  - [plugins {} 関数](#plugins--関数)
    - [概要](#概要)
    - [よく使用するプラグイン](#よく使用するプラグイン)
    - [具体例](#具体例)
  - [buildscript {} 関数](#buildscript--関数)
  - [allprojects {} 関数](#allprojects--関数)
    - [project プロパティ](#project-プロパティ)
  - [プロジェクトとは](#プロジェクトとは)
  - [subprojects {} 関数](#subprojects--関数)
    - [project プロパティ](#project-プロパティ-1)
  - [tasks{} 関数](#tasks-関数)
  - [extra 配列プロパティ](#extra-配列プロパティ)
  - [dependencyResolutionManagement {} 関数](#dependencyresolutionmanagement--関数)
  - [gradle プロパティ](#gradle-プロパティ)
    - [taskGraph](#taskgraph)
    - [startParameter](#startparameter)
    - [rootProject](#rootproject)
    - [buildFinished {}](#buildfinished-)
    - [beforeProject {}](#beforeproject-)
    - [afterProject {}](#afterproject-)
  - [rootProject プロパティ](#rootproject-プロパティ)
    - [ルートプロジェクトのディレクトリを参照](#ルートプロジェクトのディレクトリを参照)
    - [ルートプロジェクトの設定値を子プロジェクトで利用](#ルートプロジェクトの設定値を子プロジェクトで利用)


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


### project プロパティ

`allprojects { ... }` ブロック内で `project` プロパティを参照すると、そのブロックが現在処理している各プロジェクトの `Project` インスタンスが取得されます。


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


### project プロパティ

`subprojects { ... }` ブロック内で `project` プロパティを参照すると、そのブロックが現在処理している各サブプロジェクトの `Project` インスタンスが取得されます。


## tasks{} 関数

Gradle のタスクを定義したり、変更したりできます。詳細は、 [タスク.md](./タスク.md) を参照してください。


## extra 配列プロパティ

extra 配列プロパティを使用すると、プロジェクト間でプロパティを共有することが可能です。つまり、 build.gradle.kts 内で設定した値が、別の build.gradle.kts 内から参照することができます。

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

Kotlin のプロパティデリゲートを使用して、値を取得することも可能です。この場合は、プロパティ名 `val composeVersion` が `extra` 配列のキーとして使用されます。

```kotlin
val composeVersion: String by extra
println(composeVersion) // "1.6.0"
```

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

この章で説明する各コールバックの実行タイミングが、ビルド全体のどのフェーズに該当するのかについて、 [Gradleによるビルドの3フェーズ.md](./Gradleによるビルドの3フェーズ.md) で説明しているため、必要に応じてそちらも参照してください。


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

Gradle を実行するときに指定されたコマンドライン引数やオプションが格納されています。（例 : `./gradlew build --offline --parallel` など）

使用例を以下に示しますが、様々なプロパティが用意されているため、詳細は [startParameter.md](./gradleプロパティ/startParameter.md) を参照してください。

```kotlin
println("🚀 Gradle 実行パラメータ情報")
println("タスク名: ${gradle.startParameter.taskNames}")
println("オフラインモード: ${gradle.startParameter.isOffline}")
println("並列実行: ${gradle.startParameter.isParallelProjectExecutionEnabled}")
println("プロジェクトプロパティ: ${gradle.startParameter.projectProperties}")
```

CI / CD 環境で実行条件を判定したり、特定のオプション付きビルドだけ動作を変えるときに便利です。


### rootProject

ビルド全体のルートプロジェクトを参照します。どのサブプロジェクトからでも `gradle.rootProject` と記述することでアクセス可能です。

使用例を以下に示します。

```kotlin
println("ルートプロジェクトの名前: ${gradle.rootProject.name}")
println("ルートプロジェクトの場所: ${gradle.rootProject.projectDir}")

tasks.register("copyToRoot") {
    doLast {
        val dest = file("${gradle.rootProject.projectDir}/collected_outputs")
        dest.mkdirs()
        println("コピー先: $dest")
    }
}
```

ルートに成果物やログをまとめたいときに使われます。


### buildFinished {}

ビルドが 完全に終了したタイミング（成功・失敗問わず） に呼び出されます。後処理やレポート出力などに最適です。

使用例を以下に示します。

```kotlin
gradle.buildFinished { result ->
    if (result.failure != null) {
        println("ビルド失敗: ${result.failure?.message}")
    } else {
        println("ビルド成功！（所要時間: ${result.elapsedTime}）")
    }
}
```

Slack 通知やログ送信などを追加したい場合にも使えます。


### beforeProject {}

各プロジェクトの設定が開始される前に呼ばれます。ルートプロジェクトの `build.gradle.kts` に `beforeProject{}` を記述するだけで、サブプロジェクトを含むすべてのプロジェクトの設定の開始前にコールバックが呼ばれます。

使用例を以下に示します。

```kotlin
// ルートプロジェクトの build.gradle.kts
gradle.beforeProject { project ->
    println("設定開始 >>> ${project.name}")
}

gradle.afterProject { project ->
    println("設定完了 <<< ${project.name}")
}
```

```
設定開始 >>> MyApp
設定完了 <<< MyApp
設定開始 >>> app
設定完了 <<< app
設定開始 >>> library
設定完了 <<< library
```


### afterProject {}

各プロジェクトの設定が完了した後に呼ばれます。 `beforeProject {}` と同様に、ルートプロジェクトの `build.gradle.kts` に `beforeProject{}` を記述するだけで、サブプロジェクトを含むすべてのプロジェクトの設定の開始前にコールバックが呼ばれます。

使用例を以下に示します。

```kotlin
gradle.afterProject { project, state ->
    if (state.failure != null) {
        println("${project.name} の設定中にエラーが発生: ${state.failure?.message}")
    }
}
```

以下の用途で使用されることが多いです。

- 各モジュールの設定状況をログ出力する
- 設定が終わった後に共通の設定を上書き／確認する
- 設定エラーを一元管理する


## rootProject プロパティ

rootProject は、その名の通り ルートプロジェクト（最上位ディレクトリ） を表すプロパティです。マルチモジュール構成のときに「ルートプロジェクト」を参照する際に使用されます。

以下に使用例を示します。


### ルートプロジェクトのディレクトリを参照

```kotlin
tasks.register<Copy>("copyApkToRoot") {
    from("$buildDir/outputs/apk/release")
    into("${rootProject.projectDir}/releaseApks")
}
```

app モジュールからルートディレクトリ配下に成果物をコピーしたい場合などに使います。


### ルートプロジェクトの設定値を子プロジェクトで利用

```kotlin
// ルート build.gradle.kts
extra["compileSdkVersion"] = 35

// app/build.gradle.kts
android {
    compileSdk = rootProject.extra["compileSdkVersion"] as Int
}
```

`rootProject.extra` に値を入れておくことで、全モジュールで共通設定を使えるようにします。







