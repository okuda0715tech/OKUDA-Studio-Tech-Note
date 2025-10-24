<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [Service が生成されない場合のトラブルシューティング](#service-が生成されない場合のトラブルシューティング)
  - [Manifest ファイルに service タグが設定されているか](#manifest-ファイルに-service-タグが設定されているか)
<!-- TOC END -->


# Service が生成されない場合のトラブルシューティング

Service が生成されない場合は、以下のポイントを確認してください。


## Manifest ファイルに service タグが設定されているか

`<service>` タグが設定されていない場合は、 `Service` を生成することができません。  
`Service` の `onCreate()` メソッドすら呼ばれていない場合は、これが原因である可能性があります。

以下はサンプルです。

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

  <application>

    <service
        android:name=".service.TweetExeService"
        android:exported="false" />

  </application>

</manifest>
```
