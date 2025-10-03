- [各 API の継続可能時間と繰り返し処理の実行可能間隔](#各-api-の継続可能時間と繰り返し処理の実行可能間隔)


# 各 API の継続可能時間と繰り返し処理の実行可能間隔

- WorkManager の WorkRequest
  - 一つの WorkRequest につき、最大 10 分まで
    - ただし、チェーンすることにより、実質、無制限となるが、 Doze などにより、いずれ停止すると思われる。
- WorkManager の PeriodicWorkRequest
  - 最短でも、 15 分の間隔を開ける必要がある。
- JobScheduler API
  - 最短でも、 15 分の間隔を開ける必要がある。
- ウィジェットの android:updatePeriodMillis
  - 最短でも、 30 分 ( 1800000 ミリ秒) の間隔を開ける必要がある。
- BroadcastReceiver の onReceive
  - 通常、アプリのメインスレッドで実行されるブロードキャスト レシーバは、最大 10 秒間実行された後、応答なしと見なされ、アプリケーション応答なし（ANR）エラーがトリガーされます。



