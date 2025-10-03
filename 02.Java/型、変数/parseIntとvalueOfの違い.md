<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [parseIntとvalueOfの違い](#parseintとvalueofの違い)
<!-- TOC END -->


# parseIntとvalueOfの違い

String型のデータをint型やInteger型に変換したい場合には、  
Integerクラスには、 `parseInt(String)` メソッドと `valueOf(String)` メソッドが用意されています。

`parseInt(String)` メソッドは、プリミティブ型のint型を返します。  
`valueOf(String)` メソッドは、参照型のInteger型を返します。

`Redundant boxing` ワーニングが発生している場合は、不要なboxingが発生していることを示しているので、  
`parseInt` と `valueOf` を入れ替えると解消する可能性があります。
