- [Set](#set)
  - [Set の生成](#set-の生成)
  - [Set 同士の一致判定](#set-同士の一致判定)


# Set

## Set の生成

```kotlin
// *******************
// setOf 関数の使用例
// *******************
val set1 = setOf<String>() // []
val set2 = setOf("a", "b") // [a, b]

// ************************
// setOfNotNull 関数の使用例
// 【注意】
// listOfNotNull と setOfNotNull は使い方が少し違います。
// listOfNotNull は、要素が null の値を除外したリストを生成します。
// setOfNotNull は、引数が null の場合に空 (要素数 0 ) の Set を生成します。
// ************************
val set3 = setOfNotNull("a", "b", "c")

val array1 = arrayOf<String>()

val set4 = setOfNotNull(*array1) // []
// NG (アスタリスクがない)
val set5 = setOfNotNull(array1) // [[Ljava.lang.String;@82743e7]
// NG (引数には要素を直接渡すべき)
// そうしないと Set が二重になってしまう。
val set6 = setOfNotNull(setOf("a", null, "b")) // [[a, null, b]]

val list1 = listOfNotNull("a", null, "b") // [a, b]

val hashSet = hashSetOf<String>()
val linkedSet = linkedSetOf<String>()
// 
val mutableSet = mutableSetOf<String>()
```

`setOf()` 関数や `mutableSetOf()` 関数は、デフォルトでは `LinkedHashSet` を生成します。そのため、 `first()` 関数や `last()` 関数など、順序に関係した関数は機能します。

一方で、 `hashSetOf()` 関数は、 `HashSet` を生成します。これは、要素の格納順序は保持しませんが、メモリを節約する効果があります。


## Set 同士の一致判定

`==` 演算子で Set 同士が一致しているかどうか判定することができます。 `==` 演算子を使用した場合、以下の 2 つを満たしていれば、一致とみなされます。

- 要素数が同じである
- 全ての要素の `equals(Any?)` 関数で true を返す要素がもう一方の Set 内に存在している
  - 要素の並び順は関係なく、もう一方の Set に同一とみなされる要素が存在していれば、 Set 同士は一致とみなされます。






