<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [Transformations](#transformations)
  - [概要](#概要)
  - [使用方法](#使用方法)
    - [Transformations.map()](#transformationsmap)
    - [Transformations.switchMap()](#transformationsswitchmap)
  - [map() と switchMap() の違い](#map-と-switchmap-の違い)
  - [【参考】 map() , switchMap() のメソッド定義](#参考-map--switchmap-のメソッド定義)
  - [switchMap() のサンプル](#switchmap-のサンプル)
  - [初期値の代入方法](#初期値の代入方法)
<!-- TOC END -->


# Transformations

## 概要

`Transformations` クラスの `map()` メソッドと `switchMap()` メソッドは、ある `LiveData`  
を観察し、その値が変更された時に、何らかの処理 ( `Function` ) を実行して、その結果を新たな  
`LiveData` オブジェクトで返すメソッドです。


## MediatorLiveData との違い

`MediatorLiveData` を直接生成する場合との違いは、  
監視している `LiveData` の値が更新されたときに、何らかの処理を行うか、行わないかの違いです。

何らかの処理を行って、その結果を `MediatorLiveData` にセットしたい場合には、 `Transformations`  
クラスを使用した方が簡潔に実装することができます。

また、観察している `LiveData` が更新された時に、何らかの処理を行いたい場合でも、  
二つ以上の `LiveData` を観察して、その結果を一つの `LiveData` に統合したい場合には、  
`MediatorLiveData` を使用する必要があります。


## 使用方法

### Transformations.map()

`map(LiveData<X> source, Function<X, Y> mapFunction)` メソッドは、  
「第一引数の `LiveData` と第二引数の `Function` をマッピングする」という意味のメソッドである。

第一引数の `LiveData` の中身が更新された場合に、その値を `Function` のインプットとして  
`Function` を実行し、その結果を `map()` メソッドの返値である `LiveData` にセットします。

```java
LiveData<User> userLiveData = ...;
LiveData<String> userName = Transformations.map(userLiveData, user -> {
    user.name + " " + user.lastName
});
```

### Transformations.switchMap()

`map()` メソッドとの違いは次項を参照。

```Java
private LiveData<User> getUser(String id) {
  // このメソッドが DB のデータをクエリしているなど、メソッドのインプットが同じでも
  // 返す値が異なる可能性がある場合には、 map() メソッドではなく、 switchMap() メソッドを
  // 使用する必要があります。
  // switchMap() メソッドを使用すると、 LiveData が更新された場合に MediatorLiveData
  // へ反映してくれます。
}

LiveData<String> userId = ...;
LiveData<User> user = Transformations.switchMap(userId, id -> getUser(id) );
```

## map() と switchMap() の違い

`map()` は、第二引数の `Function` の返値が `LiveData` 型ではない場合に使用する。  
`switchMap()` は、第二引数の `Function` の返値が `LiveData` 型である場合に使用する。  

`switchMap()` の名前の由来は、 `Function` が実行される度に、新しい `LiveData`  
オブジェクトを生成し、それを `switchMap()` の第一引数である `LiveData` とマッピングし直すため、  
「マッピングが何度も入れ替わる」という意味で `switchMap()` という名前になっている。


## 【参考】 map() , switchMap() のメソッド定義

参考までに map() , switchMap() のメソッド定義を記載します。

```Java
@MainThread
@NonNull
public static <X, Y> LiveData<Y> map(
        @NonNull LiveData<X> source,
        @NonNull final Function<X, Y> mapFunction) {

    final MediatorLiveData<Y> result = new MediatorLiveData<>();
    result.addSource(source, new Observer<X>() {
        @Override
        public void onChanged(@Nullable X x) {
            result.setValue(mapFunction.apply(x));
        }
    });
    return result;
}

/**
 * このメソッドはややこしいので、あまり見ないほうがよい。
 * 大切なのは、 Function の返値が LiveData 型であることと、
 * それによって、 switchMap() の第一引数 LiveData の値が更新されなくても
 * Function の実行結果が変わる可能性がある場合には、 map() ではなく、 switchMap() を
 * 使用するべきであること。
 **/
@MainThread
@NonNull
public static <X, Y> LiveData<Y> switchMap(
        @NonNull LiveData<X> source,
        @NonNull final Function<X, LiveData<Y>> switchMapFunction) {

    final MediatorLiveData<Y> result = new MediatorLiveData<>();
    result.addSource(source, new Observer<X>() {
        LiveData<Y> mSource;

        @Override
        public void onChanged(@Nullable X x) {
            LiveData<Y> newLiveData = switchMapFunction.apply(x);
            if (mSource == newLiveData) {
                return;
            }
            if (mSource != null) {
                result.removeSource(mSource);
            }
            mSource = newLiveData;
            if (mSource != null) {
                result.addSource(mSource, new Observer<Y>() {
                    @Override
                    public void onChanged(@Nullable Y y) {
                        result.setValue(y);
                    }
                });
            }
        }
    });
    return result;
}
```

## switchMap() のサンプル

**XxxFragment.java**

```java
@Override
public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    final FileDirectoryDao dao = AppDatabaseSingleton.getInstance(requireContext()).fileDirectoryDao();

    // 重要。thisじゃなくて、getViewLifecycleOwner
    if (TAG_LIST_ACT.equals(getTag())) {
        // 略
    } else if (TAG_SEARCH_ACT.equals(getTag())) {
        // 検索画面のリスト表示の場合は、検索キーワードが変更される度に DB からデータを取得し直したい。
        // 検索キーワードは LiveData 型の SearchKeyWord 変数に保持しているため、 SearchKeyWord の更新を
        // 監視し続け、変更があった場合に、 DB からデータを取得する処理(Function)を実行したいため、
        // switchMap を使用している。
        // 一方、リスト画面のリスト表示は、検索キーワードのようなインプットは必要とせず、 DB の更新だけを
        // トリガーとして、 DB が更新された場合にのみ DB からデータを取得すれば良いため、 switchMap
        // は必要とせず、 Dao の Select文の返値を LiveData 型にするだけで良い。
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
    }
}
```


## 初期値の代入方法

`Transformations.map()` や `Transformations.switchMap()` で取得した `MediatorLiveData` は、
`MutableLiveData` を扱う時と同じ方法では初期値を与えることはできません。

初期値を与えるには、 `map()` や `switchMap()` の第一引数で与える `LiveData` をゲッターメソッドで参照し、  
そのゲッターメソッド内で、 `MutableLiveData` に初期値を与えます。

こうすることで、 `MediatorLiveData` が生成される直前に、その参照元の `MutableLiveData` が必ず初期化され、  
その値で `MediatorLiveData` を初期化することが可能です。

```Java
private MutableLiveData<String> exeWeekText;

public MutableLiveData<String> getExeWeekText() {
    if (exeWeekText == null) {
        exeWeekText = new MutableLiveData<>("initial value")
    }
    return exeWeekText;
}

LiveData<Boolean> isSatisfied;

public LiveData<Boolean> getIsSatisfied() {
    if (isSatisfied == null) {
        isSatisfied = Transformations.map(
                getExeWeekText(),  // <---- ポイントはここ！フィールド参照ではなく、ゲッターメソッドを参照する。
                exeWeekText -> // do something.
        );
    }
    return isSatisfied;
}
```
