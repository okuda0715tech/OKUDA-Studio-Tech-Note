- [Multiple back stacks](#multiple-back-stacks)
  - [システムの戻るボタンのメリット](#システムの戻るボタンのメリット)
  - [Fragments の複数のバック スタック](#fragments-の複数のバック-スタック)
  - [フラグメントの技術的負債を返済](#フラグメントの技術的負債を返済)
  - [フラグメントに期待できること](#フラグメントに期待できること)
  - [ナビゲーションを使用して、複数のバックスタックを任意の画面タイプに持ち込む](#ナビゲーションを使用して複数のバックスタックを任意の画面タイプに持ち込む)
  - [Navigation で複数のバックスタックを有効にする](#navigation-で複数のバックスタックを有効にする)
  - [状態を保存して、ユーザーを救いましょう](#状態を保存してユーザーを救いましょう)
  - [引用元資料](#引用元資料)


# Multiple back stacks

「バック スタック」がシステムの戻るボタンで戻ることができる一連の画面だとすると、「複数のバック スタック」はそれらの集合に過ぎません。Navigation 2.4.0-alpha01 と Fragment 1.4.0-alpha01 で追加された複数のバック スタックのサポートで、まさにそれが実現されました。


## システムの戻るボタンのメリット

Android の新しいジェスチャー ナビゲーション システムを使用する場合でも、従来のナビゲーション バーを使用する場合でも、ユーザーが「戻る」ことができる機能は Android のユーザー エクスペリエンスの重要な部分であり、これを適切に行うことは、アプリをエコシステムの自然な一部に感じさせる上で重要です。

最も単純なケースでは、システムの戻るボタンは、アクティビティを終了するだけです。これまでは、アクティビティの onBackPressed() メソッドをオーバーライドして、この動作をカスタマイズしていたかもしれません。しかし、 2021 年現在、それはまったく不要です。代わりに、 [OnBackPressedDispatcher](https://developer.android.com/reference/androidx/activity/OnBackPressedDispatcher) に、 [カスタムの戻るナビゲーション用の API](https://developer.android.com/guide/navigation/navigation-custom-back) があります。これは、 FragmentManager と NavController が、実装しているのと同じ API です。

つまり、Fragments または Navigation のいずれかを使用すると、OnBackPressedDispatcher を使用して、バック スタック API を使用している場合に、システムの戻るボタンがバック スタックにプッシュした各画面を逆にするように動作します。

複数のバック スタックがあっても、これらの基本は変わりません。システムの戻るボタンは、依然として「戻る」という一方向のコマンドです。これは、複数のバック スタック API の動作に大きな影響を与えます。


## Fragments の複数のバック スタック

見た目では、 [複数のバック スタックのサポート](https://developer.android.com/guide/fragments/fragmentmanager#multiple-back-stacks) は一見すると単純ですが、「フラグメント バック スタック」が、実際に何であるかを少し説明する必要があります。FragmentManager のバック スタックは、フラグメントで構成されているのではなく、フラグメント トランザクションで構成されています。具体的には、 [addToBackStack(String name)](https://developer.android.com/reference/androidx/fragment/app/FragmentTransaction#addToBackStack(java.lang.String)) API を使用したフラグメント トランザクションです。

つまり、addToBackStack() を使用してフラグメント トランザクションを commit() すると、FragmentManager は、トランザクションで指定した各操作 (置換など) を実行して、トランザクションを実行し、各フラグメントを期待された状態に移行します。その後、FragmentManager は、そのトランザクションをバック スタックの一部として保持します。

popBackStack() を呼び出すと (直接または FragmentManager のシステム バック ボタンとの統合を介して)、フラグメント バック スタックの最上位のトランザクションが反転されます (追加されたフラグメントが削除され、非表示のフラグメントが表示されるなど)。これにより、FragmentManager はフラグメント トランザクションが最初にコミットされる前と同じ状態に戻ります。

注: 強調してもしすぎることはありませんが、addToBackStack() を使用したトランザクションと addToBackStack() を使用しないトランザクションを同じ FragmentManager に混在させることは絶対にしないでください。バック スタック上のトランザクションは、バック スタックを変更しないフラグメント トランザクションをまったく認識しません。これらのトランザクションの下から入れ替えると、ポップ時の反転がはるかに危険な問題になります。

つまり、popBackStack() は破壊的な操作です。追加されたフラグメントの状態は、そのトランザクションがポップされると破棄されます。つまり、ビューの状態、保存されたインスタンスの状態、そのフラグメントにアタッチされた ViewModel インスタンスがすべて消去されます。これが、その API と新しい saveBackStack() の主な違いです。

saveBackStack() は、トランザクションのポップと同じ逆の処理を行いますが、ビューの状態、保存されたインスタンスの状態、および ViewModel インスタンスがすべて破壊から保護されるようにします。これにより、restoreBackStack() API は後で保存された状態からそれらのトランザクションとそのフラグメントを再作成し、保存されたすべてのものを事実上「やり直す」ことができます。魔法のようです!

ただし、これは多くの技術的負債を返済せずに実現したわけではありません。


## フラグメントの技術的負債を返済

フラグメントは常にフラグメントのビューの状態を保存していましたが、フラグメントの onSaveInstanceState() が呼び出されるのは、アクティビティの onSaveInstanceState() が呼び出されたときだけです。 saveBackStack() を呼び出すときに保存されたインスタンスの状態が保存されるようにするには、フラグメントのライフサイクル遷移の適切な時点で onSaveInstanceState() の呼び出しも挿入する必要があります。あまり早く呼び出すことはできません (フラグメントがまだ STARTED の間は状態が保存されるべきではありません) が、遅すぎてもいけません (フラグメントが破棄される前に状態を保存する必要があります)。

この要件により、フラグメントを期待された状態に移動させ、再入可能な動作とフラグメントに入るすべての状態遷移を処理する場所が 1 つあることを確認するために、FragmentManager の状態遷移を修正するプロセスが開始されました。

フラグメントの再構築に 35 の変更と 6 か月を費やした結果、延期されたフラグメントが深刻な問題を抱えていることが判明しました。その結果、延期されたトランザクションが宙に浮いた状態になり、実際にはコミットされておらず、実際にはコミットされていない状態になりました。65 を超える変更とさらに 5 か月後、FragmentManager が状態、延期された遷移、アニメーションを管理する内部構造のほとんどを完全に書き直しました。この取り組みについては、以前のブログ投稿で詳しく説明しています。

[Fragments: rebuilding the internals](https://medium.com/androiddevelopers/fragments-rebuilding-the-internals-61913f8bf48e)


## フラグメントに期待できること

技術的負債が解消され (そして、はるかに信頼性が高く理解しやすいフラグメントマネージャー)、saveBackStack() と restoreBackStack() という氷山の一角の API が追加されました。

これらの新しい API を使用しない場合、何も変わりません。単一のフラグメントマネージャー バック スタックは以前と同じように機能します。既存の addToBackStack() API は変更されません。null 名または任意の名前を使用できます。ただし、複数のバック スタックを調べ始めると、その名前が新たな重要性を帯びます。saveBackStack() および後で restoreBackStack() で使用するフラグメント トランザクションの一意のキーとなるのは、その名前です。

例で見るとわかりやすいかもしれません。アクティビティに最初のフラグメントを追加し、それぞれが単一の置換操作を含む 2 つのトランザクションを実行したとします。

```kotlin
// This is the initial fragment the user sees
fragmentManager.commit {
    setReorderingAllowed(true)
    replace<HomeFragment>(R.id.fragment_container)
}
// Later, in response to user actions, we’ve added two more
// transactions to the back stack
fragmentManager.commit {
    setReorderingAllowed(true)
    replace<ProfileFragment>(R.id.fragment_container)
    addToBackStack(“profile”)
}
fragmentManager.commit {
    setReorderingAllowed(true)
    replace<EditProfileFragment>(R.id.fragment_container)
    // この引数は "profile" が正しいのでは？というコメントがありますが、
    // ブログ投稿者は No と返答しています。
    addToBackStack(“edit_profile”)
}
```

つまり、FragmentManager は次のようになります。

<img src="./画像/FragmentManager state after three commits.webp" width="500">

profile バック スタックをスワップアウトして、 notifications フラグメントにスワップするとします。 saveBackStack() を呼び出して、新しいトランザクションを実行します。

```kotlin
fragmentManager.saveBackStack("profile")
fragmentManager.commit {
    setReorderingAllowed(true)
    replace<NotificationsFragment>(R.id.fragment_container)
    addToBackStack("notifications")
}
```

これで、ProfileFragment を追加したトランザクションと EditProfileFragment を追加したトランザクションが "profile" キーの下に保存されました。これらのフラグメントの状態は完全に保存され、FragmentManager はトランザクションの状態とともにそれらの状態を保持しています。重要な点として、これらのフラグメント インスタンスはメモリにも FragmentManager にも存在しなくなり、状態 (および ViewModel インスタンスの形式の構成以外の状態) だけが残ります。

<img src="./画像/FragmentManager state after we’ve saved the profile back stack and added one more commit.webp" width="500">

元に戻すのは非常に簡単です。「通知」トランザクションで同じ saveBackStack() 操作を実行してから、restoreBackStack() を実行します。

```kotlin
fragmentManager.saveBackStack(“notifications”)
fragmentManager.restoreBackStack(“profile”)
```

2 つのスタックの位置が実質的に入れ替わりました。

<img src="./画像/FragmentManager state after swapping the two stacks.webp" width="500">

単一のアクティブなバックスタックを維持し、その上にトランザクションをスワップするというこのスタイルにより、FragmentManager とシステムの残りの部分は、システムの戻るボタンがタップされたときに実際に何が起こるかについて常に一貫したビューを持つことができます。実際、そのロジックはまったく変更されていません。以前と同じように、最後のトランザクションをフラグメント バックスタックからポップするだけです。

これらの API は、その基礎となる効果にもかかわらず、意図的に最小限に抑えられています。これにより、フラグメント ビューの状態、保存されたインスタンスの状態、および非構成状態を保存するためのハックを回避しながら、これらのビルディング ブロックの上に独自の構造を構築できます。

もちろん、これらの API の上に独自の構造を構築したくない場合は、私たちが提供する API を使用することもできます。


## ナビゲーションを使用して、複数のバックスタックを任意の画面タイプに持ち込む

ナビゲーション コンポーネントは、最初から、ビュー、フラグメント、コンポーザブル、またはアクティビティ内に実装する可能性のある他の種類の画面や「宛先」について何も知らない汎用ランタイムとして構築されました。代わりに、特定のタイプのデスティネーションと対話する方法を知っている 1 つ以上の [Navigator](https://developer.android.com/reference/kotlin/androidx/navigation/Navigator) インスタンスを追加するのは、 [NavHost インターフェース](https://developer.android.com/reference/kotlin/androidx/navigation/NavHost) の実装の責任です。

つまり、フラグメントと対話するためのロジックは、navigation-fragment アーティファクトとその FragmentNavigator および DialogFragmentNavigator に完全にカプセル化されていました。同様に、Composable と対話するためのロジックは、完全に独立した navigation-compose アーティファクトとその ComposeNavigator にあります。この抽象化により、Composable のみを使用してアプリをビルドする場合、Navigation Compose を使用するときにフラグメントへの依存関係を強制的に取り込む必要がなくなります。

このレベルの分離により、Navigation の複数のバック スタックには、実際には 2 つのレイヤーがあります。

- NavController バック スタックを構成する個々の [NavBackStackEntry](https://developer.android.com/reference/kotlin/androidx/navigation/NavBackStackEntry) インスタンスの状態を保存します。これは NavController の責任です。

- 各 NavBackStackEntry に関連付けられた Navigator 固有の状態を保存します (例: FragmentNavigator デスティネーションに関連付けられたフラグメント)。これはナビゲーターの責任です。

ナビゲーターが更新されておらず、その状態を保存できない場合には特に注意が払われました。基盤となるナビゲーター API は状態を保存できるように完全に書き直されましたが (以前のバージョンの代わりにオーバーライドする必要がある、navigate() および popBackStack() API の新しいオーバーロードを使用)、ナビゲーターが更新されていなくても NavController は NavBackStackEntry 状態を保存します (下位互換性は Jetpack の世界では重要です)。

追記: この新しいナビゲーター API では、ミニ ナビゲーターとして機能する [TestNavigatorState](https://developer.android.com/reference/kotlin/androidx/navigation/testing/TestNavigatorState) をアタッチすることで、独自のカスタム ナビゲーターを単独でテストすることもはるかに簡単になります。

アプリでナビゲーションのみを使用している場合、ナビゲーター レベルは直接操作する必要のあるものではなく、実装の詳細になります。簡単に言うと、FragmentNavigator と ComposeNavigator を新しい Navigator API に移行して、状態を正しく保存および復元するために必要な作業はすでに完了しています。そのレベルで作業する必要はありません。


## Navigation で複数のバックスタックを有効にする

NavController をマテリアル ビュー コンポーネントに接続するための独自のヘルパー セットである [NavigationUI](https://developer.android.com/guide/navigation/navigation-ui) を使用している場合は、メニュー項目、BottomNavigationView (現在は NavigationRailView)、および NavigationView に対して複数のバックスタックがデフォルトで有効になっていることがわかります。つまり、navigation-fragment と navigation-ui の一般的な組み合わせがそのまま機能します。

NavigationUI API は、Navigation で使用できる他のパブリック API の上に意図的に構築されているため、必要なカスタム コンポーネントのセットに合わせて独自のバージョンを構築できます。バックスタックの保存と復元を可能にする API も例外ではありません。NavOptions、navOptions Kotlin DSL、Navigation XML、popBackStack() のオーバーロードに新しい API が追加され、ポップ操作で状態を保存するか、ナビゲート操作で以前に保存した状態を復元するかを指定できます。

たとえば、Compose では、グローバル ナビゲーション パターン (下部ナビゲーション バー、ナビゲーション レール、ドロワーなど、思いつくものすべて) はすべて、BottomNavigation との統合で示したのと同じ手法を使用して、saveState 属性と restoreState 属性で navigation() を呼び出すことができます。

```kotlin
onClick = {
    navController.navigate(screen.route) {
        // Pop up to the start destination of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }

        // Avoid multiple copies of the same destination when
        // reselecting the same item
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        restoreState = true
    }
}
```

## 状態を保存して、ユーザーを救いましょう

ユーザーにとって最もイライラすることの 1 つは、状態が失われることです。これが、フラグメントに状態の保存に関するページが丸々ある理由の 1 つであり、各レイヤーが複数のバックスタックをサポートするように更新されて非常にうれしい理由の 1 つです。

- **フラグメント (つまり、ナビゲーション コンポーネントをまったく使用しない)**: これは、saveBackStack および restoreBackStack の新しい FragmentManager API を使用したオプトインの変更です。

- **コア ナビゲーション ランタイム**: restoreState および saveState の新しい NavOptions メソッドと、saveState ブール値 (デフォルトは false) も受け入れる popBackStack() の新しいオーバーロードを追加します。

- **フラグメントを使用したナビゲーション**: FragmentNavigator は、新しい Navigator API を利用して、Navigation Runtime API を Fragment API に適切に変換します。

- **NavigationUI**: onNavDestinationSelected()、NavigationBarView.setupWithNavController()、および NavigationView.setupWithNavController() は、バックスタックをポップするときに常に、新しい restoreState および saveState NavOptions をデフォルトで使用します。つまり、Navigation 2.4.0-alpha01 以降にアップグレードすると、これらの NavigationUI API を使用するすべてのアプリで、コードを変更しなくても複数のバックスタックが取得されます。

この API を使用する他の例を確認する場合は、NavigationAdvancedSample (複数のバックスタックをサポートするために必要だった NavigationExtensions コードが一切含まれない、新しく更新されたサンプル) を参照してください。

[android/architecture-components-samples](https://github.com/android/architecture-components-samples/tree/master/NavigationAdvancedSample?source=post_page-----b714d974f134--------------------------------) ← Not Found になります。

Navigation Compose については、Tivi を検討してください。

[chrisbanes/tivi](https://github.com/chrisbanes/tivi?source=post_page-----b714d974f134--------------------------------)

何か問題が発生した場合は、公式の問題追跡システムを使用して、Fragments または Navigation のバグを報告してください。私たちが必ず確認させていただきます。


## 引用元資料

- [Multiple back stacks](https://medium.com/androiddevelopers/multiple-back-stacks-b714d974f134)

