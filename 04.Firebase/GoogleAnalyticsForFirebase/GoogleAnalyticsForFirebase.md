<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [GoogleAnalyticsForFirebase](#googleanalyticsforfirebase)
  - [logEvent() メソッドでイベントのログを記録する](#logevent-メソッドでイベントのログを記録する)
    - [メソッドシグネチャ](#メソッドシグネチャ)
  - [adb コマンドでログに記録されたことを確認する](#adb-コマンドでログに記録されたことを確認する)
  - [Firebase コンソールで確認する（製品版リリース後）](#firebase-コンソールで確認する製品版リリース後)
  - [Firebase コンソールで確認する（製品版リリース前）](#firebase-コンソールで確認する製品版リリース前)
<!-- TOC END -->


# GoogleAnalyticsForFirebase

## logEvent() メソッドでイベントのログを記録する

```java
Bundle bundle = new Bundle();
bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
```

### メソッドシグネチャ

```java
public void logEvent (String name, Bundle params)
```

- String name
  - イベント名を指定します。
  - イベント名は 500 種類まで作成することが可能です。
  - 定義済みの `FirebaseAnalytics.Event` を使用することが推奨されます。
    - [一覧](https://firebase.google.com/docs/reference/android/com/google/firebase/analytics/FirebaseAnalytics.Event?hl=ja)（探しずらいので、まずは以下の 「 GA4 推奨イベント」を見たほうが良い）
    - [GA4 推奨イベント](https://support.google.com/firebase/answer/9267735?hl=ja&visit_id=638047798474482362-2900121823&rd=1)
- Bundle params
  - パラメータは 25 個まで付与することができます。
  - 第一引数のイベント名が同じイベントの場合は、同じパラメータを与える必要があります。
  - 定義済みの `FirebaseAnalytics.Param` を使用することが推奨されます。
    - [一覧](https://firebase.google.com/docs/reference/android/com/google/firebase/analytics/FirebaseAnalytics.Param?hl=ja)
  - パラメータに設定できる値の型は `String / long / double` です。
  - 第二引数に null を指定すると、そのイベントにはパラメータが存在しないことを意味します。
  - パラメータ名は 40 文字以内で指定します。
  - パラメータ名は、アルファベットで始まり、[アルファニューメリック](https://docs.oracle.com/javase/8/docs/api/java/lang/Character.html#isLetterOrDigit-int-)もしくは、アンダーバーのみで構成される必要があります。
  - パラメータ名は、 "firebase_", "google_", "ga_" で始まるものは予約語であるため登録できません。
  - パラメータ値が String 型である場合は 100 文字以内で定義する必要があります。


## adb コマンドでログに記録されたことを確認する

以下のコマンドは、 Android Studio の logcat にイベントを表示するので、  
イベントが送信されていることをすぐに確認できます。  
これには、自動的に記録されたイベントと手動で記録されたイベントの両方が含まれます。

```
adb shell setprop log.tag.FA VERBOSE
adb shell setprop log.tag.FA-SVC VERBOSE
adb logcat -v time -s FA FA-SVC
```


## Firebase コンソールで確認する（製品版リリース後）

Firebase コンソールでは、イベント発生直後には、まだ確認ができません。  
ドキュメントによると、 1 日を通して定期的に画面が更新されるとのことです。  
すぐに確認したい場合には、上記の adb コマンドを使用した方法で確認をします。

Firebase コンソールで確認する場合は、以下の 2 つのページで確認ができます。

【方法 1】  
分析 - Dashboard - 「イベント数」のボードから「イベントを表示」をクリック  
こちらはグラフ等でグラフィカルに表示されます。

【方法 2】  
分析 - Events  
こちらは、一覧で表示されます。


## Firebase コンソールで確認する（製品版リリース前）

製品版リリース前に Firebase コンソールでログの送信を確認するには、  
`DebugView` を使用します。

以下の adb コマンドを実行し、 (アプリをインストールした) デバイスをデバッグモードにします。

```java
// ドキュメントには PACKAGE_NAME と記載してあるが、
// アプリケーション ID かもしれないため、後日確認したほうが良いです。
// この設定をしてから 1 分くらい経過しないと、 Firebase コンソール上で、デバイスが  
// 検出できなかった。もしくは、真ん中の列の 「秒ストリーム」 が動き出してから
// 以下のコマンドを実行する必要があるのかもしれない。ちょっとタイムラグがあるのでよくわからない...。
adb shell setprop debug.firebase.analytics.app PACKAGE_NAME
```

デバッグモードは、以下のコマンドを使用して、明示的に解除するまで続きます。

```
adb shell setprop debug.firebase.analytics.app .none.
```

Firebase コンソール上では、 DebugView というページを表示して、ログが送信されたことを確認します。
DebugView は、 `分析 -> DebugView` から表示します。

**デバッグモードでログに記録されたイベントは Analytics データ全体から除外され、  
毎日の BigQuery エクスポートには含まれませんので、安心してデバッグ作業を行うことができます。**
