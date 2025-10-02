<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [Work の進捗状況管理](#work-の進捗状況管理)
  - [進捗状況の更新](#進捗状況の更新)
  - [進捗状況の観察](#進捗状況の観察)
<!-- TOC END -->


# Work の進捗状況管理

## 進捗状況の更新

Work の進捗状況を管理するには、 `androidx.work.Data` クラスに進捗状況をセットし、  
その `Data` クラスを `Worker` の `setProgressAsync(Data data)` で渡します。

```Java
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class ProgressWorker extends Worker {

  private static final String PROGRESS = "PROGRESS";
  private static final long DELAY = 1000L;

  public ProgressWorker(@NonNull Context context, @NonNull WorkerParameters parameters) {
    super(context, parameters);
    // 進捗状況を初期化します。 ( 0 にします。)
    setProgressAsync(new Data.Builder().putInt(PROGRESS, 0).build());
  }

  // doWork() メソッド内で、処理の状況に応じて進捗状況を更新します。
  @NonNull
  @Override
  public Result doWork() {
    // Worker 内で行いたい処理を実装します。
    // ...

    // 進捗譲許を更新します。
    setProgressAsync(new Data.Builder().putInt(PROGRESS, 100).build());
    return Result.success();
  }
}
```


## 進捗状況の観察

`WorkInfo` の `getProgress()` メソッドで、進捗状況が格納された `Data` を取得します。

```Java
WorkManager.getInstance(getApplicationContext())
    // requestId is the WorkRequest id
    .getWorkInfoByIdLiveData(requestId)
    .observe(lifecycleOwner, new Observer<WorkInfo>() {
      @Override
      public void onChanged(@Nullable WorkInfo workInfo) {
        if (workInfo != null) {
          Data progress = workInfo.getProgress();
          int value = progress.getInt(PROGRESS, 0)
          // Do something with progress
        }
      }
    });
```
