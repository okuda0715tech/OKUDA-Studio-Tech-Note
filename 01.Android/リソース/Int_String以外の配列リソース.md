- [Int\_String以外の配列リソース](#int_string以外の配列リソース)
  - [概要](#概要)
  - [例](#例)
    - [文字列配列の場合](#文字列配列の場合)
    - [Int 配列の場合](#int-配列の場合)
    - [Drawable 配列の場合](#drawable-配列の場合)
    - [Color 配列の場合](#color-配列の場合)


# Int_String以外の配列リソース

## 概要

Android では文字列などのリソースを xml で定義することができる。

このリソースファイルでは文字列だけでなく、配列も定義できるので  
コードの外に出せるものはできるだけ xml に書き出すのが良いでしょう。

String, Integer はそれぞれ専用のタグ名、アクセスメソッドがあるが、  
その他のリソースは用意されていないため TypedArray で定義する必要があります。


## 例

### 文字列配列の場合

```xml
<string-array name="sample_names">
	<item>foo</item>
	<item>bar</item>
	<item>baz</item>
</string-array>
```

```java
String[] names = getResources().getStringArray(R.array.sample_names);
```


### Int 配列の場合


```xml
<integer-array name="sample_ids">
	<item>1</item>
	<item>2</item>
	<item>3</item>
</integer-array>
```

```java
int[] ids = getResources().getStringArray(R.array.sample_ids);
```


### Drawable 配列の場合

```xml
<array name="sample_images">
	<item>@drawable/title</item>
	<item>@drawable/logo</item>
	<item>@drawable/icon</item>
</array>
```

```java
TypedArray images = getResources().obtainTypedArray(R.array.sample_images);
Drawable drawable = images.getDrawable(0));
```


### Color 配列の場合

```xml
<array name="sample_colors">
	<item>#FFFF0000</item>
	<item>#FF00FF00</item>
	<item>#FF0000FF</item>
</array>
```

```java
TypedArray colors = getResources().obtainTypedArray(R.array.sample_colors);
int color = colors.getColor(0,0);
```


