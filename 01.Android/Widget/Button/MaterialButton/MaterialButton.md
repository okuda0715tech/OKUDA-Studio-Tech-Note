<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [MaterialButton](#materialbutton)
  - [ボタンにアイコンを設定する](#ボタンにアイコンを設定する)
  - [アイコンのサイズを変更する](#アイコンのサイズを変更する)
  - [アイコンのみのボタンの場合、アイコンをボタンの中央に配置する](#アイコンのみのボタンの場合アイコンをボタンの中央に配置する)
<!-- TOC END -->


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
