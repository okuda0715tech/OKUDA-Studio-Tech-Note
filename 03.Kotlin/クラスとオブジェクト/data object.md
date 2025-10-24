- [data object](#data-object)
  - [概要](#概要)
  - [使いどころ](#使いどころ)
  - [使用例](#使用例)
  - [toString() メソッドの自動実装](#tostring-メソッドの自動実装)
  - [equals() メソッドの自動実装](#equals-メソッドの自動実装)
  - [hashCode() メソッドの自動実装](#hashcode-メソッドの自動実装)


# data object

## 概要

`data object` は、以下の関数を自動実装してくれる機能です。

- toString()
- equals()
- hashCode()

特に `toString()` 関数に意味があり、これは、ボイラープレートコードを削減してくれます。


## 使いどころ

**sealed クラスや sealed インターフェースの内部で使用します。**

`data object` が Kotlin の機能に導入される前までは、  
`data object` の代わりに `object` が使用されていました。  
以下は `object` が使用されていた場合のサンプルです。

```kotlin
sealed interface ProfileScreenState {
    data class Success(val username: String): ProfileScreenState
    object Error: ProfileScreenState
    object Loading: ProfileScreenState
}
```

上記の実装を行った場合、 `toString()` 関数は以下の内容を出力します。

```
com.dataobjects.example.ProfileScreenState$Loading@6d03e736
Success(username=exampleUser1)
com.dataobjects.example.ProfileScreenState$Error@5fd0d5ae
```

`Error` と `Loading` の `toString()` 関数だけ、  
「パッケージ名」 と 「インスタンスのアドレス情報」 が出力されます。  
これはあまり意味がない情報なので、削除したほうが良いです。

また、 `Error` と `Loading` の出力は、 `Success` の出力とフォーマットが異なっており、  
`Success` のフォーマットに揃えた方が良いことがわかります。

`Error` と `Loading` の `toString()`　関数をオーバーライドすることで、  
上記の課題を解決することが可能ですが、そのコードはボイラープレートであるため、  
Kotlin では、 `object` を `data object` に変更するだけで、  
オーバーライドを行わなくて済むようになっています。


## 使用例

```kotlin
sealed interface ProfileScreenState {
    data class Success(val username: String) : ProfileScreenState
    data object Error : ProfileScreenState
    data object Loading : ProfileScreenState
}
```

`object` を `data object` に変更するだけで、 `toString()` 関数の出力が以下のように整います。

```
Loading
Success(username=exampleUser1)
Error
```


## toString() メソッドの自動実装

自動実装される `toString()` メソッドでは、オブジェクトの名前が出力されるような実装になっています。

```Kotlin
object SampleObject
data object SampleDataObject

fun main() {
    println(SampleObject)     // => SampleObject@58ceff1 (名前とハッシュ値)
    println(SampleDataObject) // => SampleDataObject (名前のみ)
}
```


## equals() メソッドの自動実装

**「 object の equals() 」 と 「 data object の equals() 」 の違い**

| equals() 関数が生成される場所 | equals() 関数の動作                                          |
| ----------------------------- | ------------------------------------------------------------ |
| data object 内                | オブジェクトの名前 (型) が同じならば、 `true` を返す。       |
| object 内                     | 同じインスタンスを参照しているならば、 `true` を返す。 |


**使い分け方**

```
リフレクションを使用して、シングルトンではなく、複数インスタンスを生成する場合には、
data object を使用することを検討します。
```

`object` 宣言されたオブジェクトは、本来シングルトンになるため、  
`data object` の `equals()` 関数と `object` の `equals()` 関数は同じ動きをします。

ただし、リフレクションを使用すると、シングルトンではなく、  
複数のインスタンスを作成することが可能になります。

その場合は、 `data` 修飾子を付与していると、クラス名が同じ場合でも  
`equals()` メソッドが `true` を返してくれるため、  
自分で `equals()` メソッドをオーバーライドする手間がなくなり、生産性があがります。

( `data` 修飾子を付与した場合、 `equals()` メソッドの実装を変更することはできません。)

```
【注意点】
data 修飾子を付与した場合は、 == 演算子で比較することを忘れないでください。

== 演算子は、 equals() メソッドを呼び出しますが、
=== 演算子は、アドレスを参照し、そのアドレスで比較を行います。

そのため、 === 演算子で比較を行ってしまうと、せっかく、別のインスタンスでも
true と判定されるはずだったものが、 false と判定されてしまいます。
```


## hashCode() メソッドの自動実装

`hashCode()` メソッドの実装を変更することはできません。

