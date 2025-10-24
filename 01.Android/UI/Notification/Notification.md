<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [Notification](#notification)
  - [基本](#基本)
  - [通知の内容が 2 行以上になる場合、「...」で丸められる](#通知の内容が-2-行以上になる場合で丸められる)
<!-- TOC END -->


# Notification

## 基本

```java
NotificationCompat.Builder builder = new NotificationCompat.Builder(
    this, TWEET_EXE_CHANNEL_ID)
    .setContentTitle("自動ツイートを実行中")
    .setContentText("自動ツイートを実行中です。")
    // 通知タップ時に実行する Intent
    .setContentIntent(contentPendingIntent)
    .setSmallIcon(R.drawable.ic_notifications_black_24dp) // 必須
    .setPriority(NotificationCompat.PRIORITY_HIGH)
    .setDefaults(NotificationCompat.DEFAULT_ALL)
    // アクションボタン (通知に付属できるボタン。ボタンは 3 つまで)
    // 第一引数は、ボタンに表示されるアイコン
    // 第二引数は、ボタンに表示される文字列
    // 第三引数は、ボタンタップ時に実行される Intent
    .addAction(R.drawable.ic_notifications_black_24dp, "Stop Alarm", stopSelf)
    // true に設定した場合、ロック画面に表示された通知から操作を行うには、ロックを解除する必要がある。
    .setAuthenticationRequired(true)
    // setAutoCancel が true の場合、ユーザーが通知をタップすると、通知が消去される。
    .setAutoCancel(true);

// Service を開始する場合
startForeground(2, builder.build());

// 単純に表示する場合
NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
int notificationId = 1;
notificationManager.notify(notificationId, builder.build());
```

```java
// 通知チャネルをあらかじめ作成しておく必要があります。
private void createNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        NotificationChannel channel = new NotificationChannel(
                TWEET_EXE_CHANNEL_ID,
                "name",
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("description");
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}
```


## 通知の内容が 2 行以上になる場合、「...」で丸められる

見出しの通りであるため、詳細は省略します。
