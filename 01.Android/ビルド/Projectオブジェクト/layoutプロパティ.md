- [layout プロパティ](#layout-プロパティ)
  - [概要](#概要)
  - [よく使用されるメンバー](#よく使用されるメンバー)
  - [使用例](#使用例)
  - [layout プロパティを使用するメリット](#layout-プロパティを使用するメリット)


# layout プロパティ

## 概要

`layout` プロパティは、ビルドディレクトリやファイルのパスを型安全に扱うための「レイアウトサービス」オブジェクトです。

layout プロパティの型は `org.gradle.api.file.ProjectLayout` です。


## よく使用されるメンバー

- buildDirectory: DirectoryProperty
  - `build/` ディレクトリを表す。
  - （例：project.layout.buildDirectory）
- projectDirectory: Directory
  - プロジェクトルート（build.gradle.kts がある場所）
- files(...): ConfigurableFileCollection / dir(...): Directory
  - パス指定から Gradle 管理下のファイルオブジェクトを生成
- file(...):RegularFile
  - 1 つのファイルを表すオブジェクトを取得


## 使用例

以下の例は、 `layout.buildDirectory` ( ＝ `build/` ) を基準に `build/copiedResources` というディレクトリを安全に参照しています。

```kotlin
tasks.register<Copy>("copyResources") {
    from("src/main/resources")
    into(layout.buildDirectory.dir("copiedResources"))
}
```


## layout プロパティを使用するメリット

従来は単に文字列で `"build/"` と書いていましたが、

- OS依存のパス区切り
- パス結合のエラー
- Gradle のキャッシュ・インクリメンタルビルドとの統合

といった問題を避けるために、 Gradle では Provider / Property ベースのファイル API ( ProjectLayout ) を導入しています。



