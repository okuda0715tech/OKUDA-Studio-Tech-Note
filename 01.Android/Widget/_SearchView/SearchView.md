<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [SearchView](#searchview)
	- [入力された文字列を取得する方法](#入力された文字列を取得する方法)
	- [SearchViewに文字列をセットする方法](#searchviewに文字列をセットする方法)
	- [未入力状態で表示するヒントをセットする方法](#未入力状態で表示するヒントをセットする方法)
	- [SearchViewを画面幅いっぱいに表示する方法](#searchviewを画面幅いっぱいに表示する方法)

<!-- /TOC -->


# SearchView

## 入力された文字列を取得する方法

```Java
searchView.getQuery()
```


## SearchViewに文字列をセットする方法

```Java
searchView.setQuery()
```


## 未入力状態で表示するヒントをセットする方法

```Java
searchView.setQueryHint(getString(R.string.query_hint));
```


## SearchViewを画面幅いっぱいに表示する方法

```Java
searchView.setMaxWidth(Integer.MAX_VALUE);
```


