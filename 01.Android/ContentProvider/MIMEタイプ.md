<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [MIMEタイプ](#mimeタイプ)
  - [概要](#概要)
  - [フォーマット](#フォーマット)
    - [テーブル形式の場合](#テーブル形式の場合)
    - [ファイル形式の場合](#ファイル形式の場合)
<!-- TOC END -->


# MIMEタイプ

## 概要

`ContentProvider` は、MIMEタイプを返す二つのメソッドを持っています。

- `getType()`
  - テーブルを提供するプロバイダ用です。
- `getStreamTypes()`
  - ファイルを提供するプロバイダ用です。


## フォーマット

### テーブル形式の場合

テーブル形式のプロバイダでは、 `String getType(Uri uri)` メソッドを使用してMIMEタイプを取得します。  
MIMEタイプは以下のフォーマットで返されます。

```
// 複数行の場合
vnd.android.cursor.dir/vnd.<name>.<type>

// 一行の場合
vnd.android.cursor.item/vnd.<name>.<type>
```

`<name>` 部分は、他のプロバイダと重複しない値にする必要があります。  
そのため、多くの場合、 `パッケージ名 + .provider` とします。

`<type>` 部分は、URI パターンに一意のものとする必要があります。  
多くの場合、 `テーブル名` を指定します。  
テーブル名は、大文字で始まることが多いですが、 `<type>` はサンプルでは小文字で始めているため、  
それを真似した方がよさそうです。


### ファイル形式の場合

ファイル形式のプロバイダでは、 `String[] getStreamTypes(Uri uri, String mimeTypeFilter)`  
メソッドを使用してMIMEタイプを取得します。  
取得するMIMEタイプの例を以下に示します。

```
{ "image/jpeg", "image/png", "image/gif"}
```

プロバイダからデータを取得するクライアントアプリが特定のファイル形式にしか対応していない場合は、  
パラメータ `mimeTypeFilter` でフィルターを掛けることができます。  
例えば、 `jpeg` のみを取得したい場合は、パラメータに `*\/jpeg` を指定することで、以下の結果を得ることができます。

```
{"image/jpeg"}
```

プロバイダが、フィルタ文字列で要求した MIME タイプを提供していない場合、getStreamTypes() は null を返します。
