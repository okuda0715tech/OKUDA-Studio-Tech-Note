<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [APILevel別リソース切り替え](#apilevel別リソース切り替え)
  - [基本は公式サイトを参照](#基本は公式サイトを参照)
  - [API Level ごとにリソースを変更する](#api-level-ごとにリソースを変更する)
  - [strings.xmlファイルなどは、オーバーライド方式](#stringsxmlファイルなどはオーバーライド方式)
<!-- TOC END -->


# APILevel別リソース切り替え

## 基本は公式サイトを参照

公式サイトを参照すればわかると思われる。

公式サイト
　[リソースの提供](https://developer.android.com/guide/topics/resources/providing-resources?hl=ja)

以下の公式サイトをまとめたページも簡単にまとまっているので、これを元にわからないところを調べてまとめ直したい
　[Android アプリケーション開発 - リソース (Resource)](https://so-zou.jp/mobile-app/tech/android/resource/)


## API Level ごとにリソースを変更する

`フォルダ名-v99` の命名規則でフォルダを作成する。  
`99` の部分には、 API Level を指定する。

（例）  
`layout` , `layout-v26` , `layout-v28` の三種類のリソースフォルダを用意したとする。  
`API Level 25` 以下のデバイスには、 `layout` のリソースが適用される。  
`API Level 26, 27` のデバイスには、 `layout-v26` のリソースが適用される。  
`API Level 28` 以上のデバイスには、 `layout-v28` のリソースが適用される。


## strings.xmlファイルなどは、オーバーライド方式

`strings.xml` ファイルなどは、 `values/strings.xml` が基底となり、 個別指定ファイルでオーバーライドする形式で利用できる。

例えば、 `values/strings.xml` 内で、 `<string name="abc">abc</string>` と定義したとする。  
`values-v26/strings.xml` などの個別指定ファイル内で、同じ `name` 属性のstringを定義した場合は、  
API Level 26 のデバイス上では、 `values-v26` 内の値が使用される。  
`values-v26/strings.xml` などの個別指定ファイル内で、 `name` 属性のstringを定義しなかった場合は、  
`values-v26/strings.xml` 内から、 `values/strings.xml` 内の `name` 属性を参照することができる。
