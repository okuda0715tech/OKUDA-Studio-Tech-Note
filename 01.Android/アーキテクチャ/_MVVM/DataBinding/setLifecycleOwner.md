<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [setLifecycleOwner](#setlifecycleowner)
  - [setLifecycleOwner() メソッドの役割](#setlifecycleowner-メソッドの役割)
<!-- TOC END -->


# setLifecycleOwner

## setLifecycleOwner() メソッドの役割

データバインディングを使用していると、以下のサンプルコードのように、 Fragment 内で、  
`binding.setLifecycleOwner(xxx);` というメソッドを呼び出すことがよくあります。

```java
public View onCreateView(@NonNull LayoutInflater inflater,
                         @Nullable ViewGroup container,
                         @Nullable Bundle savedInstanceState) {
    FragmentWinLoseBinding binding =
            DataBindingUtil.inflate(
                    inflater,
                    R.layout.fragment_win_lose,
                    container,
                    false);
    // パターン１
    binding.setLifecycleOwner(backstackEntry);
    // パターン２
    binding.setLifecycleOwner(getViewLifecycleOwner());
    return binding.getRoot();
}
```

これは、 **「データバインディング式 (以下サンプル) を、いつ破棄するのか」** を指定するためのものです。

```xml
<View android:enabled="@{viewModel.isShown ? false : true}" />
```

以下の 「パターン１」 のように、パラメータに `Fragment` を渡すと、 `Fragment` の `onDestroy()`  
が呼ばれるタイミングと同じタイミングで、データバインディング式を破棄します。

```java
// パターン１
binding.setLifecycleOwner(fragment);
```

また、 「パターン２」 のように、パラメータに `getViewLifecycleOwner()` を渡すと、  
`Fragment` の `onDestroyView()` が呼ばれるタイミングと同じタイミングで、データバインディング式を破棄します。

```java
// パターン２
binding.setLifecycleOwner(getViewLifecycleOwner());
```

**基本的には、 「パターン２」 のように、 `getViewLifecycleOwner()` を渡すのが正解だと思われます。**  
なぜなら、 「 `Fragment` は破棄されていないけれど、その `View` のみ破棄される」 という状況になった場合に、  
「パターン１」 の実装をしていた場合は、画面が更新されない不具合が発生してしまうためです。

その原因は、データバインディング式の実行結果を、再生成された新しい `View` へと、伝えることができず、  
破棄された古い `View` へ伝えてしまうためです。
