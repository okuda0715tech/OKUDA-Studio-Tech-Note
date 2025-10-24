<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [AlarmClockInfo](#alarmclockinfo)
	- [AlarmClockInfoインスタンス生成時に第二引数に渡すPendingIntentは何か](#alarmclockinfo生成時第二引数渡pendingintent何)

<!-- /TOC -->


# AlarmClockInfo

## AlarmClockInfo インスタンス生成時に第二引数に渡す PendingIntent は何か

```Java
AlarmManager mgr = (AlarmManager) ctxt.getSystemService(Context.ALARM_SERVICE);

Intent intent1 = new Intent(ctxt, PollReceiver.class);
PendingIntent pIntent1 = PendingIntent.getBroadcast(ctxt, 0, intent1, 0);

Intent intent2 = new Intent(ctxt, EventDemoActivity.class);
PendingIntent pIntent2 = PendingIntent.getActivity(ctxt, 0, intent2, 0);

AlarmManager.AlarmClockInfo alarmClockInfo =
    new AlarmManager.AlarmClockInfo(
        System.currentTimeMillis() + PERIOD,
        pIntent2); // ほとんどのアプリでは、ここに渡す PendingIntent は null で問題なし。

// ほとんどのアプリでは、ここで渡す PendingIntent が、 AlarmManager 発火時に使用される。
mgr.setAlarmClock(alarmClockInfo, pIntent1);
```

上記のように、 `setAlarmClock()` メソッドの第一引数には、 `AlarmClockInfo` インスタンスを渡し、  
第二引数には `PendingIntent` を渡します。  
`AlarmClockInfo` インスタンスを生成する際にも、第二引数に `PendingIntent` を渡します。  
これらのPendingIntentの違いは何でしょうか。

`setAlarmClock()`メソッドの第二引数の `PendingIntent` は、アラームが発行された際に  
実行される `PendingIntent` です。  

`AlarmClockInfo` インスタンスを生成する際に渡す `PendingIntent` は、アラームアプリ専用の  
`PendingIntent` です。  
最近のデバイスでは、 `Notification` 領域を最大に拡張表示した際に、左上あたりに、  
時計アイコンと一緒に、直近で発生するアラームの時刻が表示されます。  
この時刻をタップした際に、デフォルトではデフォルトのアラームアプリが開いて、  
そのアラームの変更画面が表示されます。  

しかし、 `AlarmClockInfo` インスタンスを生成する際に `PendingIntent` を渡すと、  
代わりにそこに記述した `PendingIntent` が起動されます。

デフォルトのアラームアプリを使用しない場合は、`AlarmClockInfo` インスタンスを生成する際に  
第二引数に渡す値は `null` で良い。
