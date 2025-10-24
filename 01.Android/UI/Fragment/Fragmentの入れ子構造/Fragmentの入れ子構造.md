<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [Fragmentの入れ子構造](#fragmentの入れ子構造)
  - [入れ子構造の管理方法](#入れ子構造の管理方法)
  - [子フラグメントから親フラグメントのインスタンスを取得](#子フラグメントから親フラグメントのインスタンスを取得)
<!-- TOC END -->


# Fragmentの入れ子構造

## 入れ子構造の管理方法

フラグメントは `FragmentManager` クラスを使用して管理します。  
フラグメントを入れ子にする場合は、階層ごとに `FragmentManager` が存在します。

<img src="./manager-mappings.png" width="800">

`Activity` の直下で管理する `Fragment` を操作する `FragmentManager` は、  
`getSupportFragmentManager()` メソッドで取得します。

`Fragment` の直下で、別の `Fragment` を管理する場合は、 `FragmentManager` は、  
`getChildFragmentManager()` メソッドで取得します。

自分自身の `Fragment` を管理している `FragmentManager` を取得する場合は、  
`getParentFragmentManager()` メソッドで取得します。


## 子フラグメントから親フラグメントのインスタンスを取得

```java
fragment.requireParentFragment();
```
