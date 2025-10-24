- [ActionBar と Toolbar の違い](#actionbar-と-toolbar-の違い)
  - [ActionBar](#actionbar)
    - [概要](#概要)
    - [使用例](#使用例)
  - [Toolbar](#toolbar)
    - [概要](#概要-1)
    - [使用例](#使用例-1)
      - [1. XML で Toolbar を配置](#1-xml-で-toolbar-を配置)
      - [2. Kotlin で Toolbar をセット](#2-kotlin-で-toolbar-をセット)
  - [どっちを使うべき？](#どっちを使うべき)
  - [引用元資料](#引用元資料)


# ActionBar と Toolbar の違い

Toolbar や ActionBar は、アプリの画面上部に表示される タイトル や アクションボタン（メニューアイコンなど）が配置されるエリアのことを指します。

ActionBar と Toolbar の違いを表にまとめると以下の通りです。

| 項目           | ActionBar            | Toolbar                           |
| -------------- | -------------------- | --------------------------------- |
| 提供元         | Android の Activity  | androidx.appcompat.widget.Toolbar |
| カスタマイズ性 | 限られている         | 高い（自由にレイアウト可能）      |
| XML での利用   | android:theme で設定 | layout.xml 内に自由に配置         |
| 依存関係       | Activity の一部      | ViewGroup（レイアウト内の View）  |


## ActionBar

### 概要

- Activity に標準で付属するタイトルバー
- メニューアイコンやナビゲーションボタン（戻るボタンなど）を表示
- setSupportActionBar() を使って Toolbar に置き換えることも可能


### 使用例

```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ActionBar のタイトルを変更
        supportActionBar?.title = "My App"
    }
}
```


## Toolbar

### 概要

- ActionBar の代わりに使えるカスタマイズ可能な View
- XML で自由にレイアウトに配置できる
- setSupportActionBar(toolbar) を使って ActionBar として動作させることも可能


### 使用例

#### 1. XML で Toolbar を配置

```xml
<androidx.appcompat.widget.Toolbar
    android:id="@+id/toolbar"
    android:layout_width="match_parent"
    android:layout_height="?attr/actionBarSize"
    android:background="?attr/colorPrimary"
    app:title="My Toolbar"
    app:titleTextColor="@android:color/white"/>
```


#### 2. Kotlin で Toolbar をセット

```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar) // Toolbar を ActionBar として使用
    }
}
```


## どっちを使うべき？

- カスタマイズが不要 → ActionBar
- デザインを自由に変更したい → Toolbar

最近の Android アプリでは Toolbar を ActionBar の代わりに使うのが主流です！


## 引用元資料

- ChatGPT


