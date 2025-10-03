- [Navigation: Multiple back stacks](#navigation-multiple-back-stacks)
  - [はじめに](#はじめに)
  - [複数のバックスタックのサポート](#複数のバックスタックのサポート)
  - [引用元資料](#引用元資料)


# Navigation: Multiple back stacks

ナビゲーションに関する第 2 回目の MAD スキル シリーズの別の記事へようこそ。この記事では、リクエストの多い機能である、ナビゲーションの複数のバック スタック サポートについて説明します。このコンテンツをビデオ形式でご覧になりたい場合は、次の動画をご覧ください。

https://youtu.be/Covu3fPA1nQ


## はじめに

アプリが BottomNavigationView を使用しているとします。この変更により、ユーザーが別のタブを選択すると、現在のタブのバックスタックが保存され、選択したタブのバックスタックがシームレスに復元されます。

バージョン 2.4.0-alpha01 以降、NavigationUI ヘルパーは、コードを変更することなく複数のバックスタックをサポートします。つまり、アプリが `BottomNavigationView` 、または、 `NavigationView` の `setupWithNavController()` メソッドを使用する場合、依存関係を更新するだけで、複数のバックスタックのサポートがデフォルトで有効になります。


## 複数のバックスタックのサポート

[このリポジトリ](https://github.com/android/architecture-components-samples/tree/master/NavigationAdvancedSample) の Advanced Navigation サンプルを使用して、実際に動作を確認してみましょう。

アプリは 3 つのタブで構成され、各タブには独自のナビゲーション フローがあります。以前のバージョンの Navigation で、複数のバックスタックをサポートするには、このサンプルの NavigationExtensions ファイルにヘルパーのセットを追加する必要がありました。これらの拡張機能により、アプリはタブごとに独自のバックスタックを持つ個別の NavHostFragment を保持し、ユーザーがタブを切り替えると、それらの間で切り替えます。

これらの拡張機能を削除するとどうなるか見てみましょう。これを行うには、NavigationExtensions クラスを削除してすべての使用を削除し、 BottomNavigationView を NavController に接続するために、NavigationUI の標準の setupWithNavController() メソッドに切り替えます。


```kotlin
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_container
        ) as NavHostFragment
        navController = navHostFragment.navController

        // navController を使用して、 bottomNavigationView を設定する。
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavigationView.setupWithNavController(navController)

        // 3 つのトップレベルデスティネーションと navController を使用して
        // ActionBar を設定する。
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.titleScreen, R.id.leaderboard,  R.id.register)
        )
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setupWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }
}
```

また、include タグを使用して、3 つの個別のナビゲーション グラフを 1 つのグラフに結合します。これで、アクティビティのレイアウトには、1 つのグラフを含む 1 つの NavHostFragment だけが含まれるようになりました。

```xml
<navigation
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/nav_graph"
    app:startDestination="@+id/home">

    <include app:graph="@navigation/home"/>
    <include app:graph="@navigation/list"/>
    <include app:graph="@navigation/form"/>

</navigation>
```

アプリを実行すると、今度は下部のタブが状態を保持せず、他のタブに切り替えるとバックスタックがリセットされます。NavigationExtentions が削除されたため、アプリは複数のバックスタックのサポートを失いました。

ここで、ナビゲーションとフラグメントの依存関係のバージョンを更新します。

```
versions.fragment = "1.4.0-alpha01"
versions.navigation = "2.4.0-alpha01"
```

Gradle 同期が完了したら、アプリを再度実行すると、別のタブに移動しても各タブの状態が維持されていることがわかります。この動作はデフォルトで有効になっていることに注意してください。

最後に、すべてが機能することを確認するために、テストを実行しましょう。このアプリには、複数のバックスタックの動作を確認するためのテストがすでにいくつかあります。BottomNavigationTest を実行し、さまざまなテストが実行され、下部ナビゲーションの動作がテストされるのを確認します。

ほら、すべてのテストに合格しました!

まとめ
これで完了です! アプリで BottomNavigationView または NavigationView を使用していて、複数のバックスタックのサポートを待っていた場合は、ナビゲーションとフラグメントの依存関係を更新するだけで済みます。コードを変更する必要はありません!

よりカスタムな操作を行う場合は、バックスタックの保存と復元を可能にする新しい API もあります。詳細については、 [この記事](https://medium.com/androiddevelopers/multiple-back-stacks-b714d974f134) をご覧ください。

基盤となる API の詳細と、複数のバックスタックをサポートするために変更する必要があった点について知りたい場合は、 [この記事](https://medium.com/androiddevelopers/fragments-rebuilding-the-internals-61913f8bf48e) をご覧ください。

このナビゲーション シリーズをご覧いただき、ありがとうございました!


## 引用元資料

- [Navigation: Multiple back stacks](https://medium.com/androiddevelopers/navigation-multiple-back-stacks-6c67ba41952f)










