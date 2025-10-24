<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [REST形式APIを作成](#rest形式apiを作成)
	- [料金](#料金)
	- [手順](#手順)
		- [1.Firebaseにプロジェクトを追加](#1firebaseにプロジェクトを追加)
		- [2.ローカルマシンに Node.js と npm をインストールする](#2ローカルマシンに-nodejs-と-npm-をインストールする)
			- [2-1.(準備) Node Version Manager のインストール](#2-1準備-node-version-manager-のインストール)
			- [2-2.Node.js と npm のインストール](#2-2nodejs-と-npm-のインストール)
			- [インストールに関して何か問題があれば以下を参照](#インストールに関して何か問題があれば以下を参照)
		- [3.Firebase CLI をインストールする](#3firebase-cli-をインストールする)
		- [注意事項](#注意事項)
		- [4.Firebase SDK for Cloud Functions を初期化する](#4firebase-sdk-for-cloud-functions-を初期化する)
			- [4-1.ターミナルから`firebase login`を実行する。](#4-1ターミナルからfirebase-loginを実行する)

<!-- /TOC -->

# REST形式APIを作成

## 料金

Sparkアカウントなら、利用上限が低いが無料で使用可能。


## 手順

私が作成する以下の手順は公式サイトの焼き直しです。
私の作成した手順は簡潔にまとめた手順であるため、もし、問題があった場合は公式サイトを参照してください。

[スタートガイド: 最初の関数を作成してデプロイする - 公式サイト](https://firebase.google.com/docs/functions/get-started#review_complete_sample_code)

### 1.Firebaseにプロジェクトを追加

Firebaseコンソールからプロジェクトを追加する。
プロジェクト名を指定するだけ。


### 2.ローカルマシンに Node.js と npm をインストールする

#### 2-1.(準備) Node Version Manager のインストール

以下のサイトの`Installation and Update`の章にあるコマンドでインストールする。（サイトを参照することで常に最新バージョンをインストールできる）

[nvm-sh/nvm - github](https://github.com/nvm-sh/nvm/blob/master/README.md)


#### 2-2.Node.js と npm のインストール

`npm (Node Package Manager)`とは、Node.jsのパッケージ (Package) を管理す (Manager) ツールです。

```
nvm install --lts
nvm use --lts
node -v
```

以下のような感じでバージョンが表示されればインストール完了。

```
v8.11.3
```

#### インストールに関して何か問題があれば以下を参照

[nvm + Node.js + npmのインストール](https://qiita.com/sansaisoba/items/242a8ba95bf70ba179d3)

ちなみに、上記のサイトで出てくる`.bash_profile`ファイルは、ターミナルを開いた直後に表示されるディレクトリ(ホームディレクトリ)直下に存在している。
ドットから始まるファイルは隠しファイルであるため、`ls -a`のように`-a`をつけないと表示されない。


### 3.Firebase CLI をインストールする

以下のコマンドでインストールする。

```
npm install -g firebase-tools
```

### 注意事項

多くの場合、新機能とバグ修正は Firebase CLI の最新バージョンと firebase-functions SDK でのみ利用できます。Firebase プロジェクトの functions フォルダ内で以下のコマンドを使用して Firebase CLI と SDK の両方を頻繁に更新することをおすすめします。

```
npm install firebase-functions@latest firebase-admin@latest --save
npm install -g firebase-tools
```


### 4.Firebase SDK for Cloud Functions を初期化する

#### 4-1.ターミナルから`firebase login`を実行する。

```
okudasatoshikonoMacBook-puro:~ okuda0715$ firebase login
? Allow Firebase to collect anonymous CLI usage and error reporting information?
 (Y/n)
 ```

ここは`n`でOK。

ブラウザでFirebaseへのログインが行われるので、ログインしたいアカウントを選択してログインする。

Firebase CLI(Command-Line interface)へのログイン成功画面が出たら、その画面を閉じてOK。

ログインすることにより、ローカルマシンが Firebase に接続し、Firebase プロジェクトへのアクセスが許可されます。


#### 4-2.プロジェクトディレクトリに移動して初期化する

ターミナル上で、Androidアプリケーションのプロジェクトルートディレクトリ(appフォルダの上のディレクトリ)に移動する。

以下のコマンドを実行する。

```
firebase init functions
```

実行結果

```
okudasatoshikonoMacBook-puro:Geofencing okuda0715$ firebase init functions

     ######## #### ########  ######## ########     ###     ######  ########
     ##        ##  ##     ## ##       ##     ##  ##   ##  ##       ##
     ######    ##  ########  ######   ########  #########  ######  ######
     ##        ##  ##    ##  ##       ##     ## ##     ##       ## ##
     ##       #### ##     ## ######## ########  ##     ##  ######  ########

You're about to initialize a Firebase project in this directory:

  /Users/okuda0715/AndroidStudioProjects/android-play-location-master/Geofencing


=== Project Setup

First, let's associate this project directory with a Firebase project.
You can create multiple project aliases by running firebase use --add,
but for now we'll just set up a default project.

? Select a default Firebase project for this directory: (Use arrow keys)
❯ [don't setup a default project]
  anchan-4cef6 (Anchan)
  mypush-okuda0715 (MyPush)
  [create a new project]

? Select a default Firebase project for this directory: anchan-4cef6 (Anchan)
i  Using project anchan-4cef6 (Anchan)

=== Functions Setup

A functions directory will be created in your project with a Node.js
package pre-configured. Functions can be deployed with firebase deploy.

? What language would you like to use to write Cloud Functions? JavaScript
? Do you want to use ESLint to catch probable bugs and enforce style? Yes
✔  Wrote functions/package.json
✔  Wrote functions/.eslintrc.json
✔  Wrote functions/index.js
✔  Wrote functions/.gitignore
? Do you want to install dependencies with npm now? Yes

> protobufjs@6.8.8 postinstall /Users/okuda0715/AndroidStudioProjects/android-play-location-master/Geofencing/functions/node_modules/protobufjs
> node scripts/postinstall

npm notice created a lockfile as package-lock.json. You should commit this file.
added 357 packages from 253 contributors and audited 886 packages in 134.705s
found 0 vulnerabilities


i  Writing configuration info to firebase.json...
i  Writing project information to .firebaserc...

✔  Firebase initialization complete!
```







## サンプル

### 概要

Firebaseの`Cloud Functions`にHTTPリクエストを投げて、データベースへ保存を行う。
データベースには、Firebaseの`Realtime Database`を使用する。




