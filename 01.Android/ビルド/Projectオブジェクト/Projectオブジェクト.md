- [Project オブジェクト](#project-オブジェクト)
  - [Project オブジェクトのメンバプロパティ](#project-オブジェクトのメンバプロパティ)
  - [Project オブジェクトの省略](#project-オブジェクトの省略)


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



