<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [LiveDataで一度だけの通知](#livedataて一度たけの通知)
	- [コード](#コード)

<!-- /TOC -->


# LiveDataで一度だけの通知

## コード

**MainActivity.java**

```java
public class MainActivity extends AppCompatActivity {

    MyViewModel viewModel;
    Button button;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        textView = findViewById(R.id.text_view);

        viewModel = new ViewModelProvider(
                getViewModelStore(), getDefaultViewModelProviderFactory()).get(MyViewModel.class);
        viewModel.getResult().observe(this, new EventObserver<>(new EventObserver.OnEventChanged<String>() {
            @Override
            public void onUnhandledContent(String data) {
                Snackbar.make(getRootView(), data, Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onChangedContent(String data) {
                textView.setText(data);
            }
        }));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.loadData();
            }
        });
    }

    private View getRootView() {
        return findViewById(android.R.id.content);
    }
}
```


**Event.java**

```java
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


**EventObserver**

```java
public class EventObserver<T1> implements Observer<Event<T1>> {
    private OnEventChanged<T1> onEventChanged;

    public EventObserver(OnEventChanged<T1> onEventChanged) {
        this.onEventChanged = onEventChanged;
    }

    @Override
    public void onChanged(@Nullable Event<T1> tEvent) {
        if (tEvent != null && onEventChanged != null){
            if(tEvent.isNotHandled()){
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

```java
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


