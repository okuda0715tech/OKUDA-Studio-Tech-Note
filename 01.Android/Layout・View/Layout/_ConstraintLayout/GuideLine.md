<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [GuideLine](#guideline)
  - [概要](#概要)
  - [使いどころ](#使いどころ)
  - [サンプル](#サンプル)
  - [サンプルの解説](#サンプルの解説)
<!-- TOC END -->


# GuideLine

## 概要

GuideLine は、親 ViewGroup の上下左右の位置を基準にして、そこから **dp 指定**、もしくは、  
**% 指定** した場所に、幅が 0 dp の一本の線を引く役割があります。


## 使いどころ

画面の端に指定した幅の余白を設けたい場合に使用します。


## サンプル

```xml
<androidx.constraintlayout.widget.Guideline
    android:id="@+id/guidelinePercent"
    android:layout_width="0dp"
    android:layout_height="0dp"
    app:layout_constraintGuide_percent="0.25"
    android:orientation="vertical" />

<androidx.constraintlayout.widget.Guideline
    android:id="@+id/guidelineDpEnd"
    android:layout_width="0dp"
    android:layout_height="0dp"
    app:layout_constraintGuide_end="120dp"
    android:orientation="vertical" />

<androidx.constraintlayout.widget.Guideline
    android:id="@+id/guidelineDpTop"
    android:layout_width="0dp"
    android:layout_height="0dp"
    app:layout_constraintGuide_begin="100dp"
    android:orientation="horizontal" />
```


## サンプルの解説

`android:layout_width` : GuideLine に幅の指定は必要ないため、常に 0 dp を指定します。  
`android:layout_height` : GuideLine に高さの指定は必要ないため、常に 0 dp を指定します。  
`android:orientation` : `horizontal` の場合は水平線、 `vertical` の場合は垂直線になります。  
`app:layout_constraintGuide_percent` : 水平線の場合は上端から何 % か、垂直線の場合は左端から何 % かを指定します。  
`app:layout_constraintGuide_begin` : 水平線の場合は上端からの距離、垂直線の場合は左端からの距離を指定します。  
`app:layout_constraintGuide_end` : 水平線の場合は下端からの距離、垂直線の場合は右端からの距離を指定します。

`percent` と `begin` or `end` を同時に使用することもできるかも？








