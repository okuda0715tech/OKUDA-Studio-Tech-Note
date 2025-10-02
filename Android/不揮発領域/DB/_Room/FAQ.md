- [FAQ](#faq)
  - [Caused by: java.lang.RuntimeException: Cannot find implementation for com.example.creditcardmanager.database.AppDatabase. AppDatabase\_Impl does not exist](#caused-by-javalangruntimeexception-cannot-find-implementation-for-comexamplecreditcardmanagerdatabaseappdatabase-appdatabase_impl-does-not-exist)
    - [対応方法](#対応方法)


# FAQ


## Caused by: java.lang.RuntimeException: Cannot find implementation for com.example.creditcardmanager.database.AppDatabase. AppDatabase_Impl does not exist


### 対応方法

Kotlin で実装している場合に、 build.gradle が  
以下のようになっている場合に、表題のエラーが発生した。

```
val roomVersion = "2.6.1"
annotationProcessor("androidx.room:room-compiler:$roomVersion")
```

以下のように `annotationProcessor` を `kapt` に書き換えることで、  
エラーが解消しました。

```
val roomVersion = "2.6.1"
kapt("androidx.room:room-compiler:$roomVersion")
```



