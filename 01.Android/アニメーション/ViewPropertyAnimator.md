<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [ViewPropertyAnimator](#viewpropertyanimator)
	- [回転](#回転)
	- [移動](#移動)
		- [注意点](#注意点)
	- [共通事項](#共通事項)

<!-- /TOC -->


# ViewPropertyAnimator

## 概要

ViewPropertyAnimatorは、Viewのプロパティを変更することでアニメーションを実施します。

```java
// これはViewPropertyAnimatorを返します。
View.animate();
// これはViewPropertyAnimatorを返します。
View.animate().x(100f);
```


## 回転

**Sample.java**

```Java
// ”Viewの初期状態”から”時計回り”に45度回転
View.animate().rotation(getResources().getInteger(R.integer.rotation_45);
// ”Viewの初期状態”から”反時計回り”に45度回転
View.animate().rotation(getResources().getInteger(R.integer.rotation_r45);
// ”現在の状態”から”時計回り”に45度回転
View.animate().rotationBy(45);
// ”Viewの初期状態”から”画面上に座標平面を描いたとき、そのX軸を回転軸として”に45度”奥へ”回転
View.animate().rotationX(45);
// ”現在の状態”から”画面上に座標平面を描いたとき、そのX軸を回転軸として”に45度”手前へ”回転
View.animate().rotationXBy(-45);
// ”Viewの初期状態”から”画面上に座標平面を描いたとき、そのY軸を回転軸として”に45度”左が手前へ、右が奥へ”回転
View.animate().rotationY(45);
// ”現在の状態”から”画面上に座標平面を描いたとき、そのY軸を回転軸として”に45度”右が手前へ、左が奥へ”回転
View.animate().rotationYBy(-45);
```

`”現在の状態”から`の場合は、現在アニメーションを実行中の場合は、その実行途中の状態を”現在の状態”と捉えて、そこから新たに指定された分だけ状態が変化する。


**integers.xml**

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <!--  回転角度  -->
		<integer name="rotation_45">45</integer>
		<integer name="rotation_r45">-45</integer>
</resources>
```


## 移動

**Sample.java**

```java
// ”Viewの初期位置”から"右方向へ"300ピクセル移動
View.animate().translationX(300);
// ”Viewの初期位置”から"左方向へ"300ピクセル移動
View.animate().translationX(-300);
// ”現在の位置”から"下方向へ"300ピクセル移動
View.animate().translationY(300);
// ”現在の位置”から"レイヤーの上層へ"300ピクセル分移動
// Z軸方向への移動は、View同士の重なりが想定通りにいかない場合に使用する
View.animate().translationZ(300);
// ”現在の位置”から"レイヤーの下層へ"300ピクセル分移動
View.animate().translationZ(-300);

// translationメソッドのパラメータの単位はピクセル[px]であるが、
// ピクセルだとデバイスごとに画面密度が異なるため、密度非依存ピクセル[dp]で渡したい。
// よって、dpを渡しつつ、dpをpxに変換する計算を以下のように入れる。
float scale = getResources().getDisplayMetrics().density;
View.animate().translationX(dp_value * scale);
```


### 注意点

ViewにPaddingやMarginが設定されている場合は、移動距離が少し長く見える可能性がある。しかし、それは最初から位置がずれている可能性が高く、想定通りの位置に移動しない場合は、最初の位置にずれがないか確認すると良い。


## 共通事項

- アニメーションで動いたViewは、同じViewGroupを親に持つView同士が重なった場合には、上に重なるのか下に重なるのか？
	- アニメーションに限らず、`layout.xml`ファイルで上に書いた方がレイヤーの下になり、下に書いた方がレイヤーの上になる。
	- ただし、FloatingActionButtonは特別で、常にFABがレイヤーの上層にくるので注意が必要。
	- また、`<Button>`なのか`<View>`なのかなど、どのウィジェットを使用するかによってもレイヤーの上下関係が変わるようなので注意が必要。（ButtonとViewだとxmlファイル内の記述順によらずButtonの方が上層にくる。）
	- レイヤーの上下関係が想定と異なる場合には、Z軸方向のtranslationアニメーションを使用することで、上下関係を入れ替えることができる。

（例）
```xml
<androidx.appcompat.widget.LinearLayoutCompat xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    tools:context=".MainActivity">
    <!-- xmlファイル内で上に書かれているので、レイヤー内で下層になる -->
    <View
        android:id="@+id/under_layer"
        android:layout_width="300px"
        android:layout_height="300px"
        android:background="@color/colorPrimary" />

    <!-- xmlファイル内で下に書かれているので、レイヤー内で上層になる -->
    <View
        android:id="@+id/over_layer"
        android:layout_width="300px"
        android:layout_height="300px"
        android:background="@color/colorAccent" />
</androidx.appcompat.widget.LinearLayoutCompat>
```


## 使いどころ

複数のアニメーションを同時に実行する場合は、`ValuePropertyAnimator`を使用すると簡潔に実装することができます。

以下にその他の実装方法との比較を載せます。すべて同じアニメーションを行います。

**ValuePropertyAnimatorを使用した場合**

```java
myView.animate().x(50f).y(100f);
```


**PropertyValuesHolderを使用した場合**

```java
PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("x", 50f);
PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("y", 100f);
ObjectAnimator.ofPropertyValuesHolder(myView, pvhX, pvhY).start();
```


**AnimatorSetを使用した場合**

```java
ObjectAnimator animX = ObjectAnimator.ofFloat(myView, "x", 50f);
ObjectAnimator animY = ObjectAnimator.ofFloat(myView, "y", 100f);
AnimatorSet animSetXY = new AnimatorSet();
animSetXY.playTogether(animX, animY);
animSetXY.start();
```
