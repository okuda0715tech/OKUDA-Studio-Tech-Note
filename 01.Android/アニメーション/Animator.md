<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [Animator](#animator)
	- [実行中のアニメーションを中止する](#実行中中止)

<!-- /TOC -->


# Animator

## 実行中のアニメーションを中止する

```Java
Animator anim = ...;
anim.cancel();
```

アニメーションをキャンセルした場合は、対象のViewはアニメーション終了後の状態となる。


## リスナーの設定

**継承ツリー**

- android.animation.Animator.AnimatorListener
  - android.animation.AnimatorListenerAdapter

**各クラスの違い**

- AnimatorListener
  - インターフェースであるため、全ての抽象メソッドを実装する必要がある。
- AnimatorListenerAdapter
  - AnimatorListenerの全ての抽象メソッドを空実装している抽象クラスである。
  - そのため、必要なメソッドのみをオーバーライドすることができ、実装が簡潔になる。

**実装方法**

```Java
Animator animator;
animator.addListener(new AnimatorListenerAdapter(){
	@Override
	public void onAnimationCancel(Animator animation) {
		...
	}
});
```
