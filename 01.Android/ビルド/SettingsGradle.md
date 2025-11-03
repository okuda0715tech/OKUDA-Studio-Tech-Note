- [settings.gradle](#settingsgradle)
  - [settings.gradle の概要](#settingsgradle-の概要)
  - [settings.gradle でよく使用する関数やプロパティ](#settingsgradle-でよく使用する関数やプロパティ)
    - [rootProject.name](#rootprojectname)
    - [include()](#include)
      - [実際にビルドされるためには](#実際にビルドされるためには)
    - [project()](#project)
    - [pluginManagement{}](#pluginmanagement)
    - [dependencyResolutionManagement{}](#dependencyresolutionmanagement)
    - [enableFeaturePreview()](#enablefeaturepreview)
      - [TYPESAFE\_PROJECT\_ACCESSORS](#typesafe_project_accessors)
    - [includeBuild()](#includebuild)
      - [使用例](#使用例)
  - [参考資料](#参考資料)


# settings.gradle

## settings.gradle の概要

settings.gradle はトップレベルにのみ存在し、モジュールレベルには存在していません。つまり、プロジェクト内に一つだけ存在します。

settings.gradle の主な役割は以下です。

- MavenCentral など、依存関係を取得するためのリモートリポジトリを定義する。
  - 以前までは、 build.gradle に定義するのが主流でしたが、現在は settings.gradle に定義するのが主流です。
- プロジェクトに含めるモジュールを定義する。


## settings.gradle でよく使用する関数やプロパティ

### rootProject.name

プロジェクト名を指定するためのプロパティです。デフォルトでは、 IDE でプロジェクトを開いた際のルートフォルダ名が設定されています。

```kotlin
rootProject.name = "MyAwesomeApp"
```

Gradle や IDE は、ここで指定された文字列をプロジェクト名と認識して、成果物（aab）などの名前として設定します。


### include()

include() 関数は、引数に指定したモジュールの存在をプロジェクトに知らせます。存在を知らせることで、そのモジュールは、ビルド対象の候補となります。（[実際にビルドされるためには、こちらを参照してください](#実際にビルドされるためには)）

```kotlin
include(":app")
// サブモジュールの場合
include(":feature:login")
// コンマ区切りで複数モジュールを指定することも可能
include(":app", ":feature:home", ":library:core")
```

上記のサブモジュールの例では、

```
プロジェクトルート
 └─ feature  ← フォルダ or モジュール
     └─ login  ← サブモジュール
```

という構造になっていることを前提にしています。

feature フォルダ自体は必ずしもモジュールである必要はなく、サブモジュールの名前空間（階層フォルダ）として使うこともできます。

モジュールと単なるフォルダの違いについては、 [モジュールとは](../Android%20Developers%20公式ドキュメントの翻訳と補足/設計とプランニング/アプリアーキテクチャ/2.モジュール化/モジュールとライブラリ.md/#モジュールとは) を参照してください。


#### 実際にビルドされるためには

include() 関数で指定したモジュールが、実際にビルドされるには、そのモジュールが別のモジュールから依存されている必要があります。つまり、別のモジュールの build.gradle から以下のように依存関係が定義されている必要があります。

```kotlin
dependencies {
    implementation(projects.feature.login)
}
```

このように依存関係の定義がされていないと、 login モジュールは、別のモジュールから見えておらず、 login モジュールのクラスを使用することはできません。また、 login モジュールに依存しているモジュールが全く存在しない場合は、 login モジュールはビルドの対象にはなりません。


### project()

project() 関数を使用すると、フォルダ名とモジュール名が一致しない場合でも、モジュール名とモジュールの格納場所を変更せずに済みます。

```kotlin
include(":payment")
project(":payment").projectDir = file("modules/pay_module")
```

他のモジュールからは implementation(project(":payment")) のように依存できます。

つまり、 Gradle では モジュール名と実際のフォルダパスは必ずしも一致する必要はありません。 `project(":モジュール名").projectDir = file("実際のパス")` で任意にマッピングできます。


### pluginManagement{}

`pluginManagement{}` ブロック内では、 [Gradle プラグイン](./Gradleプラグインとは.md) を取得するリモートリポジトリを定義します。

```kotlin
pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}
```

Android では、 Google リポジトリは必須です。

ライブラリを取得するリモートリポジトリの定義は、後述する `dependencyResolutionManagement{}` ブロック内で定義します。


### dependencyResolutionManagement{}

`dependencyResolutionManagement{}` ブロック内では、ライブラリを取得するリモートリポジトリを定義します。

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
```

RepositoriesMode には以下のオプションがあります。

- FAIL_ON_PROJECT_REPOS
  - 推奨される設定です。
  - モジュール（各 build.gradle.kts）内に repositories {} があるとビルドに失敗します。
  - モジュールごとのリポジトリばらつきを防止し、再現性を高めます。
- PREFER_PROJECT
  - モジュール間で統一性が崩れるため、非推奨です。
  - 各モジュールで定義されたリポジトリを優先して使用します。
  - 各モジュールで定義されたリポジトリにライブラリが存在しなければ、 settings.gradle で定義されたリポジトリから取得を試みます。
- PREFER_SETTINGS
  - 古いプロジェクトからの移行措置として使用することがあります。
  - settings.gradle で定義されたリポジトリを優先して使用します。
  - settings.gradle で定義されたリポジトリにライブラリが存在しなければ、各モジュールで定義されたリポジトリから取得を試みます。


### enableFeaturePreview()

enableFeaturePreview() 関数は、 Gradle で、まだ正式リリースされていない「実験的機能」や「将来デフォルトになる予定の機能」を有効化するための関数です。公式リリース前なので 変更や削除の可能性あります。


#### TYPESAFE_PROJECT_ACCESSORS

最近では、 TYPESAFE_PROJECT_ACCESSORS が指定されることが多いです。これは、 Gradle の型安全なプロジェクト依存アクセスを有効化する機能です。

これまでは、あるモジュールから別のモジュールへの依存関係を定義する際は、以下のように記述していました。

```kotlin
// build.gradle.kts
dependencies {
    implementation(project(":feature:login"))
}
```

この方法では、モジュール名を文字列として入力しています。そのため、文字列の入力ミスや、リネーム時にツールを使用した一括リネームができないなどの不都合がありました。 TYPESAFE_PROJECT_ACCESSORS を導入すると、以下のように記述できます。

```kotlin
// build.gradle.kts
dependencies {
    implementation(projects.feature.login)
}
```

これにより、上記の不都合が解消されます。ただし、 includeBuild を多用する特殊構成では扱いが難しいという難点もあるようです。

**注意** : settings.gradle.kts の include() の引数へは、従来通り文字列で記述する必要があります。

```kotlin
// settings.gradle.kts
include(":feature:login")
```


### includeBuild()

includeBuild() 関数は、自分が開発中のプロジェクト以外のプロジェクトに置かれたライブラリを取り込む際に使用します。

ライブラリは、ソースコードとして取り込まれ、まだコンパイルされていないため、ライブラリ側のプロジェクトでコンパイルの設定を変更してからビルドすることが可能です。

取り込む側のプロジェクトをビルドすると、取り込まれる側のソースコードも一緒にビルドされます。

includeBuild() 関数を使用する主なケースは以下の場合です。

- Maven などのリモートリポジトリに公開予定のライブラリを公開前にローカルでテストしたい場合
- 企業内の複数のプロジェクトで共通のライブラリを使用している場合
  - なお、この場合、ライブラリプロジェクトをビルドして、 JAR や AAR を生成してから、それをプロジェクトに取り込む方法もあります。

#### 使用例

```kotlin
// settings.gradle.kts
includeBuild("../my-library")
```

```kotlin
// build.gradle.kts
dependencies {
    implementation("com.example:mylibrary:1.0")
}
```

上記の implementation の記述を見ると、リモートリポジトリからライブラリを取得するように見えますが、実際には、ローカルに存在する my-library を取り込むことができます。

依存先の `group:artifact:version` が外部プロジェクトの group と artifact と version と一致する場合、ローカルの外部プロジェクトがリモートリポジトリより優先されます。

つまり、上記の例では、 `../my-library/build.gradle.kts` 内に

```kotlin
// build.gradle.kts
group = "com.example"
version = "1.0"
```

と書かれており、 artifact が一致していれば、 `implementation("com.example:mylibrary:1.0")` は、リモートではなくローカルの my-library に置き換えられます。

artifact は、明示的に定義されていなければ、 `settings.gradle.kts` の `rootProject.name` に設定された文字列が使用されます。

明示的に定義する場合は、 `build.gradle.kts` の archiveBaseName に定義されます。

```kotlin
// ../my-library/build.gradle.kts
tasks.withType<Jar> {
    archiveBaseName.set("custom-artifact")
}
```

Maven Publish プラグインを使う場合は以下のように定義されます。

```kotlin
publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.example"
            artifactId = "mylibrary"
            version = "1.0"

            from(components["java"])
        }
    }
}
```


## 参考資料

- ChatGPT

