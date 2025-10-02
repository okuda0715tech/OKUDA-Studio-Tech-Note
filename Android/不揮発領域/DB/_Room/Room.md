<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [Room](#room)
	- [Gradle](#gradle)
	- [参考](#参考)
	- [登録したデータをブラウジングアプリで確認する方法](#登録したデータをブラウジングアプリで確認する方法)
	- [参考](#参考)

<!-- /TOC -->


# Room

## Gradle

**Gradle**

```Java
dependencies {
    def room_version = "2.2.5"

    implementation "androidx.room:room-runtime:$room_version"
    annotationProcessor "androidx.room:room-compiler:$room_version"
}
```


## スキーマをエクスポートするディレクトリを設定する

初回ビルド時におそらく「schema export directory is not provided ...」というエラーが出るので、その対処として`Module:app`の`build.gradle`に以下の`javaCompileOptions`ブロックを追記する。

```Java
android {
		...
		defaultConfig {
				...
				javaCompileOptions {
						annotationProcessorOptions {
								arguments = ["room.schemaLocation":
														 "$projectDir/schemas".toString()]
						}
				}
		}
}
```

これは、テーブル構成を更新する場合にテーブルのCREATE文をエクスポートしておく物らしく、テーブルのバージョンを管理するのに必要なようだ。詳細は、以下のリンクを参照。

[Room データベースを移行する スキーマをエクスポートする](https://developer.android.com/training/data-storage/room/migrating-db-versions#export-schema)


## 参考

リリースノート
　[Room - Android Developer](https://developer.android.com/jetpack/androidx/releases/room)


## 登録したデータをブラウジングアプリで確認する方法

以下の三つのファイルがdatabaseフォルダに作成されているので、三つをダウンロードし、同一フォルダに格納する。

app-db
app-db-shm
app-db-wal

一番上のxxx-dbファイルをブラウジングアプリで開くと中身のデータを確認することができる。


## 参考

使い方の公式サイト、公式ブログ、サンプルプロジェクト等のまとめサイト
　[Room Persistence Library - Android Developer](https://developer.android.com/topic/libraries/architecture/room.html)





