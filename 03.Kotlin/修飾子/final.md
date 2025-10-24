- [final](#final)
  - [Kotlin における final 修飾子の役割](#kotlin-における-final-修飾子の役割)
  - [Java の final との違い](#java-の-final-との違い)
    - [プロパティ（フィールド）に final を付与した場合の挙動が異なる](#プロパティフィールドに-final-を付与した場合の挙動が異なる)
    - [【参考】 Kotlin では一度 override すると、スーパークラスのプロパティが参照できなくなる。](#参考-kotlin-では一度-override-するとスーパークラスのプロパティが参照できなくなる)


# final

## Kotlin における final 修飾子の役割

Kotlin では、 final 修飾子は以下の役割を果たします。

- クラスの継承を不可にする
- プロパティ、関数の override 不可にする
  - Kotlin では、プロパティの override 可否が設定できるようになった。
  - Java のプロパティは常に override 可の状態であった。

上記のいずれの場合もデフォルト値が final であるため、実際に final を記述する場面は、  
**一度 open にして、 「継承 or override 」 したものを、それ以上 「継承 or override 」 させないようにする場面** でのみ使用することになるでしょう。


## Java の final との違い

### プロパティ（フィールド）に final を付与した場合の挙動が異なる

Java では、フィールドに final を付与することで、 「再代入を不可にする」 という役割がありましたが、  
Kotlin では、 val がその役割を果たすため、 Kotlin の final にその役割はありません。  

Java では、 「サブクラス内で、スーパークラスと同名のフィールドを定義できなくする」 という概念がありませんでした。  
しかし、 Kotlin では、デフォルトでは同名のフィールド定義ができなくなります。  
同名定義を行うためには、明示的に 「 open 」 と 「 override 」 を付与し、継承しなければいけません。


### 【参考】 Kotlin では一度 override すると、スーパークラスのプロパティが参照できなくなる。

Kotlin では、いったん override したら、スーパークラス側のプロパティは、参照できなくなる模様です。

例えば、 Java なら、

```Java
        // Man は Person クラスを継承しているとする。
        Man man = new Man();
        Person person = man;

        // 【参考】 man.addres と person.address は別のメモリ空間に保持されています。
        // 1. Man の address が参照できる
        println(man.address);
        // 2. Person の address が参照できる (Java の場合のみ可能)
        println(person.address);
```

上記の通り、 Java では、 Man のインスタンスを Person クラスの変数に代入してからその address を参照すると、  
Person クラスの address が参照できました。  
しかし、 Kotlin では、 Person クラスの変数に代入したとしても、 Man クラスの address しか参照できなくなります。  
(たとえ、 as 演算子で明示的に Person クラスにキャストしても参照できません。)





