- [Channelでイベントを管理する](#channelでイベントを管理する)
  - [なぜ UI 状態で管理するとつらいのか](#なぜ-ui-状態で管理するとつらいのか)
    - [Snackbar を UI 状態に入れると起きる問題](#snackbar-を-ui-状態に入れると起きる問題)
  - [Channel（イベント）で管理すると何が良いか](#channelイベントで管理すると何が良いか)
  - [「SharedFlow じゃだめ？」という話](#sharedflow-じゃだめという話)


# Channelでイベントを管理する

スナックバーの表示のような「一度きりのイベント」は、UI 状態で管理するより `_eventChannel` （→ Flow 化）で管理するほうが適切です。


## なぜ UI 状態で管理するとつらいのか

UI 状態（StateFlow / UiState）は本質的に：

- 現在の状態を表す
- 再購読・再 Compose 時にも再通知される
- 最新値を常に保持する

という性質を持っています。


### Snackbar を UI 状態に入れると起きる問題

例えば：

```kotlin
data class UiState(
    val showSnackbar: Boolean = false
)
```

- 画面回転
- プロセス再生成
- collect 再開

→ もう一度 Snackbar が出る

これを避けるために：

- `shown = true` フラグを持つ
- consume したら state を更新する
- nullable にして null に戻す

といった **「イベント消費ロジック」**が UI 状態に混入します。


## Channel（イベント）で管理すると何が良いか

Channel は：

- 保持しない
- 流れたら終わり
- 再購読しても過去は流れない

```kotlin
private val _eventChannel = Channel<UiEvent>()
val events = _eventChannel.receiveAsFlow()

viewModelScope.launch {
    _eventChannel.send(UiEvent.ShowSnackbar("保存しました"))
}
```

UI 側：

```kotlin
LaunchedEffect(Unit) {
    viewModel.events.collect { event ->
        when (event) {
            is UiEvent.ShowSnackbar -> {
                snackbarHostState.showSnackbar(event.message)
            }
        }
    }
}
```

- 再 Compose されても 再表示されない
- UI 状態は 純粋な「状態」だけになる
- イベントの責務が明確


## 「SharedFlow じゃだめ？」という話

もちろん、

```kotlin
MutableSharedFlow<UiEvent>(
    replay = 0,
    extraBufferCapacity = 1
)
```

でも 同じ用途に使えます。

ただし、概念的には：

- Channel
  - イベントのパイプ
- SharedFlow
  - ブロードキャスト可能なイベントストリーム

なので、

- 1 箇所で消費される UI イベント
  - Channel（→ receiveAsFlow）
- 複数購読者があり得る
  - SharedFlow

と覚えておくと整理しやすいです。

