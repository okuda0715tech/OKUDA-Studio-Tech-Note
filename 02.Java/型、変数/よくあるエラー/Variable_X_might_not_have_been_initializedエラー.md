<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [Variable_X_might_not_have_been_initializedエラー](#variable_x_might_not_have_been_initializedエラー)
<!-- TOC END -->


# Variable_X_might_not_have_been_initializedエラー

Javaのプリミティブ型の変数はデフォルト値を持っています。ただし、それはフィールド変数に  
限った話であり、ローカル変数はデフォルト値を持っていません。  
そのため、初期化されていないローカル変数を参照すると上記のエラーが発生します。

これは考えてみればバグを防ぐための仕様であると思われる。  
ローカル変数の場合、スコープがメソッド内に限られるため、  
初期化されていないローカル変数をいきなり参照している場合、  
必ずデフォルト値が取得されるため、そもそも変数を参照する必要がない。  
それにもかかわらず、変数を参照しているということは、開発者が「何らかの値が入っている」と  
考えている可能性が高く、開発者の勘違いによるバグにつながる可能性が非常に高い。
