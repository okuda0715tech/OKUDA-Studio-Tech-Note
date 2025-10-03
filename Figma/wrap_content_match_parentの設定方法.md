- [wrap\_content / match\_parent の設定方法](#wrap_content--match_parent-の設定方法)
  - [wrap\_content の設定条件](#wrap_content-の設定条件)
  - [match\_parent の設定条件](#match_parent-の設定条件)


# wrap_content / match_parent の設定方法

## wrap_content の設定条件

Android でいう `wrap_content` を Figma で設定するには、  
以下の条件を満たす必要があります。

1. `wrap_content` を設定したいオブジェクトが Frame であること、もしくは、何らかの Frame の内部のオブジェクトであること
2. 項番 1 の Frame のオートレイアウトが有効になっていること


## match_parent の設定条件

`match_parent` を設定するには、 `wrap_content` の設定条件に加えて、  
以下の条件を満たす必要があります。

1. 親のオブジェクトが Frame であること
2. 親の Frame のオートレイアウトが有効になっていること



