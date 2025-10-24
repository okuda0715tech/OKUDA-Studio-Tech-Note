<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [UPDATE 文](#update-文)
  - [方法 1 : execSQL() メソッドを使用する方法](#方法-1--execsql-メソッドを使用する方法)
  - [方法 2 : update() メソッドを使用する方法](#方法-2--update-メソッドを使用する方法)
<!-- TOC END -->


# UPDATE 文

## 方法 1 : execSQL() メソッドを使用する方法

```java
SupportSQLiteDatabase database;

// database = インスタンスの取得

// String 型の値をセットする場合は、シングルクォーテーション 「 ' 」 で囲む。
database.execSQL("UPDATE `TableName` SET `columnName` = '" + stringValue + "'");
// String 型以外のプリミティブ型をセットする場合は、シングルクォーテーションは不要。
database.execSQL("UPDATE `TableName` SET `columnName` = " + booleanValue);
```


## 方法 2 : update() メソッドを使用する方法

```java
// ContentValues オブジェクトに、値を更新したい列名と値のペアをセットする。
ContentValues contentValues = new ContentValues();
contentValues.put("isShownColumn1", isShownColumn1);
contentValues.put("isShownColumn2", isShownColumn2);
contentValues.put("isShownColumn3", isShownColumn3);
// 第一引数 : テーブル名
// 第二引数 : データコンフリクト時の処理方法
// 第三引数 : 列名と値のペアを格納したオブジェクト
// 第四引数 : WHERE 句の指定。 null の場合は、全件更新を行う。
// 第五引数 : 第四引数のパラメータを動的に与えたい場合に使用する。
database.update("TableInfo", SQLiteDatabase.CONFLICT_NONE, contentValues, null, null);

// 第四引数を使用した例
// 第四引数に 「 ? 」 が含まれていない場合は、第五引数には null を指定します。
database.update("TableInfo", SQLiteDatabase.CONFLICT_NONE, contentValues, "id = 3", null);

// 第五引数を使用した例
database.update("TableInfo", SQLiteDatabase.CONFLICT_NONE, contentValues,
  "id > ? AND id < ?", new Integer[]{3, 7});
```
