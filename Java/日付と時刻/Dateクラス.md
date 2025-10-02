<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [Dateクラス](#dateクラス)
	- [現在の日時（曜日）を取得する](#現在の日時曜日を取得する)
	- [フォーマットを指定して日時を出力する（Date -> String 変換）](#フォーマットを指定して日時を出力するdate---string-変換)
	- [SimpleDateFormatのフォーマットを途中で変更する](#simpledateformatのフォーマットを途中で変更する)
	- [String -> Date 変換](#string---date-変換)
	- [Date <-> Calendar 変換](#date--calendar-変換)
	- [日付の加算、減算](#日付の加算減算)
	- [Date同士の前後比較](#date同士の前後比較)
		- [before/afterを使用した方法](#beforeafterを使用した方法)
		- [compareToを使用した方法](#comparetoを使用した方法)
	- [Long型とDate型の相互変換ユーティリティ](#long型とdate型の相互変換ユーティリティ)
<!-- TOC END -->


# Dateクラス

## 現在の日時（曜日）を取得する

**Sample.java**

```Java
import java.util.Date;

Date date = new Date();
System.out.println(date.toString()); // Sun Jun 11 08:43:53 JST 2017
```

## フォーマットを指定して日時を出力する（Date -> String 変換）

**Sample.java**

```Java
import java.text.SimpleDateFormat;

Date date = new Date();
SimpleDateFormat sdf = new SimpleDateFormat("yyyy'年'MM'月'dd'日'E'曜日'k'時'mm'分'ss'秒'", Locale.getDefault());
System.out.println(sdf.format(date)); // 2017年04月06日木曜日14時50分52秒
```

フォーマットに日本語を混ぜたい場合には、日本語の部分を（シングルクォテーション）で挟み込みます。
yは年、Mは月、dは日、Eは曜日、kは時、mは分、sは秒です。

記号 | 意味          | 例1          | 例2                  | 例3                            | 備考
-----|---------------|--------------|----------------------|--------------------------------|------------------------------
y    | 年            | yyyy -> 2019 | yy -> 19             |                                |
M    | 月            | MM -> 01     | M -> 1               |                                |
d    | 日            | dd -> 01     | d -> 1               |                                |
E    | 曜日          | E -> 月      |                      |                                |
a    | 午前/午後     | a -> 午前    |                      |                                |
k    | 時   | kk -> 13      | k -> 13                     |                                |1〜24時(※1)
h    | 時    | hh -> 01     | h -> 1               |                                |1〜12時
K    | 時    | KK -> 01      | K -> 1                     |                                |0〜11時(大文字は0始まり)
H    | 時    | HH -> 13     | H -> 13               |                                |0〜23時
m    | 分            | mm -> 01     | m -> 1               |                                |
s    | 秒            | ss -> 01     | s -> 1               |                                |
S    | ミリ秒        | S -> 1/10秒  | SS -> 1/10秒 1/100秒 | SSS -> 1/10秒 1/100秒 1/1000秒 |
w    | 年の第何週目  | ww -> 01     | w -> 1               |                                |
W    | 月の第何週目  | WW -> 01     | W -> 1               |                                |
D    | 年の第何日目  | DDD -> 051   | D ->51               |                                |
F    | 月の第何X曜日 | F -> 2       |                      |                                | 第二月曜日、第二火曜日などの2
E    | 曜日          | E -> 月      |                      |                                | 言語設定により変わる
u    | 曜日の整数    | u -> 1       |                      |                                | 1:月,2:火,3:水...

(※1) kとhは大文字と小文字で24時間表記が逆のように思えるが、この表の記載が正しい


### Locale を指定する効果

Locale に応じて、曜日名や月名の表示言語が変わります。

次の例は、フォーマットが `"EEEE, MMMM d, yyyy"` の場合の例です。

```java
Date date = new Date();

// 英語（アメリカ）
SimpleDateFormat sdfUS = new SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.US);
System.out.println(sdfUS.format(date)); 
// 出力例: "Tuesday, December 6, 2024"

// 日本語
SimpleDateFormat sdfJP = new SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.JAPAN);
System.out.println(sdfJP.format(date)); 
// 出力例: "火曜日, 12月 6, 2024"
```


## SimpleDateFormatのフォーマットを途中で変更する

SimpleDateFormatインスタンスを複数作成しなくても、フォーマットを途中で変更することができる。

**Sample.java**

```Java
//フォーマットを指定する
SimpleDateFormat sdf = new SimpleDateFormat("変更前：yyyy/MM/dd");
System.out.println(sdf.format(new Date())); // 変更前：2017/04/06

//フォーマットを変更する
sdf.applyPattern("変更後：yyyy年MM月dd日 E");
System.out.println(sdf.format(new Date())); // 変更後：2017年04月06日 Thu
```


## String -> Date 変換

**Sample.java**

```Java
String strDate = "2017/04/10";
try {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    sdf.setLenient(false); // Lenient = 緩やかな、寛容な
    Date date = sdf.parse(strDate);
} catch (ParseException e) {

}    
```

`setLenient(false)`の場合、4月31日など、存在しない日付を`parse`すると`ParseException`が発生します。
`setLenient(true)`もしくはメソッド呼び出しなしの場合、4月31日は、5月1日としてパースに成功します。


## Date <-> Calendar 変換

「日付の加算、減算」の章を参照


## 日付の加算、減算

Date型を加算、減算するには、 `Date` -> `Calendar` 変換して加算、減算してから、  
`Calendar` -> `Date` に逆変換します。

**Samaple.java**

```Java
Date date = new Date();

// Date型の日時をCalendar型に変換
Calendar calendar = Calendar.getInstance();
calendar.setTime(date);

// 日時を加算する(減算したい場合は、第二引数をマイナスにする)
calendar.add(Calendar.MONTH, 3);

// Calendar型の日時をDate型に戻す
Date d1 = calendar.getTime();
```

扱っている日付が Date 型ではなく、 String 型である場合は、 String <-> Date 変換を行います。  
以下のような流れで変換し、加減算処理し、逆変換します。  
String -> Date -> Calendar -> 加減算 -> Calendar -> Date -> String

**Sample.java**

```Java
// 加算されるDate型の日時の作成
SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
Date date = new Date();
try {
    date = sdf.parse("2017/01/01 12:00:00");
} catch(ParseException e) {
    e.printStackTrace();
}

// Date型の日時をCalendar型に変換
Calendar calendar = Calendar.getInstance();
calendar.setTime(date);

// 日時を加算する
calendar.add(Calendar.MONTH, 3);

// Calendar型の日時をDate型に戻す
Date d1 = calendar.getTime();
textView.setText(sdf.format(d1));
```


## Date同士の前後比較

### before/afterを使用した方法

**Sample.java**

```Java
Date date1 = new Date(2018, 5, 1, 13, 10, 20);
Date date2 = new Date(2018, 5, 1, 13, 10, 21);

System.out.println(date1.before(date2)); // true
System.out.println(date2.before(date1)); // false

System.out.println(date1.after(date2)); // false
System.out.println(date2.after(date1)); // true
```

### compareToを使用した方法

**Sample.java**

```Java
Date date1 = new Date(2018, 5, 1, 13, 10, 20);
Date date2 = new Date(2018, 5, 1, 13, 10, 21);

System.out.println(date1.compareTo(date2)); // -1(昔.compareTo(新))
System.out.println(date2.compareTo(date1)); // 1(新.compareTo(昔))
System.out.println(date1.compareTo(date1)); // 0(同じ)
```

**注意**
ミリ秒まで一致していないと`compareTo`の結果は`0`にはならない。


## Long型とDate型の相互変換ユーティリティ

```Java
public class DateTimeConverter {
    @TypeConverter
    public static Date longToDate(Long value) {
        if(value == null){
            return null;
        }else{
            return new Date(value);
        }
    }

    @TypeConverter
    public static Long dateToLong(Date date) {
        if (date == null) {
            return null;
        } else {
            return date.getTime();
        }
    }
}
```
