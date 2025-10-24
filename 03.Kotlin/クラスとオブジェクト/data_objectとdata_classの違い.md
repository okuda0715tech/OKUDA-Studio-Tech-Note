- [data object と data class の違い](#data-object-と-data-class-の違い)
  - [copy() 関数の有無](#copy-関数の有無)
  - [componentN() 関数の有無](#componentn-関数の有無)


# data object と data class の違い

## copy() 関数の有無

`data object` は、シングルトンとして使用することが前提であるため、 `copy()` 関数は生成されません。


## componentN() 関数の有無

`data object` は、シングルトンであるため、基本的にはプロパティを持ちません。

そのため、この関数を生成していないそうです。




