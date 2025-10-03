- [TextView](#textview)
  - [太字にする](#太字にする)
  - [斜体にする](#斜体にする)
  - [太字かつ斜体にする](#太字かつ斜体にする)
  - [HTMLタグで装飾する](#htmlタグで装飾する)
    - [サポートされているHTMLタグ](#サポートされているhtmlタグ)
    - [特殊文字のエスケープ](#特殊文字のエスケープ)
  - [一行表示ではみ出した分は「...」などで処理する方法](#一行表示ではみ出した分はなどで処理する方法)
  - [クリックできるようにする](#クリックできるようにする)
  - [コードでマージンを設定する方法](#コードでマージンを設定する方法)
  - [単語の境界で改行できるようにする（ Android 13 以上）](#単語の境界で改行できるようにする-android-13-以上)

# TextView

## 太字にする

```xml
    <TextView
        android:id="@+id/text_view"
        android:textStyle="bold" />
```


## 斜体にする

```xml
    <TextView
        android:id="@+id/text_view"
        android:textStyle="italic" />
```


## 太字かつ斜体にする

```xml
    <TextView
        android:id="@+id/text_view"
        android:textStyle="bold|italic" />
```


## HTMLタグで装飾する

```kotlin
        val html = "普通のテキスト<B>太字</B><BIG>大きく</BIG><font color=\"red\">赤</font>"
        textView.text = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_COMPACT)
```

`fromHtml(String source, int flags)` メソッドの第二引数に渡す `flag` は、以下の通りです。

`FROM_HTML_MODE_COMPACT` ：ブロックレベルの要素を「改行文字 1 つ」で区切ります。  
`FROM_HTML_MODE_LEGACY` ：ブロックレベルの要素を「改行文字 2 つ」で区切ります。


### サポートされているHTMLタグ

|意味|タグ|
|-|-|
|太字| \<b> \<em>|
|斜体| \<i> \<cite> \<dfn>|
|テキストの 25% 拡大|\<big>|
|テキストの 20% 縮小|\<small>|
|フォント プロパティの設定|\<font face=”font_family“ color=”hex_color”>. フォント ファミリーの例としては、monospace、serif、sans_serif などがあります。|
|等幅フォント ファミリーの設定|\<tt>|
|取り消し線|\<s> \<strike> \<del>|
|下線|\<u>|
|上付き文字|\<sup>|
|下付き文字|\<sub>|
|箇条書き|\<ul> \<li>|
|改行|\<br>|
|区切り|\<div>|
|CSS スタイル|\<span style=”color|background_color|text-decoration”>|
|段落|\<p dir=”rtl | ltr” style=”…”>|


### 特殊文字のエスケープ

以下のリンクを参照してください。

https://developer.android.com/guide/topics/resources/string-resource?hl=ja#StylingWithHTML


## 一行表示ではみ出した分は「...」などで処理する方法

**Sample.xml**

maxLines属性とellipsize属性を指定します。  
**注意１：三点リーダーを表示させるためには、必ず `maxLines` 属性を指定する必要があります。**  
**注意２：`maxHeight` 属性を指定しても、三点リーダーは表示されません。**

```xml
<TextView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:maxLines="1"
    android:ellipsize="end" />
```

singleLine属性はduplicateなので使用せず、maxLines属性を使用します。

<属性の説明>

ellipsize・・・以下参照

指定値 | デフォルト | 動作
-------|------------|--------------
none   | ○          | 何もしない
start  |            | …おかきくけこ
middle |            | あいう…くけこ
end    |            | あいうえおか…


## クリックできるようにする

```xml
<TextView android:clickable="true"
  android:focusable="true" />
```

TextViewはデフォルトではクリックできないが、`clickable`を`true`にすることでクリック可能となる。

その際、キーボード操作中に対象のTextViewにフォーカスがあてられるように`focusable`も`true`にしておくようにワーニングが出るため、こちらも設定しておくと良い。


## コードでマージンを設定する方法

パディングは`setPadding`メソッドが用意されていますが、マージンはそのようなメソッドがありません。

マージンを設定するには以下のようにします。

```Java
TextView textView = findViewById(R.id.textView);
ViewGroup.LayoutParams lp = textView.getLayoutParams();
ViewGroup.MarginLayoutParams mlp = (MarginLayoutParams)lp;
int margin = (int) getResources().getDimension(R.dimen.interval_3dp);
mlp.setMargins(mlp.leftMargin, margin, mlp.rightMargin, margin);
textView.setLayoutParams(mlp);
```


## 単語の境界で改行できるようにする（ Android 13 以上）

以下の属性を設定することで、単語の境界で改行してくれるようになります。

```xml
<TextView
    android:lineBreakWordStyle="phrase" />
```




