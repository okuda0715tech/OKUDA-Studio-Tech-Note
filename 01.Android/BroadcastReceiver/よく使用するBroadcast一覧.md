<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [よく使用する Broadcast 一覧](#よく使用する-broadcast-一覧)
  - [一覧](#一覧)
<!-- TOC END -->


# よく使用する Broadcast 一覧

## 一覧

| action                                    | 受信タイミング                                                             |
|-------------------------------------------|----------------------------------------------------------------------------|
| android.intent.action.MY_PACKAGE_REPLACED | 自分自身のアプリがバージョンアップされたとき                               |
| android.intent.action.PACKAGE_REPLACED    | デバイス内のいずれかのアプリがバージョンアップされたとき                   |
| android.intent.action.PACKAGE_ADDED       | デバイスに今まで存在しなかったパッケージ名のアプリがインストールされたとき |


## 詳細

| アクション    | OS のみが送信できる |
|---------------|---------------------|
| PACKAGE_ADDED | Y                   |
