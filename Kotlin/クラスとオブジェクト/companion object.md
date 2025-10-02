- [companion object](#companion-object)
  - [基本形](#基本形)
  - [companion object のインスタンスは包含クラスの static メンバーとして保持される](#companion-object-のインスタンスは包含クラスの-static-メンバーとして保持される)
  - [フィールドは常に static フィールドなので注意](#フィールドは常に-static-フィールドなので注意)
  - [Java から static メソッドとして呼び出したい場合は static メソッド化](#java-から-static-メソッドとして呼び出したい場合は-static-メソッド化)
  - [名前を省略できる](#名前を省略できる)
  - [companion object のインスタンスを参照する](#companion-object-のインスタンスを参照する)
  - [包含クラスから companion object のメンバへのアクセスは可能](#包含クラスから-companion-object-のメンバへのアクセスは可能)
  - [companion object から包含クラスのメンバへのアクセスは不可](#companion-object-から包含クラスのメンバへのアクセスは不可)


# companion object

## 基本形

クラス内部の `object` 宣言は、 `companion` 修飾子を付与することが可能です。

```kotlin
class MyClass {
    companion object Factory {
        fun create(): MyClass = MyClass()
    }
}
```

`companion object` のメンバーは、包含クラスの `クラス名.メンバー名` でアクセスすることができます。

```kotlin
val instance = MyClass.create()
```


## companion object のインスタンスは包含クラスの static メンバーとして保持される

companion object のインスタンスは包含クラスの static メンバーとして保持されます。


## フィールドは常に static フィールドなので注意

`companion object` 内部に定義されたプロパティは、 `static` なバッキングフィールドを持ちます。  
`static` フィールドであるということは、 **包含クラスのインスタンスが破棄された後も  
値が残り続けるということ** なので、扱いに注意が必要です。

アクセッサーはインスタンス関数になっています。  
アクセッサーも `static` にするためには、対象のプロパティに対して `@JvmStatic` アノテーションを付与します。


**可視性**

通常、 `companion object` 内に定義されたプロパティは、 `private` ですが、  
以下のいずれかの方法で、公開される可能性があります。

- `@JvmField` アノテーション
- `lateinit` 修飾子
- `const` 修飾子


## Java から static メソッドとして呼び出したい場合は static メソッド化

`companion object` や `object` 宣言内に定義された関数は、常にインスタンス関数が生成されます。  
ただし、関数に `@JvmStatic` アノテーションを付与した関数に関しては、  
追加で `static` メソッドも生成されます。

`@JvmStatic` を付与することによって、 Java から関数を呼び出す場合に、 `static` メソッドとして呼び出すことが可能になります。  
Kotlin から呼び出す場合には、常にインスタンス関数が呼び出されているものと思われます。

```kotlin
class KotlinClass {
    companion object {
        fun companionFunc() {
            Log.d("test", "JvmStatic なし")
        }

        @JvmStatic
        fun companionFunc2() {
            Log.d("test", "JvmStatic あり")
        }
    }
}
```

```Java
public class JavaClass {
    public void call() {
        // @JvmStatic なしの関数を呼び出す場合
        KotlinClass.Companion.companionFunc();
        // @JvmStatic ありの関数を呼び出す場合
        // static メソッドが呼び出せる
        KotlinClass.companionFunc2();
        // もちろん、インスタンス関数の呼び出しも可能
        KotlinClass.Companion.companionFunc2();
    }
}
```


## 名前を省略できる

`companion object` の名前は省略することが可能です。  
省略した場合は、自動的に `Companion` という名前が定義されます。

```kotlin
class MyClass {
    companion object { }
}
```


## companion object のインスタンスを参照する

`companion object` のインスタンスを参照する場合は、  
`companion object` の名前を省略して、包含クラスの `クラス名` でアクセスするのが一般的ですが、  
`クラス名.コンパニオンオブジェクト名` でもアクセスができます。

```kotlin
class MyClassA {
    // 名前なし
    companion object {
    }
}

class MyClassB {
    // 名前あり
    companion object Name {
    }
}

// インスタンスへのアクセス
// OK パターン 4 種
MyClassA.Companion
MyClassA
MyClassB.Name
MyClassB

// NG パターン
// Name という名前が付けられているので Companion という名前ではアクセスできない
MyClassB.Companion
```


## 包含クラスから companion object のメンバへのアクセスは可能

包含クラスから companion object の private なメンバーへのアクセスは可能です。

```kotlin
class MyClassA {

    fun memberMethod() {
        // private メンバーへのアクセス可能
        companionMethod()
    }

    companion object {
        private fun companionMethod() {}
    }
}
```


## companion object から包含クラスのメンバへのアクセスは不可

```kotlin
class KotlinClass {

    var outer = "outer"
    
    companion object {
        fun innerFun() {
            // outer は参照できない
            Log.d("test", outer)
        }
    }
}
```

