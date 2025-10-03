- [FAQ](#faq)
  - [アイコンがFabの中央からずれる場合](#アイコンがfabの中央からずれる場合)


# FAQ

## アイコンがFabの中央からずれる場合

**対応方法**

- `android:scaleType="center"`をつける
- `app:fabCustomSize`属性を追加して、`android:layout_width`や`android:layout_height`に設定した値と同じ値を設定する。

```Xml
<android.support.design.widget.FloatingActionButton
    android:id="@+id/hoge"
    app:fabCustomSize="66dp"
    android:layout_width="66dp"
    android:layout_height="66dp"
    android:scaleType="center"
    android:src="@drawable/icon" />
```

