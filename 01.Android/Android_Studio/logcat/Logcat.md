<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [Logcat](#logcat)
  - [フォーマット](#フォーマット)
    - [タグ](#タグ)
<!-- TOC END -->


# Logcat

## フォーマット

```
date time PID-TID/package priority/tag: message
```

`PID` は、プロセス ID の略  
`TID` は、スレッド ID の略


### タグ

システムが出力するログメッセージのタグは、メッセージの発信元であるシステムコンポーネントを示します  
( 例 : ActivityManager )

ユーザー定義のタグは、任意の文字列を設定することが可能ですが、システムログとレベルを合わせるなら、  
クラス名が妥当でしょう。
