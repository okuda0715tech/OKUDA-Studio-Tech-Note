- [MVC vs MVVM](#mvc-vs-mvvm)
  - [MVC（Model–View–Controller）](#mvcmodelviewcontroller)
    - [構成](#構成)
    - [流れ](#流れ)
    - [特徴](#特徴)
  - [MVVM（Model–View–ViewModel）](#mvvmmodelviewviewmodel)
    - [構成](#構成-1)
    - [流れ](#流れ-1)
    - [特徴](#特徴-1)
  - [まとめ](#まとめ)


# MVC vs MVVM

## MVC（Model–View–Controller）

### 構成

- Model：データやビジネスロジック（例：DB、API 通信、リポジトリなど）
- View：UI（Activity や Fragment の XML レイアウト）
- Controller：ユーザー入力を処理し、Model と View をつなぐ（Android では Activity や Fragment がこれを兼ねることが多い）


### 流れ

1. View（UI）でボタンが押される
2. Controller（Activity）がイベントを受け取る
3. Model にデータ操作を依頼
4. Model が結果を返す
5. Controller が View を更新する


### 特徴

- 実装が単純だが、Activity/Fragment にロジックが集中しがち。
- 大規模化すると「God Activity」問題（＝肥大化）が発生。


## MVVM（Model–View–ViewModel）

### 構成

- Model：データやビジネスロジック（MVC と同じ）
- View：UI（Compose の Composable や XML の UI）
- ViewModel：UI 状態やイベント処理を管理。View とは データバインディング（LiveData/StateFlow/Compose の state）でつながる。


### 流れ

1. View（UI）がユーザー操作を ViewModel に通知
2. ViewModel が Model に処理を依頼
3. Model から結果が返ると、ViewModel の状態（LiveData や StateFlow）を更新
4. View はその状態の変化を自動的に監視し、UI を更新


### 特徴

- View とロジックが疎結合（双方向バインディング or 状態の監視）
- UI 状態が明確に管理できる
- テストしやすく、Jetpack Compose との相性が非常に良い


## まとめ

誤解を恐れず、ざっくりと言うと、 MVC は Activity や Fragment に UI 回りの実装を記述する方法で、 MVVM は ViewModel に UI 回りの実装を記述する方法です。

| 観点                     | MVC                             | MVVM                                   |
| ------------------------ | ------------------------------- | -------------------------------------- |
| 中央的役割               | Activity/Fragment（Controller） | ViewModel                              |
| UI 更新方法              | Controller が直接更新           | ViewModel の状態変化を監視して自動更新 |
| 結合度                   | 高い（UI とロジックが密結合）   | 低い（状態を介して疎結合）             |
| テスト容易性             | 低い                            | 高い                                   |
| Jetpack Compose との相性 | △                               | ◎                                      |


