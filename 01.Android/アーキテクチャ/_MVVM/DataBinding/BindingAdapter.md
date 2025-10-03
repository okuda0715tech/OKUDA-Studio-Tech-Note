<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [BindingAdapter](#bindingadapter)
	- [概要](#概要)
	- [コード](#コード)
		- [基本形](#基本形)
		- [パラメータが複数ある場合](#パラメータが複数ある場合)

<!-- /TOC -->


# BindingAdapter

## 概要

BindingAdapterは、レイアウトxmlファイル内で`android:layout_width`などの既存の属性ではできない処理を独自の属性`app:xxx`を定義してやってしまおうというものです。


## コード

### 基本形

**MainActivity.java**

```Java
public class MemoListActivity extends AppCompatActivity {

    public final ObservableInt viewParam = new ObservableField<>(initial_value);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MemoListActBinding binding =     MemoListActBinding.inflate(getLayoutInflater());

        // LiveDataを使用しない場合
        binding.setViewParam(viewParam);

        // LiveDataを使用する場合(1と2の順番が入れ替わるとLiveData.setValueで値を更新した時に@BindingAdapterで定義したメソッドが呼ばれないので注意！)
        MyViewModel viewModel = new MyViewModel(getApplication()); // 1
        binding.setViewModel(viewModel); // 2

        setContentView(binding.getRoot());
    }

    // @BindingAdapter(xxx)」の「xxx」の部分で属性名を定義します。
    // ここで定義した属性名を使用して、xmlでは「app:xxx="@{実パラメータ}"」のようにパラメータをセットします。
    // メソッド名は任意です。
    // staticメソッドにする必要があります。
    // パラメータは、第一引数がView、第二引数以降がxmlで渡したものです。
    @BindingAdapter({"customTranslationY"}) // <- パラメータが一つの場合は、中カッコを省略可能
    public static void methodNameIsFree(View view, int distance) {
        // ここにやりたい処理を記述します。
        float scale = view.getContext().getResources().getDisplayMetrics().density;
        view.animate().translationY(distance * scale);
    }
}
```


**上記コードの処理の流れ**

1. ObservableIntの値が変更される
2. xmlがObservableIntの変更を検出し、変更後の値を取得する
3. 取得した値をパラメータにセットし、@BindingAdapterをキーにして、対応するメソッドを呼び出す。


**activity_main.xml**

```xml
<layout xmlns:android="http://schemas.android.com/apk/res/android">

    <data>
        <import type="androidx.databinding.ObservableInt" />
        <variable
            name="viewParam"
            type="ObservableInt" />
    </data>

    <!-- 「app:xxx」という属性で定義します。 -->
    <com.google.android.material.floatingactionbutton.FloatingActionButton
        android:id="@+id/add_dir_fab"
        style="@style/CustomFabMini"
        android:src="@drawable/ic_create_new_folder_18dp"
        app:customTranslationY="@{viewParam * @integer/translation_y_55dp}" />
</layout>
```


### パラメータが複数ある場合

**MainActivity.java**

```Java
public class MainActivity extends AppCompatActivity {
  // @BindingAdapterの属性の順番とメソッド引数のパラメータの順番を合わせる必要があります。ただし、メソッド引数の一番目はViewにする必要がありますので、実際には一つ順番がずれます。
  @BindingAdapter({"isShown", "result"})
  public static void showResult(View view, boolean show, String result) {
      if (show) {
          Snackbar.make(view, result, Snackbar.LENGTH_SHORT).show();
      }
  }
}
```


**activity_main.xml**

```xml
<layout xmlns:app="http://schemas.android.com/apk/res-auto">
    <data>
        <variable
            name="viewModel"
            type="com.kurodai0715.myasynctaskcallback.MyViewModel" />
    </data>

    <!-- 「app:xxx」という属性をメソッドで必要なパラメータの数だけ定義します。属性を定義する順序は任意でOKです。 -->
    <androidx.appcompat.widget.LinearLayoutCompat ...>
        <TextView
            android:id="@+id/snackbar_like"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            app:isShown="@{viewModel.showSnackbar ? true : false}"
            app:result="@{viewModel.result}" />
    </androidx.appcompat.widget.LinearLayoutCompat>
</layout>
```







