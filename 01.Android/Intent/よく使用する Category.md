- [よく使用する Category](#よく使用する-category)
  - [CATEGORY\_LAUNCHER](#category_launcher)
  - [CATEGORY\_BROWSABLE](#category_browsable)
  - [引用元資料](#引用元資料)


# よく使用する Category

Intent にカテゴリーを指定することはほとんどありませんが、以下の二つの使い方だけは覚えておくと良いです。


## CATEGORY_LAUNCHER

CATEGORY_LAUNCHER は、デバイスのホームの 「アプリ一覧」 にアプリアイコンを表示し、それをタップした時に、アプリが起動できるようにするために必要です。

通常、以下のように、 MAIN アクションとセットで指定されます。

```xml
<intent-filter>
    <action android:name="android.intent.action.MAIN" />
    <category android:name="android.intent.category.LAUNCHER" />
</intent-filter>
```


## CATEGORY_BROWSABLE

CATEGORY_BROWSABLE は、 URL リンクがタップされた際に、アプリを起動するためのカテゴリーです。ディープリンクやカスタム URL スキームで起動できるようにするアプリの場合に必要です。

`https://example.com` のリンクをタップすると、アプリが開くようにするには、以下のように実装します。

```xml
<intent-filter>
    <action android:name="android.intent.action.VIEW" />
    <category android:name="android.intent.category.BROWSABLE" />
    <data android:scheme="https" android:host="example.com" />
</intent-filter>
```


## 引用元資料

- ChatGPT


