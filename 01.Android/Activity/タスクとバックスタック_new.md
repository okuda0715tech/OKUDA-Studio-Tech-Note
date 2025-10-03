- [タスクとバックスタック\_new](#タスクとバックスタック_new)
  - [起動モードと起動フラグ](#起動モードと起動フラグ)
  - [起動モードについて](#起動モードについて)
    - [起動モードの種類](#起動モードの種類)
    - [各起動モードにおける Activity インスタンスの生成条件](#各起動モードにおける-activity-インスタンスの生成条件)
    - [各起動モードで開始した Activity を再度起動した場合の挙動の違い](#各起動モードで開始した-activity-を再度起動した場合の挙動の違い)
  - [起動フラグについて](#起動フラグについて)


# タスクとバックスタック_new

## 起動モードと起動フラグ

起動モードとは、呼び出される側の Activity に定義される属性です。  
マニフェストファイルの `<android>` タグに `android:launchMode` 属性として定義します。

起動フラグとは、 `Activity` 起動時の `Intent` に `setFlag()` メソッドで定義する属性です。


## 起動モードについて

### 起動モードの種類

- standard (デフォルト値)
  - 毎回新しい Activity を生成する。
- singleTop
  - タスク内で、一番上に同じ Activity が既に存在していたら、同じ Activity は再作成しない。
  - ゆえに、スタックの一番上 ( TOP ) に同じ Acitivity が二つ以上存在しないことから、 singleTop という名前となっていると思われる。
- singleTask
  - デバイス内のどこかのタスクに対象の Acitivity が既に存在していれば、同じ Activity は再作成しない。
  - ゆえに、すべてのタスクの中で対象の Activity が一つだけ存在することから、 singleTask という名前となっていると思われる。
  - 対象の Activity が既に存在しており、その上に別の Activity が存在している場合は、別の Activity をすべて破棄する。
- singleInstance
  - デバイス内のどこかのタスクに対象の Acitivity が既に存在していれば、同じ Activity は再作成しない。
  - 対象の Activity 専用のタスクを生成し、そのタスクは、対象の Activity 以外をスタックすることはできない。


### 各起動モードにおける Activity インスタンスの生成条件

| Activity インスタンスの生成条件                                                                    | standard | singleTop | singleTask | singleInstance |
| -------------------------------------------------------------------------------------------------- | -------- | --------- | ---------- | -------------- |
| 常に新しいインスタンス(Activity)を生成する                                                         | ○        | -         | -          | -              |
| バックスタックの一番上に該当の Activity が存在しない場合のみインスタンス(Activity)を生成する       | -        | ○         | -          | -              |
| デバイス内に該当の Activity のインスタンスが存在していない場合のみインスタンス(Activity)を生成する | -        | -         | ○          | ○              |


### 各起動モードで開始した Activity を再度起動した場合の挙動の違い

`A -> B -> A` のような順番で Activity を開始した場合、  
2 回目の `A` の開始時に `B` の Activity がどうなるかの違いを以下に示します。

| B の取り扱い                                    | standard | singleTop | singleTask | singleInstance |
| ----------------------------------------------- | -------- | --------- | ---------- | -------------- |
| 破棄されない                                    | ○        | ○         | -          | -              |
| 破棄される                                      | -        | -         | ○          | -              |
| そもそも B が同じバックスタックに乗ることはない | -        | -         | -          | ○              |


## 起動フラグについて

- Intent.FLAG_ACTIVITY_SINGLE_TOP
  - 起動モード `singleTop` と同じ振る舞いをします。
- Intent.FLAG_ACTIVITY_NEW_TASK
  - 起動モード `singleTask` or `singleInstance` と同じ振る舞いをします。
  - 何も追加の指定をしなかった場合は `singleTask` として振る舞います。
  - Activity のアフィニティを空文字 ( "" ) にした場合は、 `singleInstance` として振る舞います。
- Intent.FLAG_ACTIVITY_MULTIPLE_TASK
  - FLAG_ACTIVITY_NEW_TASK を指定した場合だけ、このフラグを同時に指定することができます。
  - FLAG_ACTIVITY_NEW_TASK と同時に指定することで、 Activity がすでに存在している場合には、新しいタスク上に Activity が起動されるようになります。





