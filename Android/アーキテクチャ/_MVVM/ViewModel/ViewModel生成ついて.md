<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [ViewModel 生成ついて](#viewmodel-生成ついて)
  - [ViewModel の生成方法（Java）](#viewmodel-の生成方法java)
  - [ViewModel の生成方法（Kotlin）](#viewmodel-の生成方法kotlin)
  - [ViewModelStore と ViewModelStoreOwner](#viewmodelstore-と-viewmodelstoreowner)
  - [Navigation コンポーネントを使用する場合](#navigation-コンポーネントを使用する場合)
    - [【参考】NavController.getViewModelStoreOwner() で取得するのは問題ないか？](#参考navcontrollergetviewmodelstoreowner-で取得するのは問題ないか)
<!-- TOC END -->


# ViewModel 生成ついて

## ViewModel の生成方法（Java）

`ViewModel` は、 `ViewModelProvider` を使用して、以下の要領で生成します。

**MyFragment.java**

```java
ViewModelProvider viewModelProvider = new ViewModelProvider(
        getViewModelStore(),
        getDefaultViewModelProviderFactory()
);
MyViewModel viewModel viewModelProvider.get(MyViewModel.class);
```

`ViewModelProvider` の第一引数には、 `ViewModelStore` もしくは、 `ViewModelStoreOwner` を渡します。  
**この引数によって、 ViewModel のスコープが決定します。**  
第一引数に渡した `Activity` や `Fragment` が恒久的に破棄されるタイミングで `ViewModel` も破棄されます。


## ViewModel の生成方法（Kotlin）

```Kotlin
// NG (直接生成すると構成変更時に ViewModel も破棄されてしまう)
val viewModel = MainViewModel()

// OK (この方法なら構成変更時に ViewModel が破棄されません )
val viewModel: MainViewModel by viewModels()

// OK (この方法なら構成変更時に ViewModel が破棄されません )
val viewModel = ViewModelProvider(this)[MainViewModel::class.java]
```


## ViewModelStore と ViewModelStoreOwner

`ViewModelStore` とは、 `ViewModel` を格納して管理するオブジェクトです。  
その `ViewModelStore` を `Activity` や `Fragment` が保持するわけですが、  
(保持することを示すために？) 、 `Activity` や `Fragment` は `ViewModelStoreOwner` インターフェースを  
実装しているようです。

`ViewModelStoreOwner` 自体には、 `getViewModelStore()` 抽象メソッドしか存在しておらず、  
おそらく、実際に `Activity / Fragment` のライフサイクルに応じて  
`ViewModel` を保持したり破棄したりする役割は、 `ViewModelStore` クラスが担っていると思われる。


## Navigation コンポーネントを使用する場合

Navigation コンポーネントを使用する場合は、 `ViewModelProvider` を生成するときに渡す  
`ViewModelStoreOwner` は、以下の方法で取得する必要があります。  
`Fragment.getViewModelStore()` で取得した `ViewModelStore` を渡してはいけません。  

**SampleFragment.java**

```Java
NavController navController = NavHostFragment.findNavController(this); // this は Fragment を示す。
NavBackStackEntry backStackEntry = navController.getCurrentBackStackEntry();
// 【参考】 DESTINATION ID を指定して取得することも可能
NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.nav_destination_id);
```

上記の方法で取得した `NavBackStackEntry` は、 `ViewModelStoreOwner` インターフェースを  
実装しているため、 `ViewModelStoreOwner` を取得しているのと同じです。

`BackStackEntry` を渡すことで、 `Fragment` がナビゲーションのバックスタックからポップされた時など  
ナビゲーション操作と連動して、 `ViewModel` を管理することができるようになります。


### 【参考】NavController.getViewModelStoreOwner() で取得するのは問題ないか？

`ViewModelProvider` を生成するときに渡す `ViewModelStoreOwner` は、以下の方法で取得することも可能です。  
しかし、結論を先に述べると、問題はありませんが、 `NavController.getCurrentBackStackEntry()` の方が良いです。

```Java
NavController navController = NavHostFragment.findNavController(this); // this は Fragment を示す。
ViewModelStoreOwner storeOwner = navController.getViewModelStoreOwner(R.id.nav_graph_id),
```

この方法で取得した `ViewModelStoreOwner` は、一つ上のサンプルで取得した `NavBackStackEntry`  
と同じインスタンスを取得します。

`NavBackStackEntry` は、 `LifecycleOwner` インターフェースも実装しているため、  
その後、 `LiveData` を `observe()` する時の第一引数にも渡すことができます。  
また、 `NavBackStackEntry` は、データバインディングと `LiveData` を同時に使用する場合に必要になる
`binding.setLifecycleOwner(backStackEntry);` の引数にも渡すことができます。  
ただし、 `setLifecycleOwner()` の引数には、基本的に `getViewLifecycleOwner()` を渡してください。  
その理由は、画面が更新されない不具合が発生してしまうためです。  
詳細は、別紙 「 DataBindig フォルダ」 の 「 setLifecycleOwner 」 のドキュメントを参照してください。
