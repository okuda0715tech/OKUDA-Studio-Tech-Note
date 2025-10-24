<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [Service](#service)
	- [概要](#概要)
	- [サービスとスレッドのどちらを選択するか](#サービスとスレッドのどちらを選択するか)
	- [サービスの起動方法による違い](#サービスの起動方法による違い)
		- [Start Service](#start-service)
		- [Bind Service](#bind-service)
	- [使用方法](#使用方法)
		- [ライフサイクルコールバック](#ライフサイクルコールバック)
		- [マニフェストにServiceを追加する](#マニフェストにserviceを追加する)
		- [Service関連クラスを拡張したクラスを作成する](#service関連クラスを拡張したクラスを作成する)
		- [Serviceの開始](#serviceの開始)
		- [バインドされたServiceの使用方法](#バインドされたserviceの使用方法)
	- [ServiceからActivityを起動する方法](#serviceからactivityを起動する方法)
	- [OSによるServiceの強制終了](#osによるserviceの強制終了)
<!-- TOC END -->


# Service

## 概要

- Serviceは、ユーザーがアプリを離れていても処理を継続するためのコントローラーにすぎません。
- Serviceをホストするプロセスのメインスレッドで実行される


## サービスとスレッドのどちらを選択するか

ユーザーがアプリを操作している間に限り、メインスレッドの外部で作業を行う必要がある場合は、代わりに新しいスレッドを作成してください。たとえば、アクティビティを実行している間だけ音楽を再生する場合は、`onCreate()`でスレッドを作成し、`onStart()`で実行を開始して、`onStop()`で停止します。


## サービスの起動方法による違い

起動方法には、`start`と`bind`があります。両方の起動方法を併用することも可能です。


### Start Service

Start Serviceを使用するケースは、ServiceをActivityなどから操作しない場合です。
処理が完了し、Serviceを破棄したい場合は、`stopSelf()`か`stopService()`を呼び出します。呼び出さないと、サービスは生存し続けます。


### Bind Service

Bind Serviceを使用するケースは、ServiceをActivityなどから操作する場合、または、他のアプリからServiceを操作する場合です。(※1)

(※1)
他のアプリから操作するためには、プロセス間通信(IPC)を使用します。

コンポーネントが`bindService()`を呼び出してサービスを作成し、`onStartCommand()`が呼び出されない場合、サービスは、コンポーネントがバインドされている間のみ動作します。すべてのクライアントからアンバインドされると、サービスは破棄されます。


## 使用方法

### ライフサイクルコールバック

コールバック     | 実装の必須性 | 説明
-----------------|--------------|-----------------------------------------
onStartCommand() | 任意         | startService()で起動したい場合は実装必須
onBind()  | 必須  | bindService()で起動したくない場合は、nullを返すように実装する。


### マニフェストにServiceを追加する

**注意**

暗黙的IntentでServiceを起動してはいけない。
予期しないコンポーネントからServiceを起動されたり、予期しないServiceを起動してしまうことによるセキュリティー上のリスクを回避するため。
（暗黙的Intentを受け付けないということは、<filter>タグを使用しないということになる。）

<自分のアプリからしか起動できないようにする>

`android:exported`属性を`false`に設定すると、サービスを自身のアプリでしか利用できないようにすることができます。（または、同一IDをもつアプリからしか利用できない見たい。）これにより、他のアプリによるサービスの起動を回避でき、たとえ明示的インテントを使用したとしても起動できなくなります。

<ユーザーによる意図しないServiceの停止を回避する>

ユーザーは、端末でどのサービスが実行されているかを確認できます。認識できないサービスや信頼できないサービスについては、停止することができます。ユーザーによってサービスが誤って停止されることがないようにするには、アプリのマニフェストで`android:description`属性を`<service>`要素に追加する必要があります。この説明に、サービスが何を行うかや、サービスにどのような利点があるかを紹介する簡潔な文を記載してください。


### Service関連クラスを拡張したクラスを作成する

Serviceを拡張したクラスを作成するには、`Service`クラス、または、`IntentService`クラスを拡張します。

<Service>

Serviceを開始したプロセスのメインスレッドで実行されます。

<IntentService>

ワーカースレッドを作成し、そこで処理を実行します。
ワーカースレッドは一つのみ作成されるため、複数のリクエストを同時に処理することはできません。


### Serviceの開始

`startService()`メソッド、または、`startForegroundService()`を使用してServiceを開始します。

基本的には、`startService()`メソッドを使用します。
`startForegroundService()`メソッドを使用するのは、APIレベルが26(Android 8.0)以上で、かつ、アプリがバックグラウンドにある状態でもServiceを実行し続けたい場合です。
`startForegroundService()`メソッドを使用する場合は、サービスが作成されたら、サービスは 5 秒以内に`startForeground()`メソッドを呼び出す必要があります。これにより、バックグラウッドの状態で生成されたServiceがフォアグラウンドになります。

<使用例>

**Caller.java**

```Java
Intent intent = new Intent(this, HelloService.class);
startService(intent);
```


#### Serviceと呼び出し側(クライアント)のデータ連携方法

**クライアント -> Service**

起動時のIntentのみ

**Service -> クライアント**

起動時のIntentにBroadCast用の`PendingIntent`を格納してServiceに渡す。
Serviceは受け取ったPendingIntentを使用してBroadCastすることによって、クライアントにデータを渡す。


#### 複数のクライアントからServiceの開始要求があった場合

- 最初の開始要求時のみ`onCreate`が呼ばれる。
- 2回目以降の開始要求では、すでにインスタンスが生成されていれば、`onStartCommand`から呼ばれる。
- 複数の開始要求が行われていても、一度の`stopSelf` or `stopService`でサービスは破棄される。


#### 複数のクライアントから開始されるServiceの終了方法の注意点

複数のクライアントから開始されるServiceは、一つ目の開始要求を処理した後に`stopSelf`などでServiceを停止すると二つ目の処理の途中で処理を中断してしまう可能性がある。
これを回避するために、`onStartCommand`のパラメータの`startId`を使用して、`stopSelf(int startId)`で停止するようにする。
Serviceは、直近に受け取った要求がどの要求かを判断できるように、`startId`という値を保持しているようだ。これは、`startService`を呼び出した際に、セットされると思われる。
Service側では、`onStartCommand`のパラメータとして受け取ることができる。あるstartIdの処理を完了したら、そのstartIdを使用して`stopSelf(startId)`を呼び出せば、もし、次の要求が別のstartIdで来ていた場合は、stardIdが一致しないため、Serviceは停止されず、次の要求の処理を最後まで実行することができる。

<使用例>

```java
public class HelloService extends Service {
  private Looper serviceLooper;
  private ServiceHandler serviceHandler;

  private final class ServiceHandler extends Handler {
      public ServiceHandler(Looper looper) {
          super(looper);
      }
      @Override
      public void handleMessage(Message msg) {
          // do something.
          // ...

          stopSelf(msg.arg1);
      }
  }

  @Override
  public void onCreate() {
    HandlerThread thread = new HandlerThread("ServiceStartArguments",
            Process.THREAD_PRIORITY_BACKGROUND);
    thread.start();

    serviceLooper = thread.getLooper();
    serviceHandler = new ServiceHandler(serviceLooper);
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
      Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

      Message msg = serviceHandler.obtainMessage();
      msg.arg1 = startId;
      serviceHandler.sendMessage(msg);

      return START_STICKY;
  }
}
```


### バインドされたServiceの使用方法

クライアント(Caller)側は`bindService`メソッドを呼び出します。
すると、Service側は`onBind`メソッドが呼ばれ、`IBinder`オブジェクトを返します。
この`IBinder`を使用して、クライアントはServiceのメソッドを呼び出します。
Serviceの使用が終わったら、クライアントは`unbindService`メソッドを呼び出して、Serviceとの接続を切断します。


## ServiceからActivityを起動する方法

Serviceから起動するActivityのIntentを生成し、それをPendingIntentでラップします。  
PendingIntentの`send()`メソッドを呼び出すとActivityが起動します。


## OSによるServiceの強制終了

**<OSがServiceを停止する契機>**

Androidシステムがサービスを停止するのは、メモリが少なくなって、ユーザーが使用しているアクティビティのシステム リソースを回復する必要が生じた場合のみです。

**<停止されたServiceの再開契機>**

システムによってサービスが強制終了された場合、リソースが回復次第、サービスが再起動されます

**<停止の優先順位>**

ユーザーが使用しているアクティビティにサービスがバインドされている場合、強制終了されることはあまりありません。

フォアグラウンドで実行するように宣言されている場合は、サービスが強制終了されることはほとんどありません。サービスが開始されてから長時間実行されている場合は、バックグラウンド タスクのリストにおけるその位置付けが徐々に低くなり、サービスが強制終了される可能性が高くなります。
