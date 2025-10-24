<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [MVVM](#mvvm)
  - [Model](#model)
  - [ViewModel](#viewmodel)
  - [View](#view)
  - [複数のFragmentでデータを共有する。](#複数のfragmentでデータを共有する)
  - [図解](#図解)
  - [参考資料](#参考資料)

<!-- /TOC -->


# MVVM

## Model

データモデルやビジネスロジックを実装した層のこと。
Observableを使用することで、ViewModelやConsumerやObserverから完全に分離することが可能となる。

## ViewModel

ViewModelはModelと相互に関係する。
ViewModelはViewから観測されるためのObservableを準備する。
VeiwModelはViewを気にする必要はない。(むしろ参照してはいけない。メモリリーク防止のため。)

## View

ViewはViewModleのObservableを観察し、変化が生じればUIに反映する。


## 複数のFragmentでデータを共有する。

以下のサンプルでは、一つのActivityに二つのFragmentを持っています。
一つはリスト、もう一つは詳細画面を表すFragmentです。
二つのFragmentが同一のActivityに持っている同一のSharedViewModelのインスタンスを参照しています。（おそらく、二つのFragmentは、どちらも`ViewModelProviders`の`of`メソッドの引数に`getActivity`で同一のActivityを指定しているところがポイントだと思う。）

**SharedViewModel.java**

```java
public class SharedViewModel extends ViewModel {
    private final MutableLiveData<Item> selected = new MutableLiveData<Item>();

    public void select(Item item) {
        selected.setValue(item);
    }

    public LiveData<Item> getSelected() {
        return selected;
    }
}
```

**MasterFragment.java**

```java
public class MasterFragment extends Fragment {
    private SharedViewModel model;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        itemSelector.setOnClickListener(item -> {
            model.select(item);
        });
    }
}
```

**DetailFragment.java**

```java
public class DetailFragment extends Fragment {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedViewModel model = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        model.getSelected().observe(this, { item ->
           // Update the UI.
        });
    }
}
```


## 図解

<img src="./画像/Android の MVVM の図.avif" width="800">

[引用元](https://qiita.com/iTakahiro/items/6b1b22efa69e55cea3fa)


## 参考資料

[DataBindingで実現するMVVM Architecture](https://speakerdeck.com/star_zero/databindingteshi-xian-surumvvm-architecture)
