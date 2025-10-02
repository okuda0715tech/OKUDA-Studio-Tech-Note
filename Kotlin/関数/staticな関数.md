- [static な関数](#static-な関数)
  - [static なメソッドに変換される関数](#static-なメソッドに変換される関数)
  - [static なメソッドも生成される関数](#static-なメソッドも生成される関数)


# static な関数

## static なメソッドに変換される関数

以下の関数は、コンパイル時に Java の `static` メソッドに変換されます。

- パッケージレベル関数


## static なメソッドも生成される関数

以下の関数は、コンパイル時に Java の `static` メソッドも生成されます。  
(通常通り、インスタンスのメソッドも生成されます。)


- `companion object` 内の `@JvmStatic` アノテーションが付与された関数
- `object` 宣言内の `@JvmStatic` アノテーションが付与された関数



