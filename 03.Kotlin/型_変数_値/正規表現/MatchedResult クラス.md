- [MatchedResult クラス](#matchedresult-クラス)
  - [メンバー](#メンバー)
  - [value](#value)
  - [groupValue](#groupvalue)
  - [range](#range)
  - [groups](#groups)
  - [destructured](#destructured)


# MatchedResult クラス

## メンバー

- value : `String`
- groupValue : `List<String>`
- range : `IntRange`
- groups : `MatchedGroupCollection`
- destructed : `Destructed`


## value

value プロパティは一致した文字列全体を示します。

```kotlin
val userData = "123,Taro Yamada,1980-11-20"
val userRegex = """\d+,[a-zA-Z ]+,[0-9-]+""".toRegex()
val matchResult = userRegex.matchEntire(userData)

println(matchResult?.value)
// 123,Taro Yamada,1980-11-20
```


## groupValue

groupValue プロパティはグループ化された文字列のリストです。

```kotlin
val userData = "123,Taro Yamada,1980-11-20"
val userRegex = """(\d+),([a-zA-Z ]+),([0-9-]+)""".toRegex()
val matchResult = userRegex.matchEntire(userData)

println(matchResult?.groupValues)
// [123,Taro Yamada,1980-11-20, 123, Taro Yamada, 1980-11-20]
```

カンマが被って見にくいのですが、最初の要素は `"123,Taro Yamada,1980-11-20"` です。これは一致文字列全体に相当します。以降、`"123"` , `"Taro Yamada"` , `"1980-11-20"` とグループ化された文字列要素が順に格納されています。


## range

range プロパティは正規表現の繰り返しが検出された開始位置と終了位置を示します。

```kotlin
val regex = Regex("""\d+""")
val matchedResult = regex.find("12345")

println("range=" + matchedResult?.range)
// range=0..4
```

検索文字列を少し変えてみると、

```kotlin
val regex = Regex("""\d+""")
val matchedResult = regex.find("a12345b")

println("range=" + matchedResult?.range)
// range=1..5
```


## groups

group プロパティは MatchedGroupCollection 型のオブジェクトです。このクラスはグループ化された文字列と range (開始位置と終了位置) を一つのクラスにしたものです。


## destructured

```kotlin
val inputString = "John 9731879"
val match = Regex("(\\w+) (\\d+)").find(inputString)!!

// 分解宣言が利用できます。
val (name, phone) = match.destructured
print(name) // John
print(phone) // 9731879

// groupValues は、要素番号 0 に全体の文字列を含みます。
print(match.groupValues.toString())
// [John 9731879, John, 9731879]

val numberedGroupValues = match.destructured.toList()
// destructured は、要素番号 0 に全体の文字列を含みません。
print(numberedGroupValues.toString())
// [John, 9731879]
```



