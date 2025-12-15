- [Flow と Channel の違い](#flow-と-channel-の違い)
  - [Channel の本質](#channel-の本質)
  - [Flow との決定的な違い](#flow-との決定的な違い)
    - [1. Channel は「プッシュ型」](#1-channel-はプッシュ型)
    - [2. Flow は「プル型」](#2-flow-はプル型)
  - [Channel が向いている用途](#channel-が向いている用途)
  - [Android / ViewModel 観点での整理](#android--viewmodel-観点での整理)
  - [まとめ](#まとめ)


# Flow と Channel の違い

## Channel の本質

Channel は、

- 複数のコルーチン間で
- 値を逐次的に送受信する
- キュー（パイプ）

です。

イメージとしては：

- send() → キューに値を入れる
- receive() → キューから値を取り出す

という **「コルーチン対応のスレッドセーフなキュー」**です。


## Flow との決定的な違い

### 1. Channel は「プッシュ型」

- 送信側が主導
- 送られた瞬間に、受信側がいれば即届く
- 受信側がいなければ、バッファに溜まる（設定次第）

```kotlin
channel.send(value)
```


### 2. Flow は「プル型」

- collect した側が主導
- collect されない限り処理は走らない
- 状態やストリームの宣言的表現

```kotlin
flow.collect { value -> ... }
```


## Channel が向いている用途

Channel は「状態」ではなく イベントに向いています。

典型例：

- ワンショットイベント
  - Toast
  - Snackbar
  - 画面遷移指示
- 仕事の分配
  - ワーカーコルーチンにタスクを流す
- Producer / Consumer パターン

```kotlin
val channel = Channel<Event>()

// 送信側
viewModelScope.launch {
    channel.send(Event.ShowToast)
}

// 受信側
viewModelScope.launch {
    for (event in channel) {
        handle(event)
    }
}
```


## Android / ViewModel 観点での整理

こう割り切るのが一番きれいです。

- UI 状態
  - StateFlow
- Repository からのデータストリーム
  - Flow
- 一度きりのイベント
  - Channel → receiveAsFlow()

```kotlin
private val _eventChannel = Channel<UiEvent>()
val events = _eventChannel.receiveAsFlow()
```

「Channel を直接 UI が receive する」

のは避けて、Flow に変換して使うのが今の Android ではほぼ定石です。


## まとめ

- Channel
  - コルーチン間の命令的なデータ受け渡し
- Flow
  - 時間とともに変化する値の宣言的な表現

