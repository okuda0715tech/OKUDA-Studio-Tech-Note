- [callbackFlow](#callbackflow)
  - [callbackFlow とは何か](#callbackflow-とは何か)
  - [なぜ必要か](#なぜ必要か)
  - [callbackFlow の役割](#callbackflow-の役割)
  - [重要ポイント（設計的に）](#重要ポイント設計的に)
    - [1. cold Flow である](#1-cold-flow-である)
    - [2. ライフサイクル管理を Flow に委ねられる](#2-ライフサイクル管理を-flow-に委ねられる)
    - [3. バッファと backpressure を扱える](#3-バッファと-backpressure-を扱える)
  - [じゃあ StateFlow / SharedFlow と何が違う？](#じゃあ-stateflow--sharedflow-と何が違う)
  - [Repository でよく使う理由](#repository-でよく使う理由)
  - [よくある誤解](#よくある誤解)
    - [❌ callbackFlow = hot flow](#-callbackflow--hot-flow)
  - [まとめ](#まとめ)


# callbackFlow

**`callbackFlow` は Kotlin Flow の中でも「境界」を担当する重要な存在** です。

---

## callbackFlow とは何か

**コールバックベースの API を Flow（cold）として包むためのビルダー** です。

> 「Flow じゃない世界」
> （リスナー / コールバック / Observer）
> を
> 「Flow の世界」に変換する道具

---

## なぜ必要か

現実の Android には、今でもこういう API が大量にあります。

* `addListener { ... }`
* `registerCallback(callback)`
* `onChanged(value)`
* `BroadcastReceiver`
* センサー / 位置情報 / Bluetooth
* Firebase / Play Billing / etc

これらはすべて：

* 自分で `emit` しない
* 勝手にイベントが飛んでくる
* 解除しないとメモリリークする

👉 **普通の `flow {}` では扱えない**

---

## callbackFlow の役割

```kotlin
val flow = callbackFlow {
    val callback = object : Callback {
        override fun onEvent(value: Int) {
            trySend(value)
        }
    }

    registerCallback(callback)

    awaitClose {
        unregisterCallback(callback)
    }
}
```

これで：

* collect された瞬間に `register`
* cancel された瞬間に `unregister`
* Flow は **cold のまま**

---

## 重要ポイント（設計的に）

### 1. cold Flow である

* `callbackFlow` 自体は **cold**
* collect されるまで何も起きない
* collect が終わると必ずクリーンアップ

👉 まさに **コールドスタンバイ**

---

### 2. ライフサイクル管理を Flow に委ねられる

```kotlin
awaitClose { ... }
```

ここが本体です。

* collect 中 = 登録済み
* collect 終了 = 必ず解除

Android で最も事故りやすい部分を
**構造で防げる**

---

### 3. バッファと backpressure を扱える

```kotlin
callbackFlow {
    trySend(value)
}
.buffer(Channel.BUFFERED)
```

* イベントが速すぎても落ちにくい
* 必要なら DROP / SUSPEND にもできる

---

## じゃあ StateFlow / SharedFlow と何が違う？

* `callbackFlow`

  * 外部イベントを **取り込む側**
  * cold
  * 境界・Adapter

* `SharedFlow / StateFlow`

  * 内部で **配る側**
  * hot
  * ハブ・分配器

👉 よくある構成：

```
[Callback API]
      ↓
 callbackFlow   ← 境界
      ↓
  shareIn / stateIn
      ↓
 ViewModel / UI
```

---

## Repository でよく使う理由

Repository はよく

* DB
* Network
* System Service

と接続します。

これらは Flow ネイティブではないことが多い。

👉 **Repository = callbackFlow の主戦場**

---

## よくある誤解

### ❌ callbackFlow = hot flow

違います。

* 中で hot なものを扱っているだけ
* 外に出る Flow は cold

hot にしたいなら **明示的に**：

```kotlin
callbackFlow { ... }
    .shareIn(scope, ...)
```

---

## まとめ

* callbackFlow = **コールバック世界と Flow 世界の橋**
* cold flow
* ライフサイクルと解放を安全に扱える
* Repository レイヤーの必須技術




