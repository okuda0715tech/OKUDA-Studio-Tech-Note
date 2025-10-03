- [AtomicInteger と volatile Integer の違い](#atomicinteger-と-volatile-integer-の違い)
  - [AtomicInteger](#atomicinteger)
    - [特徴](#特徴)
    - [使用例](#使用例)
  - [volatile Integer](#volatile-integer)
    - [特徴](#特徴-1)
    - [使用例](#使用例-1)
  - [違いのまとめ](#違いのまとめ)
  - [結論](#結論)


# AtomicInteger と volatile Integer の違い

## AtomicInteger

`AtomicInteger` は、Java の `java.util.concurrent.atomic` パッケージに含まれるクラスで、スレッドセーフな方法で整数値 (※ 1 ) を扱うためのものです。

(※ 1 )  
「整数値」 としているのは、ここで扱うのが AtomicInteger であるためです。小数を扱いたい場合は、 AtomicDouble などを使用すれば問題ないと思われます。


### 特徴

- **スレッドセーフな操作**: 複数のスレッドが同時にアクセスする場合でも、安全に整数値の更新操作を行うことができます。
- **原子操作 (atomic operations)**: インクリメント、デクリメント、加算、比較と設定（compare-and-set）などの操作を原子レベルで行います。
- **ロックフリー**: 内部でロックを使用せず、ハードウェアサポートを利用して高効率でスレッドセーフな操作を実現します。


### 使用例

```java
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerExample {
    private static AtomicInteger atomicInt = new AtomicInteger(0);

    public static void main(String[] args) {
        // 値をインクリメント
        int newValue = atomicInt.incrementAndGet();
        System.out.println("New Value: " + newValue);
    }
}
```


## volatile Integer

`volatile` 修飾子を使った `Integer` は、変数の読み書きが常にメインメモリで行われるため、すべてのスレッドが最新の値を見ることができます。


### 特徴

- **可視性保証**: 変数の読み書きが常にメインメモリで行われるため、最新の値が見えるようになります。
- **操作はスレッドセーフではない**: 単純な読み書き操作に関しては可視性を保証しますが、インクリメントや加算のような複数ステップにわたる操作はスレッドセーフではありません。


### 使用例

```java
public class VolatileExample {
    private static volatile int volatileInt = 0;

    public static void main(String[] args) {
        // 【NG】スレッドセーフではない例
        volatileInt++;
        // 【OK】スレッドセーフな例
        volatileInt = 1 + 2;

        System.out.println("New Value: " + volatileInt);
    }
}
```

インクリメント ( `volatileInt++` ) の場合、 「値の読み取り -> 加算 -> 結果の代入」 という複数ステップの処理であるため、 volatile の使用では、スレッドセーフにはなりません。


## 違いのまとめ

1. **操作のスレッドセーフ性**:
   - `AtomicInteger`: インクリメント、デクリメント、加算などの複雑な操作をスレッドセーフに実行できます。
   - `volatile Integer`: 単純な読み書きの可視性は保証しますが、複雑な操作はスレッドセーフではありません。

2. **パフォーマンス**:
   - `AtomicInteger`: ロックフリーで高効率なスレッドセーフ操作を提供しますが、単純な読み書きに比べてわずかにオーバーヘッドがあります。
   - `volatile Integer`: 単純な読み書き操作のみをサポートし、非常に軽量ですが、複雑な操作には適しません。

3. **使用シナリオ**:
   - `AtomicInteger`: スレッド間で整数値を安全に更新する必要がある場合に適しています。特にインクリメントや加算などの操作が多い場合に有用です。
   - `volatile Integer`: 単純なフラグや設定値の可視性を保証する必要がある場合に適しています。複雑な操作が必要ない場合に使用します。


## 結論

`AtomicInteger` と `volatile` それぞれには適切な使用場面があり、特定の要件に応じて選択する必要があります。複雑なスレッドセーフ操作が必要な場合は `AtomicInteger` を使用し、単純な可視性保証だけでよい場合は `volatile` を使用するのが一般的です。
