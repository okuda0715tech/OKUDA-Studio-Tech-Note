- [Thread-Per-Message と Worker Thread の違い](#thread-per-message-と-worker-thread-の違い)
  - [仕組み的な違い](#仕組み的な違い)
  - [Kotlin のコルーチンにおける違い](#kotlin-のコルーチンにおける違い)


# Thread-Per-Message と Worker Thread の違い

## 仕組み的な違い

仕組み的な違いは、処理ごとに新しいスレッドを生成するか、それとも、既存のスレッドを再利用するかの違いです。

- Thread-Per-Message
  - 新しいメッセージ (命令 or リクエスト) ごとに、新しいスレッドを生成する。
- Worker Thread
  - 既に生成され、プールされているスレッドを使用して、処理を行う。


## Kotlin のコルーチンにおける違い

Kotlin のコルーチンにおいては、 Thread-Per-Message パターンと Worker Thread パターンの違いはありません。

なぜなら、コルーチンで Thread-Per-Message パターンを実装すると、実際には、 Coroutine-Per-Message パターンとなり、メッセージ毎にコルーチンを生成することになります。コルーチンの言語設計として、開発者は、コルーチン生成時に、「新しいスレッドを生成するか？」それとも「スレッドプールのスレッドを再利用するか？」を気にすることがないようになっています。開発者が、コルーチン生成時に意識すべきことは、 Dispatchers.Default、Dispatchers.IO、Dispatchers.Main などの論理的な分類を選ぶだけです。

よって、コルーチンにおいては、  Thread-Per-Message パターンと Worker Thread パターンになるかは、気にしなくて良いのです。

