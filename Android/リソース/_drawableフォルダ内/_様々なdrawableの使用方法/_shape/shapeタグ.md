<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [shapeタグ](#shapeタグ)
  - [概要](#概要)
  - [属性の解説](#属性の解説)
  - [サンプル](#サンプル)
    - [長方形の枠線(実線)のみ](#長方形の枠線実線のみ)
    - [長方形の枠線(点線)のみ](#長方形の枠線点線のみ)
    - [長方形の枠線(背景色あり)](#長方形の枠線背景色あり)
    - [長方形の枠線(角丸)](#長方形の枠線角丸)
    - [塗りつぶしのグラデーション(線形グラデーション)](#塗りつぶしのグラデーション線形グラデーション)
    - [塗りつぶしのグラデーション(放射状グラデーション)](#塗りつぶしのグラデーション放射状グラデーション)
    - [塗りつぶしのグラデーション(円形グラデーション)](#塗りつぶしのグラデーション円形グラデーション)
    - [円](#円)

<!-- /TOC -->


# shapeタグ

## 概要

shapeタグでは、長方形、正方形、直線、円、楕円の図形をxmlで定義することができます。

さらにshapeタグ内部で、strokeタグやsolidタグなどを使用して、図形の枠線、塗り潰し、塗り潰しのグラデーション、枠線の角を丸くするなどの見た目の装飾を行うことができます。

drawble配下に装飾用の定義を記述したxmlファイルを用意しておき、Viewの`android:background="@drawble/xxx"`に指定することで、Viewのデコレーションを行うことができます。


## 属性の解説

```xml
<?xml version="1.0" encoding="utf-8"?>
<!-- shape タグは図形の形を指定
デフォルトの形は長方形 (rectangle) となります。
形が円 (ring) の場合のみ使用可能な属性があるため、それについては後述します。 -->
<shape
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape=["rectangle" | "oval" | "line" | "ring"] >
    <!-- corners タグは長方形のシェイプの場合のみ有効なタグで、角の丸みを指定します。
    radius：角丸の半径。
    四隅をそれぞれ指定したい場合は topLeft～ や BottomRight～ などの属性を使用する。 -->
    <corners
        android:radius="integer"
        android:topLeftRadius="integer"
        android:topRightRadius="integer"
        android:bottomLeftRadius="integer"
        android:bottomRightRadius="integer" />
    <!-- gradient タグは塗りつぶしのグラデーションの色の指定
    startColor：グラデーション開始地点の色
    endColor：グラデーション終了地点の色
    type：グラデーションの種類
    その他の属性についてはサンプルのグラデーションの項目を参照してください。 -->
    <gradient
        android:angle="integer"
        android:centerX="float"
        android:centerY="float"
        android:centerColor="integer"
        android:endColor="color"
        android:gradientRadius="integer"
        android:startColor="color"
        android:type=["linear" | "radial" | "sweep"]
        android:useLevel=["true" | "false"] />
    <!-- padding タグは、このシェイプを使用している View 自体の padding に適用される -->
    <padding
        android:left="integer"
        android:top="integer"
        android:right="integer"
        android:bottom="integer" />
    <!-- size タグは図形の幅と高さ -->
    <size
        android:width="integer"
        android:height="integer" />
    <!-- solid タグは図形の塗りつぶしの色 -->
    <solid
        android:color="color" />
    <!-- stroke タグは枠線の指定
    width：線の太さ
    color：線の色
    dashWidth：一本の線の長さ
    dashGap：線と線の間の長さ -->
    <stroke
        android:width="integer"
        android:color="color"
        android:dashWidth="integer"
        android:dashGap="integer" />
</shape>
```



## サンプル

### 長方形の枠線(実線)のみ

```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android">
    <stroke
        android:width="@dimen/border_thickness_3dp"
        android:color="@color/colorGray9"/>
</shape>
```


### 長方形の枠線(点線)のみ

```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android">
    <stroke
        android:width="@dimen/border_thickness_3dp"
        android:color="@color/colorGray9"
        android:dashGap="@dimen/interval_5dp"
        android:dashWidth="@dimen/interval_10dp"/>
</shape>
```


### 長方形の枠線(背景色あり)

```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android">
    <stroke
        android:width="@dimen/border_thickness_3dp"
        android:color="@color/colorGray9" />
    <solid android:color="@color/colorWhite" />
</shape>
```


### 長方形の枠線(角丸)

```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android">
    <stroke
        android:width="@dimen/border_thickness_3dp"
        android:color="@color/colorGray9" />
    <corners android:radius="@dimen/corner_radius_5dp" />
</shape>
```


### 塗りつぶしのグラデーション(線形グラデーション)

```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android">
    <gradient
        android:startColor="@color/colorPaleBlue"
        android:endColor="@color/colorRed"
        android:centerColor="@color/colorDeepBlue"
        android:angle="@integer/rotation_45"
        android:type="linear"/>
</shape>
```

- centerColor
  - 開始地点と終了地点の中間地点の色(指定してもしなくても良い)  
- angle
  - グラデーションの進行方向( 0 なら左から右へ / 45 なら左下から右上へ / 90 なら下から上へ)  
- type
  - 線形グラデーションを使用する場合は `linear` を指定する。


### 塗りつぶしのグラデーション(放射状グラデーション)


```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android">
    <stroke
        android:width="@dimen/border_thickness_3dp"
        android:color="@color/colorGray9" />
    <gradient
        android:endColor="@color/colorRed"
        android:startColor="@color/colorPaleBlue"
        android:type="radial"
        android:gradientRadius="100dp"
        android:centerX="50%"
        android:centerY="0.5"/>
</shape>
```

- type
  - 放射状グラデーションを使用する場合は `radial` を指定する。
- gradientRadius
  - グラデーションの開始地点から終了地点までの半径
  - dp 指定 / % 指定 / 100 パーセントを 1 とした小数指定、などいろいろな指定が可能
  - 放射状グラデーションの場合のみ有効な属性
- centerX
  - グラデーションの開始地点を横方向でどこにするかの指定
  - % 指定 / 100 パーセントを 1 とした小数指定が可能
  - 幅の左端を 0 % 、右端を 100 % と見なす。
- centerY
  - グラデーションの開始地点を縦方向でどこにするかの指定
  - % 指定 / 100 パーセントを 1 とした小数指定が可能
  - 高さの上端を 0 % 、下端を 100 % と見なす。


### 塗りつぶしのグラデーション(円形グラデーション)

クレープ屋さんがクレープ生地をハケで伸ばす時に鉄板の上をぐるりと一周回すようにグラデーションを作るのがこの「円形グラデーション」である。

ただし、開始地点と終了地点付近の色の差が出過ぎていて、あまり綺麗ではないので、使用する機会は少ないかもしれない。

```xml
<gradient
    android:endColor="@color/colorDeepBlue"
    android:startColor="@color/colorPaleBlue"
    android:type="sweep"
    android:centerX="100%"
    android:centerY="50%"/>
```

- type
  - 円形グラデーションを使用する場合は `sweep` を指定する。
- centerX
  - 放射状グラデーションの場合と同じ
- centerY
  - 放射状グラデーションの場合と同じ


### 円

```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:innerRadius="20dp"
    android:shape="ring"
    android:thickness="20dp"
    android:useLevel="false">
    <size
        android:width="200dp"
        android:height="200dp" />
    <gradient
        android:endColor="@color/colorDeepBlue"
        android:startColor="@color/colorPaleBlue" />
</shape>
```

**円の場合にのみ指定できるshapeタグの属性**

- innerRadius
  - 円の中心から内側の円までの幅(単位 dp)
- thickness
  - 内側の円と外側の円の幅 (単位 dp)
- useLevel
  - よくわからないが必須パラメータであり、常にfalseを指定する。そうでないと正しく円が表示されない。
- thicknessRatio
  - 「図形の描画領域の横幅」を使用して、「内側の円と外側の円の幅」を算出するために使用する比率である。
  - 「内側の円と外側の円の幅」を何倍したら、「図形の描画領域の横幅」になるかを示す値。式で表すと以下のようになる。円の横幅ではなく、描画領域の横幅なので注意！
  - `図形の描画領域の横幅 ÷ thicknessRatio = 内側の円と外側の円の幅`
  - 以下の例の場合、thicknessRatioが3なので、描画領域(`android:background="@drawble/xxx"`指定しているView)の横幅を3で割った値が「内側の円と外側の円の幅」となる。

```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="ring"
    android:innerRadius="2dp"
    android:thicknessRatio="3"
    android:useLevel="false">
    <gradient
        android:endColor="@color/colorDeepBlue"
        android:startColor="@color/colorPaleBlue" />
</shape>
```


- innerRadiusRatio
  - 「内側の円と外側の円の幅」を使用して、「内側の円の半径」を算出するために使用する比率である。
  - 「内側の円と外側の円の幅」を何倍したら、「内側の円の半径」の長さになるかを示す値。式で表すと以下のようになる。
  - `内側の円と外側の円の幅 × innerRadiusRatio = 内側の円の半径`
  - 以下の例の場合は、リングの幅が40dpで、innerRadiusRatioが3であるため、内側の円の半径が120dpとなる。

```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:innerRadiusRatio="3"
    android:shape="ring"
    android:thickness="40dp"
    android:useLevel="false">
    <gradient
        android:endColor="@color/colorDeepBlue"
        android:startColor="@color/colorPaleBlue" />
</shape>
```


