- [StyleとThemeの違い2](#styleとthemeの違い2)
  - [【違い1】何と何のマッピングか](#違い1何と何のマッピングか)
    - [スタイルの例](#スタイルの例)
    - [テーマの例](#テーマの例)
    - [テーマ属性の定義](#テーマ属性の定義)
    - [テーマ属性の参照](#テーマ属性の参照)


# StyleとThemeの違い2

## 【違い1】何と何のマッピングか

- Style
  - View属性とその値のマッピング
- Theme
  - テーマ属性とその値のマッピング

### スタイルの例

```xml
<style name="Widget.Plaid.InlineActionButton" parent="...">
    <item name="android:gravity">center_horizontal</item>
    <item name="android:textAppearance">@style/TextAppearance.CommentAuthor</item>
    <item name="android:drawablePadding">@dimen/spacing_micro</item>
</style>
```


### テーマの例

```xml
<style name="Plaid" parent="...">
    <item name="colorPrimary">@color/teal_500</item>
    <item name="colorSecondary">@color/pink_200</item>
    <item name="android:windowBackground">@color/background</item>
</style>
```

テーマのキーはテーマ独自のものです。  
例えば、 `colorPrimary` という属性は View の属性には存在していません。

テーマ属性には **セマンティック名（意味名）** を使います。  
例えば、 `colorPrimary` のような名前を使います。

セマンティック名の逆の名前を **「リテラル名」** と呼びます。
例えば、 `blue` のような名前のことを指します。


### テーマ属性の定義

テーマ属性の定義は、カスタム View のカスタム属性と同様に `<attr>` タグで定義します。

```xml
<resources>
    <attr name="colorPrimary" format="color"/>
    <attr name="actionBarSize" format="dimension"/>
</resources>
```


### テーマ属性の参照

テーマ属性は `?attr` で参照します。

```xml
<style name="Widget.MaterialComponents.Toolbar.Primary">
    <item name="android:elevation">@dimen/design_appbar_elevation</item>
    <item name="android:background">?attr/colorPrimary</item>
    <item name="titleTextColor">?attr/colorOnPrimary</item>
</style>
```
