- [use 関数](#use-関数)
  - [概要](#概要)
  - [使用例](#使用例)
  - [シグネチャ](#シグネチャ)


# use 関数

## 概要

- use 関数は、 Closeable インターフェースの拡張関数です。
- use のラムダ式内で、 Closeable オブジェクトを使用した任意のコードを実行します。
- 任意のコードの実行中に例外が発生しても、しなくても、最後に必ず close() を呼び出します。
- これにより、 finally ブロックを記述する必要がなくなり、コードがシンプルになります。
- use 関数のラムダ式の最終行で実行された処理の結果を、 use 関数の戻り値として返します。


## 使用例

```kotlin
import java.io.File

fun main() {
    val firstLine = File("example.txt").bufferedReader().use { reader ->
        reader.readLine()  // 最後の式の結果が戻り値になる
    }

    println("ファイルの1行目: $firstLine")
}
```

```
実行結果（ファイルの中身の一行目が 「Hello, Kotlin!」 の場合）

ファイルの1行目: Hello, Kotlin!
```

- `.use { reader -> }`
  - reader には、 `bufferedReader()` の結果が格納されています。


## シグネチャ

use 関数のメソッドシグネチャは、以下の公式ドキュメントを参照してください。

- [use 関数](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.io/use.html)

