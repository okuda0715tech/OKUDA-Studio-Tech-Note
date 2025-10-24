<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [ValueAnimator](#valueanimator)
	- [ValueAnimatorインスタンスの生成](#valueanimator生成)
	- [アニメーションにかかる時間の指定](#時間指定)
	- [1フレームごとの値を算出する](#1値算出)
	- [算出した1フレームごとの値を使用して、アニメーションを行う](#算出1値使用行)

<!-- /TOC -->


# ValueAnimator

## ValueAnimatorインスタンスの生成

```Java
// アニメーションの開始時の値が0f,修了時の値が100fのインスタンスを生成する例
ValueAnimator animator = ValueAnimator.ofFloat(0f, 100f);
```

`ofFloat`メソッド以外に以下のメソッドでインスタンスを生成することができる。

- `ofInt(int... values)`
- `ofObject(TypeEvaluator evaluator, Object... values)`
- `ofArgb(int... values)`
- `ofPropertyValuesHolder(PropertyValuesHolder... values)`


## アニメーションにかかる時間の指定

```Java
ValueAnimator animation = ValueAnimator.ofFloat(0f, 100f);
// 1000ミリ秒間でアニメーションが実行される設定
animation.setDuration(1000);
```


## 1フレームごとの値を算出する

```Java
ValueAnimator animation = ValueAnimator.ofFloat(0f, 100f);
animation.setDuration(1000);
animation.start();
```

`start()`メソッドを実行すると、1フレームごとの値を算出する。


## 算出した1フレームごとの値を使用して、アニメーションを行う

```Java
animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
    // updatedAnimationパラメータは、一つ前のフレームからの差分ではなく、アニメーション開始時点からの差分である。
    // 例えば、0f～100fまでのアニメーションの場合、線形補間ならば、0f,10f,20f,...,90f,100fという値が渡されてくる。
    @Override
    public void onAnimationUpdate(ValueAnimator updatedAnimation) {
        float animatedValue = (float)updatedAnimation.getAnimatedValue();
        // translationXメソッドは指定した距離だけX方向へViewを移動するメソッド
        textView.setTranslationX(animatedValue);
    }
});
```

計算された1フレームごとの値が1フレームごとのタイミングで`onAnimationUpdate()`メソッドにコールバックされます。

システムがビジー状態のときなどは、いくつかのコールバックがスキップされる。
