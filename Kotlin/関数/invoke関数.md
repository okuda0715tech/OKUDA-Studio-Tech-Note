- [invoke 関数](#invoke-関数)
  - [概要](#概要)
  - [基本形](#基本形)
  - [使いどころ](#使いどころ)
    - [インスタンス名と関数の引数が関連する場合](#インスタンス名と関数の引数が関連する場合)
    - [同じ処理を繰り返す処理が複数個所に存在する場合](#同じ処理を繰り返す処理が複数個所に存在する場合)
  - [ラムダ式の仕組みを作る上でも使われている](#ラムダ式の仕組みを作る上でも使われている)


# invoke 関数

## 概要

メソッド名を `invoke` にした関数は、カッコ `()` だけで呼び出せるようになります。  
( `invoke()` でも呼び出せます)

`invoke` 機能を使用するためには、関数に `operator` 修飾子を付与する必要があります。  
付与しないと、ただの `invoke()` という名前の関数とみなされます。  
`operator fun invoke()` という風に `fun` の前に記述します。


## 基本形

```kotlin
class Greeter {
    operator fun invoke(name: String) {
        println("Hello $name")
    }
}

val greeter = Greeter()
greeter("World")  // `Hello World`と表示されます。
```


## 使いどころ

### インスタンス名と関数の引数が関連する場合

カッコ `()` だけで関数が呼び出せるということは、関数を呼び出すときに、インスタンス名の直後にカッコ `()` が来るということです。つまり、 `インスタンスの変数名()` という形式で関数が呼び出されることになります。

これは、あたかもインスタンス名がメソッド名になったかのように見えるため、  
インスタンス名と引数の値が関連付くような関数を実装すると  
コードの可読性が上がると考えられます。

```kotlin
class Greeter(private val greeting: String) {
    operator fun invoke(name: String) {
        println("$greeting $name")
    }
}

val greeterEn = Greeter("Hello")
greeterEn("Bob")  // Hello Bob
val greeterJa = Greeter("こんにちは")
greeterJa("太郎さん")  // こんにちは 太郎さん
```


### 同じ処理を繰り返す処理が複数個所に存在する場合

レシーバーの型と関数の戻り値の型が同じ場合は、カッコを連続で任意の数記述することで、  
任意の回数の繰り返し処理が簡単に表現できます。

```kotlin
class Doubled(var num: Int) {

    // 自分自身を返すことで、カッコの連続で
    // 繰り返し呼び出しを可能にしている
    operator fun invoke(): Doubled {
        num *= 2
        return this
    }
    
}

fun main() {
    val tripled = Doubled(1)()()
    Log.d("test", "result = ${tripled.num}") // result = 4
}
```


## ラムダ式の仕組みを作る上でも使われている

ラムダ式の仕組みを作る上でも invoke() 関数は使われています。

```kotlin
val test = { value: String ->
    println(value)
}
test("test")
test.invoke("test") // invokeを使うこともできる。
```





