- [XXXのonCreate(SupportSQLiteDatabase)はDelegateのonCreate(SupportSQLiteDatabase)をオーバーライドできません](#xxxのoncreatesupportsqlitedatabaseはdelegateのoncreatesupportsqlitedatabaseをオーバーライドできません)
  - [エラーの詳細](#エラーの詳細)
  - [解決方法](#解決方法)


# XXXのonCreate(SupportSQLiteDatabase)はDelegateのonCreate(SupportSQLiteDatabase)をオーバーライドできません

## エラーの詳細

以下のような感じのエラーで、 `AppDatabase_Impl` の `onCreate()` メソッドが `protected` になっており、  
「それをオーバーライドしようとしているけどできない」というエラーが発生しました。

```
AppDatabase_Impl.java:56: error: onCreate(SupportSQLiteDatabase) in <anonymous com.droidbane.test.database.AppDatabase_Impl$1> cannot override onCreate(SupportSQLiteDatabase) in Delegate
      protected void onCreate(SupportSQLiteDatabase _db) {
                     ^
  attempting to assign weaker access privileges; was public
```


## 解決方法

Roomライブラリのバージョンをすべて同じバージョンにそろえれば解決した。

