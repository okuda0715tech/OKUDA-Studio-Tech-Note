<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [アプリの STOP 状態について](#アプリの-stop-状態について)
  - [概要](#概要)
  - [STOP 状態になる契機](#stop-状態になる契機)
  - [STOP 状態を解除する契機](#stop-状態を解除する契機)
  - [【参考】STOP 状態の管理方法](#参考stop-状態の管理方法)
  - [【参考】公式ドキュメント](#参考公式ドキュメント)
<!-- TOC END -->


# アプリの STOP 状態について

## 概要

アプリは、特定の条件下で 「 STOP 状態」 になります。  
STOP 状態になると、暗黙的 Intent を受信することができなくなります。

ただし、 `Intent.FLAG_INCLUDE_STOPPED_PACKAGES` フラグが付与されている暗黙的 Intent は  
受信することができます。

OS が送信する暗黙的 Intent は、上記のフラグが付与されていないため、  
STOP 状態では、受信することができません。


## STOP 状態になる契機

STOP 状態になる契機は以下の通りです。

- ユーザーがアプリをインストールした時 (その後、一度もアプリを起動していない場合のみ)
- アプリをアンインストールした時
- 設定アプリから該当のアプリを 「強制停止」 した時
- Android Studio の 「 Stop 'app'」 ボタン (四角い赤色のボタン) をタップした時
- ADB コマンドで強制停止した時 ( `adb shell am force-stop [対象パッケージ] ` )

STOP 状態になりそうな気がするけれど、ならない契機は以下の通りです。

- タスク一覧からアプリをスワイプして終了した時
- アプリをアップデートした時
- デバイスを再起動した時
- アプリのプロセスをキルした時 ( `adb shell ps [対象アプリのプロセスID]` )


## STOP 状態を解除する契機

STOP 状態を解除する契機は以下の通りです。

- ユーザーがアプリアイコンをタップしてアプリを起動したとき
- `FLAG_INCLUDE_STOPPED_PACKAGES` フラグが付与された Intent を受信した時
- Android Studio の 「 Run 'app'」 ボタン (三角の緑色のボタン) でアプリをインストール or アップデートしたとき


## 【参考】STOP 状態の管理方法

STOP 状態は、 `/data/system/users/0/package-restrictions.xml ( 0 はユーザーの ID )` にて  
管理されているという記事をネット上でいくつか見つけましたが、かなり古い情報であるため、  
現在はそのようなフォルダやファイルを見つけることはできませんでした。

しかし、参考までにファイルの中身のサンプルを以下に示します。

```xml
<?xml version='1.0' encoding='utf-8' standalone='yes' ?>
<package-restrictions>
    <pkg name="com.hoge.app" stopped="true" nl="true" />
    <pkg name="com.fuga.app" stopped="true" />
    <pkg name="com.piyo.app" />
</package-restrictions>
```

`stopped` が `true` の場合は、 STOP 状態になっています。  
`nl` が `true` の場合は、インストールされてから一度も起動されていないことを示しています。


## 【参考】公式ドキュメント

[Launch controls on stopped applications](https://developer.android.com/about/versions/android-3.1?hl=ja#launchcontrols)

[Notification of application first launch and upgrade](https://developer.android.com/about/versions/android-3.1?hl=ja#installnotification)
