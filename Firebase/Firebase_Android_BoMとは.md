<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [Firebase Android BoM とは](#firebase-android-bom-とは)
  - [概要](#概要)
  - [例](#例)
  - [実際にどのバージョンが使用されているかを確認する方法](#実際にどのバージョンが使用されているかを確認する方法)
<!-- TOC END -->


# Firebase Android BoM とは

## 概要

BoM は、 Bill Of Materials の略で、部品表という意味です。

これは、 Firebase の仕組みではなく、 Gradle の仕組みであり、 Gradle バージョン 5.0 から  
使用できるようになりました。

複数のライブラリのバージョンを個別に指定するのではなく、  
複数のライブラリのバージョンをセットとして保持している BoM を指定することで、  
間接的に各ライブラリのバージョンが指定される仕組みです。

BoM には、あらかじめ、互換性のあるバージョンセットが登録されているため、  
互換性がないことによるエラーを回避することができます。


## 例

以下に Firebase の例を示します。

BoM を使用していない場合は、次のように記述します。

```java
implementation "com.google.firebase:firebase-core:17.4.3"
implementation "com.google.firebase:firebase-messaging:20.2.1"
implementation 'com.google.firebase:firebase-crashlytics:17.1.0'
```

上記の記述は、BoM を使用することによって、次のように記述することができます。

```java
implementation platform('com.google.firebase:firebase-bom:26.6.0')
implementation "com.google.firebase:firebase-core"
implementation "com.google.firebase:firebase-messaging"
implementation "com.google.firebase:firebase-crashlytics"
```


## 実際にどのバージョンが使用されているかを確認する方法

BoM を使用すると、各ライブラリが実際にどのバージョンを使用しているのかがパッと見ただけでは  
わからなくなってしまいます。各ライブラリのバージョンを確認するには、以下の URL を使用します。

[Firebase BoM バージョンの比較](https://firebase.google.com/docs/android/learn-more?hl=ja#compare-bom-versions)

比較する BoM バージョンのどちらかに、確認したい BoM のバージョンを入れて比較を実施します。  
すると、各ライブラリのバージョンがいくつになっているのかを確認することができます。
