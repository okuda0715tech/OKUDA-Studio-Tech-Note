- [List 専用の操作](#list-専用の操作)
  - [インデックスによる単一要素の取得](#インデックスによる単一要素の取得)
  - [複数要素の取得](#複数要素の取得)
  - [要素の値からインデックスを取得する](#要素の値からインデックスを取得する)
  - [条件を満たす最初の要素のインデックスを取得する](#条件を満たす最初の要素のインデックスを取得する)
  - [ソートされたリストのバイナリサーチ](#ソートされたリストのバイナリサーチ)
  - [Comparator を使用したバイナリサーチ](#comparator-を使用したバイナリサーチ)
  - [Comparison バイナリサーチ](#comparison-バイナリサーチ)
  - [書き込み処理](#書き込み処理)
    - [要素の追加](#要素の追加)
    - [要素の更新](#要素の更新)
    - [要素の除外](#要素の除外)
    - [要素の並べ替え](#要素の並べ替え)


# List 専用の操作

## インデックスによる単一要素の取得

```kotlin
val numbers = listOf(1, 2, 3, 4)
println(numbers.get(0)) // 1
println(numbers[0]) // 1

// 指定したインデックスに要素が存在しなかった場合
numbers.get(5) // 例外が発生
println(numbers.getOrNull(5)) // null
println(numbers.getOrElse(5, {it})) // 5 (ラムダ式の結果を返す)
```


## 複数要素の取得

「要素をまとめて取得する」 のドキュメントに記載した共通の取得処理に加え、 List 独自の取得方法が用意されています。それは `subList()` 関数を使用した方法です。

この関数は元の List のビューを返します。つまり、元のコレクションが更新されれば、ビューにも反映されますし、その逆もしかりです。

```kotlin
val numbers = (0..10).toList()
// 第二引数の一つ手前までの要素を返します。
println(numbers.subList(3, 6)) // [3, 4, 5]
```


## 要素の値からインデックスを取得する

引数で指定したオブジェクトと同じオブジェクトが最初に見つかったインデックスを返します。 `indexOf()` 関数は先頭から検索を行い、 `lastIndexOf()` 関数は末尾から検索を行います。

```kotlin
val numbers = listOf(1, 5, 10, 15, 50, 100)
println(numbers.indexOf(10)) // 2
println(numbers.indexOf(30)) // -1
println(numbers.lastIndexOf(50)) // 4
```


## 条件を満たす最初の要素のインデックスを取得する

条件を満たす最初の要素のインデックスを取得することが可能です。 `indexOfFirst()` 関数は先頭から検索し、 `indexOfLast()` 関数は末尾から検索します。いずれの関数も、該当する要素が存在しない場合は -1 を返します。

```kotlin
val numbers = mutableListOf(1, 13, 101, 151)
println(numbers.indexOfFirst { it > 50}) // 2
println(numbers.indexOfLast { it % 2 == 1}) // 3
println(numbers.indexOfFirst { it > 1000}) // -1
```


## ソートされたリストのバイナリサーチ

リスト内の要素を検索するもう 1 つの方法に、二分探索があります。 他の組み込み検索関数よりも大幅に高速に動作しますが、リストを特定の順序 (自然順序、または関数パラメーターで指定された別の順序) に従って昇順に並べ替えされている必要があります。それ以外の場合、結果は保証されません。

ソートされたリスト内の要素を検索するには、値を引数として渡して binarySearch() 関数を呼び出します。同じと見なされる要素が存在する場合、関数はそのインデックスを返します。存在しない場合は、(-insertionPoint - 1) を返します。ここで、insertionPoint は、検索中の要素がそのリストのどこに挿入されたら、リストのソートが崩れないかを表すインデックスです。指定された値を持つ要素が複数ある場合、検索ではそれらのインデックスのいずれかを返します。どれを返すかは保証されません。

```kotlin
val numbers = mutableListOf("one", "two", "three", "four")
numbers.sort()
println(numbers) // [four, one, three, two]
println(numbers.binarySearch("two"))  // 3
println(numbers.binarySearch("z")) // -5
// 第二引数と第三引数でサーチする範囲を指定できます。
// 以下の場合は 0 <= x < 2 の範囲をサーチします。
// 第三引数のインデックスは含まれないため注意してください。
println(numbers.binarySearch("two", 0, 2))  // -3
```


## Comparator を使用したバイナリサーチ

コレクションの要素が Comparable インターフェースを実装していないけれど、バイナリサーチを使用したい場合は、 Comparator を使用したバイナリサーチを使用します。

コレクションは、あらかじめ、 Comparator の比較方法で昇順にソートされている必要があります。

```kotlin
val productList = listOf(
    Product("WebStorm", 49.0),
    Product("AppCode", 99.0),
    Product("DotTrace", 129.0),
    Product("ReSharper", 149.0))

println(productList.binarySearch(Product("AppCode", 99.0), compareBy<Product> { it.price }.thenBy { it.name }))
// 1
```


## Comparison バイナリサーチ

Comparison バイナリサーチは、要素が Comparable でもなく、 Comparator も使用したくない場合に有効な方法です。

要素の特定のプロパティで大小比較を行いたい場合に有効です。要素は、大小比較を行いたいプロパティの昇順にあらかじめソートされている必要があります。

```kotlin
import kotlin.math.sign
data class Product(val name: String, val price: Double)

fun priceComparison(product: Product, price: Double) = sign(product.price - price).toInt()

fun main() {
    val productList = listOf(
        Product("WebStorm", 49.0),
        Product("AppCode", 99.0),
        Product("DotTrace", 129.0),
        Product("ReSharper", 149.0))

    println(productList.binarySearch { priceComparison(it, 99.0) })
    // 1
}
```


## 書き込み処理

### 要素の追加

`add()` 関数、もしくは `addAll()` 関数で要素を追加することが可能です。関数の第一引数には、要素を挿入するインデックスを指定します。元々の要素をその分後ろにずらされます。

```kotlin
val numbers = mutableListOf("one", "five", "six")
numbers.add(1, "two")
numbers.addAll(2, listOf("three", "four"))
println(numbers)
// [one, two, three, four, five, six]
```


### 要素の更新

`[]` 演算子、もしくは `set()` 関数で、指定した要素を更新することが可能です。基本的には `[]` 演算子の使用が推奨されます。

```kotlin
val numbers = mutableListOf("one", "five", "three")
numbers[1] =  "two"
println(numbers) // [one, two, three]
numbers.set(2, "THREE")
println(numbers) // [one, two, THREE]
```

`fill()` 関数は全ての要素を同一のオブジェクトで更新します。

```kotlin
val numbers = mutableListOf(1, 2, 3, 4)
numbers.fill(3)
println(numbers) // [3, 3, 3, 3]
```


### 要素の除外

`removeAt()` 関数は、引数で指定したインデックスの要素を除外し、それ以降の要素を一つ前にずらします。

```kotlin
val numbers = mutableListOf("one", "two", "three")    
numbers.removeAt(1)
println(numbers) // [one, three]
```


### 要素の並べ替え

ここで紹介する List の並べ替え操作は、新しいコレクションを生成して返すのではなく、元の List 内の要素の順番を変更します。

元の List の要素の順番を変更する関数は、新しい List を生成して並べ替える関数と同じような名前の関数が用意されています。ただし、末尾に `ed` や `d` が付いていません。以下の表は、そのような関係になっている関数の一部の例です。

| 新しい List を生成する関数 | 元の List を返す関数 |
| -------------------------- | -------------------- |
| sorted()                   | sort()               |
| sortedDescending()         | sortDescending()     |
| sortedBy()                 | sortBy()             |
| shuffled()                 | shuffle()            |
| reversed()                 | reverse()            |

サンプルコードを以下に掲載します。

```kotlin
val numbers = mutableListOf("one", "two", "three", "four")

numbers.sort()
println("Sort into ascending: $numbers")
// Sort into ascending: [four, one, three, two]
numbers.sortDescending()
println("Sort into descending: $numbers")
// Sort into descending: [two, three, one, four]

numbers.sortBy { it.length }
println("Sort into ascending by length: $numbers")
// Sort into ascending by length: [two, one, four, three]
numbers.sortByDescending { it.last() }
println("Sort into descending by the last letter: $numbers")
// Sort into descending by the last letter: [four, two, one, three]

// 文字列の長さが同じ場合はアルファベット順にソート
numbers.sortWith(compareBy<String> { it.length }.thenBy { it })
println("Sort by Comparator: $numbers")
// Sort by Comparator: [one, two, four, three]

numbers.shuffle()
println("Shuffle: $numbers")
// Shuffle: [four, one, two, three]

numbers.reverse()
println("Reverse: $numbers")
// Reverse: [three, two, one, four]
```




