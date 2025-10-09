<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [親 ViewGroup に対するパーセンテージで指定する](#親-viewgroup-に対するパーセンテージで指定する)
  - [View の縦横の長さをパーセンテージで決める場合](#view-の縦横の長さをパーセンテージで決める場合)
  - [上下左右のマージンをパーセンテージで決める場合](#上下左右のマージンをパーセンテージで決める場合)
<!-- TOC END -->


# 親 ViewGroup に対するパーセンテージで指定する

## View の縦横の長さをパーセンテージで決める場合

```xml
// 幅をパーセンテージ指定する場合
// 0.5は親ViewGroupの幅を100%としたとき、このViewの幅を50%にするという意味
<View
  android:layout_width="0dp"
  app:layout_constraintWidth_percent="0.5"
  app:layout_constraintWidth_default="percent"/>
```


## 上下左右のマージンをパーセンテージで決める場合

`Guideline` ウィジェットが、親 ViewGroup に対するパーセンテージで配置できるため、
`Guideline` を配置して、それに View の端を合わせて配置する。

```xml
    <!-- 縦のガイドライン線：親 ViewGroup の左端から 95% の位置に配置される -->
    <androidx.constraintlayout.widget.Guideline
        android:id="@+id/guideline_1"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        app:layout_constraintGuide_percent="0.95" />

    <!-- TextView の左端を Guideline に合わせる -->
    <TextView
        android:id="@+id/text_view_1"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="25dp"
        android:background="@color/purple_200"
        android:text="Hello World!"
        android:textSize="25dp"
        app:layout_constraintEnd_toEndOf="@id/guideline_1"
        app:layout_constraintTop_toTopOf="parent" />
```
