- [Themeの活用例\_ColorStateList](#themeの活用例_colorstatelist)


# Themeの活用例_ColorStateList

例えば、以下のような透過度だけが違う色がたくさん用意されていたとします。

```xml
<!-- colorOn Primary * 25% alpha -->
<color name="color_on_primary_25">...</color>
<!-- colorOnPrimary * 50% alpha -->
<color name="color_on_primary_50">...</color>
<!-- colorOnPrimary * 75% alpha ... -->
```

もし、 colorOnPrimary の色が変わったとしたらどうでしょうか？

上記の定義を全て変更する必要が出てきます。

しかし、 Theme を使用することで、一ヶ所の変更で収めることができます。

**res/color/color_on_primary_25.xml**

```xml
<selector>
    <item
        android:alpha="0.25"
        android:color="?attr/colorOnPrimary" />
</selector>
```

**res/color/color_on_primary_50.xml**

```xml
<selector>
    <item
        android:alpha="0.5"
        android:color="?attr/colorOnPrimary" />
</selector>
```

上記のように ColorStateList を複数定義することで、色に変更が発生した場合には、  
`colorOnPrimary` を変更するだけで済ませることができるようになります。


