- [IntentSender](#intentsender)
  - [行した PendingIntent の処理結果を取得する例](#行した-pendingintent-の処理結果を取得する例)
  - [ユースケース](#ユースケース)


# IntentSender

IntentSender は PendingIntent から生成するオブジェクトであり、 PendingIntent の実行を制御するためのラッパーのようなものです。

IntendSender を使用することによって、以下のことが実現できます。

- 実行した PendingIntent の処理結果を取得する。
- [Sharesheet で共有した相手 (アプリ) がどのアプリなのかを取得する。](../../Android%20Developers/開発/ガイド/6.主要分野/7.アプリデータとファイル/9.データの共有/2.他のアプリへのシンプルなデータの送信.md/#共有に関する情報を取得する)

## 行した PendingIntent の処理結果を取得する例

```kotlin
// ActivityResultContract を使って、結果を受け取るためのインターフェースを作成
val activityResultLauncher =
    registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // 結果が OK だった場合の処理
            val data = result.data
            // 必要に応じて処理を行う
        } else {
            // 結果が OK でなかった場合の処理
        }
    }

val pendingIntent = MediaStore.createWriteRequest(contentResolver, listOf(uri))

// PendingIntent から IntentSender を作成
val intentSender = pendingIntent.intentSender

// registerForActivityResult を使って IntentSender を起動
activityResultLauncher.launch(IntentSenderRequest.Builder(intentSender).build())
```


## ユースケース

- IntentSender を使用する場合
  - パーミッションの許可ダイアログを表示し、その結果を取得する場合
- IntentSender を使用せず、 PendingIntent で十分な場合
  - 通知 (Notification) をタップしたときに Activity を開く
  - アラーム (AlarmManager) を設定し、指定時間後に BroadcastReceiver を起動

