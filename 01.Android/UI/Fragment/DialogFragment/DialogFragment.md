<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [DialogFragment](#dialogfragment)
  - [概要](#概要)
    - [DialogFragment とは](#dialogfragment-とは)
    - [DialogFragment と Fragment の関係](#dialogfragment-と-fragment-の関係)
    - [AlertDialog とは](#alertdialog-とは)
    - [ダイアログの実装パターン](#ダイアログの実装パターン)
    - [AlertDialog と Dialog の違い](#alertdialog-と-dialog-の違い)
    - [Dialog 関連のクラスの継承関係](#dialog-関連のクラスの継承関係)
    - [ProgressBar 関連のクラスの継承関係](#progressbar-関連のクラスの継承関係)
  - [実装方法](#実装方法)
    - [基本形](#基本形)
    - [ハードウェアのバックキーでダイアログを閉じれるかどうかを設定する方法](#ハードウェアのバックキーでダイアログを閉じれるかどうかを設定する方法)
    - [ダイアログにボタンを表示する方法](#ダイアログにボタンを表示する方法)
    - [ダイアログにリストを表示する方法](#ダイアログにリストを表示する方法)
    - [ラジオボタンを表示する方法](#ラジオボタンを表示する方法)
    - [チェックボックスを表示する方法](#チェックボックスを表示する方法)
    - [カスタムレイアウトのダイアログを表示する方法](#カスタムレイアウトのダイアログを表示する方法)
      - [AlertDialogを使用する方法](#alertdialogを使用する方法)
      - [Activityをダイアログとして表示する方法](#activityをダイアログとして表示する方法)
    - [小画面端末の場合に、DialogFragmentを全画面で（Activityであるかのように）表示する方法](#小画面端末の場合にdialogfragmentを全画面でactivityであるかのように表示する方法)
    - [【注意点】コンテンツ領域にセットすることができるものは一つだけ](#注意点コンテンツ領域にセットすることができるものは一つだけ)
  - [dismiss()メソッドとcancel()メソッドの違い](#dismissメソッドとcancelメソッドの違い)
    - [cancel()メソッドについて](#cancelメソッドについて)
      - [概要](#概要-1)
      - [主な利用場面](#主な利用場面)
    - [dismiss()メソッドについて](#dismissメソッドについて)
      - [概要](#概要-2)
      - [主な利用場面](#主な利用場面-1)
  - [画面回転対応](#画面回転対応)
    - [画面回転後にコールバックリスナーが呼ばれない問題](#画面回転後にコールバックリスナーが呼ばれない問題)
<!-- TOC END -->


# DialogFragment

## 概要

### DialogFragment とは

昔は Activity の持っている `showDialog({ダイアログID})` でダイアログを表示するのが一般的でした。しかし、この方法では、 Activity のライフサイクルに連動できず、画面回転などの際にアプリが落ちるなどの事象が発生しました。そのため、現在この方法は廃止されています。

そこで画面側のライフサイクルに沿ってダイアログを管理する為に DialogFragment が誕生しました。 DialogFragment 自体は、画面のライフサイクルに連動して適切にダイアログを表示してくれます。

例えば、画面回転時にダイアログは一度消えるのですが、それを自動的に再表示してくれます。


### DialogFragment と Fragment の関係

DialogFragment は、 Fragment を継承したクラスであるため、 Fragment のライフサイクルに則ってダイアログを表示すること可能です。

DialogFragment は、通常の Fragment として使用することも可能です。簡単にいうと、 `show()` メソッドで DialogFragment を表示すればダイアログになり、通常の Fragment のように FragmentTransaction を使用して表示すれば、通常の Fragment と同じように一枚の画面として表示されます。


### AlertDialog とは

AlertDialog は、昔から使用されているダイアログのクラスであり、ダイアログそのものと考えて問題ありません。

AlertDialog は、ダイアログの基本的な機能を備えています。例えば、以下のような機能があります。

- 標準的なダイアログのレイアウト
  - タイトルや本文などの内部コンポーネントの配置
  - デバイスを横向きにした場合のダイアログの最大幅の指定
  - など...
- ポジティブボタンやネガティブボタンのイベントリスナー
- ダイアログの枠外タップの有効 / 無効の設定
  - 現在この機能は DialogFragment に移っています。


### ダイアログの実装パターン

レイアウトを完全にカスタマイズしたい場合は DialogFragment のみを使用して、ダイアログを実装します。ダイアログの幅指定などがなく、ある程度定型的なダイアログを表示したい場合は、 AlertDialog と DialogFragment を使用してダイアログを実装します。


### AlertDialog と Dialog の違い

AlertDialog は Dialog クラスを継承している。  
Dialog クラスを使ってダイアログを表示することもできるが、 positive ボタンや negative ボタンが  
用意されていなかったり、 setMessage メソッドなどが用意されておらず、少々不便に感じる。  
完全にカスタマイズしたダイアログを作成したい時には Dialog クラスを使用しても良いかもしれないが、  
それも AlertDialog を使って実現できるし、 Dialog クラスを直接使用することはないように思う。

AlertDialog には、以下のユーザーインターフェースが用意されている。

1. タイトルエリア、
2. コンテンツエリア (リスト、ラジオボタン、チェックボックス、メッセージ、 layout.xml から inflate した独自 View)
3. ボタンエリア (positive、negative、neutral)

が用意されている。


### Dialog 関連のクラスの継承関係

- java.lang.Object
	- android.app.Dialog（ここからダイアログを作ることもできるようだがあまりやらなさそう）
		- android.app.AlertDialog（一般的なダイアログはこれでほとんどできるのでよく使う）
			- android.app.DatePickerDialog
			- android.app.TimePickerDialog
			- android.app.ProgressDialog（ API level 26 で廃止。代わりに android.widget.ProgressBar の使用を推奨 ）
		- android.text.method.CharacterPickerDialog（変わったダイアログなのでほとんど使わない）
		- android.app.Presentation（変わったダイアログなのでほとんど使わない）


### ProgressBar 関連のクラスの継承関係

- java.lang.Object
	- android.view.View
		- android.widget.ProgressBar
			- android.widget.AbsSeekBar（SeekBarやRatingBarの親となる抽象クラス）
				- android.widget.RatingBar（Amazonの評価のような星5とかのやつ）
				- android.widget.SeekBar（音量を調整するつまみのようなウィジェット）


## 実装方法

### 基本形

**SampleDialogFragment.java**

```Java
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.widget.Toast;

public class SampleDialogFragment extends AppCompatDialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) throws IllegalStateException {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        // DialogFragment生成時にActivityからパラメータを受け取りたい場合は、このようにして受け取る。
        Bundle bundle = getArguments();
        if(bundle != null){
            String arg = bundle.getString(Constant.KEY_DIR_NAME, "");
						// 受け取ったパラメータで何らかの処理を行う。
        }

        builder = builder.setTitle("タイトル")
                .setMessage("本文")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(),"OKボタンが押されました。",Toast.LENGTH_SHORT).show();
                    }
                })
                // 何もしない場合の第二引数は「null」で良い
                .setNegativeButton("キャンセル",null);

        return builder.create();
    }
}

```

`builder.create();` の部分で `AlertDialog` オブジェクトを生成して返している。


**MainActivity.java**

```Java
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton = findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SampleDialogFragment dialogFragment = new SampleDialogFragment();
                // falseにするとバックキーやダイアログ外タップ時にダイアログが閉じない。
                dialogFragment.setCancelable(false);
                // DialogFragmentにパラメータを渡したい場合はこのように渡す。
                Bundle args = new Bundle();
                args.putInt("key_arg1", value);
                dialogFragment.setArguments(args);
                dialogFragment.show(getSupportFragmentManager(),"sample_dialog");
            }
        });
    }
}
```


### ハードウェアのバックキーでダイアログを閉じれるかどうかを設定する方法

`setCancelable({true/false})` メソッドを使用します。
trueの場合は、バックキーでダイアログを閉じることができます。
falseの場合は、バックキーでダイアログを閉じることができません。


`AlertDialog.Builder` クラスと `DialogFragment` クラスの両方に `setCancelable` メソッドがありますが、 DialogFragment を使用している場合は、 AlertDialog.Builder クラスのメソッドを実装しても、設定が有効になりません。その場合は、必ず DialogFragment クラスのメソッドで設定を行ってください。


### ダイアログにボタンを表示する方法

APIでは、ボタンは3つまで表示することができる。
`positive` , `negative` , `neutral` が使える。

**Sample.java**

```java
new AlertDialog.Builder(AlertDialogSample.this)
    .setMessage("Are you sure you want to exit?")
    .setCancelable(false)
    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
              AlertDialogSample.this.finish();
          }
      })
    .setNegativeButton("No", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
              dialog.cancel();
          }
      })
    .show();
```


### ダイアログにリストを表示する方法

`setItems()` 、 `setAdapter` を使用することでリストを表示することができる。

`setItems()` で表示できるリストアイテムは、単純なアイテムだけである。  
Android リソースで定義した `<string-array>` or `<integer-array>` or `CharSequence[]` のみ表示可能である。

**Sample.java**

```Java
final CharSequence[] items = {"Red", "Green", "Blue"};

new AlertDialog.Builder(MainActivity.this)
        .setTitle("タイトル")
        .setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(
                        MainActivity.this,
                        String.format("%sが選択されました。", items[which]),
                        Toast.LENGTH_LONG
                ).show();
            }
        })
        .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 処理は何もしないがこれでダイアログは閉じる
            }
        })
        .show();
```


### ラジオボタンを表示する方法

**Sample.java**

```Java
new AlertDialog.Builder(MainActivity.this)
        .setTitle("Pick a color")
        .setSingleChoiceItems(items, 1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(
                        MainActivity.this,
                        String.format("%sが選択されました。", items[which]),
                        Toast.LENGTH_SHORT
                ).show();
            }
        })
        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, int which) {
                dialog.cancel();
            }
        })
        .show();
```


### チェックボックスを表示する方法

**Sample.java**

```Java
final CharSequence[] items = {"Red", "Green", "Blue"};
final boolean[] checkedItems = {true, false, true};

new AlertDialog.Builder(MainActivity.this)
        .setTitle("Pick a color")
        .setMultiChoiceItems(items, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            public void onClick(DialogInterface dialog, int which,
                                boolean isChecked) {
                checkedItems[which] = isChecked;
            }
        })
        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String selected = "";
                for (int i = 0; i < items.length; i++) {
                    if (checkedItems[i]) {
                        selected = String.format(" %s %s ", selected, items[i]);
                    } else {
                        Log.v("Sample", String.format("%s - not select", items[i]));
                    }
                }
                if (selected.length() > 0) {
                    Toast.makeText(
                            MainActivity.this,
                            String.format("%sが選択されました。", selected),
                            Toast.LENGTH_SHORT
                    ).show();
                }
                dialog.cancel();
            }
        })
        .show();
```


### カスタムレイアウトのダイアログを表示する方法

カスタムレイアウトのダイアログを表示する方法には、 AlertDialog を使用する方法と、 Activity をダイアログとして表示する方法があります。


#### AlertDialogを使用する方法

`builder.setView(view)`を使用して、カスタムビューをセットします。

```java
public class CreateNewDirectoryDialog extends AppCompatDialogFragment {

    /**
     * ダイアログの生成
     * @param savedInstanceState savedInstanceState
     * @return AlertDialog
     * @throws IllegalStateException アタッチするアクティビティが取得できない時に発生する例外
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) throws IllegalStateException{

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_create_new_directory, null);

        builder = builder.setTitle(R.string.create_new_dialog_title)
                .setView(view)
                .setPositiveButton(R.string.dialog_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do something.
                    }
                })
                // 何もしない場合の第二引数は「null」で良い
                .setNegativeButton(R.string.dialog_negative, null);

        return builder.create();
    }
}
```

`requireActivity()`は、`getActivity()`のnullチェックありバージョンです。Fragmentの親であるActivityを取得します。
`requireActivity()`は、取得結果がnullの場合に`RuntimeException`をthrowします。`RuntimeException`のため、`catch`しなくてもコンパイルエラーにはなりません。

`requireActivity().getLayoutInflater()`の部分ですが、`requireActivity()`を書かなくても`getLayoutInflater()`は呼び出せますが、エラーになるため、必ず、`requireActivity()`を省略せずに書いてください。


#### Activityをダイアログとして表示する方法

アクティビティを作成し、<activity> マニフェスト要素でそのテーマを Theme.Holo.Dialog に設定します。

`<activity android:theme="@android:style/Theme.Holo.Dialog" >`

これだけです。これで、アクティビティは全画面でなく、ダイアログ ウィンドウに表示されるようになります。

また、通常画面サイズ以下の端末ではActivityとして使用し、大画面の端末でのみActivityをダイアログとして表示したい場合には、Manifestに以下のように設定します。

`<activity android:theme="@android:style/Theme.Holo.DialogWhenLarge" >`


### 小画面端末の場合に、DialogFragmentを全画面で（Activityであるかのように）表示する方法

**＜重要＞**
大画面の時にダイアログ形式で表示し、小画面の時に全画面表示したい場合の実装方法は2つある。
1.小画面の場合に、DialogFragmentを全画面で表示する。
2.大画面の場合に、Activityをダイアログのように表示する。
1.と2.は逆転の発想である。

1.の方法は、本章で説明している。
2.の方法は、一つ前の章で説明している。
実装量的には2の方法の方が明らかに少ないので、そちらで実装した方がいいかな？

**SampleDialogFragment.java**

```java
public class CustomDialogFragment extends DialogFragment {
    // 全画面表示（Fragmentとして使用）する時も、ダイアログとして使用する時もonCreateViewは呼ばれます。
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        return inflater.inflate(R.layout.purchase_items, container, false);
    }

    // ダイアログとして使用する場合のみ、onCreateDialogが呼ばれます。
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }
}
```

**SampleInvoker.java**

```Java
boolean mIsLargeLayout;

@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mIsLargeLayout = getResources().getBoolean(R.bool.large_layout);
}

public void showDialog() {
    FragmentManager fragmentManager = getSupportFragmentManager();
    CustomDialogFragment newFragment = new CustomDialogFragment();

    if (mIsLargeLayout) {
        // The device is using a large layout, so show the fragment as a dialog
        newFragment.show(fragmentManager, "dialog");
    } else {
        // The device is smaller, so show the fragment fullscreen
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // For a little polish, specify a transition animation
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        // To make it fullscreen, use the 'content' root view as the container
        // for the fragment, which is always the root view for the activity
        transaction.add(android.R.id.content, newFragment)
                   .addToBackStack(null).commit();
    }
}
```

以下は端末の画面サイズが大小どちらに分類されるか判定する方法です。

**res/values/bools.xml**

```Xml
<!-- Default boolean values -->
<resources>
    <bool name="large_layout">false</bool>
</resources>
```

**res/values-large/bools.xml**

```Xml
<!-- Large screen boolean values -->
<resources>
    <bool name="large_layout">true</bool>
</resources>
```


### 【注意点】コンテンツ領域にセットすることができるものは一つだけ

`setMessage()` 、 `setItems()` 、 `setAdapter()` 、  
`setSingleChoiceItems()` 、 `setMultiChoiceItems()` 、 `setView()`  
の 6 つのメソッドで設定するものは全て同じ領域に表示されるため、重複して使用することはできない。


## dismiss()メソッドとcancel()メソッドの違い

### cancel()メソッドについて

#### 概要

`cancel()`メソッドは、ユーザーがダイアログに対してその場で意思決定をせず、操作をキャンセルしたい場合に使用します。
`cancel()`メソッドを呼んだ時に、`DialogFragment`で、`onCancel()`メソッドを呼び出した後に`onDestroy()`メソッドが呼ばれます。


#### 主な利用場面

ダイアログのボタンでNegativeボタンに「キャンセル」の操作を割り当てる場合に使用します。
`Negative`ボタンの`OnClick`コールバックメソッド内で、`cancel()`を呼び出します。
ダイアログ外の領域をタップした場合、または、ハードキーのバックキーをタップしてダイアログを閉じる場合は、OS側で`cancel()`メソッドを読んでいるため、開発者は`cancel()`メソッドを呼ぶ必要はありません。


### dismiss()メソッドについて

#### 概要

`dismiss()`メソッドは、ダイアログを閉じる為に使用します。
`dismiss()`メソッドを呼んだ時には、`onDismiss()`メソッドのみを呼び出します。`onCancel()`メソッドは呼ばれません。


#### 主な利用場面

ただし、基本的にはあまり使用する機会はありません。？ボタンタップ時などの最後に必ずdismissする必要がある？
なぜなら、AlertDialogは以下のタイミングで既に閉じるようになっている為、わざわざ`dismiss()`を呼ぶ必要がない為です。

- AlertDialogのいづれかのボタンをタップした時
- `setItems()`メソッドで実装したリストダイアログ（ラジオボタンやチェックボックスではない単なるリスト）でアイテムを選択した時


## 画面回転対応

### 画面回転後にコールバックリスナーが呼ばれない問題

例えば、ActivityからDialogFragmentを呼んだ場合、画面回転が発生すると、DialogFragmentからActivityを呼ぶコールバックが正しく呼ばれない。

これは、Activityが破棄される前の古いActivity上のコールバックを呼んでいるからである。

これに対処するためには、Activityが再生成された後に新しい方のActivityを取得し直す。
そして、そのActivity内のコールバックメソッドを呼ぶことで、画面回転後もコールバックリスナーを呼ぶことができるようになる。

なお、Activity内で無名クラスとしてリスナーを生成している場合は、画面回転後にコールバックリスナーが正しく呼ばれるようにすることができないため、Activity自身がコールバックリスナーを実装するように変更する必要がある。

サンプルコードは以下を参照。

[画面回転後もコールバックリスナーが途切れないDialogFragmentを作る](https://qiita.com/KazaKago/items/999ac7f7392de4657f30)

[ActivityとFragmentの両方で使えるDialogFragmentの書き方](https://qiita.com/androhi/items/d46b2c23add735403579)

なお、上記のサンプルコードには、FragmentからDialogFragmentを呼び出す場合の画面回転対応方法も含まれている。
