<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [URI](#uri)
  - [ID付きURI](#id付きuri)
<!-- TOC END -->


# URI

プロバイダのデータにアクセスするには、アクセスする対象の `プロバイダ` と `テーブル`  
を指定する必要があります。URLのフォーマットは以下の通りです。

```
// フォーマット
content://オーソリティ/テーブル名

// 例
content://user_dictionary/words
```

`オーソリティ` とは、簡単にいうとプロバイダ名です。 `認証局` とも呼ばれます。  
`オーソリティ/認証局` には、一般的に `パッケージ名.provider` を使用します。

`テーブル名` は、 `パス` と呼ばれることもあります。

`content://` 部分 `（スキーム）` は、常に存在し、固定値です。


## ID付きURI

多くのプロバイダでは、ID 値を URI の末尾に追加することで、テーブル内の 1 つの行にアクセスできます。  
たとえば、_ID が 4 の行を単語リストから取得するには、次のコンテンツ URI を使用します。

```java
Uri singleUri = ContentUris.withAppendedId(UserDictionary.Words.CONTENT_URI, 4);
```
