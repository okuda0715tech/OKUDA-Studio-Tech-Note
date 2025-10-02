<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [Work の処理結果を受け取る](#work-の処理結果を受け取る)
  - [概要](#概要)
  - [サンプル](#サンプル)
<!-- TOC END -->


# Work の処理結果を受け取る

## 概要

Work からアウトプットデータを取り出す場合は、 `androidx.work.Data` クラスに、 「キー & バリュー」  
形式でデータを格納しておき、そこから取り出します。  

格納できるデータ容量の上限は、約 10 KB です。

渡せるデータの種類は、プリミティブ型だけだと思われます。  
それ以外のデータを渡したい場合には、バイト型に変換して渡すことが可能です。


## サンプル

```java
public class MathWorker extends Worker {

    public static final String KEY_RESULT = "result";

    public MathWorker(
        @NonNull Context context,
        @NonNull WorkerParameters params) {
        super(context, params);
    }

    @Override
    public Result doWork() {
        // 何らかの処理を行う
        int result = myLongCalculation(x, y, z);

        // 結果を Data に格納する
        Data output = new Data.Builder()
            .putInt(KEY_RESULT, result)
            .build();

        // Data を後から取り出せる場所にセットする
        return Result.success(output); // Result は、 ListenableWorker の内部クラスの Result である。
    }
}
```

```java
WorkManager.getInstance(myContext).getWorkInfoByIdLiveData(mathWorkRequest.getId())
    .observe(lifecycleOwner, workInfo -> {
         if (workInfo != null && workInfo.getState().isFinished()) {
           // WorkInfo から Data を取り出し、 Data から Work の処理結果を取り出す。
           int myResult = workInfo.getOutputData().getInt(KEY_RESULT, myDefaultValue);
           // Work の処理結果を利用して何らかの処理を行う。
         }
    });

// workInfo.getState() の結果は、 doWork() で、 Result.success() が返されたなら
// WorkInfo.State.SUCCEEDED となり、 Result.failure() が返されたなら
// WorkInfo.State.FAILED となる。
```
