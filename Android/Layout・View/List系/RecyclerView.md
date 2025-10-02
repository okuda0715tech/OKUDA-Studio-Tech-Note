<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [RecyclerViewについて](#recyclerviewについて)
	- [横方向にスクロールさせる方法](#横方向にスクロールさせる方法)
	- [スクロールさせない方法](#スクロールさせない方法)
	- [縦スクロールのRecyclerViewで一行に複数のアイテムを並べる方法](#縦スクロールのrecyclerviewで一行に複数のアイテムを並べる方法)
	- [アイテムの削除](#アイテムの削除)
		- [参考](#参考)
			- [サンプルコード](#サンプルコード)

<!-- /TOC -->

# RecyclerViewについて

## 横方向にスクロールさせる方法

```Java
recyclerView.setHasFixedSize(true);
LinearLayoutManager layoutManager = new LinearLayoutManager(this);
layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
recyclerView.setLayoutManager(layoutManager);
recyclerView.setAdapter(adapter);
```

## スクロールさせない方法

```Java
recyclerView.setHasFixedSize(true);
LinearLayoutManager layoutManager = new LinearLayoutManager(this){
    @Override
    public boolean canScrollHorizontally() {
        return false;
    }
};
recyclerView.setLayoutManager(layoutManager);
recyclerView.setAdapter(adapter);
```

## 縦スクロールのRecyclerViewで一行に複数のアイテムを並べる方法

```Java
recyclerView.setHasFixedSize(true);
// 第二引数の「7」の部分が、一行に何個のアイテムを並べるかの指定
GridLayoutManager gridLayoutManagerVertical = new GridLayoutManager(this, 7, RecyclerView.VERTICAL,false);
gridLayoutManagerVertical.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
    @Override
    public int getSpanSize(int position) {
        // 横方法に何個のcolumnを結合するかの指定
        // （例）1なら結合なし。2なら2columnを結合して1columnのように扱う
        return 1;
    }
});
recyclerView.setLayoutManager(gridLayoutManagerVertical);
recyclerView.setAdapter(adapter);
```

## アイテムの削除

以下の3行を追加するだけで、削除後に削除されたアイテムより後ろのアイテムが一つ前に詰められるアニメーションも実施される。

**Sample.java**

```Java
public void removeAt(int position) {
    mDataset.remove(position);
    notifyItemRemoved(position);
    notifyItemRangeChanged(position, mDataSet.size());
}
```

`notifyItemRemoved`メソッドも`notifyItemRangeChanged`メソッドも`RecyclerView.Adapter`クラスのメソッドである。
`RecyclerView.Adapter`クラスは、オリジナルのアダプタクラスが継承しているはず。

### 参考

#### サンプルコード

[Recyclerviewの中身の分け方をカスタマイズ](https://qiita.com/hisakioomae/items/29e74eb4227bbb03645c)


## アイテムが空の場合に別のViewを表示する方法

```Xml
<android.support.v7.widget.RecyclerView
    android:id="@+id/recycler_view"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:scrollbars="vertical" />

<TextView
    android:id="@+id/empty_view"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:gravity="center"
    android:visibility="gone"
    android:text="@string/no_data_available" />
```

```Java
private RecyclerView recyclerView;
private TextView emptyView;

recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
emptyView = (TextView) rootView.findViewById(R.id.empty_view);

if (dataset.isEmpty()) {
    recyclerView.setVisibility(View.GONE);
    emptyView.setVisibility(View.VISIBLE);
}
else {
    recyclerView.setVisibility(View.VISIBLE);
    emptyView.setVisibility(View.GONE);
}
```

## 簡単にはできないこと

### viewの各アイテムにactivated状態をセットしておき、回転時など、OS起因のFragmentの破棄・復元が発生した時、activated状態を自動で復元する。

- 各アイテムのviewはviewなので、自動で復元されそうだが、復元されなかった。
- Viewは通常、自動で回復されるため、Viewの状態を管理するとコードが紛らわしくなりそうだったので、自分で管理するのはやめることにした。


