- [CloudMessaging実装手順](#cloudmessaging実装手順)
  - [1. FirebaseコンソールからFirebaseプロジェクトの作成](#1-firebaseコンソールからfirebaseプロジェクトの作成)
  - [2. FirebaseプロジェクトにAndroidアプリを紐づける](#2-firebaseプロジェクトにandroidアプリを紐づける)


# CloudMessaging実装手順

## 1. FirebaseコンソールからFirebaseプロジェクトの作成

主に、プロジェクト名を決めるだけの作業となります。


## 2. FirebaseプロジェクトにAndroidアプリを紐づける

1. アプリのパッケージ名を Firebase に登録する
2. アプリのニックネームを Firebase に登録する
3. 構成ファイル ( google-services.json ) をコンソールからダウンロードし、アプリに格納する  
   格納場所は次の通りです。  
   `\{プロジェクト名}\app\google-services.json`
4. Firebase SDK の追加  
    具体的な追加方法は Firebase コンソールに従うと良いですが、以下（※ 1 ）に注意してください。

（※ 1 ）

以下のように、 google と maven への参照を定義する部分が 2 箇所ありますが、

```
  repositories {
    google()  // Google's Maven repository
    mavenCentral()  // Maven Central repository
  }
```

最新のプロジェクトでは、これらの定義を `build.gradle` に記載するのではなく、  
`settings.gradle` に記述するようになっており、この部分は既に記述されている可能性があります。  

その場合は（？）、 `build.gradle` に記述するとエラーになり、同期ができませんので、注意してください。

