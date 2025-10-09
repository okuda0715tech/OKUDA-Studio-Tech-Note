- [Activity 間のデータ転送](#activity-間のデータ転送)
  - [概要](#概要)
  - [registerForActivityResult() のテンプレート](#registerforactivityresult-のテンプレート)
  - [一般的な使用例](#一般的な使用例)
  - [Permission ダイアログでの使用例](#permission-ダイアログでの使用例)
    - [Permission ダイアログから結果を受け取る使用例](#permission-ダイアログから結果を受け取る使用例)
    - [複数の Permission ダイアログから結果を受け取る使用例](#複数の-permission-ダイアログから結果を受け取る使用例)
  - [写真・動画選択ツール](#写真動画選択ツール)
  - [IntentSender](#intentsender)
  - [コンテンツ選択画面を表示](#コンテンツ選択画面を表示)
  - [参考資料](#参考資料)


# Activity 間のデータ転送

## 概要

昔は、 [startActivityForResult()](https://developer.android.com/reference/android/app/Activity#startActivityForResult(android.content.Intent,%20int)) を使用して、 Activity 間でデータ転送を行っていましたが、  
最近は、 [registerForActivityResult()](https://developer.android.com/reference/androidx/activity/result/ActivityResultCaller#public-methods_1) を使用する方法が推奨されています。


## registerForActivityResult() のテンプレート

registerForActivityResult() を使用する際のテンプレートは、以下になります。

```kotlin
val launcher = registerForActivityResult(/* コントラクト */) { result ->
    // 別の Activity から戻ってきた際に呼ばれるコールバック
}

fun startNewIntent() {
    // launch() 関数のパラメータには、様々なものが渡されます。
    // 新しく起動する Activity の情報が渡される場合もありますし、
    // ファイルピッカーでピックしたいファイルの MIME タイプが渡される場合もあります。
    launcher.launch(/* 新しい Intent を起動する際に必要なパラメータ */)
}
```


## 一般的な使用例

`StartActivityForResult()` コントラクトを使用します。

[MyActivityResultLauncher - Github](https://github.com/okuda0715tech/MyActivityResultLauncher)


## Permission ダイアログでの使用例

### Permission ダイアログから結果を受け取る使用例

`RequestPermission()` コントラクトを使用します。

[MyActivityResultLauncherPermission - Github](https://github.com/okuda0715tech/MyActivityResultLauncherPermission)


### 複数の Permission ダイアログから結果を受け取る使用例

`RequestMultiplePermissions()` コントラクトを使用します。

[MyActivityResultLauncherMultiPermission - Github](https://github.com/okuda0715tech/MyActivityResultLauncherMultiPermission)


## 写真・動画選択ツール

`PickVisualMedia()` 、または、 `PickMultipleVisualMedia(Int)` コントラクトを使用します。

[Jetpack Activity コントラクトを使用する](../../Android%20Developers/開発/ガイド/6.主要分野/7.アプリデータとファイル/4.共有ストレージに保存する/3.写真選択ツール.md)


## IntentSender

[StartIntentSenderForResult()](https://developer.android.com/reference/androidx/activity/result/contract/ActivityResultContracts.StartIntentSenderForResult) コントラクトを使用します。

- [例 1](../../Intent/PendingIntent/IntentSender.md)
- [例 2](../../Android%20Developers/開発/ガイド/6.主要分野/7.アプリデータとファイル/4.共有ストレージに保存する/2.メディア.md)


## コンテンツ選択画面を表示

`GetContent()` コントラクトを使用します。

```kotlin
val getContent = registerForActivityResult(GetContent()) { uri: Uri? ->
    // Handle the returned Uri
}

override fun onCreate(savedInstanceState: Bundle?) {
    // ...

    val selectButton = findViewById<Button>(R.id.select_button)

    selectButton.setOnClickListener {
        // Pass in the mime type you want to let the user select
        // as the input
        getContent.launch("image/*")
    }
}
```

selectButton がタップされた際に、ユーザーに画像を選択させるためのシステムのコンテンツ選択画面が表示されます。この場合、"image/*" という MIME タイプを指定しているため、画像ファイルのみを選択できるようになります。

ユーザーが画像を選択すると、その選択されたファイルの URI が getContent に渡されます。registerForActivityResult のコールバック関数が呼ばれ、uri: Uri? が引数として渡されます。


## 参考資料

- [アクティビティの結果を取得する](https://developer.android.com/training/basics/intents/result?hl=ja)

