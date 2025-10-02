- [coroutinScope{} ブロックと launch{} ブロックの違い](#coroutinscope-ブロックと-launch-ブロックの違い)
  - [主な違い](#主な違い)
  - [CoroutineScope の役割](#coroutinescope-の役割)
    - [コンポーネントのライフサイクル連動](#コンポーネントのライフサイクル連動)
    - [コルーチンの構造化](#コルーチンの構造化)
  - [Android で独自の CoroutineScope が必要な場合](#android-で独自の-coroutinescope-が必要な場合)
    - [独自のコルーチンスコープが必要なケース](#独自のコルーチンスコープが必要なケース)
      - [1. 特定の非同期タスク管理](#1-特定の非同期タスク管理)
      - [2. エラーハンドリングの制御](#2-エラーハンドリングの制御)
      - [3. 独立したライフサイクル管理](#3-独立したライフサイクル管理)
    - [独自のスコープを定義する方法](#独自のスコープを定義する方法)


# coroutinScope{} ブロックと launch{} ブロックの違い

## 主な違い

- **待機の有無**
  - coroutineScope {} は、そのスコープ内のすべての処理が完了するまで待機します。
  - launch {} は、非同期で処理を開始し、すぐに制御を戻します。

- **戻り値**
  - coroutineScope {} は、スコープ内の処理が終了した後に結果を返します。
  - launch {} は Job を返します。 Job を明示的に使用しない限り、戻り値を無視しても構いません。

- **エラーハンドリング**
  - coroutineScope {} は、ブロック内で例外が発生するとスコープ全体をキャンセルし、呼び出し元に例外を伝搬します。
  - launch {} は、例外を上位のスコープに伝搬する動作が異なることがあります。


## CoroutineScope の役割

### コンポーネントのライフサイクル連動

CoroutineScope は、何らかのライフサイクルに連動させて定義します。例えば、 Android では、 ViewModel に連動させた viewModelScope や、 Activity / Fragment に連動させた lifecycleScope のような定義済みのコルーチンスコープが存在します。

これらは、 ViewModel や Activity が破棄された際に、対応する CoroutineScope を破棄することを可能にしています。このことからわかるように、 CoroutineScope は、何らかのライフサイクルに連動するコルーチンのスコープを作成する役割があります。

一方で、 launch{} ブロックでも、コルーチンのスコープを定義することは可能ですが、これらは、そのスコープが単純なブロックになっており、他の何らかのライフサイクルには連動していません。 launch のスコープは、その場限りの単純なスコープを定義する場合に使用されます。


### コルーチンの構造化

launch {} ブロックが、同時並行処理を開始するのに対して、 CoroutineScope は同時並行処理を開始しません。

launch は、同時並行処理行うために使用します。

一方で、 CoroutineScope は、複数のコルーチンを構造的に管理するために使用されます。構造的な管理とは、例えば、以下のようなものがあります。

- 例外の伝搬
- スコープをキャンセルしたら、すべての子をまとめてキャンセルする
- すべての子が完了するまで、スコープを終了しない


## Android で独自の CoroutineScope が必要な場合

Android では、 viewModelScope や lifecycleScope のような、非常に便利なコルーチンスコープが存在し、通常、これらを活用すれば、多くの場合で十分です。ただし、以下のような場合には独自のスコープを定義する必要が出てくることもあります。


### 独自のコルーチンスコープが必要なケース

#### 1. 特定の非同期タスク管理

- viewModelScopeやlifecycleScopeは親スコープに依存していますが、特定のタスクやコンポーネントで独立したスコープを持たせたい場合は、coroutineScopeやCoroutineScopeを使って独自のスコープを定義するのが有用です。
- 例えば、バックグラウンドで長時間実行されるプロセスをViewModelとは独立して管理したい場合や、カスタムコンポーネントが必要な場合に利用されます。

#### 2. エラーハンドリングの制御

- viewModelScopeやlifecycleScopeはSupervisorJobを基にしており、子コルーチンが失敗しても他の子コルーチンには影響を与えません。しかし、親子関係があるスコープで異なるエラーハンドリング戦略を取りたい場合には、独自のスコープを作成することが求められることがあります。

#### 3. 独立したライフサイクル管理

- 例えば、ライフサイクルに依存しないコンポーネントやシステム的な非同期処理（サービスやカスタムクラスでの処理など）を扱う場合、lifecycleScopeやviewModelScopeは適していないため、カスタムのCoroutineScopeを定義することが必要です。


### 独自のスコープを定義する方法

独自のCoroutineScopeはJobやDispatcherを組み合わせて作成できます。

```kotlin
val customScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

customScope.launch {
    // 独自の非同期タスクをここで実行
}

// コルーチンスコープをクリーンアップしたい場合は
// cancel 関数を呼び出します。
// スコープ内の全てのコルーチンをキャンセルします。
customScope.cancel()
```

customScopeを使用することで、スコープ内のコルーチンはSupervisorJobの特性を持ち、Dispatchers.IOで非同期処理が行われます。

一度キャンセルされた CoroutineScope は、再利用できないため、新しくスコープを作り直す必要があります。

```kotlin
// 新しいスコープを作成
val newCustomScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
```




