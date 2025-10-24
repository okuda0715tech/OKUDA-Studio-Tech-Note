<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [observe() メソッドの第一引数について](#observe-メソッドの第一引数について)
  - [第一引数に与える LifecycleOwner とは](#第一引数に与える-lifecycleowner-とは)
  - [Navigation コンポーネントを利用して表示した Fragment で observe する場合](#navigation-コンポーネントを利用して表示した-fragment-で-observe-する場合)
    - [LifecycleOwner と Lifecycle の違い](#lifecycleowner-と-lifecycle-の違い)
<!-- TOC END -->


# observe() メソッドの第一引数について

## 第一引数に与える LifecycleOwner とは

`observe()` メソッドの第一引数は、 `LifecycleOwner` インターフェースを実装しているオブジェクトを  
受け取ります。 `LifecycleOwner` インターフェースは、 `Fragment` や `Activity` など、  
ライフサイクル関係のコールバックを持っているコンポーネントが実装するインターフェースです。


## Navigation コンポーネントを利用して表示した Fragment で observe する場合

Navigation コンポーネントを利用して表示した Fragment で observe する場合には、  
`observe()` メソッドの第一引数に `NavBackStackEntry` オブジェクトを渡します。  
`NavBackStackEntry` を渡すことによって、 Navigation のバックスタックから Fragment が  
ポップされたときに、 Fragment が Destroy されるなど、 Navigation コンポーネントによる  
操作と Fragment のライフサイクルを連携することが可能になります。

`NavBackStackEntry` オブジェクトは、以下の方法で取得できます。

**MyFragment.java**

```java
NavController navController;

@Override
public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    navController = NavHostFragment.findNavController(this); // this は Fragment のこと。
}

// backStackEntry には、現在表示されているこの Fragment が格納されます。
NavBackStackEntry backStackEntry = navController.getCurrentBackStackEntry();
```

### LifecycleOwner と Lifecycle の違い

`LifecycleOwner` は、 `Fragment / Activity` などのライフサイクルを持つコンポーネントそのものを示す。  
`Lifecycle` は、 `ON_CREATE / ON_START / ON_RESUME` などの状態そのものを示す。
