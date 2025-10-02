- [enum](#enum)
  - [基本形](#基本形)
  - [プロパティを保持する方法](#プロパティを保持する方法)
  - [ループ処理](#ループ処理)
    - [for 文を使用した例](#for-文を使用した例)
    - [forEach 文を使用した例](#foreach-文を使用した例)
  - [メソッドの定義方法](#メソッドの定義方法)
  - [Enum の定義名を String 型で取得する](#enum-の定義名を-string-型で取得する)


# enum

## 基本形

```Kotlin
enum class Fruits {
    APPLE, BANANA, GRAPE
}

fun main() {
    val fruit = Fruits.APPLE
    println(fruit)  //=> APPLE
}
```


## プロパティを保持する方法

```Kotlin
enum class Fruits(val label: String, val color: String) {
    APPLE("リンゴ", "赤色"),
    BANANA("バナナ", "黄色"),
    ORANGE("ぶどう", "紫色");  // Kotlin で唯一セミコロンが必要な場所 (任意ではなく、必須です。)

    override fun toString(): String {
        return "$label は $color です"
    }
}

fun main() {
    val f = Fruits.APPLE
    println(f.label)  //=> "リンゴ"
    println(f.color)  //=> "赤色"
    println(f)  //=> "リンゴ は 赤色 です"
}
```


## ループ処理

### for 文を使用した例

```Kotlin
enum class Fruits {
    APPLE, BANANA, GRAPE
}

fun main() {
    // 新しい書き方
    for (x: Fruits in entries) {
        println(x)
    }

    // 古い書き方
    for (x: Fruits in Fruits.values()) {
        println(x)
    }
}
```


### forEach 文を使用した例

```Kotlin
Fruits.values().forEach { println(it) }
Fruits.values().forEach(::println)
```


## メソッドの定義方法

```Kotlin
enum class MyEnum(val id: Int) {

    UNKNOWN(0), SUCCESS(1);

    // Enum の要素のインスタンス関数はメンバーとして定義する。
    fun writeLog(){
        Log.d("test", "id は $id です。")
    }

    // Enum のクラス関数は companion object の中に定義する。
    companion object {
        fun getObjectBy(id: Int): MyEnum {
            for (o: MyEnum in entries) {
                if (o.id == id) return o
            }
            throw IllegalArgumentException(
                "Illegal argument 'id = $id' was passed to the method 'getObjectBy(id: Int)'."
            )
        }
    }
}
```

```kotlin
class Main {

    fun main() {
        // インスタンス経由でのみアクセス可能
        MyEnum.UNKNOWN.writeLog()

        // Java の static メソッドのようにアクセス可能
        MyEnum.getObjectBy(1)

        // Java から Kotlin の companion object にアクセスする場合は、
        // companion object のルールに従います。
    }
}
```


## Enum の定義名を String 型で取得する

Enum の定義名を String 型で取得するには、以下のように name プロパティを使用します。

```kotlin
// "FIRST" という文字列が返ってきます。
val enumString = Route.FIRST.name

enum class Route {
    FIRST,
    SECOND,
    THIRD
}
```


