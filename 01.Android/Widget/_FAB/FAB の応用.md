- [FAB の応用](#fab-の応用)
  - [FABをタップしたら縦に複数のFabが出てきてメニューのように使用する方法](#fabをタップしたら縦に複数のfabが出てきてメニューのように使用する方法)


# FAB の応用

## FABをタップしたら縦に複数のFabが出てきてメニューのように使用する方法

以下のサイトを参考にすれば実装できます。

[How to Create Expandable Floating Action Button(FAB) Menu](https://mobikul.com/expandable-floating-action-button-fab-menu/)

ポイントは以下です。

- `layout.xml`には最初から複数のFabを定義しておく
- その際、全てのFabの`VISIBILITY`はデフォルト(VISIBLE)でOK
- `layout.xml`の一番下に定義したFABが画面上の一番上のレイヤに表示され、その他のFABは、その下のレイヤに隠れて見えない状態になる。
- 一番上のレイヤのFABをタップ下時に、他のFABがアニメーションで見える位置に移動するように実装する。




