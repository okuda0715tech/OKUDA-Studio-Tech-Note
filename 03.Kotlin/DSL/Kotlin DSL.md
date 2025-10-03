- [Kotlin DSL](#kotlin-dsl)
  - [概要](#概要)
  - [使用例](#使用例)
    - [isNullOrEmpty の使用例](#isnullorempty-の使用例)
    - [Jetpack Compose の使用例](#jetpack-compose-の使用例)


# Kotlin DSL

## 概要

Kotlin DSL は、 Domain Specific Language の略であり、 「特定の分野に特化した構文」 という意味を持っています。

これだけだとわかりにくいですが、具体的には以下の特徴を持っているものが DSL に分類されているようです。

- 命令的ではなく、宣言的な使用方法がされている。
- 関数の引数をラムダで与えている。
- 特定の特殊な目的に特化した処理を行っている。


## 使用例

Kotlin DSL が使用されている具体的な場所には、以下のような場面があります。

- build.gradle
- Kotlin Contracts
  - isNullOrEmpty 関数などで使用されています。
- Jetpack Compose
  - Composable 関数で使用されています。


### isNullOrEmpty の使用例

```kotlin
@kotlin.internal.InlineOnly
public inline fun CharSequence?.isNullOrEmpty(): Boolean {
    contract {
        returns(false) implies (this@isNullOrEmpty != null)
    }

    return this == null || this.length == 0
}
```


### Jetpack Compose の使用例

Jetpack Compose のコンポーザブル関数自体が DSL の一種なのですが、以下の例では、特に、 `item` や `items` といったコンポーザブルではない関数も DSL の一種となります。

```kotlin
LazyColumn {
    // Add a single item
    item {
        Text(text = "First item")
    }

    // Add 5 items
    items(5) { index ->
        Text(text = "Item: $index")
    }

    // Add another single item
    item {
        Text(text = "Last item")
    }
}
```

参考 : item や items 関数は、コンポーザブル関数ではありません。

コンポーザブル関数とは、関数定義の先頭に `@Composable` アノテーションが付与されている関数を指します。しかし、 item や items 関数には該当のアノテーションは付与されていません。

その引数 (ラムダブロック) にコンポーザブル関数が渡され、 LazyColumn を含めた階層構造を持った特殊な関数となっているため、これらは DSL の一種であるとみなされます。

なお、 `@Composable` アノテーションは、コンポーザブル関数を呼び出す関数には付与することが必須となっていますが、 item や items 関数には付与する必要がありません。 item や items 関数は、上記の例では、 Text コンポーザブルを呼び出しているように見えますが、実際には、引数に渡しているだけです。実際に Text コンポーザブルを呼び出しているのは、 LazyColumn コンポーザブルだと思われます。

