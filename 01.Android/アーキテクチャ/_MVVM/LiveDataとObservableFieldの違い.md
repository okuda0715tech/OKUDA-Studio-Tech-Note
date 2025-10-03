<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [LiveDataとObservableFieldの違い](#livedataとobservablefieldの違い)

<!-- /TOC -->


# LiveDataとObservableFieldの違い

1. 同じ値がセットされた場合に通知されるかされないか
  - ObservableFieldは、同じ値がセットされた場合は、Viewへの通知は行われない
  - LiveDataは、同じ値がセットされた場合は、Viewへの通知が行われる

