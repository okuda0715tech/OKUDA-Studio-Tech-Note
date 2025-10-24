- [CoroutineExceptionHandler](#coroutineexceptionhandler)
  - [コルーチン例外の処理](#コルーチン例外の処理)
  - [ハンドラーのないキャッチされない例外](#ハンドラーのないキャッチされない例外)


# CoroutineExceptionHandler

```kotlin
interface CoroutineExceptionHandler : CoroutineContext.Element
```

キャッチされない例外を処理するためのコルーチンコンテキストのオプション要素。

通常、キャッチされない例外は、 launch ビルダーを使用して作成されたルートコルーチンからのみ発生します。すべての子コルーチン (別のジョブのコンテキストで作成されたコルーチン) は、例外の処理を親コルーチンに委任し、親コルーチンも親に委任し、ルートまで同様に処理されるため、ルート以外のコンテキストにインストールされている CoroutineExceptionHandler は使用されません。

SupervisorJob で実行されるコルーチンは、例外を親に伝播せず、ルートコルーチンのように扱われます。

async を使用して作成されたコルーチンは、常にすべての例外をキャッチし、結果の Deferred オブジェクトでそれらを表すため、キャッチされない例外が発生することはありません。 **( 2024 年 7 月時点で、 Kotlin にバグがあるようで、 async が親コルーチンにキャッチされない例外を投げている模様)**


## コルーチン例外の処理

CoroutineExceptionHandler は、グローバルな「すべてをキャッチする」動作の最後の手段です。 CoroutineExceptionHandler で例外から回復することはできません。ハンドラーが呼び出されたときに、コルーチンは対応する例外ですでに完了しています。通常、ハンドラーは例外をログに記録したり、何らかのエラーメッセージを表示したり、アプリケーションを終了したり、再起動したりするために使用されます。

コードの特定の部分で例外を処理する必要がある場合は、コルーチン内の対応するコードの周りに try/catch を使用することをお勧めします。この方法では、例外によるコルーチンの完了を防止したり (例外がキャッチされた) 、操作を再試行したり、その他の任意のアクションを実行したりできます。

```kotlin
scope.launch { // launch child coroutine in a scope
    try {
         // do something
    } catch (e: Throwable) {
         // handle exception
    }
}
```


## ハンドラーのないキャッチされない例外

ハンドラーがインストールされていない場合、例外は次のように処理されます:

- 例外が CancellationException の場合、これらの例外はコルーチンをキャンセルするために使用されるため、無視されます。
- それ以外の場合、コンテキストにジョブがある場合は、 Job.cancel が呼び出されます。
- それ以外の場合は、最後の手段として、例外はプラットフォーム固有の方法で処理されます:
- JVM では、 ServiceLoader で見つかった CoroutineExceptionHandler のすべてのインスタンスと、現在のスレッドの Thread.uncaughtExceptionHandler が呼び出されます。
- ネイティブでは、例外によりアプリケーション全体がクラッシュします。
- JS では、例外はコンソール API を介してログに記録されます。

CoroutineExceptionHandler は任意のスレッドから呼び出すことができます。

