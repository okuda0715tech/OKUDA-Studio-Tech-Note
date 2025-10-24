- [inline value クラス](#inline-value-クラス)
  - [インラインの意味](#インラインの意味)
  - [インライン値クラス](#インライン値クラス)
    - [宣言方法](#宣言方法)
    - [制限](#制限)
    - [使用方法](#使用方法)
    - [インライン値クラスのメリット](#インライン値クラスのメリット)
  - [インライン値クラスへのメンバーの定義](#インライン値クラスへのメンバーの定義)
  - [継承](#継承)
  - [ラッパークラスとして扱う](#ラッパークラスとして扱う)
  - [パラメータにジェネリックを使用する](#パラメータにジェネリックを使用する)
  - [関数名にハッシュコードが付与される場合](#関数名にハッシュコードが付与される場合)
  - [インライン値クラスを引数に持つ関数を Java から呼ぶ](#インライン値クラスを引数に持つ関数を-java-から呼ぶ)
  - [インライン値クラスと typealias](#インライン値クラスと-typealias)
  - [インライン値クラスとクラスデリゲーション](#インライン値クラスとクラスデリゲーション)


# inline value クラス

## インラインの意味

インラインとは、元々、 「直列の、一列に並んだ」 などを意味する単語です。  
これは 「本来、他の場所にあったものを、別の場所に埋め込む」 という  
意味にとらえると、文章が理解できることがよくあります。


## インライン値クラス

### 宣言方法

インライン値クラスを宣言するには、 `class` の前に `value` 修飾子を付与します。

```kotlin
@JvmInline
value class Password(val stringPass: String)
```

現在、 Java には value class の機能がありませんので、  `@JvmInline` アノテーションが必要です。  
ただし、将来的には、 Java にも valeu class が実装されることが予定されており、  
その実装が終われば `@JvmInline` アノテーションが不要になる予定です。


### 制限

インライン値クラスには以下の制限事項があります。

- インライン値クラスは、プライマリコンストラクターに一つだけプロパティを定義する必要があります。
- プロパティには val しか定義できない ( var は不可)
- `===` を使用した比較は不可
  - ラッパークラスとしても扱うし、内部のクラスとしても扱う。
  - その場合、別のインスタンスになるので、同じインスタンスを参照しているかはチェックができない。


### 使用方法

```kotlin
// 実行時には、
// Password クラスはインスタンス化されません
// securePassword プロパティには String 型のデータが設定されます。
val securePassword = Password("XXX-XXX")

// ただし、コンパイル時には、
// ラッパークラスが実在しているかのように振る舞います。
// NG (コンパイルエラー)
val securePassword: String = Password("aaa")
// OK
val securePassword: Password = Password("aaa")

// 参照方法 (通常の Password クラスへのアクセスと同様)
print(securePassword.stringPass) // aaa
```


### インライン値クラスのメリット

場合によっては、値をクラスでラップして、よりドメイン固有の型を作成すると便利です。  
しかし、これは追加のヒープ割り当てにより実行時のオーバーヘッドが発生します。

さらに、ラップされた型がプリミティブである場合、通常ランタイムによって大幅に最適化される一方で、  
そのラッパーは最適化されないため、パフォーマンスに悪影響が出てしまいます。

インライン値クラスを導入することで、ラッピングのメリットを享受しつつ、  
パフォーマンスを維持することが可能になります。


## インライン値クラスへのメンバーの定義

インラインクラスは、以下のメンバーを定義することが可能です。

- プロパティ
- 関数
- `init{}` ブロック
- セカンダリーコンストラクター

プロパティについては、以下の点に注意してください。

- ただし、バッキングフィールドを持てないため、値を保持するようなことはできません。
- ゲッターは static 関数として定義されますが、使用方法は通常のプロパティと同じです。

関数については、以下の点に注意してください。

- 関数は static 関数として定義されますが、使用方法は通常のメンバ関数と同じです。


## 継承

インライン値クラスは、インターフェースを実装することが可能です。

しかし、他のクラスを継承したり、他のクラスに継承されたりすることはできません。

```kotlin
interface Printable {
    fun prettyPrint(): String
}

@JvmInline
value class Name(val s: String) : Printable {
    override fun prettyPrint(): String = "Let's $s!"
}

fun main() {
    val name = Name("Kotlin")
    // ここでも static 関数として呼び出されます。
    println(name.prettyPrint())
}
```


## ラッパークラスとして扱う

コンパイル後のコードにもラッパークラスの情報は保持されています。  
インライン値クラスのインスタンスは、実行時に、  
ラッパークラスの型としても扱うことも可能ですし、  
内部に保持しているクラスの型として扱うことも可能です。

これは、 Int 型をプリミティブ型の int として扱ったり、  
参照型の Integer として扱うのと似ています。

コンパイラは、内部に保持しているクラスの型として使用することを好みます。  
その方がパフォーマンスが向上するためです。

しかし、インライン値クラスがラッパークラスとは別の型として扱われる場合は、  
常にその型にラップされた状態で扱われます。

```kotlin
interface I

@JvmInline
value class Foo(val i: Int) : I

fun asInline(f: Foo) {}
fun <T> asGeneric(x: T) {}
fun asInterface(i: I) {}
fun asNullable(i: Foo?) {}

fun <T> id(x: T): T {
    // ここでは x は一般化されているので T として扱う。
    return x
}

fun main() {
    val f = Foo(42)

    // コンパイル時には Foo 型として扱われます。
    // つまり、実行時には内部プロパティの型として扱われます。
    asInline(f)
    // T 型として扱われます。実行時も同様。
    asGeneric(f)
    // I 型として扱われます。実行時も同様。
    asInterface(f)
    // Foo? 型として扱われます。実行時も同様。
    // ( Foo? クラスは Foo クラスとは別物であるため)
    asNullable(f)

    // f は、最初に id 関数に渡された時は T 型として扱われます。
    // これは、 id 関数内部は一般化されているため、
    // コンパイル時には、どんな型が入ってくるかがわからないためです。
    //
    // その後、文脈から f が Foo 型であることが明らかであるため、
    // id 関数から戻ってきた時に Foo 型に変わります。
    // c に型を明示する場合は Foo 型しか指定できません。
    val c = id(f)
}
```


## パラメータにジェネリックを使用する

インライン値クラスは、ジェネリック型のパラメータを設定することも可能です。

コンパイラは、ジェネリック型を `Any?` か 上限付きの型に解決します。  
上限付きというのは、上記の例でいうと、  
プロパティ c が `Foo` を上限とした型になるということだと思います。

```kotlin
@JvmInline
value class UserId<T>(val value: T)

fun compute(s: UserId<String>) {} 
// 上記の関数について、コンパイラは以下のような関数を生成します。
// fun compute-<hashcode>(s: Any?)
```


## 関数名にハッシュコードが付与される場合

関数の引数にインライン値クラスが渡されてくる場合には、  
その関数名の後ろに自動的にハッシュコードが付与されます。

例えば、以下のような関数は

```kotlin
@JvmInline
value class UInt(val x: Int)

fun compute(x: UInt)
```

コンパイルされると以下のようになります。

```java
public final void compute-<hashcode>(int x)
```

これは、上記の関数とは別に以下の関数があった場合に、  
実行時にシグネチャが一致してしまい、エラーが発生するを防ぐためです。

```kotlin
fun compute(x: Int) { }
```


## インライン値クラスを引数に持つ関数を Java から呼ぶ

Java から、インライン値クラスを引数に持つ関数を呼ぶには、  
あらかじめ、その関数に `@JvmName` アノテーションを付与しておく必要があります。

Java からそのような関数を呼ぶ場合には、  
関数名のハッシュコードを認識できないようです。  
そのため、アノテーションに定義した名前を関数名として呼び出します。

```kotlin
@JvmInline
value class UInt(val x: Int)

fun compute(x: Int) { }

// Java からは computeUInt という名前の関数に見えます。
// Kotlin からは compute という名前の関数に見えます。
@JvmName("computeUInt")
fun compute(x: UInt) { }
```

もし、 `@JvmName` アノテーションを付与しなかった場合、  
Java から見ると、コンパイル時でも  
`compute(x: UInt)` 関数は `compute(x: Int)` に見えて、  
コンパイルエラーとなります。


## インライン値クラスと typealias

`typealias` はある型の別名を定義する修飾子です。  
一見すると、インライン値クラスと同じように見えますが、  
これらには明確な違いが存在しています。

コンパイル時に以下の違いが発生します。

- `typealias` は、元の型との互換性があります。
- インライン値クラスは、元の型との互換性がありません。

```kotlin
typealias NameTypeAlias = String

@JvmInline
value class NameInlineClass(val s: String)

fun acceptString(s: String) {}
fun acceptNameTypeAlias(n: NameTypeAlias) {}
fun acceptNameInlineClass(p: NameInlineClass) {}

fun main() {
    val nameAlias: NameTypeAlias = ""
    val nameInlineClass: NameInlineClass = NameInlineClass("")
    val string: String = ""

    // OK
    // String 型のプロパティに対して、 typealias の型が代入できます。
    acceptString(nameAlias)
    // NG
    // String 型のプロパティに対して、インライン値クラスの型が代入できません。
    acceptString(nameInlineClass)

    // 逆もしかりです。
    // OK
    // typealias の型のプロパティに対して、 String 型が代入できます。
    acceptNameTypeAlias(string)
    // NG
    // インライン値クラス型のプロパティに対して、 String 型が代入できません。
    acceptNameInlineClass(string)
}
```


## インライン値クラスとクラスデリゲーション

インライン値クラスとクラスデリゲーションを組み合わせることも可能です。

```kotlin
interface MyInterface {
    fun bar()
    fun foo() = "foo"
}

// MyInterfaceWrapper は MyInterface を継承しているのではなく、委譲しています。
@JvmInline
value class MyInterfaceWrapper(val myInterface: MyInterface) : MyInterface by myInterface

fun main() {
    val my = MyInterfaceWrapper(object : MyInterface {
        // ここでインターフェースの実装ができます
        override fun bar() {
            // body
        }
    })
    println(my.foo()) // prints "foo"
}
```
