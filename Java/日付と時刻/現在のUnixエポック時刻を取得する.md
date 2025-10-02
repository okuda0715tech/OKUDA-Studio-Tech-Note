<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [現在のUnixエポック時刻を取得する](#現在のunixエポック時刻を取得する)
<!-- TOC END -->


# 現在のUnixエポック時刻を取得する

「たった今、まさにこの瞬間に、協定世界時 (UTC) で、 1970 年 1 月 1 日 0 時 から何ミリ秒経過したか」 を取得する。

```java
// import java.lang.System;

long currentTimeMillis = System.currentTimeMillis();
```

【補足】  
取得できる時刻にタイムゾーンは関係ない。  
取得するのは、協定世界時であり、どこの国にいて時刻を取得しようと、  
「協定世界時」というたった一つの時計が、 1970 年からたった今までに何ミリ秒経過したかを取得する。
