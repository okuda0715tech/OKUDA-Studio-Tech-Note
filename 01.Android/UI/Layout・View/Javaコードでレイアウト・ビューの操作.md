<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [Javaコードでレイアウト・ビューの操作](#javaコードでレイアウトビューの操作)
	- [layout_widht,layout_height,layout_weightの設定](#layout_widhtlayout_heightlayout_weightの設定)
		- [補足説明](#補足説明)
	- [dp(dipともいう)とspの変換](#dpdipともいうとspの変換)

<!-- /TOC -->

# Javaコードでレイアウト・ビューの操作

## layout_widht,layout_height,layout_weightの設定

```Java
int widthPixel = LinearLayout.LayoutParams.MATCH_PARENT;
int heightPixel = dp2px(0f, getApplicationContext());
float weight = 1f;
// layout_weightが不要な場合は、LayoutParamsの引数からweightを削除するだけでOK
LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(widthPixel, heightPixel,weight);
weekView.setLayoutParams(layoutParams);

/**
 * dp値をpx(ピクセル)に変換する
 * @param dp
 * @param context
 * @return
 */
public static int dp2px(float dp, Context context){
    float scale = context.getResources().getDisplayMetrics().density;
    return (int)(dp * scale + 0.5f);
}

/**
 * px(ピクセル)値をdpに変換する
 * @param dp
 * @param context
 * @return
 */public static float px2dp(float px, Context context){
    float scale = context.getResources().getDisplayMetrics().density;
    return (int)(px / scale + 0.5f);
}
```

- Viewが生成された後にsetLayoutParamsメソッドでパラメータをセットすればOK。
- xmlファイルにもパラメータがセットされている場合やJavaコードで複数回セットした場合は、後からセットした方が有効になる。
- 最後に0.5fを足しているのは最も近い整数に切り上げるためです。

### 補足説明

Androidのコードではピクセルが標準の単位です（ライブラリの引数や戻り値はピクセルで設計されているということ）が、複数の仕様の画面に適切に対応するためにはピクセル値でハードコーディングしてはいけません。コードでもdpで値を指定するべきです。

dpからpxに変換するには、端末の画面密度を取得して、その画面密度に応じた係数を用います。
DisplayMatrics.densityというフィールドがその係数になります。

## dp(dipともいう)とspの変換

前の「layout_widht,layout_height,layout_weightの設定」の項を参照



