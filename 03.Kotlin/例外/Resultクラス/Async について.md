- [Async の正体](#async-の正体)
- [なぜ Kotlin 標準に存在しないのか](#なぜ-kotlin-標準に存在しないのか)
- [Async の役割](#async-の役割)
- [Result との違い（復習）](#result-との違い復習)
- [なぜ ViewModel に Async があるときれいか](#なぜ-viewmodel-に-async-があるときれいか)
- [Android 標準に近いものはある？](#android-標準に近いものはある)
- [まとめ](#まとめ)


[Result と runCatching.md](./Result%20と%20runCatching.md) で出てきた `Async` は **Kotlin 標準でも Android 標準でもありません**。

---

## Async の正体

あの `Async` は、

> **アプリ（もしくはプロジェクト）側で定義する UI 状態用のクラス**

です。

よくある定義はこんな形です。

```kotlin
sealed class Async<out T> {
    object Loading : Async<Nothing>()
    data class Success<T>(val data: T) : Async<T>()
    data class Error(val messageRes: Int) : Async<Nothing>()
}
```

* Kotlin 標準ではない
* Jetpack Compose 導入時によく自作される
* `UiState` / `LoadState` / `Resource` など、名前はプロジェクト次第

---

## なぜ Kotlin 標準に存在しないのか

理由はシンプルで、

* UI の状態は **アプリごとに要件が違う**
* エラー表現も、ローディングの考え方も違う

からです。

標準化すると逆に使いにくくなります。

---

## Async の役割

`Async` は：

* 成功 / 失敗だけでなく
* **読み込み中（Loading）** を含めた
* **UI 表示のための状態**

を表すための型です。

```kotlin
Loading
Success(data)
Error(message)
```

という **画面目線の状態**。

---

## Result との違い（復習）

混ざりやすいので整理します。

* Result

  * 成功 / 失敗
  * 業務ロジックの結果
  * Domain / UseCase 向け

* Async

  * Loading / Success / Error
  * 非同期処理の進行状態
  * ViewModel / UI 向け

👉 **役割が違うので、どちらかで代用しない**

---

## なぜ ViewModel に Async があるときれいか

```kotlin
val state: StateFlow<Async<List<Item>>> =
    repository.loadItems()
        .map { Async.Success(it) }
        .onStart { emit(Async.Loading) }
        .catch { emit(Async.Error(R.string.error)) }
```

onStart については、 [Flow.onStart について.md](./Flow.onStart%20について.md) を参照してください。

UI 側は：

```kotlin
when (state) {
    is Async.Loading -> showLoading()
    is Async.Success -> showList(state.data)
    is Async.Error -> showError()
}
```

* 例外を知らなくていい
* Flow を知らなくていい
* 状態だけを描画する

👉 **Compose と相性が非常に良い**

---

## Android 標準に近いものはある？

完全に同じものはありませんが、近い概念はあります。

* Paging3 の `LoadState`
* NetworkBoundResource（昔の Architecture Components）
* Google サンプルでの `UiState` / `ViewState`

ただし、どれも **自分で定義する前提**です。

---

## まとめ

* `Async` は Kotlin / Android 標準ではない
* UI 状態表現のために自作するクラス
* Result の代替ではない
* Flow + Compose との相性が抜群
* 名前や構造はプロジェクト次第でよい

