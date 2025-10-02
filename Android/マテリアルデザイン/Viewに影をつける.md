<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [Viewに影をつける](#viewに影をつける)
	- [API level 21 以降](#api-level-21-以降)
		- [レイアウトxmlで影をつける方法](#レイアウトxmlで影をつける方法)

<!-- /TOC -->


# Viewに影をつける

## レイアウトxmlで影をつける方法（API level 21 以降）

```xml
<TextView
    android:background="@color/colorPaleBlue"
    android:elevation="@dimen/elevation_4dp"
```

`elevation`属性では、Viewに高さを与えます。

システムが高さに応じた影を自動的に表示してくれます。

影の形は、`background`属性に定義されたdrawableの形と同じになります。

`background`属性が指定されていない場合は、いくら`elevation`属性を記述しても影がつかないため、注意が必要です。

 Viewの高さは`dp`単位で指定します。

 高さが高いほど影は広くなり、ぼんやりと広がります。

### ScrollViewなど（ViewGroup全般？）に影をつける場合は、子Viewの影の付け方に注意

ScrollViewなど（ViewGroup全般？）に影をつける場合、その子Viewの影は、親ViewGroupの高さを基準として、そこからの高さ指定になる。

例えば、親ViewGroupに3dpの影をつけ、子Viewにも3dpの影をつけた場合、子Viewは親ViewGroupよりも3dp分高い位置にあるように影が付与される。


## setElevation(true)をしたのに影ができない時

- 影をつけたいViewの親ViewGroupに`android:clipToPadding="false"`を設定すると影が表示されることがあります。
- 影はbackgroundの形で描画されるため、`setBackground`や`setBackgroundColor`でbackgroundを設定すると影が表示されることがあります。



