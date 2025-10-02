- ['this@ActivityName' is not captured error Android Kotlin](#thisactivityname-is-not-captured-error-android-kotlin)
  - [原因](#原因)
  - [対応方法](#対応方法)


# 'this@ActivityName' is not captured error Android Kotlin

## 原因

Kotlin ではラムダ式の内部でデバッグを行うことができない。


## 対応方法

対応方法は 2 つあります。

1. ラムダ式の内部の処理を別のメソッドに切り出す。

または、

2. ラムダ式を使用しない記述方法で書き換える。  

例えば以下のように書き換えます。

```Kotlin
setOnClickListener(object: View.OnClickListener {..})
```
