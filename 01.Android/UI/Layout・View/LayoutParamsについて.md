<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [LayoutParamsについて](#layoutparams)
	- [LayoutParamsの役割](#layoutparams役割)
	- [具体的になにをしているか](#具体的)
	- [どのLayoutParamsを使用するのか](#layoutparams使用)
	- [具体的な使用方法](#具体的使用方法)
		- [動的にViewを追加するときに高さや幅を設定する](#動的view追加高幅設定)
		- [Viewのlayout_gravityを動的に変更する](#viewlayoutgravity動的変更)

<!-- /TOC -->


# LayoutParamsについて

## LayoutParamsの役割

親ViewGroupに自分自身のViewをどのように表示してほしいかを伝えるもの。

いつもレイアウトxmlで指定している`layout_width`や`layout_gravity`など、`layout_`で始まる全ての属性は、  
おそらく、LayoutParamsでも設定できるものである。
これらを動的に設定したい場合にLayoutParamsが使用されると思われる。


## 具体的になにをしているか

Viewの幅と高さについて、`match_parent`,`wrap_content`,`具体的な数値`で長さを指定します。

ここで指定した長さはまだ確定ではない。


## どのLayoutParamsを使用するのか

AddするViewの親ViewGroupのLayoutParamsを使用する。  
例えば、`LinearLayout`にaddするなら`LinearLayout.LayoutParams`を使用する。


## 具体的な使用方法

### 動的にViewを追加するときに高さや幅を設定する

動的にViewを追加するときに引数として渡します。

```Java
linearLayout.addView(button1, new LinearLayout.LayoutParams(
  LinearLayout.LayoutParams.WRAP_CONTENT,   // 幅
  LinearLayout.LayoutParams.WRAP_CONTENT)); // 高さ

linearLayout.addView(button2, new LinearLayout.LayoutParams(
  LinearLayout.LayoutParams.MATCH_PARENT,   // 幅
  LinearLayout.LayoutParams.MATCH_PARENT)); // 高さ

linearLayout.addView(button3, new LinearLayout.LayoutParams(
  100,   // 幅(単位:ピクセル)
  50)); // 高さ(単位:ピクセル)
```


### Viewのlayout_gravityを動的に変更する

```Java
TransitionManager.beginDelayedTransition(transitionsContainer,
    new ChangeBounds().setPathMotion(new ArcMotion()).setDuration(500));

FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) button.getLayoutParams();
params.gravity = isReturnAnimation ? (Gravity.LEFT | Gravity.TOP) :
    (Gravity.BOTTOM | Gravity.RIGHT);
button.setLayoutParams(params);
```
