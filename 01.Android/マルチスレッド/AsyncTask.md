<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [AsyncTask](#asynctask)
	- [使い方](#使い方)

<!-- /TOC -->

# AsyncTask

## 使い方

**TestTask.java**

```Java
// 型パラメータは、左から、
//「doInBackgroundのパラメータの型」
//「onProgressUpdateのパラメータの型」
//「doInBackgroundの戻り値の型 = onPostExecuteのパラメータの型」
public class TestTask extends AsyncTask<Integer, Integer, Integer> {

    private Listener listener;

		// Viewへの参照を保持する場合は、メモリリーク防止のため、WeakReferenceで行う。
		private final WeakReference<TextView> mTextViewReference;

    /**
     * コンストラクタ
     */
    public TestTask(TextView textView) {
        super();
				mTextViewReference = new WeakReference<TextView>(textView);  
    }

    // 非同期処理
    @Override
    protected Integer doInBackground(Integer... params) {

        // 10秒数える処理
        do{
            try {
                //　1sec sleep
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            Log.d("debug",""+params[0]);
            params[0]++;
            // 途中経過を返す
            publishProgress(params[0]);

        }while(params[0]<10);

        return params[0] ;
    }

    // 途中経過をメインスレッドに返す
    // パラメータprogressは可変長引数のため、配列ようにインデックスを指定して参照する。パラメータを渡す側は、その型の引数ならいくつでも渡すことができる。
    @Override
    protected void onProgressUpdate(Integer... progress) {
        if (listener != null) {
            listener.onSuccess(progress[0]);
        }
    }

    // Viewの更新処理は以下2パターンの実装方法がある。
    // 1.リスナー経由で結果をメインスレッドに返し、そこで更新処理を行う。
    // 2.コンストラクタで受け取ったViewに対する処理をonPostExecute内で行う。
    @Override
    protected void onPostExecute(Integer result) {
        // リスナーで返して、返し先でViewを更新する場合
        if (listener != null) {
            listener.onSuccess(result);
        }

        // ここでViewに対する処理を直接実施する場合
        if (mTextViewReference != null && result != null) {
            final TextView textView = mTextViewReference.get();
            if (textView != null) {
                textView.setText(String.valueOf(result));
            }
        }
    }

    void setListener(Listener listener) {
        this.listener = listener;
    }

    interface Listener {
        void onSuccess(int count);
    }
}
```


**AsyncTaskTestActivity.java**

```java
public class AsyncTaskTestActivity extends Activity {

    private TextView textView;
    private Button button;
    private Button countButton;
    private TestTask task;
    private Integer count = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //main.xmlに設定したコンポーネントをid指定で取得します。
        textView    = (TextView)findViewById(R.id.textView);
        button      = (Button)findViewById(R.id.button);
        countButton = (Button)findViewById(R.id.countButton);

        // タスクの生成
        task = new TestTask(textView);
				task.setListener(createListener());

        //buttonがクリックされた時の処理を登録します。
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Button)v).setEnabled(false);
                // 非同期処理を開始する
                // パラメータはAsyncTaskのdoInBackgroundのパラメータに渡されます。
                task.execute(1);
            }
        });

        //countButtonがクリックされた時の処理を登録します。
        countButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                ((Button)v).setText("確認用: " + count.toString());
            }
        });
    }

    private TestTask.Listener createListener() {
        return new TestTask.Listener() {
            @Override
            public void onSuccess(int count) {
                textView.setText(String.valueOf(count));
            }
        };
    }
}
```
