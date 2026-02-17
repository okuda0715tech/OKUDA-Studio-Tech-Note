- [Result の recover と recoverCatching 関数](#result-の-recover-と-recovercatching-関数)
  - [recover とは何か](#recover-とは何か)
    - [基本形](#基本形)
  - [recover の動作イメージ](#recover-の動作イメージ)
  - [recover が向いているケース](#recover-が向いているケース)
  - [recover の注意点](#recover-の注意点)
    - [transform 内で例外を投げるとどうなるか](#transform-内で例外を投げるとどうなるか)
  - [recoverCatching とは何か](#recovercatching-とは何か)
    - [定義イメージ](#定義イメージ)
  - [recover と recoverCatching の違い](#recover-と-recovercatching-の違い)
    - [recover](#recover)
    - [recoverCatching](#recovercatching)
  - [具体例で比較](#具体例で比較)
    - [recover](#recover-1)
    - [recoverCatching](#recovercatching-1)
  - [recoverCatching が向いているケース](#recovercatching-が向いているケース)
  - [Result チェーン全体の流れ](#result-チェーン全体の流れ)
  - [recover / recoverCatching と Flow の関係](#recover--recovercatching-と-flow-の関係)
  - [CancellationException に関する注意](#cancellationexception-に関する注意)
  - [判断指針（これだけ覚える）](#判断指針これだけ覚える)
  - [まとめ](#まとめ)


# Result の recover と recoverCatching 関数

## recover とは何か

`recover` は **失敗（failure）した Result を、別の成功値に置き換える関数**。

* failure のときだけ実行される
* 成功時（success）はそのまま通過
* 例外を投げない前提の回復処理向け

### 基本形

```kotlin
fun <R> Result<T>.recover(
    transform: (Throwable) -> R
): Result<R>
```

---

## recover の動作イメージ

```kotlin
val result: Result<Int> =
    runCatching { risky() }
        .recover { e ->
            0
        }
```

挙動：

* success → そのまま success
* failure → `transform` を実行し、その戻り値で **success に変換**

👉 **failure を「なかったこと」にする**

---

## recover が向いているケース

* デフォルト値で継続できる場合
* エラーが「致命的ではない」場合
* 業務ロジック上のフォールバック

例：

```kotlin
val userName =
    runCatching { loadUserName() }
        .recover { "unknown" }
        .getOrThrow()
```

---

## recover の注意点

### transform 内で例外を投げるとどうなるか

```kotlin
runCatching { risky() }
    .recover {
        throw IllegalStateException()
    }
```

* `recover` 自体は **例外を捕まえない**
* その例外は **そのまま外に投げられる**

👉 **安全ではない**

---

## recoverCatching とは何か

`recoverCatching` は、

> **recover + runCatching を組み合わせたもの**

### 定義イメージ

```kotlin
fun <R> Result<T>.recoverCatching(
    transform: (Throwable) -> R
): Result<R>
```

違いは：

* `transform` 内で例外が起きても
* それを **failure として Result に包み直す**

---

## recover と recoverCatching の違い

### recover

```kotlin
failure
    → transform 実行
        → 成功値を返す（success）
        → 例外を投げる（外に漏れる）
```

### recoverCatching

```kotlin
failure
    → transform 実行
        → 成功値を返す（success）
        → 例外を投げる（failure に変換）
```

---

## 具体例で比較

### recover

```kotlin
val result =
    runCatching { risky() }
        .recover {
            fallback() // ここで例外が出るとクラッシュ
        }
```

### recoverCatching

```kotlin
val result =
    runCatching { risky() }
        .recoverCatching {
            fallback() // 例外が出ても failure に戻る
        }
```

---

## recoverCatching が向いているケース

* フォールバック処理も失敗する可能性がある
* エラー回復自体を Result の世界に閉じたい
* チェーンを安全に続けたい

---

## Result チェーン全体の流れ

```kotlin
runCatching { load() }
    .map { it.toDomain() }
    .recover { defaultValue }
```

または：

```kotlin
runCatching { load() }
    .map { it.toDomain() }
    .recoverCatching { fallback() }
```

* success → map → success
* failure → recover / recoverCatching

---

## recover / recoverCatching と Flow の関係

* Result 用の回復手段
* Flow の `catch` とは役割が違う

対応関係のイメージ：

* `Result.recover` ≒ 「値レベルの回復」
* `Flow.catch` ≒ 「ストリームレベルの回復」

Flow で使うものではない。

---

## CancellationException に関する注意

* `recover` / `recoverCatching` も例外を扱う
* coroutine 文脈では `CancellationException` を回復対象にしない方がよい

基本指針：

* Flow / coroutine 内では Result を使わない
* 使う場合は Cancellation を再スローする設計にする

---

## 判断指針（これだけ覚える）

* 回復処理が **絶対に失敗しない**
  → `recover`
* 回復処理も **失敗する可能性がある**
  → `recoverCatching`
* 非同期ストリーム
  → Flow + `catch`
* UI 状態
  → Async / UiState

---

## まとめ

* `recover` は failure を success に変換する
* `recoverCatching` は回復処理の例外も failure に包む
* recover は安全ではない
* recoverCatching は安全
* Result は単発・業務ロジック向け
* Flow とは混ぜない

