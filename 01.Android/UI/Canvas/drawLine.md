<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [drawLine](#drawline)
  - [void drawLines(@Size(multiple = 4) @NonNull float[] pts, @NonNull Paint paint)](#void-drawlinessizemultiple--4-nonnull-float-pts-nonnull-paint-paint)
  - [線の色、太さ](#線の色太さ)
<!-- TOC END -->


# drawLine

## void drawLines(@Size(multiple = 4) @NonNull float[] pts, @NonNull Paint paint)

pts : 描画する線の開始地点の `X座標 / Y座標` 、終了地点の `X座標 / Y座標` を示す配列  
`{startX, startY, endX, endY}` の順序で定義します。  
paint : 描画する線のスタイル


## 線の色、太さ

```java
Paint paint = new Paint();
// 色
paint.setColor(Color.YELLOW);
// 太さ
paint.setStrokeWidth(5f);
```
