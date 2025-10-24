<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [Java の String と Resource の string の違い](#java-の-string-と-resource-の-string-の違い)
  - [Java の String は、アプリ内部で使用する文字の場合に使用する](#java-の-string-はアプリ内部で使用する文字の場合に使用する)
  - [Resource の strings.xml は、画面に表示する文字の場合に使用する](#resource-の-stringsxml-は画面に表示する文字の場合に使用する)
<!-- TOC END -->


# Java の String と Resource の string の違い

文字列を定義する際に Java の String で定義するのか、それとも、 Android の strings.xml  
で定義するのか迷うことがあるかもしれません。その際は、以下を参考にしてください。

## Java の String は、アプリ内部で使用する文字の場合に使用する

キー & バリュー 形式でデータを管理する場合の キー など、内部で使用する文字列については、  
Java の String を使用します。

もし、 Resource の strings.xml に定義してしまうと、結局のところ、 strings.xml を参照するための  
キーとなる文字列を Java の String で定義する必要が出てきて、二重に定義することになってしまいます。


## Resource の strings.xml は、画面に表示する文字の場合に使用する

Resource の strings.xml に文字列を定義すると、簡単に多言語対応することができるようになります。  
そのため、画面に表示する文字列については、特別な理由がない限り、 Resource の strings.xml  
を使用するのが良いでしょう。
