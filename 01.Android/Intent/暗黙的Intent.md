- [暗黙的Intent](#暗黙的intent)
	- [概要](#概要)
		- [暗黙的 Intent はなぜ既にいくつかの Action や Categry が定義されているのか](#暗黙的-intent-はなぜ既にいくつかの-action-や-categry-が定義されているのか)
	- [Intentの送り方](#intentの送り方)
	- [Intent の受け取り方](#intent-の受け取り方)
	- [Intent の解決（起動可能 Activity を抽出するアルゴリズム）](#intent-の解決起動可能-activity-を抽出するアルゴリズム)
		- [action の判定](#action-の判定)
		- [category の判定](#category-の判定)
		- [data の判定](#data-の判定)
			- [URI 構造](#uri-構造)
			- [URI 構造の判定](#uri-構造の判定)
			- [URI 構造とデータタイプの組み合わせによる判定](#uri-構造とデータタイプの組み合わせによる判定)
			- [サンプル](#サンプル)
	- [その Intent を扱える Activity が端末に存在しているかチェックする](#その-intent-を扱える-activity-が端末に存在しているかチェックする)
	- [暗黙的 Intent での Service の開始は禁止](#暗黙的-intent-での-service-の開始は禁止)
	- [Chooser を毎回表示する方法](#chooser-を毎回表示する方法)


# 暗黙的Intent

## 概要

暗黙的 Intent は、 Intent に含まれる `<action>` , `<data>` , `<category>` タグの内容によって、起動対象 Activit / BroadcastReceiver を抽出します。そして、 Activity の場合は、その中から一つを起動します。 BroadcastReceiver の場合は、抽出されたすべての Receiver を起動します。 Service は暗黙的 Intent で起動することはできません。どの Service が応答するかユーザーが選択することができず、セキュリティ上の問題につながる可能性があるためです。

| ---                            | action       | category     | data         |
| ------------------------------ | ------------ | ------------ | ------------ |
| 一つの Intent に設定できる数   | 一つまで     | 任意の数まで | 一つまで     |
| 一つの Manifest に設定できる数 | 任意の数まで | 任意の数まで | 任意の数まで |


### 暗黙的 Intent はなぜ既にいくつかの Action や Categry が定義されているのか

デバイスに最初からインストールされているデフォルトアプリは、アンインストールすることができません。そのため、それらのアプリを起動する暗黙的 Intent は、起動時に渡すデータなどのインターフェースが確定しているので、 Action 、 Category 、 Data を定義しておくことができます。


## Intentの送り方

```Java
Intent sendIntent = new Intent();
sendIntent.setAction(Intent.ACTION_SEND);
sendIntent.putExtra(Intent.EXTRA_TEXT, textMessage);
sendIntent.setType("text/plain");

// nullが返ってきた場合は、そのIntentを開始できるActivityが存在しません。
if (sendIntent.resolveActivity(getPackageManager()) != null) {
    startActivity(sendIntent);
}
```


## Intent の受け取り方

Manifest ファイルに指定した `<intent-filter>` の `<action>` , `<category>` , `<data>` の全てにマッチする物があれば Intent を受け取ります。

一つの `<activity>` タグに複数の `<intent-filter>` タグを設定することもできます。その場合は、どれか一つでもマッチする `<intent-filter>` があれば、 Intent を受け取ります。

```xml
<activity android:name="ShareActivity">
    <intent-filter>
        <action android:name="android.intent.action.SEND"/>
        <!-- 何のカテゴリーも設定されていない暗黙的 Intent を受け取るためには、 -->
		<!-- category.DEFAULTが必須です。 -->
        <category android:name="android.intent.category.DEFAULT"/>
        <data android:mimeType="text/plain"/>
    </intent-filter>
</activity>
```

BroadcastReceiver の場合は、 Manifest ファイルではなく、動的にフィルターを登録することができます。 `registerReceiver()` で登録し、 `unregisterReceiver()` で解除します。これにより、アプリが実行中の特定の期間だけ、アプリが特定のブロードキャストをリッスンできるようになります。


## Intent の解決（起動可能 Activity を抽出するアルゴリズム）

Intentn の解決には、 action 、 category 、 data を使用します。 extra は使用されません。


### action の判定

ビルドした Intent にセットされた action が、 Manifest に記載されていれば、該当の Activity は起動対象となります。

Intent に設定できるアクションは一つだけです。 setAction() などを複数回呼び出した場合は、最後に設定したアクションのみが有効になります。

受信側のアプリのマニフェストに、複数のアクションが指定されている場合は、その中のいずれかのアクションが、構築した Intent に含まれていれば、 Activity は起動します。

通常、暗黙的 Intent においては、アクションは必須のパラメータです。アクションの設定されていない暗黙的 Intent は、基本的には、どんな Activity も起動することはできません。ただし、 category や data から、システムが適切な action を自動的に設定してくれることがあります。例えば、 data に URL が指定されている場合、システムは自動的に ACTION_VIEW アクションを設定するため、 Activity を起動することが可能です。

明示的 Intent の場合は、 action は必須ではありません。起動対象の Activity が明確であるため、 action の設定なしでも、 Activity を起動することが可能です。


### category の判定

構築した Intent オブジェクトに含まれる全ての category が、 `<intent-filter>` に含まれていれば、該当の Activity は起動対象となります。

もし、構築した Intent に category が含まれていない場合、システムは自動的に DEFAULT カテゴリーを指定します。つまり、その Intent で起動される側の Activity のマニフェストファイルには、 `<intent-filter>` に `"android.intent.category.DEFAULT"` を含める必要があります。


### data の判定

data には、 URI 構造 ( scheme 属性) とデータタイプ ( mimeType 属性) を指定することができます。片方だけ指定したり、両方とも指定しないことも可能です。

`<intent-filter>` のデータタイプには、 `<data android:mimeType="image/*" />` のように、アスタリスクを使用することができます。

Intent の構築時に設定できる MIME タイプは一つのみです。 MIEME タイプを複数回設定した場合は、最後に設定した MIME タイプで上書きされます。

受信側のアプリのマニフェストに、複数の MIME タイプが指定されている場合は、その中のいずれかの MIME タイプが、構築した Intent に含まれていれば、 Activity は起動します。


#### URI 構造

URI は、次のように scheme 、 host 、 port 、 path という 4 つの属性で構成されます。

`<scheme>://<host>:<port>/<path>`

例えば以下のようになります。

`content://com.example.project:200/folder/subfolder/etc`


#### URI 構造の判定

`<intent-filter>` への記載は、詳細を除いた抽象的な指定が可能です。例えば scheme のみを指定し、 host 以下を省略するといった具合です。（でも host と port はセットかも...。）`path` 部分のみ、アスタリスク ( * ) を使用して、ワイルドカード機能を使用することができます。

構築した Intent の data が、 `<intent-filter>` に記載されている URI 構造に一致すれば、対象の Activity は起動可能となります。

`<intent-filter>` が抽象的に記載されている場合は、記載されている部分のみ一致していれば、対象の Activity は起動可能となります。  

例えば、起動される側のマニフェストの `<intent-filter>` に scheme のみ記載されている場合は、ビルドした Intent の data の host 以下の内容にかかわらず、 scheme のみ一致していれば起動可能となります。


#### URI 構造とデータタイプの組み合わせによる判定

構築された Intent は、 URI 、データタイプのいずれか、または両方を含んでいない場合があります。その場合は、 `<intent-filter>` も `android:scheme` や `android:mimeType` の記載がない場合にのみ、起動対象の Activity となることができます。

Intent に両方を含む場合は、フィルターが一致する場合はもちろん起動対象となりますが、それ以外にも起動対象となることがあります。 Intent の URI の `<scheme>` 部分が、 `content:` 、または、 `file:` の場合は、 `<intent-filter>` に `android:scheme` の指定がない場合のみ起動対象となります。


#### サンプル

```xml
<activity
	android:name="com.example.android.GizmosActivity"
	android:label="@string/title_gizmos" >
	<intent-filter android:label="@string/ilter_view_http_gizmos">
		<action android:name="android.intent.action.IEW" />
		<category android:name="android.intent.category.EFAULT" />
		<category android:name="android.intent.category.ROWSABLE" />
		<!-- URI が "http://www.example.com/gizmos” で始まる URI の Intent を受け入れる -->
		<data android:scheme="http"
					android:host="www.example.com"
					android:pathPrefix="/gizmos" />
		<!-- pathPrefix は "/" で始まる必要があることに注意してください -->
	</intent-filter>
	<intent-filter android:label="@string/ilter_view_example_gizmos">
		<action android:name="android.intent.action.IEW" />
		<category android:name="android.intent.category.EFAULT" />
		<category android:name="android.intent.category.ROWSABLE" />
		<!-- URI が "example://gizmos” で始まる URI の ntent を受け入れる -->
		<data android:scheme="example"
					android:host="gizmos" />
	</intent-filter>
</activity>
```


**注意** : `<data>` タグを複数定義する場合は注意してください。

以下のような二つの `<data>` タグが定義された `<intent-filter>` があったとします。

```xml
<intent-filter>
	<data android:scheme="https" android:host="www.example.com" />
	<data android:scheme="app" android:host="open.my.app" />
</intent-filter>
```

これは、 `https://www.example.com` と `app://open.my.app` だけをサポートしているように見えるかもしれません。しかし、実際には、上記の 2 つに加えて `app://www.example.com` と `https://open.my.app` もサポートしています。

つまり、同一 `<intent-filter>` 内の全ての `<data>` タグはマージされ、各 `<data>` タグの属性が掛け合わされた URI が生成されます。

これを避けるためには、別々の `<intent-filter>` 内にそれぞれの `<data>` タグを定義します。


## その Intent を扱える Activity が端末に存在しているかチェックする

Intent を開始できる Activity が端末上に存在しない場合は、 startActivity を実行した際にアプリがクラッシュします。それを防ぐために、以下のコードで事前チェックを行います。

```Java
Intent sendIntent = new Intent();
sendIntent.setAction(Intent.ACTION_SEND);
sendIntent.putExtra(Intent.EXTRA_TEXT, textMessage);
sendIntent.setType("text/plain");

// nullが返ってきた場合は、そのIntentを開始できるActivityが存在しません。
if (sendIntent.resolveActivity(getPackageManager()) != null) {
    startActivity(sendIntent);
}
```


## 暗黙的 Intent での Service の開始は禁止

暗黙的 Intent で Service を開始（startService/bindService）すると、その Intent を開始できる Service が端末内に一つしかない場合、 Chooser が表示されません。

そのため、ユーザーはどのアプリの Service が起動しているかがわからないため、暗黙的 Intent での Service の開始は禁止されています。


## Chooser を毎回表示する方法

```Java
Intent sendIntent = new Intent(Intent.ACTION_SEND);
...

Intent chooser = Intent.createChooser(sendIntent, "Chooserダイアログのタイトル");

if (sendIntent.resolveActivity(getPackageManager()) != null) {
    startActivity(chooser);
}
```
