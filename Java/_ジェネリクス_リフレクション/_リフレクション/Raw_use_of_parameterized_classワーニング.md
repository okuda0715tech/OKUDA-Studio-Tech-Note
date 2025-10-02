<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [Raw_use_of_parameterized_classワーニング](#rawuseofparameterizedclass)
	- [概要＆対処方法](#概要対処方法)

<!-- /TOC -->


# Raw_use_of_parameterized_classワーニング

## 概要＆対処方法

`class Class<T>`など、型引数を持つクラスをメソッドの引数に指定するとタイトルのワーニングが出力される。

例えば、以下のようなメソッドを定義した場合、メソッド引数の`Class`部分にワーニングが出力される。

```Java
public static void enable(Context context, Class receiver){
  // do something.
}
```

この`Class`クラスの型引数にどんな型が入るか決まっていない場合は、以下のようにすることでワーニングを解消することができる。

```Java
public static void enable(Context context, Class<?> receiver){
  // do something.
}
```
