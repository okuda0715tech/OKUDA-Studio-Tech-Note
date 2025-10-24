<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [onBindViewHolder() メソッド内でポジションを取得する方法](#onbindviewholder-メソッド内でポジションを取得する方法)
<!-- TOC END -->


# onBindViewHolder() メソッド内でポジションを取得する方法

`onBindViewHolder()` は、引数に `int position` が渡されるため、通常は、

```java
mRows.get(position);
```

のようにして、リストアイテムの該当のアイテムを取得することができます。  
ただし、以下のように匿名クラス内のメソッドでアイテムのポジションを取得したい場合は、  
`ViewHolder` 経由で取得するようにしてください。

匿名クラス内で `onBindViewHolder()` の引数の `position` を取得した時か何かのときに、  
`ViewHolder` 経由で取得するようにワーニングが表示されるため、  
匿名クラスのメソッド内では、常に `ViewHolder` 経由でポジションを取得するのが無難だと思います。


```java
@Override
public void onBindViewHolder(@NonNull ItemListViewHolder viewHolder, int position) {
    WinLoseTable winLoseTable = mRows.get(position);

    viewHolder.binding.menuButton.setOnClickListener(
            new View.OnClickListener() {
                // 匿名クラス内部でリストアイテムのポジションを取得したい場合は、 viewHolder 経由で取得する。
                @Override
                public void onClick(View view) {
                    viewHolder.getAdapterPosition();
                }
            }
    );
}
```
