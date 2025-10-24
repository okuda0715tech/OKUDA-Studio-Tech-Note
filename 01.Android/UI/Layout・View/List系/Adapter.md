<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [Adapter](#adapter)
	- [ArrayAdapter](#arrayadapter)
	- [SimpleCursorAdapter](#simplecursoradapter)
		- [参考にしたサイト](#参考)

<!-- /TOC -->


# Adapter

## ArrayAdapter

よくRecyclerViewなどのサンプルコードで使われるAdapter

## SimpleCursorAdapter

ListViewにデータを渡すために用いられるアダプターの1つ。
ArrayAdapterなんかが一般的だが、これはコンストラクタに表示したいデータを渡してあげなければならない。
SQLiteなどのDBから特定のデータを取り出してそれをListViewに表示するというにはデータを取り出してから渡す、という二度手間になってしまいます。
そこで、SimpleCursorAdapterを使うと、DBのカーソルを渡すことで、直接DBのデータを表示させることが出来ます。

### 参考にしたサイト

[SimpleCursorAdapterについて - petitviolet_blog](https://petitviolet.hatenablog.com/entry/20121127/1354002208)
