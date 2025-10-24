<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [clipRect](#cliprect)
	- [そもそもクリッピングとは](#そもそもクリッピングとは)
	- [clipRectとは](#cliprectとは)
	- [使用方法](#使用方法)
	- [Region.Opの種類](#regionopの種類)
<!-- TOC END -->


# clipRect

## そもそもクリッピングとは

画像処理などの分野で、画像や画面のうち指定した一部分のみを表示したり、  
その部分だけを切り抜いて残し、他の部分を消去する処理や操作のことをクリッピングという。  


## clipRectとは

`clipRect`メソッドは、カスタムViewの`onDraw()`メソッドで描画する際に、描画可能領域を制限する役割がある。


## 使用方法

```Java
canvas.clipRect(new Rect(10, 10, 100, 100));
```

上記の例の場合、canvasの領域とパラメータのRectの領域の重なる部分のみが描画可能になる。


```Java
canvas.clipRect(new Rect(10, 10, 100, 100), Region.Op.DIFFERENCE);
```

上記の例の場合、canvasの領域とパラメータのRectの領域の重なる部分が描画されなくなる。


## Region.Opの種類

Region.Op          | 意味
-------------------|---------------------------------------------------------------------------
DIFFERENCE         | 元の描画領域に対して、指定された領域を、削除します。
INTERSECT          | 元の描画領域と指定された領域の重なる部分が、描画領域となります。
REPLACE            | 元の描画領域を、指定された領域に、置き換えます。
REVERSE_DIFFERENCE | 指定された領域に対して、元の描画領域を削除した部分が、描画領域になります。
UNION              | 元の描画領域と指定された領域の両方が、描画領域となります。
XOR                | ２つの領域の論理演算XORを、とります。
