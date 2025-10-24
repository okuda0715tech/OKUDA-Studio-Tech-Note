- [companion object とクラス内 object の違い](#companion-object-とクラス内-object-の違い)


# companion object とクラス内 object の違い

| 観点                         | companion object | object                 |
| ---------------------------- | ---------------- | ---------------------- |
| インスタンスの生成タイミング | 包含クラス参照時 | 自分自身が参照された時 |
| 名前の省略可否               | 可               | 不可                   |
| 定義可能な数                 | 一つまで         | 上限なし               |
|                              |                  |                        |
