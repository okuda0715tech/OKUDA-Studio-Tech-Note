- [personal access token](#personal-access-token)
  - [personal access token (classic)](#personal-access-token-classic)
    - [作成方法](#作成方法)
    - [スコープの設定](#スコープの設定)
  - [fine-grained personal access token](#fine-grained-personal-access-token)
    - [作成方法](#作成方法-1)
  - [有効期限](#有効期限)


# personal access token

personal access token には、以下の二種類が存在しています。

- personal access token (classic)
- fine-grained personal access token

personal access token (classic) よりも、 fine-grained personal access token の使用が推奨されています。


## personal access token (classic)

Personal access tokens (classic) は安全性が低くなります。 ただし、現在、一部の機能は personal access tokens (classic) でしか機能しません。詳しくは [ドキュメント](https://docs.github.com/ja/authentication/keeping-your-account-and-data-secure/managing-your-personal-access-tokens#personal-access-tokens-classic) を参照してください。


### 作成方法

[こちら](https://docs.github.com/ja/authentication/keeping-your-account-and-data-secure/managing-your-personal-access-tokens#personal-access-token-classic-%E3%81%AE%E4%BD%9C%E6%88%90) を参照してください。


### スコープの設定

personal access token (classic) は、各 REST API エンドポイントにアクセスするために特定のスコープを必要とします。選択するスコープに関する全般的なガイダンスについては、 [OAuth アプリのスコープ](https://docs.github.com/ja/apps/oauth-apps/building-oauth-apps/scopes-for-oauth-apps#available-scopes) を参照してください。



## fine-grained personal access token

**注**: Fine-grained personal access token は現在ベータ版であり、変更される可能性があります。

fine-grained personal access token は、 personal access tokens (classic) に付与されるスコープよりも細かい制御が可能です。詳しくは、 [ドキュメント](https://docs.github.com/ja/authentication/keeping-your-account-and-data-secure/managing-your-personal-access-tokens#fine-grained-personal-access-token) を参照してください。

各エンドポイントが、 fine-grained personal access token に対応しているかどうかは、各エンドポイントのドキュメントを参照するか、 [きめ細かい個人用アクセストークンに使用できるエンドポイントの一覧](https://docs.github.com/ja/rest/authentication/endpoints-available-for-fine-grained-personal-access-tokens?apiVersion=2022-11-28) を参照してください。


### 作成方法

[こちら](https://docs.github.com/ja/authentication/keeping-your-account-and-data-secure/managing-your-personal-access-tokens#fine-grained-personal-access-token-%E3%81%AE%E4%BD%9C%E6%88%90) を参照してください。


## 有効期限

セキュリティ上の理由から、GitHub は過去 1 年間使われていない personal access token を自動的に削除します。 セキュリティを強化するため、personal access token には有効期限を設けることを強くお勧めします。




