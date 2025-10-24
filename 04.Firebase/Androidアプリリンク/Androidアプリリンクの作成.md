<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [Androidアプリリンクの作成](#androidアプリリンクの作成)
  - [assetlinks.json を生成する](#assetlinksjson-を生成する)
<!-- TOC END -->


# Androidアプリリンクの作成

## assetlinks.json を生成する

以下のウェブサイトを使用すると、必要な情報を入力してボタンを押すだけで、必要な Json 形式で  
文字列を生成してくれる。

assetlinks.json 生成ツール  
[Statement List Generator and Tester](https://developers.google.com/digital-asset-links/tools/generator?hl=ja)

「 Test Statement 」 ボタンを押ししたときに、以下のエラーが出力される場合は、単に  
「 assetlinks.json 」 ファイルをまだ配置していないだけの可能性があるため、配置してから再確認すると良い。

```
No app deep linking permission found for {App package name} at {Hosting site domain}.
```
