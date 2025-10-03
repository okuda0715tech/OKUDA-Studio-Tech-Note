<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [SnackBar](#snackbar)
	- [概要](#概要)
	- [使用方法](#使用方法)
	- [makeメソッドのviewに与えるパラメーターがない場合](#makeメソッドのviewに与えるパラメーターがない場合)

<!-- /TOC -->


# SnackBar

## 概要

Toastに変わる簡単なメッセージを画面に表示するためのウィジェットにSnackBarというものがある。


## 使用方法

```Java
Snackbar.make(view, resource, Snackbar.LENGTH_SHORT).show();
```

`resource`には、表示したい文字列がStringで入ります。


## makeメソッドのviewに与えるパラメーターがない場合

以下のようにActivityの根底に自動的に作成されるViewを使用して表示することができます。

```Java
View view = activity.findViewById(android.R.id.content);
if (view == null) return;
Snackbar.make(view, resource, Snackbar.LENGTH_SHORT).show();
```




