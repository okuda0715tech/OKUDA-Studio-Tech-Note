<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [vectorファイルをsvgファイルに変換する](#vectorファイルをsvgファイルに変換する)
  - [概要](#概要)
  - [サンプルファイル](#サンプルファイル)
  - [.svg ファイルへの変換手順](#svg-ファイルへの変換手順)
<!-- TOC END -->


# vectorファイルをsvgファイルに変換する

## 概要

Android Studio でベクター画像を利用する場合には、 xml ファイルに `<vector>` タグを使用して  
ベクター画像を用意する。その `<vector>` タグで記述された xml ファイルから、  
SVG の公式フォーマットである `.svg` 形式のファイルを生成する手順を記載する。


## サンプルファイル

**変換前（Android Studio）**

```xml
<?xml version="1.0" encoding="utf-8"?>
  <vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp"
    android:height="24dp"
    android:viewportHeight="24"
    android:viewportWidth="24">

  <path
    android:fillColor="#ffffff"
    android:pathData="M12,3L2,12h3v8h2.5v-0.8c0-1.5,3-2.2,4.5-2.2s4.5,0.8,4.5,2.2V20H19v- 8h3L12,3zM12,15.2c1.2,0-2.2-1-2.2-2.2 s1-2.2,2.2-2.2s2.2,1,2.2,2.2S13.2,15.2,12,15.2z" />
  <path android:pathData="M0,0h24v24H0V0z" />
</vector>
```

**変換後**

```xml
<svg xmlns="http://www.w3.org/2000/svg"
  width="24"
  height="24"
  viewBox="0 0 24 24" >

  <path
    fill="#ffffff"
    d="M12,3L2,12h3v8h2.5v-0.8c0-1.5,3-2.2,4.5-2.2s4.5,0.8,4.5,2.2V20H19v8h3L12,3zM12,15.2c1.2,0-2.2-1-2.2-2.2 s1-2.2,2.2-2.2s2.2,1,2.2,2.2S13.2,15.2,12,15.2z" />
  <path d="M0,0h24v24H0V0z" fill="none"/>
</svg>
```


## .svg ファイルへの変換手順

**\<vector>タグの変換**

`<vector>` タグを `<svg>` タグに変換する。  
`xmlns:android="http://schemas.android.com/apk/res/android` を  
`xmlns="http://www.w3.org/2000/svg"` に変換する。
`android:width` と `android:height` を `width` と `height` に変換する。  
`android:width="24dp"` と `android:height="24dp"` から、単位 `dp` を削除する。  
`android:viewportHeight="24"` と `android:viewportWidth="24"` は `viewBox="0 0 24 24"` に変換する。  

**\<path>タグの変換**

`android:fillColor` 属性を `fill` に変換する。  
`android:pathData` 属性を `d` に変換する。  
`<path>` タグの属性に `android:fillColor` 属性が定義されていない場合は、 `fill="none"` を定義する。

**ファイルの拡張子を .xml から .svg へ変換する**
