# 9PatchDrawable

## 概要

Androidが自動的にbitmapイメージを伸縮します。
9Patch画像は、普通のpngファイルですが、周囲に1ピクセルのボーダーエリアを含みます。
拡張子は`9.png`とします。
つまり、`image_name.9.png`とします。
pngファイルは`res/drawable`フォルダに格納します。
ボーダーエリアは必要に応じて黒色の幅1ピクセルの線を引くか、または、線を引かない部分については、
白色、もしくは、透明にします。

## 左と上の線はストレッチエリアを表す

左と上の線はストレッチエリアを表します。
また、右と下の線を設定指定ない場合は、Paddingエリアの役割も持ちます。

## 右と下の線はPaddingエリアを表します。

9PatchDrawableをViewのバックグラウンドに設定した場合、
そのViewにテキストを設定した場合には、テキストが右と下のボーダーが重なる部分にテキストが収まるようにテキストが配置されます。

もし、Paddingエリアにテキストが収まらない場合は、収まるようにストレッチエリアが拡張されます。


## 9Patchのpng画像を作成する方法

Android Studioの`WYSIWYG エディタ`を使用すると簡単に9Patch画像を作成することができます。

使用方法は以下の公式ドキュメントを参照

[9patchファイルの作成](https://developer.android.com/studio/write/draw9patch)


## 使用方法

以下は、`res/drawable/my_button_background.9.png`を使用するサンプルです。

**sample_layout.xml**

```xml
<Button android:id="@+id/tiny"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_alignParentTop="true"
        android:layout_centerInParent="true"
        android:text="Tiny"
        android:textSize="8sp"
        android:background="@drawable/my_button_background"/>

<Button android:id="@+id/big"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true"
        android:layout_centerInParent="true"
        android:text="Biiiiiiig text!"
        android:textSize="30sp"
        android:background="@drawable/my_button_background"/>
```

`layout_width`属性と`layout_height`属性は、文字がボタンにぴったりとフィットするように`wrap_content`を指定するのがおすすめです。


