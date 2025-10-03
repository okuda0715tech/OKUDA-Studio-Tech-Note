- [IME の開閉とコンポーザブルへのフォーカス](#ime-の開閉とコンポーザブルへのフォーカス)
  - [キーボードの開閉](#キーボードの開閉)
  - [フォーカスを当てる](#フォーカスを当てる)
  - [フォーカスを外す](#フォーカスを外す)
  - [カーソルの位置](#カーソルの位置)
  - [まとめ](#まとめ)
  - [引用元](#引用元)


# IME の開閉とコンポーザブルへのフォーカス

## キーボードの開閉

それらしいクラスやメソッドがあります。フォーカスが当たっていれば使えます。

```kotlin
val keyboardController = LocalSoftwareKeyboardController.current
keyboardController?.show()
keyboardController?.hide()
```

フォーカスを当てたり外したりすることのみでも、 IMEを開閉できるので今回は無視します。


## フォーカスを当てる

FocusRequester を使います。

```kotlin
val focusRequester = remember { FocusRequester() }
```

focusRequester を TextFiled に仕込みます。

```kotlin
TextField(
    modifier = Modifier.focusRequester(focusRequester)
)
```

それを Button クリックでフォーカスします。

```kotlin
Button(
    onClick = { focusRequester.requestFocus() }
) {
    Text("SHOW IME")
}
```

フォーカスと同時にIMEも開きます。

compose 時に当てたいときは、

```kotlin
LaunchedEffect(Unit) {
    focusRequester.requestFocus()
}
```


## フォーカスを外す

同様に、FocusRequester でやれると思ったら、できません。

LocalFocusManager を使います。


```kotlin
val focusManager = LocalFocusManager.current
```

フォーカスを外してくれます。

同様に、ボタンに仕込みます。

```kotlin
Button(
    onClick = { focusManager.clearFocus() }
) {
    Text("HIDE IME")
}
```

これも、フォーカスを外すと同時にIMEが閉じます。


## カーソルの位置

文字の入った TextField にフォーカスしてIMEが開いたときは、編集です。

文字の最後尾にカーソルがあったほうがいい気がします。

TextFieldValue を使います。

```kotlin
TextFieldValue(
    text = text,
    selection = TextRange(text.length)
)
```

selection がカーソルの位置です。

text の長さを数えて置きます。日本語でもいけます。


## まとめ

以下で検証してみました。

```kotlin
@Composable
fun SampleScreen() {

    var text by remember { mutableStateOf("あいうえお") }

    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    Column(
        Modifier.fillMaxSize(),
        Arrangement.Center,
        Alignment.CenterHorizontally
    ) {

        Text(text)
        Spacer(Modifier.height(16.dp))
        Row {
            Button(
                onClick = { focusRequester.requestFocus() }
            ) {
                Text("SHOW IME")
            }
            Spacer(Modifier.width(24.dp))
            Button(
                onClick = { focusManager.clearFocus() }
            ) {
                Text("HIDE IME")
            }
        }
        Spacer(Modifier.height(16.dp))
        CustomTextField(
            text = text,
            focusRequester = focusRequester,
            onChange = { changed ->
                text = changed
            }
        )
    }
}

@Composable
fun CustomTextField(
    text: String,
    focusRequester: FocusRequester,
    onChange: (String) -> Unit
) {

    var textFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = text,
                selection = TextRange(text.length)
            )
        )
    }

    TextField(
        value = textFieldValue,
        onValueChange = { changed ->
            textFieldValue = changed
            onChange(changed.text)
        },
        modifier = Modifier.focusRequester(focusRequester)
    )

}
```

<img src="./画像/IME の開閉とフォーカスの例.webp" width="300">


## 引用元

- [【Jetpack Compose】TextField の フォーカス と IME 開閉 と カーソル位置](https://android.benigumo.com/20220628/jetpackcompose-textfield-focus-ime/)


