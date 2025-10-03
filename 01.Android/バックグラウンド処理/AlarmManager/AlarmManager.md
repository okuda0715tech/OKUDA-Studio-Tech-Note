<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [AlarmManager](#alarmmanager)
	- [概要](#概要)
	- [デバイス再起動などによって、アラームが削除されるため、再設定する](#デバイス再起動などによってアラームが削除されるため再設定する)
	- [アラームタイプ](#アラームタイプ)
	- [アラームのセット](#アラームのセット)
	- [アラームのキャンセル](#アラームのキャンセル)
	- [アラームの更新](#アラームの更新)
	- [繰り返しのアラーム](#繰り返しのアラーム)
<!-- TOC END -->


# AlarmManager

## 概要

AlarmManagerは、将来のある時点でアプリが実行されていなくても、アプリを起こして処理をさせたい場合に使用します。

AlarmManagerにセットするPendintIntentはBroadcastReceiverをラップして、  
AlarmManagerが発行された場合には、まずBroadcastReceiverが起動されるようにする必要がある。  
ActivityやServiceを起動したい場合は、BroadcastReceiverから起動する。  
場合によっては、BroadcastReceiverからActivityを起動し、そのActivityからServiceを起動する場合もあるだろう。


## デバイス再起動などによって、アラームが削除されるため、再設定する

AlarmManagerのアラームは、以下のケースで削除されるため、再設定する必要があります。

- デバイス再起動
- アプリアップデート
- 端末時刻の変更
- ロケールの変更時

上記のイベントを通知するBroadCastが存在するため、それを受信して、再設定します。


## アラームタイプ


## アラームのセット

```Java
// Activity内の処理

alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

// アラームが発行した際に呼ばれるBroadcastReceiver
final Intent intent = new Intent(MainActivity.this, AlarmManagerReceiver.class);
final int requestId = 0;

// FLAGについてはPendingIntentのメモを参照
PendingIntent pendingIntent = PendingIntent.getBroadcast(
				MainActivity.this, requestId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

// 現在時刻の3秒後を示すカレンダーを作成
Calendar calendar = Calendar.getInstance();
calendar.add(Calendar.SECOND, 3);
// 第二引数についてはAlarmClockInfoのメモを参照
AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager.AlarmClockInfo(
				calendar.getTimeInMillis(), null);

// set系メソッドのいずれかを使用してアラームをセットする
alarmManager.setAlarmClock(alarmClockInfo, pendingIntent);
```


## アラームのキャンセル

セットしたアラームは、AlarmManagerの`cancel()`メソッドでキャンセルすることができます。

ドキュメントには、PendingIntent生成時に`PendingIntent.FLAG_ONE_SHOT`を指定した場合は、  
キャンセルができないと書いてあったがキャンセルできる。キャンセルしても問題ないのかわからないが、  
そもそもPendingIntent生成時に`FLAG_ONE_SHOT`を使用する必要のある場合が限られてくると思うので、  
深く考えないようにする。

```Java
alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

// アラームが発行した際に呼ばれるBroadcastReceiver
final Intent intent = new Intent(MainActivity.this, AlarmManagerReceiver.class);
final int requestId = 0;

// 新しいPendingIntentインスタンスを生成しないようにFLAG_NO_CREATEを指定する。
PendingIntent pendingIntent = PendingIntent.getBroadcast(
				MainActivity.this, requestId, intent, PendingIntent.FLAG_NO_CREATE);

if (pendingIntent != null && alarmManager != null) {
		alarmManager.cancel(pendingIntent);
}
```


## アラームの更新

アラームの発行時刻を更新する場合は、同一のPendingIntnetと更新後の発行時刻をset系メソッドに渡して、アラームをセットし直します。


## 繰り返しのアラーム

- `setRepeating()`
  - 一般的な繰り返しアラームの設定メソッドです。
  - 現在時刻がアラーム発行時刻を既に過ぎている場合は、すぐに発行されます。
    - 特に、複数回の発行時刻を過ぎている場合は、スキップされたすべてのアラームが発行されます。
    - スキップにより、アラームの発行が遅延した場合でも、その後のアラームは当初のスケジュール通り発行されます。遅延の影響によって、未発行のアラームが後ろ倒しされることはありません。
    - もし、アラームがスキップにより遅延した場合に、その後の繰り返しアラームをそれにあわせて後ろ倒ししたい場合は、一回限りのアラームを設定し、アラームが発行されるたびに次のアラームを設定するようにします。
    - APIレベル19以降では、繰り返しアラームは正確な時刻に発行される保障はありません。正確なアラームが必要な場合は、一回限りのアラームを設定する必要があります。
    - 繰り返しの正確なアラームが必要な場合は、`setRepeating()`メソッドは使用できません。アラームが発行されるたびに次のアラームを設定するようにします。
  - `cancel()`メソッドで明示的にキャンセルされるまでは繰り返されます。
- `setInexactRepeating()`
  - 複数のアプリの繰り返しアラームを同時期に発行することで、デバイスのスリープ解除の回数を減らし、消費電力を抑える効果があります。
