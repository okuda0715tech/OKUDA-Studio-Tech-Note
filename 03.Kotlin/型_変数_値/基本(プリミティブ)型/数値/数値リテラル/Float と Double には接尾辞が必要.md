- [【参考】 Float リテラルにしたい場合のみ接尾辞が必要](#参考-float-リテラルにしたい場合のみ接尾辞が必要)


# Float と Double には接尾辞が必要

## Float 型に代入したい場合には接尾辞が必要

Float 型のプロパティに値を代入する場合には、接尾辞 `f` or `F` が必要になります。

```kotlin
// NG ( Integer リテラルを Float 型に代入できないとうエラー)
val f1: Float = 100
// OK
val f2: Float = 100f
```


## Double 型に代入したい場合には小数点が必要

Double 型には接尾辞が存在していませんが、実質的には小数点が接尾辞の役割をしています。

```kotlin
// NG ( Integer リテラルを Double 型に代入できないとうエラー)
val d1: Double = 100
// OK
val d2: Double = 100.0
```



