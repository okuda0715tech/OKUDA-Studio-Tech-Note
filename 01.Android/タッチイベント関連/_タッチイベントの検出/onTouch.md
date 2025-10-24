<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [onTouch](#ontouch)
	- [返値](#返値)

<!-- /TOC -->


# onTouch

## 返値

タップ直後に呼び出される`onTouch`メソッドでfalseを返すと、そのイベントに興味をないことを示し、  
そのタップした指をスワイプしたときや画面から指を話した時に、`onTouch`メソッドが呼ばれなくなります。

つまり、`ACTION_DOWN`の場合にfalseを返すと、その後の、`ACTION_MOVE`、`ACTION_UP`の時に  
`onTouch`が呼び出されなくなります。
