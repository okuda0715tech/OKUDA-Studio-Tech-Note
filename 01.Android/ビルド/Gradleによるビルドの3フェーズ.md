- [Gradleによるビルドの 3 フェーズ](#gradleによるビルドの-3-フェーズ)
  - [初期化フェーズ (Initialization phase)](#初期化フェーズ-initialization-phase)
  - [設定フェーズ (Configuration phase)](#設定フェーズ-configuration-phase)
  - [実行フェーズ (Execution phase)](#実行フェーズ-execution-phase)


# Gradleによるビルドの 3 フェーズ

## 初期化フェーズ (Initialization phase)

- 目的
  - ビルド全体で扱うプロジェクト構成を決定する。
  - settings.gradle.kts を読み込み、ルートプロジェクトとサブプロジェクトの一覧を作る。

- 主な処理内容
  - Gradle オブジェクトの作成
  - settings.gradle.kts の読み込みと評価
  - プロジェクト階層の決定 ( `include(":app", ":library")` など)
  - 各 Project オブジェクトの生成準備（まだ設定されていない）

- 使用可能な主なフック
  - `gradle.beforeSettings { settings -> ... }`
    - settings.gradle.kts の評価直前に呼ばれる
  - `gradle.settingsEvaluated { settings -> ... }`
    - settings.gradle.kts の評価直後に呼ばれる
  - `gradle.projectsLoaded { gradle -> ... }`
    - すべての Project オブジェクトが作成された直後に呼ばれる


## 設定フェーズ (Configuration phase)

- 目的
  - 各プロジェクトの build.gradle.kts を評価し、タスクを登録したり、タスクやライブラリの依存関係を構築する。

- 主な処理内容
  - 各 Project の設定スクリプトを順番に評価
  - プラグイン適用、タスク登録、タスクとライブラリの依存関係設定など
  - ビルド構造（task graph）を確定

- 使用可能な主なフック
  - `gradle.beforeProject { project -> ... }`
    - 各 build.gradle.kts の評価直前に呼ばれる
  - `gradle.afterProject { project -> ... }`
    - 各 build.gradle.kts の評価直後に呼ばれる
  - `gradle.projectsEvaluated { gradle -> ... }`
    - すべてのプロジェクト設定完了後に一度だけ呼ばれる


## 実行フェーズ (Execution phase)

- 目的
  - 実際に要求されたタスクを実行する。

- 主な処理内容
  - 実行対象タスクを決定 (gradle.taskGraph)
  - 設定フェーズで構築したタスクの依存関係を確認しながら、実際にタスクを実行

- 使える主なフック
  - `gradle.taskGraph.whenReady { taskGraph -> ... }`
    - 実行タスクの依存関係の確認が完了したときに呼ばれる
  - `gradle.buildFinished { result -> ... }`
    - ビルド完了時（成功・失敗を問わず）に呼ばれる
  - タスク単位のフック
    - `doFirst { ... }` — タスク開始直前
    - `doLast { ... }` — タスク終了直後


