- [独自のアノテーションを定義する](#独自のアノテーションを定義する)
  - [アノテーションでできること](#アノテーションでできること)
  - [アノテーションのインターフェースを定義](#アノテーションのインターフェースを定義)
  - [アノテーションの使用](#アノテーションの使用)
  - [アノテーションの処理を定義](#アノテーションの処理を定義)
  - [実行結果](#実行結果)


# 独自のアノテーションを定義する

Java では独自のアノテーションを定義できます。


## アノテーションでできること

アノテーションで表現できることは、以下の 2 点です。

1. アノテーションが付与されているかどうか？
2. アノテーションのパラメータに何が渡されているか？

つまり、アノテーションで実現できることは、以下の 2 点です。

1. アノテーションが付与されているかどうかを判定して、付与されていた場合に何らかの独自の処理を実施する。
2. アノテーションにパラメータを渡すことで、パラメータに応じた独自の処理を実施する。


## アノテーションのインターフェースを定義

独自のアノテーションを作成するには、 @interface キーワードを使用します。以下の手順で作成できます。

```java
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// アノテーションの適用対象をクラスとメソッドに指定
@Target({ElementType.TYPE, ElementType.METHOD})
// アノテーションの有効期間をランタイムまでとする
@Retention(RetentionPolicy.RUNTIME)
public @interface MyAnnotation {
    String value() default "default";
    int count() default 1;
}
```

- `@Target` : アノテーションを付与できる対象（クラス、メソッド、フィールドなど）を指定。
- `@Retention` : アノテーションの保持期間を指定（ソースコードのみ、コンパイル時まで、実行時まで）。
- `@interface` : アノテーションを定義するキーワード。


## アノテーションの使用

アノテーションを使用するには、定義したアノテーションをクラスやメソッドに付与するだけです。また、必要に応じて、そのアノテーションにパラメータを設定するだけです。

```java
@MyAnnotation(value = "Test", count = 5)
public class MyClass {
    @MyAnnotation("Method Annotation")
    public void myMethod() {
        System.out.println("Method executed");
    }
}
```


## アノテーションの処理を定義

リフレクションを使用することで、独自に定義したアノテーションの処理を定義することができます。

```java
import java.lang.reflect.Method;

public class AnnotationProcessor {
    public static void main(String[] args) throws Exception {
        // クラスのアノテーションを取得
        Class<MyClass> obj = MyClass.class;
        if (obj.isAnnotationPresent(MyAnnotation.class)) {
            MyAnnotation annotation = obj.getAnnotation(MyAnnotation.class);
            System.out.println("Class Annotation: value = " + annotation.value() + ", count = " + annotation.count());
        }

        // メソッドのアノテーションを取得
        Method method = obj.getMethod("myMethod");
        if (method.isAnnotationPresent(MyAnnotation.class)) {
            MyAnnotation annotation = method.getAnnotation(MyAnnotation.class);
            System.out.println("Method Annotation: value = " + annotation.value());
        }
    }
}
```


## 実行結果

```
Class Annotation: value = Test, count = 5
Method Annotation: value = Method Annotation
```





