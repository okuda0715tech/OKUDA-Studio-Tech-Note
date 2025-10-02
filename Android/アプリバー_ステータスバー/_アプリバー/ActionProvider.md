- [ActionProvider](#actionprovider)


# ActionProvider

ActionProvider は、Android の Toolbar や ActionBar にカスタムのアクションビューを提供するためのクラスです。主に MenuItem に独自の UI や動作を組み込む際に使用されます。


## 概要

ActionProvider を使うと、通常の MenuItem では実現できない柔軟な動作を持つメニュー項目を作成できます。例えば、以下のような UI を実装できます：

- 検索ボックス（検索バーを直接表示）
- 共有ボタン（ShareActionProvider を利用）
- カスタムビュー（独自のボタンやリスト）


## 基本的な使い方

### 1. ActionProvider のサブクラスを作成

```kotlin
class MyActionProvider(context: Context) : ActionProvider(context) {

    override fun onCreateActionView(): View {
        val button = Button(context)
        button.text = "Click Me"
        button.setOnClickListener {
            Toast.makeText(context, "Button Clicked", Toast.LENGTH_SHORT).show()
        }
        return button
    }
}

```


### 2. メニュー XML で指定

```xml
<item
    android:id="@+id/menu_custom"
    android:title="Custom Action"
    android:icon="@drawable/ic_custom"
    app:actionProviderClass="com.example.MyActionProvider"
    app:showAsAction="always" />
```


### 3. （必要に応じて）メニューの ActionProvider をプログラムで設定

```kotlin
override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.main_menu, menu)
    val item = menu.findItem(R.id.menu_custom)
    item.actionProvider = MyActionProvider(this)
    return true
}
```


## ShareActionProvider の例

ShareActionProvider は、標準の ActionProvider の一種で、共有アクションを簡単に実装できます。


### XML での定義

```xml
<item
    android:id="@+id/menu_share"
    android:title="Share"
    android:icon="@drawable/ic_share"
    app:actionProviderClass="androidx.appcompat.widget.ShareActionProvider"
    app:showAsAction="always" />
```


### Kotlin でインテントを設定

```kotlin
override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.main_menu, menu)
    val item = menu.findItem(R.id.menu_share)
    val shareProvider = item.actionProvider as? ShareActionProvider
    shareProvider?.setShareIntent(getShareIntent())
    return true
}

private fun getShareIntent(): Intent {
    return Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, "Check out this cool app!")
    }
}
```


## ActionProvider を使うメリット

- カスタム UI を MenuItem に組み込める
- 動的にコンテンツを更新（例：ShareActionProvider でシェア内容を変更）
- コードの再利用（複数の MenuItem に適用可能）


## ActionView との違い

| 比較項目       | ActionProvider                | ActionView (setActionView()) |
| -------------- | ----------------------------- | ---------------------------- |
| 使いやすさ     | 再利用可能で柔軟              | 単純なカスタムビュー向き     |
| コードの記述   | ActionProvider を継承して実装 | 直接 View を指定             |
| 共有アクション | ShareActionProvider あり      | 手動で処理を実装             |


## まとめ

ActionProvider は、MenuItem にカスタム UI や動作を組み込むための仕組みです。特に、ShareActionProvider などの標準 ActionProvider を使えば、簡単に共有機能を実装できます。カスタム ActionProvider を作成すれば、独自のボタンやウィジェットを Toolbar に追加できます。


## 引用元資料

- ChatGPT


