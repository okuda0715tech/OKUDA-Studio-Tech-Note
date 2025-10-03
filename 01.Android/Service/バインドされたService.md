<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [バインドされたService](#service)
	- [概要](#概要)
	- [使用方法](#使用方法)
		- [IBinderインターフェースを定義する](#ibinder定義)
			- [Binderクラスを拡張する方法](#binder拡張方法)
			- [Messenger を使用する](#messenger-使用)
			- [AIDL を使用する](#aidl-使用)
	- [複数のクライアント(Caller)がServiceにバインドする場合](#複数callerservice場合)
	- [注意点](#注意点)
	- [onRebindが呼ばれるタイミング](#onrebind呼)

<!-- /TOC -->


# バインドされたService

## 概要

- Serviceにバインドしているコンポーネントが一つも存在しなくなるとServiceは破棄されます。
  - 呼び出し元のコンポーネント(Activityなど)が終了するとバインドされたServiceも終了します。
- 複数のクライアントが同時に一つのServiceにバインドすることが可能です。


## 使用方法

### IBinderインターフェースを定義する

IBinderインターフェースを定義するには、次の 3 つの方法があります。

**Binderクラスを拡張する方法**

サービスが独自のアプリ専用であり、クライアントと同じプロセスで実行される場合にこの方法で実装します。

サービスが単にアプリのバックグラウンド ワーカーである場合は、この方法が適しています。この方法が適していない唯一のケースは、サービスが他のアプリで使用されたり、異なるプロセス間で使用されたりする場合です。

**Messenger を使用する**

クライアントとServiceが異なるプロセス上で動作する場合、かつ、Serviceがシングルスレッドで動作する場合はこの方法で実装します。

一言で言うと、`Handler`を使用した実装方法になります。
`Handler`を使用していることから、Service側をマルチスレッドで動作させることはできません。
メッセージキューを使用したシングルスレッド処理になります。


**AIDL を使用する**

クライアントとServiceが異なるプロセス上で動作する場合、かつ、Serviceがマルチスレッドで動作する必要がある場合はこの方法で実装します。


#### Binderクラスを拡張する方法

<大まかな処理フロー>

1. Activityが`bindService`を呼び出す
2. IBinderを拡張したクラスが生成される
3. Serviceで`onBind`が呼ばれる
4. Activityで`onServiceConnected`が呼ばれ、IBinderを受け取る
5. IBinder経由でServiceのインスタンスを取得する
6. ボタンのクリックイベントなどでServiceのインスタンスからメソッドを呼び出して使用する。


**LocalService.java**

```java
public class LocalService extends Service {
    // Binder given to clients
    private final IBinder binder = new LocalBinder();
    // Random number generator
    private final Random mGenerator = new Random();

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        LocalService getService() {
            // Return this instance of LocalService so clients can call public methods
            return LocalService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    /** method for clients */
    public int getRandomNumber() {
      return mGenerator.nextInt(100);
    }
}
```

**BindingActivity.java**

```java
public class BindingActivity extends Activity {
    LocalService mService;
    boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
        Intent intent = new Intent(this, LocalService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(connection);
        mBound = false;
    }

    /** Called when a button is clicked (the button in the layout file attaches to
      * this method with the android:onClick attribute) */
    public void onButtonClick(View v) {
        if (mBound) {
            // Call a method from the LocalService.
            // However, if this call were something that might hang, then this request should
            // occur in a separate thread to avoid slowing down the activity performance.
            int num = mService.getRandomNumber();
            Toast.makeText(this, "number: " + num, Toast.LENGTH_SHORT).show();
        }
    }

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            LocalBinder binder = (LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
}
```


#### Messenger を使用する

`bindService`の呼び出しはワーカースレッドで行われる。
すなわち、`bindService`呼び出し後、`bindService`メソッド内の処理をワーカースレッドで行い、呼び出し元スレッドはすぐに次に進む。

**MessengerService.java**

```java
public class MessengerService extends Service {
    static final int MSG_SAY_HELLO = 1;

    static class IncomingHandler extends Handler {
        private Context applicationContext;

        IncomingHandler(Context context) {
            applicationContext = context.getApplicationContext();
        }

        @Override
        public void handleMessage(Message msg) {
            // クライアントから受け取ったメッセージ(msg.what)に応じた処理をおこなう。
            switch (msg.what) {
                case MSG_SAY_HELLO:
                    Toast.makeText(applicationContext, "hello!", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    Messenger mMessenger;

    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(getApplicationContext(), "binding", Toast.LENGTH_SHORT).show();
        mMessenger = new Messenger(new IncomingHandler(this));
        return mMessenger.getBinder();
    }
}
```

`Messenger`内に`IBinder`オブジェクトを保持している。
Service側とクライアント側の両方で別々に`Messenger`を生成するが、その`Messenger`は、同一の`IBinder`オブジェクトを参照することで通信が可能になる。

- `onBind`メソッド内で、`new Messenger(Handler handler)`を実行して、`Messenger`オブジェクトを生成する。
- 上記の`Messenger`オブジェクトの`getBinder()`メソッドを呼び出して`IBinder`オブジェクトを取得し、`onBind`メソッドの返値として返す。


**MessengerActivity.java**

```java
public class MessengerActivity extends Activity {
    Messenger mServiceMessenger = null;

    boolean bound;

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder serviceBinder) {
            // 受け取ったIBinderをパラメータに指定して、Messengerを生成
            mServiceMessenger = new Messenger(serviceBinder);
            bound = true;
        }

        // プロセスがクラッシュした場合のみ呼ばれる
        public void onServiceDisconnected(ComponentName className) {
            mServiceMessenger = null;
            bound = false;
        }
    };

    public void sayHello(View v) {
        if (!bound) return;
        Message msg = Message.obtain(null, MessengerService.MSG_SAY_HELLO, 0, 0);
        try {
            mServiceMessenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(this, MessengerService.class), mConnection,
            Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (bound) {
            unbindService(mConnection);
            bound = false;
        }
    }
}
```

- `bindService`でServiceへのバインドを開始する。
- `onServiceConnected`で受け取った`IBinder`をパラメータに渡して、`new Messenger(IBinder ibinder)`して、`Messenger`を生成する。
- `Messenger.send(int msg)`メソッドを使用して、クライアントからServiceへメッセージを送信する。


#### AIDL を使用する

長くなるため、別ファイルにまとめます。


## 複数のクライアント(Caller)がServiceにバインドする場合

- 複数のクライアントが同時に一つのServiceにバインドすることが可能です。
- `onBind()`が呼ばれるのは最初のバインド時のみです。
- 2回目以降のバインドでは、最初のバインド時に生成されたIBinderを渡します。
- `onUnbind`は、全てのクライアントがアンバインドされた時に一度だけ呼ばれます。クライアントごとに呼ばれるわけではありません。


## 注意点

**<Serviceにバインドできるコンポーネント>**

サービスにバインドできるのは、アクティビティ、サービス、コンテンツ プロバイダのみです。ブロードキャスト レシーバからサービスにバインドすることはできません。

Fragmentからのバインドは`getActivity().bindService()`とすればできるのかもしれないが、それなら、FragmentからActivityにイベントを送って、ActivityでbindServiceすると言う方法も考えられるし、どうやるのが正しいのだろう...。


**<onServiceDisconnectedは予期せぬ切断時のみ>**

サービスがクラッシュしたり強制終了されたりした場合など、サービスへの接続が予期せず失われたときに、Android システムが`onServiceDisconnected()`を呼び出します。
これは、クライアントで`unbindService`を呼び出した際には呼び出されません。


**<バインドは非同期で行われます>**

バインドは非同期的に行われます。
`bindService()`は、`IBinder`をクライアントに返すことなく、すぐに戻ります。
すなわち、ワーカースレッドで処理が行われます。


**<クライアントが予期せず終了した場合はアンバインドされる>**

クライアントが予期せず終了した場合は、予期せぬアンバインドが行われる。
そのため、クライアント側でバインド状態を管理する`boolean`フィールドを持っている場合、それを`false`に変更する必要がある。
予期せぬアンバインドが発生した場合は、`ServiceConnection`クラスの`onServiceDisconnected`メソッドが呼ばれるため、そこで`false`にするのが定石である。

**<接続が切断された時に発生する例外をキャッチする>**

接続が切れたときにスローされる`DeadObjectException`例外は、常にトラップする必要があります。リモートメソッドからスローされる例外はこれのみです。


## onRebindが呼ばれるタイミング

`onRebind`は、`startService`と`bindService`を併用している時にだけ、呼ばれる可能性があります。
`onRebind`は、`onUnbind`で`true`を返した場合、次に`bindService`した時に呼ばれます。その場合、`onBind`は呼ばれません。
`onUnbind`で`false`を返すと、何度`bindService`しても`onBind`が呼ばれます。
なお、`onUnbind`は、複数のクライアントがServiceにバインドしている場合は、全てのクライアントがアンバインドされた時に一度だけ呼ばれます。(クライアントを一つアンバインドするごとに呼ばれるわけではありません。)
