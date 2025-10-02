<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [android.graphics.Color](#androidgraphicscolor)
	- [定義ずみの色を使用する](#定義ずみの色を使用する)
	- [RGB(各0〜255)で指定する（不透明）](#rgb各0255で指定する不透明)
	- [ARGB(各0〜255)で指定する（透明度調整可能）](#argb各0255で指定する透明度調整可能)
	- [#AARRGGBB or #RRGGBB(各0〜F)で指定する（透明度調整可能）](#aarrggbb-or-rrggbb各0fで指定する透明度調整可能)

<!-- /TOC -->


# android.graphics.Color

## 定義ずみの色を使用する

```Java
Color.BLUE;
```


## RGB(各0〜255)で指定する（不透明）

```Java
Color.rgb(245,224,66);
```


## ARGB(各0〜255)で指定する（透明度調整可能）

```Java
// a=255 : 不透明
// a=0 : 透明
Color.argb(200,245,224,66);
```


## #AARRGGBB or #RRGGBB(各0〜F)で指定する（透明度調整可能）

```Java
// 不透明
Color.parseColor("#42F5EF");
// 透明度調整可能（A=F : 不透明, A=0 : 透明）
Color.parseColor("#6642F5EF");
```




