<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [Drawables概要](#drawables概要)
	- [Drawablesを定義しインスタンス化する方法](#drawablesを定義しインスタンス化する方法)
	- [リソースファイルからDrawablesを生成する](#リソースファイルからdrawablesを生成する)
		- [Drawableオブジェクトを生成する方法](#drawableオブジェクトを生成する方法)
		- [res/drawable/フォルダは圧縮がかかる](#resdrawableフォルダは圧縮がかかる)
	- [xmlファイルでDrawableを作成する](#xmlファイルでdrawableを作成する)

<!-- /TOC -->


# Drawables概要

アプリで静的な画像を使用したい場合は、Drawablesクラスまたはそのサブクラスを使用することで実現できます。
`Drawables`とは、描画されるものの抽象的な存在です。


## Drawablesを定義しインスタンス化する方法

Drawablesを定義しインスタンス化する方法は以下の三つがあります。

1. コンストラクタで行う（普通に「new」でインスタンス化する）
2. プロジェクト内に保存されたビットマップ画像ファイル(jpgやpngなど)をインフレートする
3. drawables属性を定義したxmlファイルをインフレートする


## リソースファイルからDrawablesを生成する

リソースファイルとして使用可能な拡張子は以下の通りです。

- PNG (推奨)
- JPG (推奨でも非推奨でもない)
- GIF (非推奨)

リソースファイルは`res/drawable/`フォルダに格納します。
リソースファイルは、xmlからでもjavaソースコードからでも参照できます。


### PNGが推奨の理由

アプリで使用する画像は、一般的に単一色での塗り潰し部分が多い。
そのような画像をJPGで保存すると、モスキートノイズと呼ばれるにじみが生じやすい。
そのため、ぼやっとした画像になりがちである。

一方、PNGでは、そのような画像はくっきりと表示されるため、アプリで使用する画像は、PNG形式が一般的である。

ただし、写真のような様々な色合い、濃淡、明暗で描かれた画像はJPGの方が良いです。

上記のような違いが生じるのは、PNGとJPGで画像の圧縮方法が異なるためである。
画像を圧縮するとは、どういうことかというと、
画像は基本的には「１ピクセルごとにどの色を表示するか」をデータとして保持している。
しかし、きっちりと全てのピクセルに対してデータを保持するとデータ量が大きくなってしまうため、
画像の傾向などを利用してよりすくないデータ量で保存できるように工夫している。
それが、画像の圧縮という意味である。


### Drawableオブジェクトを生成する方法

```java
Resources res = context.getResources();
Drawable myImage = ResourcesCompat.getDrawable(res, R.drawable.my_image, null);
```

**注意**

一つのリソースから複数のDrawableインスタンスを生成した場合、
そのうち一つのDrawablesオブジェクトに変更を加えると他の全てのインスタンスに同じ変更が行われます。
例えば、どれか一つの透明度を変更すると全てのオブジェクトの透明度が変更されます。


### res/drawable/フォルダは圧縮がかかる

`res/drawable/`フォルダ内のリソースはビルド時に圧縮されるため、アプリで実際に表示すると色や画質が劣化する可能性もありそう。
その場合は、`res/raw/`ディレクトリへ配置すると無圧縮のため、元の画質が保てると思われる。


## xmlファイルでDrawableを作成する

ユーザー操作などに依存しない静的なDrawableが必要な場合は、xmlファイルで定義すると良い。
もし、ユーザー操作など、アプリ内でDrawableを加工する場合でも、初期値のxmlファイルを定義すると良い。


## tintを使用して色と図形を分離する

tintを使用するとDrawableを図形部分と色部分に分離して管理することができます。
分離管理できるリソースは、`BitmapDrawable`、`NinePatchDrawable`、`VectorDrawable`の三つです。

詳細は以下の記事を参照

[Android の Drawable への着色について - ひだまりソケットは壊れない](https://vividcode.hatenablog.com/entry/android-app/drawable-tinting)


## setBoundsメソッド

`setBounds`メソッドは、Drawableオブジェクトの描画位置を指定するためのメソッドです。

描画位置は、canvas内でcanvasの左端、上端からの距離で指定します。
`setBounds(fromLeftToLeft, fromTopToTop, fromLeftToRight, fromTopToBottom)`
fromLeftToLeft：canvasの左端からdrawableの左端までの距離
fromTopToTop：canvasの上端からdrawableの上端までの距離
fromLeftToRight：canvasの左端からdrawableの右端までの距離
fromTopToBottom：canvasの上端からdrawableの下端までの距離

**Sample.java**

```java
Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_delete_24dp);
if (drawable != null) {
	drawable.setBounds(0, 0, width, height);
	drawable.draw(canvas);
}
```


## VectorDrawableをBitmapに変換する方法

以下の独自Utilityクラスを使用します。

以下の`ResourceUtil`クラスの上から三番目のpublicメソッドを使用します。

**ResourceUtil.java**

```java
public class ResourceUtil {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static Bitmap getBitmap(VectorDrawable vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }

    private static Bitmap getBitmap(VectorDrawableCompat vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap getBitmap(Context context, @DrawableRes int drawableResId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableResId);
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof VectorDrawableCompat) {
            return getBitmap((VectorDrawableCompat) drawable);
        } else if (drawable instanceof VectorDrawable) {
            return getBitmap((VectorDrawable) drawable);
        } else {
            throw new IllegalArgumentException("Unsupported drawable type");
        }
    }

}
```

呼び出し側は以下の通りです。

**SampleActivity.java**

```java
ResourceUtil.getBitmap(context, R.drawable.ic_place_vector);
```

この`ResourceUtil`クラスのソースコードは、[ VectorDrawableをBitmapに変換する - Qiita ](https://qiita.com/konifar/items/aaff934edbf44e39b04a)からの引用です。