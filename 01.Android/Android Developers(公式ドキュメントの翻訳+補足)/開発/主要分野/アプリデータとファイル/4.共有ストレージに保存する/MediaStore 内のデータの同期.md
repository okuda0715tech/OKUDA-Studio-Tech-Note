- [MediaStore 内のデータの同期](#mediastore-内のデータの同期)
  - [MediaStore の更新を検出する三つの方法](#mediastore-の更新を検出する三つの方法)
  - [【前提知識】MediaStore の内部構造](#前提知識mediastore-の内部構造)
    - [MediaStore から最新データを取得するサンプルコード](#mediastore-から最新データを取得するサンプルコード)
    - [getVersion() でスキーマの更新を検出する](#getversion-でスキーマの更新を検出する)
    - [getGeneration() で更新を検出するサンプル](#getgeneration-で更新を検出するサンプル)
    - [ContentObserver で更新を検出するサンプル](#contentobserver-で更新を検出するサンプル)
    - [getGeneration() と ContentObserver の違い](#getgeneration-と-contentobserver-の違い)
  - [引用元資料](#引用元資料)


# MediaStore 内のデータの同期

MediaStore から取得したデータをアプリ内にキャッシュしていると、 MediaStore 内のデータが更新された際に、キャッシュが古くなる可能性があります。そのような場合には、 MediaStore から最新のデータを再取得して、データの同期を行う必要性が高くなります。このドキュメントでは、この同期に関して学びます。


## MediaStore の更新を検出する三つの方法

MediaStore の更新を検出するには、以下の三つの方法があります。

- [MediaStore.getVersion(Context context, String volumeName)](https://developer.android.com/reference/android/provider/MediaStore#getVersion(android.content.Context,%20java.lang.String))
  - 実ファイルのメタデータを管理する SQLite データベースのバージョンの変更を検出する。
- [MediaStore.getGeneration(Context context, String volumeName)](https://developer.android.com/reference/android/provider/MediaStore?_gl=1*1gc1fv2*_up*MQ..*_ga*MjI0NTM2NDk1LjE3MjI3NDg4Mzc.*_ga_6HH9YJMN9M*MTcyMjc0ODgzNy4xLjAuMTcyMjc0ODgzNy4wLjAuMA..#getGeneration(android.content.Context,%20java.lang.String))
  - 実ファイルのメタデータを管理する SQLite データベースのデータの変更を検出する。 (プル型)
- [ContentObserver](https://developer.android.com/reference/android/database/ContentObserver)
  - 実ファイルのメタデータを管理する SQLite データベースのデータの変更を検出する。 (プッシュ型)


## 【前提知識】MediaStore の内部構造

このドキュメントを学習する前提として、 MediaStore の内部構造をきちんと理解しておく必要があります。以下に、 MediaStore の内部構造のポイントをリスト化します。

- MediaStore は、コンテンツプロバイダの一種である。
- コンテンツプロバイダは、データソースがデータベースであろうと、ファイルシステムであろうと、そのデータソースへアクセスするインターフェースの役割を担う。
- MediaStore は、画像、動画、オーディオなどの 「ファイル」 を管理するための仕組みであるため、データソースは、ファイルシステムとなり、データベースではありません。
- MediaStore のデータソースは、ファイルシステムであるが、そのファイルのメタデータを管理するために、内部的に SQLite データベースを持っています。
- MediaStore は、メディアファイルを 「データベース (メタデータ管理用) + ファイルシステム (メディアファイル用) 」 で管理しています。


### MediaStore から最新データを取得するサンプルコード

これから、 MediaStore の更新を検出する三つの方法を説明するにあたって、更新検出後に最新データを取得する共通処理のサンプルを以下に記載します。このサンプルでは、メタデータ管理用のデータベースから、最新のデータを取得するサンプルです。その後、必要に応じて、ファイルシステム上の実ファイルを取得してください。

```kotlin
fun refreshMediaData(context: Context) {
    val resolver = context.contentResolver
    val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

    val projection = arrayOf(
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.DISPLAY_NAME,
        MediaStore.Images.Media.DATE_ADDED
    )

    val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

    resolver.query(uri, projection, null, null, sortOrder)?.use { cursor ->
        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
        val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)

        while (cursor.moveToNext()) {
            val id = cursor.getLong(idColumn)
            val name = cursor.getString(nameColumn)

            println("画像ID: $id, ファイル名: $name")
        }
    }
}
```


### getVersion() でスキーマの更新を検出する

`MediaStore.getVersion()` メソッドは、 MediaStore のメタデータを管理しているデータベースのバージョン (すなわちスキーマ) の変更を検出するためのメソッドです。

前回のバージョンと今回のバージョンが異なる場合は、データを再取得するのがお勧めです。

getVersion() での更新の有無のチェックは、アプリのプロセスが開始されたタイミングで実行すれば十分です。データをクエリする度にチェックする必要はありません。

```kotlin
val resolver = context.contentResolver
val prefs = context.getSharedPreferences("media_store_prefs", Context.MODE_PRIVATE)

// 前回のバージョンを取得（デフォルトは空文字）
val oldVersion = prefs.getString("media_store_version", "")

// 今回のバージョンを取得
val newVersion = MediaStore.getVersion(resolver)

// 変更があるか確認
if (oldVersion != newVersion) {
    println("MediaStore のバージョンが更新されました！データを再取得します。")

    // データを再取得（例: 画像をリストに更新）
    refreshMediaData(context)

    // 新しいバージョンを保存
    prefs.edit().putString("media_store_version", newVersion).apply()
}
```

`getVersion()` メソッドは、具体的に 「バージョンが XXX なら、 ○○○ を実行する」 というものではありません。 「データベースのバージョンが変わったので、なんとなく、最新のデータを取得しておいた方が良さそう」 という程度のものです。公式ドキュメントにも、 「バージョンがいくつなら、何がどう変わって、どのような対処が必要である」 というような記載はありません。そのため、あまり深刻に考えて、具体的な実装をするものではなく、 「バージョンが変わったら、 MediaStore のメタデータの最新データを再取得すると良い」 という程度のものです。


### getGeneration() で更新を検出するサンプル

`MediaStore.getGeneration()` メソッドは、 MediaStore のメタデータのデータベースのデータが変更されるたびに、単純に (おそらく 1 ずつ) 増加します。

前回のジェネレーションと今回のジェネレーションが異なる場合は、データを再取得します。

getVersion() が更新されると、 getGeneration() は、リセットされ、再び 1 から？開始されるようです。

getGeneration() は、 getVersion() とは異なり、データの追加や更新を検出できるため、頻繁に最新のデータをチェックするのに使用できます。

```kotlin
val oldGeneration = prefs.getLong("media_store_generation", -1)
val newGeneration = MediaStore.getGeneration(resolver)

if (oldGeneration != newGeneration) {
    println("メディアファイルの変更を検出！データを更新します。")
    refreshMediaData(context)
    prefs.edit().putLong("media_store_generation", newGeneration).apply()
}
```

[DATE_ADDED](https://developer.android.com/reference/android/provider/MediaStore.MediaColumns?hl=ja&_gl=1*1a1kdzn*_up*MQ..*_ga*MjI0NTM2NDk1LjE3MjI3NDg4Mzc.*_ga_6HH9YJMN9M*MTcyMjc0ODgzNy4xLjAuMTcyMjc0ODgzNy4wLjAuMA..#DATE_ADDED) や [DATE_MODIFIED](https://developer.android.com/reference/android/provider/MediaStore.MediaColumns?hl=ja&_gl=1*1a1kdzn*_up*MQ..*_ga*MjI0NTM2NDk1LjE3MjI3NDg4Mzc.*_ga_6HH9YJMN9M*MTcyMjc0ODgzNy4xLjAuMTcyMjc0ODgzNy4wLjAuMA..#DATE_MODIFIED) などのメディア列の日付よりも getGeneration() を使用する方が安全です。これは、アプリが [setLastModified()](https://developer.android.com/reference/java/io/File?hl=ja&_gl=1*1a1kdzn*_up*MQ..*_ga*MjI0NTM2NDk1LjE3MjI3NDg4Mzc.*_ga_6HH9YJMN9M*MTcyMjc0ODgzNy4xLjAuMTcyMjc0ODgzNy4wLjAuMA..#setLastModified(long)) を呼び出したときや、ユーザーがシステムクロックを変更したときに、メディア列の値が変更される可能性があるためです。

**注意**: getGeneration() の値を使用する前に、 [メディアストアの更新を確認](#メディアストアの更新を確認するデータの再スキャンが必要かどうかを確認する) してください。メディアストアのバージョンが変更されている場合は、同期してください。


### ContentObserver で更新を検出するサンプル

`ContentObserver` は、コンテンツプロバイダーのテーブルのデータが変更される度に、自分のアプリに対して通知を行います。そのため、即時に最新のデータをアプリに反映するのに使用します。

```kotlin
val observer = object : ContentObserver(Handler(Looper.getMainLooper())) {
    override fun onChange(selfChange: Boolean) {
        println("メディアストアの変更を検出！")
        refreshMediaData(context)
    }
}

context.contentResolver.registerContentObserver(
    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
    true,
    observer
)
```


### getGeneration() と ContentObserver の違い

getGeneration() がプル型であるのに対し、 ContentObserver はプッシュ型であるため、リアルタイムでアプリにデータを反映することができます。


## 引用元資料

- ChatGPT との会話


