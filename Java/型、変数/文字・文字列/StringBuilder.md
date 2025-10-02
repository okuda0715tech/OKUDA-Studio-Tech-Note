<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [StringBuilderクラス](#stringbuilderクラス)
	- [指定した位置への文字列の挿入](#指定した位置への文字列の挿入)
	- [文字列の末尾への文字の追加](#文字列の末尾への文字の追加)
<!-- TOC END -->


# StringBuilderクラス

## 指定した位置への文字列の挿入

insertメソッドで文字列の挿入が可能である。

第一引数が挿入位置である。挿入位置は、文字列の何番目に入れるかの指定である。  
0 以上、文字数以下の値を指定する。

```Java
StringBuilder sb = new StringBuilder("ABC");
String s = "D";
sb.insert(0,s);
System.out.println(sb.toString());　// 「DABC」と出力される。
```


## 文字列の末尾への文字の追加

```java
StringBuilder sb = new StringBuilder();
sb.append("AB");
sb.append("CDE");
sb.append("FG");

System.out.println(sb.toString());  // ABCDEFG
```
