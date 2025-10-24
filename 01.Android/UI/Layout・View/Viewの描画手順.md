<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [Viewの描画手順](#viewの描画手順)
	- [1. Viewのサイズ決定（onMeasure）](#1-viewのサイズ決定onmeasure)
		- [モードについて](#モードについて)
		- [自分に子Viewが存在しない場合](#自分に子viewが存在しない場合)
		- [サイズ確定後のサイズを取得する方法](#サイズ確定後のサイズを取得する方法)

<!-- /TOC -->


# Viewの描画手順

- ViewGroup
  - ViewGroupは自身の全ての子Viewに対し、子自身のViewのサイズを計測するようにリクエストします。
    - ViewGroup(のsuperクラスのView)のmeasureメソッド内で子ViewのonMeasureメソッドを呼び出します。
  - 全ての子Viewのサイズを計測したら、それらを配置する責務があります。
    -
  - ViewGroupは自身の全ての子Viewに対し、子自身のViewを描画するようにリクエストします。
    - ViewGroupのdrawメソッド内で子ViewのonDrawメソッドを呼び出します。
- View(ViewGroupも含む)
  - Viewは、自身のViewを描画する責務を持ちます。
    - onDrawメソッドで、自身をどのように描画するかを定義する必要があります。
  - measureメソッドがリターンした時点でそのViewとその全ての子孫Viewのサイズは決定しています。
  - ただし、1回目のmeasureメソッドではサイズが決まらず、2回目のmeasureメソッドで決まる場合があります。
  - それは、1回目は、親Viewが制約を与えずに子Viewのサイズを測定したけれど、子Viewのサイズの合計が大きすぎたり小さすぎる場合です。
  - その場合は、2回目は、親Viewが制約を与えて子Viewのサイズを計測しなおします。


1. 親Viewは子Viewの`measure`を呼ぶ
2. 子Viewは自身の`measure`メソッドから、自身の`onMeasure`を呼ぶ
3. 子Viewは自身の幅・高さを決定する。（setMeasuredDimension or super.onMeasure）
4.

1. Viewのサイズ決定（onMeasure）
2. Viewの配置場所決定（onLayout）
3. Viewの描画（onDraw）

## 1. Viewのサイズ決定（onMeasure）

**Viewのサイズ決定手順**

1. 親Viewの`onMeasure(int widthMeasureSpec, int heightMeasureSpec)`が呼ばれる
  1. パラメータ`MeasureSpec`には、レイアウトxmlファイルに記述された親Viewと自分自身のViewのサイズ指定を参考に、推定サイズとモードが渡されてくる。
  2. モードとは、そのサイズが変更可能なのか、不可能なのか、許容最大サイズなのかを判別する区分である。
  3. パラメータ`MeasureSpec`はint型（32ビットの整数）であり、前2ビットがモード、後ろ30ビットがサイズ（単位：px）である
  4. モードの取得は`MeasureSpec.getMode(widthMeasureSpec)`、サイズの取得は`MeasureSpec.getSize(widthMeasureSpec)`で行う。
2. `onMeasure`メソッドは、自分自身のViewのサイズを算出し、確定させるメソッドである。
3. 子Viewが存在する場合は、`onMeasure`メソッド内で、`super.onMeasure`を呼ぶ必要がある。
4. `super.onMeasure`を呼ぶと、子の`onMeasure`メソッドが呼ばれる。
5. 子の`onMeasure`メソッドが終わると、同期で親の`super.onMeasure`が終わり、その時点で子のサイズが確定しており、それに連動して、親のサイズが確定した状態になっている。
4. 子Viewが存在しない場合は、`onMeasure`メソッド内で、`super.onMeasure`を呼ばずに、`setMeasuredDimension(int widthMeasureSpec, int heightMeasureSpec)`メソッドを呼んでもOK。
5. Viewのサイズをプログラムで動的に決めたい場合は、`onMeasure`メソッド内で自分の指定したいサイズを算出し、`super.onMeasure`メソッド、または、`setMeasuredDimension`メソッドで確定させる。
6. 子Viewを持つ親Viewの`super.onMeasure`メソッドが返って来たとき、親Viewのサイズは確定しており、`getMeasuredWidth()` or `getMeasuredHeight()`で取得することができる。


### モードについて

**モードには以下の三種類が存在します。**

- EXACTLY(int値:1073741824)
  - きっちりそのサイズでなければいけない。
- AT_MOST(int値:-2147483648)
  - 最大でもそのサイズまでしか大きくしてはいけない。
  - 子View同士では折り合いがつかず、親Viewの介入が必要な場合に渡される値です。
- UNSPECIFIED(int値:0)
  - どんな値でも良い

モードはサイズに対する要件ですが、必ずしもその要件を守らなくても表示可能であることがあります。つまり、onMeasureメソッド内で要件を無視したサイズを自分で指定しても表示可能であることがあります。


**レイアウトxmlでどのような指定をしたらどのようなモードが渡されるのかを以下に記載します。（サンプルとして少しだけ）**

親Viewの指定 | 自分自身のViewの指定 | 子Viewの指定 | 自分に渡されるモード | 自分に渡されるサイズ
-------------|----------------------|--------------|----------------------|------------------------
wrap_content | 固定値               | 子Viewなし   | EXACTLY              | 固定値
wrap_content | layout_weight指定    | 子Viewなし   | AT_MOST              | 親のMeasureSpecのサイズ


### 自分に子Viewが存在しない場合

自分に子Viewが存在する場合は、自分の`onMeasure`メソッド内で、`super.onMeasure`を呼べば、子Viewのサイズが取得できるため、そのサイズに合わせて自分自身のサイズが決まることが多い。

一方、自分に子Viewが存在しない場合は、自分の`onMeasure`メソッド内で、サイズを指定すれば、そのサイズになるし、指定しなければ`onMeasure`のパラメータで渡されてきたサイズになることが多い。


### サイズ確定後のサイズを取得する方法

`onMeasure`メソッド内で、`super.onMeasure`や`setMeasuredDimension`メソッドを実行したあとならば、サイズが確定しており、`View.getMeasuredWidth()`、`getMeasuredHeight()`メソッドで、確定値を取得することができます。

`getWidth()`、`getHeight()`メソッドは、Viewの描画が完了するまで正しい値が取得できないため、`onMeasure`メソッド内では使用できません。



