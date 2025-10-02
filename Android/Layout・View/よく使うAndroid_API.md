<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [よく使うAndroid API](#よく使うandroid-api)
	- [パッケージ「androidx.appcompat.app.AppCompatActivity.java」](#パッケージandroidxappcompatappappcompatactivityjava)
		- [addContentViewメソッド](#addcontentviewメソッド)
	- [パッケージ「android.view.LayoutInflater.java」](#パッケージandroidviewlayoutinflaterjava)
		- [inflateメソッド](#inflateメソッド)
	- [パッケージ「android.view.ViewGroup.java」](#パッケージandroidviewviewgroupjava)
		- [メソッド](#メソッド)
			- [addViewメソッド](#addviewメソッド)
			- [indexOfChildメソッド](#indexofchildメソッド)
			- [getChildCountメソッド](#getchildcountメソッド)
			- [getChildAtメソッド](#getchildatメソッド)
			- [removeViewメソッド](#removeviewメソッド)
			- [removeViewAtメソッド](#removeviewatメソッド)
			- [removeViewsメソッド](#removeviewsメソッド)
			- [removeAllViewsメソッド](#removeallviewsメソッド)
		- [コールバックインターフェース](#コールバックインターフェース)
			- [OnHierarchyChangeListenerインターフェース](#onhierarchychangelistenerインターフェース)
	- [パッケージ「android/view/View.java」](#パッケージandroidviewviewjava)
		- [getParentメソッド](#getparentメソッド)

<!-- /TOC -->

# よく使うAndroid API

## パッケージ「androidx.appcompat.app.AppCompatActivity.java」

### addContentViewメソッド

```Java
void addContentView(View view, ViewGroup.LayoutParams params)
```

パラメータ | 説明                                 | 例
-----------|--------------------------------------|---
view       | Activityのルートviewに追加したいView | 略
params     | 追加したいViewのパラメータ           | 略

通常、ActivityのルートViewを設定する場合には、setContentViewメソッドが使われる。
そのメソッドで設定したViewの親Viewには、実はFrameLayoutでContentViewという名前のViewが存在している。

このaddContentViewメソッドを使うと、そのFrameLayoutの中にViewを追加することができる。

ボトムタブで切り替えを行うアプリなどで使ってもいいかも？しれない。


## パッケージ「android.view.LayoutInflater.java」

### inflateメソッド

```Java
View inflate(@LayoutRes int resource, @Nullable ViewGroup root, boolean attachToRoot)
```

パラメータ   | 説明                                             | 例
-------------|--------------------------------------------------|---------------------------
resource     | xmlファイルのリソースID                          | R.layout.activity_main.xml
root         | xmlファイルの親としてセットするViewGroup         |
attachToRoot | true:rootを親としてくっつける/false:くっつけない |


## パッケージ「android.view.ViewGroup.java」

### メソッド

#### addViewメソッド

引数に指定したViewを親Viewの一番後ろに追加する。
childにLayoutParameterが指定されていない場合は、デフォルトで width = wrap_content, height = wrap_content が指定される。

```Java
void addView(View child)
```


引数に指定したViewを親Viewのindex番目の要素とindex+1番目の要素の間に追加する。

```Java
void addView(View child, int index)
```


引数に指定したViewを親Viewの一番後ろに追加する。
widthとheightの単位はピクセルです。

```Java
void addView(View child, int width, int height)
```


引数に指定したViewを親Viewのindex番目の要素とindex+1番目の要素の間に追加する。
引数に指定したLayoutParamsが子Viewに適用されます。

```Java
void addView(View child, int index, LayoutParams params)
```


引数に指定したViewを親Viewの一番後ろに追加する。
引数に指定したLayoutParamsが子Viewに適用されます。

```java
void addView(View child, LayoutParams params)
```

パラメータ | 説明                         | 例
-----------|------------------------------|--------------------------------------
child      | 付け加えるView               |
params     | 付け加えるViewのLayoutParams | new LinearLayout.LayoutParams(WC, WC)

このメソッドは、ViewGroupが抽象クラスであるため、それを継承したLinearLayoutクラスなどから呼ばれます。
第二引数のparamsには、呼び出し元のインスタンスに合わせたLayoutParamsをセットします。例えば、呼び出し元がLinearLayoutなら、new LinearLayout.LayoutParamsとし、RelativeLayoutなら、new RelativeLayout.LayoutParamsとします。


#### indexOfChildメソッド

引数で指定された子Viewが親Viewの中で何番目かを返す。

```Java
int indexOfChild(View child)
```


#### getChildCountメソッド

ViewGroupの子Viewの要素数を返す。

```Java
int getChildCount()
```

あるViewGroupの子Viewをfor文で順番に処理したい時にfor文の実行上限回数の指定に使用する。

#### getChildAtメソッド

指定した位置の子Viewを取得する。
パラメータは0から始まる整数である。

```Java
View getChildAt(int index)
```


#### removeViewメソッド

引数で指定された子ウィジェットを削除する

```Java
void removeView(View view)
```


#### removeViewAtメソッド

引数indexで指定された位置の子ウィジェットを削除する。

```Java
void removeViewAt(int index)
```


#### removeViewsメソッド

引数startで指定されたインデックス位置の子ウィジェットから、count個分のウィジェットを削除する。

```Java
void removeViews(int start, int count)
```


#### removeAllViewsメソッド

すべての子ウィジェットを削除する。

```Java
void removeAllViews()
```


### コールバックインターフェース

#### OnHierarchyChangeListenerインターフェース

子Viewが親Viewに追加されたら呼ばれるコールバックメソッド

```Java
void onChildViewAdded(View parent, View child)
```

子Viewが親Viewから削除されたら呼ばれるコールバックメソッド

```Java
void onChildViewRemoved(View parent, View child)
```

上記コールバックを受け取るリスナーをセットするメソッド

```Java
void setOnHierarchyChangeListener(OnHierarchyChangeListener listener)
```

## パッケージ「android/view/View.java」

### getParentメソッド

親Viewを取得する。

```Java
ViewParent getParent()
```





