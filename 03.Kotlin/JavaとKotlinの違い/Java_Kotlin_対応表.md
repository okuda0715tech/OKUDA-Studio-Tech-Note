- [Java\_Kotlin\_対応表](#java_kotlin_対応表)
  - [一覧](#一覧)
  - [サンプルコード](#サンプルコード)
  - [No.1 クラスに対して一つのメンバーを定義したい](#no1-クラスに対して一つのメンバーを定義したい)
  - [No.2 あるクラスを継承したいが、わざわざサブクラスを作るほどではなく、一時的なクラスをつくりたい場合](#no2-あるクラスを継承したいがわざわざサブクラスを作るほどではなく一時的なクラスをつくりたい場合)


# Java_Kotlin_対応表

## 一覧

| No  | 機能                                                                                           | Java           | Kotlin           |
| :-- | :--------------------------------------------------------------------------------------------- | :------------- | :--------------- |
| 1   | クラスに対して一つのメンバーを定義したい                                                       | static         | companion object |
| 2   | あるクラスを継承したいが、わざわざサブクラスを作るほどではなく、一時的なクラスをつくりたい場合 | 匿名内部クラス | object           |


## サンプルコード

## No.1 クラスに対して一つのメンバーを定義したい


## No.2 あるクラスを継承したいが、わざわざサブクラスを作るほどではなく、一時的なクラスをつくりたい場合

**Java**

```Java
window.addMouseListener(new MouseAdapter() {
  @Override
  override fun mouseClicked(MouseEvent e) {
    // ...
  }

  @Override
  mouseEntered(MouseEvent e) {
    // ...
  }
})
```


**Kotlin**

```Kotlin
window.addMouseListener(object : MouseAdapter() {
  override fun mouseClicked(e: MouseEvent) {
    // ...
  }

  override fun mouseEntered(e: MouseEvent) {
    // ...
  }
})
```

