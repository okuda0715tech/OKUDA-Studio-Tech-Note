<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [MotionEvent](#motionevent)
	- [getAction()とgetActionMasked()の違い](#getactiongetactionmasked違)
		- [どちらを使用すればよいのか](#使用)

<!-- /TOC -->


# MotionEvent

## getAction()とgetActionMasked()の違い

- getActionMasked()
  - INDEXを含まないActionを返す。
  - 例えば、`ACTION_DOWN`(Constant Value: 0 (0x00000000))や  
  - `ACTION_POINTER_DOWN`(Constant Value: 5 (0x00000005))などを返す。
- getAction()
  - Action + シフトされたINDEXを返す。

### どちらを使用すればよいのか

`getActionMasked()`を使用するほうが良い。なぜなら、取得したActionを何も加工しなくても、  
`switch`文などで`case MotionEvent.ACTION_DOWN`として分岐させることができるため。  
（もし、ActionとINDEXを同時に取得するとINDEXを分離してからでなければ判定ができない。）

INDEXを取得したい場合は、別途、`getActionIndex()`メソッドを使用するのが良い。
