<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [Threadクラス](#threadクラス)
	- [基本的なスレッドの起動と開始方法](#基本的なスレッドの起動と開始方法)
	- [自分のスレッド名を設定する](#自分のスレッド名を設定する)
	- [自分のスレッド名を取得する](#自分のスレッド名を取得する)

<!-- /TOC -->


# Threadクラス

## 基本的なスレッドの起動と開始方法

1. スレッドの生成時にRunnableを渡す方法

```Java
public class Main(){
  Runnable runnable = new Runnable(){
    void run(){
      // do something.
    }
  }

  void method(){
    Thread thread = new Thread(runnable);
    thread.start();    
  }
}
```


2. Threadクラスを継承して、run()メソッドをオーバーライドする方法

```Java
public class MyThread extend Thread {
  @Override
  void run(){
    // do something.
  }
}

public class Main {
  void method(){
    new MyThread().start();    
  }
}
```


## 自分のスレッド名を設定する

```Java
public class MyThread extends thread{
  public MyThread(String name){
    // Threadクラスのコンストラクタに渡す文字列がスレッド名となる。
    super(name);
  }
}
```


## 自分のスレッド名を取得する

以下を実行すると、以下の命令を実行したスレッド名を取得します。

```Java
String threadName = Thread.currentThread().getName();
```




