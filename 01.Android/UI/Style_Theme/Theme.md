<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [Theme](#theme)
	- [Themeとは](#themeとは)
	- [Themeの適用方法](#themeの適用方法)
		- [1. \<style\>タグを参照して適用する方法](#1-styleタグを参照して適用する方法)
		- [サンプルコード](#サンプルコード)
		- [2. Theme 属性を直接参照して適用する方法](#2-theme-属性を直接参照して適用する方法)
	- [端末のAPIレベルごとに適用するThemeを変更する方法](#端末のapiレベルごとに適用するthemeを変更する方法)
		- [APIレベル11未満のTheme](#apiレベル11未満のtheme)
		- [APIレベル11以上のTheme](#apiレベル11以上のtheme)
	- [Overlayとは](#overlayとは)
	- [既定のOverlayが存在するので必要に応じて使用すると便利](#既定のoverlayが存在するので必要に応じて使用すると便利)
	- [テーマはContextから取得できる](#テーマはcontextから取得できる)
		- [Overlayを使用している場合には注意が必要](#overlayを使用している場合には注意が必要)
	- [テーマを動的に切り替える場合は、Viewがインフレートされる前に実施する](#テーマを動的に切り替える場合はviewがインフレートされる前に実施する)
	- [テーマ属性に実際にどんな属性が存在しているかは、テーマを定義しているxmlファイルを見た方が良い](#テーマ属性に実際にどんな属性が存在しているかはテーマを定義しているxmlファイルを見た方が良い)
	- [動的にテーマの色を取得する方法](#動的にテーマの色を取得する方法)
	- [View に対して動的に Theme を適用する方法](#view-に対して動的に-theme-を適用する方法)

<!-- /TOC -->


# Theme

## Themeとは

Theme とは、アプリ単位 or Activity 単位で設定する CSS 的な xml ファイルのことです。  
(ただし、 Overlay を使用する場合は View に指定することもあります)

Android 5.0（API レベル 21）と Android サポート ライブラリ v22.1 以降では、  
レイアウト ファイルのビューに `android:theme` 属性を指定することができます。  
これにより、そのビューと子ビューのテーマが変更されます。

**また、 Theme は、「インターフェース or 実装」でいうならば、インターフェースに該当するものです。**

また、 Theme はマテリアルデザインにおける **「色」** をつかさどる仕組みであり、  
Typograpy や Shape とは参照方法が異なります。  
Typograpy は、 **「文字のフォント」** をつかさどる仕組みであり、  
Shape は、 **「View の形」** をつかさどる仕組みとなります。


## Themeの適用方法

Theme の適用方法には 2 通りの方法が存在します。

1. \<style> タグを参照して適用する方法
2. Theme 属性を直接参照して適用する方法


### 1. \<style>タグを参照して適用する方法

1. Android マニフェストの `<activity>` 要素または `<application>` 要素に `android:theme` 属性を追加します。

ThemeのXMLの記載方法については、Styleと同様です。
継承についても、Styleと同様に行うことができます。


### サンプルコード

アプリ全体にThemeを適用する場合

**application_theme_sample.xml**

```XML
<application android:theme="@style/CustomTheme">
```

Activity単位でThemeを適用する場合

**activity_theme_sample.xml**

```XML
<activity android:theme="@android:style/Theme.Dialog">
```


### 2. Theme 属性を直接参照して適用する方法

以下の例のように `?attr/` を接頭辞につけて参照することで、 Theme 属性が適用できます。

```xml
<View android:background="?attr/colorPrimary" />
```


## 端末のAPIレベルごとに適用するThemeを変更する方法


`style.xml` の格納先ディレクトリを API レベルごとに分けます。  
通常は、 `res/values` ディレクトリ内に格納します。  
例えば、 API レベル 11 (Android 3.0) 以降の端末で別のテーマを参照させたい場合は、以下のようにします。


### APIレベル11未満のTheme

`res/values`ディレクトリ

**style.xml**

```XML
<style name="LightThemeSelector" parent="android:Theme.Light">
    ...
</style>
```


### APIレベル11以上のTheme

`res/values-v11`ディレクトリ

**style.xml**

```XML
<style name="LightThemeSelector" parent="android:Theme.Holo.Light">
    ...
</style>
```

上記の例では、 API レベル 11 以上の端末では、 `Theme.Holo.Light` を継承した Theme が適用され、  
API レベル 11 未満の端末では、 `Theme.Light` を継承した Theme が適用されます。


## Overlayとは

Overlay とは、先祖 ViewGroup に適用されているテーマを活かしつつ、自分の View 及び、  
子孫 View では一部をオーバーライドしたテーマを適用することである。

**Overlay の定義方法**

- Styles.xml にどのテーマも継承していないテーマを定義する。

```xml
<!-- 空の parent 属性を定義することで、どのテーマも継承しないことを明示することもできます。 -->
<style name="ThemeOverlay.Toolbar" parent="">
    ...
</style>
```

- 定義したテーマにオーバーライドしたい属性を定義する。


**Overlay の適用方法**

- 定義したテーマをレイアウト xml 内の特定の View から、 `android:theme="@style/xxx"` で参照する。


**動的にテーマをOverlayする方法**

```Java
// オーバーレイが定義されたスタイルを読み込む
Context themedContext = ContextThemeWrapper(context, R.style.overlay_determined_theme);
// スタイルからインフレーターを生成する
Inflater inflater = LayoutInflater.from(themedContext);
// 生成したインフレーターで View を生成する
View view = inflater.inflate(...);
```


## 既定のOverlayが存在するので必要に応じて使用すると便利

例えば、ダークテーマのOverlayが存在します。

```xml
<View android:theme="@style/ThemeOverlay.MaterialComponents.Dark" />
```


## テーマはContextから取得できる

例えば、以下のように取得できます。

```kotlin
val color = ContextCompat.getColor(
	context,
	R.color.foo
)
```


### Overlayを使用している場合には注意が必要

上記の通り、テーマの値を取得するためには `Context` を使用しますが、  
`Context` が正しいものを参照していないと、意図せず Overlay されたテーマを取得してしまったり、  
逆に Overlay されていないテーマを取得してしまうことになります。

以下のように、一番近い View の Context を取得すれば、間違える可能性は低いでしょう。

```kotlin
val color = ContextCompat.getColor(
	view.context,  // 一番近い View の Context を取得
	R.color.foo
)
```

ただし、それよりも良い方法が Theme 属性を使用することです。  
名前が付いた色ではなく、 Theme 自体を参照することができるためです。

```kotlin
@Color Int
fun Context.getThemeColor(
	@AttrRes themeAttrId: Int
): Int {
	return obtainStyledAttributes(
		intArrayOf(themeAttrId)
	).use {
		it.getColor(0, Color.MAGENTA)
	}
}
```


## テーマを動的に切り替える場合は、Viewがインフレートされる前に実施する

`Context.setTheme()` メソッドや `Theme.applyStyle()` メソッドは、動的にテーマを切り替えることが  
できるメソッドであるが、これらを呼び出すタイミングは、 View がインフレートされる前でなければいけない。  
そうしなければ、テーマは変更されない。


## テーマ属性に実際にどんな属性が存在しているかは、テーマを定義しているxmlファイルを見た方が良い

「そんなに数が多いわけではないので、実際にテーマ属性を定義しているファイルを見た方が良い」と公式のYoutubeチャンネルで発言している。


## 動的にテーマの色を取得する方法


```Java
TypedValue typedValue = new TypedValue();
Theme theme = context.getTheme();
theme.resolveAttribute(R.attr.theme_color, typedValue, true);
@ColorInt int color = typedValue.data;
```


## View に対して動的に Theme を適用する方法

```kotlin
val themedContext = ContextThemeWrapper (
	context,
	R.style. ThemeOverlay_Owl_Blue
)
val inflater = LayoutInflater.from(themedContext)
val view = inflater.inflate(...)
```


