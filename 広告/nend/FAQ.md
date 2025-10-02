<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [FAQ](#faq)
  - [SDK のログが出力されない場合](#sdk-のログが出力されない場合)
<!-- TOC END -->


# FAQ

## SDK のログが出力されない場合

デフォルトでは、ログ出力が OFF になっているため、以下の例を参考に設定を行ってください。

```java
import net.nend.android.NendAdLogger;

// 情報をログに出力します
NendAdLogger.setLogLevel(NendAdLogger.LogLevel.INFO);
```

上記以外のログレベルでログ出力する場合は、以下のドキュメントを参考にしてください。

[ログ出力](https://github.com/fan-ADN/nendSDK-Android/wiki/%E3%83%AD%E3%82%B0%E5%87%BA%E5%8A%9B)
