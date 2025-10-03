- [Map 専用の操作](#map-専用の操作)
  - [キーで値を取得する](#キーで値を取得する)
  - [全てのキーを Set として取得する](#全てのキーを-set-として取得する)
  - [全ての値を Collection として取得する](#全ての値を-collection-として取得する)
  - [フィルタリング](#フィルタリング)
    - [キーと値の両方でフィルタリングする](#キーと値の両方でフィルタリングする)
    - [キーのみでフィルタリングする](#キーのみでフィルタリングする)
    - [値のみでフィルタリングする](#値のみでフィルタリングする)
  - [プラスとマイナス操作](#プラスとマイナス操作)
    - [plus](#plus)
    - [minus](#minus)
  - [書き込み処理](#書き込み処理)
    - [前提事項](#前提事項)
    - [単一の要素を追加](#単一の要素を追加)
    - [複数の要素を追加](#複数の要素を追加)
    - [要素追加後の並び順](#要素追加後の並び順)
    - [単一もしくは複数の要素の更新](#単一もしくは複数の要素の更新)
    - [単一の要素の削除](#単一の要素の削除)
    - [値を指定して要素を削除する](#値を指定して要素を削除する)


# Map 専用の操作

## キーで値を取得する

キーを指定して値を取得する方法はいくつか存在しています。指定したキーが見つからなかった場合や、見つかっても、その値が null だった場合の挙動が異なります。

| 取得方法             | 一致するキーが見つからなかった場合の挙動 | 一致するキーが見つかったがその値が null だった場合の挙動 |
| -------------------- | ---------------------------------------- | -------------------------------------------------------- |
| get() もしくは [key] | null を返す                              | null を返す                                              |
| getValue()           | 例外を投げる                             | null を返す                                              |
| getOrElse()          | 定義したラムダ式の結果を返す。           | 定義したラムダ式の結果を返す。                           |
| getOrDefault()       | 指定したデフォルト値を返す               | null を返す                                              |
|                      |                                          |                                                          |

以下にいくつかの例を示します。

```kotlin
val numbersMap = mapOf("one" to 1, "two" to 2, "three" to 3)
println(numbersMap.get("one")) // 1
println(numbersMap["one"]) // 1
println(numbersMap.getOrDefault("four", 10)) // 10
println(numbersMap["five"]) // null
//numbersMap.getValue("six")      // exception!

// ---------------------

val map = mutableMapOf<String, Int?>()

// 指定したキーが存在しない場合
println(map.getOrElse("x") { 1 }) // 1

// 指定したキーが存在し、その値が null ではない場合
map["x"] = 3
println(map.getOrElse("x") { 1 }) // 3

// 指定したキーは存在していても、その値が null の場合
map["x"] = null
println(map.getOrElse("x") { 1 }) // 1
```


## 全てのキーを Set として取得する

```kotlin
val numbersMap = mapOf("one" to 1, "two" to 2, "three" to 3)
// Set 型のオブジェクトを返します
println(numbersMap.keys) // [one, two, three]
```


## 全ての値を Collection として取得する

```kotlin
val numbersMap = mapOf("one" to 1, "two" to 2, "three" to 3)
// Iterable 型を継承した Collection 型のオブジェクトを返します。
println(numbersMap.values) // [1, 2, 3]
```


## フィルタリング

ここに記載している方法は、全て、新しい Map を生成して返すため、フィルタリングを行っても、元の Map には影響はありません。


### キーと値の両方でフィルタリングする

`filter()` 関数で要素をフィルタリングすることが可能です。

```kotlin
val numbersMap = mapOf("key1" to 1, "key2" to 2, "key3" to 3, "key11" to 11)
val filteredMap = numbersMap.filter { (key, value) -> key.endsWith("1") && value > 10}
println(filteredMap) // {key11=11}
```

### キーのみでフィルタリングする

`filterKeys()` 関数を使用するで、キーのみをフィルタリング条件に使用することが可能です。

```kotlin
val numbersMap = mapOf("key1" to 1, "key2" to 2, "key3" to 3, "key11" to 11)

val filteredKeysMap = numbersMap.filterKeys { it.endsWith("1") }

println(filteredKeysMap)
// {key1=1, key11=11}
```

### 値のみでフィルタリングする

`filterValues()` 関数を使用するで、値のみをフィルタリング条件に使用することが可能です。

```kotlin
val numbersMap = mapOf("key1" to 1, "key2" to 2, "key3" to 3, "key11" to 11)

val filteredValuesMap = numbersMap.filterValues { it < 10 }

println(filteredValuesMap)
// {key1=1, key2=2, key3=3}
```


## プラスとマイナス操作

### plus

Map における `plus` 関数 ( `+` 演算子) は、二つの Map を統合するか、もしくは、 Map と Pair を統合して、それを新しく生成した Map に格納して返します。

第二引数には、 Pair オブジェクトか Map オブジェクトをとることが可能です。

第一引数と第二引数に同じキーが存在している場合には、第二引数の値が適用されます。

```kotlin
val numbersMap = mapOf("one" to 1, "two" to 2, "three" to 3)

// 第二引数が Pair オブジェクトの場合
println(numbersMap + Pair("four", 4))
// {one=1, two=2, three=3, four=4}

// キーが重複する場合
println(numbersMap + Pair("one", 10))
// {one=10, two=2, three=3}

// 第二引数が Map オブジェクトの場合
println(numbersMap + mapOf("five" to 5, "one" to 11))
// {one=11, two=2, three=3, five=5}
```


### minus

Map における `minus` 関数 ( `-` 演算子) は、 Map から第二引数で指定された要素を除外した Map を新しく生成して返します。

第二引数には、キーを指定します。単一のキーを指定することも可能ですし、 List 型や Set 型で複数のキーを指定することも可能です。

```kotlin
val numbersMap = mapOf("one" to 1, "two" to 2, "three" to 3, "four" to 4)

println(numbersMap - "one")
// {two=2, three=3, four=4}

println(numbersMap - listOf("two", "three"))
// {one=1, four=4}

println(numbersMap - setOf("one", "two"))
// {three=3, four=4}
```

`plusAssign` 関数 ( `+=` 演算子) と `minusAssign` 関数 ( `-=` 演算子) については後述します。


## 書き込み処理

### 前提事項

Map の書き込み処理における前提事項を二つあげます。

1. Map は、値を更新することはできますが、キーを更新することはできません。一度、要素を Map に追加したら、キーは変更不可となります。
2. 読み取り専用の Map に対して書き込み処理を実施することはできません。これらの処理は、元のコレクションを直接更新する操作です。

【参考】  
`+` は単なる Map の結合であり、新しく Map を生成して返しますが、 `+=` は元の Map に対する別の Map の追加であるため、新しく Map を生成することはしません。


### 単一の要素を追加

`put()` 関数、もしくは `[key]` 演算子は、 Map に新しい要素を追加します。

```kotlin
val numbersMap = mutableMapOf("one" to 1, "two" to 2)
numbersMap.put("three", 3)
println(numbersMap)
// {one=1, two=2, three=3}

val numbersMap = mutableMapOf("one" to 1, "two" to 2)
numbersMap["three"] = 3
// {one=1, two=2, three=3}
```


### 複数の要素を追加

`putAll()` 関数を使用することで、一度に複数の要素を Map に追加することが可能です。

関数の引数には、次のいずれかのオブジェクトを使用できます。

- Map オブジェクト
- Pair オブジェクトのグループ
  - Iterable / Sequence / Array のいずれかを使用して要素を Pair オブジェクトにしたもの

```kotlin
val numbersMap = mutableMapOf("one" to 1, "two" to 2, "three" to 3)
numbersMap.putAll(setOf("four" to 4, "five" to 5))
println(numbersMap)
// {one=1, two=2, three=3, four=4, five=5}
```

または、 `plusAssign()` 関数 ( `+=` 演算子) を使用することで、一度に複数の要素を Map に追加することが可能です。

```kotlin
val numbersMap = mutableMapOf("one" to 1, "two" to 2)

// 一つだけ要素を追加することも可能です。
numbersMap += "three" to 3
// 複数の要素を追加する例
numbersMap += mapOf("four" to 4, "five" to 5)
// 関数名で呼び出す例
numbersMap.plusAssign("six" to 6)

println(numbersMap)
// {one=1, two=2, three=3, four=4, five=5, six=6}
```


### 要素追加後の並び順

Map が LinkedHashMap の場合は、追加された要素は、 Map の末尾に追加されます。 SortedMap の場合は、追加された要素は、定義された並び順になるように、キーで並べ替えられた位置に挿入されます。

```kotlin
val numbersMap = mutableMapOf("one" to 1, "two" to 2)
numbersMap.put("three", 3)
println(numbersMap)
// {one=1, two=2, three=3}

val sortedMap = sortedMapOf(1 to "one", 3 to "three")
sortedMap.put(2, "two")
println(sortedMap)
// {1=one, 2=two, 3=three}
```


### 単一もしくは複数の要素の更新

Map の要素を更新する場合は、要素の追加と同じ関数 ( `put()` or `putAll()` ) を使用します。キーが既存の要素と一致している場合、これらの関数は、要素の値を更新します。

`put()` 関数の場合のみ、関数の戻り値として、更新前の値を返します。 ( `putAll()` 関数にはこの機能はありません。)

```kotlin
val numbersMap = mutableMapOf("one" to 1, "two" to 2)
val previousValue = numbersMap.put("one", 11)

println("更新前の値 = $previousValue")
// 更新前の値 = 1

println(numbersMap)
// {one=11, two=2}
```


### 単一の要素の削除

要素を削除するには `remove()` 関数を使用します。キーだけを指定して要素を削除することもできますし、キーと値を両方指定して要素を削除することも可能です。両方を指定した場合は、両方が一致している場合のみ要素を削除します。

なお、複数の要素を一括で削除する関数は用意されていなさそうです。

```kotlin
val numbersMap = mutableMapOf("one" to 1, "two" to 2, "three" to 3)

// キーのみを指定して削除する例
numbersMap.remove("one")
println(numbersMap)
// {two=2, three=3}

// 値が一致せず削除されない例
numbersMap.remove("three", 4)
println(numbersMap)
// {two=2, three=3}
```

もしくは、 `minusAssign()` 関数 ( `-=` 演算子) を使用して要素を削除することが可能です。こちらも単一の要素の削除にのみ対応しているようで、一括で複数の要素を削除することはできないようです。

```kotlin
val numbersMap = mutableMapOf("one" to 1, "two" to 2, "three" to 3)
numbersMap -= "two"
println(numbersMap) // {one=1, three=3}
```


### 値を指定して要素を削除する

値を指定して要素を削除することが可能です。この方法では、最初に値が一致した要素のみを削除するため、それ以降に同じ値の要素が存在していても、それらは削除されません。

また、値指定と同様の記述方法で、キー指定で要素を削除することも可能ですが、これはわずらわしくなるだけなので、あまり使用されないでしょう。

```kotlin
val numbersMap = mutableMapOf("one" to 1, "two" to 2, "three" to 3, "threeAgain" to 3)

// キーを指定して要素を削除
numbersMap.keys.remove("one")
println(numbersMap)
// {two=2, three=3, threeAgain=3}

// 値を指定して要素を削除
numbersMap.values.remove(3)
println(numbersMap)
// {two=2, threeAgain=3}
```






