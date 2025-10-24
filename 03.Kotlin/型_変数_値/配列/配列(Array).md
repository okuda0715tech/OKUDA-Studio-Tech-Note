- [配列（Array）](#配列array)
  - [概要](#概要)
  - [要素がプリミティブ型の配列とオブジェクト型の配列](#要素がプリミティブ型の配列とオブジェクト型の配列)
  - [配列の使いどころ](#配列の使いどころ)
  - [配列を作成する](#配列を作成する)
    - [arrayOf() 関数を使用する例](#arrayof-関数を使用する例)
    - [arrayOfNulls() 関数を使用する例](#arrayofnulls-関数を使用する例)
    - [emptyArray() 関数を使用する例](#emptyarray-関数を使用する例)
    - [Array コンストラクタを使用する例](#array-コンストラクタを使用する例)
  - [ネストされた配列](#ネストされた配列)
  - [要素の参照と更新](#要素の参照と更新)
  - [配列同士が同一かの比較](#配列同士が同一かの比較)
  - [配列とコレクションの変換](#配列とコレクションの変換)
    - [配列から List への変換](#配列から-list-への変換)
    - [配列から Set への変換](#配列から-set-への変換)
    - [配列から Map への変換](#配列から-map-への変換)
  - [プリミティブ型の配列を扱う場合](#プリミティブ型の配列を扱う場合)
    - [基本](#基本)
    - [Array クラスと XxxArray クラスの相互変換](#array-クラスと-xxxarray-クラスの相互変換)


# 配列（Array）

## 概要

配列は、同じ型 or そのサブタイプを要素に持ち、要素の数が固定長の型です。


## 要素がプリミティブ型の配列とオブジェクト型の配列

要素の型にプリミティブ型を使用したい場合には、 `BooleanArray` 等のその型専用のクラスを使用するようにしましょう。そうしないと、 Boxing と Unboxing が発生するため、パフォーマンスに悪影響を及ぼします。


## 配列の使いどころ

満たす必要のある特殊な低レベルの要件がある場合は、Kotlin で配列を使用します。たとえば、通常のアプリケーションに必要な以上のパフォーマンス要件がある場合や、 Array 型のデータ構造を構築する必要がある場合などです。このような制限がない場合は、代わりにコレクションを使用してください。

コレクションには、配列と比較して次の利点があります。

- コレクションは読み取り専用にすることができるため、より詳細な制御が可能になり、明確な意図を持つ堅牢なコードを作成できます。

- コレクションに要素を追加したり、コレクションから要素を削除したりするのは簡単です。これに対して、配列のサイズは固定されています。配列に要素を追加または配列から要素を削除する唯一の方法は、毎回新しい配列を作成することですが、これは非常に非効率です。

```kotlin
var riversArray = arrayOf("Nile", "Amazon", "Yangtze")

// 「 += 」 演算子は、元の配列の要素をコピーし、新しい riversArray 配列を作成し、 "Mississippi" を追加します。
riversArray += "Mississippi"
println(riversArray.joinToString()) // Nile, Amazon, Yangtze, Mississippi
```

- 等価演算子 ( `==` ) を使用して、コレクションが構造的に等しいかどうかを確認できます。この演算子は配列には使用できません。代わりに、特別な関数を使用する必要があります。詳細については、「配列の比較」を参照してください。


## 配列を作成する

配列を作成するには以下のいずれかの方法を使用します。

1. 次のいづれかの関数を使用する。
   - `arrayOf()` / `arrayOfNulls()` / `emptyArray()`
2. `Array` コンストラクタを使用する。


### arrayOf() 関数を使用する例

```kotlin
// [1, 2, 3] という配列を作成します。
val simpleArray = arrayOf(1, 2, 3)
println(simpleArray.joinToString()) // 1, 2, 3
```


### arrayOfNulls() 関数を使用する例

`arrayOfNulls()` 関数は、指定されたサイズの配列を生成し、その要素が全て null になります。

```kotlin
// [null, null, null] という配列を生成します。
val nullArray: Array<Int?> = arrayOfNulls(3)
println(nullArray.joinToString()) // null, null, null
```


### emptyArray() 関数を使用する例

`emptyArray()` 関数は、要素数が 0 で、要素の型が指定した型の配列を返します。

```kotlin
// 要素の型は、右辺か左辺のどちらかに定義されていれば、もう一方を型推論することが可能です。

// 型推論で左辺の型を省略したパターン
var exampleArray = emptyArray<String>()
// 型推論で右辺の型を省略したパターン
var exampleArray: Array<String> = emptyArray()
```

空の配列は、フォールバックとして使用されることがあります。

```kotlin
return someInputArray?.filterNotNull() ?: emptyArray()
```


### Array コンストラクタを使用する例

Array コンストラクタは、要素数と要素の値を返す関数を引数に取ります。要素の値を返す関数は、要素のインデックスを引数に取ります。

```kotlin
// Array<Int> 型で要素の値が [0, 0, 0] の配列を生成します。
val initArray = Array<Int>(3) { 0 }
println(initArray.joinToString()) // 0, 0, 0

// Array<String> 型で要素の値が ["0", "1", "4", "9", "16"] の配列を生成します。
val asc = Array(5) { i -> (i * i).toString() }
asc.forEach { print(it) } // 014916
```


## ネストされた配列

配列をネストすることで多次元配列を生成することができます。

```kotlin
// 二次元配列
val twoDArray = Array(2) { Array<Int>(2) { 0 } }
println(twoDArray.contentDeepToString())
// [[0, 0], [0, 0]]

// 三次元配列
val threeDArray = Array(3) { Array(3) { Array<Int>(3) { 0 } } }
println(threeDArray.contentDeepToString())
// [
//  [[0, 0, 0], [0, 0, 0], [0, 0, 0]],
//  [[0, 0, 0], [0, 0, 0], [0, 0, 0]],
//  [[0, 0, 0], [0, 0, 0], [0, 0, 0]]
// ]
```

多次元配列は、要素数を統一する必要はありません。
任意の要素数で多次元配列を定義することが可能です。


## 要素の参照と更新

配列の要素は、 mutable なオブジェクトです。

インデックスアクセスオペレーター `[]` で要素を指定して、参照・更新を行います。

```kotlin
val simpleArray = arrayOf(1, 2, 3)
val twoDArray = Array(2) { Array<Int>(2) { 0 } }

simpleArray[0] = 10
twoDArray[0][0] = 2

println(simpleArray[0].toString()) // 10
println(twoDArray[0][0].toString()) // 2
```


## 配列同士が同一かの比較

`contentEquals()` 関数か `contentDeepEquals()` 関数を使用することで、配列同士が同一かどうかを比較することができます。 `contentEquals()` 関数は、一次元配列の場合に使用するもので、 `contentDeepEquals()` 関数は、多次元配列の場合に使用する関数です。

```kotlin
val simpleArray = arrayOf(1, 2, 3)
val anotherArray = arrayOf(1, 2, 3)

println(simpleArray.contentEquals(anotherArray)) // true

simpleArray[0] = 10
// 中置記法 (infix notation)
println(simpleArray contentEquals anotherArray) // false
```

`==` 演算子と `!=` 演算子は使用しないでください。これらは、同一のインスタンスを参照しているかどうかを返すものです。


## 配列とコレクションの変換

配列とコレクションは相互に変換することができます。


### 配列から List への変換

```kotlin
val simpleArray = arrayOf("a", "b", "c", "c")

println(simpleArray.toList()) // [a, b, c, c]
```


### 配列から Set への変換

配列から Set へ変換すると、重複した要素は一つにまとめられます。

```kotlin
val simpleArray = arrayOf("a", "b", "c", "c")

println(simpleArray.toSet()) // [a, b, c]
```


### 配列から Map への変換

配列の要素の型が `Pair<K,V>` の場合のみ、 Map へ変換することができます。

キーが重複している場合は、インデックスが後ろの値で上書きされます。

```kotlin
// キーがフルーツの名前、値がそのカロリーを想定しています。
val pairArray = arrayOf("apple" to 120, "banana" to 150, "cherry" to 90, "apple" to 140)

println(pairArray.toMap()) // {apple=140, banana=150, cherry=90}
```


## プリミティブ型の配列を扱う場合

### 基本

プリミティブ型の配列を扱う場合は、専用の Array クラスを使用することで、 Boxing によるパフォーマンス低下を避けることができます。

| 要素の型 | 専用の配列クラス |
| -------- | ---------------- |
| boolean  | BooleanArray     |
| byte     | ByteArray        |
| char     | CharArray        |
| double   | DoubleArray      |
| float    | FloatArray       |
| int      | IntArray         |
| long     | LongArray        |
| short    | ShortArray       |

プリミティブ型専用の配列クラス XxxArray は、 Array クラスとの継承関係はあませんが、 Array クラスと同じように使用可能です。

```kotlin
// 要素の型が Int で、サイズが 5 の配列を生成
val exampleArray = IntArray(5)
println(exampleArray.joinToString()) // 0, 0, 0, 0, 0
```


### Array クラスと XxxArray クラスの相互変換

XxxArray 型を Array 型に変換するには `toTypedArray()` 関数を使用します。

Array 型を XxxArray 型に変換するには `toIntArray()` 関数や `toDoubleArray()` 関数を使用します。









