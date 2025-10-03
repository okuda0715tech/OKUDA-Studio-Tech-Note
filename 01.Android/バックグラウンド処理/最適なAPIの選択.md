<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [最適なAPIの選択](#最適なapiの選択)
  - [WorkManager](#workmanager)
  - [FCM(Firebase Cloud Messaging) + WorkManager](#fcmfirebase-cloud-messaging--workmanager)
  - [Foreground services](#foreground-services)
  - [AlarmManager](#alarmmanager)
  - [DownloadManager](#downloadmanager)
  - [参考](#参考)
<!-- TOC END -->


# 最適なAPIの選択

## WorkManager

- 使用例
  - ログを圧縮してサーバーにアップロードする
    - デバイスが充電中かどうかをチェックして、充電中なら圧縮する
    - 有効なネットワーク接続があるかをチェックして、あるならアップロードする
    - Doze状態にある場合は、Doze解除後に実行する


## FCM(Firebase Cloud Messaging) + WorkManager

- 使用例
  - サーバー上の最新コンテンツの自動ダウンロード


## Foreground services

- 使用例


## AlarmManager

- 使用例


## DownloadManager

- 使用例
  - 実行時間が長い HTTP ダウンロードを実行する


## 参考

以下の公式ドキュメントが非常によくできている。
アプリでどんな処理をするかによって、以下のどのAPIを使用したら良いかが解説されている。

API

- WorkManager
- Foreground services
- AlarmManager
- DownloadManager

[Choosing the right solution for your work](https://developer.android.com/guide/background?hl=ja#choosing_the_right_solution_for_your_work)
