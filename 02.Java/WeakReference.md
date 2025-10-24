# WeakReference

## 概要

WeakReferenceは、弱参照と呼ばれ、あるインスタンスへの参照が弱参照だけの場合、ガベージコレクション実行時にそのインスタンスが削除されます。

それに対して通常の参照は強参照と呼ばれ、強参照されているインスタンスはガベージコレクションが実行されてもインスタンスは削除されません。

WeakReferenceは、単独で使用することはなく、強参照と共に使用されます。


## 使い方

**SampleClass.java**

```Java
// セット
String s = "aaa";
private WeakReference<String> stringWeakRef;
stringWeakRef = new WeakReference<>(s);

// ゲット
stringWeakRef.get();
```


## SoftReferenceとの違い

SoftReferenceは、あるインスタンスへの参照がSoftReferenceだけになったからといって、必ずそのインスタンスがガベージコレクション時に削除されるとは限りません。


## 参考

富士通が作成した勉強用資料
　[java.lang.ref パッケージについて](https://www.fujitsu.com/jp/documents/products/software/resources/technical/interstage/apserver/guide/Java-Reference-Pacakge.pdf)
