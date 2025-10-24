<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [Intentを使用したプロバイダへのアクセス](#intentを使用したプロバイダへのアクセス)
  - [概要](#概要)
  - [ContentResolverを使用した方法のメリット](#contentresolverを使用した方法のメリット)
  - [Intentを使用した方法のメリット](#intentを使用した方法のメリット)
<!-- TOC END -->


# Intentを使用したプロバイダへのアクセス

## 概要

ある「アプリA」が他の「アプリB」のコンテンツプロバイダにアクセスする方法は、二通りあります。  
一つは、 `ContentResolver` のメソッドを呼び出し、直接プロバイダへアクセスする方法です。  
もう一つは、 `Intent` を使用し、間接的にプロバイダへアクセスする方法です。

## ContentResolverを使用した方法のメリット

ユーザーに独自のUIを提供することができる。


## Intentを使用した方法のメリット

独自のUIを提供しないため、入力不可のデータが入力される恐れがない。  
実装が比較的簡単である。
