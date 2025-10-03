<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [Interpolatorの種類](#interpolator種類)

<!-- /TOC -->


# Interpolatorの種類

ここに記載したInterpolatorは、`android.view.animation`パッケージに含まれるInterpolatorであり、  
PropertyAnimationSystemでも、ViewAnimationSystemでも使用可能である。

- @android:anim/accelerate_decelerate_interpolator
  - 中間地点までだんだん加速し、その後だんだん減速する
- @android:anim/accelerate_interpolator
  - だんだん加速する
- @android:anim/anticipate_interpolator
  - 少し逆方向に進んでから順方向へ進む
- @android:anim/anticipate_overshoot_interpolator
  - 少し逆方向に進んでから順方向へ進み、終点を少し通り過ぎてから終点に戻る
- @android:anim/bounce_interpolator
  - 物を上から落としたようにだんだん加速し、終点ではじかれて、数回バウンドしてから終点で止まる
- @android:anim/cycle_interpolator
  - 三角関数のSinカーブの動きをする。sin(0°)～sin(360°)までの値をとる。
  - 具体的には、始点を0=sin(0°)、終点を1=sin(90°)として、"0" -> "1" -> "0" -> "-1" -> "0"という動きをする。
  - つまり、始点 -> 終点 -> 始点 -> 始点を軸として終点を反転した場所 -> 始点という動きをする。
- @android:anim/decelerate_interpolator
  - だんだん減速する
- @android:anim/linear_interpolator
  - 一定の速度で動く
- @android:anim/overshoot_interpolator
  - だんだん減速しつつ、終点を少し通り過ぎてから終点に戻る
- @android:anim/path_interpolator
  - 自分で定義したInterpolatorに従って移動する。
  - Pathオブジェクトの線でInterpolatorを定義する。
    - X軸は時間の経過を表し、Y軸はその時間における移動距離(の割合)を表す。
    - Pathオブジェクトは座標(0,0)で始まり(1,1)で終わるようにする必要がある。
    - PathはX軸方向の値は連続的にする必要があり、Y軸方向の値は連続していなくても良い。
