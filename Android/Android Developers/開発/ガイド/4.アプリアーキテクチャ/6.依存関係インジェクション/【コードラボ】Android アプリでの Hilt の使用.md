- [【コードラボ】Android アプリでの Hilt の使用](#コードラボandroid-アプリでの-hilt-の使用)
  - [1. はじめに](#1-はじめに)
  - [2. 設定方法](#2-設定方法)
  - [3. プロジェクトへの Hilt の追加](#3-プロジェクトへの-hilt-の追加)
    - [コンテナについて](#コンテナについて)
      - [インスタンスの再利用](#インスタンスの再利用)
  - [4. アプリでの Hilt](#4-アプリでの-hilt)
  - [5. Hilt を使用したフィールド注入](#5-hilt-を使用したフィールド注入)
    - [5-1. コンテナを作成する](#5-1-コンテナを作成する)
    - [5-2. インスタンスを注入したいプロパティを指定する](#5-2-インスタンスを注入したいプロパティを指定する)
    - [5-3. インスタンスの生成方法を指定する](#5-3-インスタンスの生成方法を指定する)
    - [5-4. 【参考】推移的依存関係に注意](#5-4-参考推移的依存関係に注意)
    - [5-5. 【参考】 @Inject アノテーションの二つの意味](#5-5-参考-inject-アノテーションの二つの意味)
      - [1. Activity / Fragment などの `lateinit var` に付与する](#1-activity--fragment-などの-lateinit-var-に付与する)
      - [2. プライマリコンストラクタ `constructor()` に付与する。](#2-プライマリコンストラクタ-constructor-に付与する)
    - [【参考】依存関係ツリー](#参考依存関係ツリー)
  - [6. インスタンスのスコープ（コンテナ）を設定する](#6-インスタンスのスコープコンテナを設定する)
  - [7. Hilt モジュール](#7-hilt-モジュール)
    - [7-1. Hilt モジュールとは](#7-1-hilt-モジュールとは)
    - [7-2. Hilt モジュールを作成する](#7-2-hilt-モジュールを作成する)
    - [7-3. Hilt モジュールのスコープを設定する](#7-3-hilt-モジュールのスコープを設定する)
    - [7-4. インスタンスの生成方法を指定する](#7-4-インスタンスの生成方法を指定する)
      - [同じ戻り値型の関数を複数定義したい場合](#同じ戻り値型の関数を複数定義したい場合)
    - [7-5. インスタンスを複数生成せずに、シングルトンにする。](#7-5-インスタンスを複数生成せずにシングルトンにする)
    - [7-6. 依存関係として使用するオブジェクトのスコープを設定する](#7-6-依存関係として使用するオブジェクトのスコープを設定する)
  - [8. @Binds を使用したインターフェースの提供](#8-binds-を使用したインターフェースの提供)
    - [注意点](#注意点)
    - [8-1. @Binds とは](#8-1-binds-とは)
    - [8-2. @Binds アノテーションの使い方](#8-2-binds-アノテーションの使い方)
      - [注意点](#注意点-1)
      - [@Binds アノテーションの使用方法](#binds-アノテーションの使用方法)
    - [8-3. @Binds で定義された依存関係注入を使用する側](#8-3-binds-で定義された依存関係注入を使用する側)
    - [【参考】 @Binds は @Provides でも実装できる](#参考-binds-は-provides-でも実装できる)
  - [9. 識別子](#9-識別子)
    - [9-1. 識別子の使用方法](#9-1-識別子の使用方法)
  - [10. UI テスト](#10-ui-テスト)
  - [11. @EntryPoint アノテーション](#11-entrypoint-アノテーション)
    - [11-1. @EntryPoint アノテーションとは](#11-1-entrypoint-アノテーションとは)
    - [11-2. @EntryPoint アノテーションの使用方法](#11-2-entrypoint-アノテーションの使用方法)
    - [12. @Provides と @Binds と @EntryPoint の違い](#12-provides-と-binds-と-entrypoint-の違い)


# 【コードラボ】Android アプリでの Hilt の使用

## 1. はじめに

省略


## 2. 設定方法

省略


## 3. プロジェクトへの Hilt の追加

### コンテナについて

コンテナには以下の役割があります。

- 依存関係を把握する
- インスタンスの生成方法を把握する
- 適切なコンテナを選択することで、インスタンスの生存期間を管理する。

コンテナは、型をインプットとして、そのインスタンスを返すメソッドを提供します。


#### インスタンスの再利用

インスタンスを返すメソッドは、デフォルトでは、常に異なるインスタンスを返しますが、同じインスタンスも返すことができます。インスタンスを返すメソッドにスコープを設定すると、そのスコープ内では、同じインスタンスを返します。


## 4. アプリでの Hilt

Hitl ライブラリを使用するには、常に、カスタムアプリケーションクラスに @HiltAndroidApp アノテーションを付与する必要があります。

```kotlin
@HiltAndroidApp
class LogApplication : Application() {
    // ...
}
```


## 5. Hilt を使用したフィールド注入

### 5-1. コンテナを作成する

LogsFragment のプロパティに対して依存性を注入するには、まず、 LogsFragment に @AndroidEntryPoint アノテーションを付与します。

```kotlin
@AndroidEntryPoint
class LogsFragment : Fragment() {
    // ...
}
```

Android クラスに `@AndroidEntryPoint` アノテーションを付けると、 Android のライフサイクルを持つ各クラスのライフサイクルに沿った依存関係コンテナが作成されます。これで、アノテーションを付与した Android クラスにインスタンスを注入できるようになりました。

現在、 Hilt は以下の Android クラスをサポートしています。

| Hilt がサポートしている Android クラス | 付与するアノテーション |
| -------------------------------------- | ---------------------- |
| Application                            | @HiltAndroidApp        |
| ViewModel                              | @HiltViewModel         |
| Activity                               | @AndroidEntryPoint     |
| Fragment                               | @AndroidEntryPoint     |
| View                                   | @AndroidEntryPoint     |
| Service                                | @AndroidEntryPoint     |
| BroadcastReceiver                      | @AndroidEntryPoint     |


### 5-2. インスタンスを注入したいプロパティを指定する

注入したいフィールド (以下の例では logger と dateFormatter プロパティ) に `@Inject` アノテーションを付けることで、そのプロパティに Hilt で依存性を注入することを指示します。 (ただし、これだけでは、まだ、インスタンスは生成できません。次のセクションで説明する 「インスタンスの生成方法」 を定義する必要があります。)

```kotlin
@AndroidEntryPoint
class LogsFragment : Fragment() {

    @Inject lateinit var logger: LoggerLocalDataSource
    @Inject lateinit var dateFormatter: DateFormatter

}
```

これは、 **フィールド注入** と呼ばれます。

Hilt によって注入されたフィールドを非公開にすることはできません。

Fragment のコンテナで作成されるインスタンスは、 `Fragment.onAttach()`
で生成され、 `Fragment.onDestroy()` で破棄されます。これは、どの Android コンポーネントでコンテナを生成するかによって異なります。詳しくは、 [コンポーネントのライフタイム](./3.Hilt%20を使用した依存関係挿入.md/#コンポーネントのライフタイム) を参照してください。


### 5-3. インスタンスの生成方法を指定する

コンストラクタを使用してインスタンスを生成するように Hilt に指示したい場合には、コンストラクタの前に @Inject アノテーションを付与します。

```kotlin
// 変更前
class DateFormatter { ... }
// 変更後
class DateFormatter @Inject constructor() { ... }
```

@Inject アノテーションを付与する場合は、 constructor キーワードが必要になります。

このように 「インスタンスをどのように生成するのか」 という情報を **「バインディング」** と呼びます。

この方法では、インジェクションが要求される度に新しいインスタンスを生成して返します。前回と同じインスタンスを返すことはしません。前回と同じインスタンスを返すには、次のセクションで、スコープの設定方法を学びます。


### 5-4. 【参考】推移的依存関係に注意

推移的依存関係 (※ 1 ) が存在する場合、 Hilt は、間接的に依存しているオブジェクトの生成方法についても知っている必要があります。

(※ 1 )  
推移的依存関係とは、クラス A , B , C について、 A -> B , B -> C という依存関係があった場合、 A が B へ依存していることにより、間接的に C にも依存している状態のことを言います。


### 5-5. 【参考】 @Inject アノテーションの二つの意味

`@Inject` アノテーションを使用する場面は、 2 つあります。

1. Activity / Fragment などの `lateinit var` に付与する。
2. プライマリコンストラクタ `constructor()` に付与する。


#### 1. Activity / Fragment などの `lateinit var` に付与する

インスタンスの生成方法を指定できないクラス ( Activity や Fragment など) のメンバプロパティに対して、 `@Inject lateinit var` のようにアノテーションを使用します。

この場合の @Inject は、 「 Hilt でインスタンスを生成して、そのプロパティに格納してほしい」 という意味になります。

簡単にいうと、生成したインスタンスの受け取り側を意味します。


#### 2. プライマリコンストラクタ `constructor()` に付与する。

インスタンスの生成方法を指定できるクラスの中でも、特に、プライマリコンストラクタでインスタンスを生成するクラスに対して、 `@Inject constructor(...)` のようにアノテーションを使用します。

この場合の @Inject は、 「このクラスのインスタンスを生成する際には、プライマリコンストラクタで生成してください。と Hilt に伝える」 という意味になります。

簡単にいうと、 Hilt がそのクラスのインスタンスを生成できるようになることを意味します。

インスタンスの生成方法を指定できるクラスの中で、プライマリコンストラクタではインスタンスを生成したくない場合 (例えば、ビルダーパターンでインスタンスを生成したい場合など) は、 [Hilt モジュール ( @Provides または @Binds ) を使用]() して、インスタンスの生成方法を指定します。

インスタンスの生成方法を指定できるクラスの場合、そのメンバプロパティにアノテーションを付与して `@Inject lateinit var` のようにすることは避けてください。この方法は、 Activity や Fragment など、特定のクラス内でのみ有効な方法であり、通常のクラスでは、自動的に Hilt がインスタンスを生成して、メンバプロパティに格納してくれることはありません。通常のクラスで、 Hilt に自動的にインスタンスを生成してもらうには、 `@Inject constructor` 、もしくは、 [Hilt モジュールを使用した方法](#7-hilt-モジュール) で、推移的依存関係を定義してください。


### 【参考】依存関係ツリー

Hilt は、内部に依存関係を表すツリーを構築します。

Hilt に、あるクラスのインスタンスの生成方法を伝えると、そのクラスが依存しているクラスを推移的に特定します。 Hilt がインスタンスを生成するためには、推移的な依存関係にある全てのクラスのインスタンスの生成方法を知っている必要があります。

つまり、 `@Inject constructor()` 、もしくは、 Hilt モジュールを使用して、推移的依存関係にある全てのクラスのインスタンスの生成方法を、 Hilt に伝える必要があります。そうすれば、 Hilt は、インスタンスを生成することが可能になります。


## 6. インスタンスのスコープ（コンテナ）を設定する

[Hilt コンポーネントのスコープ](./3.Hilt%20を使用した依存関係挿入.md/#hilt-コンポーネントのスコープ) に詳しく記載しているため、そちらを参照してください。


## 7. Hilt モジュール

### 7-1. Hilt モジュールとは

Hilt モジュールを定義すると、インスタンスの生成方法 (バインディング) を定義することが可能です。これにより、コンストラクタで注入を定義できないクラス (プロジェクトに含まれていないインターフェースやクラスなど) にバインディングを定義することができます。


### 7-2. Hilt モジュールを作成する

Hilt モジュールを作成するには、クラスに `@Module` アノテーションを付与します。 @Module は、このオブジェクトが Hilt モジュールであることを宣言します。

```kotlin
@Module
object AppModule {

}
```


### 7-3. Hilt モジュールのスコープを設定する

Hilt モジュール自身 (以下の例では、 AnalyticsModule オブジェクト) のスコープ (コンテナ) を指定するには、 `@InstallIn(XxxComponent)` アノテーションを使用します。スコープを設定することにより、この Hilt モジュールが利用可能なスコープが決定します。

Hilt コンポーネントとコンテナの対応表は以下の通りです。

| Hilt コンポーネント       | コンテナ                                          |
| ------------------------- | ------------------------------------------------- |
| SingletonComponent (※ 1 ) | Application                                       |
| ActivityRetainedComponent | なし                                              |
| ViewModelComponent        | ViewModel                                         |
| ActivityComponent         | Activity                                          |
| FragmentComponent         | Fragment                                          |
| ViewComponent             | View                                              |
| ViewWithFragmentComponent | @WithFragmentBindings アノテーションが付いた View |
| ServiceComponent          | Service                                           |

(※ 1 )  
コードラボの説明では、 Application コンテナは、 `ApplicationComponent` と記述されていますが、これだとエラーになります。正しくは、 `SingletonComponent` のようです。

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

}
```


### 7-4. インスタンスの生成方法を指定する

Hilt モジュール内に、 `@Provides` アノテーションを付与した関数を定義します。この関数には、 Hilt がインスタンスを生成する方法を定義します。これにより、 Hilt は該当のインスタンスの生成方法を知ることが出来ます。

@Provides アノテーションが付与された関数の構造は以下の通りです。

- 関数名
  - 任意の関数名を指定できますが、 `provide + クラス名` にするのが一般的かと思われます。
- 関数の本体
  - インスタンスの生成方法を定義します。
  - その型のインスタンスが要求されるたびに、関数が実行されます。
- 関数の戻り値の型
  - どのクラスをインスタンス化するのかを表します。
- 関数の引数
  - インスタンスを生成する際に必要となるオブジェクトを渡せるようにます。

```kotlin
@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    fun provideLogDao(database: AppDatabase): LogDao {
        return database.logDao()
    }

    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "logging.db"
        ).build()
    }
}
```

上記のコードでは、 LogDao のインスタンスを提供する際に、 database.logDao() を実行する必要があることを Hilt に指示しています。推移的な依存関係として AppDatabase があるため、その型のインスタンスを提供する方法を Hilt に指示する必要があります。

AppDatabase は、 Room によって生成されるため、プロジェクトに含まれないクラスです。したがって、データベースインスタンスを ServiceLocator クラスで構築するのと同様に、 @Provides 関数を使用してこのクラスを提供することもできます。

Kotlin では、@Provides 関数のみを含むモジュールを object クラスにすることができます。これにより、プロバイダは最適化され、生成されたコードに、ほぼ埋め込まれます。


#### 同じ戻り値型の関数を複数定義したい場合

@Provides 関数は特殊な関数であり、関数名を指定して関数を呼び出すわけではありません。その代わりに、関数の戻り値の型を使用して、呼び出す関数を判別します。

ということは、関数の戻り値の型が同じ場合は、どうすれば良いのでしょうか？その場合に登場するのが `@Named("xxx")` アノテーションです。

`@Named("")` アノテーションの引数に指定した名前で、呼び出す関数を判別します。具体的には、以下の例を参照してください。

関数の定義側：

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object UrlModule {

    @Provides
    @Named("prod")
    fun provideProdUrl(): String = "https://prod.example.com"

    @Provides
    @Named("dev")
    fun provideDevUrl(): String = "https://dev.example.com"
}
```

関数の呼び出し側：

```kotlin
class MyRepository @Inject constructor(
    @Named("prod") private val prodUrl: String,
    @Named("dev") private val devUrl: String
) {
    fun printUrls() {
        println("Prod URL: $prodUrl")
        println("Dev URL: $devUrl")
    }
}
```


### 7-5. インスタンスを複数生成せずに、シングルトンにする。

インスタンスを複数生成せずに、シングルトンにしたい場合には、 @Provides アノテーションが付与された関数に、 @Singleton アノテーションを付与します。

```kotlin
@Module
object DatabaseModule {

    @Provides
    fun provideLogDao(database: AppDatabase): LogDao {
        return database.logDao()
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "logging.db"
        ).build()
    }
}
```


### 7-6. 依存関係として使用するオブジェクトのスコープを設定する

通常、 @Provides アノテーションを付与した関数によって、生成されるインスタンスのスコープは、 Hilt モジュールの定義で指定したスコープ ( @InstallIn(Xxxx::class) ) になります。それよりも狭いスコープを指定したい場合には、 @Provides 関数ごとにスコープを設定することが可能です。

使用可能なアノテーションの一覧については、 [スコープの設定に使用するアノテーションの一覧](./3.Hilt%20を使用した依存関係挿入.md/#スコープの設定に使用するアノテーションの一覧) を参照してください。



## 8. @Binds を使用したインターフェースの提供

### 注意点

`@Bind` ではなく、 `@Binds` が正しいアノテーションなので、注意してください。


### 8-1. @Binds とは

インターフェースや抽象クラスの型のプロパティに対して、それを実装したクラスのインスタンスを注入したい場合に、 @Binds アノテーションを使用します。

例えば、以下のようなケースが考えられます。

```kotlin
// インターフェース
// (自分で修正できないインターフェースでも問題ありません)
interface AnalyticsService {
    fun trackEvent(eventName: String)
}

// 実装クラス
// (自分で修正ができる実装クラスであること)
class AnalyticsServiceImpl: AnalyticsService {
    override fun trackEvent(eventName: String) {
        // ...
    }
}

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // Hilt で自動的に AnalyticsServiceImpl のインスタンスを提供したい。
    @Inject lateinit var analyticsService: AnalyticsService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ここで AnalyticsService のメソッドを使用
        analyticsService.trackEvent("MainActivity Created")
    }
}
```


### 8-2. @Binds アノテーションの使い方

- @Binds アノテーションは Hilt モジュール内の関数に付与します。
- @Binds アノテーションは抽象関数に付与します。


#### 注意点

@Binds アノテーションを付与した関数と @Provides アノテーションを付与した関数を同じクラスに配置することはできません。

理由は以下の制約に違反するためです。

- @Binds を付与した関数は、抽象関数でなければいけない。 ( Hilt の仕様)
- 抽象関数を含むクラスは、抽象クラスでなければいけない。 ( Kotlin の仕様)
- @Provides を付与した関数は、具象関数でなければいけない。 ( Hilt の仕様)
- @Provides を付与した関数を含むクラスは、具象クラスでなければいけない。 ( Hilt の仕様)


#### @Binds アノテーションの使用方法

@Binds アノテーションが付与された関数の構造は以下の通りです。

- 関数の本体
  - なし
- 関数の戻り値の型
  - どのインターフェース (実際にはそのインターフェースを実装したクラス) をインスタンス化するのかを定義します。
- 関数の引数
  - 戻り値のインターフェースを実装したクラスを渡します。
  - 引数に渡すクラスについてもバインディングを定義する必要があります。

```kotlin
// インターフェース
interface AnalyticsService {
    fun trackEvent(eventName: String)
}

// 実装クラス
class AnalyticsServiceImpl @Inject constructor() : AnalyticsService {
    override fun trackEvent(eventName: String) {
        Log.d("AnalyticsService", "Tracking event: $eventName")
    }
}

// モジュールでのバインディング
@Module
@InstallIn(SingletonComponent::class) // スコープを指定
abstract class AnalyticsModule {

    @Binds
    abstract fun bindAnalyticsService(
        analyticsServiceImpl: AnalyticsServiceImpl
    ): AnalyticsService
}
```


### 8-3. @Binds で定義された依存関係注入を使用する側

この例では、Hilt を使用して AnalyticsService が必要とされる場所で、AnalyticsServiceImpl のインスタンスを生成して返します。

```kotlin
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // Hiltが自動的にAnalyticsServiceImplのインスタンスを提供
    @Inject lateinit var analyticsService: AnalyticsService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ここでAnalyticsServiceのメソッドを使用
        analyticsService.trackEvent("MainActivity Created")
    }
}
```


### 【参考】 @Binds は @Provides でも実装できる

@Binds アノテーションを使用して依存性注入を実装するコードは、 @Provides アノテーションを使用しても、実装できます。ただし、 @Provides を使用すると、一部、自分でインスタンスを生成することになるため、不要な依存関係が残ってしまします。

上記の @Binds の例を @Provides で書き直すと以下のようになります。

```kotlin
@Module
@InstallIn(SingletonComponent::class)
class AnalyticsModule {

    @Provides
    fun provideAnalyticsService(): AnalyticsService {
        // 自分でインスタンスを生成してしまっている。
        return AnalyticsServiceImpl()
    }
}
```

単純なインターフェースと実装クラスをマッピングしたいだけの場合は、 @Binds を使用するようにして、 @Binds では対応できない場合のみ、 @Provides を使用するようにしましょう。


## 9. 識別子

同じクラスに対して、複数のバインディングが定義されている場合、 Hilt はどのバインディングを使用してよいかわからないため、エラーになります。その場合、修飾子を使用することで、どのバインディングを使用するのかを指定することが可能です。これにより、クラスの使用場所によって、どのようにインスタンスを生成するのかを使い分けることが可能になります。


### 9-1. 識別子の使用方法

識別子を使用する方法は、ほぼアノテーションのみで実装が完結します。次に使用方法を示します。

```kotlin
// *****************************
// @Qualifier アノテーションは、
// カスタムアノテーションを定義します。
// *****************************

@Qualifier
annotation class InMemoryLogger

@Qualifier
annotation class DatabaseLogger


// *****************************
// 各バインディングにカスタムアノテーションで名前を付けます。
// *****************************

@InstallIn(ApplicationComponent::class)
@Module
abstract class LoggingDatabaseModule {

    @DatabaseLogger
    @Singleton
    @Binds
    abstract fun bindDatabaseLogger(impl: LoggerLocalDataSource): LoggerDataSource
}

@InstallIn(ActivityComponent::class)
@Module
abstract class LoggingInMemoryModule {

    @InMemoryLogger
    @ActivityScoped
    @Binds
    abstract fun bindInMemoryLogger(impl: LoggerInMemoryDataSource): LoggerDataSource
}


// *****************************
// アノテーションでどのバインディングを使用するかを指示します。
// *****************************

@AndroidEntryPoint
class LogsFragment : Fragment() {

    @InMemoryLogger
    @Inject lateinit var logger: LoggerDataSource
    ...
}
```


## 10. UI テスト

テストについての知見がないため後回しとする。

[10. UI テスト](https://developer.android.com/codelabs/android-hilt?hl=ja#9)


## 11. @EntryPoint アノテーション

### 11-1. @EntryPoint アノテーションとは

@EntryPoint アノテーションは、 Hilt がサポートされていないクラスで Hilt を使用できるようにするためのアノテーションです。

アノテーションが指定されたクラスのライフサイクルに連動するスコープのコンテナが作成されます。


### 11-2. @EntryPoint アノテーションの使用方法

- @EntryPoint アノテーションは、インターフェースの定義に付与します。
- @EntryPoint アノテーションを付与したインターフェースには、 @InstallIn アノテーションも付与する必要があります。
  - @InstallIn の引数には、エントリーポイントで接続したいコンテナを指定します。

```kotlin
class LogsContentProvider: ContentProvider() {

    // エントリーポイントは、 Application コンテナにアクセスしたいので、
    // @InstallIn アノテーションの引数に ApplicationComponent を指定します。
    @InstallIn(ApplicationComponent::class)
    @EntryPoint
    interface LogsContentProviderEntryPoint {
        // LogDao クラスは、どこかで、プライマリコンストラクターや
        // Hilt モジュールで、インスタンスの生成方法が定義されている必要があります。
        // エントリーポイントには、インスタンスの生成方法は定義する役割はありません。
        fun logDao(): LogDao
    }

    private fun getLogDao(appContext: Context): LogDao {
        val hiltEntryPoint = EntryPointAccessors.fromApplication(
            // @InstallIn の引数と同じコンテナを指定する？
            appContext,
            // エントリーポイント
            LogsContentProviderEntryPoint::class.java
        )
        // Application コンテナに定義されている LogDao の
        // バインディングを使用してインスタンスを取得する？
        return hiltEntryPoint.logDao()
    }
}
```


### 12. @Provides と @Binds と @EntryPoint の違い

- @Provides
  - インスタンスの生成方法を定義します。
  - インスタンスの生成方法を自分で定義することができるため、任意の生成方法を定義することが可能です。
- @Binds
  - インスタンスの生成方法を定義します。
  - ただし、簡単なバインドによる生成方法しかサポートしていません。
  - 具体的には、 「インターフェース or 抽象クラス」 と 「具象クラス」 をマッピングする場合にのみ使用します。
  - 上記の場合に @Binds を使用すると @Provides を使用するよりもボイラープレートコードが減ります。
- @EntryPoint
  - インスタンスの生成方法は定義しません。
  - @EntryPoint アノテーションは、 Hilt によるインスタンスの生成が可能なスコープを定義します。
  - Hilt によるインスタンス生成を可能にするためには、エントリーポイントの内部でコードを記述する必要があります。
  - 通常、 Application / Activity / ViewModel などは、 @HiltAndroidApp / @AndroidEntryPoint / @HiltViewModel アノテーションによって、エントリーポイント化することが可能です。
  - ただし、 ContentProvider など、アノテーションでは、エントリーポイント化できないクラスも存在します。
  - そのようなクラスに、エントリーポイントを提供する役割があります。


