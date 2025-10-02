<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [notify系メソッド](#notify系)
	- [分類](#分類)
	- [notifyDataSetChanged()](#notifydatasetchanged)
	- [notifyItemChanged(int position, Object payload)](#notifyitemchangedint-position-object-payload)
	- [notifyItemInserted(int position)](#notifyiteminsertedint-position)
	- [notifyItemMoved (int fromPosition, int toPosition)](#notifyitemmoved-int-fromposition-int-toposition)
	- [notifyItemRangeChanged (int positionStart, int itemCount, Object payload)](#notifyitemrangechanged-int-positionstart-int-itemcount-object-payload)
	- [notifyItemRangeInserted (int positionStart, int itemCount)](#notifyitemrangeinserted-int-positionstart-int-itemcount)
	- [notifyItemRangeRemoved (int positionStart, int itemCount)](#notifyitemrangeremoved-int-positionstart-int-itemcount)
	- [notifyItemRemoved (int position)](#notifyitemremoved-int-position)

<!-- /TOC -->


# notify系メソッド

## 分類

構造的な変更とアイテムの変更のニ種類に分類できます。

構造的な変更とは、リストに新しいアイテムが追加されたか、既存のアイテムが削除されたか、  
または、リスト内でアイテムの順番が入れ替わったことを意味します。

アイテムの変更とは、アイテム内のデータが更新されることを意味します。


## ペイロードとは

ペイロードの意味は、ヘッダなどを除いたデータ本体の部分を指します。  
ペイロードが渡された `notify` 系メソッドは、そのペイロードのアイテムのみのUI更新を行い、  
その他のアイテムのUIは更新しません。

例えば、アイテムのデータを更新した場合、ペイロードなしだと、更新したアイテムより下に位置する  
アイテムが無駄にアニメーションされます。  
ペイロードを与えた場合は、その無駄なアニメーションが抑制されます。


## notifyDataSetChanged()

このメソッドは、構造的な変更が発生した可能性があるとみなし、`LayoutManager`は、  
完全な再バインドと再レイアウトを行います。

開発者はより適切なnotify系のメソッドを使用することが推奨され、`nofityDataSetChanged()`  
メソッドの使用は最終的な手段にするべきです。


## notifyItemChanged(int position, Object payload)

このメソッドは、アイテムの変更イベントとして使用されます。

アイテムのIDは変更されません。

`payload`には、変更後のデータを渡します。

渡された`payload`はマージされ、アイテムがすでにViewHolderによって表示されており、  
同じViewHolderにリバウンドされる場合は、アダプターの  
`onBindViewHolder(ViewHolder holder, int position, List<Object> payloads)`  
に渡される可能性があります。

nullペイロードを渡された`notifyItemRangeChanged(int positionStart, int itemCount, Object payload)`は、  
そのアイテムの既存のペイロードをすべてクリアし、  
`onBindViewHolder(ViewHolder holder, int position, List<Object> payloads)`が  
呼び出されるまで将来のペイロードを防ぎます。  
アダプタは、ペイロードが常に`onBindViewHolder()`に渡されると想定してはなりません。  
ビューがアタッチされていない場合、ペイロードは単にドロップされます。


## notifyItemInserted(int position)

登録されたオブザーバーに、`position`に指定されたアイテムが新しく挿入されたことを通知します。  
以前の`position`にあったアイテムは、`position + 1` になります。

これは構造変更イベントです。  
データセット内の他の既存のアイテムの表示は、まだ最新であると見なされ、位置が変更される  
可能性はありますが、リバウンドはされません。


## notifyItemMoved (int fromPosition, int toPosition)

`fromPosition`に指定されたアイテムが`toPosition`に移動されたことを登録済みのオブザーバーに通知します。

これは構造変更イベントです。  
データセット内の他の既存のアイテムの表示は、まだ最新であると見なされ、位置が変更される  
可能性はありますが、リバウンドされません。


## notifyItemRangeChanged (int positionStart, int itemCount, Object payload)

登録されたオブザーバーに、位置`positionStart`から始まり、`itemCount`個のアイテムが変更されたことを通知します。  
オプションのペイロードは、変更された各アイテムに渡すことができます。

これはアイテム変更イベントであり、構造変更イベントではありません。  
これは、指定された位置範囲のデータの反映が古くなっているため、更新する必要があることを示しています。  
指定された範囲のアイテムは同じIDを保持します。

nullペイロードを渡された`notifyItemRangeChanged(int positionStart, int itemCount, Object payload)`は、  
そのアイテムの既存のペイロードをすべてクリアし、  
`onBindViewHolder(ViewHolder holder, int position, List<Object> payloads)`が  
呼び出されるまで将来のペイロードを防ぎます。  
アダプタは、ペイロードが常に`onBindViewHolder()`に渡されると想定してはなりません。  
ビューがアタッチされていない場合、ペイロードは単にドロップされます。


## notifyItemRangeInserted (int positionStart, int itemCount)

`notifyItemInserted(int position)`の場合は、一件のみのInsertを想定していますが、  
このメソッドは、`itemCount`個のInsertを想定しています。

それ以外の点では、`notifyItemInserted(int position)`と同じです。


## notifyItemRangeRemoved (int positionStart, int itemCount)

以前に`positionStart`に配置されていた`itemCount`個のアイテムがデータセットから削除されたことを、  
登録されたオブザーバーに通知します。
以前に`positionStart + itemCount`以降に配置されていたアイテムは、  
`oldPosition - itemCount`に配置されるようになりました。

これは構造変更イベントです。  
データセット内の他の既存のアイテムの表現は、まだ最新であると見なされ、位置が変更される  
可能性はありますが、リバウンドされません。


## notifyItemRemoved (int position)

以前に`position`の位置にあったアイテムがデータセットから削除されたことを、登録されたオブザーバーに通知します。  
以前は`position`以降に配置されていたアイテムが、`oldPosition - 1`に表示されるようになりました。

これは構造変更イベントです。  
データセット内の他の既存のアイテムの表現は、まだ最新であると見なされ、位置が変更される  
可能性はありますが、リバウンドされません。
