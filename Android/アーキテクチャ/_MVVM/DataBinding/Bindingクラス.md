<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [Bindingクラス](#bindingクラス)
	- [Bindingクラスの生成方法](#bindingクラスの生成方法)
		- [レイアウトファイルからインフレートおよびバインドする場合](#レイアウトファイルからインフレートおよびバインドする場合)
		- [インフレート済みのViewをバインドする場合](#インフレート済みのviewをバインドする場合)
		- [バインディングクラス名が不明な場合](#バインディングクラス名が不明な場合)
		- [Fragment,RecyclerView,ListViewにバイドする場合](#fragmentrecyclerviewlistviewにバイドする場合)
	- [getter/setterメソッド](#gettersetterメソッド)
	- [getRootメソッド](#getrootメソッド)

<!-- /TOC -->

# Bindingクラス

## Bindingクラスの生成方法

### レイアウトファイルからインフレートおよびバインドする場合

**親のViewGroupが存在しない場合**

```java
MyLayoutBinding binding = MyLayoutBinding.inflate(getLayoutInflater());
```

**親のViewGroupが存在する場合**

```java
MyLayoutBinding binding = MyLayoutBinding.inflate(getLayoutInflater(), viewGroup, false);
```

### インフレート済みのViewをバインドする場合

```java
MyLayoutBinding binding = MyLayoutBinding.bind(viewRoot);
```


### バインディングクラス名が不明な場合

```java
View viewRoot = LayoutInflater.from(this).inflate(layoutId, parent, attachToParent);
ViewDataBinding binding = DataBindingUtil.bind(viewRoot);
```

バインディングクラス名が不明の場合は、`DataBindingUtil`クラスを使用する
`ViewDataBinding`クラスは全てのバインディングクラスの親クラス


### Fragment,RecyclerView,ListViewにバイドする場合

```java
ListItemBinding binding = ListItemBinding.inflate(layoutInflater, viewGroup, false);
// or
ListItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.list_item, viewGroup, false);
```

RecyclerView,ListViewの場合は、`ListItemBinding`クラスの部分を実際に作成したリストアイテムのバインディングクラスを指定する。
Fragmentの場合は、`ListItemBinding`クラスの部分を実際に作成したバインディングクラスを指定する。


## getter/setterメソッド

Javaコードからレイアウトファイル内の変数に値をゲット/セットするためのメソッドが自動で生成される。

**MainActivity.java**

```Java
ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
binding.setUser(new User("OKUDA", "Yokohama"));
```


## getRootメソッド

xmlファイルをインフレートしたViewのルートViewを取得する。

**MainActivity.java**

```Java
ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
binding.setUser(new User("TANAKA","TOKYO"));
setContentView(binding.getRoot());
```

以下の二つは同様の意味になる。

`setContentView(binding.getRoot());`
`setContentView(R.layout.activity_main);`


## Bindingクラスのカスタマイズ

### Bindingクラスのクラス名を変更する

```xml
<data class="ContactItem">
    …
</data>
```

- Bindingクラスのクラス名が`ContactItem`になる。
- Bindingクラスの作成場所は`{パッケージ名.databinding}`の直下(デフォルト)のままである。

### Bindingクラスの作成場所をパッケージ名の直下にする

```xml
<data class=".ContactItem">
    …
</data>
```

- Bindingクラスのクラス名が`ContactItem`になる。
- Bindingクラスの作成場所は`{パッケージ名}`の直下になる。

###

```xml
<data class="com.example.ContactItem">
    …
</data>
```

- Bindingクラスのクラス名が`ContactItem`になる。
- Bindingクラスの作成場所は`com.example`の直下になる。


