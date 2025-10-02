<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [Map](#map)
  - [用語](#用語)
  - [キーの更新](#キーの更新)
  - [バリューの更新](#バリューの更新)
  - [エントリーの削除](#エントリーの削除)
  - [for ループ](#for-ループ)
    - [ループ内でキーのみを使用する場合](#ループ内でキーのみを使用する場合)
    - [ループ内でバリューのみを使用する場合](#ループ内でバリューのみを使用する場合)
    - [ループ内でキーとバリューの両方を使用する場合](#ループ内でキーとバリューの両方を使用する場合)
<!-- TOC END -->


# Map

## 用語

- エントリー
  - Map 内に格納された一つのキーとバリューのセットを示します。


## キーの更新

`Map` にキーを更新するメソッドはありません。  
キーを更新したい場合は、そのエントリーを削除して、新しいキーと前回のバリューをセットします。

```java
Map<String, String> map = new HashMap<>();

String currentKey = "key1";
String currentVal = "value1";

map.put(currentKey, currentVal);

String tempVal = map.get(currentKey);
map.remove(currentKey);
map.put("newKey", currentVal);
```


## バリューの更新

```java
// 通常の更新
map.replace(key, newValue);

// キーと現在の値がパラメータに一致する場合のみ、値を更新する
map.replace(key, oldValue, newValue);
```


## エントリーの削除

```java
map.remove(key);
```


## for ループ

### ループ内でキーのみを使用する場合

```java
Map<String, String> map = new HashMap<>();
map.put("key1", "value1");
map.put("key2", "value2");

// keySet() メソッドは、コレクションの Set オブジェクトを返します。
for (String key : map.keySet()) {
  // キーを使用して何かをする
}
```


### ループ内でバリューのみを使用する場合

```java
Map<String, String> map = new HashMap<>();
map.put("key1", "value1");
map.put("key2", "value2");

for (String val : map.values()) {
  // バリューを使用して何かをする
}
```


### ループ内でキーとバリューの両方を使用する場合

```java
Map<String, String> map = new HashMap<>();
map.put("key1", "value1");
map.put("key2", "value2");

for (Map.Entry<String, String> entry : map.entrySet()) {
  String key = entry.getKey();
  String value = entry.getValue());
}
```
