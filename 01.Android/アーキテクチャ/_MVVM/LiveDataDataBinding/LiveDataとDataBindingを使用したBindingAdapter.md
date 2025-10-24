<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [LiveDataとDataBindingを使用したBindingAdapter](#livedataとdatabindingを使用したbindingadapter)
	- [コード](#コード)

<!-- /TOC -->


# LiveDataとDataBindingを使用したBindingAdapter

## コード

**activity_main.xml**

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:app="http://schemas.android.com/apk/res-auto">

    <data>

        <variable
            name="viewModel"
            type="com.kurodai0715.mybindingadapter.MyViewModel" />

        <variable
            name="listener"
            type="com.kurodai0715.mybindingadapter.MainActivity" />
    </data>

    <androidx.appcompat.widget.LinearLayoutCompat xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:tools="http://schemas.android.com/tools"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context=".MainActivity"
        android:orientation="vertical">

        <TextView
            android:id="@+id/text_view"
            android:layout_width="match_parent"
            android:layout_height="200dp"
            app:updateMessage="@{viewModel.itemList}"/>

        <Button
            android:id="@+id/button"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:onClick="@{listener::onClickButton}"
            android:text="ボタン" />

    </androidx.appcompat.widget.LinearLayoutCompat>
</layout>
```


**MainActivity.java**

```Java
public class MainActivity extends AppCompatActivity {

    MyViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new MyViewModel(getApplication());

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);
        binding.setListener(this);
        setContentView(binding.getRoot());
    }

    public void onClickButton(View view) {
        viewModel.getItemList();
    }

    @BindingAdapter({"updateMessage"})
    public static void updateMessage(View view, MyStringList list) {
        if (list == null) return;
        Snackbar.make(view, list.get(0), Snackbar.LENGTH_SHORT).show();
    }
}
```


**MyViewModel.java**

```Java
public class MyViewModel extends AndroidViewModel implements MyTask.Listener {

    private MutableLiveData<MyStringList> itemList;

    MyViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<MyStringList> getItemList() {
        if(itemList == null){
            itemList = new MutableLiveData<>();
            loadData();
        }
        return itemList;
    }

    private void loadData(){
        MyTask task = new MyTask();
        task.setListener(new WeakReference<MyTask.Listener>(this));
        task.execute();
    }

    @Override
    public void onFinish(MyStringList list) {
        itemList.setValue(list);
    }
}
```


**MyStringList.java**

```Java
public class MyStringList extends ArrayList<String> {
}
```


**MyTask.java**

```Java
public class MyTask extends AsyncTask<Object, Object, MyStringList> {

    private WeakReference<Listener> mListener;

    @Override
    protected MyStringList doInBackground(Object... objects) {
        MyStringList list = new MyStringList();
        list.add("1");
        list.add("2");
        list.add("3");
        return list;
    }

    @Override
    protected void onPostExecute(MyStringList list) {
        super.onPostExecute(list);
        mListener.get().onFinish(list);
    }

    void setListener(WeakReference<Listener> listener) {
        mListener = listener;
    }

    interface Listener {
        void onFinish(MyStringList list);
    }
}
```

