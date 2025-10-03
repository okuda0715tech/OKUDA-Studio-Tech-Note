<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [Work の予定や履歴を確認する](#work-の予定や履歴を確認する)
  - [概要](#概要)
  - [コマンド](#コマンド)
  - [出力サンプル](#出力サンプル)
<!-- TOC END -->


# Work の予定や履歴を確認する

## 概要

WorkManager 2.4.0 以降で、デバッグビルドでは、 Work の予定や履歴を確認できるようになりました。

確認できる内容は以下の通りです。

- 過去 24 時間以内に完了した WorkRequest。
- 現在実行中の WorkRequest。
- スケジュール設定された WorkRequest。


## コマンド

```
adb shell am broadcast -a "androidx.work.diagnostics.REQUEST_DIAGNOSTICS" -p "<your_app_package_name>"
```

## 出力サンプル

```
adb shell am broadcast -a "androidx.work.diagnostics.REQUEST_DIAGNOSTICS" -p "androidx.work.integration.testapp"

adb logcat

2020-02-13 14:21:37.990 29528-29660/androidx.work.integration.testapp I/WM-DiagnosticsWrkr: Recently completed work:
2020-02-13 14:21:38.083 29528-29660/androidx.work.integration.testapp I/WM-DiagnosticsWrkr: Id  Class Name   State  Unique Name Tags
    08be261c-2def-4bd6-a716-1e4410968dc4     androidx.work.impl.workers.DiagnosticsWorker    SUCCEEDED  null    androidx.work.impl.workers.DiagnosticsWorker
    48ce04f1-8df9-450b-96ec-6eceabb9c690     androidx.work.impl.workers.DiagnosticsWorker    SUCCEEDED  null    androidx.work.impl.workers.DiagnosticsWorker
    c46f4699-c384-440c-a10e-26d56ce02963     androidx.work.impl.workers.DiagnosticsWorker    SUCCEEDED  null    androidx.work.impl.workers.DiagnosticsWorker
    ce125372-046e-484e-949f-9abb35ce62c3     androidx.work.impl.workers.DiagnosticsWorker    SUCCEEDED  null    androidx.work.impl.workers.DiagnosticsWorker
    72887ddd-8ed1-4018-b798-fac218e95e16     androidx.work.impl.workers.DiagnosticsWorker    SUCCEEDED  null    androidx.work.impl.workers.DiagnosticsWorker
    dcff3d61-320d-4996-8644-5d97944bd09c     androidx.work.impl.workers.DiagnosticsWorker    SUCCEEDED  null    androidx.work.impl.workers.DiagnosticsWorker
    acab0bf7-6087-43ad-bdb5-be0df9195acb     androidx.work.impl.workers.DiagnosticsWorker    SUCCEEDED  null    androidx.work.impl.workers.DiagnosticsWorker
    23136bcd-01dd-46eb-b910-0fe8a140c2a4     androidx.work.integration.testapp.ToastWorker   SUCCEEDED  null    androidx.work.integration.testapp.ToastWorker
    245f4879-c6d2-4997-8130-e4e90e1cab4c     androidx.work.integration.testapp.ToastWorker   SUCCEEDED  null    androidx.work.integration.testapp.ToastWorker
    17d05835-bb61-429a-ad11-fe43fc320a54     androidx.work.integration.testapp.ToastWorker   SUCCEEDED  null    androidx.work.integration.testapp.ToastWorker
    e95f12be-4b0c-4e64-88da-8ee07a31e42f     androidx.work.integration.testapp.ToastWorker   SUCCEEDED  null    androidx.work.integration.testapp.ToastWorker
    431c3ec2-4a55-469b-b50b-4072d35f1232     androidx.work.integration.testapp.ToastWorker   SUCCEEDED  null    androidx.work.integration.testapp.ToastWorker
    883a388f-f911-4098-9143-37bd8fbc098a     androidx.work.integration.testapp.ToastWorker   SUCCEEDED  null    androidx.work.integration.testapp.ToastWorker
    b904163c-6822-4299-8d5a-78df49b7e53d     androidx.work.integration.testapp.ToastWorker   SUCCEEDED  null    androidx.work.integration.testapp.ToastWorker
    453fd7b9-2b16-45b9-abc5-3d2ce7b6a4ba     androidx.work.integration.testapp.ToastWorker   SUCCEEDED  null    androidx.work.integration.testapp.ToastWorker
2020-02-13 14:21:38.083 29528-29660/androidx.work.integration.testapp I/WM-DiagnosticsWrkr: Running work:
2020-02-13 14:21:38.089 29528-29660/androidx.work.integration.testapp I/WM-DiagnosticsWrkr: Id  Class Name   State  Unique Name Tags
    b87c8a4f-4ac6-4e25-ba3e-4cea53ce468a     androidx.work.impl.workers.DiagnosticsWorker    RUNNING    null    androidx.work.impl.workers.DiagnosticsWorker
```
