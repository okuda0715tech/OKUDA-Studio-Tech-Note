- [this の意味](#this-の意味)
  - [項番 2 の例](#項番-2-の例)
  - [項番 3 の例](#項番-3-の例)
  - [this はデフォルトでは最も内側のオブジェクトを示す。変更はラベルで行う。](#this-はデフォルトでは最も内側のオブジェクトを示す変更はラベルで行う)
  - [ラベル付きの this の例](#ラベル付きの-this-の例)
  - [明示的に this が必要なケースに注意](#明示的に-this-が必要なケースに注意)


# this の意味


| 項番 | this の使用場所              | 意味                     |
| :----: | ---------------------------- | ------------------------ |
| 1    | クラス内                     | クラスのインスタンス     |
| 2    | 拡張関数内                   | レシーバーのインスタンス |
| 3 | レシーバー付き関数リテラル内 | レシーバーのインスタンス |


## 項番 2 の例

```kotlin
fun Int.doubled(): Int {
    return this * 2
}

val result = 5.doubled() // 10
```


## 項番 3 の例

```kotlin
// 例 1
val repeatFun: String.(Int) -> String = { times ->
    this.repeat(times)
}

// 例 2
// 以下のラムダ式はそもそもレシーバーを持っていないため、
// this がこのラムダ式のレシーバーを示すことはできない。
// この this は、これより外側の何かのオブジェクトを示している。
val funLit2 = { s: String ->
    val d1 = this
}
```


## this はデフォルトでは最も内側のオブジェクトを示す。変更はラベルで行う。

`this` に修飾子がない場合は、最も内側のオブジェクトを示します。  
それ以外のオブジェクトを参照したい場合は、ラベルを使用します。


## ラベル付きの this の例

【注意】  
以下のコード内のコメントで 「 A を示す」 のように記載している部分は、  
全て 「 A のインスタンスを示す」 と読み替えてください。  
コメントが長くなるのを防ぐために省略しています。

```kotlin
class A { // 暗黙的なラベル @A が生成される
    inner class B { // 暗黙的なラベル @B が生成される
        fun Int.foo() { // 暗黙的なラベル @foo が生成される
            val a = this@A // A を示す this
            val b = this@B // B を示す this

            val c = this // foo() 関数のレシーバ ( Int ) を示す
            val c1 = this@foo // foo() 関数のレシーバ ( Int ) を示す

            // 無名関数には暗黙的に生成されるラベルがありません。
            // myLambda@ は、無名関数に明示的に作成したラベルです。
            val funLit = myLambda@ fun String.() {
                val d = this // 無名拡張関数のレシーバ ( String ) を示す
                val d1 = this@myLambda // 無名拡張関数のレシーバ ( String ) を示す
            }

            val funLit2 = { s: String ->
                // foo() 関数のレシーバ ( Int ) を示す。
                // このラムダ式はレシーバを持っていないため。
                val d2 = this
            }
        }
    }
}
```


## 明示的に this が必要なケースに注意

パッケージレベル関数とメンバー関数に同じ名前の関数が存在している場合は、  
メンバー関数を呼び出したい場合には、明示的に this を記載する必要があります。  
ただし、パッケージレベル関数が定義されたパッケージを import していない場合は、  
this なしで呼び出した関数がメンバー関数になるため、 this の使用には十分に注意する必要があります。

```kotlin
fun printLine() { println("Top-level function") }

class A {
    fun printLine() { println("Member function") }

    fun invokePrintLine(omitThis: Boolean = false)  { 
        // パッケージレベル関数が呼ばれます
        if (omitThis) printLine()
        // メンバー関数が呼ばれます
        else this.printLine()
    }
}
```



