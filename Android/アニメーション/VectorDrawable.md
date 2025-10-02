<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [VectorDrawable](#vectordrawable)
	- [設定可能な属性](#設定可能属性)

<!-- /TOC -->


# VectorDrawable

## 概要

VectorDrawableは、AndroidでSVGファイルを扱う場合に使用するDrawableオブジェクトです。  
SVGファイルをリソースとしてそのままアプリに取り込むことはせず、VectorDrawableリソースに変換して取り込みます。  
SVGファイルをそのままの状態でアプリに取り込むことはまずありません。


## 設定可能な属性

ドキュメントを参照

[VectorDrawable](https://developer.android.com/reference/android/graphics/drawable/VectorDrawable)


## SVGファイルをVectorDrawableファイルに変換する方法

拡張子が`.svg`のsvgファイルをアプリリソースとして取り込む場合は、Android Studioを使用して`VectorDrawable`に変換して取り込む。

取り込む方法は、以下の通りです。

1. `res/drawable`フォルダを右クリックし、`new` -> `Vector Asset`を選択する。
2. `Asset Type`で`Local file`を選択し、ローカル上のsvgファイルを参照する。
	- もし、svgファイルが選択できない場合は、ファイル名に日本語が含まれていないか確認する。日本語はNG。
3. ウィザードを完了させるとdrawableフォルダにVectorDrawableが作成されている。
