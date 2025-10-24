<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [AndroidStudio上でドロワーが開いた状態をプレビュー表示する](#androidstudio上でドロワーが開いた状態をプレビュー表示する)
<!-- TOC END -->


# AndroidStudio上でドロワーが開いた状態をプレビュー表示する

`tools:openDrawer="start"` を記述することで、AndroidStudio上でドロワーが開いた状態をプレビュー表示することができます。

```xml
<androidx.drawerlayout.widget.DrawerLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/drawer_layout"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:fitsSystemWindows="true"
    tools:openDrawer="start">

    ...

</androidx.drawerlayout.widget.DrawerLayout>
```
