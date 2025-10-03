<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [Classクラス](#classクラス)
	- [概要](#概要)
	- [Classクラスの取得方法](#classクラスの取得方法)
		- [インスタンスを未生成の場合](#インスタンスを未生成の場合)
		- [インスタンスを生成済みの場合](#インスタンスを生成済みの場合)
	- [文字列をインプットとして、そのクラスのClassクラスを取得する方法](#文字列をインプットとしてそのクラスのclassクラスを取得する方法)
	- [インスタンスからそのインスタンスのクラス名を取得する方法](#インスタンスからそのインスタンスのクラス名を取得する方法)

<!-- /TOC -->


# Classクラス

## 概要

Classクラスは、クラスのメタデータを保持するクラスである。


## Classクラスの取得方法

### インスタンスを未生成の場合

`クラス名.class`とすることで、インスタンスを取得することができます。

(例)

```Java
Class<String> clazz = String.class;
```


### インスタンスを生成済みの場合

`getClass()`メソッドで、インスタンスを取得することができます。

(例)

```Java
String s = "abc";
Class<? extends String> clazz = s.getClass()
```

**注意点**

`getClass()`メソッドを使用した場合、コンパイラでは、変数の見かけ上の型しか判別できません。
上記の例の場合、変数`s`は、String型で定義されていますが、実際に格納されている型は`String型`とは限りません。
もしかしたら、String型を継承した別の型であるかもしれません。
そのため、`getClass()`メソッドで取得したClassクラスは`<? extends XXX>`の形か`<?>`の形で定義した変数に格納しなければいけません。


## 文字列をインプットとして、そのクラスのClassクラスを取得する方法

`forName()`メソッドは、文字列をインプットとして、そのクラスのClassクラスを取得します。

指定するクラス名はパッケージ名まで含めた完全修飾名でなければなりません。

```Java
Class<?> clazz = null;
try{
    clazz = Class.forName("java.lang.String");
}catch(ClassNotFoundException ex){
    ex.printStackTrace();
}
```


## インスタンスからそのインスタンスのクラス名を取得する方法

```java
Date d = new Date();
// パッケージ名込み
String str = d.getClass().getCanonicalName(); // java.util.Date
// クラス名のみ
String str = d.getClass().getSimpleName(); // Date
```
