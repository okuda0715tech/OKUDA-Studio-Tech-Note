<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [Permission](#permission)
  - [ユーザーへの説明](#ユーザーへの説明)
  - [Android6.0以降とAndroid5.1以前の違い](#android60以降とandroid51以前の違い)
  - [Permissionの種類](#permissionの種類)
    - [インストール時 Permission](#インストール時-permission)
    - [実行時 Permission](#実行時-permission)
  - [プロテクションレベル（保護レベル）](#プロテクションレベル保護レベル)
  - [Permissionグループ](#permissionグループ)
  - [Permission の自動追加](#permission-の自動追加)
  - [ライブラリが必要とする Permission はアプリでも必要となる](#ライブラリが必要とする-permission-はアプリでも必要となる)
  - [\<uses-permission\> タグと \<permission\> タグの違い](#uses-permission-タグと-permission-タグの違い)

<!-- /TOC -->


# Permission

## ユーザーへの説明

権限をリクエストするときは、ユーザーが十分な情報に基づいて判断できるように、以下の情報を提供する必要があります。

- アクセスする対象
- アクセスの理由
- 権限が拒否された場合に影響を受ける機能


## Android6.0以降とAndroid5.1以前の違い

- Android 6.0 以降
  - 動的に Permission の付与、削除が可能
  - Permission が必要な機能を利用する場合は、利用の直前に必ず Permission が許可されているかを確認する必要がある。
- Android 5.1 以前
  - アプリのインストール時、アプリの更新時に Permission の確認、付与を行う。
  - Permissionを許可しない場合はインストールできない。
  - アプリをアンインストールしなければPermissionを削除することができない。


## Permissionの種類

### インストール時 Permission

インストール時 Permission の特徴は以下の通りです。

- アプリでインストール時の権限を宣言すると、アプリストアでユーザーがアプリの詳細ページを開いたときに、インストール時の権限に関するお知らせが表示されます。
- ユーザーがアプリをインストールすると、自動的にアプリに権限が付与されます。
- 付与されてもユーザーにあまり危険性のない権限は、インストール時 Permission に分類されています。
- インストール後に動的に Permission を拒否することはできないようです。
  - `INTERNET` や `ACCESS_WIFI_STATE` の Permission をマニフェストに設定しても、設定アプリの権限欄にそれらの権限が表示されないため、拒否できませんでした。そのため、該当の Permission が必要な処理を実行する前に、 Permission が付与されているかどうかを確認する必要はなさそうです。

インストール時 Permission には、以下の 2 種類が存在します。

- 標準の権限
- 署名権限
    - 署名権限は、権限を定義しているアプリまたは OS と同じ証明書でアプリが署名されている場合にのみ付与されます。


### 実行時 Permission

実行時 Permission の特徴は以下の通りです。

- 権限を必要とする処理を実行する直前に、権限が付与されているかを確認する必要があります。
- 実行時に権限が付与されていなければ、ユーザーに権限付与を求めるダイアログを表示し、ユーザーからの許可を待ちます
- 特に、アクセスされるとユーザーに危険が及ぶ可能性の高いデータやアクションに関する権限は、実行時 Permission に分類されています。

実行時 Permission には、以下の 2 種類が存在します。

- 実行時の権限
- 特別な権限
  - 特別な権限の場合も実行時にユーザー許可を取得する必要があります。


## プロテクションレベル（保護レベル）

- Normal Permission
  - アプリのインストール時にシステムが自動的に Permission を許可します。
  - マニフェストファイルに `<uses-permission>` を記載するだけで該当の許可が必要なAPIを使用することができます。
- Dengerous Permission
  - ユーザーが許可すると Permission を付与します。
  - Dengerous Permission になる可能性のある Permission の例
	  - ユーザーのプライバシー情報へのアクセス
	  - 保存されたデータに影響を与える
	  - 他のアプリを操作する
- Signed permission
	- 使用する機会はあまりありません
	- アプリのインストール時にシステムが自動的にPermissionを許可します。
	- 同じPermission、同じ署名を持っているアプリによってのみ、Permissionが使用されます。


## Permissionグループ

権限は権限グループに属します。権限グループは、論理的に関連する一連の権限で構成されています。  
たとえば、SMS メッセージを送信する権限と受信する権限は、どちらもアプリによる SMS の操作に関連するため、  
同じグループに属することがあります。

権限グループは、アプリが密接に関連する権限をリクエストしたときに、ユーザーに表示される  
システムダイアログの数を最小限に抑えるために役立ちます。  
アプリに権限を付与するように求めるプロンプトがユーザーに表示された場合、  
同じグループに属する権限が同じインターフェースに表示されます。

ただし、権限は予告なくグループを変更することがあるため、特定の権限が必ずしも  
他の権限とグループ化されているわけではありません。そのため、アプリでは、権限が特定の権限グループに属しているかどうかに依存するロジックを使用するべきではありません。

```
*******************************************
*** 古い情報（現在も存在する仕様かは不明） ***
*******************************************

Android システムのすべての Dangerous パーミッションは、パーミッション グループに帰属します。  
Android 6.0（API レベル 23）の端末、かつアプリの targetSdkVersion が 23 以降の場合、  
アプリが Dangerous パーミッションをリクエストしたときのシステム動作は次の通りです。

Permissionグループ | Permission
-------------------|---------------
CONTACTS           | READ_CONTACTS
CONTACTS           | WRITE_CONTACTS

- 同じPermissionグループ内のPermissionがまだ一つも許可されていない場合
  - アプリはPermissionグループ`CONTACTS`の許可要求ダイアログを表示します。
  - 許可した場合、個別の`Permission`に対して許可が出ます。
- 同じPermissionグループ内のPermissionが一つでも許可されている場合
  - 別のPermissionの許可要求はユーザーに確認されることなく、直ちに許可されます。

Android 5.1（API レベル 22）以前の端末、あるいはアプリの targetSdkVersion が 22 以前の場合、
ユーザーはインストール時にパーミッションの付与を求められます。
ユーザーには個別のパーミッションではなく、どのパーミッショングループがアプリに必要かが伝えられます。
```


## Permission の自動追加

- 新しい API レベルの OS が誕生すると、新たな Permission が誕生することがあります。
- 以下の条件を満たす場合に、アプリのマニフェストに自動的に新たな Permission が追加されます。
	- デバイスの API レベル >= 新たな Permission が追加された API レベル
	- かつ
	- targetSdkVersion < 新たな Permission が追加された API レベル
- これは、実際に Permission が必要な API を呼んでいるかどうかの判定が難しいため、対象の API を呼んでいると見なすことで安全性を高めているためです。
- 実際に対象の API を使用していないにもかかわらず、インストール時に Permission の許可を求めるダイアログが表示される可能性があります。
- これを防ぐために、常に targetSdkVersion を最新に保つことが重要です。

新たな API レベルが誕生した際に、新たな Permission が追加される場合は、以下のリンクで内容を確認できます。

[Build.VERSION_CODES](https://developer.android.com/reference/android/os/Build.VERSION_CODES?hl=ja#top_of_page)


## ライブラリが必要とする Permission はアプリでも必要となる

ライブラリを追加すると、そのライブラリが必要とする権限もアプリで必要となります。  
ライブラリが必要とする Permission をアプリでも忘れずに要求する必要があります。  
また、ライブラリが必要とする Permission が本当に必要な Permission なのかも確認するようにしましょう。


## \<uses-permission\> タグと \<permission\> タグの違い

`<uses-permission>` タグは、開発中のアプリに許可してもらいたい Permission がある場合に、  
その Permission を定義するのに使用します。

`<permission>` タグは、独自の Permission を定義したい場合に使用します。

独自の Permission の定義方法については以下を参照すると良いかと思います。

[アプリマニフェストファイル Permission - Android developer](https://developer.android.com/guide/topics/manifest/permission-element?hl=ja)


