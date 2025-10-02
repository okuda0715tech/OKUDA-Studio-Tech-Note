<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [APIレベルによって参照するstyle.xmlファイルを切り替える](#apiレヘルによって参照するstylexmlファイルを切り替える)
	- [`values-v21/styles.xml`のテーマは、`values/styles.xml`スタイルを継承できます。](#values-v21stylesxmlのテーマはvaluesstylesxmlスタイルを継承できます)

<!-- /TOC -->


# APIレベルによって参照するstyle.xmlファイルを切り替える

```
res/values/styles.xml        # themes for all versions
res/values-v21/styles.xml    # themes for API level 21+ only
```


## `values-v21/styles.xml`のテーマは、`values/styles.xml`スタイルを継承できます。

**res/values/styles.xml**

```xml
<resources>
    <!-- base set of styles that apply to all versions -->
    <style name="BaseAppTheme" parent="Theme.AppCompat.Light.DarkActionBar">
        <item name="colorPrimary">@color/primaryColor</item>
        <item name="colorPrimaryDark">@color/primaryTextColor</item>
        <item name="colorAccent">@color/secondaryColor</item>
    </style>

    <!-- declare the theme name that's actually applied in the manifest file -->
    <style name="AppTheme" parent="BaseAppTheme" />
</resources>
```


**res/values-v21/styles.xml**

```xml
<resources>
    <!-- extend the base theme to add styles available only with API level 21+ -->
    <style name="AppTheme" parent="BaseAppTheme">
        <item name="android:windowActivityTransitions">true</item>
        <item name="android:windowEnterTransition">@android:transition/slide_right</item>
        <item name="android:windowExitTransition">@android:transition/slide_left</item>
    </style>
</resources>
```
