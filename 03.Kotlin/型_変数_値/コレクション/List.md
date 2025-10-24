- [List](#list)
  - [List の生成](#list-の生成)
  - [LinkedList を生成する](#linkedlist-を生成する)
  - [関数を使用したリストの生成](#関数を使用したリストの生成)
  - [要素の除外](#要素の除外)
    - [minus() 関数とマイナス演算子](#minus-関数とマイナス演算子)
    - [フィルター](#フィルター)
  - [List 同士の一致判定](#list-同士の一致判定)


# List

## List の生成

```kotlin
// List<T> を生成
val list1: List<String> = listOf()
// 値が null である要素を除外した List<T> を生成
val list2: List<String> = listOfNotNull("a", null, "b") // ["a", "b"]
// ArrayList<T> を生成
val list3: List<String> = arrayListOf()
// MutableList<T> を生成
val list4: List<String> = mutableListOf()
```

`MutableList<T>` は、実際には `ArrayList<T>` を生成しているため、 `arrayListOf()` と `mutableListOf()` は同じものを返します。どちらを使用するかこだわりがなければ、 Kotlin から新しく登場した `mutableListOf()` を使用すれば良いかなと思います。

## LinkedList を生成する

LinkedList を生成したい場合は、 `LinkedList` のコンストラクタを使用します。

```kotlin
val linkedList = LinkedList<String>(listOf("one", "two", "three"))
```


## 関数を使用したリストの生成

List の場合は、引数を二つ持つコンストラクタで、関数を使用して、要素を初期化することが可能です。

```kotlin
// 第一引数は、要素数です。
// 第二引数は、要素を初期化する関数です。
val doubled = List(3) { it * 2 }
println(doubled) // [0, 2, 4]
```


## 要素の除外

### minus() 関数とマイナス演算子

`minus()` 関数やマイナス演算子 ( `-` ) を使用する場合は、元の List オブジェクトとは別の List オブジェクトが生成されて、それが返されます。

```kotlin
val list = listOf(10, 20, 30, 40)

// minus 関数を使用した例
println(list.minus(40)) // [10, 20, 30]

// マイナス演算子 ( - ) を使用した例
println(list - listOf(10, 20, 30)) // [40]
```

`minusAssign()` 関数やマイナスイコール演算子 ( `-=` ) を使用する場合は、元の List オブジェクトから要素が除外され、元のオブジェクトが返されます。

`minusAssign()` 関数やマイナスイコール演算子 ( `-=` ) が使用できるのは、 `MutableCollection<T>` の場合のみです。

```kotlin
val list1 = mutableListOf(10, 20, 30)
list1.minusAssign(20) // [10, 30]

val list2 = mutableListOf(10, 20, 30, 40)
list2 -= listOf(10, 20) // [30, 40]
```

### フィルター

```kotlin
val numbers = listOf("one", "two", "three", "four")  
val longerThan3 = numbers.filter { it.length > 3 }
println(longerThan3) // [three, four]
```

`filter()` 関数の特徴は以下の通りです。

- 浅いコピーであるため、要素に加えた変更は、元のコレクションとコピー先のコレクションで同期します。
- 新しい List を生成して返すため、要素の追加や削除は、コピー元とコピー先で同期しません。


## List 同士の一致判定

`==` 演算子で List 同士が一致しているかどうか判定することができます。 `==` 演算子を使用した場合、以下の 2 つを満たしていれば、一致とみなされます。

- 要素数が同じである
- 全ての要素の `equals(Any?)` 関数が true を返す
  - Map と Set の場合は、要素の並び順は関係ありませんが、 List の場合は要素の並び順も一致していないと同一とは見なされません。




