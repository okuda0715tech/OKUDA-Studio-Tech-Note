<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [onMeasureについて](#onmeasure)

<!-- /TOC -->


# onMeasureについて

`widthMeasureSpec`と`heightMeasureSpec`は親Viewから自身のViewに渡された要求です。

`getSuggestedMinimumWidth()`は`getMinimumWidth()`と違い、Viewの幅だけではなく、  
ViewのBackgroundDrawableの幅も考慮し、いづれか大きいほうの値を返します。


`widthMeasureSpec`には二つの情報が格納されており、以下のように取得することができます。

```Java
int mode = MeasureSpec.getMode(widthMeasureSpec);
int size = MeasureSpec.getSize(widthMeasureSpec); // 単位：ピクセル
```

modeには以下の三通りがあります。

- MeasureSpec.EXACTLY
  - 指定されたサイズに従う必要があることを意味します。
  - `android:layout_width="64dp"`や`match_parent`の場合が該当します。
- MeasureSpec.AT_MOST
  - 指定されたサイズを最大値として、できる限り大きい値にすることを意味します。
  - `match_parent`や`wrap_content`の場合が該当します。
- MeasureSpec.UNSPECIFIED
  - 好きなだけ十分大きな値をとることが可能であることを意味します。
  - これは、親Viewがそれぞれの子Viewがどんな大きさになりたいのかを、`measure`を再び呼ぶ前に定義したい場合に使用します。


自身のViewのサイズを決めたら、親Viewの制約も満たしているかどうか確認し、必要ならば調整することになります。  
調整するには`resolveSize(int size, int measureSpec)`メソッドを使用します。  
第一引数`size`には、親Viewの制約に関係なく自身のViewで指定したいサイズを渡します。  
第二引数`measureSpec`には、親Viewやレイアウトxmlの自身のViewパラメータから自身のViewへの制約(modeとsize)を渡します。

`resolveSize()`メソッドは以下のように動作します。  
`MeasureSpec.UNSPECIFIED`の場合、自身のViewが要求したサイズ（パラメータ`size`）を返します。  
`MeasureSpec.EXACTLY`の場合、`measureSpec`に格納されているサイズを返します。  
`MeasureSpec.AT_MOST`の場合、上記の二つのうちの小さいほうのサイズを返します。

`resolveSize`の規定の動作が自分の想定と異なる場合は、カスタマイズすることが可能です。

`resolveSize`メソッドの実際の動作は以下のメソッドと同じ動作となる。

```Java
private int measureDimension(int desiredSize, int measureSpec) {
    int result;
    int specMode = MeasureSpec.getMode(measureSpec);
    int specSize = MeasureSpec.getSize(measureSpec);

    if (specMode == MeasureSpec.EXACTLY) {
        result = specSize;
    } else {
        result = desiredSize;
        if (specMode == MeasureSpec.AT_MOST) {
            result = Math.min(result, specSize);
        }
    }

    if (result < desiredSize){
        Log.e("ChartView", "The view is too small, the content might get cut");
    }
    return result;
}
```

`resolveSize`メソッドをカスタマイズしたい場合は、以下のようにして、上記のコードをカスタマイズすればよい。

```Java
@Override
protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int desiredWidth = getSuggestedMinimumWidth() + getPaddingLeft() + getPaddingRight();
    int desiredHeight = getSuggestedMinimumHeight() + getPaddingTop() + getPaddingBottom();

    setMeasuredDimension(measureDimension(desiredWidth, widthMeasureSpec),
            measureDimension(desiredHeight, heightMeasureSpec));
}
```


## カスタムViewで汎用的に使用できるおすすめの`onMeasure`メソッドの実装サンプル

```Java
@Override
protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    // コンテンツのBackgroundがセットされていない場合のコンテンツの最小サイズを定義する。
    int minChartSize = getResources().getDimensionPixelSize(R.dimen.min_chart_size);

    setMeasuredDimension(
            // getSuggestedMinimumWidth()はコンテンツのBackgroundがセットされている場合のViewの最小サイズを返す。
            // すなわち、ViewのサイズとBackgroundのサイズの大きいほうを返す。
            Math.max(getSuggestedMinimumWidth(),
                    // 第一引数：このView自身が所望するサイズ
                    // 第二引数：このViewへの制約情報(レイアウトxmlや親Viewからの制約)
                    // resolveSizeメソッドは、両方の引数をチェックして、最適なサイズを返す。
                    resolveSize(minChartSize + ViewのPadding, widthMeasureSpec)),
            Math.max(getSuggestedMinimumHeight(),
                    resolveSize(minChartSize + ViewのPadding, heightMeasureSpec)));
}
```
