- [API Key を安全にプロジェクトに追加する](#api-key-を安全にプロジェクトに追加する)
  - [概要](#概要)
  - [secrets.properties](#secretsproperties)


# API Key を安全にプロジェクトに追加する

## 概要

API Key を安全にプロジェクトに追加するには、バージョン管理システムにはチェックインせず、  
プロジェクトのルートディレクトリにある secrets.properties ファイルに保存することが  
Google から推奨されています。

secrets.properties ファイルについて詳しくは、 [Gradle プロパティ ファイル](https://developer.android.com/studio/build?hl=ja#properties-files) をご覧ください。

このタスクを効率化するには、 [Android 用 Secrets Gradle](https://github.com/google/secrets-gradle-plugin) プラグインの使用をおすすめします。

日本語のドキュメントは、以下を参照すると良いです。  
ただし、最新のドキュメントではない可能性は、ほんの少しだけあるので、注意してください。  
なお、以下のドキュメントは Maps SDK for Android の場合なので、他の API Key の場合は、適宜読み替えてください。

[ステップ 3: API キーをプロジェクトに追加する](https://developers.google.com/maps/documentation/android-sdk/config?hl=ja#step_3_add_your_api_key_to_the_project)


## secrets.properties

secrets.properties というファイルは自動では生成されないようなので、  
自分で生成する必要があると思われる。

その際、本来であれば、 local.properties ファイルに保存されるはずだったプロパティを  
secrets.properties に保存するように、 `secret{}` ブロックを記述して、  
設定を変更してあげる必要がある。


