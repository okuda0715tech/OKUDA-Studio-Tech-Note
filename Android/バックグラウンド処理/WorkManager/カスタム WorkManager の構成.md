- [WorkManager のカスタム構成と初期化](#workmanager-のカスタム構成と初期化)
  - [オンデマンド初期化](#オンデマンド初期化)
    - [デフォルトのイニシャライザを削除する](#デフォルトのイニシャライザを削除する)
    - [Configuration.Provider を実装する](#configurationprovider-を実装する)
  - [WorkManager 2.1.0 より前のカスタム初期化](#workmanager-210-より前のカスタム初期化)
    - [デフォルトの初期化](#デフォルトの初期化)
    - [カスタム初期化](#カスタム初期化)


# WorkManager のカスタム構成と初期化

**デフォルトでは、WorkManager はアプリの起動時に自動的に構成され、** ほとんどのアプリに適した合理的なオプションが設定されます。WorkManager が作業を管理およびスケジュールする方法を詳細に制御する必要がある場合は、WorkManager を自分で初期化して、WorkManager 構成をカスタマイズできます。


## オンデマンド初期化

**オンデマンド初期化では、アプリが起動するたびではなく、そのコンポーネントが必要な場合にのみ WorkManager を作成できます。** これにより、クリティカルなスタートアップパスから WorkManager が除外され、アプリのスタートアップのパフォーマンスが向上します。オンデマンド初期化を使用するには、以下の手順を実施します。


### デフォルトのイニシャライザを削除する

カスタム構成を設定するには、まずデフォルトのイニシャライザを削除する必要があります。そのためには、マージルール `tools:node="remove"` を使用して、 AndroidManifest.xml を更新します。

WorkManager 2.6 以降、 WorkManager の内部で [Initializer](https://developer.android.com/reference/kotlin/androidx/startup/Initializer?_gl=1*19o90zk*_up*MQ..*_ga*MzI2OTg4NjcuMTcyMjMzMDE4OQ..*_ga_6HH9YJMN9M*MTcyMjMzMDE4OS4xLjAuMTcyMjMzMDU0Ni4wLjAuMA..) が使用されています。カスタムのイニシャライザを指定するには、androidx.startup ノードを削除する必要があります。

アプリで Initializer を使用しない場合は、完全に削除できます。

```xml
<!-- android.startup を完全に無効化したい場合 -->
<provider
    android:name="androidx.startup.InitializationProvider"
    android:authorities="${applicationId}.androidx-startup"
    tools:node="remove">
</provider>
```

それ以外の場合は、 WorkManagerInitializer ノードのみを削除します。

```xml
<provider
    android:name="androidx.startup.InitializationProvider"
    android:authorities="${applicationId}.androidx-startup"
    android:exported="false"
    tools:node="merge">
    <!-- WorkManager のイニシャライザだけを無効化したい場合 -->
    <meta-data
        android:name="androidx.work.WorkManagerInitializer"
        android:value="androidx.startup"
        tools:node="remove" />
</provider>
```

2.6 より前のバージョンの WorkManager を使用する場合は、代わりに workmanager-init を削除します。 (古い WorkManager では、コンポーネントごとに初期化用のコンテンツプロバイダが提供されています。)

```xml
<provider
    android:name="androidx.work.impl.WorkManagerInitializer"
    android:authorities="${applicationId}.workmanager-init"
    tools:node="remove" />
```


### Configuration.Provider を実装する

Application クラスで Configuration.Provider インターフェースを実装し、Configuration.Provider.getWorkManagerConfiguration の独自の実装を提供します。実装の例を次に示します。

```kotlin
class MyApplication() : Application(), Configuration.Provider {
    override fun getWorkManagerConfiguration() =
        // Configuration.Builder() は、 WorkManager 用の構成ビルダーです。
        Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()
}
```

WorkManager を使用する場合は、 **必ず WorkManager.getInstance(Context) メソッドを呼び出してください。** WorkManager はアプリのカスタム getWorkManagerConfiguration() メソッドを呼び出して、その Configuration を検出します (自分で WorkManager.initialize を呼び出す必要はありません) 。

**注**: WorkManager が初期化される前に、パラメータがない WorkManager.getInstance() メソッド（非推奨）を呼び出すと、メソッドは例外をスローします。WorkManager をカスタマイズしない場合でも、必ず WorkManager.getInstance(Context) メソッドを使用してください。

**注**: 繰り返しになりますが、カスタム getWorkManagerConfiguration() の実装を有効にするには、デフォルトのイニシャライザを削除する必要があります。


## WorkManager 2.1.0 より前のカスタム初期化

バージョン 2.1.0 より前の WorkManager には、2 つの初期化方法があります。ほとんどの場合、デフォルトの初期化で十分です。WorkManager をより正確に制御する場合は、独自の構成を指定できます。


### デフォルトの初期化

WorkManager は、アプリの起動時に、カスタム ContentProvider により自動的に初期化されます。このコードは内部クラス androidx.work.impl.WorkManagerInitializer に存在し、デフォルトの Configuration を使用します。デフォルトのイニシャライザは、明示的に無効にする場合を除き、自動的に使用されます。デフォルトのイニシャライザは、ほとんどのアプリに適しています。


### カスタム初期化

初期化プロセスを制御したい場合は、カスタム構成を定義する前に、デフォルトのイニシャライザを無効にする必要があります。

デフォルトのイニシャライザを削除したら、WorkManager を手動で初期化できます。

```kotlin
// provide custom configuration
val myConfig = Configuration.Builder()
    .setMinimumLoggingLevel(android.util.Log.INFO)
    .build()

// initialize WorkManager
WorkManager.initialize(this, myConfig)
```

WorkManager のシングルトンの初期化 ( `WorkManager.initialize` ) が Application.onCreate() か ContentProvider.onCreate() のいずれかで実行されるようにします。

利用可能なカスタマイズの完全なリストについては、 [Configuration.Builder() リファレンスドキュメント](https://developer.android.com/reference/androidx/work/Configuration.Builder?_gl=1*89uyts*_up*MQ..*_ga*MzI2OTg4NjcuMTcyMjMzMDE4OQ..*_ga_6HH9YJMN9M*MTcyMjMzMDE4OS4xLjAuMTcyMjMzMDE4OS4wLjAuMA..) をご覧ください。



