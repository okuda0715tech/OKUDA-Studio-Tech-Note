- [Nothing](#nothing)
  - [概要](#概要)
  - [Nothing の使いどころ](#nothing-の使いどころ)
  - [Nothing が使用されている例](#nothing-が使用されている例)


# Nothing

## 概要

Nothing 型は関数の戻り値の型として、ごくまれに使用されます。

Nothing 型には、 **「何も返さない」** という意味があります。

メソッドの戻り値の型に Nothing 型を指定した場合は、そのメソッドは、途中で例外が発生し、戻り値を戻すことがないということを意味します。


## Nothing の使いどころ

Nothig 型を返すメソッドを呼び出す側のメソッドは、  
「このメソッドは途中で例外で抜ける」 ということを認識できるようになります。  
これにより、以下のような実装が可能になります。

```Kotlin
    fun methodA(): String {
        methodB()
    }

    fun methodB(): Nothing {
        throw Exception()
    }
```

`methodA()` の中では、一切 `String` 型の戻り値を返していません。  
普通ならば、これはコンパイルエラーになりますが、  
`methodB()` が `Nothing` 型を戻り値に指定しているために、  
コンパイル、及び、実行が可能なコードになっています。


## Nothing が使用されている例

Nothing 型は `TODO()` 関数で使用されています。

```Kotlin
fun doSomething(): String {
    TODO("まだ考え中")
}
// An operation is not implemented: まだ考え中
```

`TODO()` 関数を使用することで、以下のメリットがあります。

- チームメンバーがコードを見たときに未実装であることがわかる。
- もし、未実装部分の処理が実行された場合は、アプリをクラッシュさせ、メッセージで未実装であることを伝えることができる。

