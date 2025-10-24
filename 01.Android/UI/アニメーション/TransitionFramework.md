<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [TransitionFramework](#transitionframework)
	- [Scene](#scene)

<!-- /TOC -->


# TransitionFramework

## Scene

`Scene`には、ビュー階層の状態（対象のViewGroupとその子Viewの状態）が保持されます。  
`Scene`内の状態は、`Scene`インスタンスを作成した時点の状態が保存されています。
XMLレイアウトファイルから`Scene`インスタンスを作成する場合は、そのレイアウトファイル内の全てのViewの状態が保持されます。  
レイアウトファイルの一部のView階層のみの`Scene`インスタンスを作成することはできません。

`Scene`インスタンス用のxmlレイアウトファイルは、`res/layout`フォルダに格納します。

**Sceneオブジェクトの生成**

`Scene`インスタンスを生成するには、`Scene`を定義したレイアウトファイルもしくはそれをインフレートしたViewオブジェクトと`Scene`のレイアウトが埋め込まれるルートViewが必要です。  
ルートViewと`Scene`用のレイアウトViewが結び付けられて初めて`Scene`オブジェクトが生成されます。
