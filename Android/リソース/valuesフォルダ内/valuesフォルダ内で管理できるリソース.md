<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [valuesフォルダ内で管理できるリソース](#valuesフォルタ内て管理てきるリソース)
	- [基本的なルールや仕組み](#基本的なルールや仕組み)
		- [ファイル名は基本的には任意である](#ファイル名は基本的には任意である)
		- [タグの種類で、それが何のリソースなのかを判定している](#タグの種類でそれが何のリソースなのかを判定している)
		- [ファイルのルートタグは `<resources>` とする](#ファイルのルートタグは--とする)
		- [Javaコードからリソースを取得する場合は、 `getResources().getXXX()` で取得する](#javaコードからリソースを取得する場合は-getresourcesgetxxx-で取得する)
	- [values ディレクトリ配下で管理できるリソース一覧（タグ一覧）](#values-ディレクトリ配下で管理できるリソース一覧タグ一覧)
	- [integer-array の扱い方](#integer-array-の扱い方)
	- [string-array の扱い方](#string-array-の扱い方)
	- [その他の特定の型を配列で扱う方法](#その他の特定の型を配列で扱う方法)
		- [例](#例)
<!-- TOC END -->


# valuesフォルダ内で管理できるリソース

## 基本的なルールや仕組み

### ファイル名は基本的には任意である

ファイル名は任意だが、一般的なファイル名は、タグ名の複数形である。  
例えば、 `<string>` タグなら、 strings.xml とするのが一般的である。

他には、アプリの設定情報を管理するファイルに、 configuration.xml と名づけるのもあり。


### タグの種類で、それが何のリソースなのかを判定している

タグによって、扱っている値が数値型の値なのか文字列型の値なのかなど、型の判定を行っている。


### ファイルのルートタグは `<resources>` とする

### Javaコードからリソースを取得する場合は、 `getResources().getXXX()` で取得する

`getXXX()` の `XXX` 部分には、各リソースのタグに応じた名前が入る。  
例 : `<integer>` の場合には、 `getInt()`


## values ディレクトリ配下で管理できるリソース一覧（タグ一覧）

| 格納するもの                                 | タグ                          | R クラスで参照する場合 |
|----------------------------------------------|-------------------------------|------------------------|
| ブール値                                     | \<bool>                       | R.bool.name            |
| 整数                                         | \<integer>                    | R.integer.name         |
| 整数配列                                     | \<integer-array>              | R.array.name           |
| 文字列                                       | \<string>                     | R.string.name          |
| 文字列配列                                   | \<string-array>               | R.array.name           |
| サイズ（文字サイズやViewのサイズ）           | \<dimen>                      | R.dimen.name           |
| 色                                           | \<color>                      | R.color.name           |
| スタイル（パディングや背景色などViewの設定） | \<style>                      |                        |
| ID                                           | \<item type="id" name="xxx"/> | R.id.name              |
| 型付き配列（任意のリソースの配列）           | \<array>                      | R.array.name           |


## integer-array の扱い方

```Xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
  <integer-array name="color_list">
    <item>@color/color1</item>
    <item>@color/color2</item>
    <item>@color/color3</item>
  </integer-array>
</resources>
```

```Java
int[] integerArray = getResources().getIntArray(R.array.color_list);
```


## string-array の扱い方

```Xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
  <string-array name="color_list">
    <item>@string/red</item>
    <item>@string/blue</item>
    <item>@string/yellow</item>
  </string-array>
</resources>
```

```Java
String[] stringArray = getResources().getStringArray(R.array.color_list);
```


## その他の特定の型を配列で扱う方法

`int`型、`String`型以外の型でも、よく使用される型については配列で扱うことができます。

取り扱い可能な型については、以下のドキュメントの`public method`で`getXXX`の`XXX`から確認できる。

[android.content.res.TypedArray](https://developer.android.com/reference/android/content/res/TypedArray?hl=ja#public-methods)


### 例

```Xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
  <array name="icons">
    <item>@drawable/home</item>
    <item>@drawable/settings</item>
    <item>@drawable/logout</item>
  </array>
  <array name="colors">
    <item>#FFFF0000</item>
    <item>#FF00FF00</item>
    <item>#FF0000FF</item>
  </array>
</resources>
```

```Java
Resources res = getResources();
TypedArray icons = res.obtainTypedArray(R.array.icons);
Drawable drawable = icons.getDrawable(0);

TypedArray colors = res.obtainTypedArray(R.array.colors);
int color = colors.getColor(0,0);
```
