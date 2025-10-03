- [operator](#operator)
  - [概要](#概要)
  - [オーバーロード or オーバーライドできる演算子の一覧](#オーバーロード-or-オーバーライドできる演算子の一覧)
  - [例](#例)
  - [参考](#参考)


# operator

## 概要

`operator` 修飾子は、 `+` や `-` などの既存の演算子をオーバーロードやオーバーライドするための修飾子です。

## オーバーロード or オーバーライドできる演算子の一覧

以下は、オーバーロード or オーバーライドできる演算子の一覧です。

「関数名」 の列は、実際にオーバーロード or オーバーライドする場合は、以下の関数名でオーバーロード or オーバーライドしなければいけません。

| 演算子    | 関数名  |
| :-------- | :---------- |
| +         | plus        |
| -         | minus       |
| *         | times       |
| /         | div         |
| %         | rem         |
| +=        | plusAssign  |
| -=        | minusAssign |
| *=        | timesAssign |
| /=        | divAssign   |
| %=        | remAssign   |
| ++        | inc         |
| --        | dec         |
| +（単項） | unaryPlus   |
| -（単項） | unaryMinus  |
| ..        | rangeTo     |
| !         | not         |
| in        | contains    |
| []        | get / set   |
| ()        | invoke      |
| ==        | equals      |
| >、<      | compareTo   |

getValue と setValue については、 [デリゲートプロパティ](../型_変数_値/プロパティ・定数/デリゲートプロパティ.md) を参照してください。


## 例

```kotlin
//自作の複素数クラス（整数のみ対応）
data class Complex(val real: Int, val image: Int){
    //operatorキーワードを使って+演算子をオーバーロード
    operator fun plus(value: Complex): Complex{
        return Complex(real + value.real, image + value.image)
    }

    //-演算子（2項）
    operator fun minus(value: Complex): Complex{
        return Complex(real - value.real, image - value.image)
    }

    //-演算子（単項）
    operator fun unaryMinus(): Complex{
        return Complex(-real, -image)
    }

    //複素数的な表記を返すように文字列化
    override fun toString(): String{
        if(image > 0)
            return "$real+${image}i"
        else
            return "$real${image}i"
    }
}
```

```kotlin
//複素数クラスを使ってみる
var c1 = Complex( 2, 2)  //  2+2i
var c2 = Complex(-1, 5)  // -1+5i
var c3 = c1 + c2 //普通に+演算子が使える
 
println(c3)      //加算結果     「1+7i」
println(c1 - c2) //減算結果     「3-3i」
println(-c1)     //単項の-演算子「-2-2i」
```


## 参考

[Kotlinの拡張関数、範囲、分解宣言と多重戻り値、演算子オーバーロード](https://atmarkit.itmedia.co.jp/ait/articles/1805/14/news012_3.html)

[Operator overloading - Kotlin 公式ドキュメント](https://kotlinlang.org/docs/operator-overloading.html)

