- [Project オブジェクト](#project-オブジェクト)
  - [Project オブジェクトのメンバプロパティ](#project-オブジェクトのメンバプロパティ)
  - [Project オブジェクトの省略](#project-オブジェクトの省略)
  - [Project オブジェクトのメンバ関数](#project-オブジェクトのメンバ関数)
    - [project()](#project)
    - [file()](#file)
    - [fileTree()](#filetree)


# Project オブジェクト

## Project オブジェクトのメンバプロパティ

主な Project オブジェクトのメンバプロパティを紹介します。

- name: String
  - サブプロジェクト名。
  - 例 : `println(name)` → "app" など。

- path: String
  - ルートからのパス
  - `:app` のような形式です。

- projectDir: File / buildDir: File
  - 各サブプロジェクトのソースコードフォルダ・ビルド成果物フォルダ
  - 例 : `println(buildDir)` → .../app/build

- rootProject: Project
  - ルートプロジェクト（最上位）の Project インスタンス。

- logger: Logger
  - ログ出力に使う。
  - 例 : `logger.lifecycle("Building ${project.name}")`
  - 例 : `logger.warn("This is a warning message")`
  - 詳細は [loggerプロパティ.md](./loggerプロパティ.md) を参照してください。

- group: String
  - Maven などで使用するグループ ID

- version: Any
  - プロジェクトのバージョン

- tasks: TaskContainer
  - タスクを管理するコンテナ
  - 詳細は、 [タスク.md](../タスク.md) を参照してください。

- dependencies: DependencyHandler
  - ライブラリやモジュールの依存関係を登録するためのハンドラ
  - `dependencies{}` ブロックを使用して依存関係を定義します。
  - 詳細は、 [ライブラリの依存関係.md](../ライブラリの依存関係.md) を参照してください。

- repositories: RepositoryHandler
  - リポジトリ設定用

- extensions: ExtensionContainer
  - 拡張機能（ `android` など）へのアクセス

- layout: ProjectLayout
  - ディレクトリやファイルを安全に扱うためのレイアウトサービス
  - 詳細は [layoutプロパティ.md](./layoutプロパティ.md) を参照してください。


## Project オブジェクトの省略

build.gradle.kts のトップレベルで Project オブジェクトのメンバーにアクセスする際は、 Project オブジェクトを省略することが可能です。

つまり、例えば、 `project.name` のように記述せず、 `name` と記述することが可能です。

これは、 Project のインスタンスを this として暗黙的に参照してるためです。

ただし、省略できない場合もあるため、注意してください。例えば、以下の場合は、 Project オブジェクトの name ではなく、 Task オブジェクトの name を取得します。

```kotlin
tasks.register("myTask") {
    println(name) // → "myTask"
}
```


## Project オブジェクトのメンバ関数

### project()

`project()` 関数は、他のサブプロジェクトを参照する際に使用します。

```kotlin
dependencies {
    implementation(project(":core"))
}
```


### file()

`file(path: Any): File` 関数は、引数で指定されたパスの File オブジェクトを返します。

引数のパスは、 file() 関数のレシーバーが示す Project のルートからの相対パスになります。

```kotlin
val srcDir = file("src/main/java")
// → 実際のパス: /yourproject/src/main/java
```


### fileTree()

`fileTree()` 関数は、複数のファイルを扱いたい場合に使用する関数です。

シグネチャは以下の 2 種類が存在します。

```kotlin
fun fileTree(baseDir: Any): ConfigurableFileTree
fun fileTree(args: Map<String, *>): ConfigurableFileTree
```

様々な使用方法が存在するため、代表的な使用例を以下に示します。

| 目的                         | 記述例                                                            |
| ---------------------------- | ----------------------------------------------------------------- |
| 単純にディレクトリ全体を取得 | `fileTree("src/main/java")`                                       |
| ファイル種を絞る             | `fileTree("src") { include("**/*.kt") }`                          |
| 複数ツリーを結合             | `files(fileTree("a"), fileTree("b"))`                             |
| Copy タスクなどで使う        | `from(fileTree("src/resources"))`                                 |
| 高度な指定（Map形式）        | `fileTree(mapOf("dir" to "src", "include" to listOf("**/*.kt")))` |

上記の記述例で、 `**` のようなワイルドカードの記述がありますが、これは、 Ant 形式によるファイルマッチングとなります。 Ant 形式とは、簡単に言うと、 「ファイルのマッチングに特化した正規表現」 のことです。通常の正規表現とは表現方法が異なります。ファイルマッチングに特化しているため、通常の正規表現よりも種類が少なく、ファイルマッチングに向いています。

Ant 形式の各記号の意味は以下の通りです。

| 記号              | 意味                          | 例                                          |
| ----------------- | ----------------------------- | ------------------------------------------- |
| `*`               | 任意の1階層内で、任意の文字列 | `*.java` → 直下のすべての `.java` ファイル  |
| `**`              | 任意の階層を再帰的に含む      | `**/*.java` → 下層すべての `.java` ファイル |
| `?`               | 任意の1文字                   | `Test?.kt` → `Test1.kt`, `TestA.kt` など    |
| `{a,b}`           | いずれかの候補                | `**/*.{kt,java}` → `.kt` または `.java`     |
| `!`（Gradle拡張） | 除外                          | `!**/temp/**` → `temp` フォルダ以下を除外   |




