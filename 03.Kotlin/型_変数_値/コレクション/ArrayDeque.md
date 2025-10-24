- [ArrayDeque](#arraydeque)
  - [概要](#概要)
  - [サンプル](#サンプル)


# ArrayDeque

## 概要

`ArrayDeque<T>` は両端キューの実装であり、キューの先頭または末尾の両方で要素を追加または削除できます。 そのため、ArrayDeque は Kotlin のスタックとキューの両方のデータ構造の役割も果たします。 ArrayDeque は、必要に応じてサイズを自動的に調整するサイズ変更可能な配列を使用してバックグラウンドで実現されます。 ( List を継承していますが、要素の保持は配列で行っているようです。)


## サンプル

```kotlin
fun main() {
    val deque = ArrayDeque(listOf(1, 2, 3))

    deque.addFirst(0)
    deque.addLast(4)
    println(deque) // [0, 1, 2, 3, 4]

    println(deque.first()) // 0
    println(deque.last()) // 4

    deque.removeFirst()
    deque.removeLast()
    println(deque) // [1, 2, 3]
}
```

