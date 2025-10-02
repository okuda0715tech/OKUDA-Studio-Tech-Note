<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [Null チェック](#null-チェック)
  - [Objects.requireNonNull(T obj)](#objectsrequirenonnullt-obj)
    - [【注意】](#注意)
    - [概要](#概要)
    - [メソッドの中身](#メソッドの中身)
  - [Objects.requireNonNull(T obj, String message)](#objectsrequirenonnullt-obj-string-message)
  - [Objects.isNull(Object obj)](#objectsisnullobject-obj)
<!-- TOC END -->


# Null チェック

## Objects.requireNonNull(T obj)

### 【注意】

`Object` クラスではなく、 `Objects` クラスなので注意すること。


### 概要

プログラムの読み手には、オブジェクトが `null` ではいけないことを伝えます。

プログラムの書き手は、オブジェクトが `null` ではないことを宣言することで、  
以降の `NullPointerException` ワーニングを抑制します。

もし、 null だった場合には、例外が発生します。  
null ではなかった場合には、引数に渡された値をそのまま返します。


### メソッドの中身

```Java
public static <T> T requireNonNull(T obj) {
    if (obj == null)
        throw new NullPointerException();
    return obj;
}
```


## Objects.requireNonNull(T obj, String message)

上記メソッド `Objects.requireNonNull(T obj)` のエラーメッセージ付きのメソッドです。  
エラーメッセージは、 `NullPointerException` に渡されます。


## Objects.isNull(Object obj)

null かどうかを返すメソッドです。
