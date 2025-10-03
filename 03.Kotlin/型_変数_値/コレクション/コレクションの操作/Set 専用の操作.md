- [Set 専用の操作](#set-専用の操作)
  - [二つの Set を結合する](#二つの-set-を結合する)
  - [二つの Set に共通する要素のみを取り出す](#二つの-set-に共通する要素のみを取り出す)
  - [もう一方の Set に存在しない要素のみを取り出す](#もう一方の-set-に存在しない要素のみを取り出す)
  - [二つの Set に共通しない要素のみを取り出す](#二つの-set-に共通しない要素のみを取り出す)
  - [List に対して union(), intersect(), subtract() を使用する](#list-に対して-union-intersect-subtract-を使用する)


# Set 専用の操作

## 二つの Set を結合する

`union()` 関数、またはその中置記法の `a union b` は、二つの Set を結合します。

並び替え済みのコレクションを結合する場合は、注意してください。第一引数の Set の後ろに第二引数の要素が連結されるため、必要に応じて、結合後に並べ替えを行ってください。

第一引数の Set と第二引数の Set に同一と見なされるオブジェクトが存在する場合には、第一引数の要素が残り、第二引数の要素が除外されます。

新しい LinkedHashSet を生成し、二つの要素の Set の要素を格納して返します。

```kotlin
val numbers = setOf("one", "two", "three")

// output according to the order
println(numbers union setOf("four", "five"))
// [one, two, three, four, five]
println(setOf("four", "five") union numbers)
// [four, five, one, two, three]
```


## 二つの Set に共通する要素のみを取り出す

```kotlin
val numbers = setOf("one", "two", "three")

// 新しい LinkedHashSet を生成し、共通する要素のみを格納して返す。
println(numbers intersect setOf("two", "one"))
// [one, two]
```


## もう一方の Set に存在しない要素のみを取り出す

第一引数の Set から、第二引数の Set に存在しない要素のみを取り出して、新しい Set に格納して返します。

やっていること自体は minus ( `-` ) 演算子と同じと思われますが、 minus 関数はレシーバーと同じ型のオブジェクトを返すのに対して、 subtract 関数はレシーバーがたとえ List 型だったとしても Set 型のオブジェクトを返す点で異なります。詳細は後述しています。

```kotlin
val numbers = setOf("one", "two", "three")

// 新しい LinkedHashSet を生成し、もう一方の Set に存在しない要素のみを格納して返す。
println(numbers subtract setOf("three", "four"))
// [one, two]
```


## 二つの Set に共通しない要素のみを取り出す

```kotlin
val numbers = setOf("one", "two", "three")
val numbers2 = setOf("three", "four")

println((numbers - numbers2) union (numbers2 - numbers))
// [one, two, four]
```


## List に対して union(), intersect(), subtract() を使用する

union(), intersect(), subtract() 関数は、 List に対して使用する
こともできます。ただし、結果は Set で返されます。そのため、共通する要素が存在する場合はマージされます。

```kotlin
val list1 = listOf(1, 1, 2, 3, 5, 8, -1)
val list2 = listOf(1, 1, 2, 2, 3, 5)

// 共通する要素のみを取得して Set で返します。
println(list1 intersect list2)
//[1, 2, 3, 5]

// 二つの List を連結して Set で返します。
println(list1 union list2)
// [1, 2, 3, 5, 8, -1]
```



