- [Scaffold をネストした場合どうなる？](#scaffold-をネストした場合どうなる)
  - [結論](#結論)
  - [何が起きるか（現象）](#何が起きるか現象)
    - [よく見る症状](#よく見る症状)
  - [なぜダメなのか（設計視点）](#なぜダメなのか設計視点)
  - [それでもネストしていいケース（限定）](#それでもネストしていいケース限定)
    - [ケース①：**ナビゲーション単位で分かれている**](#ケースナビゲーション単位で分かれている)
    - [ケース②：**Insets を無効化している**](#ケースinsets-を無効化している)
  - [NG なネストの典型](#ng-なネストの典型)


# Scaffold をネストした場合どうなる？

## 結論

**原則 NG**
ただし **「責務が完全に分かれている場合のみアリ」**

---

## 何が起きるか（現象）

```kotlin
Scaffold { outerPadding ->
    Scaffold { innerPadding ->
        Content(
            Modifier.padding(innerPadding)
        )
    }
}
```

この場合：

* outer Scaffold

  * systemBars / TopBar / BottomBar を考慮
* inner Scaffold

  * **再び systemBars を考慮**

👉 **padding が二重にかかる**

### よく見る症状

* 上下に謎の余白
* BottomBar がやたら遠い
* ジェスチャーナビ端末で特にズレる

---

## なぜダメなのか（設計視点）

Scaffold は：

> **「画面全体のレイアウト責務を一手に引き受ける」**

コンポーネントです。

つまり：

* 1画面 = 1 Scaffold
* 複数置くと **「誰が画面を支配しているか」が曖昧**

---

## それでもネストしていいケース（限定）

### ケース①：**ナビゲーション単位で分かれている**

```kotlin
@Composable
fun AppRoot() {
    Scaffold { rootPadding ->
        NavHost(
            modifier = Modifier.padding(rootPadding),
            ...
        )
    }
}
```

```kotlin
@Composable
fun ScreenA() {
    Scaffold(
        topBar = { ... },
        bottomBar = { ... }
    ) { padding ->
        ScreenAContent(Modifier.padding(padding))
    }
}
```

* **Root Scaffold**

  * systemBars / ナビ全体
* **Screen Scaffold**

  * その画面固有の Top/Bottom

👉 責務が「ナビ階層」で分かれているので OK

---

### ケース②：**Insets を無効化している**

```kotlin
Scaffold(
    contentWindowInsets = WindowInsets(0)
) { _ ->
    Scaffold { padding ->
        Content(Modifier.padding(padding))
    }
}
```

* 外側 Scaffold：見た目上のラッパー
* 内側 Scaffold：実レイアウト担当

👉 **どちらが insets を処理するかを明示**

---

## NG なネストの典型

* Fragment ごとに Scaffold
* 共通レイアウトのために Scaffold をラップ
* 「とりあえず Scaffold」文化

これは **確実にレイアウトバグ製造機**です。

