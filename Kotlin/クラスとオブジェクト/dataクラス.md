- [data クラス](#data-クラス)
  - [概要](#概要)
  - [基本形](#基本形)
  - [自動生成されるメンバ](#自動生成されるメンバ)
    - [equals() 関数](#equals-関数)
    - [hashCode() 関数](#hashcode-関数)
    - [toString() 関数](#tostring-関数)
    - [componentN() 関数](#componentn-関数)
    - [copy() 関数](#copy-関数)
  - [自動生成の注意点](#自動生成の注意点)
    - [意味のあるコードが生成される条件](#意味のあるコードが生成される条件)
    - [独自実装が優先されるケース](#独自実装が優先されるケース)
    - [独自実装ができないケース](#独自実装ができないケース)
    - [componentN()](#componentn)
    - [equals() 関数のシグネチャ](#equals-関数のシグネチャ)
  - [自動生成される関数から一部のプロパティを除外する](#自動生成される関数から一部のプロパティを除外する)


# data クラス

## 概要

Kotlin の `data` クラスは、データを保持することを主な目的とするクラスです。  
(主な目的は上記の通りですが、関数が定義できないわけではありません。)

`data` クラスは、以下のような特定のメンバー関数を自動的に生成します。

- インスタンスを文字列で出力する関数 ( `toString()` )
- インスタンスを比較する関数 ( `equals()` )
- インスタンスをコピーする関数 ( `copy()` )
- [destructuring declaration](./Destructuring%20declarations(分解宣言).md) を利用するための関数 ( `componentN()` )


## 基本形

`data` 修飾子を付与します。

```Kotlin
data class User(val name: String, val age: Int)
```


## 自動生成されるメンバ

### equals() 関数

Kotlin の data クラスにおける equals 関数の実装は 「等価」 です。

data クラスでは、 equals 関数は自動的に生成され、プロパティの値を基に等価性を判断します。つまり、 data クラスのインスタンスが等価であるためには、同じ型であり、全てのプロパティの値が等しい必要があります。

```kotlin
data class Person(val name: String, val age: Int)

val person1 = Person("Alice", 25)
val person2 = Person("Alice", 25)
val person3 = Person("Bob", 30)

println(person1 == person2) // true
println(person1 == person3) // false
```


### hashCode() 関数


### toString() 関数

`クラス名(プロパティ名=値, プロパティ名=値)` 形式の文字列が生成されます。

例えば `"User(name=John, age=42)"` のような感じになります。


### componentN() 関数

`N` には正の整数が入ります。  
`componentN()` 関数は `N` 番目のプロパティを取得するゲッターの役割を果たします。

`componentT()` 関数は、 Destructuring declarations を利用するために必要なメソッドになります。


### copy() 関数

インスタンスのコピーを作成します。

元のインスタンスと同じクラスのインスタンスを生成します。  
生成したインスタンスのプロパティには、元のインスタンスのプロパティを代入します。

つまり、以下のような `User` という `data` クラスに対して

```kotlin
data class User(val name: String, val age: Int)
```

以下のような `copy()` 関数が自動で生成されます。

```kotlin
fun copy(name: String = this.name, age: Int = this.age) = User(name, age)
```

`copy()` 関数の利用は以下のようになります。

```kotlin
val jack = User(name = "Jack", age = 1)
// name が同じで、 age が 2 のインスタンスが生成されます。
val olderJack = jack.copy(age = 2)
```


## 自動生成の注意点

### 意味のあるコードが生成される条件

自動生成されたコードの一貫性と意味のある動作を保証するには、 `data` クラスが次の要件を満たす必要があります。

- プライマリコンストラクターには少なくとも 1 つのパラメーターが必要です。
- すべてのプライマリコンストラクターパラメーターは、 val または var としてマークされている必要があります。
- `data` クラスを `abstract` 、 `open` 、 `sealed` 、または内部にすることはできません。


### 独自実装が優先されるケース

`data` クラスの中に自分で `equals()` 、 `hashCode()` 、 `toString()` を実装した場合は、自分の実装が優先され、自動実装は行われません。

また、 `data` クラスのスーパークラスで `final` 修飾子付きで `equals()` 、 `hashCode()` 、 `toString()` を実装した場合は、スーパークラスの実装が優先され、自動実装は行われません。


### 独自実装ができないケース

`data` クラスの内部では、明示的に `ComponentN()` 関数や `copy()` 関数を定義することはできません。


### componentN() 

親に `componentN()` 関数が定義されており、それが自分の `data` クラスでオーバーライドできない場合には、エラーになります。

例えば、親の `componentN()` 関数が `final` で定義されている場合にエラーとなります。


### equals() 関数のシグネチャ

いくら自分のクラスで `equals()` 関数を実装しても、シグネチャが間違っていては、 `equals()` 関数は呼ばれません。

正しいシグネチャは `equals(other: Any?)` です。これ以外のシグネチャ (例えば `equals(other: Foo)` ) を実装しても、意図したタイミングで equals() 関数は呼ばれません。


## 自動生成される関数から一部のプロパティを除外する

プライマリコンストラクタの引数にプロパティを定義するのではなく、  
`data` クラスの内部にプロパティを定義することで、  
そのプロパティを自動生成される関数に含まれないようにすることが可能です。

例えば、以下のコードでは、 `name` プロパティは、プライマリコンストラクタに引数として定義されており、  
`age` プロパティは、クラスの内部に定義されています。

```kotlin
data class Person(val name: String) {
    var age: Int = 0
}
```

これにより、 `name` が同じで `age` が異なる二つの `Person` オブジェクトが存在している場合は、  
例えば、 `equals()` 関数は、これら二つのインスタンスを同一のもとして判定します。

`equals()` 関数以外にも、 `toString()` 、 `hashCode()` 、 `copy()` 関数も全て同じです。  
これらの関数に、 `age` プロパティの情報は含まれません。


