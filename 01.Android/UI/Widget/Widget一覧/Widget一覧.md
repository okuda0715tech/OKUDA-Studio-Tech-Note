<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [Widget一覧](#widget一覧)
  - [状態ボタン](#状態ボタン)
    - [CompoundButton](#compoundbutton)
    - [Switch](#switch)
    - [ToggleButton](#togglebutton)
  - [ドラムロール](#ドラムロール)
    - [NumberPicker](#numberpicker)
    - [DatePicker](#datepicker)
    - [TimePicker](#timepicker)
  - [ドロップダウンリスト](#ドロップダウンリスト)
    - [Spinner](#spinner)
  - [入力補完機能付き EditText](#入力補完機能付き-edittext)
    - [AutoCompleteTextView](#autocompletetextview)
<!-- TOC END -->


# Widget一覧

## 状態ボタン

### CompoundButton

チェックボックスをはじめとした「チェック状態」と「非チェック状態」の二つの状態を持つボタンのベースとなるクラスである。

`CompoundButton` を継承している主なクラスは `CheckBox` , `RadioButton` , `Switch` あたりです。


### Switch

`Switch` は `CompoundButton` を継承したクラスです。  
以下のような ON と OFF を切り替えるためのウィジェットです。

<img src="./image/Switch.PNG" width="200">


### ToggleButton

ToggleButton は、 Swich ができるまで使用されていたボタンで、現在はほとんど使われません。  
見た目は以下のようになっています。

<img src="./image/ToggleButton.PNG" width="150">


## ドラムロール

### NumberPicker

[NumberPicker - Android Developer](https://developer.android.com/reference/android/widget/NumberPicker)

TimePickerの時刻きざみを5分単位に変えたい場合などは、こちらを使用する。  
もしくは、時間や日時に関係なく、任意の数値を選択する場合に使用する。

### DatePicker

[DatePicker - Android Developer](https://developer.android.com/reference/android/widget/DatePicker)

### TimePicker

[TimePicker - Android Developer](https://developer.android.com/reference/android/widget/TimePicker.html)


## ドロップダウンリスト

### Spinner

[Spinner - Android Developer](https://developer.android.com/reference/android/widget/Spinner)

[Spinner使用方法 入門編](https://developer.android.com/guide/topics/ui/controls/spinner?hl=ja)


## 入力補完機能付き EditText

### AutoCompleteTextView

[AutoCompleteTextView - Android Developer](https://developer.android.com/reference/android/widget/AutoCompleteTextView)

