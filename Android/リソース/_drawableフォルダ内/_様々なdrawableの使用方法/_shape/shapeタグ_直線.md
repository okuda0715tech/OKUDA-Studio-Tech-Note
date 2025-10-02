<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [shapeタグ_直線](#shapeタク_直線)
	- [サンプル](#サンプル)
		- [水平な直線](#水平な直線)
		- [垂直な直線](#垂直な直線)

<!-- /TOC -->


# shapeタグ_直線

## サンプル

### 水平な直線

```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="line">

    <stroke
        android:width="3dp"
        android:color="@color/colorPaleBlue" />

</shape>
```


### 垂直な直線

以下の例では、Viewの中心点から水平線を90度回転させて垂直な線にしています。

```xml
<?xml version="1.0" encoding="utf-8"?>
<rotate xmlns:android="http://schemas.android.com/apk/res/android"
    android:fromDegrees="90"
    android:pivotX="50%"
    android:pivotY="50%"
    android:toDegrees="90">

    <shape android:shape="line">

        <stroke
            android:width="3dp"
            android:color="@color/colorPaleBlue" />

    </shape>

</rotate>
```



