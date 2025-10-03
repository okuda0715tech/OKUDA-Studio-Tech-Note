- [Kotlin で独自のアノテーションを定義する](#kotlin-で独自のアノテーションを定義する)
  - [アノテーションでできること](#アノテーションでできること)
  - [アノテーションのインターフェースを定義](#アノテーションのインターフェースを定義)
  - [アノテーションの使用](#アノテーションの使用)
  - [アノテーションの処理を定義](#アノテーションの処理を定義)
  - [実行結果](#実行結果)


# Kotlin で独自のアノテーションを定義する

Kotlin でも、独自のアノテーションを定義できます。


## アノテーションでできること

[Java で独自のアノテーションを定義する.md](../../Java/アノテーション/Java%20で独自のアノテーションを定義する.md) を参照してください。


## アノテーションのインターフェースを定義

```kotlin
import kotlin.annotation.AnnotationRetention
import kotlin.annotation.AnnotationTarget

// アノテーションの適用対象をクラスと関数に指定
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
// アノテーションの保持期間をランタイムまでとする
@Retention(AnnotationRetention.RUNTIME)
annotation class MyAnnotation(val value: String = "default", val count: Int = 1)
```

- `@Target` : アノテーションを付与できる対象（クラス、関数、プロパティなど）を指定。
- `@Retention` : アノテーションの保持期間を指定（ソースのみ、バイトコードまで、実行時まで）。
- `annotation class` : Kotlin では annotation キーワードを使用してアノテーションを定義。


## アノテーションの使用

```kotlin
@MyAnnotation(value = "Test", count = 5)
class MyClass {

    @MyAnnotation("Method Annotation")
    fun myMethod() {
        println("Method executed")
    }
}
```


## アノテーションの処理を定義

```kotlin
fun main() {
    val clazz = MyClass::class

    // クラスのアノテーションを取得
    val classAnnotation = clazz.annotations.filterIsInstance<MyAnnotation>().firstOrNull()
    classAnnotation?.let {
        println("Class Annotation: value = ${it.value}, count = ${it.count}")
    }

    // メソッドのアノテーションを取得
    val method = clazz.members.find { it.name == "myMethod" }
    val methodAnnotation = method?.annotations?.filterIsInstance<MyAnnotation>()?.firstOrNull()
    methodAnnotation?.let {
        println("Method Annotation: value = ${it.value}")
    }
}
```

Kotlin ではリフレクションを使う際に KClass ( ::class ) を利用し、 annotations プロパティを通じてアノテーションを取得できます。

Java の getAnnotation() メソッドに相当するものは filterIsInstance<T>().firstOrNull() を使うことで実現できます。


## 実行結果

```
Class Annotation: value = Test, count = 5
Method Annotation: value = Method Annotation
```



