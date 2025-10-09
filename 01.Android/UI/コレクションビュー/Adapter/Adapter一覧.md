- [Adapter 一覧](#adapter-一覧)
  - [RecyclerView.Adapter](#recyclerviewadapter)
    - [主な特徴](#主な特徴)
    - [代表的な拡張ライブラリ](#代表的な拡張ライブラリ)
  - [PagingDataAdapter](#pagingdataadapter)
  - [ConcatAdapter](#concatadapter)
  - [ExpandableListAdapter](#expandablelistadapter)
  - [ArrayAdapter](#arrayadapter)
  - [BaseAdapter](#baseadapter)
  - [CursorAdapter / SimpleCursorAdapter](#cursoradapter--simplecursoradapter)


# Adapter 一覧

## RecyclerView.Adapter

- 現代の主流。
- ArrayAdapter の上位互換的な存在で、リストやグリッド表示を行う際はほぼこれを使います。
- 柔軟でパフォーマンスが良く、View の再利用や差分更新 (DiffUtil) にも対応。

### 主な特徴

- RecyclerView とセットで使用。
- カスタム ViewHolder パターンで効率的に描画。
- リスト、グリッド、カルーセル、チャットなど多様な UI に対応。

### 代表的な拡張ライブラリ

- ListAdapter（ RecyclerView.Adapter のサブクラス。 DiffUtil と連携しやすい）
- PagingDataAdapter（Paging 3 ライブラリ用）


## PagingDataAdapter

- Jetpack の Paging 3 ライブラリ専用。
- サーバーやデータベースから大量のデータを少しずつ読み込むアプリ（例：SNSタイムラインやニュースアプリ）で使用。


## ConcatAdapter

- 複数の Adapter を 連結して 1 つの RecyclerView に表示 できる便利クラス。
- 例：ヘッダー + リスト + フッターを別々の Adapter で管理。


## ExpandableListAdapter

- 親子階層を持つリスト（例：グループ展開式リスト）で使用。
- 今はあまり使われませんが、設定画面などで階層データを表示する用途に。


## ArrayAdapter

- シンプルなリスト表示用。


## BaseAdapter

- ArrayAdapter より柔軟に作りたいときに使う汎用 Adapter。
- ただし、RecyclerView.Adapter の登場以降は利用機会が減少。
- 例：GridView や ListView 向けに完全カスタム表示をしたい場合。


## CursorAdapter / SimpleCursorAdapter

`Cursor` 自体が非推奨であるため、 `CursorAdapter` も非推奨です。

以下は、 [公式ドキュメント](https://developer.android.com/training/data-storage/room/accessing-data?hl=ja) からの引用です。

```
注意: Cursor API は、行の存在や行に含まれる値を保証しないため、使用しないことを強くおすすめします。
この機能は、カーソルを必要とするコードがすでにあり、簡単にリファクタリングできない場合にのみ使用してください。
```

- データベース（SQLite） と直接連携するための旧来の Adapter。
- 今ではほとんど使われません（Room + RecyclerView の方が一般的）。
- ただし古いコードベースではまだ残っていることがあります。





