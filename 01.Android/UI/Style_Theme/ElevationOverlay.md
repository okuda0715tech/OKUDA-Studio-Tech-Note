


# Elevation Overlay

## Elevation Overlayとは

ダークモードの場合に、ViewのElevationが高ければ高いほど、白っぽく見える表示方法のことである。

Viewの上層(Overlay)に、白色半透明のレイヤーをかぶせ、ViewのElevationが高くなるにつれて、白色半透明のレイヤーに近づくため、Viewが白っぽく見える。  
ユーザーはViewと白色半透明のレイヤーの間に立ってViewを見ていると想像するとわかりやすい。


## 注意点

- colorPrimaryを参照している場合はElevationOverlayが適用されない
- colorSurfaceを参照させるとElevationOverlayが適用される


## elevationOverlayが自動で適用されるウィジェット

- Top App Bar
- Bottom App Bar
- Bottom Navigation
- Tabs
- Card
- Dialog
- Menu
- Bottom Sheet
- Navigation Drawer
- Switch


## AppBarLayoutの例

- AppBarLayoutには最初からelevationが設定されており、基本的にはelevationの変更できないみたいだ。

```xml
<!-- テーマに以下の属性を指定してあげるとダークテーマの場合にcolorSurfaceを参照してくれて、
しかもelevationはデフォルトで設定されているため、自動的にElevationOverlayが適用される。 -->
<style name="Base.Theme.MemoMVVM" parent="Theme.MaterialComponents.DayNight.NoActionBar">
  <item name="appBarLayoutStyle">@style/Widget.MaterialComponents.AppBarLayout.PrimarySurface</item>
</style>
```


## ElevationOverlayを無効にしたい場合

```xml
<style name="Base.Theme.MemoMVVM" parent="Theme.MaterialComponents.DayNight.NoActionBar">
  <item name="appBarLayoutStyle">@style/Widget.MaterialComponents.AppBarLayout.PrimarySurface</item>
  <!-- これを指定すると無効になる -->
  <item name="elevationOverlayEnabled">false</item>
</style>
```
