- [トップレベルの BuildGradle](#トップレベルの-buildgradle)
  - [トップレベルの build.gradle の役割](#トップレベルの-buildgradle-の役割)
  - [トップレベルの build.gradle でよく使用される関数やプロパティ](#トップレベルの-buildgradle-でよく使用される関数やプロパティ)
    - [extra 配列プロパティ](#extra-配列プロパティ)
    - [subprojects {} 関数](#subprojects--関数)
    - [allprojects {} 関数](#allprojects--関数)
  - [参考資料](#参考資料)


# トップレベルの BuildGradle

## トップレベルの build.gradle の役割

プロジェクトのルートディレクトリにあるトップレベルの build.gradle ファイルにより、プロジェクトのすべてのモジュールに適用されるビルド設定が定義されます。

トップレベルの build.gradle に定義されたプロパティは、モジュールレベルの build.gradle から参照することができます。


## トップレベルの build.gradle でよく使用される関数やプロパティ

### extra 配列プロパティ

extra 配列プロパティを使用すると、トップレベルの build.gradle で定義したプロパティをモジュールレベルの build.gradle から参照することが可能です。

```kotlin
// トップレベル build.gradle.kts
extra["composeVersion"] = "1.6.0"
extra["compileSdk"] = 34
```

```kotlin
// モジュールレベル build.gradle.kts
android {
    compileSdk = rootProject.extra["compileSdk"] as Int

    composeOptions {
        kotlinCompilerExtensionVersion =
            rootProject.extra["composeVersion"] as String
    }
}
```

**注意** : Extra Properties は Any 型なので、使用時にキャストが必要です

Groovy の名残で `ext{}` ブロックが使用されることもあります。

```kotlin
ext {
    set("composeVersion", "1.6.0")
    set("kotlinVersion", "1.9.10")
}
```

基本的には、 Kotlin DSL に移行しているのであれば、 extra 配列プロパティを使用するようにしましょう。


### subprojects {} 関数



### allprojects {} 関数



## 参考資料

- ChatGPT


