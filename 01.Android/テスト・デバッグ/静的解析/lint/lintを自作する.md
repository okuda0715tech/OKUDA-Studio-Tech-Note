<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [lint を自作する](#lint-を自作する)
  - [自作に使用するクラス](#自作に使用するクラス)
<!-- TOC END -->


# lint を自作する

## 自作に使用するクラス

- Detector
  - コードを解析し、問題のあるコードを検出する実装そのもの
- Issue
  - Detector によって報告されるプロジェクト内の問題
- Scope
  - Detector が解析する対象のファイルを指定する
- Scanner
  - ファイルの解析を実行する。
  - 問題を検出した時のコールバックも持っている。
- UAST と PSI
  - UAST ・・・ Unified AST の略
  - ソースコードの構造をツリー構造で表現したもの。
  - ソースコード内の要素を特定するのに利用する。
