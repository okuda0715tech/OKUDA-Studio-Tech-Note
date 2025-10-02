
# AtomicInteger とは

`AtomicInteger` は、Javaの `java.util.concurrent.atomic` パッケージに含まれるクラスで、スレッドセーフな方法で整数を操作するために使用されます。これは、複数のスレッドが同時に整数値 (※ 1 ) を変更する場合に特に便利です。特に、単純な値の読み書きだけではなく、インクリメントや加減算などの複数ステップに渡す操作でもスレッドセーフを保証します。

(※ 1 )  
ここでは、 AtomicInteger の説明なので、 「整数値」 としています。小数を扱いたい場合は、 AtomicDouble などを使用すれば問題ないと思われます。

`AtomicInteger` は、内部的にロックを使用せずに原子操作を提供するため、パフォーマンスの面で優れています。


## 主な機能と使い方

### 初期化

```java
AtomicInteger atomicInt = new AtomicInteger(0);
```

### 値の取得と設定

```java
int value = atomicInt.get();
atomicInt.set(5);
```


### 値のインクリメントとデクリメント

```java
atomicInt.incrementAndGet(); // 1を加算して新しい値を返す
atomicInt.decrementAndGet(); // 1を減算して新しい値を返す
atomicInt.getAndIncrement(); // 現在の値を返し、後で1を加算する
atomicInt.getAndDecrement(); // 現在の値を返し、後で1を減算する
```


### 値の加算と減算

```java
atomicInt.addAndGet(10); // 10を加算して新しい値を返す
atomicInt.getAndAdd(10); // 現在の値を返し、後で10を加算する
```


### 値の更新

```java
atomicInt.updateAndGet(x -> x * 2); // 値を2倍にして新しい値を返す
atomicInt.getAndUpdate(x -> x * 2); // 現在の値を返し、後で値を2倍にする
```


### 値の比較と更新

```java
boolean updated = atomicInt.compareAndSet(5, 10); // 値が5なら10に設定し、成功したかどうかを返す
```

これらの操作はすべて原子操作であるため、複数のスレッドが同時に操作を行ってもデータの整合性が保たれます。これは、特にスレッドセーフなカウンタやインクリメント操作が必要な場合に役立ちます。



