<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [IntentServiceクラス](#intentservice)
	- [概要](#概要)
	- [使用方法](#使用方法)
		- [IntentServiceクラスの実装](#intentservice実装)
		- [呼び出し側の実装](#呼出側実装)
		- [マニフェストへの登録](#登録)
		- [処理結果をActivityなどに伝える方法](#処理結果activity伝方法)
		- [注意点](#注意点)
		- [処理結果をUIに反映したい場合](#処理結果ui反映場合)

<!-- /TOC -->


# IntentServiceクラス

## 概要

Service起動時にワーカースレッドを作成し、そこで処理を実行します。
ワーカースレッドは一つのみ作成されるため、複数のリクエストを同時に処理することはできません。
Serviceクラスを使用した場合は、自分でワーカースレッドを作成しなければメインスレッドで実行されてしまいますが、その手間を省いてくれるのがIntentServiceです。

IntentServiceは、`startService`にのみ対応しており、`bindService`には対応していません。  
`bindService`を使用することもできますが、その場合、`IntentService`としての「ワーカースレッドを自動生成して、そこで処理を行う」  
という機能がありませんので、自分でその機能を実装しない場合は、メインスレッドでの処理になります。

`onHandleIntent()`メソッドがreturnすると、IntentServiceは自動的に破棄されます。  
そんため、通常のServiceのように`stopSelf()`や`stopService()`を呼び出すことはしません。


## 使用方法

### IntentServiceクラスの実装

**HelloIntentService.java**

```java
public class HelloIntentService extends IntentService {

  /**
   * コンストラクタは必須です。
   * superメソッドのパラメータには、ワーカースレッドの名前を渡します。
   */
  public HelloIntentService() {
      super("HelloIntentService");
  }

  /**
   * サービス起動時に渡されたIntentを引数に摂ります。
   * このメソッド内に記述された処理は、IntentServiceが生成したワーカースレッド内で実行されます。
   * このメソッド内の処理が完了すると、Serviceが適切に破棄されます。
   */
  @Override
  protected void onHandleIntent(Intent intent) {
      // Normally we would do some work here, like download a file.
      // For our sample, we just sleep for 5 seconds.
      try {
          Thread.sleep(5000);
      } catch (InterruptedException e) {
          // Restore interrupt status.
          Thread.currentThread().interrupt();
      }
  }
}
```

- nullを返す`onBind()`のデフォルト実装を提供します。
- インテントを`onHandleIntent()`に送信する`onStartCommand()`のデフォルト実装を提供します。
- マルチスレッドの懸念を排除するため、一度に 1 つのインテントを`onHandleIntent()`の実装に渡すワークキューを作成します。
	- 複数のリクエストがあった場合でも、ワークキューから一つずつリクエストを取り出してシリアルに処理します。
	- 一つの処理が終わったら、次の処理を開始します。
- 必須なのは、`コンストラクタ`と`onHandleIntent()`の実装だけです。
- 実行中の処理が中断されることはありません。


### 呼び出し側の実装

**SampleActivity.java**

```java
@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    Intent intent = new Intent(this, HelloIntentService.class);
    intent.putExtra("IntentServiceCommand","TestText");
    this.startService(intent);
}
```

### マニフェストへの登録

通常のServiceクラス同様にマニフェストファイルへの登録が必要である。  
使用するタグ名称は`<Service>`タグで良い。`<IntentService>`というタグは存在しない。


### 処理結果をActivityなどに伝える方法

LocalBroadcastManagerを使用する。ただし、AndroidJetPackからは非推奨になったようである。  
代わりに、`LiveData`か`reactive streams`が使用できないか検討してみて欲しいと公式ドキュメントに記載がある。

<LocalBroadcastManagerの使用方法>

[Report work status](https://developer.android.com/training/run-background-service/report-status?hl=ja)


### 注意点

`onCreate()`、`onStartCommand()`、`onDestroy()`などの他のコールバック メソッドもオーバーライドする場合は、  
IntentService がワーカースレッドの生存状態を正しく処理できるように、  
**必ず`super`メソッドを呼び出します。Serviceクラスを使用する場合は、`super`メソッドを呼び出す必要がないため、混同しないように注意しましょう。**

ただし、`onBind()`のみ`super`メソッドを呼び出す必要はありません。  
これを実装する必要があるのは、サービスでバインドを許可する場合のみです。  
`IntentService`では、そもそも`bindService`をサポートしていません。

`super`クラスのメソッドが返り値を持っている場合、例えば、`onStartCommand()`など、はオーバーライドしたメソッドの`return`で`super`メソッドを呼び出すようにしてください。これにより、返り値
が呼び出し側に返ります。（返し忘れがNGである）

<superの処理結果をreturnで返す例>

```java
@Override
public int onStartCommand(Intent intent, int flags, int startId) {
    Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
    return super.onStartCommand(intent,flags,startId);
}
```


### 処理結果をUIに反映したい場合

`onHandleIntent`メソッド内で、UIを操作することはできません。
UIを操作したい場合は、処理結果をActivityに返す必要があります。
