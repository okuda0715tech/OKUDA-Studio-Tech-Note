- [Sequence](#sequence)
  - [Sequence とは](#sequence-とは)
    - [要素を垂直処理](#要素を垂直処理)
    - [水平処理と垂直処理の実行順序](#水平処理と垂直処理の実行順序)
    - [垂直処理の遅延実行](#垂直処理の遅延実行)
    - [Sequence は遅延生成のコレクション](#sequence-は遅延生成のコレクション)
  - [生成](#生成)
    - [要素から生成](#要素から生成)
    - [Iterable オブジェクトから生成](#iterable-オブジェクトから生成)
    - [関数から生成する](#関数から生成する)
    - [かたまりから生成する](#かたまりから生成する)


# Sequence

## Sequence とは

### 要素を垂直処理

Sequence は、 List に似ていますが、 List は処理が **水平実行** であるのに対し、 Sequence は、基本的には処理が **垂直実行** となります。

水平実行とは、関数ごとに実行する方式です。一方、垂直処理とは、要素ごとに実行する方式です。例えば、次のサンプルのように実行されます。

水平実行サンプルコード

```kotlin
val numberList = (1..3).toList()
val resultList = numberList
    .map { println("1st map: $it"); it + 1 }
    .map { println("2nd map: $it"); it + 2 }

println(resultList)
```

実行結果

```
1st map: 1
1st map: 2
1st map: 3
2nd map: 2
2nd map: 3
2nd map: 4
[4, 5, 6]
```

垂直実行サンプルコード

```kotlin
val numberSeq = (1..3).asSequence()
val resultSeq = numberSeq
    .map { println("1st map: $it"); it + 1 }
    .map { println("2nd map: $it"); it + 2 }
    .toList()
println(resultSeq)
```

実行結果

```
1st map: 1
2nd map: 2
1st map: 2
2nd map: 3
1st map: 3
2nd map: 4
[4, 5, 6]
```

Sequence は、基本的には垂直実行と書きましたが、水平実行される場合もあります。それは、どのような関数を実行しているかによります。 `sorted()` 関数や `count()` 関数など、全ての要素を参照しなければ実行できない関数は、水平実行され、それ以外の関数は垂直実行されます。

水平実行される関数のことを **Stateful な操作** や **終端 (terminal) 操作** と呼びます。垂直実行される関数のことを **Stateless な操作** や **中間 (intermediate) 操作** と呼びます。


### 水平処理と垂直処理の実行順序

ドットで連結された関数チェーンの中で、水平関数に当たるまでは、垂直実行されます。すべての要素が、水平関数の直前の垂直関数の実行を終えると水平関数の実行が開始されます。

```kotlin
val result = (2 downTo 0).asSequence()
    .onEach {
        Log.d("test", "operation1, it = $it")
    }
    .onEach {
        Log.d("test", "operation2, it = $it")
    }
    .sorted()
    .onEach {
        Log.d("test", "operation3, it = $it")
    }
    .onEach {
        Log.d("test", "operation4, it = $it")
    }
    .toList()
Log.d("test", "result: $result")
```

実行結果

```
operation1, it = 2
operation2, it = 2
operation1, it = 1
operation2, it = 1
operation1, it = 0
operation2, it = 0
operation3, it = 0
operation4, it = 0
operation3, it = 1
operation4, it = 1
operation3, it = 2
operation4, it = 2
result: [0, 1, 2]
```


### 垂直処理の遅延実行

垂直処理は即時実行されません。水平処理に当たった際に、垂直処理の実行が開始されます。簡単な図にすると以下の通りです。

```
mySequence
.垂直関数1() // 遅延 
.垂直関数2() // 遅延
.垂直関数3() // 遅延
.水平関数1() // 垂直関数1 から 垂直関数3 までの実行を開始し、完了したら水平関数1 を開始する
.垂直関数4() // 遅延
.水平関数2() // 垂直関数4 を開始し、完了したら水平関数2 を開始する
```

つまり、水平処理は直前までの垂直処理を開始し、その後、自分自身の水平処理を即時実行します。


### Sequence は遅延生成のコレクション

Sequence オブジェクトは遅延生成のコレクションです。そのため、必要になってから初めて要素が生成されます。

```kotlin
fun main() {
    val seq = sequence {
        println("Start sequence")
        yield(1)
        println("After yield 1")
        yield(2)
        println("After yield 2")
        yield(3)
        println("After yield 3")
    }

    // シーケンスの要素を反復処理
    for (value in seq) {
        println("Received $value")
    }
}
```

`sequence` 関数の使用方法については、本ドキュメントの最後の項目を参照してください。

```
実行結果

Start sequence
Received 1
After yield 1
Received 2
After yield 2
Received 3
After yield 3
```

実行結果を見ると、 sequence ブロック内のコードが開始されてから、 yield 関数実行後に一時停止し、次の要素が要求されると再開されることがわかります。シーケンスは必要なときにのみ要素を生成し、メモリの効率的な使用を可能にします。この特性により、大規模なデータセットや無限シーケンスの処理に適しています。


## 生成

### 要素から生成

```kotlin
val numbersSequence = sequenceOf("four", "three", "two", "one")
```


### Iterable オブジェクトから生成

List や Set などの Iterable オブジェクトから生成する方法は以下の通りです。

```kotlin
val numbers = listOf("one", "two", "three", "four")
val numbersSequence = numbers.asSequence()
```


### 関数から生成する

`generateSequence()` 関数を使用することによって、 Sequence を生成することができます。 引数に与えられた 「次の値を生成する関数」 が null を返すと Sequence の生成がストップします。

```kotlin
// it は一つ前の要素を表します。
val oddNumbers = generateSequence(1) { it + 2 }
println(oddNumbers.take(5).toList()) // [1, 3, 5, 7, 9]

// 【NG】 シーケンスは無限のためエラーになります。
// println(oddNumbers.count())

// null を返してシーケンスをストップする例
val oddNumbersLessThan10 = generateSequence(1) {
    if (it < 8) it + 2 else null
}
println(oddNumbersLessThan10.toList()) // [1, 3, 5, 7, 9]
println(oddNumbersLessThan10.count()) // 5
```


`generateSequence()` 関数には 3 種類のオーバーロードがあります。

**1. 引数に関数型を一つ持つタイプ**

関数のシグネチャ

```kotlin
fun <T : Any> generateSequence(
    nextFunction: () -> T?
): Sequence<T>
```

使用例

```kotlin
var count = 3

val sequence = generateSequence {
    (count--).takeIf { it > 0 } // it が 0 になると null を返します。
}

println(sequence.toList()) // [3, 2, 1]

// sequence.forEach {  }  // <- iterating that sequence second time will fail
```

**2. 引数にプロパティと関数型を一つずつ持つタイプ**

```kotlin
fun <T : Any> generateSequence(
    seed: T?,
    nextFunction: (T) -> T?
): Sequence<T>
```

```kotlin
fun fibonacci(): Sequence<Int> {
    // fibonacci terms
    // 0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610, 987, 1597, 2584, 4181, 6765, 10946, ...
    return generateSequence(Pair(0, 1), { Pair(it.second, it.first + it.second) }).map { it.first }
}

println(fibonacci().take(10).toList()) // [0, 1, 1, 2, 3, 5, 8, 13, 21, 34]
```


**3. 引数に関数型を二つ持つタイプ**

```kotlin
fun <T : Any> generateSequence(
    seedFunction: () -> T?,
    nextFunction: (T) -> T?
): Sequence<T>
```

サンプルはややこしかったので省略します。


### かたまりから生成する

`sequence()` 関数を使用することで、かたまりから Sequence を生成することが可能です。

この関数は、 `yield()` 関数と `yieldAll()` 関数の呼び出しを含むラムダ式を受け取ります。これらは要素をシーケンスコンシューマに返します。 yield() 関数の引数には、要素としてコンシューマーに渡したい値を与えます。 yieldAll() 関数は、Iterable オブジェクト、Iterator、または別の Sequence を受け取ることができます。 yieldAll() の Sequence 引数は無限にすることができます。ただし、そのような呼び出しは最後でなければなりません。なぜなら、後続の処理に到達することがないためです。

```kotlin
val oddNumbers = sequence {
    // yield() 関数には要素が一つだけのものを渡せる
    yield(1)
    // yieldAll() 関数には要素が複数のものが渡せる
    yieldAll(listOf(3, 5))
    // 無限になるものは sequence() 関数内の最後に記述する
    yieldAll(generateSequence(7) { it + 2 })
}
// yield() や yieldAll() で渡した要素が一つの Sequence に格納されている。
println(oddNumbers.take(5).toList()) // [1, 3, 5, 7, 9]
```

Sequ

