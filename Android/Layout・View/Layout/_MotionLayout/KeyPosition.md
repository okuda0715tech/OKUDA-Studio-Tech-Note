<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [KeyPosition](#keyposition)
  - [属性](#属性)
    - [motion:motionTarget](#motionmotiontarget)
    - [motion:framePosition](#motionframeposition)
    - [motion:percentX / motion:percentY](#motionpercentx--motionpercenty)
    - [motion:keyPositionType](#motionkeypositiontype)
<!-- TOC END -->


# KeyPosition

## 属性

### motion:motionTarget

この `<KeyPosition>` によってモーションが制御される View を指定します。


### motion:framePosition

`framePosition` は、わかりやすく言うと、  
0 ～ 100 ページまでの 101 ページから成るパラパラマンガがあるとして、  
その何コマ目かを指定する属性です。

実際に指定できる値は、 1 ～ 99 までの整数になります。  
( 0 は始点、 100 は終点になります。)


### motion:percentX / motion:percentY

ある `framePosition` における View の位置を表します。

`keyPositionType` 属性の値によって、これらの値の解釈方法が変わってきます。


### motion:keyPositionType

`percentX` 値と `percentY` 値の解釈方法を指定します。


#### parentRelative

`percentX` と `percentY` は親ビューを基準に指定されます。  
`X` は横軸で、 `0` は、親 View の左端を意味し、 `1` は、親 View の右端を意味します。  
`X` は縦軸で、 `0` は、親 View の上端を意味し、 `1` は、親 View の下端を意味します。  


#### deltaRelative

画面横方向を X 軸とし、画面縦方向を Y 軸と定義します。  
`percentX` は、 View が移動する軌跡 (※ 1 ) の X 軸方向の距離を 100 % として、  
その何パーセントにあたるかを示します。  
`percentY` は、 View が移動する軌跡 (※ 1 ) の Y 軸方向の距離を 100 % として、  
その何パーセントにあたるかを示します。

```
(※ 1 )  
ここでは、 View の軌跡は直線であることを前提とします。  
軌跡が曲線である場合についての確認はとれていません。
```


#### pathRelative

View が移動する軌跡 (※ 1 ) に対して、その進行方向を X 軸と定義します。  
X 軸に垂直な方向を Y 軸と定義します。

```
(※ 1 )  
ここでは、 View の軌跡は直線であることを前提とします。  
軌跡が曲線である場合についての確認はとれていません。
```

`pathRelative` を指定する場合は、画面の横方向が X 軸、縦方向が Y 軸とは限らない点に  
注意してください。

`motion:percentX` も `motion:percentY` も軌跡の長さを 100 % として、  
「その何パーセントにあたるか」 という長さを計算します。  
計算した長さを、上記で定義した X 軸 or Y 軸方向に対して適用して KeyPosition を決定します。
