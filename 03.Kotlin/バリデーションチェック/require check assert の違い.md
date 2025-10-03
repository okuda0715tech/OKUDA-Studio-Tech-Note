- [require check assert の違い](#require-check-assert-の違い)
  - [require 関数](#require-関数)
  - [check 関数](#check-関数)
  - [assert 関数](#assert-関数)


# require check assert の違い

require 関数、 check 関数、 assert 関数は、どれも同じような役割ですが、このドキュメントでは、それらの違いについて説明します。


## require 関数

表明が満たされない場合 IllegalArgumentException が発生します。この例外は関数の引数が不正であることを示すためのものです。つまり、 [require](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/require.html) 関数は、関数の引数が不正であることを示すのに使用します。

```kotlin
fun getIndices(count: Int): List<Int> {
    require(count >= 0) { "Count must be non-negative, was $count" }
    // ...
    return List(count) { it + 1 }
}
```


## check 関数

表明が満たされない場合 IllegalStateException が発生します。この例外は 、リクエストされた処理を実行するために適切な状態になっていないことを示すものです。つまり、 require 関数が、関数の引数の状態を確認するものであるのに対して、 [check](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/check.html) 関数は、関数の引数以外のプロパティやローカル変数の状態を確認するものであると言えます。

```kotlin
var someState: String? = null
fun getStateValue(): String {
    val state = checkNotNull(someState) { "State must be set beforehand" }
    check(state.isNotEmpty()) { "State must be non-empty" }
    // ...
    return state
}
```


## assert 関数

[assert](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/assert.html) 関数は、表明が満たされない場合 AssertionError が発生します。また、 require や check とは異なり、デフォルトの設定で、 debug ビルドでは有効となり、 release ビルドでは無効となります。 ( require や check は、 release ビルドでも有効です。)

このデフォルトの設定を活かすために、 assert は、デバッグやテストでの使用に向いています。

release ビルドで作成されたアプリでも例外を認識し、例外処理を実行する必要がある場合は、 require や check を使用してください。


