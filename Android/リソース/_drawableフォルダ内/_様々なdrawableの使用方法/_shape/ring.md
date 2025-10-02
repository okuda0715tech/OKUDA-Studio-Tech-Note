<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [ring](#ring)
	- [<ring\>タグの属性](#ring属性)
	- [<solid\>タグの属性](#solid属性)
	- [<stroke\>タグの属性](#stroke属性)

<!-- /TOC -->


# ring

## <ring\>タグの属性

- android:thickness
  - リングの厚さ。
- android:innerRadius
  - リングの内側の円の半径


## <solid\>タグの属性

- android:color
  - ドーナツ状のリング部分の塗りつぶし色


## <stroke\>タグの属性

`<stroke>`タグは、リングの外側と内側のエッジに輪郭（りんかく）線を描きます。

- android:color
  - 輪郭線の色
- android:width
  - 輪郭線の太さ

輪郭線は`1dp`や`2dp`程度の細い線にすることをお勧めします。  
なぜなら、線が太くなるほどリングの塗りつぶし部分が輪郭線で覆われてしまうためです。
