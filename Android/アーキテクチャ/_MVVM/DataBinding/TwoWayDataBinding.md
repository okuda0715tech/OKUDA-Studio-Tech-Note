<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [TwoWayDataBinding](#twowaydatabinding)
	- [TwoWayDataBindingとは](#twowaydatabindingとは)
	- [TextViewのサンプル(複数Viewの連携サンプル)](#textviewのサンプル複数viewの連携サンプル)
	- [Radioボタンのサンプル](#radioボタンのサンプル)
	- [ユーザーが入力した値を変換してからViewModelにセットする方法](#ユーザーが入力した値を変換してからviewmodelにセットする方法)
	- [無限ループについて](#無限ループについて)
	- [既存のViewでTwoWayDataBindingが使用できるxml属性](#既存のviewでtwowaydatabindingが使用できるxml属性)

<!-- /TOC -->


# TwoWayDataBinding

## TwoWayDataBindingとは

- 普通のDataBinding
  - ViewModelの値を変更する -> Viewに自動で反映されてる！
- 双方向DataBinding
  - ViewModelの値を変更する -> Viewに自動で反映されてる！
  - ユーザがViewに入力 -> 自動でViewModelに値がセットされてる！

つまり、TwoWayDataBindingでは、Viewに入力があった時にViewModelのsetterが呼ばれてViewModelに値がセットされます。  
OneWayDatabindingで同じことを実現したい場合は、ViewModelへ値をセットするために、自分でViewのリスナーを呼び出すコードを記述する必要があります。

**two_way_databinding.xml**

```xml
<CheckBox
    android:id="@+id/rememberMeCheckBox"
    android:checked="@={viewmodel.rememberMe}"
/>
```

**one_way_databinging.java**

```xml
<CheckBox
    android:id="@+id/rememberMeCheckBox"
    android:checked="@{viewmodel.rememberMe}"
    android:onCheckedChanged="@{viewmodel.rememberMeChanged}"
/>
```

一方向データバインディングと双方向データバインディングの実装方法の違いは、  
基本的には`@{}`にイコールがついて`@={}`となるだけである。  
ただし、 `Observable` ではなく、 `LiveDataのobserve` を使用してデータモデルからビューへの通知を実装している場合は、  
`LiveData` ではなく、 `MutableLiveData` を使用する必要があります。`LiveData`を使用している場合は、  
(※1)のようなビルド時エラーメッセージが出力されます。  

(※1)

```
The expression 'viewModelXXXX.getValue()' cannot be inverted, so it cannot be used in a two-way binding

Details: There is no inverse for method getValue, you must add an @InverseMethod annotation to the method to indicate which method should be used when using it in two-way binding expressions
```

**注意1**

なお、 `View -> ViewModelへの連動のみ` を行いたい場合でも、 `View -> ViewModel` への  
OneWayDatabindingは実装できないため、双方向の実装を行う必要がある。


## TextViewのサンプル(複数Viewの連携サンプル)

EditTextに入力した場合は、ボタンを活性化し、EditTextを空にした場合は、ボタンを非活性にするサンプルです。  
ポイントは、ViewModel側のEditTextの値を保持する変数に変化があった場合に、View側のボタンの状態を保持する変数に通知を送ってあげることです。  
つまり、以下の`MyViewModel.java`内の`setName`メソッドで、ボタンの`notifyPropertyChanged`を呼ぶことです。


**layout.xml**

```XML
<?xml version="1.0" encoding="utf-8"?>
<layout>

    <data>
    <variable
        name="viewModel"
        type="com.kurodai0715.mytwowaydatabinding.MyViewModel" />
    </data>

    <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical">

        <EditText
            android:layout_width="match_parent"
            android:layout_height="70dp"
            android:hint="input area"
            android:text="@={viewModel.name}"/>

        <Button
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Button"
            android:enabled="@{viewModel.btnEnabled}"/>
    </LinearLayout>
</layout>
```

**MainActivity.java**

```Java
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setViewModel(new MyViewModel());
    }
}
```

**MyViewModel.java**

```java
public class MyViewModel extends BaseObservable {

    private String name;

    @Bindable
    public boolean getBtnEnabled() {
        return !TextUtils.isEmpty(name);
    }

    @Bindable
    public String getName() { return name; }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
        notifyPropertyChanged(BR.btnEnabled);
    }
}
```


## Radioボタンのサンプル

ラジオボタン1を選択した場合は、ボタンを活性化し、ラジオボタン2を選択した場合は、ボタンを非活性にするサンプル。

**layout.xml**

```XML
<?xml version="1.0" encoding="utf-8"?>
<layout>

    <data>
    <variable
        name="viewModel"
        type="com.kurodai0715.mytwowaydatabinding.MyViewModel" />
    </data>

    <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical">

        <RadioGroup
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:checkedButton="@={viewModel.selectedRadioButton}">
            <RadioButton
                android:id="@+id/radio_button_1"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:text="ラジオボタン1" />
            <RadioButton
                android:id="@+id/radio_button_2"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:text="ラジオボタン2"/>

        </RadioGroup>

        <Button
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Button"
            android:enabled="@{viewModel.btnEnabled}"/>
    </LinearLayout>
</layout>
```

**MainActivity.java**

```Java
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setViewModel(new MyViewModel());
    }
}
```

**MyViewModel.java**

```java
public class MyViewModel extends BaseObservable {

  private int selectedRadioButton;

  @Bindable
  public boolean getBtnEnabled() {
      return selectedRadioButton == R.id.radio_button_1;
  }

  @Bindable
  public int getSelectedRadioButton() {
      return selectedRadioButton;
  }

  public void setSelectedRadioButton(int selectedRadioButton) {
      this.selectedRadioButton = selectedRadioButton;
      notifyPropertyChanged(BR.selectedRadioButton);
      notifyPropertyChanged(BR.btnEnabled);
  }
}
```


## ユーザーが入力した値を変換してからViewModelにセットする方法

ユーザーが入力した値をViewModelにセットしたり、逆に、ViewModelの値を画面表示する際に、型変換や文字列のフォーマットが必要な場合がある。
その場合は、変換用メソッドが必要になるのだが、特に、TwoWayDataBindingを利用する場合には、画面入力値をViewModelのフィールドに適した形に変換するメソッドが余分に必要になる。
そのメソッドを`InverseMethod`アノテーションを使って、呼び出せるようにできる。

以下の例では、UI上では`String`型でデータ(誕生日)を管理し、ViewModelのフィールド上では`long`型で管理している。`String`型と`long`型で相互変換が必要となる。

**layout.xml**

```xml
<EditText
    android:id="@+id/birth_date"
    android:text="@={Converter.dateToString(viewmodel.birthDate)}"
/>
```

上記では、`dateToString`メソッドを呼び出して、`long`->`String`変換を行ってからUIに表示するように記述されている。ここまでは、OneWayDatabindingの場合も同じ記述が必要である。

**Converter.java**

```Java
public class Converter {
    @InverseMethod("stringToDate")
    public static String dateToString(EditText view, long oldValue,
            long value) {
        // Converts long to String.
    }

    public static long stringToDate(EditText view, String oldValue,
            String value) {
        // Converts String to long.
    }
}
```

`dateToString`メソッドに`InverseMethod("stringToDate")`アノテーションをつけることで、「`dateToString`メソッドの逆変換をするメソッドは、`stringToDate`メソッドですよ。」という関連付けを行っている。
これにより、UIから入力があった場合に、`stringToDate`メソッドを呼び出して、`String`型のデータを`long`型に変換してからViewModelにセットしてくれるようになる。


## 無限ループについて

TwoWayDataBindingでは、`Viewの更新 -> ViewModelの更新 -> Viewの更新 ...` を繰り返すため、無限ループするのではないかと言う心配がある。

一部では、無限ループする可能性があるし、一部では、無限ループしないようになっている。


**無限ループする場合1**

イコール付きのバインディング式`@={}`を使用せず、TwoWayDataBindingを実装した場合は、無限ループする。
そのため、自分で無限ループを断ち切るコードを記述する必要がある。
イコールを使用しないTwoWayDataBindingの記述方法は、昔の記述方式であるため、（しかも、不便だし）使用しない方が良いと思う。

無限ループを断ち切るコード例は以下の通り。

```xml
<EditText android:text="@{user.name}"
          android:afterTextChanged="@{callback.change}" ../>
```

```java
public void change(Editable s) {
  final String text = s.toString();
  if (!text.equals(name.get())) {
    name.set(text);
  }
}
```


**無限ループする場合2**

`@BindingAdapter`アノテーションを使用して独自のアダプターを定義する場合（独自のView属性を定義する場合）には、無限ループする可能性がある。

`BindingAdapter`アノテーションと`InverseBindingAdapter`アノテーションを同時に使用する場合は、相互に呼び出しあうため、ブレークするコードを記述しなければ、無限ループに陥ってしまう。
ブレークするには、`BindingAdapter`アノテーションがついているメソッドで、新旧値比較を行い、同じであれば値をセットしないようにすれば良い。


**無限ループしない場合**

イコール付きのバインディング式`@={}`を使用してTwoWayDataBindingを実装した場合、かつ、既存のViewの属性定義(※1)を使用した場合は、無限ループしない。

(※1)`android:text="@={variable}"`などの既存の属性のことを指す。これに対をなす新規のViewの属性定義とは、`@BindingAdapter`アノテーションを使用して自作した属性`(例)app:xxx="@={variable}"など`のことを指す。

実際に、ライブラリで用意されている`TextViewBindingAdapter.java`クラスなどを確認すると、以下のように、`@BindingAdapter`アノテーションのついたメソッドの中で値の新旧比較を行い、不一致の場合のみデータをセットするように実装されている。

```java
@BindingAdapter("android:text")
public static void setText(TextView view, CharSequence text) {
		final CharSequence oldText = view.getText();
		if (text == oldText || (text == null && oldText.length() == 0)) {
				return;
		}
		if (text instanceof Spanned) {
				if (text.equals(oldText)) {
						return; // No change in the spans, so don't set anything.
				}
		} else if (!haveContentsChanged(text, oldText)) {
				return; // No content changes, so don't set anything.
		}
		view.setText(text);
}
```

以下は、上記メソッドの呼び出し元のコードである。参考までに記載しておく。

**ActivityMainBindingImpl.java**

```Java
@Override
protected void executeBindings() {
	if ((dirtyFlags & 0x9L) != 0) {
		// api target 1

		androidx.databinding.adapters.TextViewBindingAdapter.setText(this.nameInput, nameGet);
		androidx.databinding.adapters.TextViewBindingAdapter.setText(this.nameView, nameGet);
	}
}
```


## 既存のViewでTwoWayDataBindingが使用できるxml属性

既存のViewでTwoWayDataBinding(@={XXX})が使用できるxmlの属性は以下の通り。

[Two-way attributes - Android Developer](https://developer.android.com/topic/libraries/data-binding/two-way?hl=ja#two-way-attributes)

`Class`列のクラス（もしくはそのParentViewクラス（※1））の`Attribute(s)`列の属性には、`@={XXX}`というバインド式が使用できます。という意味の表だと思われる。

（※1）RadioButtonならRadioGroupクラス
