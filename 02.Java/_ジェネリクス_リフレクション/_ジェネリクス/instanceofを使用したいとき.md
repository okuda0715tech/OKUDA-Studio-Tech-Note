<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [instanceof を使用したいとき](#instanceof-を使用したいとき)
  - [概要](#概要)
  - [仮型引数 T の型を判別して、その型にキャストする](#仮型引数-t-の型を判別してその型にキャストする)
  - [Object 型のデータなど、実態の型が不明なデータがジェネリクス型かどうかを判定する](#object-型のデータなど実態の型が不明なデータがジェネリクス型かどうかを判定する)
  - [パラメータが二つの場合は以下を参照](#パラメータが二つの場合は以下を参照)

<!-- /TOC -->


# instanceof を使用したいとき

## 概要

ジェネリクスの型情報はコンパイル時にのみ存在しており、実行時には消えています。  
そのため、 `instanceof` で、型パラメータ `<>` を使用することはできません。

つまり、 `instanceof T` や `T instanceof` や `instanceof List<String>` などの実装を行うことはできません。

仮型引数 `T` などの型情報を取得したい場合には、 `T` の `Class` クラスを取得し、  
そこから型を判別することになります。


## 仮型引数 T の型を判別して、その型にキャストする

```Java
public class NormalClass {

    /**
     * instance を T2 型から T1 型へキャストするメソッド.
     *
     * @param type キャスト先クラスの Class クラスのインスタンス
     * @param instance キャスト元クラスのインスタンス
     * @param <T1> キャスト先クラス
     * @param <T2> キャスト元クラス
     */
    public <T1, T2> void genericMethod(Class<T1> type, T2 instance) {
        // キャスト可能かどうかの判定
        if (type.isAssignableFrom(instance.getClass())) {
            // 実際のキャスト
            type.cast(instance);
        }
    }

}
```


## Object 型のデータなど、実態の型が不明なデータがジェネリクス型かどうかを判定する

型が不明なデータについて、 「ジェネリクスを使用した、ある特定の型かどうか」 を判定するには、  
以下のように原型を使用します。

```Java
    public void execute(Object o) {
        // 原型の Set を使用して判定します。
        // (原型とは、山カッコ <> が付いていないクラス名のことです。)
        if (o instanceof Set){
            // Set の要素が何の型なのかは、要素の中身を確認しないとわからないため、
            // まずは、非境界ワイルドカード型の Set<?> にしかキャストできません。
            Set<?> set = (Set<?>) o;

            // Set<?> にキャストしたら、中身を取り出して、要素の型を確認することは可能です。
            for (Object item : set){
                // 要素の型が String の場合
                if(item instanceof String) {
                    // List<String> 型の変数に詰め替えることが可能です。
                    List<String> stringList = new ArrayList<>();
                    stringList.add(item);
                }
            }
        }
    }
```

原型を使用する正しい機会は、この機会だけです。


## パラメータが二つの場合は以下を参照

この実装は使ったことがないと思われるため、ちゃんと動くのかわからない。

```java
    public static <K, V> HashMap<K, V> castHash(HashMap input,
                                                Class<K> keyClass,
                                                Class<V> valueClass) {

        HashMap<K, V> output = new HashMap<K, V>();

        if (input == null)
            return output;

        for (Object key : input.keySet().toArray()) {
            // key == null の場合は input.get(key) でエラーになりそうなので、
            // この条件はいらない気がするがどうなんだろう。
            if ((key == null) || (keyClass.isAssignableFrom(key.getClass()))) {
                Object value = input.get(key);
                // value == null の場合は valueClass.cast(value) でエラーになりそうなので、
                // この条件はいらない気がするがどうなんだろう。
                if ((value == null) || (valueClass.isAssignableFrom(value.getClass()))) {
                    K k = keyClass.cast(key);
                    V v = valueClass.cast(value);
                    output.put(k, v);
                } else {
                    throw new AssertionError(
                            "Cannot cast to HashMap<" + keyClass.getSimpleName()
                                    + ", " + valueClass.getSimpleName() + ">"
                                    + ", value " + value + " is not a " + valueClass.getSimpleName()
                    );
                }
            } else {
                throw new AssertionError(
                        "Cannot cast to HashMap<" + keyClass.getSimpleName()
                                + ", " + valueClass.getSimpleName() + ">"
                                + ", key " + key + " is not a " + keyClass.getSimpleName()
                );
            }
        }
        return output;
    }
```
