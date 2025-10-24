<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [描画関連概要](#描画関連概要)
	- [Drawable](#drawable)
	- [BitmapとDrawableの違い](#bitmapとdrawableの違い)
	- [DrawableとViewの違い](#drawableとviewの違い)

<!-- /TOC -->

# 描画関連概要

## Drawable

Drawableとは、「描画可能なリソースをプログラム内で扱う時に使用する型」です。

ほとんどの描画可能リソースはDrawable型で扱うことができます。


## BitmapとDrawableの違い

- `android.graphics.Bitmap`と`android.graphics.drawable.Drawable`の違い
  - Bitmap
    - jpg,pngなどのビットマップ形式の画像ファイルを扱うためのクラス
    - 画像をベクター形式ではなく、ビットマップ形式で扱う
  - Drawable
    - 描画可能なリソースを扱うためのクラス
      - 描画可能なリソースには、jpg,pngなどのビットマップ画像やxmlで定義したシェープやベクター画像やアニメーションリソースなどがある
    - いわば、DrawableはBitmapの機能も含む
      - 実際に、`android.graphics.Bitmap`クラスとは別に、`Drawable`クラスを継承した`android.graphics.drawable.BitmapDrawable`クラスも存在している。

したがって、jpg,pngなどのビットマップ形式のデータをメモリに展開したい場合は、`Bitmap/BitmapDrawable`クラスを使用します。  
ポリモーフィズムを使用してビットマップ以外のデータも一律で処理したい場合には、`BitmapDrawable`を使用した方が便利でしょう。


## DrawableとViewの違い

- View
  - イベントを受信したり、ユーザーとやり取りする機能があります。
- Drawable
  - DrawableはViewを継承していないため、イベントを受信したり、ユーザーとやり取りする機能がありません。
