<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [Enum](#enum)
  - [基本形](#基本形)
  - [値を割り当てる方法](#値を割り当てる方法)
  - [割り当てた値を取得する方法（ Enum 内部にメソッドを定義する）](#割り当てた値を取得する方法-enum-内部にメソッドを定義する)
  - [Enum の String 型を取得する](#enum-の-string-型を取得する)
  - [要素を配列で取得する](#要素を配列で取得する)
  - [要素の数を取得する](#要素の数を取得する)
  - [for ループで Enum の要素を一つずつ処理する](#for-ループで-enum-の要素を一つずつ処理する)
  - [ordinary メソッドは使わないようにしましょう](#ordinary-メソッドは使わないようにしましょう)
  - [String型のEnum名をインプットにEnum型のEnum値を取得する](#string型のenum名をインプットにenum型のenum値を取得する)
  - [ID から Enum を取得する（要素名そのものを取得する）](#id-から-enum-を取得する要素名そのものを取得する)
  - [Enum で定義されたメソッドを 特定の Enum の場合のみ Override する](#enum-で定義されたメソッドを-特定の-enum-の場合のみ-override-する)
  - [getNext() メソッドのサンプル](#getnext-メソッドのサンプル)
  - [`==` で enum オブジェクトの比較は可能である。](#-で-enum-オブジェクトの比較は可能である)
<!-- TOC END -->


# Enum

## 基本形

```Java
public enum Fruit {
    Orange,
    Apple,
    Melon
};
```


## 値を割り当てる方法

```Java
public enum Fruit {
    // コンストラクタの呼び出し
    Orange(1, "愛媛"),
    Apple(2, "青森"),
    Melon(3, "茨城");

    // フィールドの定義
    private int id;
    private String variety;

    // コンストラクタの定義
    private Fruit(int id, String variety) {
        this.id = id;
        this.variety = variety;
    }
}
```


## 割り当てた値を取得する方法（ Enum 内部にメソッドを定義する）

```Java
public enum Fruit {
    // コンストラクタの呼び出し
    Orange(1, "愛媛"),
    Apple(2, "青森"),
    Melon(3, "茨城");

    // フィールドの定義
    private int id;
    private String variety;

    // コンストラクタの定義
    private Fruit(int id, String variety) {
        this.id = id;
        this.variety = variety;
    }

    // Enum内には以下のようにメソッドを定義できる。
    public String getValue(){
        return this.variety;
    }
}
```

```Java
// 「愛媛」が返ってくる。
String orangeValue = Fruit.Orange.getValue();
```


## Enum の String 型を取得する

```Java
// String型で「Orange」(Enum名そのもの)が返ってくる。
String orangeValue = Fruit.Orange.name();
```


## 要素を配列で取得する

values() メソッドは、要素の配列を返します。

```java
public enum Fruit {
    Orange,
    Apple,
    Melon;

    private Fruit[] getElements() {
        return values();
    }
};
```


## 要素の数を取得する

```java
public enum Fruit {
    Orange,
    Apple,
    Melon;

    private int getLength() {
        // 配列の length フィールドを参照しているだけ。
        return values().length; // 3
    }
};
```


## for ループで Enum の要素を一つずつ処理する

```Java
public enum Fruit {
    Orange,
    Apple,
    Melon
};
```

```Java
for(Fruit fruit : Fruit.values()) {
    // Fruit.Orange, Fruit.Apple, Fruit.Melonが順番に取得されて、処理される。

    // do something.
}
```


## ordinary メソッドは使わないようにしましょう

ordinary メソッドは、先頭の Enum 値から順番に 0 から連番を割り当てますが、  
順番が変わった際や、要素が追加された場合に値が変わってしまうため、使用しないようにしましょう。

Enum 値に定数を与えたい場合は、自分で固定値を与えて使用しましょう。


## String型のEnum名をインプットにEnum型のEnum値を取得する

```Java
Fruit orange = Fruit.valueOf("Orange");
```


## ID から Enum を取得する（要素名そのものを取得する）

```Java
public enum Fruit {
    // コンストラクタの呼び出し
    Orange(1, "愛媛"),
    Apple(2, "青森"),
    Melon(3, "茨城"),
    Unknown(100, "不正なIDでgetIdメソッドが呼ばれた");

    // フィールドの定義
    private int id;
    private String variety;

    // コンストラクタの定義
    private Fruit(int id, String variety) {
        this.id = id;
        this.variety = variety;
    }

    // Enum内には以下のようにメソッドを定義できる。
    public String getValue(){
        return this.variety;
    }

    // IDからEnum値を取得する。
    public static Fruit getBy(int id) {
        for(Fruit e : values()) {
            if(e.id == id) return e;
        }
        // 処理を継続する場合
        return Unknown;
        // 異常終了させる場合
        throw new IllegalArgumentException("Illegal argument 'id' was passed to the method 'Fruit.getBy(int id)'.");
        // 個人的には異常値はテストで発見しやすくするため異常終了させるのがおすすめ。
    }		
}
```

```Java
Fruit melon = Fruit.getBy(3); // Fruit.melon
Fruit unknown1 = Fruit.getBy(4); // Fruit.Unknown
Fruit unknown2 = Fruit.getBy(100); // Fruit.Unknown
```


## Enum で定義されたメソッドを 特定の Enum の場合のみ Override する

以下のサンプルでは、Cherryの場合のみ、`toString`メソッドを呼び出した時に、小文字で`cherry`と表示されます。

```Java
public enum Fruits {
  Apple("リンゴ"),
  Banana("バナナ"),
  Cherry("チェリー") {
    @Override
    public String toString() {
      return name().toLowerCase(); // cherry
    }
  };

  @Override
  public String toString() {
    return this.name().toUpperCase(); // Apple, Banana
  }
}
```


## getNext() メソッドのサンプル

```Java
public enum UseStatus { // enum の前に static を付与する必要がある可能性あり。

    // 【注意】
    // ordinal() メソッドを使用しているため、要素の定義順を入れ替える場合は、
    // ordinal() メソッドを使用しないように変更する必要がある。
    VACANCY,
    IN_USE,
    CLEANING_UP;

    private static final UseStatus[] VALUES = values();

    public UseStatus getNextState() {
        return VALUES[(ordinal() + 1) % VALUES.length];
    }
}
```


## `==` で enum オブジェクトの比較は可能である。

見出しの通りであるため、この件についての詳細はなし。
