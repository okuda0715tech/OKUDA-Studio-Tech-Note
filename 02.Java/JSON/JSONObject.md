<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [org.json.JSONObject](#orgjsonjsonobject)
  - [概要](#概要)
  - [JSON フォーマットの文字列から値を取得する](#json-フォーマットの文字列から値を取得する)
    - [基本形](#基本形)
    - [JSON オブジェクトが入れ子になっている場合](#json-オブジェクトが入れ子になっている場合)
    - [配列の場合](#配列の場合)
  - [JSON 文字列を POJO に変換するには自分でマッピングする必要がある](#json-文字列を-pojo-に変換するには自分でマッピングする必要がある)
<!-- TOC END -->


# org.json.JSONObject

## 概要

このドキュメントでは `org.json.JSONObject` クラスを使用して JSON パースする方法を記載します。


## JSON フォーマットの文字列から値を取得する

### 基本形

```java
// 文字列リテラル内なので、 '"' の前にはエスケープが必要。
String jsonData = "{\"key1\" : \"value1\"}";
try {
    JSONObject json = new JSONObject(jsonData);
    String value1 = json.getString("key1");
    println(value1); // value1
} catch (Exception e) {
    Log.e("test", e.toString());
}
```


### JSON オブジェクトが入れ子になっている場合

```java
String jsonData = "{\"key2\" : {\"key3\" : \"value3\"}}";

JSONObject json = new JSONObject(jsonData);

JSONObject value2 = json.getJSONObject("key2");
println(value2); // {"key3":"value3"}

String value3 = value2.getString("key3");
println(value3); // value3
```


### 配列の場合

```java
String jsonData = "[{\"key0\" : \"value0\"},{\"key1\" : \"value1\"}]";

JSONArray jsonArray = new JSONArray(jsonData);

// 配列のインデックスを渡して要素を取得する。
JSONObject jsonObject = jsonArray.getJSONObject(1);
println(jsonObject); // {"key1":"value1"}

String value = jsonObject.getString("key1");
println(value); // value1
```


## JSON 文字列を POJO に変換するには自分でマッピングする必要がある

```java
import org.json.JSONObject;

public class MyPojo {
    private String name;
    private int age;

    // コンストラクタ、ゲッター、セッターは省略

    public static MyPojo fromJson(String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString);
        MyPojo myPojo = new MyPojo();
        myPojo.setName(jsonObject.getString("name"));
        myPojo.setAge(jsonObject.getInt("age"));
        return myPojo;
    }
}
```



