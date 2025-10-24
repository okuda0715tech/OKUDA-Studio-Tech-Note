<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [androidx.appcompat.widget.Toolbar](#androidxappcompatwidgettoolbar)
	- [導入手順](#導入手順)
		- [build.gradle](#buildgradle)
		- [AppCompatActivityを拡張する](#appcompatactivityを拡張する)
		- [ActionBarによるアプリバーの表示を行わないようにする](#actionbarによるアプリバーの表示を行わないようにする)
	- [ToolbarをActionBarのメソッドで操作する](#toolbarをactionbarのメソッドで操作する)
	- [スタイルの設定](#スタイルの設定)

<!-- /TOC -->


# androidx.appcompat.widget.Toolbar

## 導入手順

### build.gradle

```
implementation 'androidx.appcompat:appcompat:1.1.0'
```


### AppCompatActivityを拡張する

```Java
// Toolbar をアプリバーとして使用するアプリのすべてのアクティビティにこの変更を適用します。
public class MyActivity extends AppCompatActivity {
  // ...
}
```


### ActionBarによるアプリバーの表示を行わないようにする

アプリのテーマでActionBarによるアプリバーが表示されることがあります。今回アプリバーとして使用するのはToolbarであるため、デフォルトで設定されているActionBarの設定を削除します。

AndroidManifest.xml

```
<application
    android:theme="@style/Theme.AppCompat.Light.NoActionBar"
    />
```

もしくは、

styles.xml

```
<style name="AppTheme" parent="Theme.AppCompat.Light.NoActionBar">
```

とします。NoActionBarのテーマであれば、どのテーマでもOKです。


## ToolbarをActionBarのメソッドで操作する


```Java
// getSupportActionBar()でActionBarオブジェクトを取得すると、ToolbarをActionBarのメソッドで操作できるようになります。
ActionBar bar = getSupportActionBar();
bar.hide();
```


## スタイルの設定

styles.xml

```xml
<style name="AppToolbarTheme" parent="Theme.AppCompat.Light.NoActionBar">
		<!-- タイトルテキストの色 -->
		<item name="android:textColorPrimary">@color/colorWhite</item>
		<!-- アイコンの色 -->
		<item name="android:textColorSecondary">@color/colorWhite</item>
</style>

<style name="AppTheme.PopupOverlay" parent="ThemeOverlay.AppCompat.Light" />
```

search_view_activity.xml

```xml
<androidx.appcompat.widget.Toolbar
		android:id="@+id/toolbar"
		android:layout_width="match_parent"
		android:layout_height="?attr/actionBarSize"
		android:background="@color/colorPrimary"
		android:theme="@style/AppToolbarTheme"
		app:popupTheme="@style/AppTheme.PopupOverlay" />
```



