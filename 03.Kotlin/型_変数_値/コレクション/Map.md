- [Map](#map)
  - [インスタンスの生成と初期化](#インスタンスの生成と初期化)
  - [通常の初期化とメモリを節約した初期化](#通常の初期化とメモリを節約した初期化)
  - [要素の追加 (キー重複時は上書き)](#要素の追加-キー重複時は上書き)
  - [要素の追加 (キー重複時は以前の値を保持)](#要素の追加-キー重複時は以前の値を保持)
  - [新しい Map を生成して要素を追加 (キー重複時は上書き)](#新しい-map-を生成して要素を追加-キー重複時は上書き)
  - [要素の削除](#要素の削除)
  - [要素の更新](#要素の更新)
  - [要素の取得](#要素の取得)
  - [要素が空かどうかの判定](#要素が空かどうかの判定)
  - [要素に特定のキーが含まれているかどうかの判定](#要素に特定のキーが含まれているかどうかの判定)
  - [要素に特定のバリューが含まれているかどうかの判定](#要素に特定のバリューが含まれているかどうかの判定)
  - [キーの一覧を取得](#キーの一覧を取得)
  - [値の一覧を取得](#値の一覧を取得)
  - [Map 同士の一致判定](#map-同士の一致判定)


# Map

## インスタンスの生成と初期化

```Kotlin
// Pair<K, V> を引数にとります。
val m = mutableMapOf(1 to "hoge")

// 複数の Pair を設定可能です。
val m = mutableMapOf(1 to "hoge", 2 to "fuga")

// Immutable な Map を作成する。
// 【参考】
// Map クラスを返すので追加や更新ができない Map になっているが、
// 実際には LinkedHashMap が生成されているので、キャストすれば追加や更新がでる。
val m = mapOf(1 to "hoge", 2 to "fuga")
```

`mutableMapOf()` 関数で生成される Map は、 `LinkedHashMap` です。

`mapOf()` 関数や `mutableMapOf()` 関数は、デフォルトでは `LinkedHashMap` を生成します。 `LinkedHashMap` は要素の追加された順序を保持します。

一方、 `hashMapOf()` 関数は、 `HashMap` を生成します。これは、要素の格納順序は保持しませんが、メモリを節約する効果があります。


## 通常の初期化とメモリを節約した初期化

```kotlin
// 通常の初期化
val numbersMap = mapOf("one" to 1, "two" to 2)

// メモリを節約した初期化
val numbersMap = mutableMapOf<String, Int>().apply { this["one"] = 1; this["two"] = 2 }
```

通常は `to` 中置記法で初期化します。その方が記述量も少なく、見やすいため、メリットがあります。ただし、パフォーマンスを優先したい場合 (メモリ消費を減らし、速度の高速化が必要な場合) は、 Map を生成してから、 `apply` 関数で初期化します。こうすることによって、 `Pair` オブジェクトを生成する必要がない分、処理の高速化が計れます。


## 要素の追加 (キー重複時は上書き)

Java では、 Map の要素の追加には `map.put(key, value)` メソッドを使用しますが、  
Kotlin では、 put 関数は警告が出るため、配列のような記述方法になります。

```Kotlin
val m = mutableMapOf(1 to "hoge", 2 to "fuga")
// key が 3 で、 value が "moge" です。
m[3] = "moge"
```

キーが重複した場合は、上書きされます。


## 要素の追加 (キー重複時は以前の値を保持)

キー重複時に上書きしたくない追加しない場合には、 `putIfAbsent()` 関数を使用します。

```Kotlin
val m = mutableMapOf(1 to "hoge", 2 to "fuga")
m.putIfAbsent(3, "moge")
```

`putIfAbsent()` 関数は、 `put()` 関数と違って、ワーニングは表示されないため、使用しても問題なさそうです。


## 新しい Map を生成して要素を追加 (キー重複時は上書き)

`map.plus(pair)` 関数は、新しい `LinkedHashMap` を生成し、 元の `map` の要素を新しく生成した Map にコピーします。  
そして、要素 `pair` を `put()` 関数で追加します。

`put()` 関数は、キー重複時は上書きします。


## 要素の削除

要素を削除するには、 `remove()` 関数を使用します。

```Kotlin
val m = mutableMapOf(1 to "hoge", 2 to "fuga")
// キーが一致すれば要素を削除
// 削除前のバリューを返します。
m.remove(1)
// キーとバリューが一致した場合のみ要素を削除
// 要素が削除された場合は true を返します。
m.remove(2, "fuga")
```


## 要素の更新

要素を更新するには、 `replace()` メソッドを使用します。

```Kotlin
val m = mutableMapOf(1 to "hoge", 2 to "fuga")
// 更新前の値は気にせず、キーが一致する要素が存在していれば、値を更新します。
// 更新前のバリューを返します。
m.replace(1, "hoge1-2")
// キーとバリューが一致している要素が存在している場合のみ、第三引数の値で要素のバリューを更新します。
// 要素が更新された場合は true を返します。
m.replace(1, "hoge", "hoge1-2")
```


## 要素の取得

`put()` 関数同様に、 `get()` 関数で値を取得するとワーニングが表示されるため、配列のように取得する方法が推奨されています。

```Kotlin
val m = mapOf(1 to "hoge", 2 to "fuga")
// 要素の取得
val s = m[1]
```


## 要素が空かどうかの判定

```Kotlin
// 空かどうか
m.isEmpty()
// 空ではないかどうか
m.isNotEmpty()
```


## 要素に特定のキーが含まれているかどうかの判定

```Kotlin
val m = mapOf(1 to "hoge", 2 to "fuga")
// 方法 1
if (m.containsKey(1)) {
    // 含まれている
}
// 方法 2
if (1 in m) {
    // 含まれている
}
```


## 要素に特定のバリューが含まれているかどうかの判定

```Kotlin
val m = mapOf(1 to "hoge", 2 to "fuga")
// 方法 1
if (m.containsValue("hoge")) {
    // 含まれている
}
// 方法 2
if ("hoge" in m.values) {
    // 含まれている
}
```


## キーの一覧を取得

```kotlin
val numbersMap = mapOf("key1" to 1, "key2" to 2)

println("All keys: ${numbersMap.keys}") // All keys: [key1, key2]
```



## 値の一覧を取得

```kotlin
val numbersMap = mapOf("key1" to 1, "key2" to 2)

println("All values: ${numbersMap.values}") // All values: [1, 2]
```


## Map 同士の一致判定

`==` 演算子で Map 同士が一致しているかどうか判定することができます。 `==` 演算子を使用した場合、以下の 2 つを満たしていれば、一致とみなされます。

- 要素数が同じである
- 全ての要素の `equals(Any?)` 関数で true を返す要素がもう一方の Map 内に存在している
  - 要素の並び順は関係なく、もう一方の Map に同一とみなされる要素が存在していれば、 Map 同士は一致とみなされます。


