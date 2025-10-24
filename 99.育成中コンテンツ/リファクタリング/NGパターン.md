<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [NGパターン](#ngパターン)
  - [値に「読んだだけではわからない意味」を持たせてしまっている](#値に読んだだけではわからない意味を持たせてしまっている)
<!-- TOC END -->


# NGパターン

## 値に「読んだだけではわからない意味」を持たせてしまっている

例えば、 API を時間になったら定期的に実行するプログラムがあるとする。  
API の実行をスケジュール登録するテーブルとその実行結果を登録するテーブルが同じテーブルであるとする。  
テーブルには、 `statusCode` というカラムを保持しており、 API を実行した時の  
サーバからのレスポンスのステータスコードを格納するとする。

この場合、 `statusCode == null` の場合は、 「 API を未実行」 の状態であるわけだが、  
画面に実行状況 (未実行、実行成功、実行失敗) を表示する際に、  
`statusCode == null` なら 「未実行」 と表示するべきではない。

なぜなら、後からプログラムを読み返した時に、 「 `statusCode == 0` なら未実行という状態で良いのかな？」  
とプログラマに迷いが生じるためである。

もし、

```java
SomethingEnum status;
int statusCode;

// ここで statusCode に代入を行うとする
// statusCode = xxx;

if (statusCode == 0) {
    status = SomethingEnum.NOT_EXECUTED
} else if (statusCode >= 200 && statusCode < 300){
    statusCode = SomethingEnum.SUCCESS
} else {
    statusCode = SomethingEnum.FAILED
}
```

というようにプログラムが組まれていたら、もし、プログラマが今後、実行状況を判断する必要が生じた場合に  
「実行状況は `statusCode` の値で判断すれば良い」 と判断することができる。  
しかし、 `statusCode` という項目が存在せず、 `status` という項目を見ただけでは、  
`status` が `0` であることは、 `0` という値であることしか意味しておらず、  
`0 == 未実行` なのかどうかは、保証されていないのである。  
もしかしたら、別のプログラマが実行失敗時に `status` に `0` を代入している可能性もはらんでいる訳である。
