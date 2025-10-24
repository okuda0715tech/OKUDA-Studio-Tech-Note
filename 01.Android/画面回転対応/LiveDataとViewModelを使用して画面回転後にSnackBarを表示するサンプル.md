<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [LiveDataとViewModelを使用して画面回転後にSnackBarを表示するサンプル](#livedataとviewmodelを使用して画面回転後にsnackbarを表示するサンフル)
	- [コード](#コード)

<!-- /TOC -->


# LiveDataとViewModelを使用して画面回転後にSnackBarを表示するサンプル

## コード

**activity_main.xml**

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:app="http://schemas.android.com/apk/res-auto">
    <data>
        <variable
            name="viewModel"
            type="com.kurodai0715.myasynctaskcallback.MyViewModel" />
    </data>

    <androidx.appcompat.widget.LinearLayoutCompat
        app:isShown="@{viewModel.showSnackbar ? true : false}"
        app:result="@{viewModel.result}"
        ...>

        ...

    </androidx.appcompat.widget.LinearLayoutCompat>
</layout>
```


**MainActivity.java**

```java
public class MainActivity extends AppCompatActivity {

    ...

    @BindingAdapter({"isShown", "result"})
    public static void showResult(View view, boolean isShown, String result) {
        if (isShown) {
            Snackbar.make(view, result, Snackbar.LENGTH_SHORT).show();
        }
    }
}
```


**MyViewModel.java**

```java
public class MyViewModel extends AndroidViewModel implements MyTask.Listener {

    private final MutableLiveData<Boolean> mIsShownSnackbar = new MutableLiveData<>();

    public LiveData<Boolean> getIsShownSnackbar() {
        return mIsShownSnackbar;
    }

    void loadData() {
        MyTask task = new MyTask();
        task.setListener(new WeakReference<MyTask.Listener>(this));
        task.execute();
    }

    // このサンプルでは、AsyncTaskの処理が終わって呼ばれるコールバックメソッド
    @Override
    public void onFinish(String result) {
        mResult.setValue(result);
        mIsShownSnackbar.setValue(true);
        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    // 一旦MainThreadでmIsShownSnackbarの値を読み込ませてSnackBarを表示させてから、
                    // mIsShownSnackbarの表示フラグをOFFにする。
                    // フラグをONのままにしていると、画面回転の度にSnackbarが表示されてしまうため、一旦OFFにする。
                    // sleepを入れているのは、フラグをOFFにする処理がSnackbar表示よりも先に実行されないようにするため。
                    // わざわざWorkerThreadでフラグをOFFにするのは、MainThreadでOFFにすると、OFFにした後にしか、
                    // Viewがフラグを読みにこないため、フラグが一度も表示されなくなってしまうため。
                    Thread.sleep(100);
                    mIsShownSnackbar.postValue(false);
                } catch (InterruptedException e) {
                    Log.d("debug","Thread is Interrupted.");
                }
            }
        }).start();
    }
}
```


