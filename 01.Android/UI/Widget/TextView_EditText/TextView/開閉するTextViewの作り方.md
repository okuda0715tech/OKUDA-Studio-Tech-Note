- [開閉するTextViewの作り方](#開閉するtextviewの作り方)
  - [説明](#説明)
  - [サンプルプロジェクト](#サンプルプロジェクト)


# 開閉するTextViewの作り方

## 説明

`TextView` の `setMaxLines(行数)` メソッドで最大行数をセットするだけで、  
開閉する `TextView` が作成できます。

`setMaxLines()` メソッドの中で `requestLayout()` メソッドを呼び出して、画面を再描画しているため、  
`setMaxLines()` メソッド以外の処理は全く必要ありません。


## サンプルプロジェクト

[https://github.com/okuda0715tech/MyMaxLines](https://github.com/okuda0715tech/MyMaxLines)
