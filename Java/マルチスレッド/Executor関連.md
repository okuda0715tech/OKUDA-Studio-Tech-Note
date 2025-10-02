<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [Thread生成周り](#thread生成周)
	- [ThreadFactoryインターフェース](#threadfactory)
	- [Executorsクラス](#executors)
		- [Executors.defaultThreadFactory()](#executorsdefaultthreadfactory)
		- [Executors.newCachedThreadPool()](#executorsnewcachedthreadpool)
	- [Executorインターフェース](#executor)
		- [void execute(Runnable r)](#void-executerunnable-r)
	- [ExecutorServiceインターフェース](#executorservice)
		- [void execute(Runnable r)](#void-executerunnable-r)
		- [void shutdown()](#void-shutdown)
	- [ScheduledExecutorServiceインターフェース](#scheduledexecutorservice)
		- [ScheduledFuture<?> schedule(Runnable r, long delay, TimeUnit unit)](#scheduledfuture-schedulerunnable-r-long-delay-timeunit-unit)

<!-- /TOC -->


# Thread生成周り

オブジェクト                                  | クラス/インターフェース区分 | 概要
----------------------------------------------|-----------------------------|-----------------------------------------------
java.lang.Thread                              |                             |
java.lang.Runnable                            |                             |
java.util.concurrent.ThreadFactory            | インターフェース            | Thread生成の抽象化メソッドを持つ
java.util.concurrent.Executors                | クラス                      | ThreadやThreadFactoryを生成するユーティリティ
java.util.concurrent.Executor                 | インターフェース            | Threadを生成して開始する抽象化メソッドを持つ
java.util.concurrent.ExecutorService          | インターフェース            | Threadの生成や破棄の管理をする抽象化メソッドを持つ。Threadを開始する抽象化メソッドを持つ。自分でこのインターフェースを実装することはまれであり、ThreadPoolExecutorクラスなどの実装済みクラスを生成し、ExecutorService型の変数に代入して使用することが多い。  
java.util.concurrent.ScheduledExecutorService | インターフェース                      | ExecutorServiceを実装したインターフェースであり、Threadの遅延開始を抽象化したメソッドを持つ。

クラスは、ワーカースレッドで実施してほしい内容を「自分で」ワーカースレッドに依頼するイメージ。  
インターフェースは、実施してほしい内容を書いた紙を上司に渡して、「上司を通じて」ワーカースレッドに依頼するイメージ。  
ここでいう「依頼」とは、スレッドを生成たり起動したりすること。


## ThreadFactoryインターフェース

Thread生成する部分の抽象メソッドを持ったインターフェース

```Java
// この抽象メソッドを実装して、Threadの生成部分を実装する必要がある。
Thread newThread(Runnable r);
```


## Executorsクラス

### Executors.defaultThreadFactory()

以下のシンプルなThreadFactoryを生成して返すメソッド

```Java
new ThreadFactory(){
	public Thread newThread(Runnable r){
		return new Thread(r);
	}
}
```


### Executors.newCachedThreadPool()

スレッドプールを生成して返します。  
スレッドプールは、ThreadPoolExecutorをnewで生成しますが、返値の型はExecutorServiceになります。  
一度生成したスレッドは、アイドル状態になってから60秒間生存し続けます。  
タスクの開始要求を出したときにアイドル状態のスレッドがあれば再利用され、なければ新しいスレッドが生成されます。


## Executorインターフェース

### void execute(Runnable r)

パラメータで受け取ったタスクを実行するインターフェースです。  
通常、スレッドを生成して、開始する処理を実装します。


## ExecutorServiceインターフェース

### void execute(Runnable r)

ExecutorServiceインターフェースはExecutorインターフェースを継承しているため、Executorのexecute(Runnable r)メソッドも継承しています。  
execute(Runnable r)メソッドを呼び出すと、パラメータで渡したタスクを実行してくれます。  
スレッドの生成や破棄などの管理と、タスクの開始はExecutorService内部に隠蔽されています。


### void shutdown()

ExecutorServiceは常にアイドル状態のスレッドが待機している可能性があるため、  
ワーカースレッドの処理が完了した場合には、`shutdown()`メソッドで明示的にリソースを解放する必要があります。  
以下のようにbefore/afterパターンで漏れなく終了させましょう。

```java
ExecutorService executorService = Executor.newCachedThreadPool();

Runnable r = new Runnable(){
	public void run(){
		// ワーカースレッドで実行する何らかのタスク
	}
}

try{
	executorService.execute(r);
	executorService.execute(r2);
	executorService.execute(r3);
	...
}finally{
	executorService.shutdown();
}
```


## ScheduledExecutorServiceインターフェース

### ScheduledFuture<?> schedule(Runnable r, long delay, TimeUnit unit)

指定された遅延後に有効になる単発的なアクションを作成して実行します。  
unitには、longで指定した遅延時間の単位を指定します。  
`TimeUnit.SECONDS`,`TimeUnit.MILLISECONDS`,`TimeUnit.MICROSECONDS`,`TimeUnit.NANOSECONDS`のいづれかを指定します。

以下にサンプルを記載します

```java
ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);
scheduledExecutorService.schedule(
	new Runnable(){
		public void run(
			// do something.
		)
	},
	3L,
	TimeUnit.SECONDS
);
```
