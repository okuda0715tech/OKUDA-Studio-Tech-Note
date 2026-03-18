- [invoke が使われているところ](#invoke-が使われているところ)
- [1. 自分で定義する `invoke` 関数](#1-自分で定義する-invoke-関数)
- [2. 関数型でも同じ仕組み](#2-関数型でも同じ仕組み)
- [3. Kotlinの関数型の正体](#3-kotlinの関数型の正体)
- [4. つまり整理すると](#4-つまり整理すると)
    - [① 自分で定義する invoke](#-自分で定義する-invoke)
    - [② 関数型の呼び出し](#-関数型の呼び出し)
- [5. Kotlinのすごく面白いポイント](#5-kotlinのすごく面白いポイント)
- [6. 実はここが Kotlin の設計の美しいところ](#6-実はここが-kotlin-の設計の美しいところ)


# invoke が使われているところ

Kotlinでは

**`() 演算子 = invoke() を呼ぶ構文糖衣（syntax sugar）**

です。

つまり

```
obj()
```

はコンパイラによって

```
obj.invoke()
```

に変換されます。

---

# 1. 自分で定義する `invoke` 関数

```kotlin
class Printer {
    operator fun invoke(message: String) {
        println(message)
    }
}

val printer = Printer()

printer("hello")
```

これは実際には

```
printer.invoke("hello")
```

が呼ばれています。

---

# 2. 関数型でも同じ仕組み

Kotlinの関数型

```
(T) -> Unit
```

は、実体としては **invoke を持つオブジェクト** です。

例えば

```kotlin
val handler: (String) -> Unit = { text ->
    println(text)
}
```

これは裏ではだいたいこういうイメージです。

```kotlin
class Lambda : Function1<String, Unit> {
    override operator fun invoke(p1: String) {
        println(p1)
    }
}
```

つまり

```
handler("hello")
```

は

```
handler.invoke("hello")
```

です。

---

# 3. Kotlinの関数型の正体

Kotlinの関数型

```
(A) -> B
```

は実際には

```
Function1<A, B>
```

というインターフェースです。

簡略化するとこうです。

```kotlin
interface Function1<A, B> {
    operator fun invoke(a: A): B
}
```

つまり

**関数型 = invoke を持つオブジェクト**

です。

Kotlin の関数型は Function0〜Function22 で実装されており、引数 0 個の関数から 22 個の関数まで対応しています。

---

# 4. つまり整理すると

### ① 自分で定義する invoke

```kotlin
operator fun invoke()
```

### ② 関数型の呼び出し

```
handler()
```

これは

**同じ仕組み**です。

関数型は

**invoke を実装したオブジェクト**

だからです。

---

# 5. Kotlinのすごく面白いポイント

この仕組みを使うと、こんなこともできます。

```kotlin
class UseCase {
    operator fun invoke(id: String) {
        println("execute $id")
    }
}

val useCase = UseCase()

useCase("123")
```

Androidでもよく使われます。

```
loginUseCase()
fetchUserUseCase()
saveOrderUseCase()
```

みたいな書き方です。

---

# 6. 実はここが Kotlin の設計の美しいところ

Kotlinでは

```
関数
ラムダ
関数オブジェクト
invoke
```

が **全部同じモデルで統一されています。**

つまり

```
関数 = invoke を持つオブジェクト
```

です。



