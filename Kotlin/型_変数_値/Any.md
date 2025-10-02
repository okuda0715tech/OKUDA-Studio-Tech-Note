- [Any](#any)
  - [Any / Any? とは](#any--any-とは)
  - [Kotlin では Object の使用は非推奨](#kotlin-では-object-の使用は非推奨)
  - [Any と Any? と Object の関係](#any-と-any-と-object-の関係)
  - [Any の中身](#any-の中身)


# Any

## Any / Any? とは

`Any` / `Any?` は、 Kotlin における全てのクラスのスーパークラスになります。  
Java でいう `Object` クラスと同等の役割を果たします。


## Kotlin では Object の使用は非推奨

Kotlin においても Java の Object ( java.lang.Object ) は存在しています。  
ただし、 Kotlin で Object クラスを参照すると  
「 Object は推奨されていません。Any に置き換えてください。」 というワーニングが表示されます。


## Any と Any? と Object の関係

Any?  
 ┣ ━ Any  
 ┃ ┗ ━ その他のすべての Non-null  
 ┗ ━ その他のすべての Nullable ( Object も含む)

上記の通り、 Java の `Object` は `null` を許容しますが、 Kotlin の `Any` は `null` を許容していません。  
そのため、 `Object` を `Any` に置き換えたい場合には、 `Any` ではなく `Any?` の方を使用しましょう。


## Any の中身

```Kotlin
public open class Any {

    public open operator fun equals(other: Any?): Boolean

    public open fun hashCode(): Int

    public open fun toString(): String

}
```



