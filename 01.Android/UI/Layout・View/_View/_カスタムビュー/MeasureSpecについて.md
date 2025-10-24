<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [MeasureSpecについて](#measurespec)
	- [用途](#用途)

<!-- /TOC -->


# MeasureSpecについて

`android.view.View.MeasureSpec`は、`onMeasure(int widthMeasureSpec, int heightMeasureSpec)`メソッドのパラメータとして渡されるオブジェクトである。  
内部には`size`と`mode`を保持している。  
`mode`には、`UNSPECIFIED`,`EXACTLY`,`AT_MOST`が存在する。
`MeasureSpec`クラス自体は`int`型をしている。


## 用途

カスタムViewを定義した場合に、自分自身の高さ、幅を算出するために使用される。
また、その過程で、カスタムViewの子孫Viewの高さ、幅を算出するために使用することもある。
