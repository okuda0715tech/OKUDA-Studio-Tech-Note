<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [OAuth1.0a_仕様](#oauth10a_仕様)
  - [概要](#概要)
  - [用語の定義](#用語の定義)
<!-- TOC END -->


# OAuth1.0a_仕様

## 概要

このドキュメントは OAuth1.0a の仕様理解のために、以下のサイトを勉強し、まとめたメモである。

[OAuth_net_core_1.0](https://oauth.net/core/1.0/)


## 用語の定義

- Service Provider:
  - A web application that allows access via OAuth.
  - Twitter API の場合は、Twitterサービスのことを示す。
- User:
  - An individual who has an account with the Service Provider.
  - アプリ、サービスにログインを行うユーザー自身を示す。
- Consumer:
  - A website or application that uses OAuth to access the Service Provider on behalf of the User.
  - Twitter API の場合は、 API を使用する第三者アプリのことを示す。
- Protected Resource(s):
  - Data controlled by the Service Provider, which the Consumer can access through authentication.
  - 認証を行うことで第三者アプリが使用することのできるデータを示す。
- Consumer Developer:
  - An individual or organization that implements a Consumer.
- Consumer Key:
  - A value used by the Consumer to identify itself to the Service Provider.
- Consumer Secret:
  - A secret used by the Consumer to establish ownership of the Consumer Key.
- Request Token:
  - A value used by the Consumer to obtain authorization from the User, and exchanged for an Access Token.
  - コンシューマーがユーザーから承認を得るために使用する値のこと。
- Access Token:
  - A value used by the Consumer to gain access to the Protected Resources on behalf of the User, instead of using the User’s Service Provider credentials.
  - コンシューマーがユーザーのサービスプロバイダのIDの代わりに使用するIDのこと。
- Token Secret:
  - A secret used by the Consumer to establish ownership of a given Token.
  - コンシューマーがユーザーのサービスプロバイダのパスワードの代わりに使用するパスワードのこと。
- OAuth Protocol Parameters:
  - Parameters with names beginning with oauth_.
- Nonce
  - ノンスは、暗号通信で用いられる、使い捨てのランダムな値のことである。
  - ノンスはたいてい、認証の過程で使われ、リプレイ攻撃を行えないようにする働きを担っている。
  - OAuth1.0a の場合、同じタイムスタンプのリクエストが複数存在する場合、それらのリクエスト内で一意となるランダムな値を Consumer で生成すれば良い。
