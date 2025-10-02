<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [clone メソッドによるディープコピー](#clone-メソッドによるディープコピー)
  - [概要](#概要)
  - [サンプル](#サンプル)
  - [何かのサブクラスをクローンする場合](#何かのサブクラスをクローンする場合)
  - [使用上の注意点](#使用上の注意点)
    - [コンストラクタは呼ばれない。](#コンストラクタは呼ばれない)
  - [マーカーインターフェースとは](#マーカーインターフェースとは)
<!-- TOC END -->


# clone メソッドによるディープコピー

## 概要

`Object` クラスに定義されている `clone()` メソッドを使用すると、  
そのオブジェクトの新しいインスタンスが生成され、  
プリミティブ型のフィールドは、新しいインスタンスに値がコピーされます。  
参照型のフィールドは、既存のオブジェクトへの参照がコピーされます。  
**(デフォルトでは、参照型のフィールドは、シャローコピーです。)**

参照型のフィールドをディープコピーするには、そのオブジェクトにも同様に `clone()` メソッドを実装します。


## サンプル

```Java
// Cloneable インターフェースを実装してください。
// 実装しないと CloneNotSupportedException が発生します。
// ただし、 Cloneable インターフェースは中身が空なのでオーバーライドするメソッドはありません。
class Shop implements Cloneable {
  String name;
  Address address;

  public Shop(String name, Address address) {
    this.name = name;
    this.address = address;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setAddressState(String state) {
    this.address.setState(state);
  }

  // 【アクセス修飾子について】
  // もし protected になっている可能性があるため、必要に応じて public にしてください。
  @Override
  public Shop clone() throws CloneNotSupportedException {
    // super.clone() を呼び出すと、自分自身のインスタンスが新しく生成され、
    // プリミティブ型のフィールドは、新しいインスタンスに値がコピーされます。
    // 参照型のフィールドは、シャローコピーされます。
    Shop clone = (Shop) super.clone();
    // 参照型のフィールドは、別途、 clone() メソッドを呼び出すことで、ディープコピーを行います。
    clone.address = this.address.clone();
    return clone;
  }
}
```

```Java
class Address implements Cloneable {
  String state;

  public Address(String state) {
    this.state = state;
  }

  public String getState() {
    return this.state;
  }

  public void setState(String state) {
    this.state = state;
  }

  @Override
  public Address clone() throws CloneNotSupportedException {
    return (Address) super.clone();
  }
}
```

```Java
Address address = new Address("Tokyo");
Shop s1 = new Shop("Ace Coffee", address);
Shop s2 = s1.clone();
```

## 何かのサブクラスをクローンする場合

何かのサブクラスをクローンする場合は、スーパークラスのどこかで Cloneable インターフェース  
を実装していれば、 CloneNotSupportedException は発生しません。


## 使用上の注意点

### コンストラクタは呼ばれない。

`clone()` メソッドはコピーを行うだけであり、コンストラクタは呼びません。  
よって、インスタンス生成時に、フィールドのコピー以外の何か特別な処理が必要な場合は、  
`clone()` メソッド内で実装する必要があります。


## マーカーインターフェースとは

Cloneable インターフェースは単に、 「 clone によってコピーすることができる」 という印として  
使用されます。このようなインターフェースのことをマーカーインターフェースと言います。
