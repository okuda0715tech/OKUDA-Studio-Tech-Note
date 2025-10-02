- [TextField のタップイベントをインターセプトする](#textfield-のタップイベントをインターセプトする)
  - [悪い例](#悪い例)
    - [1. Modifier.clickable{} を使用する](#1-modifierclickable-を使用する)
    - [2. Modifier.onFocasChanged{} を使用する](#2-modifieronfocaschanged-を使用する)
  - [良い例](#良い例)
    - [引用元資料](#引用元資料)


# TextField のタップイベントをインターセプトする

## 悪い例

### 1. Modifier.clickable{} を使用する

TextField の enabled パラメータを false に設定しないとイベントをキャッチできない。

false にしてしまうと、 TextField が全体的に灰色になってしまい、ユーザーにタップできない印象を与えてしまう。

その回避方法として、 Disabled 状態の色を変更する方法が stackoverflow に掲載されていた。しかし、その方法を試したところ、意味不明のエラーとなったため、その方法は有効ではない。


### 2. Modifier.onFocasChanged{} を使用する

onFocasChanged{} を使用した場合、フォーカスが当たっている場合にタップしても、フォーカスが変更しないため、イベントをキャッチできません。


## 良い例

```kotlin
@Composable
fun SelectTextField(
    value: String,
    onValueChange: (String) -> Unit,
    onClick: () -> Unit,
) {
    val interactionSource = remember {
        object : MutableInteractionSource {
            override val interactions = MutableSharedFlow<Interaction>(
                extraBufferCapacity = 16,
                onBufferOverflow = BufferOverflow.DROP_OLDEST,
            )

            override suspend fun emit(interaction: Interaction) {
                when (interaction) {
                    is PressInteraction.Press -> {
                        onClick()
                    }
                }

                interactions.emit(interaction)
            }

            override fun tryEmit(interaction: Interaction): Boolean {
                return interactions.tryEmit(interaction)
            }
        }
    }

    TextField(
        readOnly = true,
        interactionSource = interactionSource,
    )
}
```

### 引用元資料

- [【Jetpack Compose】OutlinedTextField 全体に対して onClick する方法](https://qiita.com/rnk0085/items/a017c6702fe27e86c918)



