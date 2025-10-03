<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [EditText](#edittext)
	- [改行不可のEditText](#改行不可のedittext)
	- [改行可のEditText](#改行可のedittext)
	- [行数による最大高さ指定](#行数による最大高さ指定)
	- [行数による最小高さ指定](#行数による最小高さ指定)
	- [dimensionによる"最大/最小"の"高さ/幅"の指定](#dimensionによる最大最小の高さ幅の指定)
	- [文字数による幅の指定](#文字数による幅の指定)
	- [文字数による"最大幅/最小幅"の指定](#文字数による最大幅最小幅の指定)
	- [文字数の上限制限を行う](#文字数の上限制限を行う)
	- [EditTextで初期の高さ（行数）を指定しつつ入力に従って動的に高さを調整する](#edittextで初期の高さ行数を指定しつつ入力に従って動的に高さを調整する)

<!-- /TOC -->


# EditText

## 改行不可のEditText

```Xml
<EditText
  android:inputType="text" />
```


## エンターキーを次のEditTextへの移動にする

上記「改行不可のEditText」と同じ


## 改行可のEditText

```Xml
<EditText
  android:inputType="textMultiLine" />
```


## エンターキーを改行ボタンにする

上記「改行可のEditText」と同じ


## 行数による最大高さ指定

```Xml
<EditText
  android:layout_height="wrap_content"
  android:maxLines="2" />
```

高さが指定した行数までは広がるが、それ以上は広がらず、スクロールして表示させる。
（スクロールビューなどはなしでスクロールできる。）


## 行数による最小高さ指定

```Xml
<EditText
  android:layout_height="wrap_content"
  android:maxLines="2" />
```

高さが指定した行数までは狭まるが、それ以上は狭まらない。


## dimensionによる"最大/最小"の"高さ/幅"の指定

```Xml
<EditText
  android:layout_height="wrap_content"
  android:maxHeight="20dp" />
```

最小の場合は、`minHeight`を指定する。

幅の場合は、`maxWidth/minWidth`を指定する。


## 文字数による幅の指定

**【注意】**  
**この設定は、だいたいそうなるけれど、 1 文字程度の誤差が出る可能性が大いにあります。**

```Xml
<EditText
  android:layout_width="wrap_content"
  android:ems="3" />
```

「半角数字の横幅 × 2 」 の幅を 1 ems と定義します。  
例えば、 3 ems なら、 「 112233 」 のように、半角数字が 6 文字入る横幅になります。

`ems` を設定する場合は、 `layout_width="wrap_content"` を指定する必要があります。  
`layout_width` に、それ以外の指定をした場合は、 `ems` の設定が、その設定でオーバーライドされます。


## 文字数による"最大幅/最小幅"の指定

```Xml
<EditText
  android:layout_width="wrap_content"
  android:maxEms="2" />
```

文字が指定した幅に治りきらない場合は、折り返して表示されます。


## 文字数の上限制限を行う

```Xml
<EditText
  android:maxLength="5" />
```

半角でも全角でも一文字を一文字としてカウントします。


## EditTextで初期の高さ（行数）を指定しつつ入力に従って動的に高さを調整する

EditTextに以下の属性を設定すればできる。

```Java
android:layout_height="wrap_content"
android:inputType="textMultiLine"
android:minLines="5"
```

<属性の説明>

minLines・・・初期の高さ（行数）
