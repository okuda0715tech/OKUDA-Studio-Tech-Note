- [Greedy vs Reluctant vs Possessive Qualifiers](#greedy-vs-reluctant-vs-possessive-qualifiers)
  - [サンプル](#サンプル)
  - [Greedy Qualifiers（貪欲な量指定子）](#greedy-qualifiers貪欲な量指定子)
    - [概要](#概要)
    - [詳細](#詳細)
  - [Reluctant Qualifiers（消極的な量指定子）](#reluctant-qualifiers消極的な量指定子)
    - [概要](#概要-1)
    - [詳細](#詳細-1)
  - [Possessive Qualifiers（さらに貪欲な量指定子）](#possessive-qualifiersさらに貪欲な量指定子)
    - [概要](#概要-2)
    - [詳細](#詳細-2)


# Greedy vs Reluctant vs Possessive Qualifiers

## サンプル

以下の例について説明していきます。

```kotlin
// 貪欲な量指定子
val regex1 = Regex(""".*foo""")
val matchedResult1 = regex1.matches("xfooxxxxxxfoo") // true
//    val matchedResult1 = regex1.matches("xfooxxxxxxfoox") // false

// 消極的な量指定子
val regex2 = Regex(""".*?foo""")
val matchedResult2 = regex2.matches("xfooxxxxxxfoo") // true
//    val matchedResult2 = regex2.matches("xfooxxxxxxfo") // false

// さらに貪欲な量指定子
val regex3 = Regex(""".*+foo""")
val matchedResult3 = regex3.matches("xfooxxxxxxfoo")
```


## Greedy Qualifiers（貪欲な量指定子）

### 概要

Greedy Qualifiers は、超簡単に述べると、後ろから判定する方法です。


### 詳細

詳細な判定プロセスは以下の通りです。

1. `.*` は、対象の文字列を最後まで読み込んで、 「一致した」 という判定を内部に持つ。
2. `.*` に続く次のパターン (サンプルで言えば `f` ) を、対象の文字列の後ろから一文字ずつバックしながら一致するか判定する。
   1. 一致した場合、次のパターン (サンプルで言えば `o` ) について同じことを繰り返す。
      1. 後続のパターンが全て一致し、対象の文字列に文字が残っていなければ、全体としての 「一致」 を返す。
      2. 後続のパターンが全て一致しても、対象の文字列に文字が残っていれば、全体としての 「不一致」 を返す。
   2. 一致しなかった場合、内部の一致判定を 「不一致」 に書き換え、さらに一文字バックして一致するかどうか判定する。最初の一文字目までバックしても一致と見なされなかった場合には、全体としての 「不一致」 を返す。


## Reluctant Qualifiers（消極的な量指定子）

### 概要

Reluctant Qualifiers は、超簡単に述べると、前から判定する方法です。

Reluctant Qualifiers は、 Non-greedy Qualifiers (非貪欲な量指定子) とも呼ばれます。


### 詳細


1. `.*?` は、対象の文字列に対して、全く一致していないと見なし、次のパターン (サンプルで言えば `f` ) について、対象の文字列の先頭から一致判定を開始します。
   1. 一致しなかった場合、対象の文字列を一文字読み進めて、一致するかどうか判定します。一致しなかった場合は、これを繰り返します。
      1. 対象の文字列の最後の文字についても一致しなかった場合は、全体として 「不一致」 の判定を返します。
   2. 一致した場合、パターンと対象の文字列を一文字ずつ読み進めて、一致するかどうか判定します。
      1. 全てのパターンが一致した後、対象の文字列が残っていなければ、全体として 「一致」 の判定を返します。
      2. 全てのパターンが一致した後、対象の文字列が残っていれば、全体として 「不一致」 の判定を返します。


## Possessive Qualifiers（さらに貪欲な量指定子）

### 概要

Possessive Qualifiers は、超簡単に述べると、前から判定する Greedy Qualifiers をベースにしていますが、バックトラックを行わない方法です。


### 詳細

詳細な判定プロセスは以下の通りです。

1. `.*+` は、対象の文字列を最後まで読み込んで、 「一致した」 という判定を内部に持つ。
2. `.*+` に続く次のパターン (サンプルで言えば `f` ) を、対象の文字列の未判定の部分から開始しようと試みますが、対象の文字列には未判定の部分が残っていないため、判定は常に 「不一致」 と見なされます。





