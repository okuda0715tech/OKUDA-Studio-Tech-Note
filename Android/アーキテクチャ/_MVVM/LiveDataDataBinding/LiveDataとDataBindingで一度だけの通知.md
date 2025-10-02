<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [LiveDataとDataBindingで一度だけの通知](#livedataとdatabindingて一度たけの通知)
	- [コード](#コード)

<!-- /TOC -->


# LiveDataとDataBindingで一度だけの通知

DataBindingを使用していない時と使用している時のコードに特段大きな違いはない。変更内容は、普段、DataBinding未導入のプロジェクトにDataBindingを導入する方法と変わりはない。ただし、MutableLiveDataの実型引数が次のように入れ子状態になっている`MutableLiveData<Event<String>>`ため、レイアウトxmlで該当変数を参照するときに`android:text="@{viewModel.result.content}"`と入れ子になる点が少し注意するべき点だと思う。


## コード

**activity_main.xml**

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout>

    <data>

        <variable
            name="listener"
            type="com.kurodai0715.myeventoncedatabinding.MainActivity" />

        <variable
            name="viewModel"
            type="com.kurodai0715.myeventoncedatabinding.MyViewModel" />
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
            android:text="Button" />

        <TextView
            android:id="@+id/text_view"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@{viewModel.result.content}" />

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
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        binding.setLifecycleOwner(this);
        binding.setListener(this);
        viewModel = new ViewModelProvider(
                getViewModelStore(), getDefaultViewModelProviderFactory()).get(MyViewModel.class);
        binding.setViewModel(viewModel);
        setContentView(binding.getRoot());

        viewModel.getResult().observe(this, new EventObserver<>(new EventObserver.OnEventChanged<String>() {
            @Override
            public void onUnhandledContent(String data) {
                Snackbar.make(getRootView(), data, Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onChangedContent(String data) {
                // DataBindingを使用している場合は、DataBindingで直接xmlにイベントを送るため、
                // 基本的にこのメソッドで必要な処理は何もない可能性が高い。
            }
        }));
    }

    private View getRootView() {
        return findViewById(android.R.id.content);
    }

    public void onClickButton(View view) {
        viewModel.loadData();
    }
}
```


**Event.java**

```Java
public class Event<T> {
    private boolean hasBeenHandled = false;
    private T content;

    public Event(T content) {
        this.content = content;
    }

    public T getContentIfNotHandled() {
        if (hasBeenHandled) {
            return null;
        } else {
            hasBeenHandled = true;
            return content;
        }
    }

    public boolean isHandled() {
        return hasBeenHandled;
    }

    public boolean isNotHandled() {
        return !hasBeenHandled;
    }

    /**
     * 既にコンテンツの変更を一度通知済みでもコンテンツを返す。
     */
    public T getContent() {
        return content;
    }
}
```


**EventObserver.java**

```Java
public class EventObserver<T1> implements Observer<Event<T1>> {
    private OnEventChanged<T1> onEventChanged;

    public EventObserver(OnEventChanged<T1> onEventChanged) {
        this.onEventChanged = onEventChanged;
    }

    /**
     * データが変更された時（setValueメソッドが呼ばれた時）と
     * 画面回転などのActivityの再生成時に毎回呼ばれるコールバックメソッドです。
     * ＜注意点＞
     * ・初回Activity生成時には呼ばれません。
     * ・observeメソッドで観察を開始しただけでは呼ばれません。
     * ・ただし、ViewModel.getXXXメソッド内で「setValue」を読んでいる場合は当然呼ばれます！
     *
     * 初回は、「onUnhandledContent」も「onChangedContent」も両方コールするので注意！
     * 片方だけにしたい場合はelse文などを検討すること。
     */
    @Override
    public void onChanged(@Nullable Event<T1> tEvent) {
        if (tEvent != null && onEventChanged != null){
            if(tEvent.isNotHandled()){
                // Eventクラス内のデータを一度も取得していない場合
                // つまり、「ViewModel.setValueメソッド」で新しいEventオブジェクトをセットしてから
                // そのデータを一度も取得していない場合に呼ばれる。
                // 言い換えれば、ここの処理は、setValueした直後に必ず一度だけ実行される処理である。
                onEventChanged.onUnhandledContent(tEvent.getContentIfNotHandled());
            }
            onEventChanged.onChangedContent(tEvent.getContent());
        }
    }

    interface OnEventChanged<T2> {
        // データが未処理の場合のみ呼び出されるメソッド
        void onUnhandledContent(T2 data);

        // データ変更時に常に呼び出されるメソッド
        void onChangedContent(T2 data);
    }
}
```


**MyViewModel.java**

```Java
public class MyViewModel extends AndroidViewModel implements MyTask.Listener {

    private final MutableLiveData<Event<String>> mResult = new MutableLiveData<>();

    public MyViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Event<String>> getResult() {
        return mResult;
    }

    void loadData() {
        MyTask task = new MyTask();
        task.setListener(new WeakReference<MyTask.Listener>(this));
        task.execute();
    }

    @Override
    public void onFinish(String result) {
        mResult.setValue(new Event<>(result));
    }
}
```


**MyTask.java**

```Java
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
            Log.d("debug", "Thread is Interrupted.");
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
