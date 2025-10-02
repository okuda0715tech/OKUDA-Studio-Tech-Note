- [オブジェクト式(Object expressions)](#オブジェクト式object-expressions)
  - [概要](#概要)
  - [1 から匿名クラスを作成する場合](#1-から匿名クラスを作成する場合)
  - [既存のクラス・インターフェースを継承・実装する](#既存のクラスインターフェースを継承実装する)
  - [複数のクラスやインターフェースを持ちたい場合](#複数のクラスやインターフェースを持ちたい場合)
  - [包含クラスから匿名オブジェクトのプロパティへのアクセス可否](#包含クラスから匿名オブジェクトのプロパティへのアクセス可否)
  - [匿名オブジェクトから包含クラスのプロパティへのアクセス可否](#匿名オブジェクトから包含クラスのプロパティへのアクセス可否)


# オブジェクト式(Object expressions)

## 概要

オブジェクト式は、匿名クラスのオブジェクトを作成します。  
このようなクラスは、 1 回限りの使用に役立ちます。

匿名クラスを作成する際の継承元には以下の 3 つのパターンがあります。

- 既存のクラスを継承する。
- インターフェイスを実装する。
- 何も継承や実装せず、 1 から作成する。

匿名クラスのインスタンスは、名前ではなく式によって定義されるため、 **匿名オブジェクト** とも呼ばれます。


## 1 から匿名クラスを作成する場合


```Kotlin
val helloWorld = object {
    val hello = "Hello"
    val world = "World"
    // object は Any クラスを継承しています。
    // そのため override が必要になります。
    override fun toString() = "$hello $world"
}

print(helloWorld)
```


## 既存のクラス・インターフェースを継承・実装する

`object : 継承したいクラス or インターフェース` の文法で、継承や実装ができます。

```Kotlin
window.addMouseListener(object : MouseAdapter() {
    override fun mouseClicked(e: MouseEvent) { /*...*/ }

    override fun mouseEntered(e: MouseEvent) { /*...*/ }
})
```


## 複数のクラスやインターフェースを持ちたい場合

複数のクラスやインターフェースを持ちたい場合は、コンマ `,` で区切ります。

当然ですが、クラスは一つしか継承することはできません。

```Kotlin
open class A(x: Int) {
    public open val y: Int = x
}

interface B { /*...*/ }

// 型は明示的に宣言された型になる。
// この例の場合は A 型の変数なので A となる。
val ab: A = object : A(1), B {
    override val y = 15
}
```


## 包含クラスから匿名オブジェクトのプロパティへのアクセス可否

以下の 1 , 2 のいずれかの場合は、包含クラスから匿名オブジェクトのプロパティにアクセスすることが可能です。

**1. private 修飾子かつ inline ではない場合**

```Kotlin
class C {
    // private かつ inline ではない関数
    private fun getObject() = object {
        val x: String = "x"
    }

    // private かつ inline ではないプロパティ
    private val o = object {
        val x: String = "x"
    }

    fun printX() {
        // x へのアクセスが可能です。
        getObject().x
        // x へのアクセスが可能です。
        o.x
    }
}
```

関数やプロパティが `private` 以外の場合、または、 `inline` になっている場合は、プロパティへのアクセスができません。


**2. ローカル変数に代入された場合**

```Kotlin
class C {
    fun myMethod() {
        // object インスタンスがローカル変数に代入された場合
        val o = object {
            val x: String = "x"
        }

        // x へのアクセスが可能です。
        o.x
    }
}
```


## 匿名オブジェクトから包含クラスのプロパティへのアクセス可否

匿名オブジェクトから包含クラスのプロパティへは、常にアクセスが可能です。

```Kotlin
fun countClicks(window: JComponent) {
    var clickCount = 0
    var enterCount = 0

    window.addMouseListener(object : MouseAdapter() {
        override fun mouseClicked(e: MouseEvent) {
            // アクセス可能
            clickCount++
        }

        override fun mouseEntered(e: MouseEvent) {
            // アクセス可能
            enterCount++
        }
    })
    // ...
}
```





