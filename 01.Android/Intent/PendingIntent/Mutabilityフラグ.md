<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [Mutability フラグ](#mutability-フラグ)
  - [概要](#概要)
  - [実装方法](#実装方法)
<!-- TOC END -->


# Mutability フラグ

## 概要

TargetSDKVersion 31 (Android 12) 以降では、 `PendingIntent` を作成する際に、  
`FLAG_MUTABLE` か `FLAG_IMMUTABLE` を選択する必要があります。

Intent の中身を途中で変更する可能性がある場合には、 `FLAG_MUTABLE` を選択しますが、  
ほとんどの場合は、 `FLAG_IMMUTABLE` で問題ありません。


## 実装方法

```java
PendingIntent pendingIntent = PendingIntent.getBroadcast(
        context,
        tweetExeSchedule.broadcastRequestCode,
        autoTweetExeReceiverIntent,
        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
```
