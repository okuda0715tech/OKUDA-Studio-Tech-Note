- [Contract](#contract)
  - [概要](#概要)
  - [isNullOrEmpty() 関数の例](#isnullorempty-関数の例)
  - [contract ブロックの文法](#contract-ブロックの文法)
    - [保証内容の定義方法](#保証内容の定義方法)
    - [条件の定義方法](#条件の定義方法)
  - [apply() 関数の例](#apply-関数の例)
  - [ユーザー定義の Contracts の例](#ユーザー定義の-contracts-の例)


# Contract

## 概要

Kotlin 1.3.0 から Contracts が実装されました。 Contracts を使うことで、関数がどのような振る舞いをするか、どういう効果をもたらすかを定義 (契約) することが出来ます。

標準ライブラリの Contracts は正式にリリースされていますが、ユーザー定義の Contracts は 2024 年 3 月時点で、まだ正式リリースはされていません。


## isNullOrEmpty() 関数の例

文字列が null か空文字の場合のみ true を返し、それ以外の場合は false を返す isNullOrEmpty() という関数があります。

Contracts を使用することにより、 isNullOrEmpty メソッドが false を返すなら、 null でないことが保証できます。

```kotlin
val a: String? = ...
if (!a.isNullOrEmpty()) {
    println(a.length) // !! を書く必要がない
}
```

Contracts がない時代だと呼び出し元で isNullOrEmpty がどんな振る舞いをするかを知るすべがなかったので、 `!!` をつける必要があったのですが、 Contracts により null でないことが保証できるので、 `!!` を省略できます。

isNullOrEmpty の実装は次のようになります。

```kotlin
@kotlin.internal.InlineOnly
public inline fun CharSequence?.isNullOrEmpty(): Boolean {
    contract {
        returns(false) implies (this@isNullOrEmpty != null)
    }

    return this == null || this.length == 0
}
```

contract は [DSL](../DSL/Kotlin%20DSL.md) として定義されています。 contract ブロック内で、この関数の振る舞いをコンパイラに伝える事ができます。

isNullOrEmpty の場合は returns(false) implies (this@isNullOrEmpty != null) が契約として定義されています。

これは、 「 `returns(false)` : falseを返すなら、 `(this@isNullOrEmpty != null)` : 自分自身が null ではない.。」 という意味になります。 なので呼び出し元では false が返ってきたら、 null ではないことが保証されるので、 smartcast により `!!` をつける必要がなくなるわけです。

なので、例えば T.isEmpty(t: T?): Boolean のようなメソッドがあり、ついでに null チェックもこの関数の中でやっているようなときは、 contract を定義することでより使いやすい関数にすることが出来ます。


## contract ブロックの文法

### 保証内容の定義方法

contract ブロック内で使用される `implies` 中置関数は、 **引数で与えられた値が true であることを保証する** という意味の関数です。

contract ブロック内で使用される `callsInPlace` 関数は、 **引数で与えられたラムダ式が、引数で指定した回数分実行されていることを保証する** という意味の関数です。


### 条件の定義方法

implies 関数の条件には以下のものがあります。

- returns()
  - 関数が正常に終了した場合を表します。
- return(value)
  - 関数の戻り値が value である場合を表します。
  - value には、 true / false / null のみが指定できます。
- returnsNotNull()
  - 関数の戻り値が null でない場合を表します。

callsInPlace 関数のオプションには以下のものがあります。

- InvocationKind.EXACTLY_ONCE
- InvocationKind.AT_LEAST_ONCE
- InvocationKind.AT_MOST_ONCE
- InvocationKind.UNKNOWN


## apply() 関数の例

他の例を見てみます。スコープ関数applyの実装は次になります。

```kotlin
@kotlin.internal.InlineOnly
public inline fun <T> T.apply(block: T.() -> Unit): T {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    block()
    return this
}
```

apply 関数内では、 callsInPlace(block, InvocationKind.EXACTLY_ONCE) が契約として定義されています。 これは、 block 関数が必ず一度呼び出されることを意味します。 これにより、以下のように書くことが可能になります。

```kotlin
val a: String
hoge.apply {
    a = "hoge"
}
println(a) // a が初期化済みであることが保証される
```

apply 関数は一度しか呼び出されないので `val a: String` の初期化が、apply 関数内で正しく行われることが保証されます。 Kotlin 1.3.0 以前の contract がない時代では上記のコードはコンパイルエラーになっていたのですが、 contract により、実行することが可能になりました。


## ユーザー定義の Contracts の例

今まで見てきたのは Kotlin のスタンダートライブラリに入っていた関数ですが、カスタムで定義することも可能です。 今回は例として、 ActivityScenario.onActivity メソッドを contract + 拡張関数を使ってより便利にしたいと思います。

ActivityScenario.onActivity メソッドは、 callback を登録すると、 Activity の準備ができたタイミングで callback が叩かれます。そして、この onActivity メソッドは一度しかコールされず、実行したスレッドをブロックします。なので、前述した apply 関数と同じ contract を書くことが可能です。

以下のように拡張関数を書きます。

```kotlin
@UseExperimental(ExperimentalContracts::class)
fun <T : Activity> ActivityScenario<T>.onActivity2(block: (T) -> Unit) {
  contract {
    callsInPlace(block, InvocationKind.EXACTLY_ONCE)
  }
  onActivity {
    block(it)
  }
}


// コンパイルエラーにならない!!
val activity: Activity
scenario.onActivity2 {
    activity = it
}
println(activity)
```

@UseExperimental(ExperimentalContracts::class)をつけることで、ユーザ定義のcontractを定義することが出来ます。

