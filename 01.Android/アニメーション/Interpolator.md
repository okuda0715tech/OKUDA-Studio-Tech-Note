<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [Interpolator](#interpolator)
	- [概要](#概要)
	- [独自のインターポレーターを作成する場合](#独自作成場合)

<!-- /TOC -->


# Interpolator

## 概要

インターポレーターとは、アニメーションで１フレームごとに進む距離を計算するアルゴリズムのことである。  
例えば`LinearInterpolator`は、各フレームで進む距離を一定になるように計算するアルゴリズムであり、  
また、`AccelerateDecelerateInterpolator`は、加速、原則を含む計算アルゴリズムである。


## 独自のインターポレーターを作成する場合

`TimeInterpolator`インターフェースを実装することで、独自のインターポレーターを作成することができます。  
`TimeInterpolator`では、`getInterpolation()`メソッドをオーバーライドします。  
例えば、以下は、`AccelerateDecelerateInterpolator`クラスのインターポレータの例です。

```Java
// 引数は、アニメーションの現在の進行状況を表します。0が開始した瞬間、1が終了する瞬間です。
// 返値は、アニメーションで動かすViewの現在の場所を表す係数です。
// 開始地点よりも前に戻したい場合はマイナスになる可能性があります。
// 逆に、終了地点よりも後ろに進めたい場合は１より大きくなる可能性があります。
@Override
public float getInterpolation(float input) {
    return (float)(Math.cos((input + 1) * Math.PI) / 2.0f) + 0.5f;
}
```
