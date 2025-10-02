<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [AnimatorSet](#animatorset)
  - [実行順序制御](#実行順序制御)
  - [アニメーション開始](#アニメーション開始)
  - [API Level 26 未満の場合、AnimatorSetのアニメーションを途中から開始することができない。](#api-level-26-未満の場合animatorsetのアニメーションを途中から開始することができない)
<!-- TOC END -->


# AnimatorSet

## 実行順序制御

- 同時実行
  - `AnimatorSet.playTogether(animator, animator)`


## アニメーション開始

```Java
AnimatorSet.start();
```


## API Level 26 未満の場合、AnimatorSetのアニメーションを途中から開始することができない。

API Level 26 未満では、`setCurrentPlayTime()`メソッドが使用できない。  
AnimatorSet内の個別のAnimatorに対して`setCurrentPlayTime()`を設定することはできるが、  
そのAnimatorSetをstartしても、セットした時間から開始されない。

API Level 26 未満でアニメーションを途中から開始したい場合、AnimatorSetを使用せずに、  
個別のAnimatorを連携して独自に実装する必要がある。
