- [MaterialButton](#materialbutton)
  - [ボタンにアイコンを設定する](#ボタンにアイコンを設定する)
  - [アイコンのサイズを変更する](#アイコンのサイズを変更する)
  - [アイコンのみのボタンの場合、アイコンをボタンの中央に配置する](#アイコンのみのボタンの場合アイコンをボタンの中央に配置する)
  - [参考になるサイト](#参考になるサイト)


# MaterialButton

## ボタンにアイコンを設定する

```xml
<com.google.android.material.button.MaterialButton
  app:icon="@drawable/xxx" />
```


## アイコンのサイズを変更する

```xml
<com.google.android.material.button.MaterialButton
  app:iconSize="64dp" />
```


## アイコンのみのボタンの場合、アイコンをボタンの中央に配置する

```xml
<com.google.android.material.button.MaterialButton
  app:iconPadding="0dp"
  android:padding="0dp"
  app:iconGravity="textStart" />
```

これがないとアイコンが左寄せになってしまう。


## 参考になるサイト

[Goodbye "shape" AndroidのMaterialButtonがすごい良かった話](https://qiita.com/Reyurnible/items/20457d2ef9572b0eee94)

