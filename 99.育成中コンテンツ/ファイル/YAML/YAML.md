- [YAML](#yaml)
  - [YAML の基本的な特徴](#yaml-の基本的な特徴)
  - [具体例（GitHub Actions の場合）](#具体例github-actions-の場合)
  - [⚙️ YAMLの基本文法まとめ](#️-yamlの基本文法まとめ)
  - [注意点](#注意点)


# YAML

YAML（ヤムル、YAML Ain’t Markup Language）は、設定ファイル（コンフィグファイル）を人間が読みやすく書くためのフォーマットです。

## YAML の基本的な特徴

- 人間が読みやすい
  - インデント（スペース）で階層を表現する。
  - JSON や XML よりもシンプルで見やすい。
- 構造化されたデータを表現できる
  - リスト（配列）、マップ（辞書）、文字列、数値などを扱える。
- コメントが書ける
  - `#` で始めるとコメントになる。
- 主な用途
  - CI / CD ツール（例：GitHub Actions、CircleCI、GitLab CI）
  - パッケージ管理設定（例：Dependabot）
  - インフラ定義（例：Docker Compose、Kubernetes）
  - アプリ設定ファイル（例：Spring Boot、Rails など）


## 具体例（GitHub Actions の場合）

```yaml
name: CI

on:
  push:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - name: コードをチェックアウト
        uses: actions/checkout@v4

      - name: Javaをセットアップ
        uses: actions/setup-java@v4
        with:
          java-version: '17'

      - name: ビルドを実行
        run: ./gradlew build
```

この例は、 GitHub Actions で 「 main ブランチに push されたら、 Java 17 環境で Gradle ビルドを実行する」 というワークフローを定義した YAML ファイルです。


## ⚙️ YAMLの基本文法まとめ

マップ

```yaml
name: Okuda
age: 38
```

リスト

```yaml
fruits:
  - apple
  - banana
  - orange
```

ネスト（入れ子）

```yaml
user:
  name: Okuda
  skills:
    - Kotlin
    - Compose
    - Gradle
```


## 注意点

YAML は、インデントでネストを表現するため、インデントがずれると正しく認識されません。そのため、サーバーとクライアント間で、一行にシリアライズされたデータを送受信する際には、 YAML ではなく JSON を使用することが一般的です。


