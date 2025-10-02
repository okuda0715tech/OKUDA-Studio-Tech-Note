- [@AssistedInject の使い方](#assistedinject-の使い方)
  - [基本構成](#基本構成)
  - [ファクトリーインターフェースを定義する](#ファクトリーインターフェースを定義する)
  - [使用側（DIコンテナから取得）](#使用側diコンテナから取得)
  - [ポイントまとめ](#ポイントまとめ)


# @AssistedInject の使い方

`@AssistedInject` は、 Jetpack Compose では非推奨らしいですが、参考までに使い方を説明します。


## 基本構成

```kotlin
class MyClass @AssistedInject constructor(
    private val repository: MyRepository, // DI で注入
    @Assisted private val userId: String   // 実行時に渡す
) {
    fun greet() = "Hello $userId"
}
```


## ファクトリーインターフェースを定義する

```kotlin
@AssistedFactory
interface MyClassFactory {
    fun create(userId: String): MyClass
}
```


## 使用側（DIコンテナから取得）

```kotlin
@AndroidEntryPoint
class MyActivity : AppCompatActivity() {

    @Inject lateinit var factory: MyClassFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userId = "user123"
        val myClass = factory.create(userId)
        Log.d("MyClass", myClass.greet())
    }
}
```


## ポイントまとめ

- @AssistedInject → コンストラクタに付ける
- @Assisted → 実行時に手動で渡したい引数に付ける
- @AssistedFactory → create() メソッドの定義必須（引数は @Assisted と一致させる）



