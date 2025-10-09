- [Adapter のコールバックが呼ばれない場合](#adapter-のコールバックが呼ばれない場合)
  - [【原因１】 LayoutManager が設定されていない](#原因１-layoutmanager-が設定されていない)
  - [【原因２】 getItemCount() メソッドが 0 を返している](#原因２-getitemcount-メソッドが-0-を返している)


# Adapter のコールバックが呼ばれない場合

## 【原因１】 LayoutManager が設定されていない

RecyclerView に LayoutManager が設定されていない場合は、  
Adapter のコールバックが呼ばれません。

xml か Java コードで LayoutManager を設定する必要があります。

**xml で設定する場合**

```xml
<androidx.recyclerview.widget.RecyclerView
    app:layoutManager="androidx.recyclerview.widget.LinearLayoutManager" />
```

**Java で設定する場合**

```java
recycler.setLayoutManager(new LinearLayoutManager(context));
```


## 【原因２】 getItemCount() メソッドが 0 を返している

Adapter の getItemCount() メソッドが 0 を返している場合も、  
Adapter のコールバックが呼ばれません。

データ取得前の疑似データで動作確認を行っている場合などに、  
よく起こりうる状況です。

テストデータを挿入するのを Adapter クラスのコールバックで  
実装するのではなく、 Adapter にデータの追加メソッドを実装して、  
Adapter の外からそのメソッドを呼び出し、要素を追加しましょう。


