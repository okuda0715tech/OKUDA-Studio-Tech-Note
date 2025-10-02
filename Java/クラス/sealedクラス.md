- [sealed クラス](#sealed-クラス)
  - [概要](#概要)
  - [基本形](#基本形)


# sealed クラス

## 概要

`sealed` クラスは、継承できるクラスを限定する機能です。  
`sealed` は `interface` でも使えます。


## 基本形

```Java
public abstract sealed class Shape
    permits Circle, Rectangle, Square {...}
```

上記のようにすると Shape クラスを継承できるのは Circle 、 Rectangle 、 Square に限定されます。  
Circle、Rectangle、Squareは必ず定義する必要があります。




