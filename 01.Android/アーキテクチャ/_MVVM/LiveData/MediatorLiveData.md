<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [MediatorLiveData](#mediatorlivedata)
  - [概要](#概要)
  - [使いどころ](#使いどころ)
    - [ネットワークから取得したデータと DB から取得したデータを一つの LiveData にまとめて管理したい場合](#ネットワークから取得したデータと-db-から取得したデータを一つの-livedata-にまとめて管理したい場合)
    - [LiveData が更新された時に、何らかの処理を行って、 MutableLiveData にセットしたい場合](#livedata-が更新された時に何らかの処理を行って-mutablelivedata-にセットしたい場合)
  - [実装例](#実装例)
  - [初期値の設定方法](#初期値の設定方法)
<!-- TOC END -->


# MediatorLiveData

## 概要

`MediatorLiveData` は、 `MutableLiveData` を継承したクラスです。

`MediatorLiveData` は複数の `LiveData` を統合するために使用します。

例えば、複数の `LiveData` の中から最後に更新されたデータを保持するために使用します。

また、 `MutableLiveData` に紐づく `LiveData` が更新された時に、何らかの処理を行って、  
その結果を `MutableLiveData` にセットすることも可能です。そのような場合には、  
`androidx.lifecycle.Transformations` クラスの `map()` メソッドや `switchMap()` メソッドを  
使用すると、より簡潔に実装することが可能となります。


## 使いどころ

### ネットワークから取得したデータと DB から取得したデータを一つの LiveData にまとめて管理したい場合

例えば、ネットワークからデータを取得して、 LiveData1 に保持するとします。  
ネットワークから一度取得したデータはデータベースに保存するとします。  
また、データベースからデータを取得して LiveData2 に保持するとします。  
これらのデータは最新データかどうかの違いであり、どちらも同じデータであることが普通です。  
よって、 LiveData1 も LiveData2 も同じ LiveData3 で管理することができれば、  
View は LiveData3 だけを監視していれば良く、実装が簡潔になります。  
その LiveData3 の役割を担うのが MediatorLiveData です。


### LiveData が更新された時に、何らかの処理を行って、 MutableLiveData にセットしたい場合

別紙 `Transformations` に関する説明を参照してください。


## 実装例

```java
LiveData liveData1 = ...;
LiveData liveData2 = ...;

MediatorLiveData liveDataMerger = new MediatorLiveData<>();
// addSource() メソッドの第二引数は Observer オブジェクトが入ります。
liveDataMerger.addSource(liveData1, value -> liveDataMerger.setValue(value)); // (※ 1)
liveDataMerger.addSource(liveData2, value -> liveDataMerger.setValue(value));
```

参考までに、上記の (※ 1) 部分のラムダ式を展開すると以下のようになります。

```java
liveDataMerger.addSource(liveData1, new Observer() {
	@Override public void onChanged(@Nullable Integer value) {
		liveDataMerger.setValue(value);
	}
});
```


## 初期値の設定方法

`MediatorLiveData` は、 `MutableLiveData` を継承したクラスですが、 `MediatorLiveData`  
のコンストラクタに初期値を渡すことはできません。  
初期化する際には、 `setValue()` メソッドを使用して値を設定します。

```java
MediatorLiveData<Boolean> mediatorLiveData;

// NG 例
mediatorLiveData = new MediatorLiveData<>(false);

// OK 例
mediatorLiveData = new MediatorLiveData<>();
mediatorLiveData.setValue(false);
```
