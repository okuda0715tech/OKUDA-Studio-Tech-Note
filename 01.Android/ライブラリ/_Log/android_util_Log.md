<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [android.util.Log](#androidutillog)
	- [ログ出力メソッド](#ログ出力メソッド)
	- [ログレベルとは](#ログレベルとは)
		- [ログレベルの使用方法](#ログレベルの使用方法)
		- [ログレベルを変更する方法](#ログレベルを変更する方法)
			- [adb shell stopを実行した時にstop: must be rootとなる場合](#adb-shell-stopを実行した時にstop-must-be-rootとなる場合)
			- [adb rootを実行した時にadbd cannot run as root in production buildsとなる場合](#adb-rootを実行した時にadbd-cannot-run-as-root-in-production-buildsとなる場合)
		- [ログレベルの確認](#ログレベルの確認)

<!-- /TOC -->

# android.util.Log

## ログ出力メソッド

ログ出力メソッドは引数が異なるメソッドが二つあります。

```Java
Log.e(String tag, String message);
Log.e(String tag, String message, Throwable throwable);
```

tag：アプリケーション固有のタグもしくは、処理のタグを記載します。
処理のタグとは、そのコードが書かれているクラス名などを指します。
message：デバッグメッセージを記載します。
throwable：exception発生時のエラーを渡すとログに出力してくれます。

## ログレベルとは

ログの種類`(VERBOSE,DEBUG,INFO,WARN,ERROR)`ごとにログ出力有無を制御するための基準となるログの種類のことをログレベルという。
端末内でタグごとにログレベルを管理している。

イメージするなら以下のような感じになる。

**端末内のログレベル表**

タグ       | ログレベル
----------|----------
MyApp     | INFO
YourApp   | INFO
Taro'sApp | DEBUG

デフォルトのログレベルは`INFO`である。

そのため、リリースしたアプリを使用する一般ユーザーのログレベルは`INFO`になっている。

### ログレベルの使用方法

**Sample.java**

```Java
private final String TAG = "MY_APP";
if (Log.isLoggable(TAG, Log.INFO)) {
    Log.v(TAG, "Hello World");
}
```

`isLoggable()`メソッドの第二引数には、`if`文内に記述したログの種類を指定します。

上記の例では、`TAG = MY_APP`のログレベルが`VERBOSE,DEBUG,INFO`の場合に`isLoggable()`メソッドは`true`を返し、ログを出力します。

タグに指定されているログレベルと出力するログの種類を比較し、出力するログの種類がログレベルよりも優先度が高ければログを出力します。

ログの優先度は以下の通りです。

```
ASSERT > ERROR > WARN(WARNING) > INFO > DEBUG > VERBOSE
```

例えば、ログレベルが`INFO`の場合は、`ERROR,WARN,INFO`のログを出力します。


### ログレベルを変更する方法

```shell
adb root // 場合によっては必要
adb shell stop
adb shell setprop log.tag.{設定を変更したいタグ名} {設定したいログレベル} // WARNINGに設定したい場合は"WARN"ではなく"WARNING"を指定した方が良さそう。"WARN"だとインストールに失敗する。
adb shell start
```

（例）

```shell
adb shell setprop log.tag.MY_APP VERBOSE
```

`adb shell`で端末にログインしてからであれば、いきなり`setprop`から初めてOKです。

#### adb shell stopを実行した時にstop: must be rootとなる場合

```shell
adb root
```

#### adb rootを実行した時にadbd cannot run as root in production buildsとなる場合

エミュレータで使用している`System Image`によっては、上記のエラーが出る場合がある。
具体的には、
`Google Play Intel x86 Atom System Image`
を使用しているとエラーになるとのこと。
本来使用するべきは、
`Google APIs Intel x86 Atom System Image`
であるとのこと。
ただし、シミュレータの追加画面で上記の選択はなかったので、色々なシミュレータを試してみる必要がある。


### ログレベルの確認

```shell
adb shell getprop log.tag.{タグ名}
```

何も設定していない状態であれば空白が返って来ます。
`setprop`で設定してあると、現在設定されているログ出力レベルが返って来ます。


### ログレベルをアプリ開発者が指定したい時

上記のログレベルはadbコマンドを使用して端末保持者（ユーザ）が設定する仕組みであるため、アプリ開発者が指定することはできません。
アプリ開発者が指定したい場合は、アプリ内で端末側でやっているログレベルの管理を行うことになります。
または、/data/local.propに次の1行を追加することでログレベルを変更することができるようです。(local.propが無ければ作成する)
`log.tag.MyActivity=VERBOSE`
この場合、TAG"MyActivity"のLogレベルがVERBOSEに設定されます。
この方法を用いても開発者側でログレベルを設定できそうです。やったことはないので、確認が必要ですが。


### リリースビルドのみ任意のログレベルのログを出力しないようにする方法

proguard-rules.pro に以下の記述を追加します。

```
-assumenosideeffects public class android.util.Log {
    public static *** v(...);
    public static *** d(...);
    public static *** i(...);
    public static *** w(...);
    public static *** e(...);
    public static *** wtf(...);
}
```

以下のように ProGuard を有効にします。
proguard-android-optimize.txt を使っているところに注意。デフォルトでは proguard-android.txt なので書き換える必要があります。

```
buildTypes {
    release {
        runProguard true
        proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
    }
}
```
