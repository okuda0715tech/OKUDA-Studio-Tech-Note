<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [BroadcastReceiver](#broadcastreceiver)
	- [概要](#概要)
	- [BroadcastReceiver のライフサイクル](#broadcastreceiver-のライフサイクル)
<!-- TOC END -->


# BroadcastReceiver

## 概要

ブロードキャストレシーバは、ブロードキャストの連絡を受信して、次のアクションを開始するだけのコンポーネントです。  
自分自身で何らかの具体的な処理は行いません。  
次の処理とは、 `Activity` の起動や `Notification` の発行などがあります。

ブロードキャストで送信される連絡は、主にシステム関連の連絡になります。  
例えば、以下のようなものがあります。  

- タイムゾーンが変更された
- 電池の残量が少なくなった
- 写真が撮影された
- 言語設定が変更された

アプリケーション間での通知に使用することもできます。  
例えば、何らかのデータがデバイスにダウンロードされて利用できるようになったことを、  
他のアプリケーションにブロードキャストで知らせるなどの使用方法が考えられます。

`AlarmManager` と組み合わせて使用することもあります。  
`BroadcastReceiver` の `Intent` を `PendingIntent` でラップして、それを `AlarmManager`  
にセットすると、将来のある時点において、その `BroadcastReceiver` が呼ばれます。


## BroadcastReceiver のライフサイクル

`onReceive()` メソッドが `return` されると `BroadcastReceiver` は破棄されます。  
そのため、 `onReceive()` メソッド内でワーカースレッドを生成して、その中で何らかの処理をするべきではありません。  
このような場合は、 `BroadcastReceiver` の `JobService` のスケジュールを設定するのが一般的です。  
そうすることで、プロセス内に有効な実行中処理が残っていることをシステムに認識させられます。
