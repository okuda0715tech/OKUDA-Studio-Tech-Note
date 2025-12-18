- [require check assert の違い](#require-check-assert-の違い)
  - [require 関数](#require-関数)
  - [check 関数](#check-関数)
  - [assert 関数](#assert-関数)
  - [!!](#)
    - [checkNotNull と requireNotNull と !! の違い](#checknotnull-と-requirenotnull-と--の違い)


# require check assert の違い

require 関数、 check 関数、 assert 関数は、どれも同じような役割ですが、このドキュメントでは、それらの違いについて説明します。


## require 関数

表明が満たされない場合 IllegalArgumentException が発生します。この例外は関数の引数が不正であることを示すためのものです。つまり、 [require](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/require.html) 関数は、関数の引数が不正であることを示すのに使用します。

```kotlin
fun getIndices(count: Int): List<Int> {
    require(count >= 0) { "Count must be non-negative, was $count" }

    // ...

    // count は生成するリストの要素の数
    // ラムダ内は、要素のインデックスを it として、各要素に代入する値を計算
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

[assert](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/assert.html) 関数は、表明が満たされない場合 AssertionError が発生します。

また、 release ビルドでの挙動が異なります。

- assert は、デフォルトの設定で debug ビルドでは有効となり、 release ビルドでは無効となります。
- require や check は、 release ビルドでも有効です。

このデフォルトの設定を活かすために、 assert は、デバッグやテストでの使用に向いています。

release ビルドで作成されたアプリでも例外を認識し、例外処理を実行する必要がある場合は、 require や check を使用してください。


## !!

`check())` や `require()` には null 専用の `checkNotNull()` や `requireNotNull()` が用意されています。一方で、 `param!!` のように、変数名の後ろに `!!` をつけることで、 null でないことを宣言する方法もあります。

`!!` の特徴は以下の通りです。

- 変数が null だった場合には、 NullPointerException を発生させる。
- 変数を使用する際に null でないことを宣言する。
- スマートキャスト機能はない
- null だった場合に表示するメッセージを自分で定義できない


### checkNotNull と requireNotNull と !! の違い

`!!` は、変数名の直後に付与することから、変数を使用する場面になって初めて null でないことを宣言します。

一方で、 `checkNotNull` や `requireNotNull` は、 null ではないことを任意の場所で確認することが可能です。そのため、関数の先頭で宣言したり、変数に値を格納した直後に宣言して、事前に null ではないことを確認することが可能です。不具合を早い段階で発見することで、不具合の原因を特性しやすくなります。宣言と同時に適切なメッセージを設定することで、そのコードの読み手に意図を伝えることができます。

上記の通り、 `!!` は場当たり的な宣言、 `check` や `require` は早めの宣言になるため、できる限り check や require を使用するようにしましょう。




