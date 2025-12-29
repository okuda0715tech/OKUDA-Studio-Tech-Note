- [Scaffold のラムダ引数 paddingValues の使い方](#scaffold-のラムダ引数-paddingvalues-の使い方)
  - [結論から（どう使うか）](#結論からどう使うか)
  - [paddingValues の正体](#paddingvalues-の正体)
  - [なぜ自分で padding を決めちゃダメなのか？](#なぜ自分で-padding-を決めちゃダメなのか)
    - [問題点](#問題点)
  - [なぜ「ラムダ引数」なのか？](#なぜラムダ引数なのか)
    - [理由](#理由)
  - [よくある使い分けパターン](#よくある使い分けパターン)
    - [① content 全体に適用（基本）](#-content-全体に適用基本)
    - [② スクロール領域だけに適用](#-スクロール領域だけに適用)
    - [③ padding を分解して使う（上級）](#-padding-を分解して使う上級)
  - [WindowInsets との関係（重要）](#windowinsets-との関係重要)
    - [原則](#原則)
  - [設計観点でのまとめ](#設計観点でのまとめ)
  - [一文で覚えるなら](#一文で覚えるなら)


# Scaffold のラムダ引数 paddingValues の使い方

Jetpack Compose の `Scaffold` に出てくる **ラムダ引数の `paddingValues`** は、

**「Scaffold が占有している領域（TopBar / BottomBar / FAB / system bars など）を避けるための余白」**を、

**中身（content 側）に伝えるためのもの**です。

---

## 結論から（どう使うか）

**Scaffold の content 直下で、必ず `Modifier.padding(paddingValues)` を適用する**

これが基本かつ正解です。

```kotlin
Scaffold(
    topBar = { TopAppBar(title = { Text("Title") }) },
    bottomBar = { BottomAppBar { /* ... */ } }
) { paddingValues ->
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues) // ← ここが本体
    ) {
        Text("Content")
    }
}
```

---

## paddingValues の正体

`paddingValues` の型は `PaddingValues` です。

中身はざっくり言うと：

* `top` : TopAppBar + status bar 分
* `bottom` : BottomBar + navigation bar 分
* `start / end` : system insets（ジェスチャーナビ等）

つまり、

> **「Scaffold がレイアウト都合で確保した余白の合計値」**

を **計算済みで渡してくれている** ものです。

---

## なぜ自分で padding を決めちゃダメなのか？

よくある NG 例：

```kotlin
Scaffold(
    topBar = { TopAppBar(...) }
) {
    Column(
        modifier = Modifier.padding(top = 56.dp) // ❌
    ) { ... }
}
```

### 問題点

* TopAppBar の高さは **Material / WindowInsets / 端末**で変わる
* system bars（ステータスバー、ナビゲーションバー）を無視している
* 将来 TopBar を変えた瞬間に崩れる

👉 **Scaffold の責務を無視している**

---

## なぜ「ラムダ引数」なのか？

```kotlin
Scaffold { paddingValues -> ... }
```

これは偶然ではありません。

### 理由

* TopBar / BottomBar / FAB の **有無やサイズは毎回変わる**
* 再コンポジションのたびに **正しい padding を再計算**
* 状態駆動 UI と相性が良い

つまり：

> **paddingValues は「状態」**

---

## よくある使い分けパターン

### ① content 全体に適用（基本）

```kotlin
Scaffold { padding ->
    Box(
        Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        ScreenContent()
    }
}
```

最も安全で、ほぼすべての画面でこれ。

---

### ② スクロール領域だけに適用

```kotlin
Scaffold { padding ->
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = padding // ← ここが重要
    ) {
        items(100) {
            Text("Item $it")
        }
    }
}
```

* `LazyColumn` / `LazyRow` の場合は
  **`Modifier.padding` ではなく `contentPadding`**

理由：

* スクロール範囲として padding を扱うため

---

### ③ padding を分解して使う（上級）

```kotlin
Scaffold { padding ->
    val topPadding = padding.calculateTopPadding()

    Column(
        modifier = Modifier.padding(top = topPadding)
    ) {
        Header()
        Content()
    }
}
```

* ヘッダーだけずらしたい
* bottom は FAB に重ねたい、など

👉 **calculateXxxPadding() を使う**

---

## WindowInsets との関係（重要）

Material3 の `Scaffold` は内部で：

* `WindowInsets.systemBars`
* `WindowInsets.navigationBars`

などを **自動的に考慮**しています。

そのため、

```kotlin
Modifier.systemBarsPadding()
Modifier.navigationBarsPadding()
```

を **Scaffold 内で重ねがけすると二重 padding** になりがちです。

### 原則

* **Scaffold の中では paddingValues を信じる**
* Insets を自前で触るのは「Scaffold の外」か「特殊ケースのみ」

---

## 設計観点でのまとめ

設計の観点で言うと：

* `Scaffold`
  → **レイアウト責務の集約**
* `paddingValues`
  → **Scaffold → content への「レイアウト状態の伝搬」**
* content 側
  → **それをそのまま受け取るだけ**

つまり、

> **paddingValues を無視する = Scaffold の責務を破壊する**

と言っていいレベルです。

---

## 一文で覚えるなら

> **Scaffold の content では、paddingValues を「考えるな、当てろ」**

