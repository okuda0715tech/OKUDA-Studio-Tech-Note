<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [Google Advertising ID を取得する](#google-advertising-id-を取得する)
  - [取得手順](#取得手順)
<!-- TOC END -->


# Google Advertising ID を取得する

## 取得手順

アプリのログ出力を有効化します。

```java
import net.nend.android.NendAdLogger;

// 情報をログに出力します
// デフォルトでは、ログ出力が OFF になっているため、この記述が必要です。
NendAdLogger.setLogLevel(NendAdLogger.LogLevel.INFO);
```

動画リワード広告のインスタンスを生成して、 adb ログを確認します。  
「Google Advertising ID = 」で出力された値を確認してください。

出力された値を管理画面へ登録します。
