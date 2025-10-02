- [String](#string)
  - [基本](#基本)
  - [インデックスでアクセス](#インデックスでアクセス)
  - [for ループでアクセス](#for-ループでアクセス)
  - [String 型は Immutable である](#string-型は-immutable-である)
  - [+ 演算子で連結する](#-演算子で連結する)
  - [文字列リテラル](#文字列リテラル)
    - [Escaped strings](#escaped-strings)
  - [Multiline strings](#multiline-strings)
    - [基本](#基本-1)
    - [スペースを取り除く](#スペースを取り除く)
    - [注意点](#注意点)
  - [String templates](#string-templates)
    - [基本](#基本-2)
    - [Multiline strings の中でも String template は有効](#multiline-strings-の中でも-string-template-は有効)
  - [String formatting](#string-formatting)


# String

## 基本

文字列はダブルクォーテーションで囲まれます。

```kotlin
val str = "abcd 123"
```


## インデックスでアクセス

文字列は、その N 番目の文字にインデックスでアクセスすることが可能です。

```kotlin
val str = "abcd" 
println(str[1]) // b
```


## for ループでアクセス

文字列は for ループを使用して、一文字ずつアクセスすることが可能です。

```kotlin
val str = "abcd" 
for (c in str) {
    print(c + ",") // a,b,c,d,
}
```


## String 型は Immutable である

String 型は Java 同様に Immutable となっています。


## + 演算子で連結する

+ 演算子の左辺が String 型なら、 + は文字列連結として機能します。

```kotlin
val s = "abc" + 1
println(s + "def")
// abc1def    
```

ほとんどの場合、文字列の連結には、 String テンプレートかマルチライン String が好まれます。


## 文字列リテラル

Kotlin には以下の二種類の文字列リテラルが存在しています。

- Escaped strings
- Multiline strings


### Escaped strings

Escaped strings とは、その名の通り、エスケープされた文字を含む文字列です。例えば、以下のような文字列です。

```kotlin
val s = "Hello, world!\n"
```

エスケープは従来の方法でバックスラッシュ ( `\` ) を使用して行われます。サポートされているエスケープシーケンスのリストについては、 「 [Characters](https://kotlinlang.org/docs/characters.html) 」 ページを参照してください。


## Multiline strings

### 基本

Multiline strings は、新しい行と任意のテキストを含むことができます。三つのダブルクォーテーション ( `"""` ) で囲まれて表現されます。

エスケープ文字を含まず、改行や他のどんな文字を含みます。

```kotlin
val text = """
    for (c in "foo")
        print(c)
"""
print(text)
```

```
実行結果
----------------
    for (c in "foo")
        print(c)
```


### スペースを取り除く

文字列に対して半角スペースと全角スペースを除外した結果を取得することも可能です。

除外したいスペースの右側に縦棒 ( `|` ) を入れ、文字列に対して `trimMargin()` 関数を実行します。

```kotlin
// 一行目と二行目は半角スペースを入れています。
// 三行目と四行目は全角スペースを入れています。
val text = """
    |Tell me and I forget.
    Teach me and I remember.
　|Involve me and I learn.
　(Benjamin Franklin)
""".trimMargin()
```

```
実行結果
----------------
Tell me and I forget.
    Teach me and I remember.
Involve me and I learn.
　(Benjamin Franklin)
```

縦棒以外の記号を使用することも可能です。その場合は、次のように関数の引数にその文字を指定してください。 `trimMargin(">")` 


### 注意点

- 最後の `"""` と同一行に存在するスペースは常に除外される。
  - 最初の `"""` と同一行に存在するスペースは明示的に除外しない限り除外されない。
- 最初の文字や空白が現れる前の改行は除外される。
- 最後の文字以降の改行は除外される。
- ドル記号 `$` だけは特殊文字として認識されてしまうことがある。詳細は後続の String templates の項で説明します。


## String templates

### 基本

String リテラルでは、テンプレート表現が使用できます。テンプレート表現が処理された後、自動的に `toString()` 関数が呼ばれます。

```kotlin
val i = 10
println("i = $i") 
// i = 10

val letters = listOf("a","b","c","d","e")
println("Letters: $letters") 
// Letters: [a, b, c, d, e]
```

コード部分を明示するには中カッコを使用します。

```kotlin
val s = "abc"
println("$s.length is ${s.length}") 
// abc.length is 3
```


### Multiline strings の中でも String template は有効

Multiline strings の中でも String template は有効です。すなわち、ドル記号 `$` が特殊文字として扱われるということです。ただし、常に特殊記号として扱われるわけではありません。 `$` の直後に変数として使用可能な文字が続く場合のみ、 String template として認識されるため、特殊文字として扱われます。例えば、

```kotlin
// これは通常の文字として認識されます。
val a = """$'"""
println(a) // $'

// これは String template として認識されます。
val s = "STRING"
val b = """$s"""
println(b) // STRING
```

自分の思った通りに認識してくれれば良いですが、そうならない場合もあるでしょう。特に、意図せず String template として認識されてしまうことが問題になることがあります。その逆は起こりえないでしょう。そのような場合は以下のように対応することが可能です。

```kotlin
// ${'$'} とすることで明示的に $ という文字であることを示せます。
val price = """
${'$'}_9.99
"""
print(price) // $_9.99
```


## String formatting

String データをフォーマットするには `String.format()` 関数を使用します。

使い方はだいたいは Java と同じっぽい。

`String.format()` 関数は、 String templates と似た機能を提供します。ただし、 String.format() はより多くのオプションが用意されているため、多様なフォーマットに対応できます。


