- [Tuples ( Pair Triple )](#tuples--pair-triple-)
  - [概要](#概要)
  - [Pair](#pair)
    - [to 関数によるインスタンスの生成](#to-関数によるインスタンスの生成)
    - [コンストラクタによるインスタンスの生成](#コンストラクタによるインスタンスの生成)
  - [Triple](#triple)
    - [コンストラクタによるインスタンスの生成](#コンストラクタによるインスタンスの生成-1)


# Tuples ( Pair Triple )

## 概要

Tuples とは、 Pair や Triple クラスを保持するファイル名である。


## Pair

### to 関数によるインスタンスの生成

```Kotlin
val p: Pair<Int, String> = 3 to "Three"
```


### コンストラクタによるインスタンスの生成

```Kotlin
val p: Pair<Int, String> = Pair(3, "Three")
```


## Triple

### コンストラクタによるインスタンスの生成

`Triple` には、 `to` 関数のようなものは用意されていないため、  
コンストラクタでインスタンスを生成します。

```Kotlin
val t: Triple<Int, String, Boolean> = Triple(3, "Three", false)
```




