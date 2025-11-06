- [startParameter](#startparameter)
  - [taskNames: List\<String\>](#tasknames-liststring)
  - [projectProperties: Map\<String, String\>](#projectproperties-mapstring-string)
  - [isOffline: Boolean](#isoffline-boolean)
  - [isBuildCacheEnabled: Boolean](#isbuildcacheenabled-boolean)
  - [isParallelProjectExecutionEnabled: Boolean](#isparallelprojectexecutionenabled-boolean)
  - [maxWorkerCount: Int](#maxworkercount-int)
  - [excludedTaskNames: Set\<String\>](#excludedtasknames-setstring)
  - [isDryRun: Boolean](#isdryrun-boolean)
  - [isProfile: Boolean](#isprofile-boolean)
  - [showStacktrace: ShowStacktrace](#showstacktrace-showstacktrace)
  - [logLevel: LogLevel](#loglevel-loglevel)
  - [isContinueOnFailure: Boolean](#iscontinueonfailure-boolean)
  - [buildFile: File?](#buildfile-file)
  - [settingsFile: File?](#settingsfile-file)


# startParameter

`gradle.startParameter` は、Gradle の起動時に指定されたすべての実行パラメータ（オプションや設定） を表すオブジェクトです。

つまり、「 `./gradlew ...` をどう実行したか」の情報が全部ここに入っています。

以下では、 gradle.startParameter の主なプロパティと関数を中心に説明します。


## taskNames: List\<String\>

実行対象のタスク名リスト

```kotlin
println(gradle.startParameter.taskNames)
// 例: ["assembleRelease", "testDebugUnitTest"]
```


## projectProperties: Map\<String, String\>

`-Pkey=value` で指定されたプロジェクトプロパティ。

```kotlin
println(gradle.startParameter.projectProperties)
// 例: {apiKey=abcdef12345, buildType=release}
```


## isOffline: Boolean

`--offline` オプションが付いているかどうか。

```kotlin
if (gradle.startParameter.isOffline) {
    println("📡 オフラインモードでビルドしています。")
}
```


## isBuildCacheEnabled: Boolean

`--build-cache` オプションの有効／無効。

```kotlin
println("ビルドキャッシュ: ${gradle.startParameter.isBuildCacheEnabled}")
```


## isParallelProjectExecutionEnabled: Boolean

`--parallel` オプションで並列ビルドが有効か。

```kotlin
if (gradle.startParameter.isParallelProjectExecutionEnabled) {
    println("🚀 並列ビルドが有効です")
}
```


## maxWorkerCount: Int

`--max-workers` で指定されたワーカースレッド数。

```kotlin
println("🧵 使用ワーカー数: ${gradle.startParameter.maxWorkerCount}")
```


## excludedTaskNames: Set\<String\>

`-x taskName` で除外されたタスクの一覧。

```kotlin
println("除外タスク: ${gradle.startParameter.excludedTaskNames}")
```


## isDryRun: Boolean

`--dry-run` (実行せずシミュレーションのみ) 指定の確認。

```kotlin
if (gradle.startParameter.isDryRun) {
    println("🧪 ドライランモードで実行中（実際のタスクは実行されません）")
}
```


## isProfile: Boolean

`--profile` オプション（ビルド時間のプロファイル出力）指定の確認。

```kotlin
println("プロファイル出力: ${gradle.startParameter.isProfile}")
```


## showStacktrace: ShowStacktrace

エラー発生時のスタックトレース出力レベル。

```kotlin
println("スタックトレース出力設定: ${gradle.startParameter.showStacktrace}")
```

（ShowStacktrace.ALWAYS, ShowStacktrace.INTERNAL_EXCEPTIONS, などの列挙値）


## logLevel: LogLevel

`--info, --debug, --quiet` などのログレベル。

```kotlin
println("ログレベル: ${gradle.startParameter.logLevel}")
```


## isContinueOnFailure: Boolean

`--continue` オプション指定時、タスク失敗後も続行するか。

```kotlin
if (gradle.startParameter.isContinueOnFailure) {
    println("⚠️ 失敗しても続行モードで実行中")
}
```


## buildFile: File?

`-b` オプションで指定されたビルドスクリプトファイル（通常は null）。


## settingsFile: File?

`-c` オプションで指定された settings.gradle ファイル（通常は null）。



