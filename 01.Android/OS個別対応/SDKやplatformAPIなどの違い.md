<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [SDKやplatformAPIなどの違い](#sdkやplatformapiなとの違い)

<!-- /TOC -->


# SDKやplatformAPIなどの違い

- platform
  - OSのことを指します。
  - デフォルトでインストールされているPlayStore,メール,写真アプリなどを含む場合もあります。
- platformAPI
  - platformAPIのことを指す
  - Android OSに含まれる機能、デバイスに付属する機器（カメラなど）などを操作するためのAPIを提供する
  - つまり、OSに最初から入っているライブラリを操作するためのAPIと言える
- SDK
  - アプリ開発に必要な様々な機能やソフトウェア部品を含む
    - コンパイル、デバッガ、デバイスドライバ、ADBなど
      - これらは、SDK Manager上では、「SDK Tools」という分類にまとめられている。
    - ライブラリ（platformAPI）、エミュレーター
      - 各Androidバージョン（APIレベル）の動作を模した環境を実現するためにAndroidバージョンごとにパッケージ化されている。
      - これらは、SDK Manager上では、「SDK Platforms」という分類にまとめられている。
      - 開発中に実際のデバイス内のplatformAPIを参照することは困難であるため、そのコピーを参照できるようにしたものである。
  - platformに含まれる（platformを含む？）
  - Android Studioに含まれている
- Androidx
  - platformAPIだけでは、APIが初歩的であり開発に時間がかかりすぎてしまうため、便利なラッパーライブラリを作ってまとめたものがAndroidxである。
    - 例えば、新しいOSが登場した時、古いOSでも新しいOSと同じ動作をするようにバックポートする役割がある。
      - Androidxでは、新しいAPIのラッパーメソッド（類似のメソッド？）を作成することで、バックポートを実現している。
    - デバイスには入っていない外部のライブラリである。
