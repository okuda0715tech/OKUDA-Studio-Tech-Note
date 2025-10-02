- [GradleUserHomeのデフォルト値を変更する](#gradleuserhomeのデフォルト値を変更する)
  - [内容](#内容)
  - [参考資料](#参考資料)


# GradleUserHomeのデフォルト値を変更する

## 内容

Gradle のダウンロードフォルダは、デフォルトで `$USER_HOME/.gradle` が指定されています。  
これ以外のフォルダに Gradle をダウンロードしてきて、 Android Studio でそこを参照するには、
環境変数 `GRADLE_USER_HOME` を指定します。

(例)

`GRADLE_USER_HOME = G:\Program Files\Android\.gradle`

こうすることで、 Android Studio の設定画面のデフォルト値が上記のフォルダになります。


## 参考資料

[Gradle settings - IntelliJ IDEA](https://www.jetbrains.com/help/idea/gradle-settings.html)

