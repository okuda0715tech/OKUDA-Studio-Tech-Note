# JobIntentServiceクラス

## 概要

IntentService はサービスであるため、バックグラウンドサービスに対する`Android 8.0`以降の新しい制限事項の対象になります。
そのため、`IntentService`に依存する多くのアプリは、`Android 8.0`以降を対象にすると正常に動作しません。
こうした理由から、`Android Support Library 26.0.0`に新しい`JobIntentService`クラスが導入されました。このクラスは`IntentService`と同じ機能を提供しますが、`Android 8.0`以降で実行されるときにサービスではなくジョブを使用します。


## 使用方法

### JobIntentServieの起動方法

**RSSPullService.java**

```java
public class RSSPullService extends IntentService {
    @Override
    protected void onHandleWork(Intent workIntent) {
        // Gets data from the incoming Intent
        String dataString = workIntent.getDataString();
        ...
        // Do work here, based on the contents of dataString
        ...
    }
}
```

**SampleActivity.java**

```java
// Intentにデータを格納
serviceIntent = new Intent();
serviceIntent.putExtra("download_url", dataUrl));

private static final int RSS_JOB_ID = 1000;
// JobIntentServiceを起動(RSSPullServiceがJobIntentServieを継承したクラス)
RSSPullService.enqueueWork(getContext(), RSSPullService.class, RSS_JOB_ID, serviceIntent);
```




