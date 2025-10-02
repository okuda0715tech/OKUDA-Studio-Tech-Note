- [TextWatcherで入力の変更を処理する](#textwatcherで入力の変更を処理する)
  - [beforeTextChanged](#beforetextchanged)
  - [onTextChanged](#ontextchanged)
  - [afterTextChanged](#aftertextchanged)
  - [コールバックは Enter キーを押す前に２回呼ばれることがある](#コールバックは-enter-キーを押す前に２回呼ばれることがある)


# TextWatcherで入力の変更を処理する

## beforeTextChanged

```kotlin
beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int)
```

s : 以下のいずれかの文字  
キーボードの文字をタップする前の EditText の文字の状態  
or  
ケータイ打ちの文字確定時の確定前の EditText の文字の状態  
or  
Enter キータップ時のタップ前の EditText の文字の状態  
or  
Delete キータップ時のタップ前の EditText の文字の状態  
(削除する文字がケータイ打ちの文字確定前か確定後かは関係ない。  
確定前だろうが後だろうが、とにかく Delete キータップ前の EditText の文字の状態が格納されている)  
start :   
count :   
after :   


## onTextChanged

```kotlin
onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int)
```

s :   
start :   
before :   
count :   


## afterTextChanged

```kotlin
afterTextChanged(s: Editable?)
```

s :   


## コールバックは Enter キーを押す前に２回呼ばれることがある

ケータイ打ち（「あ」を連打すると「あ」、「い」、「う」と文字が変わる入力形式）が有効になっている場合は、  
「あ」をタップした後、約 1 秒以内にもう一度「あ」がタップされると「い」になります。  
約 1 秒の未確定期間内に、再度「あ」がタップされなかった場合は、「あ」が確定します。

**この仕様のために、ケータイ打ちが有効になっている場合は、 TextWatcher のコールバック (※ 1 ) が、  
Enter キーのタップ前に 2 回呼ばれます。 ( Enter キータップ時も含めれば、合計 3 回呼ばれます。)**

 (※ 1 )   
 ここの話で取り上げるコールバックとは、 `beforeTextChanged` と `onTextChanged` と  
 `afterTextChanged` の 3 つのコールバックメソッドのことを示します。

まず、「あ」をタップした瞬間に 1 回目のコールバックが呼ばれます。  
その後、約 1 秒後の「あ」が確定した瞬間に、 2 回目のコールバックが呼ばれます。

ケータイ打ちを無効にしている場合は、「あ」と打った瞬間に「あ」が確定するため、  
`TextWatcher` のコールバックは 1 回ずつしか呼ばれません。  
( Enter キーをタップした瞬間も含めると、 合計 2 回コールバックが呼ばれます。

ただし、ケータイ打ちの有効 / 無効を切り替えても、アプリを再起動するまでは、  
設定が反映されていないように見えました。