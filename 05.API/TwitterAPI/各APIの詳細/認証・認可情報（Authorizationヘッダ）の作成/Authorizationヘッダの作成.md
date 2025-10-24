<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [Authorization ヘッダの作成](#authorization-ヘッダの作成)
  - [Authorization を使用する目的](#authorization-を使用する目的)
  - [例](#例)
  - [パラメータの収集](#パラメータの収集)
    - [oauth\_consumer\_key](#oauth_consumer_key)
    - [oauth\_nonce](#oauth_nonce)
    - [oauth\_signature](#oauth_signature)
    - [oauth\_signature\_method](#oauth_signature_method)
    - [oauth\_timestamp](#oauth_timestamp)
    - [oauth\_token](#oauth_token)
    - [oauth\_version](#oauth_version)
  - [Authorization ヘッダ文字列の構築](#authorization-ヘッダ文字列の構築)
<!-- TOC END -->


# Authorization ヘッダの作成

## Authorization を使用する目的

Authorization ヘッダを使用することで以下の 4 点を明確にすることができます。

- どのアプリケーションがリクエストを行っているのか
- どのユーザーに代わってリクエストが投稿されているのか
- ユーザーに代わって投稿することをユーザーがアプリケーションに許可したかどうか
- 転送中に第三者によってリクエストが改ざんされたかどうか

Twitter API を利用する上で、どの API エンドポイントにアクセスするときでも、  
上記の 4 点を明確にするために、 Authorization ヘッダを付与する必要があります。


## 例

**Authorization ヘッダ付与前のリクエスト例**

```
POST /1.1/statuses/update.json?include_entities=true HTTP/1.1
Accept: */*
Connection: close
User-Agent:OAuth gem v0.4.4
Content-Type: application/x-www-form-urlencoded
Content-Length:76
Host: api.twitter.com

status=Hello%20Ladies%20%2b%20Gentlemen%2c%20a%20signed%20OAuth%20request%21
```

**Authorization ヘッダ付与後のリクエスト例**

通常、 Authorization ヘッダーは 1 行にする必要があります。また、インデントは設定しません。  
しかし、ここでは読みやすいように折り返しやインデントを行っています。

```
POST /1.1/statuses/update.json?include_entities=true HTTP/1.1
Accept: */*
Connection: close
User-Agent:OAuth gem v0.4.4
Content-Type: application/x-www-form-urlencoded
Authorization:
      OAuth oauth_consumer_key="xxxxxxxxxxxxxxxxxxxxxx",
            oauth_nonce="xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
            oauth_signature="xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
            oauth_signature_method="HMAC-SHA1",
            oauth_timestamp="1318622958",
            oauth_token="999999999-xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
            oauth_version="1.0"
Content-Length:76
Host: api.twitter.com

status=Hello%20Ladies%20%2b%20Gentlemen%2c%20a%20signed%20OAuth%20request%21
```


## パラメータの収集

ヘッダーには7つのキーと値のペアが含まれており、キーはすべて文字列 「 oauth_ 」 で始まっています。  
それぞれの値は以下のように生成されます。


### oauth_consumer_key

[ 役割 ]

`oauth_consumer_key` は、どのアプリケーションがリクエストを行っているかを識別します。

[ 取得方法 ]

この値は、開発者ポータルのTwitterアプリの設定ページから取得できます。


### oauth_nonce

[ 役割 ]

`oauth_nonce` は、リプレイ攻撃を無効化する役割があります。  
同じタイムスタンプのリクエストが複数存在する場合、 `oauth_nonce` が同一のリクエストは無効化されます。


[ 取得方法 ]

リクエストごとにランダムな値を生成します。  
値は、 32 バイトのバイナリデータを base64 でエンコードしたものを `oauth_nonce` にセットします。


### oauth_signature

[ 役割 ]

`oauth_signature` の役割は以下の通りです。

- リクエストが転送中に変更されていないことを Twitter が確認すること
- リクエストを送信しているアプリケーションを確認すること
- アプリケーションがユーザーのアカウントとやりとりする権限を持っていることを確認すること


[ 取得方法 ]

別紙 ( create_OAuth_signature.pu ) を参照してください。


### oauth_signature_method

[ 役割 ]

署名の作成で使用するハッシュアルゴリズムを明確にする役割があります。


[ 取得方法 ]

固定値 `HMAC-SHA1` を指定します。


### oauth_timestamp

[ 役割 ]

`oauth_timestamp` は、リクエストがいつ作成されたかを示す役割があります。  
Twitterでは作成された時間があまりにも古いリクエストは拒否されます。


[ 取得方法 ]

リクエストを作成した時の Unix 時間をセットします。  
Unix 時間とは、 「 1970 年 1 月 1 日 午前 0 時 ( UTC ) 」 からの経過秒数で表された現在の時刻です。  
例えば、 「 2021 年 3 月 8 日 午前 0 時」 を Unix 時間で表すと、 「 1615129200 」 です。

UTC ( Coordinated Universal Time) は、日本語で 「協定世界時」 と呼ぶ。  
世界各地の時刻を算出する基準となる時刻である。  
イギリスの標準時刻である GMT ( Greenwich Mean Time / グリニッジ標準時) と実質的に同じ時刻を指す。

【補足】  
Coordinated Universal Time なら、略称が CUT となりそうだが、 UTC が正確な略称である。  
フランス語では TUC 、イタリア語では TCU となったりするため、国際電気通信連合が UTC と呼ぶことを定めた。


### oauth_token

[ 役割 ]

`oauth_token` は、多くの場合、アクセストークンが格納される。  
アクセストークンをまだ取得していない場合は、アクセストークンを取得するためのリクエストトークンが格納される。  
リクエストトークンをまだ取得していない場合は、 `Authorization` ヘッダに `oauth_token` フィールドを  
を含める必要はない。


[ 取得方法 ]

別紙 (3LeggedOAuth.pu) を参照してください。  
[公式ドキュメント英語版](https://developer.twitter.com/en/docs/authentication/oauth-1-0a/obtaining-user-access-tokens)  
[公式ドキュメント日本語版](https://developer.twitter.com/ja/docs/authentication/oauth-1-0a/obtaining-user-access-tokens)


### oauth_version

[ 役割 ]

OAuth 認証のバージョンを明確にする役割があります。

[ 取得方法 ]

Twitter API に送信されるすべてのリクエストの `oauth_version` は、常に " 1.0 " を指定します。


## Authorization ヘッダ文字列の構築

ヘッダー文字列を構築するために、 "DST" という名前の変数に書き込むと想定します。

1. 文字列 「OAuth 」 (最後のスペースを含む) を DST に追加します。
2. 上述の 7 つのパラメーターの各キー / 値のペアについて
   1. キーをパーセントエンコードして DST に追加します。
   2. 等号文字 「=」 を DST に追加します。
   3. 二重引用符 「”」 を DST に追加します。
   4. 値をパーセントエンコードして DST に追加します。
   5. 二重引用符 「”」 を DST に追加します。
   6. キーと値のペアが残っている場合は、コンマ 「,」 とスペース 「 」 を DST に追加します。

パーセントエンコーディングの仕様に注意してください。  
例えば、 `ATxmUIj07cWHq44gCs1OSKk/jLY=` の `oauth_signature` 値は、  
`ATxmUIj07cWHq44gCs1OSKk%2FjLY%3D` とエンコードされる必要があります。
