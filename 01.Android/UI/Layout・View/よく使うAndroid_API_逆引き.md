# よく使うAndroid_API_逆引き

## ViewのBackgroundColorをコード上で取得する。

Viewのインスタンスから`setBackgroundColor()`というメソッドは存在するが、`getBackgroundColor()`というメソッドは存在しない。
BackgroundColorをコード上で取得するには以下の方法で行う。

```Java
if (targetView.getBackground() instanceof ColorDrawable) {
    ColorDrawable colorDrawable = (ColorDrawable) targetView.getBackground();
    int backgroundColor = colorDrawable.getColor();
}
```

