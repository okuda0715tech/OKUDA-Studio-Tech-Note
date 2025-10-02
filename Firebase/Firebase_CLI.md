<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [Firebase CLI](#firebase-cli)
	- [Firebase CIL とは](#firebase-cil-とは)
	- [npm とは](#npm-とは)
	- [Node.js とは](#nodejs-とは)
	- [インストール方法](#インストール方法)
		- [Mac](#mac)
		- [Windows](#windows)
	- [コマンド一覧](#コマンド一覧)
	- [公式ドキュメント](#公式ドキュメント)
<!-- TOC END -->


# Firebase CLI

## Firebase CIL とは

Firebase CIL とは、 Firebase Command Line Interface の略です。  
コマンドから Firebase を操作するために必要です。

インストールすると Windows のコマンドプロンプトからも `firebase` コマンドが使えるようになりますし、  
`firebase-tools-instant-win.exe` ファイルを立ち上げて、そのプロンプトからも `firebase`  
コマンドが使えるようになります。


## npm とは

Node.js をインストールすると一緒にインストールされるものに 「 npm ( Node Package Manager) 」  
があります。 npm はパッケージ (プログラムとその関連ファイルをまとめたもの) を管理するツールで、  
便利な機能をもつ様々なパッケージを手軽に Node.js に導入することができます。


## Node.js とは

Node.js は、 JavaScript を実行する実行環境です。  
Node.js を使用することにより、 JavaScript 単独ではできなかった  
「 JavaScript で OS の機能を使用する」 ということができるようになります。


## インストール方法

### Mac

未調査。


### Windows

以下の公式ドキュメントの通りにインストールすると、 `firebase` コマンドが認識されなかった。

[Firebase CLI リファレンス](https://firebase.google.com/docs/cli/?hl=ja)

以下のサイトの通りにインストールしたら、`firebase`コマンドが認識された。

[Firebase プロジェクト作成 Windows10にNode.js・Firebase-tools インストール・ログイン](https://densi.biz/firebase)

おそらく、`npm`のバージョンが古かったから通らず、2つ目のやり方だと、最新の`npm`がインストールされて通ったのだと思われる。


## コマンド一覧

| コマンド                       | 用途                                                              |
|--------------------------------|-------------------------------------------------------------------|
| firebase login                 | Google アカウントで Firebase にログインする                       |
| firebase list                  | Firebase プロジェクトの一覧を表示する / ログイン状態の確認 (※ 1 ) |
| firebase logout                | Firebase からログアウトする                                       |
| firebase deploy --only hosting | Hosting のみデプロイする                                          |

(※1) ログイン済みの場合、プロジェクトの一覧が表示される



## 公式ドキュメント

[Firebase CLI リファレンス](https://firebase.google.com/docs/cli/?hl=ja)
