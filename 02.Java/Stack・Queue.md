<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [Stack・Queue](#stackqueue)
	- [java.util.Queueインターフェース](#javautilqueueインターフェース)
		- [メソッド一覧](#メソッド一覧)
	- [java.util.Stackインターフェース](#javautilstackインターフェース)
	- [java.util.Deque](#javautildeque)
		- [メソッド一覧](#メソッド一覧)
			- [PushとAddとOfferが混ざって使用された場合、どのような順番で取り出されるのか？](#pushとaddとofferが混ざって使用された場合どのような順番で取り出されるのか)

<!-- /TOC -->

# スタックとキュー

Javaには、 `Queue` 、 `Stack` 、`Deque` というクラスが存在しています。  
`Stack` は非推奨であるため、 `Queue` 、または `Deque` を使用してスタックを実現しましょう。


## java.util.Queueインターフェース

一般的にQueueといえばFIFOですが、一手間加えることで、LIFOのスタックとして使用することもできます。
ただし、Queueインターフェースを使ってLIFOを実現した場合、（代替メソッドはあるが）pushやpopなどのStack系メソッドが使用できないため、以降で紹介するDequeなどを使う方が一般的だと思われる。

**QueueSample.java**

```Java
Queue<String> queue = new LinkedList<String>();
String s = queue.poll();
```

**StackSample.java**

```Java
Queue<String> stack = Collections.asLifoQueue(new LinkedList<String>());
String s = stack.poll();
```

### メソッド一覧

Queueとして使用する場合も、Stackとして使用する場合も使用できるメソッドは同じです。
Stack系のメソッド（pushやpop等）は使用できません。

メソッド名 | get/set | 値の削除 | 要素が空でゲットできない場合
-----------|---------|----------|-----------------------------
add        | set     | -        | -
offer      | set     | -        | -
element    | get     | しない   | 例外をスロー
peek       | get     | しない   | nullを返す
remove     | get     | する     | 例外をスロー
poll       | get     | する     | nullを返す


## java.util.Stackインターフェース

古いインターフェースなので、非推奨になっています。
Stackを実装したい場合は、QueueもしくはDequeを使用します。


## java.util.Deque

デックと読む。
Qeueu・Stackのどちらとしても使用することができるコレクションのインターフェースである。

**DequeSample.java**

```Java
// QueueとしてもStackとしても働きます
Deque<String> deque = new LinkedList<String>();
String s = deque.poll();
```

### メソッド一覧

Queue/Stack列は、コレクションをQueue・Stack・Dequeのどのような使い方をする時に使用するメソッドなのかを示した列です。

Queue/Stack | メソッド名               | get/set | 値の削除 | 要素が空でゲットできない場合
------------|--------------------------|---------|----------|-----------------------------
Qeueu       | add                      | set     | -        | -
Qeueu       | offer                    | set     | -        | -
Qeueu       | element                  | get     | しない   | 例外をスロー
Qeueu       | peek                     | get     | しない   | nullを返す
Qeueu       | remove                   | get     | する     | 例外をスロー
Qeueu       | poll                     | get     | する     | nullを返す
Stack       | push                     | set     | -        | -
Stack       | pop                      | get     | する     | 例外をスロー
Deque       | addFirst                 | set     | -        | -
Deque       | addLast                  | set     | -        | -
Deque       | offerFirst               | set     | -        | -
Deque       | offerLast                | set     | -        | -
Deque       | getFirst                 | get     | しない   | 例外をスロー
Deque       | getLast                  | get     | しない   | 例外をスロー
Deque       | peekFirst                | get     | しない   | nullを返す
Deque       | peekLast                 | get     | しない   | nullを返す
Deque       | removeFirst              | get     | する     | 例外をスロー
Deque       | removeLast               | get     | する     | 例外をスロー
Deque       | pollFirst                | get     | する     | nullを返す
Deque       | pollLast                 | get     | する     | nullを返す
Deque       | removeFirstOccurrence(※) | get     | する     | 例外をスロー
Deque       | removeLastOccurrence(※)  | get     | する     | 例外をスロー

removeFirstOccurrence、removeLastOccurrenceは引数に指定した要素が最初に現れた要素を取り出すメソッドです。

#### PushとAddとOfferが混ざって使用された場合、どのような順番で取り出されるのか？

上記の表における「共通」メソッドを使わず、Queue用メソッド、Stack用メソッドを混合して使用した場合に、どのような順序で取り出されるのか？

1. AddやOfferよりもPushしたものが優先的に取り出される。
2. どのようなメソッドで取り出しても取り出し順番は変わらない。（セットしたメソッドにのみ依存する）
  2-1. Pushしたものはどんなメソッドで取り出してもLIFOの順番で取り出される。
  2-2. AddやOfferしたものはどんなメソッドで取り出してもFIFOの順番で取り出される。


## java.util.PriorityQueueクラス

優先順位付のQueueを提供します。

要素に優先順位を指定してQueueに追加するのではなく、要素の大小比較メソッドを用意してPriorityQueueに渡すことで、Queueが自動的に要素を並べ替えます。

### サンプルコード

要素同士の優先順位を比較するcompareメソッドを実装したクラス

**MyComparator.java**

```Java
import java.util.Comparator;

public class MyComparator implements Comparator {

    public int compare(Object obj1, Object obj2) {

        String str1 = (String)obj1;
        String str2 = (String)obj2;

        if(str1.length() > str1.length()) {
            return 1;
        } else if(str1.length() < str2.length()) {
            return -1;
        } else{
            return 0;
        }

    }

}
```

上記では、文字列の長さを比較していますが、他の比較ルールで比較すれば、並び方を変更することもできます。
（例えば、文字列の先頭文字のアルファベット順で比較するなど。）

**SampleActivity.java**

```Java
public void onCreate(Bandle savedInstanceState) {
		// 第一引数でQueueの初期容量を指定します。初期容量より多くの要素を追加しようとした場合、自動的に容量は拡張されます。
		Queue queue = new PriorityQueue(1, new MyComparator());
		// 逆順にしたい場合
    // Queue queue = new PriorityQueue(1, new MyComparator().reversed());

    String[] obj = {"あいうえお", "良い天気ですね。", "こんにちは", "やあ", "ばいばい"};

    for(int i = 0; i < obj.length; i++) {
        queue.add(obj[i]);
    }

    while(true) {
        String str = (String)queue.poll();

        if(str == null) {
            break;
        }

        System.out.println(str);
    }
}
```

**実行結果**

```
良い天気ですね。
あいうえお
こんにちは
ばいばい
やあ
```

### 参考にしたサイト

基本的な内容の解説＋実装例
　[11. 新たに追加されたコレクション - TECHSCORE](https://www.techscore.com/tech/Java/JavaSE/Utility/11/)

応用的な内容（ちょっとわからなかった。）
　[[Java] Comparable, Comparator のメモ - Qiita](https://qiita.com/yoshi389111/items/4211a5ea7040ec1c2f60#javautilcomparatorreverseorder)

Mapのソート
　[TreeMap - Javaコード入門](http://java-code.jp/230)
