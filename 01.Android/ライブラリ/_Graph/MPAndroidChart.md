<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [MPAndroidChart](#mpandroidchart)
	- [参考にしたサイト](#参考にしたサイト)
	- [build.gradleの設定](#buildgradleの設定)
	- [よく使うAPI](#よく使うapi)
		- [グラフ描画エリアの背景色の有無の設定](#グラフ描画エリアの背景色の有無の設定)
		- [グラフ描画エリアの背景色の変更](#グラフ描画エリアの背景色の変更)
		- [グラフ右下に詳細コメントを記載する。](#グラフ右下に詳細コメントを記載する)
		- [グラフ表示時に点を一つずつ表示するアニメーションを行う。](#グラフ表示時に点を一つずつ表示するアニメーションを行う)
		- [グラフを更新するには invalidate メソッドを呼びます。](#グラフを更新するには-invalidate-メソッドを呼びます)
		- [軸に直行する補助線のメモリが小数点になってしまうのを防ぐ方法（補助線の最小間隔を設定する方法）](#軸に直行する補助線のメモリが小数点になってしまうのを防ぐ方法補助線の最小間隔を設定する方法)
		- [軸の最大値をプロットの最大値に連動させている場合に、プロットの最大値よりも軸の最大値を大きくしたい場合、どれだけ大きくするかの設定](#軸の最大値をプロットの最大値に連動させている場合にプロットの最大値よりも軸の最大値を大きくしたい場合どれだけ大きくするかの設定)
		- [軸の最大値と最小値の設定](#軸の最大値と最小値の設定)
		- [ピンチイン・ピンチアウトの可否の設定](#ピンチインピンチアウトの可否の設定)
		- [グラフにデータがセットされていない場合に表示される文言の設定](#グラフにデータがセットされていない場合に表示される文言の設定)
		- [凡例](#凡例)
			- [凡例の文字列が画面からはみ出したら次の凡例は改行して表示する方法](#凡例の文字列が画面からはみ出したら次の凡例は改行して表示する方法)

<!-- /TOC -->

# MPAndroidChart

## 参考にしたサイト

- 公式サイト
  - Github
    - [MPAndroidChart - Github](https://github.com/PhilJay/MPAndroidChart)
  - Javadoc
    - [MPChartLib v3.1.0 API - Github](https://javadoc.jitpack.io/com/github/PhilJay/MPAndroidChart/v3.1.0/javadoc/)
  - 機能からコードを調べる
    - [MPAndroidChart Documentation - Weeklycoding](https://weeklycoding.com/mpandroidchart-documentation/)
  - 一般開発者のサンプルプロジェクト（Githubリンク付き）
    - [【Android】Androidでグラフを描画する - 線グラフ編 - Qiita](https://qiita.com/entan05/items/a21906f8c71b5c208f07)
      - ソースコードにはパラメータの説明が色々と書いてあるので参考になる。


## build.gradleの設定

**プロジェクトレベルのbuild.gradle**

```Java
repositories {
    maven { url 'https://jitpack.io' }
}
```

**アプリレベルのbuild.gradle**

```Java
dependencies {
    implementation 'com.github.PhilJay:MPAndroidChart:v3.1.0'
}
```


## よく使うAPI

### グラフ描画エリアの背景色の有無の設定

**Sample.java**

```Java
private LineChart mChart;
mChart.setDrawGridBackground(true);
```

X軸とY軸を長辺と短辺とする長方形部分の背景色の設定。
trueで背景色あり、falseでなし。

### グラフ描画エリアの背景色の変更

**Definition.java**

```Java
void setGridBackgroundColor(int color)
```

### グラフ右下に詳細コメントを記載する。

**Sample.java**

```Java
private LineChart mChart;
mChart.getDescription().setEnabled(false);
```

trueでコメントを表示、falseで非表示になる。
setEnabledメソッドは、コメント以外の様々な部品に対して使える表示/非表示切り替え設定だと思われる。

### グラフ表示時に点を一つずつ表示するアニメーションを行う。

**Sample.java**

```Java
private LineChart mChart;
mChart.animateX(2500);
```

animateXのパラメータはアニメーション開始から終了までに要する時間(単位：ミリ秒)
アニメーションが不要な場合は、animateXメソッドを削除するか、時間を0ミリ秒にする。

### グラフを更新するには invalidate メソッドを呼びます。

グラフにデータをセットしただけでは、グラフは再描画されません。
何もしていない場合は、データのセット後にグラフのViewをタップすると再描画されます。
ただし、それではUXが悪いので、アプリで invalidate メソッドを呼びます。
そうすることでタップせずともグラフが再描画されます。

**Sample.java**

```Java
private LineChart mChart;
mChart.invalidate();
```


### 軸に直行する補助線のメモリが小数点になってしまうのを防ぐ方法（補助線の最小間隔を設定する方法）

**Sample.java**

以下の設定をすることで、軸に直行する補助線の最小間隔を指定します。
設定値のおすすめは「1」にすること。
1ならば、小数になることがないし、プロットの数が3個程度などで少ない場合にも、補助線が3本ほど表示されて、いい感じになる為。

```Java
private LineChart mChart;
XAxis xAxis = mChart.getXAxis();
// X軸に直行する補助線の最小間隔を設定
xAxis.setGranularity(1.0f);
// setGranularityの有効無効切り替え
xAxis.setGranularityEnabled(true);
```

グラフに表示するプロット数が少ない場合、軸に直行する補助線のメモリが小数点になってしまうことがあります。
その場合は、上記のように補助線の最小間隔を設定します。
そうすれば、どんなにプロット数の少ないグラフでも、設定した間隔未満の補助線が引かれることはなく、その為、補助線に小数が発生することも無くなります。
なお、この設定は、最小間隔の設定であり、常に設定した間隔になる訳ではありません。したがって、プロット数が多いグラフになった場合に、補助線が多くなりすぎることがなく、見やすさをキープすることができます。


### 軸の最大値をプロットの最大値に連動させている場合に、プロットの最大値よりも軸の最大値を大きくしたい場合、どれだけ大きくするかの設定

**Sample.java**

```Java
private LineChart mChart;
XAxis xAxis = mChart.getXAxis();
xAxis.setSpaceMax(10f);
```

### 軸の最大値と最小値の設定

**Sample.java**

```Java
private LineChart mChart;
XAxis xAxis = mChart.getXAxis();
xAxis.setAxisMaximum(50f);
xAxis.setAxisMinimum(0f);
```

### ピンチイン・ピンチアウトの可否の設定

**Sample.java**

```Java
private LineChart mChart;
// X軸・Y軸の両方向のズーム可否
mChart.setScaleEnabled(false);
// X軸方向のズーム可否
mChart.setScaleXEnabled(false);
// Y軸方向のズーム可否
mChart.setScaleYEnabled(false);
```


### グラフにデータがセットされていない場合に表示される文言の設定

**Sample.java**

```Java
private LineChart mChart;
mChart.setNoDataText("ここにグラフが表示されます。");
```


### 凡例

#### 凡例の文字列が画面からはみ出したら次の凡例は改行して表示する方法

**Sample.java**

```Java
Legend legend = chart.getLegend();
legend.setWordWrapEnabled(true);
```



