- [Compose のスナップショットシステム](#compose-のスナップショットシステム)
  - [Compose の状態](#compose-の状態)
  - [「スナップショット」](#スナップショット)
    - [例に関する注意](#例に関する注意)
  - [スナップショットの「復元」](#スナップショットの復元)
  - [Mutable スナップショット](#mutable-スナップショット)
  - [読み取りと書き込みの追跡](#読み取りと書き込みの追跡)
  - [SnapshotStateObserver](#snapshotstateobserver)
  - [ネストされたスナップショット](#ネストされたスナップショット)
  - [グローバル スナップショット](#グローバル-スナップショット)
  - [マルチスレッドとグローバル スナップショット](#マルチスレッドとグローバル-スナップショット)
  - [競合するスナップショット書き込み](#競合するスナップショット書き込み)
  - [結論](#結論)
  - [参考資料](#参考資料)
  - [引用元資料](#引用元資料)


# Compose のスナップショットシステム

Jetpack Compose では、観測可能な状態を処理する新しい方法が導入されています。Android でのリアクティブ プログラミングに関する歴史的背景の紹介については、この投稿の前編である [Compose リアクティブ状態モデルの歴史的紹介](https://blog.zachklipp.com/a-historical-introduction-to-the-compose-reactive-state-model/) をご覧ください。この投稿では、低レベルのスナップショット API について説明します。ここにある内容の多くは、かなり深い学術的 / コンピューター サイエンスのルーツを持っていますが、これは論文ではないので、より実用的な観点から取り上げます。将来的には、スナップショット メカニズムの内部実装について取り上げる別の投稿を行う可能性があります。これはさらに複雑で、おそらくこの投稿に収まるには、内容が多すぎるでしょう。それまでは、その情報については、私の講演 [「スナップショットのシャッターを開く」](https://blog.zachklipp.com/talk-opening-the-shutter-on-snapshots/) をご覧ください。

📖この記事は、Compose 状態に関するシリーズの一部です。他の記事は [こちら](https://blog.zachklipp.com/tag/compose-state-series/) をご覧ください。


## Compose の状態

Compose では、コンポーザブル関数によって読み取られるすべての状態は、次のような関数によって返される、特別な状態オブジェクトに、よってサポートされる必要があります。

- [mutableStateOf](https://developer.android.com/reference/kotlin/androidx/compose/runtime/package-summary?ref=blog.zachklipp.com#mutableStateOf(kotlin.Any,androidx.compose.runtime.SnapshotMutationPolicy)) / [MutableState](https://developer.android.com/reference/kotlin/androidx/compose/runtime/MutableState?ref=blog.zachklipp.com)
- [mutableStateListOf](https://developer.android.com/reference/kotlin/androidx/compose/runtime/package-summary?ref=blog.zachklipp.com#mutableStateListOf()) / [SnapshotStateList](https://developer.android.com/reference/kotlin/androidx/compose/runtime/snapshots/SnapshotStateList?ref=blog.zachklipp.com)
- [mutableStateMapOf](https://developer.android.com/reference/kotlin/androidx/compose/runtime/package-summary?ref=blog.zachklipp.com#mutableStateMapOf()) / [SnapshotStateMap](https://developer.android.com/reference/kotlin/androidx/compose/runtime/snapshots/SnapshotStateMap?ref=blog.zachklipp.com)
- [derivedStateOf](https://developer.android.com/reference/kotlin/androidx/compose/runtime/package-summary?ref=blog.zachklipp.com#derivedStateOf(kotlin.Function0))
- [rememberUpdatedState](https://developer.android.com/reference/kotlin/androidx/compose/runtime/package-summary?ref=blog.zachklipp.com#rememberUpdatedState(kotlin.Any))
- collect*AsState

基本的に、 [`State<T>`](https://developer.android.com/reference/kotlin/androidx/compose/runtime/State?ref=blog.zachklipp.com) インターフェース ( `MutableState<T>` を含む) 、または、 [StateObject](https://developer.android.com/reference/kotlin/androidx/compose/runtime/snapshots/StateObject?ref=blog.zachklipp.com) インターフェース (よく調べれば、MutableState の組み込み実装も実際にこれを実装しています) を実装するものすべてです。remember について言及しなかった理由がわからない場合は、数分かけて違いを説明している [この投稿](https://blog.zachklipp.com/remember-mutablestateof-a-cheat-sheet/) をお読みください。

スナップショット状態オブジェクトを使用して状態を保持する主な理由、または、少なくとも Compose を学習するときに最初に遭遇する理由は、状態が変化するとコンポーザブルが自動的に更新されるためです。同じくらい重要でありながら、あまり議論されていないもう 1 つの理由は、異なるスレッド (たとえば、LaunchedEffects) からの構成可能な状態への変更が適切に分離され、競合状態なしで安全に実行できるようにするためです。この 2 つの理由は関連していることがわかりました。

用語に関する注意: MutableStates のようなものを指すために、「スナップショット状態」または「状態値」という用語を使用します。「スナップショット状態」という用語は、Compose のソースとドキュメントに登場します。その理由は、この投稿の最後までに明らかになるでしょう。

以下で紹介する API のほとんどは、日常的なコードで使用することを意図したものではないことに注意してください。Compose を使用するために、これらの API について知る必要すらありません。これらの API の多くは、簡単に間違えてトラブルに巻き込まれます。たとえば、多くの API では、さまざまなハンドルを明示的に破棄する必要があります。これらは、snapshotFlow 関数や、Compose ランタイムが再コンポジションをトリガーするために使用するグルー コードなど、上位レベルの概念を構築するのに便利ですが、アプリを構築するときにこれらが必要になることはほとんどありません。それでも、これらの低レベルのビルディング ブロックがどのように機能するかを確認しておくと、上位レベルのビルディング ブロックについて理解できるようになり、必要になったときや読みたいと思ったときに Compose ソースを読む準備が整います。


## 「スナップショット」

Compose は、Snapshot と呼ばれる型と、それを操作するための一連の API を定義します。Snapshot は、ビデオ ゲームのセーブ ポイントによく似ています。つまり、履歴の 1 つの時点でのプログラム全体の状態を表します。まあ、これは完全に正しいわけではありません。スナップショットは、取得時にプログラムに存在していたすべてのスナップショット状態値の状態を表します。したがって、説明を簡単にするために、すべての状態がスナップショット状態にあると仮定しましょう。どのコードでもスナップショットを取得できます。これらはパブリック API です。

説明をわかりやすくするために、単純な Dog クラスと、それに対していくつかの操作を実行するメイン関数の例を紹介します。Dog は、名前が MutableState オブジェクトに格納されるため、スナップショットをサポートします。

```kotlin
class Dog {
    var name: MutableState<String> = mutableStateOf(“”)
}

fun main() {
    val dog = Dog()
    dog.name.value = “Spot”
    // スナップショット (セーブ) する
    val snapshot = Snapshot.takeSnapshot()
    dog.name.value = “Fido”

    // スナップショットの使用が終了したら、必ず破棄する必要があります。
    // 以降の例では簡潔にするためにこの手順を省略していますが、
    // このコードのいずれかをコピーして貼り付ける場合は忘れずに実行してください。
    // dispose は、処分するという意味の英単語です。
    snapshot.dispose()
}
```

### 例に関する注意

コードをより明確にするため、例のコードでは、プロパティ委譲 (by 構文) なしで、明示的な MutableState プロパティを使用します。

例のコンテンツのほとんどはメイン関数にあります。自分で実行する場合は、コードをコピーして JUnit テスト関数に貼り付けると、IDE から簡単に実行できます。ただし、スナップショット システムは Compose の一部として出荷されていますが、他の Compose 機能とは独立しており、他の機能を必要としないため、理論的には通常のコマンド ライン プログラムとして実行できるはずです。


## スナップショットの「復元」

復元できないセーブ ポイントは役に立ちません。同様に、スナップショットは、 (ある程度) 復元できます。スナップショットを復元しても、スナップショットの値で、最新の状態が上書きされるわけではありません。スナップショットが復元されるスコープ (ラムダ) を指定します。スコープ内では、すべての状態値は、スナップショットされた値になります。

犬の名前を変更して、スナップショットから最初の名前を読み取ってみましょう。

```kotlin
fun main() {
    val dog = Dog()
    dog.name.value = “Spot”
    val snapshot = Snapshot.takeSnapshot()
    dog.name.value = “Fido”

    println(dog.name.value)
    // enter 関数のラムダブロックがスコープとなります。
    snapshot.enter {
        // スナップショットが有効なスコープ
        println(dog.name.value)
    }
    println(dog.name.value)
}
```

```
// Output:

Fido
Spot
Fido
```

これがあまり印象的でないと思われる場合は、takeSnapshot() は、作成場所や設定場所に関係なく、プログラム内のすべての状態値のスナップショットを取得することを思い出してください。enter 関数は、呼び出しスタックが深い場合でも、関数内のすべてのコードにスナップショット状態を一時的に復元します。

```kotlin
fun main() {
    // …
    snapshot.enter { reminisce(dog) }
}

private fun reminisce(dog: Dog) {
    printName(dog.name)
}

// MutableState<T> extends the interface State<T>
private fun printName(name: State<String>) {
    println(name.value)
}
```

これまでのところ、スナップショットされたデータを読み取っただけです。実際のプログラムはデータを変更しますが、それをどのように行うのでしょうか?


## Mutable スナップショット

enter ブロック内で犬の名前を変更してみましょう。

```kotlin
fun main() {
  val dog = Dog()
  dog.name.value = "Spot"

  val snapshot = Snapshot.takeSnapshot()

  println(dog.name.value)
  snapshot.enter {
    println(dog.name.value)
    dog.name.value = "Fido"
    println(dog.name.value)
  }
  println(dog.name.value)
}
```

```
// Output:

Spot

java.lang.IllegalStateException: Cannot modify a state object in a read-only snapshot
```

takeSnapshot() で作成されたスナップショットは読み取り専用です。enter ブロック内では、スナップショットされた値を読み取ることはできますが、書き込むことはできません。可変スナップショットを作成するには、別の関数 takeMutableSnapshot() が必要です。この関数は、MutableSnapshot のインスタンスを返します。これは Snapshot と全く同じですが、追加の機能があります。enter メソッドもありますが、enter メソッドは状態値を変更できます。

クラッシュを修正しましょう。

```kotlin
fun main() {
    val dog = Dog()
    dog.name.value = "Spot"

    val snapshot = Snapshot.takeMutableSnapshot()
    println(dog.name.value)
    snapshot.enter {
        dog.name.value = "Fido"
        println(dog.name.value)
    }
    println(dog.name.value)
}
```

```
// Output:

Spot
Fido
Spot 
```

クラッシュは発生しません。しかし、ちょっと待ってください。Enter ブロックで名前を変更した後でも、Enter が戻るとすぐに変更が元に戻ってしまいます。スナップショットを使用すると、過去を覗くことができるだけでなく、変更を分離して他のものに影響しないようにすることもできます。しかし、それらの変更を「保存」したい場合はどうすればよいでしょうか。スナップショットを「適用」する必要があります。

```kotlin
fun main() {
    val dog = Dog()
    dog.name.value = "Spot"

    val snapshot = Snapshot.takeMutableSnapshot()
    println(dog.name.value)
    snapshot.enter {
        dog.name.value = "Fido"
        println(dog.name.value)
    }
    println(dog.name.value)
    snapshot.apply()
    println(dog.name.value)
}
```

```
// Output:

Spot
Fido
Spot
Fido 
```

できました! apply() を呼び出すと、新しい値が Enter ブロックの外側に表示されます。 MutableSnapshot を取得し、それに対して関数を実行して、適用するというパターンは非常に一般的なので、定型句を処理するヘルパー関数 [Snapshot.withMutableSnapshot()](https://developer.android.com/reference/kotlin/androidx/compose/runtime/snapshots/Snapshot.Companion?ref=blog.zachklipp.com#withMutableSnapshot(kotlin.Function0)) があります。上記の例は次のように簡略化できます。

```kotlin
fun main() {
    val dog = Dog()
    dog.name.value = "Spot"

    Snapshot.withMutableSnapshot {
        println(dog.name.value) // Spot
        dog.name.value = "Fido"
        println(dog.name.value) // Fido
    }
    println(dog.name.value) // Fido
}
```

まとめましょう。これまでのところ、次のことができます。

- すべての状態のスナップショットを取得し、
- 特定のコード ブロックのスナップショットを「復元」し、
- 状態の値を変更します。

ただし、実際に変更を観察する方法はまだ説明していません。次に、それを理解しましょう。


## 読み取りと書き込みの追跡

オブザーバー パターンを使用して状態の変更を観察するには、2 つの部分から成るプロセスが必要です。まず、状態に依存するコードの部分が変更リスナーを登録します。次に、値が変更されると、登録されているすべてのリスナーに通知されます。オブザーバーを手動で登録し、登録解除することを忘れないようにすると、エラーが発生しやすくなります。Compose 状態は、明示的な関数呼び出しなしで、状態の読み取り時にオブザーバーを登録できるようにすることで、この点に役立ちます。たとえば、状態を読み取るコードがコンポーズ可能な関数である場合、Compose ランタイムは関数を再構成して更新します。

takeMutableSnapshot() 関数には、実際にはオプションのパラメーターがいくつかあります。 「読み取りオブザーバー」と「書き込みオブザーバー」が必要です。どちらも単純な (Any) -> Unit 関数で、それぞれ、enter ブロック内でスナップショットの状態値が読み取られたり書き込まれたりするときに呼び出されます。これらの値で何をするかは、実際にはあなた次第ですが、値は文字通り何でもかまわないので、実際には多くのことはできません。実行できることの 1 つは、スナップショットから読み取られたすべての値を何らかのデータ構造で追跡し、将来変更リスナーを登録して、変更された値が以前に読み取った値であるかどうかを判断できるようにすることです。ただし、Compose には、これを行うヘルパー クラスが含まれています (以下を参照)。

犬の名前がアクセスされるかどうかを確認しましょう。

```kotlin
fun main() {
    val dog = Dog()
    dog.name.value = "Spot"

    val readObserver: (Any) -> Unit = { readState ->
        if (readState == dog.name) println("dog name was read")
    }
    val writeObserver: (Any) -> Unit = { writtenState ->
        if (writtenState == dog.name) println("dog name was written")
    }

    val snapshot = Snapshot.takeMutableSnapshot(readObserver, writeObserver)
    println("name before snapshot: " + dog.name.value)
    snapshot.enter {
        dog.name.value = "Fido"
        println("name before applying: ")
        // This could be inlined, but I've separated the actual state read
        // from the print statement to make the output sequence more clear.
        val name = dog.name.value
        println(name)
    }
    snapshot.apply()
    println("name after applying: " + dog.name.value)
}
```

```
// Output:

name before snapshot: Spot
dog name was written
name before applying: 
dog name was read
Fido
name after applying: Fido
```

dog.name プロパティに格納されている MutableState インスタンスが、読み取りおよび書き込みオブザーバーに渡されることがわかります。値にアクセスされると、オブザーバーもすぐに呼び出されます。上記の出力では、実際の名前の前に「dog name was read」が出力されていることに注意してください。

スナップショットのもう 1 つの利点は、コール スタックのどの深さで発生しても、状態の読み取りが監視されることです。つまり、状態を読み取るコードを関数またはプロパティに分解でき、それらの読み取りは引き続き追跡されます。State 型のプロパティ デリゲート拡張はこれに依存しています。これを証明するために、例に間接参照を追加して、読み取りが引き続き報告されることを確認します。

```kotlin
fun Dog.getActualName() = nameValue
val Dog.nameValue get() = name.value

fun main() {
    val dog = Dog()
    dog.name.value = "Spot"

    val readObserver: (Any) -> Unit = { readState ->
        if (readState == dog.name) println("dog name was read")
    }

    val snapshot = Snapshot.takeSnapshot(readObserver)
    snapshot.enter {
        println("reading dog name")
        val name = dog.getActualName()
        println(name)
    }
}
```

```
// Output:

reading dog name
dog name was read
Spot
```

状態の変更は重複が排除されます。状態値が同じ値に設定されている場合、その値の変更は記録されません。どの値が「等しい」と見なされるかを定義するロジックは、実際にはポリシーを介してカスタマイズできます。これについては、この投稿の後半で競合するスナップショット書き込みを調べるときに説明します。


## SnapshotStateObserver

特定の関数のすべての読み取りを追跡し、それらの状態値のいずれかが変更されたときにコールバックを実行するというパターンは非常に一般的なので、それを実装するクラスがあります。 [SnapshotStateObserver](https://developer.android.com/reference/kotlin/androidx/compose/runtime/snapshots/SnapshotStateObserver?ref=blog.zachklipp.com) です。このクラスの使用方法は次のようになります。

1. SnapshotStateObserver を作成し、変更通知コールバックを実行する Java スタイルの Executor として機能する関数を渡します。
2. start() を呼び出して変更の監視を開始します。
3. observeReads() を 1 回以上呼び出し、読み取りを監視する関数と、読み取り値のいずれかが変更されたときに実行するコールバックを渡します。コールバックが呼び出されるたびに、監視されている状態のセットがクリアされ、変更の追跡を継続するには observeReads() を再度呼び出す必要があります。
4. 変更の監視を停止し、監視に必要なリソースを解放するには、stop() と clear() を呼び出します。
これは少し長い例ですが、SnapshotStateObserver を使用して犬の名前の状態変更を監視する方法を示しています。

```kotlin
fun main() {
    val dog = Dog()

    fun immediateExecutor(runnable: () -> Unit) {
        runnable()
    }

    fun blockToObserve() {
        println("dog name: ${dog.name.value}")
    }

    val observer = SnapshotStateObserver(::immediateExecutor)

    fun onChanged(scope: Int) {
        println("something was changed from pass $scope")
        println("performing next read pass")
        observer.observeReads(
            scope = scope + 1,
            onValueChangedForScope = ::onChanged,
            block = ::blockToObserve
        )
    }

    dog.name.value = "Spot"

    println("performing initial read pass")
    observer.observeReads(
        // This can be literally any object, it doesn't need
        // to be an int. This example just uses an int to
        // demonstrate subsequent read passes.
        scope = 0,
        onValueChangedForScope = ::onChanged,
        block = ::blockToObserve
    )

    println("starting observation")
    observer.start()

    println("initial state change")
    Snapshot.withMutableSnapshot {
        dog.name.value = "Fido"
    }

    println("second state change")
    Snapshot.withMutableSnapshot {
        dog.name.value = "Fluffy"
    }

    println("stopping")
    observer.stop()

    println("third state change")
    Snapshot.withMutableSnapshot {
        // This change won't trigger the callback.
        dog.name.value = "Fluffy"
    }
}
```

```
// Output:

performing initial read pass
dog name: Spot
starting observation
initial state change
something was changed from pass 0
performing next read pass
dog name: Fido
second state change
something was changed from pass 1
performing next read pass
dog name: Fluffy
stopping
third state change
```

SnapshotStateObserver と再起動可能な関数全般の詳細については、 [「Restartable functions from first principle」](https://blog.zachklipp.com/restartable-functions-from-first-principles/) を参照してください。

ただし、まだ重要なコンポーネントが 1 つ欠けています。スナップショットの状態値は、スナップショット内だけでなく、どこでも変更できます。最初のサンプルを思い出してください。メイン関数で直接名前を変更できるため、それらの「トップレベル」の書き込みを監視する方法があるはずですよね?


## ネストされたスナップショット

実は、すべてのコードは、その周囲に明示的に作成されたスナップショットがない場合でも、実際にはスナップショット内で実行されます。スナップショットには、まだ説明していないもう 1 つの機能があります。ネストです。別のスナップショットの Enter ブロック内で takeMutableSnapshot() を呼び出すと、内側のスナップショットが適用されると、外側のスナップショットにのみ適用されます。次に、外側のスナップショットを順番に適用して、変更がスナップショット階層を上方に伝播し続け、ツリーを上方に伝播して、ルート スナップショットに変更が適用されるまで続きます。このルート スナップショットは「グローバル スナップショット」と呼ばれ、常に開いています。グローバル スナップショットは少し特殊なケースなので、それについて説明する前に、スナップショットのネストについて見てみましょう。

innerSnapshot というネストされたスナップショットを作成し、dog の名前を変更して何が起こるか見てみましょう。

```kotlin
Fun main() {
    val dog = Dog()
    dog.name.value = "Spot"

    val outerSnapshot = Snapshot.takeMutableSnapshot()
    println("initial name: " + dog.name.value)
    outerSnapshot.enter {
        dog.name.value = "Fido"
        println("outer snapshot: " + dog.name.value)

        val innerSnapshot = Snapshot.takeMutableSnapshot()
        innerSnapshot.enter {
            dog.name.value = "Fluffy"
            println("inner snapshot: " + dog.name.value)
        }
        println("before applying inner: " + dog.name.value)
        innerSnapshot.apply().check()
        println("after applying inner: " + dog.name.value)
    }
    println("before applying outer: " + dog.name.value)
    outerSnapshot.apply().check()
    println("after applying outer: " + dog.name.value)
}
```

```
// Output:

initial name: Spot
outer snapshot: Fido
inner snapshot: Fluffy
before applying inner: Fido
after applying inner: Fluffy
before applying outer: Spot
after applying outer: Fluffy
```

最後の 2 行に注目してください。「Fluffy」への名前変更は、外部スナップショットを適用するまで最上位の値には適用されませんでした。スナップショットの適用を状態変更操作と考えると、これは理にかなっています。スナップショット内の状態変更は apply() が呼び出されるまで適用されないため、ネストされたスナップショットの適用は、外部スナップショットが適用されるまで表示されない別の種類の変更操作にすぎません。


## グローバル スナップショット

グローバル スナップショットは、スナップショット ツリーのルートにある変更可能なスナップショットです。適用しないと有効にならない通常の変更可能なスナップショットとは対照的に、グローバル スナップショットには「適用」操作がありません。適用する対象がありません。代わりに、「進める」ことができます。グローバル スナップショットを進めることは、アトミックに適用してすぐに再度開くことに似ています。前回の進め以降にグローバル スナップショットで変更されたすべてのスナップショット状態値の変更通知が送信されます。

グローバル スナップショットを進めるには、次の 3 つの方法があります。

1. 変更可能なスナップショットを適用する。上記の例では、outerSnapshot が適用されると、グローバル スナップショットに適用され、グローバル スナップショットが進められます。
2. [Snapshot.notifyObjectsInitialized](https://developer.android.com/reference/kotlin/androidx/compose/runtime/snapshots/Snapshot.Companion?ref=blog.zachklipp.com#notifyObjectsInitialized()) を呼び出します。これにより、前回の進め以降に変更された状態値の変更通知が送信されます。
3. [Snapshot.sendApplyNotifications()](https://developer.android.com/reference/kotlin/androidx/compose/runtime/snapshots/Snapshot.Companion?ref=blog.zachklipp.com#sendApplyNotifications()) を呼び出します。これは、notifyObjectsInitialized に似ていますが、実際に変更があった場合にのみスナップショットを進めます。この関数は、最初のケースで、変更可能なスナップショットがグローバル スナップショットに適用されるたびに暗黙的に呼び出されます。

グローバル スナップショットは takeMutableSnapshot 呼び出しによって作成されないため、通常のように読み取りおよび書き込みオブザーバーを渡すことはできません。代わりに、グローバル スナップショットから状態の変更が適用されたときにコールバックをインストールする専用の関数があります: [Snapshot.registerApplyObserver()](https://developer.android.com/reference/kotlin/androidx/compose/runtime/snapshots/Snapshot.Companion?ref=blog.zachklipp.com#registerApplyObserver(kotlin.Function2)) 。

明示的なスナップショットなしで、registerApplyObserver と sendApplyNotifications を使用して犬の名前を変更してみましょう。

```kotlin
fun main() {
    val dog = Dog()
    Snapshot.registerApplyObserver { changedSet, snapshot ->
        if (dog.name in changedSet) println("dog name was changed")
    }

    println("before setting name")
    dog.name.value = "Spot"
    println("after setting name")

    println("before sending apply notifications")
    Snapshot.sendApplyNotifications()
    println("after sending apply notifications")
}
```

```
// Output:

before setting name
after setting name
before sending apply notifications
dog name was changed
after sending apply notifications
```

Compose ランタイムは、グローバル スナップショット API を使用して、UI の描画を担当するフレームとスナップショットを調整します。コンポジションの外部でスナップショットの状態値が変更されると (たとえば、クリックなどのユーザー イベントによって、状態を変更するイベント ハンドラーが起動される)、Compose ランタイムは、次のフレームが描画される前に sendApplyNotifications 呼び出しが行われるようにスケジュールします。フレームを生成するとき、sendApplyNotifications 呼び出しによってグローバル スナップショットが進められ、それに加えられた変更が適用されます。グローバル スナップショットから送信された変更通知は、どのコンポーザブルを再コンポーズする必要があるかを判断するために使用されます。次に、Compose は変更可能なスナップショットを取得し、そのスナップショット内でそれらのコンポーザブルを再コンポーズして、最後にスナップショットを適用します。そのスナップショット アプリケーションによって、グローバル スナップショットが再度進められ、再コンポーズ中に行われた状態変更が、グローバル スナップショットで実行されているコード (他のスレッドで実行されているコードを含む) に表示されるようになります。


## マルチスレッドとグローバル スナップショット

グローバル スナップショットは、マルチスレッド コードにとって重要な意味を持ちます。明示的なスナップショットを使用せずに、バックグラウンド スレッドでコードを実行することは珍しくありません。IO ディスパッチャの LaunchedEffect から実行され、応答で MutableState を更新するネットワーク呼び出しについて考えてみましょう。このコードは、バックグラウンド スレッドでスナップショットなしでスナップショット状態の値を変更します (覚えておいてください: Compose はスナップショットでコンポジションをラップするだけで、エフェクトはコンポジションの外部で実行されます)。スナップショットがなければ、これは危険です。他のスレッドでその状態を読み取るコードは新しい値をすぐに確認し、値が間違ったタイミングで変更されると競合状態が発生する可能性があります。ただし、スナップショットには [「分離」](https://en.wikipedia.org/wiki/Snapshot_isolation?ref=blog.zachklipp.com) と呼ばれるプロパティがあります。

特定のスレッドのスナップショット内では、そのスナップショットが適用されるまで、他のスレッドから状態値に加えられた変更は表示されません。スナップショットは他のスナップショットから「分離」されています。コードが何らかの状態を操作する必要があり、その間に他のスレッドがその状態を操作できないようにする必要がある場合、通常、そのコードはミューテックスのようなものを使用して状態へのアクセスを保護します。ただし、スナップショットは分離されているため、代わりにスナップショットを取得して、スナップショット内の状態を操作することができます。その後、他のスレッドが状態を変更した場合、スナップショットを持つスレッドは、スナップショットが適用されるまで変更に気付かず、その逆も同様です。スナップショット内の状態に加えられた変更は、スナップショットが適用され、グローバル スナップショットが自動的に進められるまで、他のスレッドには表示されません。

しかし、明示的なスナップショットなしで他のスレッドで実行されているコードはどうでしょうか。そのコードは、他のスレッドがスナップショットを適用すると決定した場合、その変更をすぐに認識します。データベースの用語では、スナップショット システムは一貫性と可用性をトレードオフします。一貫性が必要なコードはスナップショットを取得する必要がありますが、ほとんどの場合、スナップショットは無視できます。


## 競合するスナップショット書き込み

これまでの例では、スナップショットを 1 つだけ取得していましたが、いくつか取得できない理由はありません。結局のところ、Compose がスナップショットを使用して複数のスレッドでコードを並列実行する場合、複数のスナップショットが必要になります。

変更可能なものをいくつか取り、同時に名前を変更してみましょう (すべてを 1 つのスレッドで実行していますが、スナップショット操作と状態の変更はインターリーブされています)。

```kotlin
fun main() {
    val dog = Dog()
    dog.name.value = "Spot"

    val snapshot1 = Snapshot.takeMutableSnapshot()
    val snapshot2 = Snapshot.takeMutableSnapshot()

    println(dog.name.value)
    snapshot1.enter {
        dog.name.value = "Fido"
        println("in snapshot1: " + dog.name.value)
    }
    // Don’t apply it yet, let’s try setting a third value first.

    println(dog.name.value)
    snapshot2.enter {
        dog.name.value = "Fluffy"
        println("in snapshot2: " + dog.name.value)
    }

    // Ok now we can apply both.
    println("before applying: " + dog.name.value)
    snapshot1.apply()
    println("after applying 1: " + dog.name.value)
    snapshot2.apply()
    println("after applying 2: " + dog.name.value)
}
```

```
// Output:

Spot
in snapshot1: Fido
Spot
in snapshot2: Fluffy
before applying: Spot
after applying 1: Fido
after applying 2: Fido
```

両方の Enter ブロックは内部的に変更された値を確認し、最初のスナップショットは変更を正常に適用しましたが、2 番目のスナップショットを適用した後、名前は「Fluffy」ではなく「Fido」のままでした。ここで何が起こっているかを理解するために、apply() メソッドを詳しく見てみましょう。実際には、SnapshotApplyResult 型の値を返します。これは、Success または Failure のいずれかになるシールされたクラスです。apply() 呼び出しの周囲に print ステートメントを追加すると、最初の呼び出しは成功しますが、2 番目の呼び出しは失敗します。その理由は、更新の間に解決できない競合があるためです。つまり、両方のスナップショットが同じ初期値 (「Spot」) に基づいて同じ名前の値を変更しようとしています。2 番目のスナップショットは「Spot」という名前で実行されたため、スナップショット システムは新しい値「Fluffy」がまだ正しいと想定できません。新しいスナップショットを適用した後で Enter ブロックを再実行するか、名前をマージする方法を明示的に指示する必要があります。これは、競合のある git ブランチをマージしようとしているときと同じ状況です。競合を解決するまでマージは失敗します。

Compose には、マージ競合を解決するための API が実際にあります。mutableStateOf() は、オプションの [SnapshotMutationPolicy](https://developer.android.com/reference/kotlin/androidx/compose/runtime/SnapshotMutationPolicy?ref=blog.zachklipp.com) を受け取ります。このポリシーは、特定のタイプの値を比較する方法 (equivalent) と競合を解決する方法 (merge) の両方を定義します。いくつかのポリシーはすぐに使用できます。

- structuralEqualityPolicy – オブジェクトを equals メソッド (==) を使用して比較し、すべての書き込みは競合していないと見なされます。
- referentialEqualityPolicy – オブジェクトを参照 (===) で比較し、すべての書き込みは競合していないと見なされます。
- neverEqualPolicy – すべてのオブジェクトを不等として扱い、すべての書き込みは競合していないと見なされます。これは、たとえば、スナップショットが可変値を保持し、== または === では検出できない方法で値が変更されたことを表す場合に使用します。可変オブジェクトを可変状態オブジェクトに保持することは安全ではありません (変更がまったく分離されていないため) が、オブジェクトの実装が制御できない場合には便利です。

事前に構築されたポリシーのいずれも書き込み競合を解決しないことに注意してください。競合解決はユースケースに大きく依存するため、適切なデフォルトはありません。一方、競合は簡単に解決できる場合もあります。 [merge メソッドのメソッド ドキュメント](https://developer.android.com/reference/kotlin/androidx/compose/runtime/SnapshotMutationPolicy?hl=en&ref=blog.zachklipp.com#merge(kotlin.Any,kotlin.Any,kotlin.Any)) には、カウンターの例が含まれています。カウンターが 2 つのスナップショットで独立して増加した場合、マージされたカウンター値は、両方のスナップショットでカウンターが増加した量の合計になります。

この例では、名前をマージしてもあまり意味がないので、代わりに以前の名前を新しい名前に含めるだけにします。

```kotlin
class Dog {
    var name: MutableState<String> =
        mutableStateOf("", policy = object : SnapshotMutationPolicy<String> {
            override fun equivalent(a: String, b: String): Boolean = a == b

            override fun merge(previous: String, current: String, applied: String): String =
                "$applied, briefly known as $current, originally known as $previous"
        })
}

fun main() {
    // Same as before.
}
```

```
// Output:

Spot
in snapshot1: Fido
Spot
in snapshot2: Fluffy
before applying: Spot
after applying 1: Fido
after applying 2: Fluffy, briefly known as Fido, originally known as Spot
```

スナップショットの適用によって解決できない書き込み競合が発生した場合、適用操作は失敗したとみなされます。その時点で残された唯一の作業は、新しいスナップショットを取得して変更を再試行することです。


## 結論

Compose のスナップショット システムは、その設計の主要な機能の一部を実現します。

1. リアクティブ性: ステートフル コードは常に自動的に最新の状態に保たれます。機能開発者だけでなく、ライブラリ開発者であっても、無効化やサブスクリプションの追跡について心配する必要はありません。手動でダーティ フラグを追加することなく、スナップショットを使用して独自の「リアクティブ」コンポーネントを作成できます。

2. 分離性: ステートフル コードは、異なるスレッドで実行されているコードによってその状態が変更されることを心配することなく、状態を操作できます。Compose はこれを利用して、古い View ツールキットではできなかったトリック (複数のバックグラウンド スレッドへの再コンポジションの展開など) を実行できます。

そして、これはすべて、昔ながらの Kotlin 機能を使用して行われます。コンパイラ マジックは使用されません。Compose にはコンパイラ プラグインが含まれていますが、そのプラグインは他の用途に使用されます。スナップショット システムは、内部で ThreadLocal を使用します。これは、Java の標準概念であり、長い間使われてきました。後続の投稿で、その使用法について説明する予定ですので、お楽しみに!

Compose の残りの部分を使用せずにスナップショット システムを試したい場合は、ランタイム アーティファクトへの依存関係を追加するだけです。

```
implementation("androidx.compose.runtime:runtime:$composeVersion")
```

これで、mutableStateOf が Compose インフラストラクチャの残りの部分と連携して状態の変化を監視し、それに対応する仕組みがわかるようになると思います。さらに質問がある場合は、コメントでお知らせください。


## 参考資料

実際のスナップショット アルゴリズムの仕組みについて詳しく知りたい場合は、 [「スナップショットのシャッターを開く」](https://blog.zachklipp.com/talk-opening-the-shutter-on-snapshots/) という講演で詳細を解説し、多くの図を使って説明しています。

残念ながら、この記事の執筆時点では、Compose のこの部分に関する公式ドキュメントはあまりありません。私が見つけた中で最も近いのは、公式 Android ポッドキャスト Android Developers Backstage の [「Jetpack Compose Compilation」エピソード](https://adbackstage.libsyn.com/episode-164-jetpack-compose-compilation?ref=blog.zachklipp.com) です。このエピソードでは、Leland と Adam がスナップショットの仕組みについて説明しています。

コードがどのように機能するかを学ぶ最良の方法は、コードを読むことです。Compose のソース コードは、一般的に非常に簡潔で、コメントも充実しているため、非常に優れた出発点となります。掘り下げるべきことはまだまだたくさんあります。この記事では、スナップショットのパブリック API のサブセットのみを取り上げています。API のほとんどは [Snapshot.kt ファイル](https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:compose/runtime/runtime/src/commonMain/kotlin/androidx/compose/runtime/snapshots/Snapshot.kt;l=45?q=Snapshot&sq=&ss=androidx&ref=blog.zachklipp.com) にあるため、そこから自分で調べ始めるのがよいでしょう。この API の一部が実際にどのように使用されているかを確認したい場合は、 [snapshotFlow の実装](https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:compose/runtime/runtime/src/commonMain/kotlin/androidx/compose/runtime/SnapshotState.kt;l=733-790;drc=a3551f26005a5a3828677c41779345e379375f0b?ref=blog.zachklipp.com) を確認してください。

この背後にある理論を本当に深く掘り下げたい場合は、さらにいくつかのヒントがあります。

- まずは、 [マルチバージョン同時実行制御 (MVCC) に関する Wikipedia の記事](https://en.wikipedia.org/wiki/Multiversion_concurrency_control?ref=blog.zachklipp.com) から始めることができます。 Compose のスナップショット システムは MVCC の実装です。MVCC は通常、データベースのコンテキストで説明されますが、この記事で指摘されているように、揮発性メモリに保存された状態にも適用されます。よく見てみると、揮発性メモリも一種のデータベースです。
- スナップショット コードには、論文 [「シリアル化可能なマルチバージョン同時実行制御の再考」](https://arxiv.org/pdf/1412.2324.pdf?ref=blog.zachklipp.com) の PDF にリンクする [コメント](https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:compose/runtime/runtime/src/commonMain/kotlin/androidx/compose/runtime/snapshots/Snapshot.kt;l=570;drc=5563346d0db8dd40b336943c823138f53ed227a1?ref=blog.zachklipp.com) もあります。
- 上記の反例は、「競合のないデータ型」と呼ばれるものの例です。詳細については、 [こちら](https://en.wikipedia.org/wiki/Conflict-free_replicated_data_type?ref=blog.zachklipp.com) をご覧ください。

また、 [Compose の状態に関する私の他の記事](https://blog.zachklipp.com/tag/compose-state-series/) もご覧ください。

この記事をレビューして早期にフィードバックを提供してくれた Mark Murphy、Matt McKenna、Adam McNeilly、そして広範囲にわたる技術的修正を提供し、その過程で多くのことを教えてくれた Chuck Jazdzewski と Sean McQuillan に感謝します。


## 引用元資料

- [Introduction to the Compose Snapshot system](https://blog.zachklipp.com/introduction-to-the-compose-snapshot-system/)

