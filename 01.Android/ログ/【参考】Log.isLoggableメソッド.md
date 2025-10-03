- [【参考】Log.isLoggableメソッド](#参考logisloggableメソッド)
  - [概要](#概要)
  - [実装方法](#実装方法)
  - [isLoggable の結果を変更するには adb コマンドを使用する](#isloggable-の結果を変更するには-adb-コマンドを使用する)
  - [再起動後も true を返すコマンドもある](#再起動後も-true-を返すコマンドもある)


# 【参考】Log.isLoggableメソッド

## 概要

Log.isLoggable() メソッドは、特定のデバイスでログ出力を  
絞り込みたい場合に使用するものです。

あまり利用するメリットはわかりません。


## 実装方法

`isLoggable()` メソッドのタグとログレベルが、  
if 文内部のログのタグとログレベルに一致している必要があります。

```java
if (Log.isLoggable("FOO_TAG", Log.VERBOSE)) {
    Log.v("FOO_TAG", "Message for logging.");
}
```


## isLoggable の結果を変更するには adb コマンドを使用する

以下の adb コマンドを使用することで、  
指定したタグと指定したログレベルの `isLoggable()` メソッドが  
`true` を返すようになります。

```
adb shell setprop log.tag.FOO_TAG VERBOSE
```

`false` を返すようにする adb コマンドはわかりません。  
デバイスを再起動すると `false` を返すようになります。



## 再起動後も true を返すコマンドもある

【注意】  
再起動後も true を返すコマンドもありますが、  
今のところ、永続的な設定を行った場合の解除方法がわからないので、  
基本的には永続的な設定を行わないようにしてください。

危険そうなので、設定方法もここには記載しません。



