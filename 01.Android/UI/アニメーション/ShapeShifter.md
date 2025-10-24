<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [ShapeShifter](#shapeshifter)
	- [Import](#import)
		- [SVGとVectorDrawableの違い](#svgvectordrawable違)
		- [AnimatedVectorDrawableはインポートできない](#animatedvectordrawable)

<!-- /TOC -->


# ShapeShifter

## Importする

### SVGとVectorDrawableの違い

- SVG
  - 拡張子が`.svg`であるファイル
  - ファイルの中身は`<svg>`タグで囲まれており、`<line>`タグや`<rect>`タグで図形が描画される
- VectorDrawable
  - 拡張子が`.xml`であるファイル
  - Androidのクラスの一つ
  - 静的なドローワブルを定義することができる
  - ファイルの中身は`<vector>`タグで囲まれており、`<group>`、`<path>`、`<clip-path>`タグなどで構成されます。
  - Android StudioのVector Asset StudioでMaterial DesignのリソースをインポートしたときはVectorDrawable形式でリソースが生成される。
    - Vector Asset Studioとは、res/drawableフォルダを右クリックして、New -> Vector Assetを選択した時に表示されるウィザードのこと。


### AnimatedVectorDrawableはインポートできない


## 編集する

### 変形（morphy）方法を変更するためにポイントを移動させたいが、移動できない場合

`fromValue`、`toValue`で指定された座標にポイントがデフォルトでセットされ、青色のポイントとなる。  
これらのポイントはGUI上で変更することはできない。  
青色のポイントを移動したい場合は`fromValue`or`toValue`を変更する必要がある。

というより、移動すると図形が変わってしまうポイントはGUI上で変更することができないのかな？


## 保存する

作成したリソースを保存する場合は、Saveボタンで`.shapeshifter`拡張子のファイル形式で保存します。  
`AnimatedVectorDrawable`形式でエクスポートした場合、そのリソースをインポートすることができないため、  
以後、そのリソースを編集することができなくなってしまいます。
