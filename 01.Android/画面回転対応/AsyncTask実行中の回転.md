<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [AsyncTask実行中の回転](#asynctask実行中の回転)
	- [LiveDataを使用する対処方法](#livedataを使用する対処方法)
		- [コード](#コード)

<!-- /TOC -->


# AsyncTask実行中の回転

AsyncTaskからActivity/Fragmentへリスナーコールバックで結果を返す場合、AsyncTask実行中に画面回転すると、Activity/Fragmentが再生成されて、再生成後のActivity/Fragmentへコールバックを返すことができなくなる。

ここでは、その問題への対処方法を記載する。


## LiveDataを使用する対処方法

AsyncTaskの処理結果をLiveDataフィールドへ格納してやれば、ViewModelの機能でデータオブジェクトを保持しつつ、LiveDataの機能で再生成後のActivity/Fragmentへ最新のデータを通知することができる。

### コード

以下のサンプルコードは、ボタンをタップすると３秒間待機して、その後、0〜99までのランダムな数値を取得して、画面に表示するものです。

３秒間の待機と乱数の取得処理をAsyncTaskで実行しています。


**build.gradle**

`implementation 'androidx.lifecycle:lifecycle-extensions:2.2.0'`


**MainActivity.java**

```java
public class MainActivity extends AppCompatActivity {

    MyViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        binding.setLifecycleOwner(this);

        viewModel = new ViewModelProvider(
                getViewModelStore(), getDefaultViewModelProviderFactory()).get(MyViewModel.class);

        binding.setViewModel(viewModel);
        binding.setListener(this);
        setContentView(binding.getRoot());
    }

    public void onClickButton(View view) {
        viewModel.loadData();
    }
}
```


**activity_main.xml**

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout>

    <data>

        <variable
            name="listener"
            type="com.kurodai0715.myasynctaskcallback.MainActivity" />

        <variable
            name="viewModel"
            type="com.kurodai0715.myasynctaskcallback.MyViewModel" />
    </data>

    <androidx.appcompat.widget.LinearLayoutCompat xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:tools="http://schemas.android.com/tools"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical"
        tools:context=".MainActivity">

        <Button
            android:id="@+id/button"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:onClick="@{listener::onClickButton}"
            android:text="ボタン" />

        <TextView
            android:id="@+id/text_view"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@{viewModel.result}" />


    </androidx.appcompat.widget.LinearLayoutCompat>
</layout>
```


**MyViewModel.java**

```java
public class MyViewModel extends AndroidViewModel implements MyTask.Listener {

    private final MutableLiveData<String> result = new MutableLiveData<>();

    public MyViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<String> getResult(){
        return result;
    }

    void loadData(){
        MyTask task = new MyTask();
        task.setListener(new WeakReference<MyTask.Listener>(this));
        task.execute();
    }

    @Override
    public void onFinish(String result) {
        this.result.setValue(result);
    }
}
```


**MyTask.java**

```java
public class MyTask extends AsyncTask<Object, Object, Integer> {

    private WeakReference<Listener> mListener;
    private Integer randomVal;

    @Override
    protected Integer doInBackground(Object... objects) {
        try {
            Thread.sleep(3000);
            Random random = new Random();
            randomVal = random.nextInt(100);
        } catch (InterruptedException e) {
            Log.d("debug","Thread is Interrupted.");
        }
        return randomVal;
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        mListener.get().onFinish(String.valueOf(result));
    }


    void setListener(WeakReference<Listener> listener) {
        mListener = listener;
    }

    public interface Listener {
        void onFinish(String result);
    }
}
```