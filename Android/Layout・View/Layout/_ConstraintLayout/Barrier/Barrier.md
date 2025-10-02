<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [Barrier](#barrier)
  - [概要](#概要)
  - [使いどころ](#使いどころ)
  - [xml のサンプル](#xml-のサンプル)
<!-- TOC END -->


# Barrier

## 概要

Barrier は、複数の View を並べたときに、それらの View の大小関係が一定ではなく、  
どの View を基準に隣の View を配置したら良いかが不明瞭な場合に使用します。


## 使いどころ

次の画像は、 「 ABC 」 という View と、 「ABC...XYZ」 という View が左端をそろえて配置されています。

<img src="./barrier-end.png" width="300">

<p/>

このアプリは、場合によっては、下側に配置されている View の方が幅が短くなることがあるとします。  
その場合、二つの View の右隣に新たな View を配置したい場合は、 「 ABC 」 の右端に制約を  
付けるべきか、それとも 「 ABC...XYZ 」 の右端に制約を付けるべきかを決定することができません。

そんな時に 「 ABC 」 と 「 ABC...XYZ 」 の両方のうち、長い方に制約を付けるという制約の  
記述方法があり、それが Barrier です。


## xml のサンプル

```xml
<androidx.constraintlayout.widget.Barrier
    android:id="@+id/barrier"
    android:layout_width="0dp"
    android:layout_height="0dp"
    app:barrierDirection="end"
    app:barrierMargin="24dp"
    app:constraint_referenced_ids="itemName1, itemName2, itemName3, itemName4" />
```

`app:barrierDirection` : 複数の View のどちら側にバリアを設置するかの指定です。  
大抵の場合、画面の左から右へ View が配置されていくため、 `end` を指定します。  
`app:barrierMargin` : 基準となる View の端からバリアを設置する場所までのマージンです。  
`app:constraint_referenced_ids` : バリアの基準となる複数の View の ID をコンマ区切りで指定します。
