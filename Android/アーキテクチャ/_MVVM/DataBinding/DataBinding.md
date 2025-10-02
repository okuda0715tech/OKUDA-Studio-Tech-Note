<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [DataBinding](#databinding)
  - [概要](#概要)
  - [用語](#用語)
  - [導入手順](#導入手順)
    - [1. appのbuild.gradleに追記する。](#1-appのbuildgradleに追記する)
  - [XMLファイルの記述方法](#xmlファイルの記述方法)
  - [ActivityとXMLファイルをバインドする方法](#activityとxmlファイルをバインドする方法)
  - [FragmentとXMLファイルをバインドする方法](#fragmentとxmlファイルをバインドする方法)
  - [XMLファイル内で使用可能なJava文法](#xmlファイル内で使用可能なjava文法)
  - [コレクションやプリミティブ型をバインドする方法](#コレクションやプリミティブ型をバインドする方法)
  - [バインドパラメータによってリソースを切り替える方法](#バインドパラメータによってリソースを切り替える方法)
  - [Stringリソースのプレースフォルダにバインドした値をセットする方法](#stringリソースのプレースフォルダにバインドした値をセットする方法)
  - [XMLからのメソッド呼び出し](#xmlからのメソッド呼び出し)
    - [メソッド参照と呼ばれる方法](#メソッド参照と呼ばれる方法)
    - [リスナーバインディングと呼ばれる方法](#リスナーバインディングと呼ばれる方法)
    - [getterメソッドは特殊](#getterメソッドは特殊)
    - [ラジオボタンのボタン選択時のコールバックメソッドなど、ライブラリが用意してくれているメソッドは特殊な呼び出し方もある。](#ラジオボタンのボタン選択時のコールバックメソッドなどライブラリが用意してくれているメソッドは特殊な呼び出し方もある)
  - [ButtonのonClickは簡単に記述できる](#buttonのonclickは簡単に記述できる)
  - [XMLファイル内で\<import\>する](#xmlファイル内でimportする)
    - [View.VISIBLEなどよく使用する定数の利用](#viewvisibleなどよく使用する定数の利用)
    - [\<import\> した型は \<variable\> タグの type 属性でも使用できます](#import-した型は-variable-タグの-type-属性でも使用できます)
    - [キャスト](#キャスト)
    - [staticフィールド、staticメソッドの参照](#staticフィールドstaticメソッドの参照)
    - [エイリアスの使用](#エイリアスの使用)
  - [データが更新されたら画面を更新する(OneWayDatabinding)](#データが更新されたら画面を更新するonewaydatabinding)
    - [(方法1)フィールドをObservableな型にする方法](#方法1フィールドをobservableな型にする方法)
      - [Observableフィールドの役割](#observableフィールドの役割)
      - [プリミティブな型をObservableにする場合](#プリミティブな型をobservableにする場合)
      - [MapをObservableにする場合](#mapをobservableにする場合)
      - [ListをObservableにする場合](#listをobservableにする場合)
    - [(方法2)BaseObservableを継承する方法](#方法2baseobservableを継承する方法)
    - [(方法3)Observableインターフェースを実装する方法](#方法3observableインターフェースを実装する方法)
  - [include タグで下位のレイアウトに変数を渡す。](#include-タグで下位のレイアウトに変数を渡す)
  - [BindingMethodsアノテーション](#bindingmethodsアノテーション)
  - [BindingAdapterアノテーション](#bindingadapterアノテーション)
    - [複数のパラメータを渡す場合](#複数のパラメータを渡す場合)
    - [複数パラメータが全て必須ではない場合](#複数パラメータが全て必須ではない場合)
  - [ViewModelのObservableフィールドが更新された時にコールバックを呼ぶ方法](#viewmodelのobservableフィールドが更新された時にコールバックを呼ぶ方法)
<!-- TOC END -->


# DataBinding

## 概要

DataBindingとは、レイアウトxmlとデータモデルを直接連携することにより、UIコントローラー(ActivityやFragment)よるデータ連携処理の記述を不要にするライブラリです。

(例)

DataBindingライブラリを使用しない場合は、以下のようにしてデータと画面表示を連動させます。

```Java
TextView textView = findViewById(R.id.sample_text);
textView.setText(viewModel.getUserName());
```

DataBindingライブラリを使用する場合は、以下のようになります。

```XML
<TextView android:text="@{viewmodel.userName}" />
```


## 用語

**バインディングクラス：**

`レイアウトファイル内の変数`と`UIコントローラーのデータフィールド(データモデル)`のマッピングを管理するクラスのこと。

ライブラリによって、自動生成される。

レイアウトファイルごとに一つ作成される。


## 導入手順

1. appのbuild.gradleに追記する。

### 1. appのbuild.gradleに追記する。

```
// Kotlin でデータバインディングを使用する場合はこれも必要
plugins {
    id("kotlin-kapt")
}

android {
    ...
    // 古い記述方法
    dataBinding {
        enabled = true
    }

    // 新しい記述方法
    buildFeatures {
        dataBinding = true
    }
}
```

## XMLファイルの記述方法

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android">
   <data>
       <variable name="user" type="com.example.User"/>
   </data>
   <LinearLayout
       android:orientation="vertical"
       android:layout_width="match_parent"
       android:layout_height="match_parent">
       <TextView android:layout_width="wrap_content"
           android:layout_height="wrap_content"
           android:text="@{user.firstName}"/>
       <TextView android:layout_width="wrap_content"
           android:layout_height="wrap_content"
           android:text="@{user.lastName}"/>
   </LinearLayout>
</layout>
```

- `<layout>`タグで囲む
- `<layout>`タグ内は`<data>`タグとレイアウトのルートタグを配置する
- `<data>`タグ内に`<variable>`タグを記述する
- `<variable>`タグは複数記述可能
- バインドしたい項目は`@{dataタグ内のname.フィールド名}`でバインドする
- `"@{user.firstName}"`は`getFirstName()`を探し、getterがあればそれを使用して参照する。無ければfirstNameフィールドを探す。
	- `firstName()`メソッドが存在する場合には、最優先的に？それに紐付けられます。

## ActivityとXMLファイルをバインドする方法

**MainActivity.java**

```Java
@Override
protected void onCreate(Bundle savedInstanceState) {
   super.onCreate(savedInstanceState);
   // 方法1(パラメータで生成するバインディングクラスが変わるため、動的に生成するバインディングクラスを変更することができる。)
   ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
   binding.setUser(new User("Test", "User"));
   // 方法2(静的なバインディングクラス生成)
   ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
   binding.setUser(new User("Test", "User"));
   setContentView(binding.getRoot());
}
```

- 方法1
	- `Activity.setContentView(R.layout.xxx)`ではなく、`DataBindingUtil.setContentView(Activity, R.layout.xxx)`を使用する
	- バインディングクラス名(上記の例では`ActivityMainBinding`)はレイアウトxmlファイルのファイル名から決まります。
		- xmlファイルのパスカルケース+Bindingがクラス名になります。
- 方法2
	- `activity_main.xml`というレイアウトファイルから生成した`ActivityMainBinding`を参照しているので、レイアウトファイルへの参照が出てこなくてもレイアウトファイルが特定できる。
	- `setContentView(R.layout.xxx)`のパラメータには、`{Bindingクラス}.getRoot()`を指定する。


## FragmentとXMLファイルをバインドする方法

DataBindingを使用する場合と使用しない場合の違いは、`onCreateView`のみです。

**UserFragment.java**

```Java
public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// DataBindingを使用する場合
		UserFragBinding binding = DataBindingUtil.inflate(inflater, R.layout.user_frag, container, false);
		binding.setUser(mUser);
		return binding.getRoot();

		// DataBindingを使用しない場合
		return inflater.inflate(R.layout.user_frag, container, false);
}
```


## XMLファイル内で使用可能なJava文法

| 概要             | Java文法                                                   |
|------------------|------------------------------------------------------------|
| 算術演算         | + - / * %                                                  |
| 文字列連結(※3)   | +                                                          |
| 論理演算         | && 縦棒×2 &(AND) 縦棒(OR) ^(XOR)                           |
| 単行演算子(※1)   | +(足し算) -(引き算) !(論理反転) ~(2進数ビット反転)         |
| シフト演算子     | >> >>> <<                                                  |
| 比較演算子       | == > < >= <= ( < は特殊文字なので \&lt; で代替する)        |
| 型チェック       | instanceof                                                 |
| グループ化       | ()                                                         |
| 扱える型         | Char, String, 数値, null                                   |
| キャスト         | 可能                                                       |
| メソッド呼び出し | 可能                                                       |
| フィールド参照   | 可能                                                       |
| 配列参照         | 可能                                                       |
| 条件判定(※4)     | {条件} ? {真の場合の記述} : {偽の場合の記述}               |
| null判定(※2)(※4) | {nullではない場合に参照する値} ?? {nullの場合に参照する値} |

(※1)
単行演算子：オペランドを一つだけ持つ演算子
(例) `i = 2`の場合
単行演算子：-i は -2
二項演算子：3-i は 1

(※2)
(例) 以下の二つの記述は同じ意味です。  
`android:text="@{user.displayName ?? user.lastName}"`  
`android:text="@{user.displayName != null ? user.displayName : user.lastName}"`

(※3)  
文字列リソースの場合は以下のようにします。

```xml
<TextView
	android:text="@{ @string/month + @string/day }" />
```

(※4)  
真の場合の返値が `LiveData` 型 or `MutableLiveData` 型の場合は、  
偽の場合の返値も `LiveData` 型 or `MutableLiveData` 型である必要があります。  
もし、片方の返値に異なる型が設定されている場合は、以下のビルドエラーとなります。  
```
DataBinderMapperImpl.java:13: エラー: シンボルを見つけられません
import {パッケージ名}.databinding.XxxBindingImpl;
```


## コレクションやプリミティブ型をバインドする方法

XMLファイルとJavaクラス間でオブジェクト単位でバインドするのではなく、コレクションやプリミティブ型単位でバインドすることもできる。

```Xml
<data>
    <import type="android.util.SparseArray"/>
    <import type="java.util.Map"/>
    <import type="java.util.List"/>
    <variable name="list" type="List&lt;String>"/>
    <variable name="sparse" type="SparseArray&lt;String>"/>
    <variable name="map" type="Map&lt;String, String>"/>
    <variable name="index" type="int"/>
    <variable name="key" type="String"/>
</data>
…
android:text="@{list[index]}"
…
android:text="@{sparse[index]}"
…
android:text="@{map[key]}"
```

Mapへのアクセスは、`@{map[key]}`だけでなく、`@{map.key}`でアクセスすることもできます。

文字列リテラル(※1)を使用する場合は、以下の2パターンの表記方法が使用できます。

(※1)リテラルとは、「文字通りの」「ありのままの」という意味があります。
プログラム中で`int`と書けば、整数の変数の型を意味しますが、
`"abc"`と書けば、abcという文字の意味を表します。
つまり、プログラム中で「この文字は特別な意味はなく、書いた通りの意味ですよ」という場合に、
その文字を文字列リテラルといいます。

1.`@{}`をシングルクォーテーション`'`で囲み、文字列をダブルクォーテーション`"`で囲む方法

```XML
<TextView
	android:text='@{map["firstName"]}'>
</TextView>
```

2.`@{}`をダブルクォーテーション`"`で囲み、文字列をバッククォーテーション`で囲む方法

Macでバッククォーテーションを入力するには「SHIFT + @」をタイプします。

```XML
<TextView
	android:text="@{map[`firstName`]}">
</TextView>
```


## バインドパラメータによってリソースを切り替える方法

```Xml
<TextView
	android:padding="@{large? @dimen/largePadding : @dimen/smallPadding}">
</TextView>
```

trueなら左、falseなら右の式が適用されます。


## Stringリソースのプレースフォルダにバインドした値をセットする方法

Viewでstrings.xmlの定型文を参照したいが、定型文の一部はデータを入れ替え可能にして、データに応じた定型文を表示したい場合は、以下のようにします。

**strings.xml**

```XML
<string name="title">Title:%1$s</string>
```

**layout/sample.xml**

```Xml
<TextView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text='@{@string/title(article.title)}' />
```

strings.xmlの`Title:%1$s`についてですが、
`1$`は`title(article.title)`の第一引数の値をセットすることを意味します。
もし、`2$`なら`title(article.title, article.author)`などの第二引数の値をセットすることを意味します。
`%s`は文字列リテラルを表し、`%d`なら数値を表します。


## XMLからのメソッド呼び出し

大きく分けて`メソッド参照`と呼ばれる方法と`リスナーバインディング`と呼ばれる方法の二通りの実装方法が存在する。

### メソッド参照と呼ばれる方法

**特徴**

1. コンパイル時に文法チェックされる
2. コールバック関数に渡せるパラメータは、タップされたViewのみ
3. 記述方法が多少シンプルになる

**sample.xml**

```Xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android">
<data>
	<variable name="listener" type="com.example.Sample"/>
</data>
<Button
	 android:layout_width="300dp"
	 android:layout_height="wrap_content"
	 android:text="SEND"
	 android:onClick="@{listener::onClickSend}"/>
</layout>
```

**Sample.java**

```Java
public class Sample {
		public void onClickSend(View view){
		// do something
		// 【注意】メソッド定義のパラメータが空だと以下のコンパイル時エラーが出るので、必ず「(View view)」を記述すること。
		// エラー: シンボルを見つけられません
		// シンボル:   クラス XXXBindingImpl
		// 場所: パッケージ {パッケージ名}.databinding
		}
}
```


### リスナーバインディングと呼ばれる方法

**特徴**

1. 実行時に文法チェックされる
2. 任意のパラメータをコールバック関数のパラメータに渡せる
3. 記述方法が多少複雑になる

**Sample1.xml**

```XML
<Button
    android:layout_width="300dp"
    android:layout_height="wrap_content"
    android:text="INVITE"
    android:onClick="@{() -> handler.onClickInvite(user)}"
    />
```

`() ->`の`()`は、`(view)`の省略表記です。
そのため、`(view) ->`という記述にしても動作は変わりません。
ただし、いづれの場合もjavaコードのコールバック関数のパラメータにタップされたViewを渡すことはできません。
Viewを渡したい場合は、Sample2のように記述する必要があります。

**Sample1.java**

```java
public void onClickInvite(User user){
    // do something
}
```


**Sample2.xml**

```XML
<Button
    android:layout_width="300dp"
    android:layout_height="wrap_content"
    android:text="SHARE"
    android:onClick="@{(view) -> handler.onClickShare(view, user)}"
    />
```

Sample1とは違い`(view) ->`の`view`を省略することはできないため、注意してください。
Sample1との違いは、`handler.onClickShare(view, user)`の引数に`view`が指定されていることだけです。

**Sample2.java**

```java
public void onClickShare(View view, User user){
    // do something
}
```


### getterメソッドは特殊

メソッドはメソッドでも、getterメソッドの呼び出しは特別であり、フィールド参照と同じ記述方法で呼び出せる。

**layout.xml**

```Xml
<Button android:enabled="@{viewModel.btnEnabled}" …/>
```

**Sample.java**

```java
public boolean getBtnEnabled() {
		return !TextUtils.isEmpty(name);
}    
```


### ラジオボタンのボタン選択時のコールバックメソッドなど、ライブラリが用意してくれているメソッドは特殊な呼び出し方もある。

```xml
<RadioGroup
		android:layout_width="match_parent"
		android:layout_height="wrap_content"
		android:onCheckedChanged="@{listener.myCheckedChanged}">

		<RadioButton
				android:id="@+id/button1"
				android:layout_width="match_parent"
				android:layout_height="wrap_content"
				android:text="button1" />

		<RadioButton
				android:id="@+id/button2"
				... />
</RadioGroup>
```

```Java
public class MainActivity extends AppCompatActivity {
	public void myCheckedChanged(RadioGroup radioGroup, int checkedRadioButtonId){
		RadioButton radioButton = findViewById(checkedRadioButtonId);
		Snackbar.make(getRootView(), radioButton.getText(), Snackbar.LENGTH_SHORT).show();
	}
}
```

上記のサンプルの通り、xmlファイルでは、`android:onCheckedChanged="@{listener.myCheckedChanged}"`のようにパラメータなしでメソッドを呼んでいるが、Activityあるメソッド定義側では、自動的に`RadioGroup`と`checkedRadioButtonId`のパラメータが渡されている。

このような場合は、その都度、そのライブラリにあった呼び出し方を行う必要がある。


## ButtonのonClickは簡単に記述できる

`android:onClick`属性に文字列でメソッド名を記載するだけで、レイアウトxmlをホストするActivityの同名のメソッドが呼べる。

この方法は、あくまでもActivityの同名メソッドを呼ぶためのものであり、Fragmentの同名メソッドを呼ぶことはできない。

```xml
<button
	...
	android:onClick="onMyButtonClick"/>
```

```java
public class MainActivity ...{
	public void onMyButtonClick(View view){
		// do something.
	}
}
```


## XMLファイル内で\<import>する

XMLファイルの\<data>タグ内で\<import>タグを使って、クラスをインポートすることができます。インポートすると以下のことが可能になります。

1. `View.VISIBLE`など、XMLに普段記載する定数をバインド式内でも参照できる。
2. インポートしたクラスは、`<data>`タグ内の`<variable>`タグの`type`属性にパッケージ名を省略して記載することができる。
3. バインド式内で型キャストができる。
4. バインド式内でstaticフィールド、staticメソッドを参照できる。


### View.VISIBLEなどよく使用する定数の利用

**ConstantSample.xml**

```Xml
<data>
    <import type="android.view.View"/>
</data>
...
<TextView
   android:text="@{user.lastName}"
   android:layout_width="wrap_content"
   android:layout_height="wrap_content"
   android:visibility="@{user.isAdult ? View.VISIBLE : View.GONE}"/>
```


### \<import> した型は \<variable> タグの type 属性でも使用できます

**TypeSample.xml**

```XML
<data>
    <import type="com.example.User"/>
    <import type="java.util.List"/>
    <variable name="user" type="User"/>
    <variable name="userList" type="List&lt;User>"/>
</data>
```


### キャスト

**CastSample.xml**

```Xml
<TextView
   android:text="@{((User)(user.connection)).lastName}"
   android:layout_width="wrap_content"
   android:layout_height="wrap_content"/>
```

`user.connection`を`User`にキャストするサンプルです。


### staticフィールド、staticメソッドの参照

**StaticSample.xml**

```Xml
<data>
    <import type="com.example.MyStringUtils"/>
    <variable name="user" type="com.example.User"/>
</data>
…
<TextView
   android:text="@{MyStringUtils.capitalize(user.lastName)}"
   android:layout_width="wrap_content"
   android:layout_height="wrap_content"/>
```


### エイリアスの使用

異なるパッケージ内にある同一クラス名のクラスをインポートする場合は、エイリアスを設定します。

```Xml
<import type="android.view.View"/>
<import type="com.example.real.estate.View"
        alias="EstateView"/>
```

`View`を参照した場合には、`android.view.View`を参照し、`EstateView`を参照した場合には、`com.example.real.estate.View`を参照します。


## データが更新されたら画面を更新する(OneWayDatabinding)

実装方法には、三つの方法がある。
以下で順に説明する。


### (方法1)フィールドをObservableな型にする方法

#### Observableフィールドの役割

Javaクラス側のObservableフィールドを更新した時に、そのフィールドにバインドしたxmlファイル側の変数を更新する役割がある。  
逆に言うと、Observableフィールドにしていない場合は、Javaクラス側のフィールドを更新しても、xmlファイル側の変数は更新されない。


#### プリミティブな型をObservableにする場合

**User.java**

```Java
public class User {
	// 初期値をインスタンス生成時に与えない場合
	public final ObservableField<String> mName = new ObservableField<>();
	// 初期値をインスタンス生成時に与える場合
	public final ObservableField<String> mName = new ObservableField<>("initial value");

	public User(String name) {
		this.mName.set(name);
	}
}
```

- フィールドが`Observable<String>`型になるので、`{フィールド名}.set({文字列})`や`{フィールド名}.get()`でアクセスする。
- フィールドの初期値にObservableFieldをnewしてセットしておく必要がある。
- プリミティブ型には`ObservableInt`や`ObservableFloat`など、各型に対応した型が用意されている。
- 参照型のオブジェクト用に、`ObservableParcelable`型が用意されている。
- `Observable`フィールドが監視しているオブジェクトが入れ替わるのを防ぐために、一般的に、フィールドは`final`で定義する。
- `Observable`なフィールドは`public`にしなければいけない？
	- Activity内のフィールドに定義した`Observable`は`private`で問題なかった。


**MainActivity.Java**

```Java
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);

	ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
	User user = new User("sample_name");
	// パターン1：ObservableFieldを包んだクラスをパラメータで渡す場合
	binding.setUser(user);
	// パターン2：ObservableFieldを直接パラメータで渡す場合
	binding.setUser(user.mName);
	binding.setListener(this);
	setContentView(binding.getRoot());
}
```


**activity_main.xml**

```xml
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <data>
        <variable
            name="userFrag"
            type="com.kurodai0715.mydatabindingobservablefield.UserFragment" />
        <!-- パターン1
        ObservableFieldを包んだクラスをパラメータで受け取る場合
        -->
        <variable
            name="user"
            type="com.kurodai0715.mydatabindingobservablefield.User" />
        <!-- パターン2
        ObservableFieldを直接パラメータで受け取る場合
        「&gt;」は「>」としてもOK
        -->
        <import type="androidx.databinding.ObservableField" />
        <variable
            name="userName"
            type="ObservableField&lt;String&gt;" />
    </data>
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical">

        <!-- パターン1
        ObservableFieldを包んだクラスをパラメータで受け取る場合
        -->
        <!-- データバインディング式(@{})内では、
        Observableフィールドを直接参照します。
        ObservableField.get()のようにgetメソッドを使用すると
        コンパイルエラーとなってしまいます。 -->
        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@{user.mName}" />

        <!-- パターン2
        ObservableFieldを直接パラメータで受け取る場合
        -->
        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@{userName}" />
    </LinearLayout>
</layout>
```



#### MapをObservableにする場合

**Map.java**

```java
ObservableArrayMap<String, Object> user = new ObservableArrayMap<>();
user.put("firstName", "Google");
user.put("lastName", "Inc.");
user.put("age", 17);
```

**layout.xml**

```xml
<data>
    <import type="android.databinding.ObservableMap"/>
    <variable name="user" type="ObservableMap&lt;String, Object>"/>
    <!-- 閉じカッコは「&gt;」と記載してもOK。開きカッコは必ず「&lt;」とする必要がある。 -->
</data>
…
<TextView
    android:text="@{user.lastName}"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"/>
<TextView
    android:text="@{String.valueOf(1 + (Integer)user.age)}"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"/>
```


#### ListをObservableにする場合

**List.java**

```java
ObservableArrayList<Object> user = new ObservableArrayList<>();
user.add("Google");
user.add("Inc.");
user.add(17);
```

**layout.xml**

```xml
<data>
    <import type="android.databinding.ObservableList"/>
    <import type="com.example.my.app.Fields"/>
    <variable name="user" type="ObservableList&lt;Object>"/>
</data>
…
<TextView
    android:text='@{user[Fields.LAST_NAME]}'
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"/>
<TextView
    android:text='@{String.valueOf(1 + (Integer)user[Fields.AGE])}'
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"/>
```


### (方法2)BaseObservableを継承する方法

**Sample.xml**

```xml
<TextView android:layout_width="wrap_content"
		android:layout_height="wrap_content"
		android:text="@{user.name}"/>
```

`getName()`メソッドで参照する。

**User.java**

```Java
public class User extends BaseObservable {

    private String mName;

    public User(String name) {
        this.mName = name;
    }

    @Bindable
    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
        notifyPropertyChanged(BR.name);
    }
}
```

**実装方法**

- BaseObservableを継承する。
- getter/setterを作成する。（利便性向上のため）
- getterに`@Bindable`アノテーションを付与する。
- getterではなく、`private String mName`の一行上に`@Bindable`を付与しても大丈夫そう？(公式ドキュメントには記載されていないがそういうコードが世の中にはある)
- `@Bindable`は、定数`BR.name`を生成するために必要
- set完了後にViewへ通知するため`notifyPropertyChanged`メソッドを呼ぶ。
- `notifyPropertyChanged`が呼ばれるとViewがgetterを呼び出して最新の値を取得する。

**注意点**

- `BR`クラスはビルドしないと作成されない。(BR:Binding Resourceの略と思われる)
- `notifyPropertyChanged()`メソッドをパラメータなしで記述するとビルドエラーになるので、BRクラスが作成できないので、一旦削除してからビルドを通す必要がある。
- `BR`クラスのプロパティ名はgetterの`getXXX()`の`XXX`から作成される
- `BR`クラスは二つ存在するが、使用するのは、パッケージ名の直下に作成されるBRクラスである。
	- ただし、android studioのバグ？のせいか、`BR.フィールド名`のフィールド名の部分がエラー表示になることがある。
	- (エラー表示になるが、コンパイルもできるし、動作に問題はない。ただ、他のエラーを見落としてしまうかもしれない。)
	- そのため、エラー表示になる場合は、`android.databinding.library.baseAdapters`直下のBRクラスを使用すると良い。(import文でそちらを参照させる。)
	- 内容はどちらのBRクラスも同じであるため、動作に差異は起こらない。


### (方法3)Observableインターフェースを実装する方法

Javaでは一つのクラスしか継承できないので、implementsを使ったこちらの方法の方が使いやすい。実装方法は`BaseObservable`を継承する方法とほとんど同じ。

**Sample.xml**

```xml
<TextView android:layout_width="wrap_content"
		android:layout_height="wrap_content"
		android:text="@{user.name}"/>
```

`getName()`メソッドで参照する。

**User.java**

```Java
public class User implements Observable {

    private PropertyChangeRegistry registry = new PropertyChangeRegistry();
    private String mName;

    public User(String name, String address) {
        this.mName = name;
        this.mAddress = address;
    }

    @Bindable
    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
        registry.notifyChange(this, BR.name);
    }

    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        registry.add(callback);
    }

    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        registry.remove(callback);
    }
}
```

- 基本的には`BaseObservable`を継承する場合と同じ
- 異なる部分は以下
	- addとremoveのオーバーライドメソッドの実装が必要(定形文)
	- setterの通知メソッドが変わった
	- `PropertyChangeRegistry`オブジェクトをフィールドに保持する必要がある


## include タグで下位のレイアウトに変数を渡す。

**parent_layout.xml**

```Xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:bind="http://schemas.android.com/apk/res-auto">
   <data>
       <variable name="user" type="com.example.User"/>
   </data>
   <LinearLayout
       android:orientation="vertical"
       android:layout_width="match_parent"
       android:layout_height="match_parent">
       <include layout="@layout/name"
           bind:user="@{user}"/>
       <include layout="@layout/contact"
           bind:user="@{user}"/>
   </LinearLayout>
</layout>
```

上記の例では、name.xmlのバインド変数`user`とcontact.xmlのバインド変数`user`に、parent_layout.xmlのバインド変数`user`をセットしています。


## BindingMethodsアノテーション

`setOnClickListener(Listener listener)`などのイベントリスナーのリスナー設定をバインド式で設定したい時に使用します。

**Sample.java**

```java
@BindingMethods({
       @BindingMethod(type = "android.widget.ImageView",
                      attribute = "android:tint",
                      method = "setImageTintList"),
})
```

上記の記述はどのクラスに描いてもOKらしい。

**layout.xml**

```Xml
<ImageView
		android:layout_width="wrap_content"
		android:layout_height="wrap_content"
		android:tint="@{fragment}"/>
```

上記の例は、`ImageView`クラスの`setImageTintList(Listener listener)`メソッドのパラメータに`fragment`を与えた`setImageTintList(fragment)`と同等になる。

`attribute = "android:tint"`の`"android:tint"`部分は任意の名前を指定できます。


## BindingAdapterアノテーション

`BindingMethods`アノテーションは、レイアウトファイルで指定したパラメータをそのまま既存のsetメソッドでセットする場合に使用する。
対して、`BindingAdapter`アノテーションは、レイアウトファイルで指定したパラメータをsetする前にJavaコードで何らかの処理をする必要がある場合に使用する。

**Sample.java**

```Java
@BindingAdapter("android:paddingLeft")
public static void setPaddingLeft(View view, int padding) {
  view.setPadding(padding,
                  view.getPaddingTop(),
                  view.getPaddingRight(),
                  view.getPaddingBottom());
}
```

- メソッドの第一引数は属性を指定した自分自身のViewを渡すようにメソッドを定義します。

<補足>
`"android:paddingLeft"`は既存の属性だけど、`"android:paddingLeft"="5dp"`など、単位を指定しなければいけないので、int型のパラメータは指定できない。int型のパラメータを指定するなら例のようにJavaコードで`setPadding`メソッドを使用する実装方法になる。


### 複数のパラメータを渡す場合

**Sample.java**

```java
@BindingAdapter({"imageUrl", "error"})
public static void loadImage(ImageView view, String url, Drawable error) {
  Picasso.get().load(url).error(error).into(view);
}
```

**layout.xml**

```XML
<ImageView app:imageUrl="@{venue.imageUrl}" app:error="@{@drawable/venueError}" />
```

<疑問点>
レイアウトファイルの属性が`"app:XXX"`の場合は、Javaコードの`BindingAdapter`アノテーションのパラメータは`app:`部分が省略可能なのか？


### 複数パラメータが全て必須ではない場合

```java
@BindingAdapter(value={"imageUrl", "placeholder"}, requireAll=false)
public static void setImageUrl(ImageView imageView, String url, Drawable placeHolder) {
  if (url == null) {
    imageView.setImageDrawable(placeholder);
  } else {
    MyImageLoader.loadInto(imageView, url, placeholder);
  }
}
```

`BindingAdapter`アノテーションのパラメータに`requireAll=false`を設定します。


## ViewModelのObservableフィールドが更新された時にコールバックを呼ぶ方法

以下の例は、ViewModelの`SnackbarText`が更新されたタイミングで、Snackbarを表示するサンプルである。
Snackbarを表示するには、パラメータに`View`が必要だが、ViewModelから`View`を参照するのは、MVVMアーキテクチャの概念に反するため、できない。そのため、View(Fragment)側でSnackbarを表示するコードを記述する必要がある。
具体的には、以下のフローでSnackbarを表示する。

1. ユーザーが画面から入力を行う。
2. ViewModel側でSnackbarTextに表示する文字列をセットする
3. SnackbarTextの値変更をView側のコールバック関数で受け取り、Snackbarを表示する。

上記のように、画面入力が行われた場合に、別のVeiwを更新したい場合は、ViewModelのObservableフィールドの変更を検知するコールバックをViewに持たせて、その中でViewを更新する。

**SampleFragment.java**

```java
public class TaskDetailFragment extends Fragment{
	private void setupSnackbar() {
		mSnackbarCallback = new Observable.OnPropertyChangedCallback() {
				@Override
				public void onPropertyChanged(Observable observable, int i) {
						SnackbarUtils.showSnackbar(getView(), mViewModel.getSnackbarText());
				}
		};
		mViewModel.snackbarText.addOnPropertyChangedCallback(mSnackbarCallback);
	}
}
```

コールバックの登録方法

`{監視したいObservableフィールド}.addOnPropertyChangedCallback(変更時に呼ばれるコールバックのインスタンス)`

変更時に呼ばれるコールバックのインスタンスは、クラス名が`Observable.OnPropertyChangedCallback`、コールバックメソッド名が`void onPropertyChanged(Observable observable, int i)`である。
