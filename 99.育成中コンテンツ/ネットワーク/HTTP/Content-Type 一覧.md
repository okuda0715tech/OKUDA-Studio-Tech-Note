- [Content-Type 一覧](#content-type-一覧)
  - [application/x-www-form-urlencoded](#applicationx-www-form-urlencoded)
    - [仕組み](#仕組み)
    - [使用場所](#使用場所)
  - [application/json](#applicationjson)
  - [multipart/form-data](#multipartform-data)
    - [構造](#構造)
    - [例](#例)
    - [主なポイント](#主なポイント)
    - [クライアント側での実装](#クライアント側での実装)
    - [サーバー側での処理](#サーバー側での処理)


# Content-Type 一覧

## application/x-www-form-urlencoded

### 仕組み

- **キーと値のペア**: 送信するデータは「キー＝値」のペアとしてエンコードされます。
  - 例えば、 `name=John&age=30` のように。

- **URL エンコード**: 特殊文字やスペースはURLエンコードされます。例えば、スペースは + または %20 にエンコードされ、& や = などの特殊文字もエンコードされます。
  - 例: `name=John+Doe&age=30`


### 使用場所

- **HTML フォームのデフォルト**: HTML フォームの `method="POST"` で送信されるデータは、デフォルトでこの形式にエンコードされます。

- **GET リクエストのクエリパラメータ**: GET リクエストでデータを送信する場合、フォームエンコードされたデータは URL のクエリ文字列（ `?` 以降）として含まれます。


## application/json

JSON 形式です。


## multipart/form-data

multipart/form-data は、HTTP リクエストにおいてファイルを含むデータを送信するために使用されるエンコーディングの一種です。この方式は、特にファイルアップロードなどを行う際に便利です。通常の application/x-www-form-urlencoded ではキーと値のペアしか送れませんが、multipart/form-data では複数の種類のデータ（テキストとバイナリファイル）を一緒に送信できます。


### 構造

multipart/form-data は、データをいくつかのパートに分けて送信します。各パートは次のような構成になっています。

1. Content-Disposition ヘッダー: そのパートが何のデータを表しているのかを示します（例: フォームフィールド名やファイル名）。
2. Content-Type ヘッダー（任意）: パートのデータがどのようなMIMEタイプかを示します。通常はファイルに使われます。
3. データ本体: パートに含まれる実際のデータ（テキストやファイルのバイナリデータ）。

各パートは特定のバウンダリー（境界）文字列で区切られます。このバウンダリーはリクエスト全体にわたって一貫して使用され、サーバーが各パートを認識できるようになっています。


### 例

以下は、multipart/form-data リクエストの例です。

```
POST /upload HTTP/1.1
Host: example.com
Content-Type: multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW
Content-Length: 355

------WebKitFormBoundary7MA4YWxkTrZu0gW
Content-Disposition: form-data; name="text_field"

Sample Text
------WebKitFormBoundary7MA4YWxkTrZu0gW
Content-Disposition: form-data; name="file"; filename="example.txt"
Content-Type: text/plain

This is the content of the file.
------WebKitFormBoundary7MA4YWxkTrZu0gW--
```

このリクエストでは、1つのテキストフィールドと1つのファイルが送信されています。boundary は、パート間を区切るための文字列で、送信するたびにランダムに生成されます。


### 主なポイント

- フォームフィールドの送信: 通常のテキストデータを送信できます。
- ファイルの送信: ファイルのバイナリデータを送信できます。Content-Disposition で filename 属性を指定します。
- 複数のファイルの送信: 各ファイルを個別のパートとして扱い、複数のファイルを一度に送信できます。


### クライアント側での実装

HTMLフォームでは、ファイルをアップロードするために以下のような設定を行います。

```html
<form action="/upload" method="POST" enctype="multipart/form-data">
    <input type="text" name="text_field">
    <input type="file" name="file">
    <input type="submit" value="Upload">
</form>
```

- JavaScriptでは、FormData オブジェクトを使用して multipart/form-data リクエストを作成できます。

```js
const formData = new FormData();
formData.append("text_field", "Sample Text");
formData.append("file", fileInputElement.files[0]);

fetch('/upload', {
    method: 'POST',
    body: formData
});
```


### サーバー側での処理

サーバー側では、各パートを解析してデータやファイルを取得します。多くのフレームワークやライブラリがこれをサポートしています。

multipart/form-data はファイルアップロードや複数のデータを同時に送信する際に非常に便利です。



