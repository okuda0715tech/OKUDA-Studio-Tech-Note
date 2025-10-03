- [Float.NaN](#floatnan)
  - [概要](#概要)
  - [例](#例)


# Float.NaN

## 概要

Float.NaN は、 Not a Number の略です。通常、計算が無効または定義されていないときに使用します。例えば、 0 で割り算をした場合や、不正な平方根を取ろうとした場合に Float.NaN を返します。


## 例

NaN を使う一般的な例としては、計算結果が定義されていない場合に NaN を返すことで、その後の処理でエラーハンドリングを行う場合があります。

```kotlin
fun safeDivide(a: Float, b: Float): Float {
    return if (b == 0.0f) Float.NaN else a / b
}

val result = safeDivide(5.0f, 0.0f)
if (result.isNaN()) {
    println("無効な計算です")
}
```

割る数が 0 かどうかのチェックをせずに除算を実行すると、例外が発生して、アプリが強制終了します。

