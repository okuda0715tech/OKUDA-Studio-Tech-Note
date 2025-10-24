<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [Style](#style)
  - [Styleとは](#styleとは)
  - [Styleの定義方法](#styleの定義方法)
  - [サンプルコード](#サンプルコード)
    - [Styleを読み込む側のXML](#styleを読み込む側のxml)
    - [読み込まれるStyleのXML](#読み込まれるstyleのxml)
  - [Styleの継承](#styleの継承)
    - [基本形](#基本形)
    - [Android Platform の Style を継承する場合](#android-platform-の-style-を継承する場合)
    - [自作のStyleを継承する場合](#自作のstyleを継承する場合)
    - [parent属性による継承と .(ドット) による継承の両方が記述されている場合](#parent属性による継承と-ドット-による継承の両方が記述されている場合)
    - [parent=""は何も継承しないことを明示する手法](#parentは何も継承しないことを明示する手法)
  - [指定できる属性の確認方法](#指定できる属性の確認方法)

<!-- /TOC -->

# Style

## Styleとは

View単位で設定するCSS的なXMLファイルのこと。  
指定したViewにのみ有効であり、ViewGroupに指定したとしても、子Viewには適用されません。  
子Viewにも適用させるには、ThemeとしてStyleを適用する必要があります。


## Styleの定義方法

1.  `res/values/` ディレクトリ内に XML ファイルを作成する。
2.  ファイルのルート属性は`<resources>`とする。
3.  `<style>`タグを記載し、その中に`<item>`タグで各スタイルの設定を行う。
4. Styleを読み込む側のXMLは`style="@style/{<style>タグのname}"`でXMLを読み込む。


## サンプルコード

### Styleを読み込む側のXML

**sample_layout.xml**

```XML
<TextView
    style="@style/CodeFont"
    android:text="@string/hello" />
```

style属性には`android:`名前空間をつけません。

### 読み込まれるStyleのXML

**style_sample.xml**

```XML
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <style name="CodeFont" parent="@android:style/TextAppearance.Medium">
        <item name="android:layout_width">fill_parent</item>
        <item name="android:layout_height">wrap_content</item>
        <item name="android:textColor">#00FF00</item>
        <item name="android:typeface">monospace</item>
    </style>
</resources>
```

もし、`<item>`タグの`name`属性で指定した値（`android:xxx`など）が、そのスタイルを読み込んでいるViewで指定できない場合は、その指定は単に無視されます。エラーにはなりません。

<属性の説明>

| タグ     | 必須 | 属性   | 必須 | 属性の値                                             | タグの内容       |
|----------|------|--------|------|------------------------------------------------------|------------------|
| <style\> | ◯    | name   | ◯    | Javaコードからstyleタグを一意に識別するための名前    | <item\>タグ      |
| <style\> | ◯    | parent | -    | 別のStyleを継承してStyleを作成する場合に使用します。 |                  |
| <item\>  | ◯    | name   | ◯    | 通常レイアウトXMLに指定している属性                  | 属性に指定する値 |


## Styleの継承

### 基本形

```xml
<style name="GreenText" parent="@android:style/TextAppearance">
    <item name="android:textColor">#00FF00</item>
</style>
```

`name` 属性にはこのスタイルの名前、 `parent` 属性には継承元のスタイルを定義します。  
`<style>` タグの内部には、 `<item>` タグでスタイルを追加します。


### Android Platform の Style を継承する場合

Android Platform からスタイルを継承する場合は、  
`@android:style/` を `parent` 属性の先頭に付与します。  
Androidx や自アプリで定義したスタイルを継承する場合は、この接頭辞は必要ありません。

```xml
<style name="GreenText" parent="@android:style/TextAppearance">
    <item name="android:textColor">#00FF00</item>
</style>
```

上記は `TextAppearance` という Platform の Style を継承し、 `GreenText` という Style を作成する場合の例です。


### 自作のStyleを継承する場合

自作のスタイルを継承する場合のみ、 `parent` 属性を使用せず、 `.(ドット)` で継承することができます。

```xml
<style name="CodeFont.Red">
    <item name="android:textColor">#FF0000</item>
</style>
```

上記は `CodeFont` という自作のStyleを継承し、 `CodeFont.Red` というStyleを作成する場合の例です。  
このStyleを読み込む場合は、 `@style/CodeFont.Red` と記載します。

継承は何階層でも可能です。


### parent属性による継承と .(ドット) による継承の両方が記述されている場合

`parent` 属性が優先され、 `parent` 属性を継承します。


### parent=""は何も継承しないことを明示する手法

`<style>` タグの `parent` 属性に空文字を指定した場合、その Style は何も継承しないことを示します。

この手法は、 Overlay の Style を定義する場合に使用する手法になります。


## 指定できる属性の確認方法

各Viewクラスの `XML attributes` 表に使用できる属性の一覧が記載されています。  
例えば、TextViewの一覧は以下になります。

公式サイト  
　[TextView - Android Developer](https://developer.android.com/reference/android/widget/TextView.html?hl=ja#lattrs)

各Viewは親ViewのXML属性も指定することができます。例えば、 `EditText` は、 `TextView` を継承しており、  
`TextView` は `View` を継承しているため、 `EditText` では、 `TextView` で定義されているXML属性と  
`View` で定義されているXML属性も使用することができます。

全てのViewの属性を確認したい場合は、以下のページを確認してください。

公式サイト  
　[R.attr - Android Developer](https://developer.android.com/reference/android/R.attr.html?hl=ja)

もし、Viewに指定できない属性がStyleに指定されている場合には、その属性は無視されます。

`R.attr`の中には、どのViewにも指定できない属性があります。  
これらは、Activityやアプリケーションにのみ適用できるTheme用の属性になります。  
`window`で始まる属性がほとんどです。  
（例）`windowNoTitle`や`windowBackground`
