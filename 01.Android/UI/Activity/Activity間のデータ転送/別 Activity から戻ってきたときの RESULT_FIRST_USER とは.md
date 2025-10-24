- [別 Activity から戻ってきたときの RESULT\_FIRST\_USER とは](#別-activity-から戻ってきたときの-result_first_user-とは)
  - [概要](#概要)
  - [RESULT\_FIRST\_USER の意味](#result_first_user-の意味)
  - [RESULT\_FIRST\_USER の使い方](#result_first_user-の使い方)
  - [使用例](#使用例)


# 別 Activity から戻ってきたときの RESULT_FIRST_USER とは

## 概要

別の Activity から自分のアプリの Activity に戻ってきた際に  
渡される結果コードには、一般的には以下のものがあります。

- `Activity.RESULT_OK(-1)`
- `Activity.RESULT_CANCELLED(0)`

これらが何を意味するのか理解するのは容易です。

しかし、それ以外に `Activity.RESULT_FIRST_USER(1)` という値も  
存在しています。  
これはいったいどういったもので、どういう風に使用するかについて、  
この記事では説明していきます。


## RESULT_FIRST_USER の意味

まず、 FIRST_USER とは、アプリのユーザーではなく、  
アプリの開発者を示しているそうです。


## RESULT_FIRST_USER の使い方

Activity の RESULT を返す側のアプリをアプリ開発者が作成する場合、  
`RESULT_OK(-1)` と `RESULT_CANCELLED(0)` 以外にも  
任意の値を返したい場合があるでしょう。

その場合に、アプリで定義する RESULT に `RESULT_FIRST_USER`  
という下駄を履かせるのに使用するそうです。

使用例は、以下に示します。  
(正直言って、下駄を履かせなくても、普通に 1 から採番していけば  
良い気がするので、自分には、なぜこんなことをしているのかわかりません。  
ただ、 StackOverFlow で調べたところ、そのような使い方をするようでした。)


## 使用例

```Java
public static final int MY_RESULT_CODE = Activity.RESULT_FIRST_USER + 1;
```



