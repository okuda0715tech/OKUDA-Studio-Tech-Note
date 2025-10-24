<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [Standard v1.1 の RateLimit](#standard-v11-の-ratelimit)
  - [引用元](#引用元)
  - [OAuth 2.0 Bearer Token で認証した場合はアプリごとに RateLimit が決定する](#oauth-20-bearer-token-で認証した場合はアプリごとに-ratelimit-が決定する)
  - [OAuth 1.0a User Context で認証した場合は Twitter アカウントごとに RateLimit が決定する](#oauth-10a-user-context-で認証した場合は-twitter-アカウントごとに-ratelimit-が決定する)
  - [POSTメソッドのRateLimit](#postメソッドのratelimit)
<!-- TOC END -->


# Standard v1.1 の RateLimit

## 引用元

[Rate limits: Standard v1.1](https://developer.twitter.com/en/docs/twitter-api/v1/rate-limits)


## OAuth 2.0 Bearer Token で認証した場合はアプリごとに RateLimit が決定する

`OAuth 2.0 Bearer Token` で認証した場合はアプリごとに RateLimit が決定する。  
Twitter のユーザーごとの制限は存在しないため、表の "ユーザー毎" の部分は無視してよい？


## OAuth 1.0a User Context で認証した場合は Twitter アカウントごとに RateLimit が決定する

`OAuth 1.0a User Context` で認証した場合は Twitter アカウントごとに RateLimit が決定する。  
アプリごとの制限は存在しないため、表の "アプリ毎" の部分は無視してよい？

と思ったのだが、Twitter のコミュニティーフォーラムを確認したところ、アプリごとの制限もかかっているようだ。  
アプリユーザーが多くなった場合、 Twitter に申請を行えば、このアプリごとの制限は削除されるようだ。

以下、ある問い合わせに関してのTwitter社員の返信

```
andypiper / Twitter Staff
Sep '20

Your rate limits are per app.

You really should only have a single developer account / Twitter account,
and you could have multiple (by default, up to 10) apps registered to that account.
Each of those APPS has the rate limit. You can authenticate multiple users
to those apps, but by default, the APP will be capped.
If your app is posting a lot - for example for many many users - you can apply
to have the APP level cap removed.
```


## POSTメソッドのRateLimit

Endpoint                        | 計測時間 | ユーザー毎の制限 | アプリ毎の制限
--------------------------------|----------|------------------|---------------
POST statuses/update            | 3 hours* | 300*             | 300*
POST statuses/retweet/:id       | 3 hours* | 300*             | 300*
POST favorites/create           | 24 hours | 1000             | 1000
POST friendships/create         | 24 hours | 400              | 1000
POST direct_messages/events/new | 24 hours | 1000             | 15000

`POST statuses/update` と `POST statuses/retweet/:id` のリクエストは合算して  
3 時間で 300 リクエストまでです。
