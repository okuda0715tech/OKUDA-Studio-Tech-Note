<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [ParselableとSerializable](#parselableとserializable)
  - [Parselableの概要](#parselableの概要)
  - [Serializable の概要](#serializable-の概要)
    - [バイトストリームの特徴](#バイトストリームの特徴)
  - [ParselableとSerializableの違い](#parselableとserializableの違い)

<!-- /TOC -->


# ParselableとSerializable

## Parselableの概要

- Parcelという単語は小包という意味。データをまとめて渡すようなものをイメージ
- Serializableでは同一のアプリケーションでの明示的なIntentであれば問題ない。しかし暗黙的なIntentの場合，自分が作成したActivityがアクションを受け取るとは限らない。なのでSerializeしたオブジェクトが復元できる保証はない。
- Parcelableを使用することで，アプリ間でオブジェクトのようなものの受け渡しをすることができる。アプリ間でParcelableクラスの実体が異なっても構わない。


## Serializable の概要

Serializable は、オブジェクトをバイトストリームに変換可能である旨を示すマーカーインターフェースです。バイトストリーム (Byte Stream) とは、データを 1 バイトずつ連続して扱うデータストリームのことを指します。コンピュータはデータをバイナリ形式（0 と 1 の列）で扱うため、バイトストリームはデータをバイト単位でシーケンシャルに読み書きするための方法です。


### バイトストリームの特徴

- **シーケンシャル処理** : データは連続したバイトの列として処理されるため、順番に読み込むか、書き込むかを行います。
- **汎用性** : テキストや画像、音声など、あらゆる種類のデータを扱うことができます。
- **低レベル処理** : バイトストリームはデータをバイト単位で処理するため、通常はファイル操作やネットワーク通信などの低レベルな入出力操作に使用されます。


## ParselableとSerializableの違い

- Parselableの特徴
  - Android独自のクラス
  - プロセス間（アプリ間）のデータの受け渡しが可能
  - プロセス間（アプリ間）のデータの受け渡しが目的
- Serializableの特徴
  - Java共通のクラス
  - アプリ間のデータの受け渡しが不可能
  - データの永続化が目的

