<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [Serviceクラス](#serviceクラス)
	- [同じServiceを複数回開始した場合](#同じserviceを複数回開始した場合)
	- [サービスの強制終了](#サービスの強制終了)
	- [onStartCommandの戻り値](#onstartcommandの戻り値)
		- [START_NOT_STICKY](#start_not_sticky)
		- [START_STICKY](#start_sticky)
		- [START_REDELIVER_INTENT](#start_redeliver_intent)

<!-- /TOC -->


# Serviceクラス

## 同じServiceを複数回開始した場合

つまり、`startService()`メソッドを複数回呼び出した場合の動作は、

- Serviceは一つだけ生成される(onCreateは一度だけ呼ばれる)。
  - すでに生成されたものが存在している場合は生成しない。
- `startService`を呼び出した回数分だけ`onStartCommand`が呼ばれる。
- 複数回Serviceを呼び出した時に、マルチスレッドで複数の処理を並列処理するか、一つのスレッドでメッセージキューを使用して直列処理するかは実装方法による。
  - 一つのスレッドでメッセージキューを使用する形式にしたいなら、`HandlerThread`を使用すれば良い。


## サービスの強制終了

サービスはシステムによる強制終了と再起動を考慮した設計にする必要があります。
サービスがバックグラウンドで長時間実行し続けるほど、システムにより強制終了を受ける確率が高くなります。
システムによりサービスが強制終了されても、リソースが再利用可能になればサービスを再起動します。
ただし、onStartCommandの戻り値によってサービスの強制終了時の動作を変更することが可能です。


## onStartCommand(Intent, int, int)のパラメータ

onStartCommand(Intent, int, int)のパラメータの意味は以下の通り

  - Intent
  - `startService(Intent)`で渡されるIntent


## onStartCommandの戻り値

onStartCommandメソッドの戻り値でサービス強制終了後の振る舞いを制御することができます。

### START_NOT_STICKY

サービスが強制終了した場合、サービスは再起動しない。

### START_STICKY

サービスが強制終了した場合、サービスは再起動する。
その際、onStartCommand()のIntentにはnullが渡される。

これは、コマンドは実行しないが、無期限に動作し、ジョブを待機するメディア プレーヤー（または同様のサービス）に適しています。

### START_REDELIVER_INTENT

サービスが強制終了した場合、サービスは再起動する。
その際、onStartCommand()のIntentには直前のデータが渡される。

これは、ファイルのダウンロードなど、活発にジョブを実行し、直ちに再開する必要のあるサービスに適しています。
