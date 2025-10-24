- [plugins の記述方法](#plugins-の記述方法)
  - [id() 関数を使用](#id-関数を使用)
  - [kotlin() 関数を使用](#kotlin-関数を使用)
  - [alias 関数を使用](#alias-関数を使用)


# plugins の記述方法

## id() 関数を使用

```kotlin
plugins {
    id("com.android.application") version "8.1.0"
    id("org.jetbrains.kotlin.jvm") version "1.9.10"
}
```

`id()` 内に、プラグインの完全な id を指定します。


## kotlin() 関数を使用

```kotlin
plugins {
    kotlin("plugin.serialization") version "2.0.21"
}
```

Kotlin Gradle Plugin が提供する拡張関数です。Kotlin プラグインの特定のコンポーネントを簡潔に指定できるようになっています。

`kotlin()` 関数は、 `id("org.jetbrains.kotlin")` に該当します。つまり、上記の例の場合は、 `id("org.jetbrains.kotlin.plugin.serialization")` と記述したのと同じことになります。


## alias 関数を使用

libs.versions.toml ファイルを参照する方法です。詳しくは、 [[plugins] セクション](./libs.version.toml%20の記述方法.md/#plugins-セクション) を参照してください。




