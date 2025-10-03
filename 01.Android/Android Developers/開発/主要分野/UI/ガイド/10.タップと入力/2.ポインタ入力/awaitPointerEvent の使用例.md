- [awaitPointerEvent](#awaitpointerevent)
  - [サンプルコード](#サンプルコード)
  - [実行結果](#実行結果)


# awaitPointerEvent

以下のサンプルコードでは、タップした位置を画面に表示します。


## サンプルコード

```kotlin
@Composable
fun PointerEventExample() {
    var position by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        // ポインターイベントを待つ
                        val event = awaitPointerEvent()

                        // 現在のポインターの座標を取得
                        val pointerInputChange = event.changes
                            .firstOrNull()
                        pointerInputChange?.let {
                            position = it.position
                        }
                    }
                }
            }
    ) {
        BasicText(
            text = "Position: (${position.x}, ${position.y})",
        )
    }
}
```


## 実行結果

<img src="./画像/awaitPointerEvent サンプル.png" width="300">

