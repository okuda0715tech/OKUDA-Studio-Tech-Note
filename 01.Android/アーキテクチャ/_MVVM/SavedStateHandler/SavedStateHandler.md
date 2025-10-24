- [SavedStateHandler](#savedstatehandler)
  - [準備](#準備)
  - [概要](#概要)
  - [OS がバックグラウンドアプリを強制終了した場合](#os-がバックグラウンドアプリを強制終了した場合)
  - [ただし、例外的に保存されないケースもある](#ただし例外的に保存されないケースもある)
  - [参考資料](#参考資料)


# SavedStateHandler

## 準備

`build.gradle` へ以下を追加

```java
implementation 'androidx.lifecycle:lifecycle-viewmodel-savedstate:2.2.0'
// ViewModel生成時の getDefaultViewModelProviderFactory() 呼び出しのために必要
implementation 'androidx.preference:preference:1.1.1'
```

## 概要

SavedStateHandler は、 ViewModel ごとに状態を保存するための仕組みです。 Activity や Fragment の Bundle に相当するものです。

SavedStateHandler は、 Bundle と連携してデータを保存したり、取り出したりします。

例えば、 Activity や Fragment が onSaveInstanceState() で保存したデータを、 Jetpack の仕組みが SavedStateHandle に橋渡ししてくれます。


## OS がバックグラウンドアプリを強制終了した場合

ユーザーがホームボタンやナビゲーションバーでアプリをバックグラウンドにしたあと、 OS がメモリ不足でプロセスを終了する場合：

1. アプリがバックグラウンドに行くときに、通常は Activity の onSaveInstanceState() が呼ばれます。
2. その時点で、SavedStateHandle にも状態が同期されます。
3. もしその後プロセスが強制終了しても、onSaveInstanceState() に保存された Bundle はディスク上に残されます（スナップショット的に）。
4. 次回アプリを再起動したとき、 ViewModel は SavedStateHandle 経由でその Bundle から状態を復元できます。

つまり、「バックグラウンド化 → 強制終了 → 再起動」で、状態は復元されます。


## ただし、例外的に保存されないケースもある

以下のような場合は保存されません。

- OS が異常に急激なリソース解放を行い、onSaveInstanceState()を呼ぶ時間すらない場合
- ViewModelがSavedStateHandleを明示的に使っていない場合
- ActivityがnoHistoryなどの特殊フラグ付きで起動されている場合


## 参考資料

[SavedState ViewModelを使ってデータを保持する](https://star-zero.medium.com/savedstate-viewmodel%E3%82%92%E4%BD%BF%E3%81%A3%E3%81%A6%E3%83%87%E3%83%BC%E3%82%BF%E3%82%92%E4%BF%9D%E6%8C%81%E3%81%99%E3%82%8B-5ea68631ce0f)

