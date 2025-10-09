<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [attr タグの format に指定できる型](#attr-タグの-format-に指定できる型)
	- [補足](#補足)

<!-- /TOC -->


# attr タグの format に指定できる型

format には、 View の属性に指定する値の型を指定します。

| 属性      | 内容                   | 設定する値の例                                                        |
| --------- | ---------------------- | --------------------------------------------------------------------- |
| integer   | int 値です            | 10                                                                    |
| float     | float 値です          | 0.5                                                                   |
| string    | 文字列です           | 文字列ですが@string/○○○も使用可です。                                 |
| boolean   | boolean 型です        | true / false                                                          |
| enum      | 列挙型です           | {normal = 1, round = 2, circle = 3}                                   |
| flag      | ビットフラグです     | {normal = 1, round = 2, circle = 4}                                   |
| dimension | dimension 単位です    | 10dp,10pxとか、width=とかで設定しているサイズの値と同じです。         |
| color     | Colorクラスです      | #AA0000とかの他にresに定義したcolorリソースも@color/○○○で利用できます |
| reference | リソース ID の参照です | @layout/○○○○ とか、 @drawable/○○○ とか                                      |
| freaction | 有理数です           | 10% とか百分率を指定してつかいます。                                   |


## 補足

format が reference の場合、`@color/...`や`@layout/...`のように、他のリソースを参照できることを示します。  
ただし、 `@color/` や `@string/` は、そもそも `format="color"` や `format="string"` が用意されているため  
そちらを使用するとよいでしょう。

