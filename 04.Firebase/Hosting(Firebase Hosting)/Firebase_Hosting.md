<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [Firebase Hosting](#firebase-hosting)
  - [Hosting の利用の流れ](#hosting-の利用の流れ)
  - [firebase init hosting のオプション](#firebase-init-hosting-のオプション)
    - [? What do you want to use as your public directory?](#-what-do-you-want-to-use-as-your-public-directory)
    - [? Configure as a single-page app (rewrite all urls to /index.html)?](#-configure-as-a-single-page-app-rewrite-all-urls-to-indexhtml)
  - [firebase.json の設定](#firebasejson-の設定)
    - [参考](#参考)
    - [rewrites 属性](#rewrites-属性)
    - [public 属性](#public-属性)
    - [Hosting のレスポンスの優先順位](#hosting-のレスポンスの優先順位)
  - [ローカルサーバでエミュレートする](#ローカルサーバでエミュレートする)
    - [参考にしたサイト](#参考にしたサイト)
  - [Firebaseサーバにデプロイする (ファイルのアップロードもこれで行う)](#firebaseサーバにデプロイする-ファイルのアップロードもこれで行う)
  - [Realtime Database 連携](#realtime-database-連携)
    - [参考にしたページ](#参考にしたページ)
    - [Firebase SDK をインポートする](#firebase-sdk-をインポートする)
    - [変数(config)に、アプリの認証情報を埋め込む。](#変数configにアプリの認証情報を埋め込む)
    - [DBのデータを取得する](#dbのデータを取得する)
<!-- TOC END -->


# Firebase Hosting

## Hosting の利用の流れ

[Firebase Hosting を使ってみる](https://firebase.google.com/docs/hosting/quickstart?hl=ja)


## firebase init hosting のオプション

### ? What do you want to use as your public directory?

- public ディレクトリとしてどんな名前のディレクトリを作成するかの指定
- public ディレクトリとは、 index.html をはじめ、コンテンツファイルを配置するルートディレクトリ

### ? Configure as a single-page app (rewrite all urls to /index.html)?

- YES を選ぶと、firebase.json にリライト設定が追記される
- NO を選ぶと、404.html が作成される


## firebase.json の設定

### 参考

[Hosting 動作をカスタマイズする](https://firebase.google.com/docs/hosting/url-redirects-rewrites?hl=ja)

### rewrites 属性

`source`のパターンにマッチするファイルが存在しない場合に、`destination`に指定したサイトにリダイレクトする機能である。

主に、ユーザが指定したURLに対応するファイルが存在しない場合に、index.htmlに飛ばす為に使用される。

**サンプル**

下記の例では、フォルダにファイルが存在しない場合、 /index.html のコンテンツを返します。

**firebase.json**

```json
"hosting": {
  "rewrites": [ {
    "source": "**",
    "destination": "/index.html"
  } ]
}
```

### public 属性

- public 属性は、必須パラメータです。
- public 属性には、Firebase Hosting にデプロイするディレクトリを指定します。
- デフォルト値は public という名前のディレクトリですが、プロジェクト ディレクトリ内に存在している任意のディレクトリのパスを指定できます。

```json
"hosting": {
  "public": "dist/app"
}
```

```json
"hosting": {
  "public": "public"
}
```

### Hosting のレスポンスの優先順位

Firebase Hosting 構成オプションが競合する場合があります。競合が発生した場合、Hosting からのレスポンスは次の優先順位に従って決定されます。

1. /__/* パスセグメントで始まる予約済みの名前空間
2. リダイレクトの構成
3. 正確に一致する静的コンテンツ
4. リライトの構成
5. カスタムの 404 ページ
6. デフォルトの 404 ページ


## ローカルサーバでエミュレートする

以下のコマンドをターミナルから実行するとローカルサーバのURLが発行される為、ブラウザからアクセスするとWebページが表示される。

```
firebase serve --only hosting
```

プロジェクト全体をデプロイしたい場合は、以下のコマンドを実行する。

```
firebase serve
```

個別にデプロイするものを指定したい場合は、デプロイしたい機能をオプションで追加する。以下に例を示す。

```
firebase serve --only functions,hosting
```

### 参考にしたサイト

[ローカルでテストしてサイトにデプロイする](https://firebase.google.com/docs/hosting/deploying?hl=ja)


## Firebaseサーバにデプロイする (ファイルのアップロードもこれで行う)

プロジェクトフォルダ直下 ( app フォルダの上位フォルダ内) に移動し、  
以下のコマンドでFirebaseサーバにデプロイします。  
もしくは、 `E:\FirebaseProject\_StoreReleasedProject\RestaurantSeatsManager_Firebase\public`  
このあたりで以下のコマンドを実行します。  
どのディレクトリ内でコマンドを実行してもデプロイできるような気もする。  
ログインしているアカウント内のすべての Hosting サーバーをデプロイしているのかな？

```
firebase deploy
```

Hosting サービスのみデプロイする場合

```
firebase deploy --only hosting
```

デプロイにコメントする場合には`-m`オプションを付与します。

```
firebase deploy -m "{デプロイコメント}"
```

デプロイに成功すると、 Hosting しているサイトにアクセスする URL がターミナル上に表示されるので、  
その URL にブラウザからアクセスします。


## Realtime Database 連携

**サンプル**

以下のサンプルは、 Realtime Database をリアルタイムに参照し、 HostingしているWebサイトに変更がある都度、最新のDBの値をWebページに表示する

**index.html**

```html
<!DOCTYPE html>
<html>
  <head>
    <meat charset="utf-8" />
    <title>Getting Started with Firebase on the Web</title>
  </head>
  <body>
    <h1 id="bigOne"></h1>
    <script src="https://www.gstatic.com/firebasejs/4.6.0/firebase-app.js"></script>
    <script src="https://www.gstatic.com/firebasejs/4.6.0/firebase-messaging.js"></script>
    <script src="https://www.gstatic.com/firebasejs/4.6.0/firebase.js"></script>

    <script type="text/javascript">
     var config = {
       apiKey: "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
       authDomain: "xxxxxxxxxxxx.firebaseapp.com",
       databaseURL: "https://xxxxxxxxxxxx.firebaseio.com",
       projectId: "xxxxxxxxxxxx",
       storageBucket: "xxxxxxxxxxxx.appspot.com",
       // messagingSenderId: "sender-id",
     };

     firebase.initializeApp(config);

     let bigOne = document.querySelector('#bigOne');
     let dbRef = firebase.database().ref('users');
     dbRef.on('value', function(snapshot){
       bigOne.innerText = snapshot.child('123').child('email').val();
     });
    </script>
  </body>
</html>
```


### 参考にしたページ

[Firebase を JavaScript プロジェクトに追加する](https://firebase.google.com/docs/web/setup)

[ウェブでのデータの取得](https://firebase.google.com/docs/database/web/retrieve-data?hl=ja)


### Firebase SDK をインポートする

htmlファイルの`<body>`タグ内に以下のように記載する。

```javascript
<script src="https://〜〜〜"></script>
```

Realtime Database に連携するには以下の記載を行う。

```javascript
// firebaseのコアSDK　このimportは常に必要であり、他の全てのimportの中で先頭に記載する必要がある。
<script src="https://www.gstatic.com/firebasejs/5.10.1/firebase-app.js"></script>
// Realtime Database用のSDK
<script src="https://www.gstatic.com/firebasejs/5.10.1/firebase-database.js"></script>
```

その他の機能のSDKを使用したい場合には、以下のページにURLが記載されているので、そのURLから取り込む。

[JavaScript アプリ用に入手可能な Firebase SDK（CDN から暗黙的に取得）](https://firebase.google.com/docs/web/setup#libraries_CDN)


### 変数(config)に、アプリの認証情報を埋め込む。

認証情報は、`google-services.json`から取得する。

FirebaseにAndroidアプリのプロジェクトを登録済みの場合は、以下の手順でFirebaseから`google-services.json`を取得することができる。

Project Overview -> 上から三行目あたりのプロジェクト名 -> 設定ボタン -> 最新の構成ファイル(google-services.json)をダウンロード

`google-services.json`から取得したデータを以下のフォーマットに埋め込んでいく。
不要なパラメータがあれば`//`でコメントアウトすればOK。

**サンプル**

```javascript
var config = {
  apiKey: "api-key",
  authDomain: "project-id.firebaseapp.com",
  databaseURL: "https://project-id.firebaseio.com",
  projectId: "project-id",
  storageBucket: "project-id.appspot.com",
  messagingSenderId: "sender-id",
};

firebase.initializeApp(config);
```


### DBのデータを取得する

**サンプル**

```JavaScript
// Tableオブジェクトの取得
let inTable = firebase.database().ref('check_in_users');
// コールバック関数の定義
inTable.on('child_added', function(snapshot){
  var key = snapshot.key;
  var userName = snapshot.val().mUserName;
});
```

**コールバック関数の種類**

`on()`関数の第一引数に渡す値によって、コールバック関数が呼ばれる契機が決まる。

on() 関数の第一引数 | 呼ばれる契機
--------------------|---------------------------------------------
child_added         | ref('XXX')のXXXに子が追加された時
child_changed       | 参照している子もしくはその子孫が変更された時
child_removed       | 参照している直接の子が削除された時


snapshotには、追加された子オブジェクトが入っている。
`snapshot.key`で子要素のキーを取得
`snapshot.val()`で子要素のバリューを取得
`snapshot.val().フィールド名`で子要素の対象フィールドのバリューを取得

**サンプルJson**

```json
{
  "check_in_users" : {
    "00001" : {
      "mActionDateTime" : "2019年07月30日 火曜日 18時42分05秒",
      "mUserId" : "00001",
      "mUserName" : "ICHIRO"
    },
    "00002" : {
      "mActionDateTime" : "2019年07月30日 火曜日 18時42分05秒",
      "mUserId" : "00002",
      "mUserName" : "JIRO"
    },
    "00003" : {
      "mActionDateTime" : "2019年07月30日 火曜日 18時42分05秒",
      "mUserId" : "00003",
      "mUserName" : "SABURO"
    }
  }
}
```

上記のサンプルコード、サンプルJsonを使用した場合、
`snapshot`には、`"00001" : { 〜 }`や`"00002" : { 〜 }`が格納される。
`snapshot.key`には、`00001`や`00002`が格納される。
`snapshot.val()`には、`"00001" : { 〜 }`や`"00002" : { 〜 }`の`〜`部分が格納される。
`snapshot.val().mUserName`には、`"ICHIRO"`や`"JIRO"`が格納される。
