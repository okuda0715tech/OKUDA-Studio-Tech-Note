- [よく使用する Action](#よく使用する-action)
  - [一覧](#一覧)
  - [Webページを開く](#webページを開く)
  - [電話をかける](#電話をかける)
  - [マップを開く](#マップを開く)
  - [メールを送る](#メールを送る)
  - [SMS を送る](#sms-を送る)
  - [写真・動画をギャラリーから選択](#写真動画をギャラリーから選択)
  - [カメラアプリを開いて写真を撮る](#カメラアプリを開いて写真を撮る)
  - [共有（シェア）機能を使う](#共有シェア機能を使う)
    - [文字列を共有する](#文字列を共有する)
    - [画像を共有する](#画像を共有する)
    - [ファイルを作成する](#ファイルを作成する)
  - [スキーム（Scheme）](#スキームscheme)
  - [引用元資料](#引用元資料)


# よく使用する Action

Android の 暗黙的 Intent (Implicit Intent) の Action にはさまざまなものがありますが、特によく使用するものを紹介します。


## 一覧

| 機能               | Action (Intent)        |
| ------------------ | ---------------------- |
| Webページを開く    | ACTION_VIEW            |
| 電話をかける       | ACTION_DIAL            |
| マップを開く       | ACTION_VIEW (geo)      |
| メールを送る       | ACTION_SENDTO (mailto) |
| SMSを送る          | ACTION_SENDTO (smsto)  |
| 画像を選択         | ACTION_PICK            |
| カメラを開く       | ACTION_IMAGE_CAPTURE   |
| 共有する           | ACTION_SEND            |
| ファイルを作成する | ACTION_CREATE_DOCUMENT |


## Webページを開く

```kotlin
val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"))
startActivity(intent)
```

- `Intent.ACTION_VIEW`：指定した Uri のコンテンツを開く
- `Uri.parse("https://example.com")`：Web ページの URL


## 電話をかける

```kotlin
val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:0123456789"))
startActivity(intent)
```

- `Intent.ACTION_DIAL`：ダイヤルアプリを開く（即発信しない）
- `Intent.ACTION_CALL`：即発信する（パーミッションが必要）
- `Manifest.permission.CALL_PHONE` を付与する必要がある


## マップを開く

```kotlin
val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:35.6895,139.6917"))
startActivity(intent)
```

- `geo:緯度,経度` の形式で Google マップを開く

特定の場所を検索したい場合：

```kotlin
val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=Tokyo Tower"))
startActivity(intent)
```


## メールを送る

```kotlin
val intent = Intent(Intent.ACTION_SENDTO).apply {
    data = Uri.parse("mailto:example@example.com")
    putExtra(Intent.EXTRA_SUBJECT, "件名")
    putExtra(Intent.EXTRA_TEXT, "本文")
}
startActivity(intent)
```

- `Intent.ACTION_SENDTO`：メールアプリを開く（ `mailto:` が必要）
- `Intent.EXTRA_SUBJECT`：件名
- `Intent.EXTRA_TEXT`：本文


## SMS を送る

```kotlin
val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:0123456789")).apply {
    putExtra("sms_body", "こんにちは！")
}
startActivity(intent)
```

- `Intent.ACTION_SENDTO`：SMS を送信（ `smsto:` が必要）


## 写真・動画をギャラリーから選択

```kotlin
val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
startActivityForResult(intent, REQUEST_CODE)
```

- `Intent.ACTION_PICK`：特定の種類のデータを選択
- `MediaStore.Images.Media.EXTERNAL_CONTENT_URI`：ギャラリーの画像を開く


## カメラアプリを開いて写真を撮る

```kotlin
val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
startActivityForResult(intent, REQUEST_CODE)
```

- `MediaStore.ACTION_IMAGE_CAPTURE`：カメラアプリを開く


## 共有（シェア）機能を使う

### 文字列を共有する

```kotlin
val intent = Intent(Intent.ACTION_SEND).apply {
    type = "text/plain"
    putExtra(Intent.EXTRA_TEXT, "共有するテキスト")
}
startActivity(Intent.createChooser(intent, "共有するアプリを選択"))
```

- `Intent.ACTION_SEND`：テキストや画像などを共有
- `Intent.EXTRA_TEXT`：共有する内容
- `Intent.createChooser()`：ユーザーにアプリ選択画面を表示


### 画像を共有する

```kotlin
// ギャラリーから画像を選択するには、以下のように MediaStore API を使用します。
val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
startActivityForResult(intent, REQUEST_CODE)

// ユーザーが画像を選択した後に呼ばれるコールバック
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
        val imageUri = data?.data ?: return
        shareImage(imageUri)
    }
}

// 画像を共有する処理
private fun shareImage(imageUri: Uri) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "image/*"
        putExtra(Intent.EXTRA_STREAM, imageUri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    startActivity(Intent.createChooser(intent, "画像を共有"))
}
```

上記のコードを registerForActivityResult を使用して書き直したものは以下になります。

```kotlin
// ActivityResultContracts を使って画像選択用のインテントを登録
private val pickImageResultLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
    uri?.let {
        shareImage(it)
    }
}

// 画像を選択するボタンなどのイベントハンドラで呼び出す
fun openGallery() {
    pickImageResultLauncher.launch("image/*")  // 画像タイプを指定してギャラリーを開く
}

// 画像を共有するメソッド
private fun shareImage(imageUri: Uri) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "image/*"
        putExtra(Intent.EXTRA_STREAM, imageUri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    startActivity(Intent.createChooser(intent, "画像を共有"))
}
```


### ファイルを作成する

詳細は、 [新しいファイルを作成する](../Android%20Developers/開発/ガイド/6.主要分野/7.アプリデータとファイル/4.共有ストレージに保存する/4.ドキュメントおよび他のファイル.md/#新しいファイルを作成する) を参照してください。 


## スキーム（Scheme）

**注意** : スキーム ( Scheme ) とスキーマ ( Schema ) は別ものです。スキーマは、データベースや JSON の構造のことです。

Android でよく使用されるスキームには、 `content://` や `file://` などがありますが、このドキュメントで使用されている `tel:` や `mailto:` もスキームです。 `tel:` や `mailto:` スキームは、特定の ACTION とセットで使用されるので、あまり使用頻度は高くはありません。覚える必要もないでしょう。

**参考** : `content://` のように、末尾にスラッシュ 2 個が付与されるスキームと、 `tel:` のようにスラッシュが付与されないスキームがあります。スラッシュが付与されるスキームは、それが階層的 URI であることを示します。つまり、 `content://authority/path/to/data` のように、後続のデータがスラッシュで区切られる可能性があります。一方で、フラッシュが付与されないスキームは、 `tel:0123456789` のように、その URI がフラットな構造であることを示します。


## 引用元資料

- ChatGPT



