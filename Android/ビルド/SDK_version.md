<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [SDK_version](#sdk_version)
  - [minSdkVersion](#minsdkversion)
  - [targetSdkVersion](#targetsdkversion)
  - [compileSdkVersion](#compilesdkversion)
  - [buildToolsVersion](#buildtoolsversion)
  - [各 SdkVersion の関係性](#各-sdkversion-の関係性)
  - [Added in API level XX](#added-in-api-level-xx)
  - [Deprecated in API level XX](#deprecated-in-api-level-xx)
    - [Deprecated になった機能を使用している箇所を見つける方法](#deprecated-になった機能を使用している箇所を見つける方法)
<!-- TOC END -->


# SDK_version

## minSdkVersion

`minSdkVersion` とは、アプリをインストール・実行できる最小 API レベルです。  
このバージョン未満の OS が稼働しているデバイスでは、アプリはインストールできません。  
また、 PlayStore でアプリを検索しても表示されません。

省略時は1となるため、実質的に省略不可です。


## targetSdkVersion

`targetSdkVersion` とは、アプリが 「この SDK バージョンの仕様まで対応済みですよ」 という宣言である。

- targetSdkVersion <= デバイスの API レベル
  - targetSdkVersion の動作・表示になる
- targetSdkVersion > デバイスの API レベル
  - デバイスの API レベルの動作・表示になる

一言でいうと、アプリは、 「 targetSdkVersion 」 と 「デバイスの API レベル」 のうち、
低い方のバージョンに合わせた動作や表示になります。  

例えば、 API レベル 23 ( OS バージョン 6.0 ) で `Runtime Permission` の仕組みが導入されました。  
それまでは、 Dangerous Permission はインストール時に一括で許可する必要がありました。  
しかし、 API レベル 23 以降では、実行時に個別で許可する仕様に変更になりました。

そのため、 targetSdkVersion を 22 以下に設定しているアプリでは、インストール時に  
一括で確認される動きとなり、 23 以上に設定しているアプリでは、実行時に個別で確認される動きとなります。  
ただし、 targetSdkVersion の設定によらず、 API レベル 23 未満のデバイスでは、常にインストール時に  
一括で許可する動きになります。

ちなみに、 targetSdkVersion の設定によらず、 API レベル 23 以降のデバイスでは、  
インストール後に個別に Permission を拒否することが可能です。拒否する場合、  
targetSdkVersion が 23 未満のアプリであれば、 「拒否することでアプリが正常に動作しなくなる可能性がある」  
旨を設定アプリがユーザーに通知してくれます。

- できるだけ最新の API レベルを指定します。
	- 古い API レベルのままだと、 PlayStore でアプリを更新できなくなったり、新規アプリの場合は登録ができません。
- minSdkVersion 以上のレベルを指定します。
- 属性省略時は minSdkVersion のレベルになります。


## compileSdkVersion

`compileSdkVersion` とは、コンパイルに使用する SDK のバージョンである。  

ここで指定した API レベルより後に追加された API は実装しても名前解決できずにコンパイルエラーとなります。


## buildToolsVersion

`buildToolsVersion` とは、コンパイル後のファイルなどのリソースを組み合わせてアプリを作る時に  
使用するツールのバージョンのことである。

この属性は必須ではありません。省略した場合は、プラグインが最適なバージョンを使用してくれます。


## 各 SdkVersion の関係性

以下の要件を満たす必要があります。

<要件>  
compileSdkVersion >= targetSdkVersion >= minSdkVersion


## Added in API level XX

- API レベル XX で追加された機能であり、それ未満の API レベルの端末では使用できない機能である。
- それ未満の API レベルのデバイスでもアプリがインストールできる場合（ minSdkVersion がそれ未満の場合）は、条件分岐でその機能を呼ばないようにする必要がある。
  - 条件分岐をしていないコードが赤色の波線で警告表示される。
  - ただし、コンパイルエラーとはならず、アプリをインストールすることができる。

条件分岐は以下のように実装する。

```Java
// Build.VERSION.SDK_INT は、このアプリが実行されているデバイスの API レベルを返します。
if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1){
     //APIレベル10以前の機種の場合の処理
}else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
     //APIレベル11以降の機種の場合の処理
}
```


## Deprecated in API level XX

- 指定された API レベル以降のデバイスでは非推奨となった機能である。
- 非推奨ではあるが、その時点の API レベルではまだ使用可能である。
- ただし、将来的に登場する API レベルでは、いつか削除されるため、ドキュメントに記載されている内容を参考に実装し直す必要がある。
  - 対応方法の例
    - 既存の機能の代わりに新しく追加された機能に置き換える。
    - ハードウェアの高性能化に伴い、ソフトウェアとしてそのような機能が必要なくなったため、削除する。


### Deprecated になった機能を使用している箇所を見つける方法

プロジェクト名の build.gradle に以下を記載してビルドすると Android Studio の `Build` タブの  
`Build Output` タグに使用している箇所が表示されます。  
ただし、一度ビルドすると二回目以降のビルドでは表示されなくなります。その際は、 `Clean Project`  
もしくは `Rebuild Project` で再度ビルドすると使用箇所が表示されます。

```xml
allprojects {
  gradle.projectsEvaluated {
    tasks.withType(JavaCompile) {
      options.compilerArgs << "-Xlint:unchecked" << "-Xlint:deprecation"
    }
  }
}
```
