<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [HMACとは](#hmacとは)
  - [概要](#概要)
  - [HMACの定義](#hmacの定義)
  - [HMACのMAC値の長さ](#hmacのmac値の長さ)
  - [HMACの役割](#hmacの役割)
  - [HMACの暗号強度](#hmacの暗号強度)
<!-- TOC END -->


# HMACとは

## 概要

`HMAC` とは、 `Hash-based Message Authentication Code` または、  
`Keyed Hashing for Message Authentication Code` の略である。

HMAC は、 MAC アルゴリズムの具体的なアルゴリズムの一つである。


## HMACの定義

HMAC アルゴリズムを用いて MAC 値を算出する計算式は以下の通りである。

<img src="./HMAC_definition.svg" width="400">

```
h : 繰り返し型ハッシュ関数  
K : 秘密鍵  
m : メッセージ  
opad : 定数パディング（ 0 埋めみたいなイメージ）  
ipad : 定数パディング（ 0 埋めみたいなイメージ）  
|| : ビット列の連結  
丸で囲まれた+ : 排他的論理和
```

この計算式からもわかる通り、 HMAC 自身もまだ抽象的な計算式にすぎず、実際にどのハッシュ関数を  
使用するかによって、 `HMAC-SHA256` や `HMAC-MD5` という呼ばれ方をする。


## HMACのMAC値の長さ

HMACのMAC値の長さは、利用されるハッシュ関数の出力長と等しい。  
例えばHMAC-SHA256であればタグは256ビットである。


## HMACの役割

基本的には、 MAC と同様の役割として、「データが改ざんされているかされていないか」の証明を行います。  
MAC 値の算出に公開鍵ではなく、秘密鍵を使用することから、「秘密鍵を持っているのは本人のみ」  
という理屈で、本人確認にもなると書いてあるサイトもありました...。本人確認として使用して  
良いかどうかはよく検討した方がよいかもしれない。


## HMACの暗号強度

HMAC の暗号強度は、使用されているハッシュ関数の特性に依存します。
