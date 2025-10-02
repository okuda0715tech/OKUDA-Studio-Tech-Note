<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [char と String 間の変換](#char-と-string-間の変換)
  - [char から String への変換](#char-から-string-への変換)
    - [一つの char を String に変換したい場合](#一つの-char-を-string-に変換したい場合)
    - [複数の char をつなげて、一つの String に変換したい場合](#複数の-char-をつなげて一つの-string-に変換したい場合)
    - [2 進数、 10 進数、 8 進数、 16 進数の文字列に変換する](#2-進数-10-進数-8-進数-16-進数の文字列に変換する)
  - [String から char への変換](#string-から-char-への変換)
    - [String を char の配列に格納する](#string-を-char-の配列に格納する)
    - [String 内の N 番目の文字を取得して、 char に格納する](#string-内の-n-番目の文字を取得して-char-に格納する)
<!-- TOC END -->


# char or int と String 間の変換

## char から String への変換

### 一つの char を String に変換したい場合

```java
char c = 'A';
String s1 = Character.toString(c);
String s2 = String.valueOf(c);
```


### 複数の char をつなげて、一つの String に変換したい場合

様々な方法がありますので、以下の例を参考にしてください。

```java
char c1 = 'A';
char c2 = 'B';

// NG パターン
String s1 = c1 + c2; // c1 + c2 は int になり、 int を String に代入することはできません。
// OK パターン
String s2 = c1 + "" + c2;
String s3 = String.valueOf(c1) + String.valueOf(c2); // String.valueOf() はどちらか片方だけでも OK
String s4 = String.format("%c%c", c1, c2);
String s5 = new String(new char[] { c1, c2 });
String s6 = new StringBuilder().append(c1).append(c2).toString();
String s7 = new StringBuilder().append(new char[] {c1, c2}).toString();
```


### 2 進数、 10 進数、 8 進数、 16 進数の文字列に変換する

```java
char c = 'A';

String s1 = String.format("%16s", Integer.toBinaryString(c)).replace(" ", "0"); // 2進数
String s2 = String.format("%06o", (int) c); // 8進数
String s3 = String.format("%d", (int) c); // 10進数
String s4 = String.format("%02x", (int) c); // 16進数

System.out.println(s1); // → "0000000001000001"
System.out.println(s2); // → "000101"
System.out.println(s3); // → "65"
// 10 進数で print するだけなら、単に int や short へキャストするだけでも可
System.out.println((int) c); // → "65"
System.out.println(s4); // → "41"
```


## String から char への変換

### String を char の配列に格納する

この方法は、 String に含まれる文字が、 16 ビットで表現可能な文字だけで構成されている場合にのみ、使用可能です。  
その理由は、 `toCharArray()` メソッドの返値が `char` 型であるためです。  
`char` 型は、 16 ビットのデータ形式であるため、 32 ビットで表される文字は、正しく認識できません。

```java
String s = "ABC";
// 配列の要素に一文字ずつ格納する。
char[] c = s.toCharArray();
```


### String 内の N 番目の文字を取得して、 char に格納する

String 内の最初の一文字目を 0 番目として、 N 番目の場所にある文字を取得します。  
対象の文字列に含まれる文字が、必ず 16 ビット文字であるという保障があるかどうかで、取得方法が異なります。


#### 【非推奨】対象の文字列に含まれる文字が必ず 16 ビットであるという保障がある場合

この方法は、 32 ビット文字に対応していないため、あまり使用する機会はないでしょう。

```java
String s = "ABC";
char c = s.charAt(1); // B が取得されます。
```

もしも、サロゲートペアの文字 ( 32 ビット) を `charAt()` メソッドで取得した場合は、  
完全な文字を取得することができず、上位サロゲート (前半 16 ビット) or  
下位サロゲート (後半 16 ビット) を取得することになります。


#### 対象の文字列に含まれる文字が必ず 16 ビットであるという保障がない場合

例えば、 「𪛊」 という文字は 32 ビット文字です。  
Unicode のコードポイントは、 16 進数で `2A6CA` 、 10 進数で `173770` です。  
上位サロゲートは、 16 進数で `D869` 、 10 進数で `55401` です。  
下位サロゲートは、 16 進数で `DECA` 、 10 進数で `57034` です。  

```java
String s = "𪛊ABC";
int c1 = s.codePointAt(0); // 173770 ("𪛊") が取得されます。
int c2 = s.codePointAt(1); // 57034 ("𪛊" の下位サロゲート) が取得されます。
```


### String 内の文字を一文字ずつ取得して、 char に格納する

この方法は、 `String` に格納されたデータに 16 ビットと 32 ビットの文字が混在している場合でも使用可能です。

```java
String s = "𪛊ABC";
int i = 0;
while(i < s.length()){
  int codePoint = s.codePointAt(i);
  if(Character.isBmpCodePoint(codePoint)){
    // Unicode コードポイントが、基本多言語面 ( BMP ) の範囲に含まれる場合
    // すなわち、 16 ビットで表現可能な文字である場合

    // 16 ビットで表現される文字である場合は、インデックスを 1 つ進める。
    i++;
  } else if(Character.isSupplementaryCodePoint(codePoint)){
    // Unicode コードポイントが補助文字の範囲に含まれる場合
    // すなわち、 16 ビットでは表現不可能であり、 32 ビットで表現可能な文字である場合

    // 32 ビットで表現される文字である場合は、 16 ビットで表現される場合の 2 倍の
    // 領域を使用してデータを保持しているため、インデックスを 2 つ進める必要がある。
    i = i + 2;
  }
}
```


## int から String への変換

サロゲートペアの文字は、 32 ビットで表現されるため、 int 型の変数に格納されています。  
サロゲートペアのコード値 ( int ) を String 型に変換するには、以下のようにします。

```java
int i = 0x20BB7;
String s = String.format("%c", i);
```
