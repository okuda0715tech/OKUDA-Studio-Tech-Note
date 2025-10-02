<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [ObjectAnimator](#objectanimator)
	- [透明から不透明にするサンプル](#透明不透明)

<!-- /TOC -->


# ObjectAnimator

## 動的に使用する方法

### 透明から不透明にするサンプル

```Java
ObjectAnimator animation = ObjectAnimator.ofFloat(textView, "alpha", 0f, 1f);
animation.setDuration(1000);
```


### 横スライドするサンプル

```Java
// Viewの初期位置から100ピクセル右へ移動する
ObjectAnimator animation = ObjectAnimator.ofFloat(textView, "translationX", 100f);
animation.setDuration(1000);
```


### アニメーションを開始する

`ObjectAnimator.start()`メソッドでアニメーションを開始します。
