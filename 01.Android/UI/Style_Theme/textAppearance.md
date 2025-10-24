<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [TextAppearance](#textappearance)
	- [概要](#概要)
	- [android:TextAppearance で設定可能な属性](#androidtextappearance-で設定可能な属性)

<!-- /TOC -->


# TextAppearance

## 概要

TextAppearance は通常の xml 属性と異なり、 View を inflate した後でも見た目を変更することができる。


## android:TextAppearance で設定可能な属性

https://developer.android.com/reference/android/R.styleable#TextAppearance

|Attribute|Description|
|-|-|
|android:textSize|Size of text.|
|android:typeface|Default text typeface.|
|android:textStyle|Default text typeface style.|
|android:textColor|Color of text (usually same as colorForeground).|
|android:textColorHighlight|Color of highlighted text.|
|android:textColorHint|Color of hint text (displayed when the field is empty).|
|android:textColorLink|Color of link text (URLs).|
|android:shadowColor|Place a blurred shadow of text underneath the text, drawn with the specified color.|
|android:shadowDx|Horizontal offset of the text shadow.|
|android:shadowDy|Vertical offset of the text shadow.|
|android:shadowRadius|Blur radius of the text shadow.|
|android:textAllCaps|Present the text in ALL CAPS.|
|android:fontFamily|Default font family.|
|android:elegantTextHeight|Elegant text height, especially for less compacted complex script text.|
|android:letterSpacing|Text letter-spacing.|
|android:fontFeatureSettings|Font feature settings.|
|android:fontVariationSettings|The variation settings to be applied to the font.|
|android:fallbackLineSpacing|Whether to respect the ascent and descent of the fallback fonts that are used in displaying the text.|
|android:textFontWeight|Weight for the font used in the TextView.|
|android:textLocale|Specifies the LocaleList for the text in this TextView.|
|android:lineBreakStyle|Specifies the line-break strategies for text wrapping.|
|android:lineBreakWordStyle|Specifies the line-break word strategies for text wrapping.|
|android:searchResultHighlightColor|Color of search results highlight.|
|android:focusedSearchResultHighlightColor|Color of focused search result highlight.|
