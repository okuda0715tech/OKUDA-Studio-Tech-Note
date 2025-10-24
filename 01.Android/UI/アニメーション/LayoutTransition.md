<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [LayoutTransition](#layouttransition)
	- [概要](#概要)
	- [アニメーションが行われるようにする](#行)
	- [アニメーションをカスタマイズする方法](#方法)
		- [アニメーションのトリガーの種類](#種類)

<!-- /TOC -->


# LayoutTransition

## 概要

LayoutTransitionは、指定したViewGroup内にViewが動的に追加、削除、表示/非表示、サイズ変更されたときに、  
そのView及び、それに伴いLayout上で移動したViewについてアニメーションを設定することができる仕組みである。  
アニメーションの内容には、デフォルトのアニメーションが設定されている。


## アニメーションが行われるようにする

**Javaで動的に設定する**

```Java
ViewGroup.setLayoutTransition(new LayoutTransition());
```

**XMLで静的に設定する**

```xml
<ViewGroup
		android:animateLayoutChanges="true" />
```


## アニメーションをカスタマイズする方法

```Java
LayoutTransition transition = new LayoutTransition();
ObjectAnimator animation = ObjectAnimator.ofFloat(textView, "translationX", 100f);

// 【注意】アニメーションのDurationの設定方法について
//   アニメーションのDurationは、LayoutTransition.setDuration()を使用すること。
//   Animator.setDuration()は設定しても無視されるため意味がない。
//   transitionType別にDurationを設定したい場合は、
//   LayoutTransition.setDuration(int transitionType, long duration)を使用すること。

// 第一引数：どのイベンドが起きたときのアニメーションをカスタマイズしたいか
// 第二引数：アニメーションの定義
transition.setAnimator(LayoutTransition.DISAPPEARING, animation);
```

### アニメーションのトリガーの種類

- `LayoutTransition.APPEARING`
  - ViewGroupにaddされたViewに対するアニメーションを示すタグ/ラベル
  - ViewGroup内で非表示から表示状態になったViewに対するアニメーションを示すタグ/ラベル
- `LayoutTransition.DISAPPEARING`
  - ViewGroupからremoveされたViewに対するアニメーションを示すタグ/ラベル
  - ViewGroup内で表示状態から非表示になったViewに対するアニメーションを示すタグ/ラベル
- `LayoutTransition.CHANGE_APPEARING`
  - ViewGroupにViewがaddされたとき、そのaddされたView以外のViewのうち、位置が変わるViewに対するアニメーションを示すタグ/ラベル
- `LayoutTransition.CHANGE_DISAPPEARING`
  - ViewGroupからViewがremoveされたとき、そのremoveされたView以外のViewのうち、位置が変わるViewに対するアニメーションを示すタグ/ラベル
- `LayoutTransition.CHANGING`
  - Viewのレイアウト変更（`onLayout()`による配置変更）が行われたViewに対するアニメーションを示すタグ/ラベル
  - デフォルトでは、このタグに対応するアニメーションは無効になっているため、`LayoutTransition.enableTransitionType(LayoutTransition.CHANGING);`メソッドを実行して、有効にする必要がある。
  - ただし、有効にした場合でも、他に現在実行中のアニメーションが存在する場合は、レイアウト変更が発生してもそのViewはアニメーションが行われない。
  - 主に「Viewのサイズ変更」、「Viewの位置変更」のアニメーションとして使用される。
  - `CHANGE_APPEARING / CHANGE_DISAPPEARING`と`CHANGING`は重複する場合があるため、想定通りに動かない場合は、複合的に検証する必要がある。
    - 例えば、`CHANGE_APPEARING`、`CHANGE_DISAPPEARING`を無効にしても、`CHANGING`が有効になっている場合は、Viewをaddやremoveしたときに周辺のViewがアニメーションされる可能性がある。
