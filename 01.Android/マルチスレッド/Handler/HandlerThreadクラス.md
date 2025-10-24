<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [HandlerThreadクラス](#handlerthreadクラス)
	- [概要](#概要)
	- [使用方法](#使用方法)

<!-- /TOC -->


# HandlerThreadクラス

## 概要

`HandlerThread`は、Javaの`java.lang.Thread`クラスを継承したクラスで、Threadクラスの機能拡張クラスです。
通常のThreadクラスとの違いは、ワーカースレッドで簡単にHandlerのメッセージキュー機能を使用できる点です。

Handlerやメッセージキューを使ったUIスレッドへのメッセージ送信機能は、Android独自の機能であり、UIスレッドへのキューイングは、自分でLooperなどを生成しなくても簡単にこの機能を使用することができます。
しかし、ワーカースレッドでこのキューイングを使用しようとした場合、通常のThreadでは、Looperの生成などの実装が必要になります。
その手間を排除してキューイング機能を使用できるようにしたのが、`HandlerThread`になります。

## 使用方法

HandlerThreadを使用することで、UIスレッド上でHandlerを使用するのとほとんど同じ要領でワーカースレッド上でHandlerを使用することができます。

以下に、UIスレッド上でのHandlerの使用方法とワーカースレッド上でのHandlerの使用方法を載せています。見比べてみると、ほとんど違いがないことがわかります。

**UIThreadSample.java**

```Java
@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    Handler handler = new Handler();
    handler.post(new Runnable() {
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName());
        }
    });
}
```

**WorkerThreadSample.java**

```Java
@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    // 別スレ生成 -> 開始 ("otherはスレッド名")
    HandlerThread handlerThread = new HandlerThread("other");
    handlerThread.start();

    Handler handler = new Handler(handlerThread.getLooper());
    handler.post(new Runnable() {
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName());
        }
    });
}
```

`handlerThread.start();`の内部でLooperを生成しています。(Looper.prepareを呼んでいます。)
`handlerThread.start();`は、`java.lang.Thread`クラスの`start`メソッドとは使い方が違い、通常の`Thread`クラスでは、`run()`メソッドにアプリで実行したい処理を実装しますが、`HandlerThread`クラスの場合は、`run()`メソッド内ではアプリで実行したい処理は記述せず、実行したい処理はメッセージキュー経由で渡すことになります。
