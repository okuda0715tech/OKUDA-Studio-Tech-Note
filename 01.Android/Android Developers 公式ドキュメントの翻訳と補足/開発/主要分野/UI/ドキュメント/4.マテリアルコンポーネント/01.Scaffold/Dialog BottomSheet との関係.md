- [Dialog / BottomSheet との関係](#dialog--bottomsheet-との関係)
  - [Dialog は Scaffold の外側にいる](#dialog-は-scaffold-の外側にいる)
    - [ポイント](#ポイント)
    - [よくある誤解](#よくある誤解)
  - [BottomSheet（ModalBottomSheet）](#bottomsheetmodalbottomsheet)
    - [ModalBottomSheet は「Scaffoldの外」](#modalbottomsheet-はscaffoldの外)
  - [BottomSheetScaffold は「Scaffoldの仲間」](#bottomsheetscaffold-はscaffoldの仲間)
    - [ここでの関係](#ここでの関係)
  - [よくある事故パターン](#よくある事故パターン)
    - [事故①：BottomSheet の中で Scaffold を使う](#事故bottomsheet-の中で-scaffold-を使う)
    - [事故②：Sheet 内で systemBarsPadding()](#事故sheet-内で-systembarspadding)
  - [設計的まとめ（重要）](#設計的まとめ重要)
    - [Scaffold を使う判断基準](#scaffold-を使う判断基準)
    - [Dialog / Sheet](#dialog--sheet)
    - [言語化](#言語化)


# Dialog / BottomSheet との関係

ここが **Compose 初心者〜中級者の鬼門**。

---

## Dialog は Scaffold の外側にいる

```kotlin
if (showDialog) {
    AlertDialog(...)
}
```

### ポイント

* Dialog は **別 Window**
* Scaffold の paddingValues **無関係**
* systemBars も **OS 側が処理**

👉 Scaffold 内の padding を流用しない

### よくある誤解

```kotlin
AlertDialog(
    modifier = Modifier.padding(paddingValues) // ❌
)
```

これは **意味がない or 崩れる**

---

## BottomSheet（ModalBottomSheet）

### ModalBottomSheet は「Scaffoldの外」

```kotlin
ModalBottomSheet(
    onDismissRequest = ...
) {
    SheetContent()
}
```

* 画面全体を覆う
* Scaffold の BottomBar や FAB とは独立

👉 **Scaffold の paddingValues を渡さない**

---

## BottomSheetScaffold は「Scaffoldの仲間」

```kotlin
BottomSheetScaffold(
    sheetContent = { ... },
    topBar = { ... }
) { padding ->
    Content(Modifier.padding(padding))
}
```

### ここでの関係

* BottomSheetScaffold = Scaffold の派生
* paddingValues の意味は **通常の Scaffold と同じ**
* sheet の高さを考慮した padding が渡る

👉 **普通の Scaffold と同じ感覚で OK**

---

## よくある事故パターン

### 事故①：BottomSheet の中で Scaffold を使う

```kotlin
ModalBottomSheet {
    Scaffold { padding -> ... } // ⚠️
}
```

* Sheet は「画面の一部」
* Scaffold は「画面全体」

👉 責務不一致

### 事故②：Sheet 内で systemBarsPadding()

* Sheet 自体が insets を考慮済み
* 二重 padding になりやすい

---

## 設計的まとめ（重要）

### Scaffold を使う判断基準

* **画面全体を支配する？**

  * YES → Scaffold
  * NO → 使わない

### Dialog / Sheet

* Dialog

  * OS 管理の別レイヤ
  * Scaffold とは切り離す
* ModalBottomSheet

  * Overlay
  * paddingValues 不要
* BottomSheetScaffold

  * Scaffold と同列
  * paddingValues を素直に使う

---

### 言語化

Scaffold は **「便利なレイアウト」ではなく「画面の統治者」** です。

* 統治者は一人
* 境界（Dialog / Sheet）を越えない
* 権限（Insets）を二重にしない

この感覚が身につくと、 Compose のレイアウト事故はほぼ消えます。






