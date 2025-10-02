<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [@idと@+idの違い](#idid違)

<!-- /TOC -->


# @idと@+idの違い

R.javaクラスに登録するかどうかの違いです。

```xml
<!-- これはID名がxxxのIDリソースをR.javaクラスに登録してからandroid:id(ViewのID)にセットします -->
<View android:id="@+id/xxx"/>

<!-- これはID名がxxxのIDリソースをR.javaクラスに登録せずにandroid:id(ViewのID)にセットします -->
<View app:layout_constraintTop_toBottomOf="@id/dir_name_edit"/>
```
