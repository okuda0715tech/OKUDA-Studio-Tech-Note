- [derivedStateOf を使用すべき状況](#derivedstateof-を使用すべき状況)
  - [derivedStateOf の基本](#derivedstateof-の基本)
  - [よくある質問](#よくある質問)
    - [derivedStateOf は remember でラップする必要がありますか?](#derivedstateof-は-remember-でラップする必要がありますか)
    - [remember(key) と derivedStateOf の違いは何ですか?](#rememberkey-と-derivedstateof-の違いは何ですか)
    - [remember(key) と derivedStateOf を一緒に使用する必要があることはありますか? それはいつ必要ですか?](#rememberkey-と-derivedstateof-を一緒に使用する必要があることはありますか-それはいつ必要ですか)
    - [複数の状態を組み合わせるために derivedStateOf を使用する必要がありますか?](#複数の状態を組み合わせるために-derivedstateof-を使用する必要がありますか)
  - [結論](#結論)
  - [引用元資料](#引用元資料)


# derivedStateOf を使用すべき状況

## derivedStateOf の基本

[derivedStateOf](https://developer.android.com/reference/kotlin/androidx/compose/runtime/package-summary#derivedStateOf(kotlin.Function0)) — 本当によく聞かれる質問は、この API を使用する適切な場所とタイミングはどこかということです。

この質問に対する答えは、 **derivedStateOf {} は、UI を更新するよりも、状態、または、キーの変化が大きい場合に使用すべきだということです。** 言い換えると、 `derivedStateOf` は Kotlin Flows や、その他の同様のリアクティブ フレームワークの `distinctiveUntilChanged` のようなものです。 Composable は、読み取った Compose State オブジェクトが変更されると、再構成されることを覚えておいてください。 derivedStateOf を使用すると、必要な分だけ変更される新しい State オブジェクトを作成できます。

例を見てみましょう。ここには、ユーザー名フィールドと、ユーザー名が有効な場合に有効になるボタンがあります。

```kotlin
var username by remember { mutableStateOf("") }
val submitEnabled = isUsernameValid(username)
```

<img src="./画像/Initial state of username and submitEnabled.bin" width="800">

最初は空なので、状態は false です。ユーザーが入力を開始すると、状態が正しく更新され、ボタンが有効になります。

しかし、ここで問題があります。ユーザーが入力を続けると、ボタンに状態が何度も不必要に送信されてしまいます。

<img src="./画像/State updates after the user continues typing.bin" width="800">

ここで derivedStateOf が役立ちます。状態は UI の更新に必要な以上に変化しているため、derivedStateOf を使用して再構成の回数を減らすことができます。

```kotlin
var username by remember { mutableStateOf("") }
val submitEnabled = remember {
    // この remember {} ブロックは、 username の状態が変更されて、
    // 再コンポーズが発生しても呼ばれません。

    derivedStateOf {
        // この derivedStateOf {} ブロックは、 username の状態が変更されて、
        // 再コンポーズが発生すると呼ばれます。

        isUsernameValid(username)
    }
}
```

<img src="./画像/Updating the code to use derivedStateOf.webp" width="800">

違いを確認するために、同じ例をもう一度見てみましょう。

<img src="./画像/State updates with derivedStateOf.bin" width="800">

ユーザーは入力を開始しますが、今回はユーザー名の状態だけが変わります。送信状態は true のままです。そしてもちろん、ユーザー名が無効になった場合、派生状態は再び正しく更新されます。

さて、この例は少し単純化されすぎています。実際のアプリでは、入力パラメータが変更されていないため、Compose は送信コンポーザブルの再コンポジションをスキップする可能性が高くなります。

実際には、derivedStateOf が必要な状況は、ほとんどないように感じられるかもしれません。しかし、必要なケースが見つかった場合、再コンポジションを最小限に抑えるのに非常に効果的です。

**derivedStateOf が意味を成すためには、入力引数と出力結果の間の変化量に、差が必要であることを常に覚えておいてください。**

使用できる例をいくつか示します (網羅的ではありません):

- スクロールがしきい値を超えたかどうかを観察する (scrollPosition > 0)
- リスト内の項目が、しきい値より大きい (items > 0)
- 上記のようなフォーム検証 (username.isValid())


## よくある質問

次に、derivedStateOf に関するその他のよくある質問を見てみましょう。


### derivedStateOf は remember でラップする必要がありますか?

Composable 関数内にある場合は、ラップする必要があります。derivedStateOf は、mutableStateOf や、再構成後も存続する必要があるその他のオブジェクトと同じです。composable 関数内で使用する場合は、remember でラップする必要があります。そうしないと、再コンポーズのたびに、 Derived State が再構築 (初期化) されます ( derivedStateOf 関数が呼ばれます) 。

```kotlin
@Composable
fun MyComp() {
    // 再コンポーズ時に状態を保持するために remember が必要です。
    val state = remember { derivedStateOf { … } }
}

class MyViewModel: ViewModel() {
    // ViewModel はコンポジションの外なので、 remember は不要です。
    val state = derivedStateOf { … }
}
```

ただし、後述のセクション [remember(key) と derivedStateOf を一緒に使用する必要があることはありますか?](#rememberkey-と-derivedstateof-を一緒に使用する必要があることはありますか-それはいつ必要ですか) でも説明するように、 derivedStateOf {} ブロック内で、 State ではない変数を参照している場合は、その変数が変わるたびに Derived State を再構築する必要があります。その具体的な方法は、該当のセクションを参照して下さい。


### remember(key) と derivedStateOf の違いは何ですか?

remember(key) と derivedStateOf は、一見すると非常に似ているように見えます。

```kotlin
val result = remember(state1, state2) { calculation(state1, state2) }
val result = remember { derivedStateOf { calculation(state1, state2) } }
```

remember(key) と derivedStateOf は、そもそもの目的が異なります。

derivedStateOf {} は、その入力の状態が頻繁に変更されるが、その出力の状態の変更が、入力に比べて非常に少ない場合に使用します。これにより、不必要な再コンポーズが多発するのを防ぎます。

remember(key) は、 remember {} ブロックの再実行のために使用します。通常、 remember {} ブロックは、再コンポーズの際に、再実行されません。しかし、場合によっては、再コンポーズが必要な場合があるため ( [remember(key) と derivedStateOf を一緒に使用する必要があることはありますか?](#rememberkey-と-derivedstateof-を一緒に使用する必要があることはありますか-それはいつ必要ですか) セクションを参照) 、その場合に必要になります。

とえば、ユーザーが LazyColumn をスクロールした場合にのみ、ボタンを有効にするとします。

```kotlin
val isEnabled = lazyListState.firstVisibileItemIndex > 0
```

firstVisibleItemIndex は、ユーザーがスクロールすると 0、1、2 などに変更され、変更されるたびに、それを参照しているコンポーザブル関数が、再構成されます。0 より大きいかどうかだけが問題です。必要な入力と出力の量には違いがあるため、ここでは不要な再構成をバッファリングするために derivedStateOf が使用されます。

```kotlin
val isEnabled = remember {
    derivedStateOf { lazyListState.firstVisibleItemIndex > 0 }
}
```

ここで、パラメーターを使用して何かを計算する高価な関数があるとします。その関数の出力が変わるたびに UI が再構成されるようにしたいのです (重要なのは、関数もべき等であるということです)。ここでは、キーが変わるたびに UI を更新する必要があるため、キーとともに remember を使用します。つまり、入力と出力の量は同量です。

```kotlin
val output = remember(input) { expensiveCalculation(input) }
```


### remember(key) と derivedStateOf を一緒に使用する必要があることはありますか? それはいつ必要ですか?

ここで少しややこしくなります。 derivedStateOf は、 Compose State オブジェクトを読み取るときにのみ更新できます。 derivedStateOf 内で読み取られるその他の変数は、派生状態が作成されたときに、その変数の初期値を取得します。計算でこれらの変数を使用する必要がある場合は、それらを remember 関数のキーとして提供できます。この概念は、例を挙げると、はるかに理解しやすくなります。前の isEnabled の例を取り上げ、ボタンを有効にするタイミングのしきい値を 0 ではなく、変更できるように拡張してみましょう。

```kotlin
@Composable
fun ScrollToTopButton(lazyListState: LazyListState, threshold: Int) {
    // ここにはバグが含まれます。
    val isEnabled by remember {
        derivedStateOf { lazyListState.firstVisibleItemIndex > threshold }
    }
  
    Button(onClick = { }, enabled = isEnabled) {
        Text("Scroll to top")
    }
}
```

上記のコードには、リストがしきい値を超えてスクロールされたときに、有効になるボタンがあります。余分な再コンポーズを削除するために derivedStateOf を正しく使用していますが、微妙なバグがあります。

threshold パラメータが変更され、 ScrollToTopButton コンポーザブルが、再コンポーズされる場合について考えてみましょう。 remember のラムダ式内は、再コンポーズのため、再実行されません。そのため、 derivedStateOf 関数は呼び出されません。すると、 derivedStateOf 内の threshold には、 ScrollToTopButton に渡された新しい threshold が反映されません。

実際のコードの出力を見てみましょう。最初はすべてが正しく機能しています。

| scrollPosition | threshold | isEnabled ( scrollPosition > threshold ) |
| -------------- | --------- | ---------------------------------------- |
| 0              | 0         | false                                    |
| 1              | 0         | true                                     |
| 2              | 0         | true                                     |

しかし、その後、しきい値の新しい値 (5) が composable に渡されたとします。

| scrollPosition | threshold | isEnabled ( scrollPosition > threshold ) |
| -------------- | --------- | ---------------------------------------- |
| 2              | 5         | true (意図しない結果)                    |
| 3              | 5         | true (意図しない結果)                    |
| 4              | 5         | true (意図しない結果)                    |

scrollPosition がしきい値より小さい場合でも、isEnabled は true に設定されてしまいます。

正しい実装にするには、しきい値をキーとして追加します。これにより、しきい値が変更されるたびに derivedStateOf 状態が再初期化されます。

```kotlin
val isEnabled by remember(threshold) {
    derivedStateOf { lazyListState.firstVisibleItemIndex > threshold }
}
```

ここで、しきい値が変更されると、isEnabled 状態が正しく更新されることがわかります。

| scrollPosition | threshold | isEnabled ( scrollPosition > threshold ) |
| -------------- | --------- | ---------------------------------------- |
| 0              | 0         | false                                    |
| 2              | 0         | true                                     |
| 3              | 0         | true                                     |
| 4              | 5         | false                                    |
| 5              | 5         | false                                    |
| 6              | 5         | true                                     |


### 複数の状態を組み合わせるために derivedStateOf を使用する必要がありますか?

おそらく、そうではありません。複数の状態を組み合わせて結果を作成する場合、そのうちの 1 つが変更されるたびに、再コンポーズが行われるようにする必要があります。

たとえば、名と姓を入力してフルネームを表示するフォームを考えてみましょう。

```kotlin
var firstName by remember { mutableStateOf("") }
var lastName by remember { mutableStateOf("") }

// This derivedStateOf is redundant
val fullName = remember { derivedStateOf { "$firstName $lastName" } }
```

ここでは、出力が入力と同じくらい変化するため、derivedStateOf は何も意味のある処理はせず、わずかなオーバーヘッドを引き起こすだけです。derivedStateOf は非同期更新にも役立ちません。 [Compose State スナップショット システム](./【ブログ】Compose%20のスナップショットシステム.md) ( [引用元資料はこちら](https://blog.zachklipp.com/introduction-to-the-compose-snapshot-system/) ) がそれを個別に処理し、ここでの呼び出しは同期的です。

この場合、 Derived State オブジェクトはまったく必要ありません。

```kotlin
var firstName by remember { mutableStateOf("") }
var lastName by remember { mutableStateOf("") }

val fullName = "$firstName $lastName"
```

## 結論

まとめると、 derivedStateOf は、 UI を更新するよりも、状態、または、キーの変化が大きい場合に使用されることを覚えておいてください。言い換えると、 derivedStateOf の入力が変化しても、 derivedStateOf の出力 (状態) が変わらない可能性がある場合は、 derivedStateOf を使用すると効果があります。

逆に言うと、入力量と出力量に差がない場合は、使用する必要はありません。その場合に使用すると、逆に、わずかかもしれませんが、オーバーヘッドが増えます。


## 引用元資料

- [Jetpack Compose — When should I use derivedStateOf?](https://medium.com/androiddevelopers/jetpack-compose-when-should-i-use-derivedstateof-63ce7954c11b)



