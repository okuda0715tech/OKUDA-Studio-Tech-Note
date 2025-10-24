<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [wrap_contentしているViewの高さを取得する方法](#wrap_contentしているviewの高さを取得する方法)

<!-- /TOC -->


# wrap_contentしているViewの高さを取得する方法

```Java
// 第一引数が幅、第二が高さ
view.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
int height = view.getMeasuredHeight();
```

