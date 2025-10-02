<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [weightの設定方法](#weight設定方法)
	- [チェーンした複数のViewの中で残りのスペースをすべて埋める場合](#複数view中残埋場合)

<!-- /TOC -->


# weightの設定方法

## チェーンした複数のViewの中で残りのスペースをすべて埋める場合

引き伸ばしたいViewの幅や高さに`0dp`を指定することで残りのスペースをすべて埋めることができます。

比率を指定したい場合は、`layout_constraintHorizontal_weight`もしくは`layout_constraintVertical_weight`を指定することで比率を指定できます。  
これは、`LinearLayout`の`layout_weight`と同じように使用することができます。
