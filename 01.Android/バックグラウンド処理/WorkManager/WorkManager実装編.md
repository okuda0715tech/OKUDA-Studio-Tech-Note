<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [WorkManager 実装編](#workmanager-実装編)
  - [build.gradle への追加](#buildgradle-への追加)
  - [主なクラスの役割](#主なクラスの役割)
    - [Work](#work)
    - [WorkRequest](#workrequest)
    - [WorkManager](#workmanager)
  - [基本的な実装例](#基本的な実装例)
  - [1 回のみ実行する場合](#1-回のみ実行する場合)
    - [WorkRequest の作成](#workrequest-の作成)
  - [定期的に実行する場合](#定期的に実行する場合)
    - [WorkRequest の作成](#workrequest-の作成-1)
    - [フレックス期間のある定期的な WorkRequest の作成](#フレックス期間のある定期的な-workrequest-の作成)
  - [WorkRequest を WorkManager のキューに登録する](#workrequest-を-workmanager-のキューに登録する)
  - [優先処理として WorkRequest を作成する](#優先処理として-workrequest-を作成する)
  - [実行条件を追加する](#実行条件を追加する)
  - [初期遅延を設定する](#初期遅延を設定する)
  - [WorkRequest ごとに固有の ID を取得する](#workrequest-ごとに固有の-id-を取得する)
  - [タグを設定 / 取得する](#タグを設定--取得する)
  - [タグを使用した WorkRequest の一括操作](#タグを使用した-workrequest-の一括操作)
    - [特定のタグを持つすべての処理リクエストをキャンセルする](#特定のタグを持つすべての処理リクエストをキャンセルする)
    - [WorkInfo オブジェクトのリストを取得する](#workinfo-オブジェクトのリストを取得する)
  - [WorkRequest に名前を付ける（WorkManager のキュー内部で一意となる名前）](#workrequest-に名前を付けるworkmanager-のキュー内部で一意となる名前)
    - [キュー内部で同じ名前の WorkRequest が競合した場合](#キュー内部で同じ名前の-workrequest-が競合した場合)
  - [WorkInfo を取得する](#workinfo-を取得する)
  - [WorkInfo の更新を観察する](#workinfo-の更新を観察する)
  - [WorkInfo を検索する](#workinfo-を検索する)
  - [Work をキャンセルする](#work-をキャンセルする)
  - [Work がキャンセルされた場合のクリーンアップ処理](#work-がキャンセルされた場合のクリーンアップ処理)
<!-- TOC END -->


# WorkManager 実装編

## build.gradle への追加

```java
dependencies {
    def work_version = "2.7.1"

    // (Java only)
    implementation "androidx.work:work-runtime:$work_version"

    // Kotlin + coroutines
    implementation "androidx.work:work-runtime-ktx:$work_version"

    // optional - RxJava2 support
    implementation "androidx.work:work-rxjava2:$work_version"

    // optional - GCMNetworkManager support
    implementation "androidx.work:work-gcm:$work_version"

    // optional - Test helpers
    androidTestImplementation "androidx.work:work-testing:$work_version"

    // optional - Multiprocess support
    implementation "androidx.work:work-multiprocess:$work_version"
}
```


## 主なクラスの役割

### Work

- バックグラウンドスレッドで実行する処理を記述します。
- 実行した処理が正常に完了したか、失敗したかを WorkManager サービスに通知します。
- 失敗した場合には、再試行ポリシーにしたがって、別の時間に再試行するかどうかを指定します。


### WorkRequest

- Work を実行するスケジュールを定義します。
  - 1 回かぎりなのか、定期的に実行するのかを指定します。
  - 実行間隔がどれくらいかを指定します。


### WorkManager

- 処理内容と処理スケジュールを受け取って、適切なタイミングで実行します。


## 基本的な実装例

```Java

public class UploadWorker extends Worker {
   public UploadWorker(
       @NonNull Context context,
       @NonNull WorkerParameters params) {
       super(context, params);
   }

   // doWork() メソッドは、ワーカースレッドで実行されます。
   @Override
   public Result doWork() {

     // doWork() メソッド内に、ワークで実行したい処理を記述します。
     // ...

     // 処理が成功したか？失敗したか？
     // 失敗した場合は、リトライするか？を指示します。
     return Result.success();
     return Result.failure(); // 失敗、かつ、リトライなし
     return Result.retry(); // 失敗、かつ、リトライあり
   }
}
```

```Java
// WorkRequest を作成する。
WorkRequest uploadWorkRequest =
  new OneTimeWorkRequest.Builder(UploadWorker.class)
    .build();

// WorkRequest を WorkManager に送信する。
WorkManager
  .getInstance(myContext)
  .enqueue(uploadWorkRequest);
```


## 1 回のみ実行する場合


### WorkRequest の作成

```java
// 追加の設定が不要な場合
WorkRequest myWorkRequest = OneTimeWorkRequest.from(MyWork.class);

// 追加の設定が必要な場合
WorkRequest uploadWorkRequest =
  new OneTimeWorkRequest.Builder(MyWork.class)
    // ここに追加のパラメータを指定することができます。
    .build();
```


## 定期的に実行する場合

### WorkRequest の作成

```Java
PeriodicWorkRequest saveRequest =
       new PeriodicWorkRequest.Builder(SaveImageToFileWorker.class, 1, TimeUnit.HOURS)
           // 必要に応じて制約を付加する
           .build();
```

`Builder` のコンストラクタの引数は以下の通りです。

- 第二引数 : 最小実行間隔の数値
- 第三引数が : 最小実行間隔の単位

上記の例では、最小実行間隔は 「 1 時間」 を表しています。

**定義可能な最小実行間隔は 15 分です。**


### フレックス期間のある定期的な WorkRequest の作成

```Java
WorkRequest saveRequest =
       new PeriodicWorkRequest.Builder(SaveImageToFileWorker.class,
               1, TimeUnit.HOURS,
               15, TimeUnit.MINUTES)
           .build();
```

`Builder` のコンストラクタの引数は以下の通りです。

- 第二引数 : 最小実行間隔の数値
- 第三引数 : 最小実行間隔の単位
- 第四引数 : フレックス期間の数値
- 第五引数 : フレックス期間の単位

上記の例では、最小実行間隔は 「 1 時間」 、フレックス期間は 「 15 分」 を表しています。

**定義可能な最小実行間隔は 15 分です。**
**定義可能な最小フレックス期間は 5 分です。**


## WorkRequest を WorkManager のキューに登録する

```Java
WorkRequest myWork = // ... OneTime or PeriodicWork
WorkManager.getInstance(requireContext()).enqueue(myWork);
```


## 優先処理として WorkRequest を作成する

```Java
OneTimeWorkRequest request = new OneTimeWorkRequestBuilder<T>()
    .setInputData(inputData)
    .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
    .build();
```

Builder の `setExpedited()` メソッドを呼び出すことで、処理を優先処理として `WorkManager` の  
キューに登録するように指示します。

`setExpedited()` メソッドのパラメータには、優先処理として登録できなかった場合 (※1) の扱いを指定します。  
`OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST` は、通常処理として登録する場合に使用します。  
`OutOfQuotaPolicy.DROP_WORK_REQUEST` は、処理を登録するのをやめます。

(※1) 「WorkManager 理論編」 の 「割り当て」 を参照。


## 実行条件を追加する

以下の例では、 「 Wi-Fi に接続している」 、かつ、 「充電中」 の場合にのみ実行されます。

```Java
Constraints constraints = new Constraints.Builder()
       .setRequiredNetworkType(NetworkType.UNMETERED)
       .setRequiresCharging(true)
       .build();

WorkRequest myWorkRequest =
       new OneTimeWorkRequest.Builder(MyWork.class)
               .setConstraints(constraints)
               .build();
```


## 初期遅延を設定する

定期的な処理の場合でも、 1 回限りの処理の場合でも初期遅延を行うことができます。  
次の例は、処理がキューに登録されてから 10 分以上経過してからタスクを実行するように設定しています。

```Java
WorkRequest myWorkRequest =
      new OneTimeWorkRequest.Builder(MyWork.class)
               .setInitialDelay(10, TimeUnit.MINUTES)
               .build();
```

初期遅延とフレックス期間の違いは、別紙 「 WorkManager 理論編」 を参照してください。


## WorkRequest ごとに固有の ID を取得する

```Java
WorkRequest myWorkRequest = OneTimeWorkRequest.from(MyWork.class);
UUID uuid = myWorkRequest.getId();
```

UUID とは、 10 進数で 36 桁程度の莫大な数値を用いて、重複しないように生成したランダムな ID である。  
重複する確率は限りなく小さいため、重複しないものとして扱うことができる。


## タグを設定 / 取得する

**WorkRequest にタグを設定する。**

```Java
WorkRequest myWorkRequest =
       new OneTimeWorkRequest.Builder(MyWork.class)
       .addTag("tag_name")
       .build();

// なお、 WorkInfo オブジェクトや Worker オブジェクトからタグを取得する方法はありません。
```

**タグを取得する。**

```Java
// 以下のいずれかの方法で取得します。

// 方法 1
workInfo.getTags();

// 方法 2
worker.getTags();

// なお、 WorkRequest オブジェクトからタグを取得する方法はありません。
```


## タグを使用した WorkRequest の一括操作

### 特定のタグを持つすべての処理リクエストをキャンセルする

```java
workManager.cancelAllWorkByTag(String tag);
```


### WorkInfo オブジェクトのリストを取得する

```Java
workManager.getWorkInfosByTag(String tag);
```


## WorkRequest に名前を付ける（WorkManager のキュー内部で一意となる名前）

```Java
String uniqueName = "sendLogs";

PeriodicWorkRequest sendLogsWorkRequest =
    new PeriodicWorkRequest.Builder(SendLogsWorker.class, 24, TimeUnit.HOURS)
        .setConstraints(
            new Constraints.Builder()
                .setRequiresCharging(true)
                .build())
        .build();

// 【 1 回限りの Work の場合】
// enqueueUniqueWork() メソッドでキューに登録する
WorkManager.getInstance(requireContext()).enqueueUniqueWork(
    uniqueName,
    ExistingWorkPolicy.KEEP,
    sendLogsWorkRequest);

// 【定期的な Work の場合】
// enqueueUniquePeriodicWork() メソッドでキューに登録する
WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
    uniqueName,
    ExistingPeriodicWorkPolicy.KEEP,
    sendLogsWorkRequest);
```


### キュー内部で同じ名前の WorkRequest が競合した場合

キュー内部で同じ名前の WorkRequest が競合した場合は、以下のオプションから選択することができます。

- REPLACE
  - 新しい処理で既存の処理を置き換えます。既存の処理はキャンセルされます。
- KEEP
  - 既存の処理を保持し、新しい処理をキューに追加することなく破棄します。
- APPEND ( 1 回限りの Work でのみ使用可)
  - 新しい処理を既存の処理の最後に追加します。
  - 新しい処理が既存の処理に連結され、既存の処理の完了後に実行されます。
  - 既存の処理が CANCELLED または FAILED になると、新しい処理も CANCELLED または FAILED になります。
- APPEND_OR_REPLACE ( 1 回限りの Work でのみ使用可)
  - 基本的には APPEND と同じ動作をします。
  - ただし、既存の処理が CANCELLED または FAILED になっても、新しい処理を実行します。

1 回限りの Work の場合は、 `ExistingWorkPolicy` の各オプションを指定します。  
定期的な Work の場合は、 `ExistingPeriodicWorkPolicy` の各オプションを指定します。
( `ExistingPeriodicWorkPolicy` は、定期的な Work に指定不可なオプションが削除されています。)


## WorkInfo を取得する

`WorkRequest` の ID で取得する方法、名前で取得する方法、タグで取得する方法の 3 種類の取得方法が存在し、  
それぞれに `ListenableFuture` で取得する方法と `LiveData` で取得する方法がある。

```Java
// WorkRequest の id で取得
ListenableFuture<WorkInfo> workInfo = workManager.getWorkInfoById(myWorkRequest.getId());
LiveData<WorkInfo> workInfo = workManager.getWorkInfoByIdLiveData(myWorkRequest.getId());

// WorkRequest の名前で取得
ListenableFuture<List<WorkInfo>> workInfos = workManager.getWorkInfosForUniqueWork("work_request_name");
LiveData<List<WorkInfo>> workInfos = workManager.getWorkInfosForUniqueWorkLiveData("work_request_name");

// WorkRequest のタグで取得
ListenableFuture<List<WorkInfo>> workInfos = workManager.getWorkInfosByTag("work_request_tag");
LiveData<List<WorkInfo>> workInfos = workManager.getWorkInfosByTagLiveData("work_request_tag");
```

取得した `WorkInfo` には、 `WorkRequest` の ID 、タグ、現在の状態 ( `State` )、  
`Work` の処理結果である `Data` が含まれます。


## WorkInfo の更新を観察する

```java
workManager.getWorkInfoByIdLiveData(syncWorker.id).observe(
    getViewLifecycleOwner(),
    workInfo -> {
        if (workInfo.getState() != null && workInfo.getState() == WorkInfo.State.SUCCEEDED) {
          // Work が成功した場合の処理
        }
    });
```


## WorkInfo を検索する

`WorkManager` のキューに登録された `WorkRequest` の `WorkInfo` を検索することができます。  
検索条件として 「タグ」 、 「状態」 、 「名前」 を指定することができます。

```Java
WorkQuery workQuery = WorkQuery.Builder
    .fromTags(Arrays.asList("syncTag")) // タグで検索
    .addStates(Arrays.asList(WorkInfo.State.FAILED, WorkInfo.State.CANCELLED)) // 状態で検索
    .addUniqueWorkNames(Arrays.asList("preProcess", "sync")) // 名前で検索
    .build();

// ListenableFuture で結果を取得する場合
ListenableFuture<List<WorkInfo>> workInfos = workManager.getWorkInfos(workQuery);
// LiveData で結果を取得する場合
LiveData<List<WorkInfo>> workInfos = workManager.getWorkInfosLiveData(workQuery);
```

各コンポーネント (タグ、状態、名前) は、AND で他のコンポーネントと連結されます。  
コンポーネント内の各値は OR で連結されます。

【例】
( name1 OR name2 OR ... ) AND ( tag1 OR tag2 OR ... ) AND ( state1 OR state2 OR ... )


## Work をキャンセルする

一つの Work をキャンセルする場合は、通常、 ID で Work を指定するよりも、名前で指定する方が  
便利なことが多いでしょう。

```Java
// by id
workManager.cancelWorkById(syncWorker.id);

// by name
workManager.cancelUniqueWork("sync");

// by tag
workManager.cancelAllWorkByTag("syncTag");

// 【注意】
// 以下の cancelAllWork() メソッドはとても危険なので使用しない方がよい。
// ライブラリやモジュール内の Work もキャンセルされ、意図しない動作につながる可能性があるため。
workManager.cancelAllWork();
```


## Work がキャンセルされた場合のクリーンアップ処理

Worker がキャンセルされた場合は、 `worker.onStopped()` が呼び出されます。  
このメソッドをオーバーライドして、ファイルと閉じたり、データベースを切断するなど、  
クリーンアップ処理を行います。

このメソッドは、どのスレッドで実行されるかが保障されていないため、 UI スレッドで実行される可能性もあります。  
そのため、非常に軽量な処理にとどめるようにしてください。


あ
