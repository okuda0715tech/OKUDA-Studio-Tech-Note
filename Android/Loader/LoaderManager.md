<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [LoaderManager](#loadermanager)
	- [概要](#概要)
	- [ライフサイクル](#ライフサイクル)
	- [Loaderの管理](#loaderの管理)
		- [Loaderの生成、非同期処理の開始](#loaderの生成非同期処理の開始)
			- [Loaderの生成、及び、非同期処理の開始のトリガー](#loaderの生成及び非同期処理の開始のトリガー)
			- [実際のLoaderの生成](#実際のloaderの生成)
			- [補足・注意点など](#補足注意点など)
		- [Loaderの再生成](#loaderの再生成)
		- [Loaderの破棄](#loaderの破棄)
	- [参考文献](#参考文献)

<!-- /TOC -->


# LoaderManager

## 概要

LoaderManagerは、ActivityやFragmentといったViewControllerのライフサイクルと、AsyncTaskのようなバックグラウンド処理のライフサイクルを別々で管理する仕組みです。


## ライフサイクル

LoaderManagerは、ActivityやFragmentに一つだけ存在することができます。

ActivityやFragmentから、
`LoaderManager.getInstance(Activity/Fragment)`
で、LoaderManagerのインスタンスを生成します。
パラメータに渡したActivity/FragmentがLoaderManagerを管理する親のViewControllerになります。
以前は`Activity.getLoaderManager()`メソッドでインスタンスを生成&取得をしていましたが、現在、このメソッドは非推奨となっています。

LoaderManagerのインスタンスは、Activity/Fragmentの`onStart()`が呼ばれるまでに生成しておく必要があります。

LoaderManagerはconfiguration change(画面回転など)によって破棄・生成されず再利用されます。


## Loaderの管理

一つのLoaderManagerは、複数のLoaderを管理します。

LoaderはLoader IDで管理します。

Activity/Fragmentのインスタンスが異なれば返却されるLoaderManagerのインスタンスは異なるため、LoaderのID重複を心配する必要はありません。


### Loaderの生成、非同期処理の開始

#### Loaderの生成、及び、非同期処理の開始のトリガー

Loaderの生成、及び、非同期処理は、
`LoaderManager.initLoader(int loader_id, Bundle args, LoaderCallbacks<D> callback);`
メソッドを呼ぶことで開始されます。

#### 実際のLoaderの生成

`initLoader`メソッドを呼ぶと、`LoaderManager.LoaderCallbacks.onCreateLoader`が呼ばれます。
（`onCreateLoader`メソッドは、`LoaderCallbacks`インターフェースの抽象メソッドであるため、Overrideしている必要があります。）
その`onCreateLoader`メソッド内で、`AsyncTaskLoader`のインスタンスを生成し、`return`文でそのインスタンスを返却します。

ただし、`initLoader`のパラメータに指定したLoader IDのLoaderを既に保持している場合は、そのインスタンスを再利用します。
そのため、インスタンスを再生成する必要はなく、`onCreateLoader`メソッドも呼ばれません。


#### 補足・注意点など

既に該当Loader IDのLoaderの非同期処理が開始されている場合は、Loaderは生成せず、非同期処理も開始しません。

非同期処理が終わった場合、または、終わっていた場合は、`onLoadFinished()`コールバックメソッドが呼ばれます。

非同期処理の実行中に画面回転などでActivity/Fragmentが一度破棄されている場合は、再度`LoaderManager.initLoader()`メソッドを呼ばないと、`onLoadFinished()`が呼ばれないため、注意してください。
画面回転などが生じると、コールバックを返すインスタンスが古いインスタンスへの参照になってしまいます。そのため、新しいActivity/Fragmentへの参照を持たせるために、`initLoader()`メソッドを呼びます。`initLoader()`メソッドの第三引数に指定するインスタンスが新たなコールバック対象のインスタンスに設定されます。
以下は、画面回転後にinitLoaderを呼び、再生成後の新しいActivity/Fragmentで非同期処理の結果を受け取れるようにしたサンプルです。

```Java
@Override
protected void onCreate(Bundle savedInstanceState) {
  if (mLoaderManager.getLoader(LOADER_ID) != null) {
    mLoaderManager.initLoader(LOADER_ID, null, this);
  }
}
```


### Loaderの再生成

Loaderは一度生成されると、それを使いまわします。しかし、場合によっては再生成したい時もあるでしょう。例えば、Loaderに渡すパラメータを変更したい場合などは、再生成する必要があります。
そんな時は、
`LoaderManager.restartLoader(int loader_id, Bundle args, LoaderManager.LoaderCallbacks<D> callback)`
メソッドを呼びます。

`restartLoader`メソッドの特徴は以下の通りです。

- 既存の同一Loader IDのLoaderは、キャンセル、破棄され、新たなインスタンスが生成されます。
- 新たなLoaderインスタンスには、前回とは異なるパラメータを渡すことができます。
- 前回生成したLoaderからのコールバックは受け取りません。


### Loaderの破棄

`LoaderManager.destroyLoader(int loader_id)`
でLoaderを破棄します。

破棄する前に既に`onLoadFinished`を呼んでいた場合は、`onLoaderReset`が呼ばれます。



## 参考文献

[ローダ - Android developers](https://developer.android.com/guide/components/loaders)
日本語で基本事項が書かれているので、まずは基本を理解したい場合に一番良い。

[1画面で複数AsyncTaskLoader - sms日記](http://ryosms.livedoor.blog/archives/5712827.html)
LoaderManager.LoaderCallbacks<D>は、一つのActivity/Fragmentに一つしか実装できないため、Dの仮型引数が一つしか指定できない。そんな時、複数のLoaderで複数の型に対応させるにはどうしたら良いかが書かれている。

[正しいAsyncTaskLoaderの使い方 - Qiita](https://qiita.com/akkuma/items/a1252498e95c1f68316c

[AsyncTaskLoader - Android developers](https://developer.android.com/reference/android/content/AsyncTaskLoader.html)

[LoaderManager - Android developers](https://developer.android.com/reference/android/app/LoaderManager.html)

[LoaderManager.LoaderCallbacks - Android developers](https://developer.android.com/reference/android/app/LoaderManager.LoaderCallbacks)

[LoaderManager support library - Android developers](https://developer.android.com/reference/android/support/v4/app/LoaderManager.html#destroyloader)

[androidx.loader.content androidX - Android developers](https://developer.android.com/reference/androidx/loader/content/package-summary)

[Android Loader, AsyncTaskLoader, CursorLoader のソースコードを読んでまとめてみた - Qiita](https://qiita.com/kino2718/items/8f7b51c6045404798e5c#asynctaskloader-%E3%81%BE%E3%81%A8%E3%82%81)

[AsyncTaskLoaderのあるActivityに戻ってきたときに再度loadInBackgroundが呼ばれる問題](http://blog.loadlimits.info/2012/09/asynctaskloader%e3%81%ae%e3%81%82%e3%82%8bactivity%e3%81%ab%e6%88%bb%e3%81%a3%e3%81%a6%e3%81%8d%e3%81%9f%e3%81%a8%e3%81%8d%e3%81%ab%e5%86%8d%e5%ba%a6loadinbackground%e3%81%8c%e5%91%bc%e3%81%b0/)

[AsyncTaskLoaderを使ってみる](https://inon29.hateblo.jp/entry/2014/03/24/221904)

[今更ながら入門する AsyncTaskLoader](http://exception-think.hatenablog.com/entry/20160831/1472569200)