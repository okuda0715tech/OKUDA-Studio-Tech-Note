<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [PendingIntent](#pendingintent)
	- [概要](#概要)
	- [使用例](#使用例)
	- [PendingIntentの生成方法](#pendingintentの生成方法)
	- [PendingIntentの生成、取得時のフラグ](#pendingintentの生成取得時のフラグ)
	- [PendingIntent が同一かどうかの判定方法](#pendingintent-が同一かどうかの判定方法)
		- [同一かどうか判定するのはどんなときか](#同一かどうか判定するのはどんなときか)
	- [具体的なフラグ、同一判定のユースケース](#具体的なフラグ同一判定のユースケース)
<!-- TOC END -->


# PendingIntent

## 概要

PendingIntentはIntentクラスのラッパークラスです。  
`startActivity`や`startService`などのIntentを実行する主体が自分のアプリの場合は、通常のIntentを使用しますが、  
他のアプリが自分のアプリのActivityやServiceを開始したい場合は、PendingIntentを使用します。  
PendingIntentは、他のアプリが自分のアプリのコンポーネントを開始する権限（トークン）を持ったIntentです。  
他のアプリがPendingIntentを使用して自分のアプリを起動する場合は、自分のアプリのプロセスが終了していても、他のアプリのプロセスがPendingIntentを使用することができます。（実際に自分のアプリが起動するプロセスは他のアプリとは別プロセスなのかな？）

自分のアプリが他のアプリを起動する場合は、マニフェストに権限を記述するなどして権限を与えることができますが、  
逆に他のアプリが自分のアプリを起動したい場合は、他のアプリのマニフェストを編集するわけにはいきません。  
そこで登場するのがPendingIntentであるという考え方もできます。


## 使用例

- 通知からユーザーがアクションを実行するとき
  - `NotificationManager`が`Intent`を実行します
- アプリのウィジェット（ホーム画面の横長の天気予報表示など）からユーザーがアクションを実行するとき
  - ホーム画面のアプリが Intent を実行します
- 将来の指定された時間に処理を実施する時
  - `AlarmManager`が`Intent`を実行します
- ActivityからServiceを開始し、Serviceの処理が完了したら別のActivityを起動したい場合
  - ActivityからServiceにPendingIntentを渡します。
  - PendingIntentには、Serviceの処理が完了した時に起動したいActivityのIntentをラップします。
  - Serviceの処理が完了したら、受け取ったPendingIntentの`send()`メソッドを呼び出すとActivityが開始されます。


## PendingIntentの生成方法

```Java
// Activityを開始するIntentの場合
PendingIntent pendingIntent = PendingIntent.getActivity();
// Serviceを開始するIntentの場合
PendingIntent pendingIntent = PendingIntent.getService();
// BroadcastReceiverを開始するIntentの場合
PendingIntent pendingIntent = PendingIntent.getBroadcast();
```


## PendingIntentの生成、取得時のフラグ

- FLAG_CANCEL_CURRENT
  - 既存のIntentの`Extra data`のみを変更したい場合に使用するフラグです。
  - 同じPendingIntentが既に存在する場合は、既存のものをキャンセルし、新しく生成したPendingIntentのみが有効になります。
  - このフラグを使用する場合は、まずは`FLAG_UPDATE_CURRENT`を使用できないか検討した方がよいです。
- FLAG_UPDATE_CURRENT
  - 既存のIntentの`Extra data`のみを変更したい場合に使用するフラグです。
  - 同じPendingIntentが既に存在する場合は、既存のものをキャンセルせず、`Extra data`のみを入れ替えます。
  - PendingIntentを改めてAlarmManager等にセットする必要はありません。
- FLAG_IMMUTABLE
  - このインテントの未入力のプロパティを入力するためにsendメソッドに渡される追加のインテント引数が無視されることを意味します。
- FLAG_NO_CREATE
  - PendingIntent生成時点で同じPendingIntentが存在していない場合は、PendingIntentのインスタンスは生成されず、nullを返します。
- FLAG_ONE_SHOT
  - このPendingIntentは一度だけ使用することができます。
  - 一度send()メソッドを呼び出したら、以降のsend()メソッドは無効になります。
  - 使用上の注意
    - AlarmManagerにセットされたPendingIntentがこのフラグを使用している場合は、`alarmManager.cancel(pendingIntent)`メソッドでキャンセルすることはできません。


## PendingIntent が同一かどうかの判定方法

`PendingIntent` の同一判定は、 `PendingIntent` 生成時に渡される以下の 3 要素によって判定されます。

- `Intent`
- フラグ `PendintIntent.FLAG_XXX`
- `requestCode`

これらの 3 要素は、 `PendingIntent` インスタンスを生成する時に設定します。

`Intent` が同一かどうかは、 `intent.filterEquals()` メソッドによって判定されます。

### 同一かどうか判定するのはどんなときか

例えば、 `AlarmManager` を使用して、指定した時刻になったら自動的に処理を開始する場合、  
`AlarmManager` に対して、 `PendingIntent` を登録します。登録した `PendingIntent`  
を削除したい場合に、削除対象の `PendingIntent` を指定するために同一かどうかを判定する  
必要があります。


## 具体的なフラグ、同一判定のユースケース

同時に二つのNotificationを表示する必要がある場合などは、同一判定で不一致の結果が返るようにする必要があります。  
一方、その必要がない場合は、`FLAG_CANCEL_CURRENT`、または、`FLAG_UPDATE_CURRENT`を使用して、PendingIntentを作り直す or 更新するようにします。
