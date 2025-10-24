

# JUnit 4からJUnit 5に移行する：重要な違いと利点

## 改善点と新機能が魅力のJUnit 5

著者：Brian McGlauflin

2020年4月6日

[JUnit 5](https://junit.org/junit5/) は、 JUnit フレームワークの強力かつ柔軟なアップデートであり、テスト・ケースの整理、および、記述を行うためや、テスト結果の理解に役立てるためのさまざまな改善と新機能が提供されています。JUnit 5へのアップデートは、すばやく簡単です。プロジェクトの依存性を更新し、新機能を使い始めるだけです。

しばらくJUnit 4を使っていた方は、テストの移行を大変な作業だと感じるかもしれません。朗報なのは、おそらくテストの変換がまったく必要ないという点です。JUnit 5ではVintageライブラリを使ってJUnit 4テストを実行できます。

とは言うものの、新しいテストをJUnit 5で書き始めるべき4つの確かな理由を次に示します。

- JUnit 5では、ラムダ関数などのJava 8以降の機能を活用することで、テストが強力になるとともに、メンテナンスしやすくなっています。
- JUnit 5には、テストの記述、整理、実行に、とても便利ないくつかの新機能が追加されています。たとえば、テストの表示名がわかりやすくなり、テストを階層的に整理できるようになっています。
- JUnit 5は複数のライブラリで構成されているため、必要な機能だけがプロジェクトにインポートされます。 Maven や Gradle などのビルド・システムを使えば、適切なライブラリを含めるのは簡単です。
- JUnit 5では、複数の拡張機能を同時に使用できます。これは JUnit 4 では不可能でした（一度に使えるランナーは 1 つだけでした）。つまり、 Spring 拡張機能と他の拡張機能（独自のカスタム拡張機能など）を容易に組み合わせることができます。

JUnit 4からJUnit 5への切り替えはとても簡単です。この点は、既存の JUnit 4 テストがある場合でも変わりません。ほとんどの組織では、新機能が必要な場合を除き、古い JUnit テストをJUnit 5に変換する必要はありません。新機能が必要な場合は、以下の手順に従います。

1. [ライブラリとビルド・システムを JUnit 4 から JUnit 5 にアップデートします。](https://junit.org/junit5/docs/current/user-guide/#overview-getting-started) 既存のテストを実行できるように、テスト・ランタイム・パスに junit-vintage-engine アーティファクトを含めます。
2. 新しいJUnit 5コンストラクトを使って、新しいテストの構築を始めます。
3. （省略可能）JUnitテストをJUnit 5に変換します。


## 重要な違い

JUnit 5テストは、ほとんどJUnit 4テストと同じように見えますが、認識しておくべき違いがいくつかあります。

**インポート** ： JUnit 5 では、アノテーションとクラスに新しい `org.junit.jupiter` パッケージが使われています。たとえば、 `org.junit.Test` は、 `org.junit.jupiter.api.Test` になっています。

**アノテーション** ： @Test アノテーションにはパラメータがなくなり、各パラメータは関数に移行しています。たとえば、テストで例外のスローが想定される場合、JUnit 4では次のように記述します。

```java
@Test(expected = Exception.class)
public void testThrowsException() throws Exception {
    // ...
}
```

JUnit 5 では、次のように変更されています。

```java
@Test
void testThrowsException() throws Exception {
    Assertions.assertThrows(Exception.class, () -> {
        //...
    });
}
```

タイムアウトも同様に変更されています。次に示すのは、 JUnit 4 での例です。

```java
@Test(timeout = 10)
public void testFailWithTimeout() throws InterruptedException {
    Thread.sleep(100);
}
```

JUnit 5 では、次のように変更されています。

```java
@Test
void testFailWithTimeout() throws InterruptedException {
    Assertions.assertTimeout(Duration.ofMillis(10), () -> Thread.sleep(100));
}
```

変更されたその他のアノテーションは次のとおりです。

- @Before は @BeforeEach となっています。
- @After は @AfterEach となっています。
- @BeforeClass は @BeforeAll となっています。
- @AfterClass は @AfterAll となっています。
- @Ignore は @Disabled となっています。
- @Category は @Tag となっています。
- @Rule と @ClassRule は、なくなっています。代わりに @ExtendWith と @RegisterExtension を使用します。

**アサーション** ： JUnit 5 のアサーションは、 org.junit.jupiter.api.Assertions に含まれるようになっています。 assertEquals() や assertNotNull() など、ほとんどの一般的なアサーションは、以前と同じように見えますが、いくつかの違いがあります。

- エラー・メッセージが最後の引数になっています。たとえば、assertEquals("my message", 1, 2)はassertEquals(1, 2, "my message")となっています。
- ほとんどのアサーションで、エラー・メッセージを構築するラムダを受け取るようになっています。このラムダが呼ばれるのは、アサーションに失敗した場合のみです。
- assertTimeout() と assertTimeoutPreemptively() は、 @Timeout アノテーションに代わるものです ( JUnit 5 にも @Timeout アノテーションは存在しますが、 JUnit 4 とは動作が異なります) 。
- 後述しますが、いくつかの新しいアサーションが追加されています。

なお、お好みであればJUnit 5テストでJUnit 4のアサーションを継続して使用できることに注意してください。

**前提条件** ： 前提条件は org.junit.jupiter.api.Assumptions に移動しました。 

同じ前提条件が存在していますが、 BooleanSupplier や、条件との照合を行う [Hamcrest Matcher](http://hamcrest.org/) もサポートされるようになっています。条件を満たした場合のコード実行に、ラムダ ( Executable 型) を使用できます。

次に示すのは、 JUnit 4 での例です。

```java
@Test
public void testNothingInParticular() throws Exception {
    Assume.assumeThat("foo", is("bar"));
    // assumeThat が false を返す場合、
    // テストは失敗とならず、後続の処理がスキップされます。
    assertEquals(...);
}
```

JUnit 5 では、次のように書きます。

```java
@Test
void testNothingInParticular() throws Exception {
    Assumptions.assumingThat("foo".equals(" bar"), () -> {
        // このラムダブロックは、
        // assumingThat() 関数の第一引数が true を返す場合にのみ、実行されます。
        assertEquals(...);
    });
}
```


## JUnit の拡張

JUnit 4 でのフレームワークのカスタマイズは、一般的に、 @RunWith アノテーションを使ってカスタムのランナーを指定することを意味していました。複数のランナーを使った場合は問題が生じやすかったため、通常は、連鎖させることや @Rule を使用することが必要でした。 JUnit 5 では、拡張機能を使ってこの点が改善され、シンプルになっています。

たとえば、 Spring フレームワークを使ってテストを構築する場合、 JUnit 4 では次のようにしていました。

```java
@RunWith(SpringJUnit4ClassRunner.class)
public class MyControllerTest {
    // ...
}
```

JUnit 5 では、次のようにして Spring 拡張機能をインクルードします。

```java
@ExtendWith(SpringExtension.class)
class MyControllerTest {
    // ...
}
```

@ExtendWith アノテーションは繰り返し可能であるため、複数の拡張機能を容易に組み合わせることができます。

org.junit.jupiter.api.extension で定義されたインタフェースを 1 つ、または、複数実装したクラスを作成し、 @ExtendWith を使ってそのクラスをテストに追加して、独自のカスタム拡張機能を容易に定義することもできます。


## テストを JUnit 5 に変換する

既存の JUnit 4 テストを JUnit 5 に変換するためには、以下の手順を使用します。この手順は、ほとんどのテストにおいて有効であるはずです。

1. インポートを更新し、 JUnit 4 を削除して JUnit 5 を追加します。たとえば、 @Test アノテーションのパッケージ名を更新し、アサーションのパッケージ名とクラス名を両方とも更新します ( Asserts を Assertions に) 。この段階でコンパイル・エラーが発生しても心配しないでください。以下の手順を完了することで、エラーは解決するはずです。
2. テスト全体で、古いアノテーションとクラス名を新しいものに置き換えます。たとえば、すべての @Before を @BeforeEach に、すべての Asserts を Assertions に置き換えます。
3. アサーションを更新します。メッセージを提供するアサーションはすべて、メッセージ引数を最後に移動する必要があります (引数が 3 つとも文字列の場合は、特に注意してください) 。さらに、タイムアウトと、想定される例外も更新します (前述の例をご覧ください) 。
4. 前提条件を使用している場合は、それを更新します。
5. @RunWith 、 @Rule 、 @ClassRule の各インスタンスを適切な @ExtendWith アノテーションで置き換えます。その際のサンプルとして、使用している拡張機能に関する更新版ドキュメントをオンラインで探すとよいでしょう。

なお、パラメータ化テストを移行する場合は、もう少しリファクタリングが必要になることに注意してください。 Unit 4 の Parameterized を使っている場合は特にそうです ( JUnit 5 のパラメータ化テストの形式は、 JUnitParams にかなり近いものになっています) 。


## 新機能

ここまで説明してきたのは、既存の機能と、それがどう変わったかについてだけでした。しかし、 JUnit 5 には、テストの表現力やメンテナンス性を向上させる新機能が多数搭載されています。

**表示名** ： JUnit 5 では、クラス、および、メソッドに @DisplayName アノテーションを付加できます。この名前は、レポートの生成時に使用されます。そのため、テストの目的の記述や、失敗の追跡を行いやすくなります。次に例を示します。

```java
@DisplayName("Test MyClass")
class MyClassTest {
    @Test
    @DisplayName("Verify MyClass.myMethod returns true")
    void testMyMethod() throws Exception {    
        // ...
    }
}
```

また、表示名ジェネレータを使ってテスト・クラスやテスト・メソッドを処理し、任意の形式でテスト名を生成することもできます。 [詳細および例については、JUnitのドキュメントをご覧ください。](https://junit.org/junit5/docs/current/user-guide/#writing-tests-display-name-generator)

**アサーション** ： JUnit 5 では、以下のようないくつかの新しいアサーションが導入されています。

- assertIterableEquals() では、 equals() を使って 2 つの Iterable のディープ検証を行います。
- assertLinesMatch() では、 2 つの文字列リストが一致することを検証します。 expected 引数は正規表現を受け取ります。
- assertAll() では、複数のアサーションをグループ化します。個々のアサーションが失敗しても、すべてのアサーションが実行されるという利点がもたらされます。
- assertThrows() と assertDoesNotThrow() は、 @Test アノテーションの expected プロパティに代わるものです。

**テストのネスト** ： JUnit 4 のテスト・スイートは便利でしたが、 JUnit 5 の「テストのネスト」の方が、セットアップやメンテナンスが簡単です。同時に、テスト・グループ間のリレーションシップの表現力が向上しています。次に例を示します。

```java
@DisplayName("Verify MyClass")
class MyClassTest {
    MyClass underTest;

    @Test
    @DisplayName("can be instantiated")
    public void testConstructor() throws Exception {    
        new MyClass();
    }
    @Nested
    @DisplayName("with initialization")
    class WithInitialization {
        @BeforeEach
        void setup() {
            underTest = new MyClass();
            underTest.init("foo");
        }

        @Test
        @DisplayName("myMethod returns true")
        void testMyMethod() {
            assertTrue(underTest.myMethod());
        }
    }
}
```

上記の例では、 MyClass に関連するすべてのテストを 1 つのクラスで行っていることがわかります。このクラスのインスタンスを作成できることは、外側のテスト・クラスで検証できます。ネストされたインナー・クラスで、すべてのテストを行っており、そこで MyClass のインスタンスを作成し、初期化しています。 @BeforeEach メソッドは、ネスト・クラスのテストにのみ適用されます。

テストとクラスの @DisplayNames アノテーションは、テストの目的と構成の両方を表しています。これにより、テストが行われる条件 ( Verify MyClass with initialization (初期化時に MyClass を検証する) ) と、テストで検証する内容 ( myMethod returns true ( myMethod が true を返す) ) を確認できるため、テスト・レポートがわかりやすくなります。これが JUnit 5 の優れたテスト設計パターンです。

**パラメータ化テスト** ： テストのパラメータ化は JUnit 4 にも存在しており、 JUnit4Parameterized などの組込みライブラリや、 JUnitParams などのサードパーティ製ライブラリがありました。 JUnit 5 では、パラメータ化テストが完全に組み込まれる形になり、 JUnit4Parameterized および JUnitParams の優れた機能の一部が採用されています。次に例を示します。

```java
@ParameterizedTest
@ValueSource(strings = {"foo", "bar"})
@NullAndEmptySource
void myParameterizedTest(String arg) {
    underTest.performAction(arg);
}
```

形式は、 JUnitParams に似ており、パラメータが直接テスト・メソッドに渡されます。なお、テストに使う値を複数のソースから取得できる点に注意してください。ここでは、パラメータが 1 つしかないため、 @ValueSource を使うのは簡単です。 @EmptySource と @NullSource は、実行に使う値のリストに、それぞれ空の文字列と NULL を追加することを示します (両方を使う場合は、上記 ( @NullAndEmptySource ) のように組み合わせて使うこともできます) 。 @EnumSource や @ArgumentsSource (カスタムの値プロバイダ) など、値のソースは、他にも複数あります。複数のパラメータが必要な場合は、 @MethodSource や @CsvSource を使うこともできます。

JUnit 5 には、 @RepeatedTest というテスト・タイプも追加されました。このテスト・タイプは、 1 つのテストを指定された回数だけ繰り返すものです。

**条件付きテスト実行** ： JUnit 5 では、テスト、または、コンテナ（テスト・クラス）を条件によって、有効化、または、無効化する ExecutionCondition 拡張機能 API が提供されています。これはテストで @Disabled を使うことに似ていますが、カスタムの条件を定義できます。以下のような複数の組込み条件があります。

- @EnabledOnOs/@DisabledOnOs：指定されたオペレーティング・システムでのみ、テストを有効化または無効化する
- @EnabledOnJre/@DisabledOnJre：特定のバージョンのJavaでテストを有効化または無効化することを指定する
- @EnabledIfSystemProperty：JVMシステム・プロパティの値に基づいてテストを有効化する
- @EnabledIf：スクリプトのロジックで記述された条件が満たされた場合にテストを有効化する

**テスト・テンプレート** ： テスト・テンプレートは、通常のテストとは異なり、実行する一連の手順を定義するものです。このテンプレートは、特定の起動コンテキストを使って他の場所から実行することができます。つまり、テスト・テンプレートをいったん定義してから、そのテストを行うために使用する、実行時の起動コンテキストのリストを作成することができます。 [詳細および例については、ドキュメントをご覧ください。](https://junit.org/junit5/docs/current/user-guide/#extensions-test-templates)

**動的テスト** ： 動的テストは、テスト・テンプレートに似ており、行うテストがその実行時に生成されます。ただし、テスト・テンプレートは、特定の一連の手順を複数回実行するように定義されるのに対し、動的テストでは、同じ起動コンテキストを使用して、別のロジックを実行することができます。動的テストの使用方法の 1 つとして、抽象オブジェクトのリストをストリーミングし、それぞれの具象型に基づいて、別々のアサーション・セットを実行することが考えられます。 [ドキュメントには、見本となる例が記載されています。](https://junit.org/junit5/docs/current/user-guide/#writing-tests-dynamic-tests)


## まとめ

JUnit 5 の新機能を使いたい場合を除けば、古い JUnit 4 テストを JUnit 5 に変換する必要はおそらくないでしょう。しかし、 JUnit 5 に切り替えるべき大きな理由があります。その 1 つとして、 JUnit 5 のテストは強力になるとともに、メンテナンスしやすくなっていることが挙げられます。さらに、 JUnit 5 には、便利な機能が数多く搭載されています。使う機能だけがインポートされることに加え、複数の拡張機能を使うことや、独自のカスタム拡張機能を作成することもできます。こういった変更点や新機能が組み合わされて、強力で柔軟な、 JUnit フレームワークのアップデートが実現しています。


## 引用元資料

- [JUnit 4からJUnit 5に移行する：重要な違いと利点](https://blogs.oracle.com/otnjp/post/migrating-from-junit-4-to-junit-5-important-differences-and-benefits-ja)


## 参考資料

-  [JUnit4とJUnit5の違い - Qiita](https://qiita.com/torayamadajp/items/e0c84296105edb472942)
