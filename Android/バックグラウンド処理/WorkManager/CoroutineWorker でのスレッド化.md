- [CoroutineWorker でのスレッド化](#coroutineworker-でのスレッド化)
  - [CoroutineWorker を別のプロセスで実行する](#coroutineworker-を別のプロセスで実行する)


# CoroutineWorker でのスレッド化

Kotlin ユーザーは WorkManager を使用することによってコルーチンを最大限に活用できます。まず、gradle ファイルに work-runtime-ktx を追加します。 **Worker を拡張する代わりに、doWork() の suspend バージョンを持つ CoroutineWorker を拡張する** 必要があります。たとえば、簡単な CoroutineWorker を作成してネットワーク オペレーションを実行する場合は、次のようにします。

```kotlin
class CoroutineDownloadWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val data = downloadSynchronously("https://www.google.com")
        saveData(data)
        return Result.success()
    }
}
```

なお、 **CoroutineWorker.doWork() は、** suspend 関数です。Worker とは異なり、このコードは、Configuration で指定された Executor では実行できません。代わりに、 **デフォルトでは Dispatchers.Default** になります。これをカスタマイズするには、独自の CoroutineContext を用意します。上記の例の場合、Dispatchers.IO で次のように処理を行うことをおすすめします。

```kotlin
class CoroutineDownloadWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        withContext(Dispatchers.IO) {
            val data = downloadSynchronously("https://www.google.com")
            saveData(data)
            return Result.success()
        }
    }
}
```

CoroutineWorker は、コルーチンをキャンセルすることにより、自動的に停止します。停止のために特別な操作は必要ありません。


## CoroutineWorker を別のプロセスで実行する

ListenableWorker の実装である RemoteCoroutineWorker を使用して、ワーカーを特定のプロセスにバインドすることもできます。

RemoteCoroutineWorker は、2 つの引数 ARGUMENT_CLASS_NAME と ARGUMENT_PACKAGE_NAME を追加で使用して、特定のプロセスにバインドします。これらの引数は、処理リクエストの作成時に入力データの一部として指定します。

次の例は、特定のプロセスにバインドされた処理リクエストの作成を示しています。

```kotlin
val PACKAGE_NAME = "com.example.background.multiprocess"

val serviceName = RemoteWorkerService::class.java.name
val componentName = ComponentName(PACKAGE_NAME, serviceName)

val data: Data = Data.Builder()
   .putString(ARGUMENT_PACKAGE_NAME, componentName.packageName)
   .putString(ARGUMENT_CLASS_NAME, componentName.className)
   .build()

return OneTimeWorkRequest.Builder(ExampleRemoteCoroutineWorker::class.java)
   .setInputData(data)
   .build()
```

また、RemoteWorkerService ごとに、AndroidManifest.xml ファイルにサービス定義を追加する必要もあります。

```kotlin
<manifest ... >
    <service
            android:name="androidx.work.multiprocess.RemoteWorkerService"
            android:exported="false"
            android:process=":worker1" />

        <service
            android:name=".RemoteWorkerService2"
            android:exported="false"
            android:process=":worker2" />
    ...
</manifest>
```



