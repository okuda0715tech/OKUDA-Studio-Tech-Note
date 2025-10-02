<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [Adapter 一覧](#adapter-一覧)
  - [ArrayAdapter](#arrayadapter)
  - [CursorAdapter](#cursoradapter)
<!-- TOC END -->


# Adapter 一覧

## ArrayAdapter



## CursorAdapter

`Cursor` 自体が非推奨であるため、 `CursorAdapter` も非推奨です。

以下は、 [公式ドキュメント](https://developer.android.com/training/data-storage/room/accessing-data?hl=ja) からの引用です。

```
注意: Cursor API は、行の存在や行に含まれる値を保証しないため、使用しないことを強くおすすめします。
この機能は、カーソルを必要とするコードがすでにあり、簡単にリファクタリングできない場合にのみ使用してください。
```
