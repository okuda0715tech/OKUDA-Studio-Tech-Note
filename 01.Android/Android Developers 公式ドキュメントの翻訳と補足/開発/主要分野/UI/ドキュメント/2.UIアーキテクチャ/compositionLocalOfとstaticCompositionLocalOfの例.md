

# compositionLocalOfとstaticCompositionLocalOfの例

## compositionLocalOf の使いどころ

### ① UI の見た目に影響する「環境値」

**典型例**

- Theme（色・文字サイズ・spacing など）
- Density / FontScale
- ダークモード・表示モード
- 最近独自で定義した DensityMode など

```kotlin
val LocalDensityMode = compositionLocalOf { DensityMode.Normal }
```

**理由**

- 値が切り替わることがある
- 影響範囲は「読んでいる UI だけ」で十分
- 無駄な再コンポジションを避けたい

**ポイント**

「この値を使っている Composable だけ更新されてほしい」なら compositionLocalOf


### ② 状態に近いが、引数で渡すのがつらいもの

- ネストが深く、毎回引数で渡すと地獄
- ただし 画面の一部だけが反応すればいい

```kotlin
val LocalSnackbarHost = compositionLocalOf<SnackbarHostState> {
    error("No SnackbarHostState provided")
}
```

これも「使っている場所だけ再描画」でよい。


## staticCompositionLocalOf の使いどころ

### ① ほぼ不変の「依存関係」

**典型例**

- Repository
- UseCase
- Logger
- Navigator
- Analytics
- Clock / Dispatcher

```kotlin
val LocalUserRepository =
    staticCompositionLocalOf<UserRepository> {
        error("No UserRepository provided")
    }
```

**理由**

- 実行中に差し替わる前提がない
- 仮に変わるなら「画面ごと作り直す」レベル
- 変更時に Composition 全体が再構築されても問題ない

**ポイント**

「これは DI コンテナの代替」なら staticCompositionLocalOf

「BaseActivity の代替として Hilt + CompositionLocal」の文脈では、ここはほぼ全部 staticCompositionLocalOf です。


### ② 再コンポジションを起こしたくないもの

```kotlin
val LocalAnalytics =
    staticCompositionLocalOf<Analytics> { NoOpAnalytics }
```

- ログ送信で UI が再コンポジションされるのは意味不明
- 「参照できればいい」もの


## よくある間違いと指針

❌ なんでも compositionLocalOf

- Repository / UseCase を compositionLocalOf にする
- → 値が変わると UI が部分的に再描画される
- → 「なぜここが recomposition された？」地獄

❌ 状態を staticCompositionLocalOf に入れる

```kotlin
val LocalUiState = staticCompositionLocalOf<UiState> { ... }
```

- 状態更新のたびに Composition 全体が再構築
- パフォーマンスも思考モデルも崩壊


## 実務での即断ルール（覚えやすい）

- これは UI の見た目・振る舞いを変えるか？
  - YES → compositionLocalOf
- これは依存関係・サービス・仕組みか？
  - YES → staticCompositionLocalOf
- 変わる前提で設計しているか？
  - YES → compositionLocalOf
  - NO → staticCompositionLocalOf


## Compose 的に正しい考え方

Compose は本来、

**「状態が変わる → UI が再構築される」**

という世界です。

- compositionLocalOf → 状態を運ぶための仕組み
- staticCompositionLocalOf → 依存を注入するための仕組み

と捉えると、設計が一気にクリアになります。




