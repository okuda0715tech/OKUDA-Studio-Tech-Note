- [getIntrinsicWidth と getWidth の違い](#getintrinsicwidth-と-getwidth-の違い)
  - [説明](#説明)
  - [例](#例)


# getIntrinsicWidth と getWidth の違い

## 説明

Intrinsic には、 「本質的な」 とう意味があります。

`getIntrinsicWidth()` は、 res フォルダーに格納した Drawable リソースのそのままの幅を返します。  
一方で、 `getWidth()` は、 Drawable リソースを格納した ImageView の幅を返します。


## 例

例えば、 drawable フォルダーに `200*200` の画像配置し、それを `100*100` の ImageView に表示したとします。

この場合、 `getIntrinsicWidth()` は、 200 を返し、 `getWidth()` は、 100 を返します。

ここでは、単位は考慮していません。 `getIntrinsicWidth()` や `getWidth()` は px 単位で値を返すため、 ImageView や Drawable リソースが dp 指定されている場合は、 px に変換した後の値が返されます。


