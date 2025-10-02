- [一つの画面内での View システムと Jetpack Compose の混在](#一つの画面内での-view-システムと-jetpack-compose-の混在)
  - [Android View 内部に Compose UI を組み込む](#android-view-内部に-compose-ui-を組み込む)
  - [Compose UI 内部に Android Viewを組み込む](#compose-ui-内部に-android-viewを組み込む)
    - [XML を使用せずに、 Kotlin で View システムを利用する場合](#xml-を使用せずに-kotlin-で-view-システムを利用する場合)
    - [XML で記述した View を Compose UI に埋め込む場合](#xml-で記述した-view-を-compose-ui-に埋め込む場合)


# 一つの画面内での View システムと Jetpack Compose の混在

## Android View 内部に Compose UI を組み込む

既存の XML レイアウト内に `ComposeView` を配置し、その View を Kotlin で取得して、 setContent() 関数を呼び出し、そのラムダ式内に、コンポーズ関数を記述します。

```kotlin
class MyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val composeView = findViewById<ComposeView>(R.id.compose_view)
        composeView.setContent {
            // Compose UIを記述する
            Text("Hello from Compose!")
        }
    }
}
```


## Compose UI 内部に Android Viewを組み込む

### XML を使用せずに、 Kotlin で View システムを利用する場合

Compose の Composable 関数内で `AndroidView` を呼び出し、既存の View を直接組み込むことができます。

```kotlin
@Composable
fun MyScreen() {
    Column {
        Text("Hello from Compose!")
        AndroidView(factory = { context ->
            // この Button は、 View システムのオブジェクトです。
            Button(context).apply {
                text = "Click me from View"
                // 既存の View のイベントなどを設定
            }
        })
    }
}
```


### XML で記述した View を Compose UI に埋め込む場合

```groovy
// Groovy
dependencies {
    implementation 'androidx.compose.ui:ui-viewbinding:1.x.x' // バージョンは適宜調整してください
}
```

```xml
<!-- activity_main.xml -->
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Hello from XML!" />

</LinearLayout>
```

`AndroidViewBinding` を使用して、 XML レイアウトを Compose に組み込みます。

```kotlin
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidViewBinding

@Composable
fun MyScreen(binding: ActivityMainBinding) {
    Column {
        Text("Hello from Compose!")
        AndroidViewBinding(
            factory = { context ->
                // ここでインフレートしても良いし、もっと上流でインフレートして、
                // パラメータで渡された View を使用しても OK.
                ActivityMainBinding.inflate(
                    LayoutInflater.from(context),
                    null,
                    false)
            }
        ) { viewBinding ->
            viewBinding.root
        }
    }
}
```

```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val binding = ActivityMainBinding.inflate(layoutInflater)
            MyScreen(binding)
        }
    }
}
```


