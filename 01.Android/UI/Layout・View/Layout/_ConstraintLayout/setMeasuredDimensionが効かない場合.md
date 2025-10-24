<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [setMeasuredDimensionが効かない場合](#setmeasureddimensionが効かない場合)
  - [概要](#概要)
  - [原因と対処方法](#原因と対処方法)
<!-- TOC END -->


# setMeasuredDimensionが効かない場合

## 概要

ConstraintLayout内にカスタムViewを配置し、そのカスタムViewのサイズを動的に変更する場合、  
カスタムViewの `onMeasure()` メソッド内でカスタムViewのサイズを指定しても、そのサイズで表示されないことがある。


## 原因と対処方法

レイアウトxml内でカスタムViewの `layout_height` や `layout_width` が  
`0dp` や `match_parent` になっていると動的に指定したサイズが反映されない。  

レイアウトxmlでは、 `100dp` などの固定値を指定する必要がある。

そして、カスタムViewの `onMeasure()` メソッド内でサイズを算出し、  
算出したサイズを `setMeasuredDimension()` メソッド等で確定させます。
