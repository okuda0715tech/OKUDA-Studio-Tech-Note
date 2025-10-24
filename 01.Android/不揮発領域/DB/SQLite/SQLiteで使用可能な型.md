<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [SQLiteで使用可能な型](#sqlite使用可能型)
	- [booleanは使用不可](#boolean使用不可)

<!-- /TOC -->


# SQLiteで使用可能な型

## booleanは使用不可

booleanは使用不可であるため、 `true` は `1` / `false` は `0` に置き換えてDBに保存されます。
