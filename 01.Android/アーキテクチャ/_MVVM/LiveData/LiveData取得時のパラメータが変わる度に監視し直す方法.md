<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [LiveData取得時のパラメータが変わる度に監視し直す方法](#livedata取得時のハラメータか変わる度に監視し直す方法)
	- [概要](#概要)
	- [コード](#コード)

<!-- /TOC -->


# LiveData取得時のパラメータが変わる度に監視し直す方法

## 概要

LiveDataを監視する場合、Activity/Fragmentで`viewModel.getXxxLiveData(param).observe()`とするが、アタッチしたActivity/Fragmentが生存している間に`param`が動的に変更する場合は、工夫が必要となる。このドキュメントでは、その実装方法について解説する。


## コード

**SearchFragment.java**

```Java
LiveData<List<FileDirectory>> list = Transformations.switchMap(viewModel.getSearchKeyWord(),
        new Function<String, LiveData<List<FileDirectory>>>() {
            @Override
            public LiveData<List<FileDirectory>> apply(String input) {
                return dao.searchLiveDataBy(input);
            }
        });
list.observe(getViewLifecycleOwner(), new Observer<List<FileDirectory>>() {
    @Override
    public void onChanged(List<FileDirectory> fileDirectories) {
        adapter.setRows(MemoListHelper.transItemType(fileDirectories));
        sortList();
        viewModel.getListSize().setValue(fileDirectories.size());
    }
});
```


**XxxDao.java**

```Java
@Query("SELECT * FROM FileDirectory WHERE " +
        "item_name LIKE ('%'||:queryString||'%') OR " +
        "file_content LIKE ('%'||:queryString||'%')")
LiveData<List<FileDirectory>> searchLiveDataBy(String queryString);
```


`Transformations.switchMap()`メソッドを使用するのがポイントである。

このメソッドの引数、返値は以下の通りである。

`LiveData<Y> switchMap (LiveData<X> source,
                Function<X, LiveData<Y>> switchMapFunction)`

Functionクラスについては、Markdownドキュメントの「関数型インターフェース」を参照。

`switchMap`メソッドは、簡単に説明すると、

- sourceからgetValue()で、LiveDataの中身の値を取得する。
- その値をswitchMapFunction（実装は自分で定義する）の引数に渡して、処理を実行する。
- 実行結果を`switchMap`メソッドの返値として返す。

という処理を行う。

`switchMap`メソッドのすごいところは、`source`の中身のデータが変更される度に`switchMapFunction`で定義したメソッド(`apply`メソッド)が呼び出されるところである。

これにより、`source`のデータが変更される度に、`viewModel.
getXxxLiveData(source)`を呼び出すことが可能になり、その結果、`source`の値に応じた`xxxLiveData`を常に監視することができるようになる。



