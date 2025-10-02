<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [アイテム内の特定のViewをデフォルトで非表示にする](#内特定view非表示)

<!-- /TOC -->


# アイテム内の特定のViewをデフォルトで非表示にする

Databindingを使用している時に、リストアイテム内の特定のViewをデフォルトで非表示にしたつもりが、  
アイテム生成直後に一瞬だけ表示されてしまうことがある。その場合は、以下のようにして  
デフォルト値を設定することで解決することができる。

```xml
<View
  android:visibility="@{viewmodel.expandable ? View.VISIBLE : View.GONE, default=gone}"
/>
```
