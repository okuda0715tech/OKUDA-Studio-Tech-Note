<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [saveとrestore](#saverestore)
	- [概要](#概要)

<!-- /TOC -->


# saveとrestore

## 概要

`Canvas.save()`メソッドと`Canvas.restore()`メソッドは`Canvas`の状態を保存、復元するためのメソッドです。  
勘違いしやすいですが、ここで保存・復元する状態とは、Canvas自体を操作する処理のみを示します。  
例えば、`Canvas`を一枚の紙だとします。  
その紙を`save()`で保存して、`rotate`で回転して、`drawXXX()`で描画して、`restore()`で復元したとします。  
描画した内容は取り消されませんが、`rotate`で回転した操作は取り消されます。  
つまり、紙の状態のみを保存・復元していることになります。

回転以外には、`translate`、`scale`、`skew`、`concat`、`clipRect`、`clipPath`などが、保存・復元できるものになります。  

以下に参考にしたもっとわかりやすい説明へのリンクも載せておきます。

[Save canvas then restore, why is that?](https://stackoverflow.com/questions/29040064/save-canvas-then-restore-why-is-that)
