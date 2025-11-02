- [Dependabot](#dependabot)
  - [概要](#概要)
  - [設定方法](#設定方法)
    - [dependabot.yml の記述方法](#dependabotyml-の記述方法)
      - [updates 内のハイフン](#updates-内のハイフン)
      - [package-ecosystem](#package-ecosystem)
      - [directory](#directory)
      - [schedule](#schedule)
      - [open-pull-requests-limit](#open-pull-requests-limit)
      - [allow と ignore](#allow-と-ignore)
      - [allow](#allow)
      - [ignore](#ignore)
      - [groups](#groups)
      - [commit-message](#commit-message)
    - [バージョンカタログ（libs.versions.toml）にも対応](#バージョンカタログlibsversionstomlにも対応)
  - [手動実行](#手動実行)
  - [参考資料](#参考資料)


# Dependabot

## 概要

Dependabot は、アプリが依存しているライブラリなどの最新バージョンが存在するかどうかを自動的にチェックし、更新などがあれば、自動的にプルリクエストを作成したり、メールなどで通知してくれるサービスです。 GitHub が提供しているサービスです。


## 設定方法

リポジトリのルートに `.github/dependabot.yml` を配置してプッシュすると、その設定ファイルに従って 自動的に依存関係の更新チェックとプルリクエストの作成を実行してくれます。


### dependabot.yml の記述方法

dependabot.yml の各項目の意味は以下の通りです。

#### updates 内のハイフン

Dependabot が自動チェックする対象（モジュールやエコシステム）の数だけ、 `updates` 内にハイフン ( `-` )  で子要素を作成します。これは、 YAML の文法で、 `updates` がリストであることを意味ます。


#### package-ecosystem

Dependabot にチェックさせる対象（エコシステム）を指定します。

Android で使用する可能性があるのは以下になります。

- `gradle` → アプリ／ライブラリ依存
- `github-actions` → CI/CD ワークフロー
- `docker` → Docker を使っている場合
- `maven` → Gradle を使わずに Maven 依存を直接管理している場合


#### directory

`directory` には、依存関係を監視したい build.gradle.kts や settings.gradle.kts が存在するパスを指定します。

プロジェクトルートを指定する場合は、以下のようにします。

```yaml
updates:
  - package-ecosystem: "gradle"
    directory: "/"
```

サブモジュールを指定する場合は、そのパスを指定します。以下はその例です。

```yaml
updates:
  - package-ecosystem: "gradle"
    directory: "/app" # root/app の場合

  - package-ecosystem: "gradle"
    directory: "/core"　# root/core の場合
```

バージョンカタログ (libs.versions.toml) を使用している場合、 `directory` にはルート ( `directory: "/"` ) を指定します。 libs.versions.toml ファイルは、 `ProjectRoot/gradle` フォルダに格納されていますが、 `directory: "/gradle"` と記述する必要はありません。


#### schedule

- `interval`
  - 更新の頻度。必須。
  - 値は `"daily" | "weekly" | "monthly"` のいずれか。

- `day`
  - `interval: "weekly"` の場合に、週の何曜日にチェックするかを指定できます。
  - 値は `"monday" | "tuesday" | ... | "sunday"` 。
  - 省略すると GitHub 側で自動的に決まります。

- `time`
  - UTC 時間で Dependabot が更新を開始する時刻を指定できます。
  - `"HH:MM"` 形式で指定（例： `"04:00"` ）。
  - 省略すると GitHub 側で自動的に決まる。

- `timezone`
  - `time` をローカルタイムで指定したい場合に使用。
  - 例： `"Asia/Tokyo"`
  - 省略すると UTC で解釈される。

- `allow-unauthenticated`
  - 依存関係の更新時に認証なしでアクセスできるパッケージを許可するかどうか。
  - デフォルトは `false` 。


#### open-pull-requests-limit

同時に開ける PR の数を制限できます（最初の頃に大量 PR が出ると対応が大変なため）。

この制限は 「オープン中のプルリクエスト数」 に対する制限です。クローズ済みやマージ済みの PR はカウントされません。

設定しなかった場合のデフォルトは 5 件です。

上限に達した場合、 Dependabot は新しい PR を作成しなくなります。

PR がクローズされたりマージされて、オープン中の PR が減った場合でも、保留中の PR は即作成されることはなく、次回チェック時まで新しい PR は作成されません。


#### allow と ignore

allow と ignore は、自動更新の対象となる依存関係を指定する方法です。

- allow
  - オプトイン方式
  - 指定した依存関係だけを自動更新の **対象** にして、それ以外の依存関係は自動更新の対象外にします。
- ignore
  - オプトアウト方式
  - 指定した依存関係だけを自動更新の **対象外** にして、それ以外の依存関係は自動更新対象にします。

デフォルトでは、オプトアウト方式となっており、すべての依存関係が更新対象になります。

これらは、まったく逆の考え方ですが、同時に指定することも可能です。その場合は、デフォルトでオプトアウトとなり、 allow で対象とされたものの中から、一部を ignore で対象外することが可能です。この方法を使うケースとしては、特定のライブラリの自動更新を ON にしたいが、その中でも特定のバージョンのみ OFF にしたいといったケースで使われます。


#### allow

`dependency-type` を使用すると、ざっくりとした分類で許可する対象を指定できます。分類は以下の中から選択します。

- `"all"` : 全ての依存関係
- `"direct"` : 自分のプロジェクトで直接宣言している依存関係
- `"indirect"` : 直接依存ではない、間接依存（トランジティブ）

以下は記述例です。

```yaml
version: 2
updates:
  - package-ecosystem: "gradle"
    allow:
      - dependency-type: "direct"
```

`dependency-name` を使用すると、個別に許可する対象を指定できます。特定の依存関係だけを更新対象にする場合に使用します。

以下は記述例です。

```yaml
allow:
  - dependency-name: "com.google.guava:guava"
```


#### ignore

`ignore` では、以下の項目を記述することができます。

- `dependency-name`
  - 必須項目
  - 無視するライブラリ名（完全一致）を指定します。
- `versions`
  - 任意項目（省略するとすべてのバージョンを無視）
  - 無視するバージョン、または、バージョン範囲を指定します。

以下は記述例です。

```yaml
updates:
  - package-ecosystem: "gradle"
    ignore:
      - dependency-name: "org.jetbrains.kotlin:kotlin-stdlib"
        versions: ["1.8.x"]
```

バージョンは以下のように指定することが可能です。

- `"1.8.x"` : 1.8系全体
- `">=1.8.0 <1.9.0"` : 1.8.0 以上 1.9.0 未満
- `"*"` : すべてのバージョン

完全なワイルドカードはアスタリスク ( `*` ) ですが、ドットを含まない一部の数値のみのワイルドカードはエックス ( `x` ) を使用します。


#### groups

groups は、Dependabot が作成するプルリクエスト（PR）をまとめるための設定です。これを使うと、関連する依存関係を 1 つの PR にまとめたり、ライブラリごとにグループ化したりできます。

groups を使用するメリットは以下です。

- 関連ライブラリをまとめることで PR が大量に分かれるのを防げる
- グループごとに更新の戦略や優先度を変えられる
- 特定ライブラリだけ毎日更新、それ以外は週1などの運用も可能

以下に例を示します。

```yaml
updates:
  - package-ecosystem: "gradle"
    groups:
      spring:
        patterns:
          - "org.springframework.*"
      kotlin:
        patterns:
          - "org.jetbrains.kotlin.*"
```

- groups 配下にはグループ名を定義します。
- グループ名はいくつでも定義できます。
- patterns に正規表現を指定します。
  - 正規表現にマッチすれば、依存関係はそのグループに入ります。
- 依存関係が複数のグループにマッチする場合は、最初にマッチしたグループに入ります。

各グループには以下のオプションが設定できます。

- `patterns`
  - 正規表現で依存関係をグループ化
- `exclude-patterns`
  - 逆にこのパターンはグループから除外する
- `versioning-strategy`
  - `auto` / `widen` / `increase` / `lockfile-only` などの戦略をグループ単位で上書き可能
- `update-types`
  - `all` / `security` / `version-update:semver-patch` など、プルリクエストに含める更新タイプを制限

以下に例を示します。

```yaml
groups:
  spring:
    patterns:
      - "org.springframework.*"
    versioning-strategy: "increase"
    update-types:
      - "all"
```


#### commit-message

`commit-message` は、 Dependabot が自動で作成するコミットメッセージのフォーマット（特に接頭辞）をカスタマイズするための機能です。

- prefix
  - コミットメッセージの先頭につける文字列です。
  - 通常は、 `"chore"` / `"build"` のいずれかが使用されます。
- prefix-development
  - prefix 同様に、コミットメッセージの先頭につける文字列です。
  - ただし、開発環境でしか使用されていない依存関係を更新する際には、こちらが使用されます。（詳細は後述します。）
- include
  - コミットメッセージに含めたいオプションを指定します。
  - ただし、現在指定できる値は "scope" または "nothing" だけであり、実質的に scope しか含めることができません。
  - scope については、 [Conventional Commits の scope](../../Git/コミットメッセージの規約.md/#scope) を参照してください。

prefix が使用されるか、 prefix-development が使用されるかは、以下のルールで Dependabot が自動的に判定します。

- `implementation` / `api` / `runtimeOnly` → 本番用依存関係なので prefix が使用される。
- `testImplementation` / `androidTestImplementation` → 開発用依存関係なので、 prefix-development が使用される。

基本的な例を以下に示します。

```yaml
updates:
  - package-ecosystem: "gradle"
    commit-message:
      prefix: "build"
      include: "scope"
```

上記の dependabot.yml の場合は、以下のコミットメッセージが生成されます。

```scss
build(deps): bump junit from 4.12 to 4.13.2
```

deps は、 dependencies の略です。依存関係の更新である旨を示します。

prefix-development を使用した例を以下に示します。

```yaml
commit-message:
  prefix: "build"
  prefix-development: "build(dev)"
  include: "scope"
```

上記の dependabot.yml の場合は、以下のコミットメッセージが生成されます。

```scss
// 本番依存関係
build(deps): bump retrofit from 2.9.0 to 2.10.0

// 開発依存関係
build(dev-deps): bump junit from 4.12 to 4.13.2
```


### バージョンカタログ（libs.versions.toml）にも対応

Dependabot は、バージョンカタログ（libs.versions.toml）にも対応しています。バージョンカタログを使っていても、 dependabot.yml の記述方法は変わりません。

Dependabot は、以下のファイルを自動的にスキャンします。

- build.gradle / build.gradle.kts
- settings.gradle / settings.gradle.kts
- gradle/libs.versions.toml


## 手動実行

基本的には、スケジュール設定に従って自動的に実行されますが、手動で実行することも可能です。

`GitHub のリポジトリ画面 → 「Security」タブ → 「Dependabot」→「Check for updates」ボタン` を押すと、設定に従って手動で更新チェックを実行できます。


## 参考資料

- [ライブラリのバージョン管理ツールDependabotを導入しました](https://zenn.dev/coconala/articles/1bf6256bf11ec2)



