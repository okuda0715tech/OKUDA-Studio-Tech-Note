- [Destructuring declarations (宣言の構造化 / 分解宣言 / 分割宣言)](#destructuring-declarations-宣言の構造化--分解宣言--分割宣言)
  - [日本語訳](#日本語訳)
  - [概要](#概要)
  - [data クラスのサンプル](#data-クラスのサンプル)
  - [Destructuring declarations の仕組み](#destructuring-declarations-の仕組み)
  - [for ループのサンプル](#for-ループのサンプル)
  - [Map の for ループのサンプル](#map-の-for-ループのサンプル)
  - [Pair / Triple クラスのサンプル](#pair--triple-クラスのサンプル)
  - [4 つ以上のプロパティを持つクラスの場合](#4-つ以上のプロパティを持つクラスの場合)
  - [自分で componentN() 関数を作成する場合](#自分で-componentn-関数を作成する場合)
  - [使用しないプロパティはアンダーラインで処理をスキップして高速化](#使用しないプロパティはアンダーラインで処理をスキップして高速化)
  - [ラムダ式のサンプル](#ラムダ式のサンプル)


# Destructuring declarations (宣言の構造化 / 分解宣言 / 分割宣言)

## 日本語訳

Destructuring declarations の正式な日本語訳が存在しないようですが、  
日本語訳の候補としては以下のようなものがあります。

- 宣言の構造化
- 分解宣言
- 分割宣言


## 概要

Destructuring declarations とは、以下のような形式で定義され、プロパティを自動的に複数の変数に代入する手法です。

```Kotlin
// val でも var でも OK.
val (name, age) = user
```

プロパティ一つ一つに対して、代入文を記述する必要がないため、よりスピーディーに実装ができるようになります。

プロパティの数に制限はなく、プロパティが多ければ多いほど、利用する価値があります。


## data クラスのサンプル

**User.kt**

```Kotlin
data class User(val name: String, val age: Int)
```


**MyClass.kt**

```Kotlin
class MyClass {

    fun myMethod() {
        val user = User("Taro", 23)
        // Destructuring declarations
        val (name, age) = user
        Log.d("test", "Name = ${name}, Age = ${age}")
    }

}
```

## Destructuring declarations の仕組み

```Kotlin
val (name, age) = user
```

上記のコードは、コンパイルされると以下のコードになります。

```Kotlin
val name = user.component1()
val age = user.component2()
```

`componentN()` メソッドは、 `data class` を定義した際に自動的に実装されるコードです。

プライマリコンストラクタのプロパティの定義順に 1 から正の整数が割り当てられ、各プロパティの値を取得するメソッドになっています。


## for ループのサンプル

```Kotlin
for ((a, b) in collection) { ... }
```

`a` と `b` はそれぞれ、コレクションの要素の `component1()` 関数と `component2()` 関数の戻り値を示します。


## Map の for ループのサンプル

Map にも `componentN()` メソッドが実装されているため、 Destructuring declarations が使用可能となっています。

```Kotlin
for ((key, value) in map) {
   // do something with the key and the value
}
```


## Pair / Triple クラスのサンプル

`Pair` クラスや `Triple` クラスは `data class` で実装されているため、自動的に `componentN()` メソッドが実装されます。  
そのため、 Destructuring declarations が使用可能となっています。

```Kotlin
// Pair の例
val (num1, num2) = Pair(1,2)
// Triple の例
val (str1, str2, int1) = Triple("A", "B", 1)
```


## 4 つ以上のプロパティを持つクラスの場合

4 つ以上のプロパティを持つクラスの場合は、 Pair や Triple のようなクラスは用意されていないため、  
自分で `data class` を作成することで Destructuring declarations の仕組みを利用しましょう。


## 自分で componentN() 関数を作成する場合

自分で componentN() 関数を作成する場合には、 `operator` 修飾子を付与する必要があります。  
これがないと、 Destructuring declarations として利用することができません。

```Kotlin
class Lang(
    val name: String,
    val version: Double,
) {
    operator fun component1() = name
    operator fun component2() = version
}

fun main() {
    val kotlin = Lang("Kotlin", 1.7)
    val (name, version) = kotlin
}
```


## 使用しないプロパティはアンダーラインで処理をスキップして高速化

```Kotlin
val (_, status) = getResult()
```

使用しないプロパティが存在する場合は、プロパティ名をアンダーラインにすることで、 `componentN()` 関数の呼び出しをスキップするため、処理の高速化につながります。


## ラムダ式のサンプル

```kotlin
// destructuring declarations を使用しない場合
map.mapValues { entry -> "${entry.value}!" }
// destructuring declarations を使用した場合
map.mapValues { (key, value) -> "$value!" }
```

上記のサンプルでは、 `Map.Entry` クラスが `componentN()` 関数を実装してる (らしい。実装していないように見えたが...) ので、  
ラムダ式の引数部分の `entry` を destructuring devlarations の表現 `(key, value)` に置き換えることが可能になっています。

`Map.Entry` クラスに限らず、 `Pair` など、 `componentN()` 関数を持っているクラスがラムダ式の引数になっていれば、  
destructuring devlarations の表現を使用することが可能になっています。



