<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [Group](#group)
  - [概要](#概要)
  - [サンプル](#サンプル)
<!-- TOC END -->


# Group

## 概要

複数の View の Visibility を一括で変更したい場合に、それらの View をまとめるために使用します。


## サンプル

```xml
<androidx.constraintlayout.widget.Group
         android:id="@+id/group"
         android:layout_width="wrap_content"
         android:layout_height="wrap_content"
         android:visibility="visible"
         app:constraint_referenced_ids="button4,button9" />
```

`app:constraint_referenced_ids` : まとめたい View の ID をコンマ区切りで指定します。
