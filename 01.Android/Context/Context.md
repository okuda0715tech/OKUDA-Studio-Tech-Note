<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [Context](#context)
	- [Contextの取得方法](#contextの取得方法)

<!-- /TOC -->


# Context

## Contextの取得方法

- view.getContext()


## Contextの役割

- テーマ情報を保持する
- `startActivity()`や`startService()`で開始するコンポーネントのパッケージ名を与えるため。
  - 上記のようなstartメソッドは、自分のアプリ以外のコンポーネントを開始できるため、クラス名だけではどのクラスを開始してよいのか区別することができない。そのため、パッケージ名を渡す必要がある。


## Contextの使用例

- Viewを動的に生成する際に、コンストラクタのパラメータとして使用する。
