<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [fillAfter_fillBefore_fillEnabled](#fillafterfillbeforefillenabled)
	- [結論から言うと](#結論言)
	- [fillEnabled](#fillenabled)
	- [fillBefore](#fillbefore)
	- [fillAfter](#fillafter)

<!-- /TOC -->


# fillAfter_fillBefore_fillEnabled

## 結論から言うと

デフォルトでは、アニメーション後にViewがアニメーション前の状態に戻る。  
アニメーション後の状態を維持するためには、`fillAfter="TRUE"`をセットする。  
TRUEをセットしても状態が維持されない場合は、親ViewGroupがアニメーション前の状態に戻る設定になっている可能性が高い。


## fillEnabled

まず、ドキュメントに誤りがあるらしく、この属性は「`fillBefore`にしか影響しない」と書かれているが、実際には`fillAfter`にも影響するらしい。

- 役割
  - `fillEnabled=true`の場合のみ`fillBefore/fillAfter`の設定が考慮され、  
  - `fillEnabled=false`の場合は、無視される。
- デフォルト値
  - ？


## fillBefore

- 役割
  - アニメーション終了後、Viewをアニメーション開始前の状態に戻す役割がある。
- デフォルト値
  - TRUE


## fillAfter

- 役割
  - アニメーション終了後、Viewをアニメーション終了後の状態で維持する
- デフォルト値
  - FALSE
