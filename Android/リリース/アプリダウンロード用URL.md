<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [アプリダウンロード用URL](#アプリダウンロード用url)
<!-- TOC END -->


# アプリダウンロード用URL

アプリダウンロード用URLは、以下のフォーマットになっています。

```
https://play.google.com/store/apps/details?id=XXXXX
```

最後の「XXXXX」には、アプリケーション ID が入ります。  
アプリケーション ID は、 build.gradle 内の applicationId で確認できます。

このURLは、内部テスト版、製品版で共通です。（試していませんが、オープンテスト版、  
クローズテスト版でも共通だと思われます。）
