- [onStart とは何か](#onstart-とは何か)
- [よくある使い方：Loading 状態を流す](#よくある使い方loading-状態を流す)
- [emit できる理由](#emit-できる理由)
- [onStart の実行タイミング（重要）](#onstart-の実行タイミング重要)
- [catch との関係](#catch-との関係)
- [onStart が向いているケース](#onstart-が向いているケース)
- [onStart が向いていないケース](#onstart-が向いていないケース)
- [onStart と似たオペレータとの違い](#onstart-と似たオペレータとの違い)
- [注意点（意外とハマる）](#注意点意外とハマる)
  - [collect のたびに呼ばれる](#collect-のたびに呼ばれる)
- [まとめ](#まとめ)


**`onStart` は Kotlin Coroutines Flow に標準で用意されているオペレータ**です。

かなり便利ですが、ちゃんと使いどころを理解していないと誤解もしやすいので、要点を整理します。

---

## onStart とは何か

一言でいうと：

> **Flow が collect される直前に、追加の処理や値の emit を行うためのオペレータ**

です。

```kotlin
flow
    .onStart { /* ここ */ }
    .collect { ... }
```

この `{}` の中は、

* collect が始まった瞬間
* 上流の Flow がまだ何も emit していないタイミング

で **1 回だけ** 実行されます。

---

## よくある使い方：Loading 状態を流す

一番よく使われるのがこれです。

```kotlin
val state: StateFlow<Async<List<Item>>> =
    repository.loadItems()
        .map { Async.Success(it) }
        .onStart { emit(Async.Loading) }
        .catch { emit(Async.Error(R.string.error)) }
```

意味としては：

1. collect 開始
2. `Async.Loading` を emit
3. Repository からデータが来たら `Async.Success`
4. 例外が出たら `Async.Error`

👉 UI は **自然に状態遷移を表現できる**

---

## emit できる理由

`onStart` のラムダは：

```kotlin
onStart { emit(value) }
```

という形をしています。

* `emit` は **FlowCollector の関数**
* 上流に「仮想的な値」を差し込める

つまり：

```kotlin
emit(Async.Loading)
```

は、

> 「本物のデータが来る前に、UI 用の状態を 1 つ流す」

という意味。

---

## onStart の実行タイミング（重要）

```kotlin
flow
    .onStart { ... }
    .onEach { ... }
    .collect { ... }
```

実行順は：

1. `onStart`
2. 上流 Flow の処理
3. `onEach`
4. `collect`

ただし **onStart は上流側** に属します。

---

## catch との関係

```kotlin
flow
    .onStart { emit(Loading) }
    .catch { emit(Error) }
```

* `onStart` 内で投げた例外
  → `catch` に流れる
* 上流 Flow / map の例外
  → `catch` に流れる

なので **エラー処理と自然につながる**。

---

## onStart が向いているケース

* ローディング状態を emit したい
* 初期化処理を入れたい（ログ、計測など）
* collect 開始時に 1 回だけ処理したい

---

## onStart が向いていないケース

* データごとに毎回処理したい
  → `onEach`
* 完了時に処理したい
  → `onCompletion`
* 値を変換したい
  → `map`

---

## onStart と似たオペレータとの違い

* `onEach`

  * 各 emit ごとに実行
* `onCompletion`

  * Flow 完了時に実行（正常・異常どちらも）
* `catch`

  * 上流の例外を処理

---

## 注意点（意外とハマる）

### collect のたびに呼ばれる

```kotlin
flow.onStart { ... }
```

は、

* Flow を collect するたびに
* 毎回実行される

`StateFlow` に `stateIn` する前に置くかどうかで挙動が変わる点には注意。

---

## まとめ

* `onStart` は Flow 標準のオペレータ
* collect 開始直前に 1 回だけ呼ばれる
* Loading 状態の emit に最適
* UI 状態管理（Async / UiState）と相性が良い
* collect のたびに実行されることを理解して使う

