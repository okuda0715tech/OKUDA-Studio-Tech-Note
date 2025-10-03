

# Property delegate must have a 'getValue(XXXActivity, KProperty)' method. None of the following functions are suitable

タイトルのようなビルドエラーが発生する場合の対処方法について記載します。


## ケース1

### エラーが発生したコード

```Kotlin
class DarkModeSettingsActivity : AppCompatActivity() {

    private val binding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_dark_mode_settings)
    }

}
```


### エラーの原因

`binding` フィールドに対して、 `lazy{}` ブロックで取得した値をセットしようとしているが、  
`setContentView()` メソッドの返値の型に仮型 `T` が含まれているため、  
`binding` フィールドの型が確定できないことが原因のエラーである。


### 対処方法

以下のように `binding` フィールドの型を明示的に定義することで解決できます。

```Kotlin
    private val binding: ActivityDarkModeSettingsBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_dark_mode_settings)
    }
```


## ケース2

### エラーが発生したコード

```Kotlin
    private var darkModeSettings: DarkModeSettings by lazy {
        preferences.getInt(PREF_KEY_DARK_THEME_SETTINGS, DarkModeSettings)
    }
```


### エラーの原因

`by lazy{}` は、 `val` の場合しか使用できないにもかかわらず、 `var` で使用しているため。 


### 対処方法

`var` を `val` に変更する必要がある。  
あるいは、 `lateinit` を使用して、別の場所で初期化を行う必要がある。



