- [settings.gradle](#settingsgradle)
- [settings.gradle の役割](#settingsgradle-の役割)
  - [settings.gradle はトップレベルにのみ存在する](#settingsgradle-はトップレベルにのみ存在する)
  - [settings.gradle でよく使用する関数やプロパティ](#settingsgradle-でよく使用する関数やプロパティ)
    - [rootProject.name](#rootprojectname)
    - [include()](#include)
    - [](#)
    - [](#-1)
    - [](#-2)
    - [](#-3)
    - [](#-4)
    - [](#-5)
  - [参考資料](#参考資料)


# settings.gradle

# settings.gradle の役割

- MavenCentral など、依存関係を取得するためのリモートリポジトリを定義する。
  - 以前までは、 build.gradle に定義するのが主流でしたが、現在は settings.gradle に定義するのが主流です。
- プロジェクトに含めるモジュールを定義する。


## settings.gradle はトップレベルにのみ存在する

settings.gradle はトップレベルにのみ存在し、モジュールレベルには存在していません。つまり、プロジェクト内に一つだけ存在します。


## settings.gradle でよく使用する関数やプロパティ

### rootProject.name

```kotlin
rootProject.name = "MyAwesomeApp"
```


### include()

プロジェクトに追加するモジュールを指定します。

```kotlin
include(":app")
// モジュール内に定義されたサブモジュールの場合
include(":feature:login")
```

上記のサブモジュールの例では、

```
プロジェクトルート
 └─ feature  ← フォルダ or モジュール
     └─ login  ← サブモジュール
```

という構造になっていることを前提にしています。

feature フォルダ自体は必ずしもモジュールである必要はなく、サブモジュールの名前空間（階層フォルダ）として使うこともできます。

モジュールと単なるフォルダの違いについては、 [モジュールとは](../Android%20Developers(公式ドキュメントの翻訳+補足)/) を参照してください。


### 

```kotlin
```


### 

```kotlin
```


### 

```kotlin
```


### 

```kotlin
```


### 

```kotlin
```


### 

```kotlin
```



## 参考資料

- ChatGPT

