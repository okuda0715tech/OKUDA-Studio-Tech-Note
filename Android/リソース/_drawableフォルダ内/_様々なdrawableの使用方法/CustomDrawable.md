<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [CustomDrawable](#customdrawable)
	- [作り方](#作り方)
	- [使用方法](#使用方法)

<!-- /TOC -->


# CustomDrawable

## 作り方

以下は、円を描画するCustomDrawableです。

```Java
public class MyDrawable extends Drawable {
    private final Paint redPaint;

    public MyDrawable() {
        // 色、テキストサイズの指定を行う。
        // サンプルは円を描くだけなのでテキストサイズの指定はない。
        redPaint = new Paint();
        redPaint.setARGB(255, 255, 0, 0);
    }

    // 描画処理
    @Override
    public void draw(Canvas canvas) {
        // DrawableのBounds(領域)を取得する
        // Boundsの設定は、このDrawableを使用するクラス側で行う。
        int width = getBounds().width();
        int height = getBounds().height();
        // minメソッドは、widthとheightのどちらか小さい方を取得
        float radius = Math.min(width, height) / 2;

        // 領域(Bounds)の中央に赤色の円を描画する
        canvas.drawCircle(width/2, height/2, radius, redPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        // 必須メソッド
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        // 必須メソッド
    }

    @Override
    public int getOpacity() {
        // 次のいずれかを指定する必要がある
        // PixelFormat.UNKNOWN, TRANSLUCENT, TRANSPARENT, or OPAQUE
        return PixelFormat.OPAQUE;
    }
}
```

`draw(Canvas)`メソッド内で描画したい図形を描画します。
`getBounds()`メソッドは、Drawableを格納する長方形の領域を取得する。
領域の幅と高さは、このDrawableを使用するクラス側で設定する。


## 使用方法

ImageViewに表示させる場合

```Java
MyDrawable mydrawing = new MyDrawable();
ImageView image = findViewById(R.id.imageView);
image.setImageDrawable(mydrawing);
image.setContentDescription(getResources().getString(R.string.my_image_desc));
```


Android 7.0 (API level 24)以上では、xmlファイルから使用することもできます。
この時、クラス名`MyDrawable`はパッケージ名を含めた`fully-qualified class name`で記載する必要があります。
また、`MyDrawable`クラスは`public`である必要があります。

```xml
<com.myapp.MyDrawable xmlns:android="http://schemas.android.com/apk/res/android"
    android:color="#ffff0000" />
```

内部クラスとしてCustomDrawableを定義している場合は、以下のようにします。
xmlタグ名は`drawable`とします。
`class`属性にパッケージ名を含めた`fully-qualified class name`を記載します。
アウタークラスは`public`クラスである必要があります。
インナークラスは`public static`クラスである必要があります。

```xml
<drawable xmlns:android="http://schemas.android.com/apk/res/android"
    class="com.myapp.MyTopLevelClass$MyDrawable"
    android:color="#ffff0000" />
```






