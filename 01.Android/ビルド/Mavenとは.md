- [Maven とは](#maven-とは)
  - [概要](#概要)
  - [構造](#構造)
  - [例](#例)
  - [Gradle の maven { url "..." } の意味](#gradle-の-maven--url---の意味)
  - [Maven Central は「Maven 仕様に従っている大規模な公共リポジトリ」の一つにすぎない](#maven-central-はmaven-仕様に従っている大規模な公共リポジトリの一つにすぎない)


# Maven とは

## 概要

「 Maven 」とは “リモートリポジトリの名前” ではなく、 “依存ライブラリをどう配置・管理するかの標準（仕様）” の名前です。

つまり、 Maven は、 「 Maven リポジトリ形式」というフォルダ構造とルールを指します。

リポジトリがどこに置かれていようと、 ( Web サーバー / GitHub Raw / Amazon S3 / ローカルフォルダ etc.) Maven の仕様に定められたフォルダ構造に従っていれば Maven Repository になります。


## 構造

Maven Repository の本質はこのフォルダ構造です。

```
/groupId/
    /artifactId/
        /version/
            artifactId-version.jar
            artifactId-version.pom
```


## 例

```
https://repo.maven.apache.org/maven2/
    com/
       google/
            gson/
                2.10/
                    gson-2.10.jar
                    gson-2.10.pom
```


このように置いてあれば、
そこは Maven Repository として動作する、という仕様です。


## Gradle の maven { url "..." } の意味

これは、

この URL 配下を 「 Maven 形式のリポジトリ」 として扱う

という宣言です。

よって、 url 部分には GitHub Raw などのリポジトリの場所が指定されます。


## Maven Central は「Maven 仕様に従っている大規模な公共リポジトリ」の一つにすぎない

Maven Central は “Maven リポジトリ形式” を実装した巨大サービスのことです。

企業や OSS が自前で “Maven リポジトリ形式” を作ることも普通です。

例えば、 Adfurikun は GitHub 上に自作の Maven リポジトリを置いています。

```kotlin
dependencyResolutionManagement {
    repositories {
        maven("https://github.com/adfurikun/adfurikun_maven/raw/master")
        google()
        mavenCentral()
    }
}
```





