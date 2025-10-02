<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [ScrollView](#scrollview)
  - [基本ルール](#基本ルール)
  - [スクロールバーを常に表示する](#スクロールバーを常に表示する)
  - [横スクロールの ScrollView](#横スクロールの-scrollview)
<!-- TOC END -->


# ScrollView

## 基本ルール

`ScrollView` を使用する際は、スクロールする方向に対して、  
`ScrollView` 自身のサイズの最大値を決めておかなければいけない。


## スクロールバーを常に表示する

以下の設定を記述することで、スクロールバーを常に表示させることができます。

```xml
android:fadeScrollbars="false"
```


## 横スクロールの ScrollView

横スクロールする ScrollView を実装したい場合は、 `<ScrollView>` の代わりに  
`<HorizontalScrollView>` を使用します。
