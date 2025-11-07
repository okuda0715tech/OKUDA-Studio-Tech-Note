- [logger プロパティ](#logger-プロパティ)
  - [概要](#概要)
  - [出力内容の例](#出力内容の例)
  - [ログレベルの種類と違い](#ログレベルの種類と違い)
  - [使用例](#使用例)


# logger プロパティ

## 概要

Gradle の「標準出力（コンソール）」に、ライフサイクルレベル（＝通常の情報レベル）のログメッセージを出力します。 Logcat ではなく、 Build コンソールにログが出力されるのが特徴です。

開発者がビルドの進行状況やカスタムタスクの動作を確認するためによく使うログ出力です。


## 出力内容の例

たとえば app というサブプロジェクトの build.gradle.kts に次のように書いたとします：

```kotlin
logger.lifecycle("Building ${project.name}")
```

Gradle 実行時 (例 : `./gradlew assemble` ) のコンソール出力に次のような行が表示されます。

```ruby
> Task :app:preBuild UP-TO-DATE
Building app
> Task :app:assemble
BUILD SUCCESSFUL in 2s
```

つまり、 `println` のように標準出力へ出すのではなく、 Gradle のログシステムを通して出力されるのがポイントです。


## ログレベルの種類と違い

Gradle の logger は、 org.gradle.api.logging.Logger インターフェースを実装しており、以下のログレベルがあります。

| メソッド      | レベル    | 表示される条件                 | 主な用途                               |
| ------------- | --------- | ------------------------------ | -------------------------------------- |
| `quiet()`     | QUIET     | 常に表示                       | 最低限のメッセージ。`--quiet` でも出る |
| `lifecycle()` | LIFECYCLE | デフォルトで表示               | 普通の進行状況や結果など               |
| `info()`      | INFO      | `--info` オプション時のみ表示  | 詳細な情報                             |
| `debug()`     | DEBUG     | `--debug` オプション時のみ表示 | デバッグ用詳細情報                     |
| `warn()`      | WARN      | 常に表示                       | 警告メッセージ                         |
| `error()`     | ERROR     | 常に表示                       | エラー内容                             |


## 使用例

```kotlin
tasks.register("hello") {
    doLast {
        logger.lifecycle("Hello from ${project.name}")
    }
}
```

実行結果

```ruby
> Task :app:hello
Hello from app
```

