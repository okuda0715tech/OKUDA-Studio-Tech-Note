<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [Annotation](#annotation)
  - [@Px](#px)
  - [API Level 関連](#api-level-関連)
    - [@RequiresApi(Build.VERSION\_CODES.TIRAMISU)](#requiresapibuildversion_codestiramisu)
    - [@TargetApi(Build.VERSION\_CODES.TIRAMISU)](#targetapibuildversion_codestiramisu)

<!-- /TOC -->


# Annotation

## @Px

ピクセルでの指定を意味する。


## API Level 関連

### @RequiresApi(Build.VERSION_CODES.TIRAMISU)

- アノテーションは関数に付与する。
- アノテーションが付与された関数は、指定した OS バージョンよりも高いバージョンでのみ実行される必要がある。
- もし、指定したバージョンよりも低いバージョンで対象の関数が呼び出されると赤色のワーニングが表示される。
- ワーニングであるため、コンパイルエラーは発生せず、低いバージョンの OS で実行されると実行時エラーとなる。


### @TargetApi(Build.VERSION_CODES.TIRAMISU)

- アノテーションは関数に付与する。
- `@RequiresApi()` アノテーションが登場する前に使われていた古いアノテーションです。今は、 @RequiresApi() を使用するのが一般的でしょう。
- `@TargetApi()` は、 @RequiresApi() とは違い、指定した API Level 以下の API Level で実行される可能性があっても、ワーニングを表示してくれません。単に、ワーニングを抑制するだけの働きをするため、あまり効果的なアノテーションではないでしょう。



