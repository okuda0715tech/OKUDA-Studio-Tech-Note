- [Value Object とは](#value-object-とは)
  - [基本定義](#基本定義)
  - [よくある勘違い](#よくある勘違い)


# Value Object とは

## 基本定義

Value Object とは

- 値が同じなら「同じもの」とみなす
- ID を持たない。 ( ID で同じものなのかを判定しない)
- 原則として不変
- 自分の妥当性を自分で保証する

```kotlin
Email("a@b.com") == Email("a@b.com") // true
```

## よくある勘違い

以下はよくある間違った認識です。

- data class だから Value Object
- equals が実装されているから Value Object

つまり、「 ID で同じかを判定していない」ことが本質





