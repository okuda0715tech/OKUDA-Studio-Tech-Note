- [Gradle (Kotlin) の文法](#gradle-kotlin-の文法)
  - [build.gradle.kts の文法は超簡単](#buildgradlekts-の文法は超簡単)
    - [パラメータ代入の例](#パラメータ代入の例)
    - [関数呼び出しの例](#関数呼び出しの例)
  - [その他できること](#その他できること)
    - [コメント](#コメント)
    - [変数が定義できる](#変数が定義できる)
    - [if や for が使える](#if-や-for-が使える)
    - [リストやマップが使える](#リストやマップが使える)


# Gradle (Kotlin) の文法

昔は、 build.gradle は、 Groovy DSL によって記述されていたため、 Groovy の文法を理解する必要がありました。しかし、最近は、 Kotlin で build.gradle.kts が記述できるようになり、 Groovy の文法を覚える必要はなくなりました。

よって、 Kotlin の文法知識があれば、文法的には build.gradle.kts を記述することができます。


## build.gradle.kts の文法は超簡単

build.gradle.kts は、 Kotlin のパラメータ代入と関数呼び出しだけで構成されています。

### パラメータ代入の例

以下の例では、 `minSdk` というパラメータに 24 という値が代入されています。

```kotlin
android {
    defaultConfig {
        minSdk = 24
    }
}
```


### 関数呼び出しの例

以下の例では、文字列 `"androidx.core:core-ktx:1.17.0"` が引数に渡されて `implementation` という関数が呼び出されています。

```kotlin
dependencies {
    implementation("androidx.core:core-ktx:1.17.0")
}
```

以下の例では、 `android` という関数が呼び出されています。 android 関数の引数には関数型が入るため、ラムダブロックで引数が渡されています。

```kotlin
android {
    defaultConfig {
        minSdk = 24
    }
}
```


## その他できること

### コメント

コメントの書き方は Kotlin と同じです。

```kotlin
// 1行コメント
/* 複数行コメント */
```


### 変数が定義できる

```kotlin
val version = "1.0"
versionName = version
```

### if や for が使える

```kotlin
if (enableFeature) {
    println("Enabled")
}
```


### リストやマップが使える

```kotlin
val list = listOf("a", "b")
val map = mapOf("key" to "value")
```


