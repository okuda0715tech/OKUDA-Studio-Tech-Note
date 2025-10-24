- [dp-px 変換と sp-px 変換](#dp-px-変換と-sp-px-変換)
  - [dp を px に変換する](#dp-を-px-に変換する)


# dp-px 変換と sp-px 変換

## dp を px に変換する

```kotlin
val px = with(LocalDensity.current) {
    50.dp.toPx()
}
```

with 関数のラムダ式内で、 Density インスタンス ( LocalDensity.current の戻り値) を直接使用していないのに、なぜ with(LocalDensity.current){} ブロックが必要になるのか疑問に思った場合は、以下を参照してください。

- [拡張関数(レシーバー付き関数).md -> this 系のスコープ関数のラムダ式内から呼び出す場合の例](../../Kotlin/関数/拡張関数(レシーバー付き関数).md/#this-系のスコープ関数のラムダ式内から呼び出す場合の例)



