- [Result クラスと runCatching](#result-クラスと-runcatching)
  - [Result とは何か](#result-とは何か)
    - [代表的な形（概念）](#代表的な形概念)
  - [Result の目的](#result-の目的)
  - [runCatching とは何か](#runcatching-とは何か)
    - [定義イメージ](#定義イメージ)
  - [runCatching の戻り値型](#runcatching-の戻り値型)
    - [failure のとき何を返しているか](#failure-のとき何を返しているか)
  - [Nothing が成り立つ理由](#nothing-が成り立つ理由)
  - [Result の主な使いどころ](#result-の主な使いどころ)
    - [向いているケース](#向いているケース)
  - [Result が向いていないケース](#result-が向いていないケース)
    - [Flow と組み合わせた UI 状態管理](#flow-と組み合わせた-ui-状態管理)
  - [runCatching 使用時の注意点](#runcatching-使用時の注意点)
    - [CancellationException を捕まえてしまう](#cancellationexception-を捕まえてしまう)
  - [Result と他の概念の役割分担](#result-と他の概念の役割分担)
  - [判断指針（迷ったらこれ）](#判断指針迷ったらこれ)
  - [まとめ](#まとめ)


# Result クラスと runCatching

## Result とは何か

`Result<T>` は **処理の成功 / 失敗を値として表現する型**。

* 成功時：値 `T` を持つ
* 失敗時：`Throwable` を持つ
* 例外を投げずに、呼び出し側に判断を委ねる

### 代表的な形（概念）

```kotlin
sealed class Result<out T> {
    data class Success<T>(val value: T) : Result<T>()
    data class Failure(val error: Throwable) : Result<Nothing>()
}
```

※ Kotlin 標準の `Result<T>` は sealed class ではないが、考え方は同じ。

---

## Result の目的

* 例外を「制御フロー」ではなく「値」として扱う
* 成功 / 失敗の分岐を **明示的に強制** する
* 業務ロジックや単発処理の結果を表現するのに向いている

---

## runCatching とは何か

`runCatching` は **例外を捕まえて `Result` に変換する関数**。

### 定義イメージ

```kotlin
fun <T> runCatching(block: () -> T): Result<T> =
    try {
        Result.success(block())
    } catch (e: Throwable) {
        Result.failure(e)
    }
```

* 成功 → `Result.success(value)`
* 例外 → `Result.failure(exception)`

`try-catch` の糖衣構文。

---

## runCatching の戻り値型

```kotlin
val result = runCatching { loadUser() }
```

* `loadUser()` が `User` を返す場合
* `T = User`
* 戻り値型は **`Result<User>`**

### failure のとき何を返しているか

```kotlin
Result.failure(e)
```

* 実体は **成功値を持たない Result**
* 型的には `Result<Nothing>`

---

## Nothing が成り立つ理由

* `Nothing` はすべての型のサブタイプ
* `Result` は共変（`out T`）

そのため：

```kotlin
Result<Nothing> は Result<User> として扱える
```

失敗時には「成功値は存在しない」ことが型で保証される。

---

## Result の主な使いどころ

### 向いているケース

* 単発の処理結果
* 成功 / 失敗で明確に分岐したい業務ロジック
* UseCase / Domain レイヤー

例：

```kotlin
fun registerUser(): Result<RegisteredUser>
```

```kotlin
when (val result = useCase()) {
    isSuccess -> proceed()
    isFailure -> showError()
}
```

---

## Result が向いていないケース

### Flow と組み合わせた UI 状態管理

* Flow には `catch` がある
* UI には `Async` / `UiState` がある

```kotlin
flow
    .map { Async.Success(it) }
    .catch { emit(Async.Error(...)) }
```

この構成に `Result` を混ぜると、

* エラー表現が二重になる
* 可読性が下がる

---

## runCatching 使用時の注意点

### CancellationException を捕まえてしまう

`runCatching` は **すべての Throwable を捕まえる**。

* coroutine キャンセルも failure 扱いになる
* Flow / coroutine 文脈では事故になりやすい

対策例：

```kotlin
runCatching {
    block()
}.onFailure {
    if (it is CancellationException) throw it
}
```

---

## Result と他の概念の役割分担

* Result

  * 成功 / 失敗
  * 業務判断
  * 単発処理

* Flow + catch

  * データの流れ
  * 非同期ストリーム

* Async / UiState

  * Loading / Success / Error
  * UI 表示の都合

---

## 判断指針（迷ったらこれ）

* 状態の「流れ」か？ → Flow
* 成功 / 失敗の「結果」か？ → Result
* UI 表示の話か？ → UiState / Async
* 業務上の判断か？ → Result

---

## まとめ

* `Result` は成功 / 失敗を値で表す型
* `runCatching` は例外を `Result` に変換する関数
* failure 時の中身は `Nothing` で表現される
* Flow + UI 状態管理では無理に使わない
* 単発処理・業務ロジックで真価を発揮する


