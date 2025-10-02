<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [shapeAppearance](#shapeappearance)
	- [概要](#概要)

<!-- /TOC -->


# shapeAppearance

## 概要

マテリアルデザインのシェイプを使用するためには、ウィジェットの`background`に、`MaterialShapeDrawable`を指定できる必要があります。  
全てのMaterialDesignComponentは`MaterialShapeDrawable`を指定することができます。  
カスタムViewもこれを指定可能にすることでシェイプを適用することができます。

マテリアルデザインのシェイプは、以下のことができます。
- Viewをテーマに沿った形状にする
- 影の描画？
- ダークテーマ適用時のエレベーションの透過度調整？
- など...


## 利用の前提条件

`shapeAppearance`を利用できるViewは、以下のViewに限られています。

- Bottom Sheet
- Card
- Chip
- Date Picker
- Dialog
- Extended Floating Action Button
- Floating Action Button
- Button
- Toggle Button
- Navigation View
- Slider
- Text Field

上記以外のViewでViewの形状を変更したい場合には、drawableフォルダ内に`<shape>`タグを使用した  
静的な形状リソースを定義して、レイアウトxmlにて、`android:background="@drawable/xxxxx"`とすることで  
Viewの形状変更を実装します。
