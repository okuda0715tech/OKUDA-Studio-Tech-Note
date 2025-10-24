<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [フォアグラウンドService](#フォアグラウンドservice)
  - [フォアグラウンドServiceとは](#フォアグラウンドserviceとは)
  - [使用する場面](#使用する場面)
  - [フォアグラウンドServiceの使いすぎに注意](#フォアグラウンドserviceの使いすぎに注意)
  - [使用方法](#使用方法)
    - [startForegroundService() と startService() の違い](#startforegroundservice-と-startservice-の違い)
  - [メモ](#メモ)

<!-- /TOC -->


# フォアグラウンドService

## フォアグラウンドServiceとは

`Service` 内で、 `startForegroundService()` を呼び出した `Service` は  
フォアグラウンドサービスと見なされます。  
それ以外のサービスはバックグラウンドサービスと見なされます。


## 使用する場面

フォアグラウンドサービスは、システムが即座に実行するタスク、または実行し続ける必要があるタスクに対してのみ使用します。  
たとえば、ソーシャル メディアに写真をアップロードしたり、  
音楽プレーヤーアプリがフォアグラウンドで実行していないときに音楽を再生したりする場合です。


## フォアグラウンドServiceの使いすぎに注意

アプリがアイドル状態にならないようにする目的で、フォアグラウンドサービスを開始しないでください。  
アイドル状態にならないようにする目的とは、フォアグラウンドで処理を実行する必要がないにも関わらず、  
アプリが破棄されるのを防ぐためにフォアグラウンドサービスを使用することである。

フォアグラウンド `Service` を使用する際は、アプリが `Service` でどんな処理をしているのかを  
`Notification` でユーザーに通知する必要があります。  
そのため、開発者は優先度が `PRIORITY_LOW` 以上のステータスバー通知を表示する必要があります。  
操作の重要性が低く、 `Notification` にアプリの処理内容を表示したくない場合は、  
サービスを使用するのではなく、スケジュールされたジョブ (※1) を使用することを検討してください。

(※1)
[Guide to background processing](https://developer.android.com/guide/background?hl=ja)


## 使用方法

サービスをフォアグラウンドで実行するようリクエストするには、 `startForeground()` を呼び出します。  
このメソッドは、通知を一意に識別する整数と、ステータスバーの `Notification` の 2 つのパラメータを  
受け取ります。通知の優先度は `PRIORITY_LOW` 以上である必要があります。  
次に例を示します。

`startForeground() に渡す整数 ID には、0 は使用できません。`

```Java
Intent notificationIntent = new Intent(this, ExampleActivity.class);
PendingIntent pendingIntent =
        PendingIntent.getActivity(this, 0, notificationIntent, 0);

Notification notification =
          new Notification.Builder(this, CHANNEL_DEFAULT_IMPORTANCE)
    .setContentTitle(getText(R.string.notification_title))
    .setContentText(getText(R.string.notification_message))
    .setSmallIcon(R.drawable.icon)
    .setContentIntent(pendingIntent)
    .setTicker(getText(R.string.ticker_text))
    .build();

// ID に 0 は使用できません。
// サービスが開始されてから5秒以内に startForeground() を呼び出さなかった場合は、 ANR が発生します。
startForeground(ONGOING_NOTIFICATION_ID, notification);
```

サービスをフォアグラウンドから削除するには、 `stopForeground()` を呼び出します。  
このメソッドは、ステータスバー通知も削除するかどうかを示す int 値を受け取ります。  
このメソッドでは、サービスは停止されません。

ちなみに、サービスがまだフォアグラウンドで実行中に停止した場合は、通知も削除されます。


### startForegroundService() と startService() の違い

- `startForegroundService()`
  - `startForegroundService()` メソッドを呼び出した瞬間は、バックグラウンドサービスが生成されますが、このサービスは後でフォアグラウンドに昇格することを意味しています。
  - android 8.0以上のデバイスでフォアグラウンドサービスを開始する場合に使用します。  -
- `startService()`
  - バックグラウンドサービスが生成されます。このサービスは後でフォアグラウンドに昇格するとは約束されていません。
  - android 8.0 未満のデバイスでサービスを開始する場合に使用します。
  - または、 android 8.0 以上のデバイスで、（アプリがフォアグラウンドにある場合に限り）バックグラウンドサービスを開始する場合に使用します。


## メモ

アプリがフォアグラウンドにある場合、そのアプリはフォアグラウンド サービスとバックグラウンド サービスの両方を制限なく作成して実行できます。
バックグラウンド アプリによるバッグラウンド サービスの作成を許可しません。
