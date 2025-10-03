<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [NavigationView のテーマの設定](#navigationview-のテーマの設定)
  - [サンプル](#サンプル)
<!-- TOC END -->


# NavigationView のテーマの設定

## サンプル

**activity_main.xml**

```xml
<com.google.android.material.navigation.NavigationView
    android:id="@+id/nav_view"
    android:layout_width="wrap_content"
    android:layout_height="match_parent"
    android:layout_gravity="start"
    android:fitsSystemWindows="false"
    app:headerLayout="@layout/nav_header_main"
    app:itemBackground="@drawable/drawable_menu_item_background"
    app:itemIconTint="@color/drawer_menu_item_text_and_icon"
    app:itemTextColor="@color/drawer_menu_item_text_and_icon"
    app:menu="@menu/activity_main_drawer" />
```

**color/drawer_menu_item_text_and_icon.xml**

必ず `color` フォルダを作成して、その中にファイルを格納する必要があります。

```xml
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:color="?attr/colorPrimaryVariant" android:state_checked="true" />
    <item android:color="?attr/colorPrimaryVariant" />
</selector>
```

**drawable/drawer_menu_item_background.xml**

```xml
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:drawable="@color/lightPink_500_light" android:state_checked="true" />
    <item android:drawable="@color/white" />
</selector>
```
