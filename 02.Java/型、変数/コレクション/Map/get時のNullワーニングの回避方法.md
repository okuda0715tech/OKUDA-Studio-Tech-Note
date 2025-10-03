<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [get 時の Null ワーニングの回避方法](#get-時の-null-ワーニングの回避方法)
  - [ワーニングが発生する状況](#ワーニングが発生する状況)
  - [ワーニングの回避方法](#ワーニングの回避方法)
<!-- TOC END -->


# get 時の Null ワーニングの回避方法

## ワーニングが発生する状況

以下の例のように Map のバリューをゲットした時に、そのまま if 文判定などに使用するとワーニングが出力されます。

```java
Map<String, Boolean> myMap = new HashMap<>();

// ここにマップにプットする処理があるとする
// ...

if (myMap.get("key1")) { // <--- ここでワーニングが発生する。
  // do something.
}
```

ワーニングの内容は以下の通りです。

`Unboxing of 'myMap.get("key1")' may produce 'NullPointerException'`


## ワーニングの回避方法

上記のワーニングを回避するために一番良いと思われる方法は、以下のユーティリティを使用して  
バリューを取得することです。

```java
public class MapUtil {

    @NonNull
    public static <K, V> V getNotNullVal(Map<K, V> map, K key) {
        V value = map.get(key);
        if (value == null){
            throw new NullPointerException();
        }
        return value;
    }

}
```

`map.get(key)` メソッドを使用してバリューを取得するのではなく、上記のメソッドを使用するのが良いです。
