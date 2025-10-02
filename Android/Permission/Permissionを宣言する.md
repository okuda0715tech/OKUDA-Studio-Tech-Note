- [Permissionを宣言する](#permissionを宣言する)
  - [概要](#概要)
  - [ハードウェア機能の Permission を要求する場合](#ハードウェア機能の-permission-を要求する場合)
  - [実行時にその機能を搭載しているかどうかをチェックする方法](#実行時にその機能を搭載しているかどうかをチェックする方法)
  - [API レベルを指定して Permission を宣言する](#api-レベルを指定して-permission-を宣言する)
  - [従来の実装方法の例](#従来の実装方法の例)
  - [新しい実装方法の例](#新しい実装方法の例)
    - [一つの権限を要求する場合](#一つの権限を要求する場合)
    - [複数の権限を要求する場合](#複数の権限を要求する場合)
  - [従来の実装方法と新しい実装方法の使い分け](#従来の実装方法と新しい実装方法の使い分け)
    - [使い分けのガイドライン](#使い分けのガイドライン)
  - [新しい方法を Jetpack Compose で使用する方法（ Accompanist ライブラリを使用しない方法）](#新しい方法を-jetpack-compose-で使用する方法-accompanist-ライブラリを使用しない方法)
    - [引用元資料](#引用元資料)
  - [新しい方法を Jetpack Compose で使用する方法（ Accompanist ライブラリを使用する方法１）](#新しい方法を-jetpack-compose-で使用する方法-accompanist-ライブラリを使用する方法１)
    - [引用元資料](#引用元資料-1)
  - [新しい方法を Jetpack Compose で使用する方法（ Accompanist ライブラリを使用する方法２）](#新しい方法を-jetpack-compose-で使用する方法-accompanist-ライブラリを使用する方法２)
    - [引用元資料](#引用元資料-2)


# Permissionを宣言する

## 概要

「アプリの権限」をリクエストする場合は、アプリのマニフェストファイルで、対象となる権限を宣言します。これは、インストール時権限に限らず、実行時権限など、すべての権限は、マニフェストファイルへの宣言が必要です。ただし、昔の Notification など、一部の Permission 的なシステムダイアログを表示するものの中には、 Permission とは別管理のものがあり、その場合は、マニフェストファイルへの記述は行いません。昔の Notification は、通知チャネルを生成する時に、 OS が Permission 的なダイアログを自動で表示していました。

マニフェストへの宣言により、アプリストアとユーザーは、アプリが要求する可能性のある権限セットについて理解できます。

また、実行時に権限をリクエストする必要がある場合は、追加で実行時にリクエストを行う処理を記述する必要があります。


## ハードウェア機能の Permission を要求する場合

カメラやブルートゥース機能などのハードウェア機能は、デバイスによってはその機能を持っていない可能性があります。それらの機能の Permission を要求したい場合は、まず、それらの機能がアプリにとって、必須なのか、必須ではないのか、を明確にする必要があります。

もし、アプリにとって必須な機能であるのならば、その機能を持っていないデバイスは、 PlayStore からアプリをダウンロードできないようにすることができます。

```xml
<!-- アプリにとってその機能が必須ではない場合 -->
<uses-feature android:name="android.hardware.camera" android:required="false" />
<!-- アプリにとってその機能が必須である場合 -->
<uses-feature android:name="android.hardware.camera" android:required="true" />
```

`required="false"` が設定されていれば、その機能を持っていないデバイスでも、アプリをインストールすることが可能です。それ以外の場合、 ( `required="true"` が設定 / `<uses-feature>` が省略 / `required` 属性が省略 ) されていれば、その機能を持っていないデバイスでは、 PlayStore で検索しても、対象のアプリは表示されません。また、ブラウザ版の PlayStore から該当端末にインストールしようとした場合でも 「お使いの端末にはインストールできません」という旨のメッセージが表示され、インストールすることができないそうです。

`<uses-feature>` の記述が省略されている場合、 `<uses-permission>` タグから uses-feature タグが自動生成されることがあります。例えば、カメラの Permission を宣言している場合、対応する uses-feature が存在するため、省略されていても、自動生成されます。自動生成された場合は、 `android:required` 属性が true で宣言されます。そのため、 false を宣言したい場合は、省略せずに明記する必要があることに注意してください。


## 実行時にその機能を搭載しているかどうかをチェックする方法

ハードウェア機能の搭載が必須ではないアプリの場合は、当然、その機能を搭載していないデバイスがアプリを使用している可能性があります。そのようなユーザーが、アプリを正常に利用できるようにするために、デバイスがその機能を搭載しているかどうかを実行時にチェックしましょう。

機能を搭載しているかどうかのチェックは以下のように実施します。

```java
// 以下のどちらかで実装します。

PackageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA);

getApplicationContext().getPackageManager().hasSystemFeature(
PackageManager.FEATURE_CAMERA)
```


## API レベルを指定して Permission を宣言する

以下の Permission は API レベル 23 以上、 33 以下のデバイスでのみ宣言されます。

```xml
<uses-permission-sdk-23 android:name="..."
    android:maxSdkVersion="33" />
```


## 従来の実装方法の例

以下のフローと API にそって実装が可能です。

<img src="./実行時 Permission の処理フロー.svg" width="800">

実際の実装例は、以下になります。ただし、以下の例では、 `hasSystemFeature()` 関数の呼び出しと `shouldShowRequestPermissionRationale()` の呼び出しは、省略されています。

```kotlin
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // カメラ permissions が許可されているか確認
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            // カメラ permissions をリクエスト
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
    }

    /**
     * REQUIRED_PERMISSIONS に含まれている全ての permission が許可されているかどうか.
     *
     * すべて許可されていれば true. それ以外は false.
     */
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        // 許可されているかどうかを確認したい permission のリスト
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }
}
```


## 新しい実装方法の例

新しい実装方法では、 `registerForActivityResult()` 関数を使用します。

権限を要求するパーミッションが一つなのか、複数なのかによって、 registerForActivityResult() のパラメータが異なります。


### 一つの権限を要求する場合

```kotlin
import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Permission ダイアログを表示
        // 【注意】
        // Manifest クラスはデフォルトでは、このプロジェクトの Manifest ファイルを参照してしまうため、
        // 自分で "import android.Manifest" を記述しなければいけない点に注意する。
        launcher.launch(Manifest.permission.CAMERA)
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // ユーザーが Permission を許可した場合
                Snackbar.make(getRootView(), "許可されました", Snackbar.LENGTH_SHORT).show()
            } else {
                // ユーザーが Permission を許可しなかった場合
                Snackbar.make(getRootView(), "否認されました", Snackbar.LENGTH_SHORT).show()
            }
        }

    private fun getRootView(): View {
        return findViewById(android.R.id.content)
    }
}
```


### 複数の権限を要求する場合

```kotlin
import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Permission ダイアログを表示
        // 【注意】
        // Manifest クラスはデフォルトでは、このプロジェクトの Manifest ファイルを参照してしまうため、
        // 自分で "import android.Manifest" を記述しなければいけない点に注意する。
        launcher.launch(
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            val cameraIsGranted = it[Manifest.permission.CAMERA] ?: false
            val locationIsGranted = it[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

            if (cameraIsGranted && locationIsGranted) {
                Snackbar.make(getRootView(), "許可されました", Snackbar.LENGTH_SHORT)
                    .show()
            } else {
                Snackbar.make(getRootView(), "否認されました", Snackbar.LENGTH_SHORT)
                    .show()
            }
        }

    private fun getRootView(): View {
        return findViewById(android.R.id.content)
    }

}
```


## 従来の実装方法と新しい実装方法の使い分け

従来の実装方法は、実装量も多く、実装の順番も複雑であるため、実装が難しいですが、その分、カスタマイズ性において優れています。

一方で、新しい実装方法は、実装量が少なく、実装が簡単である半面、途中で説明を表示する等のカスタマイズが難しい場合があります。

新しいプロジェクトやシンプルなケースでは、新しい実装方法を優先し、互換性や複雑な要件がある場合には従来の方法を採用するのが最適です。


### 使い分けのガイドライン

| 要件                                         | 適した方法                                         |
| -------------------------------------------- | -------------------------------------------------- |
| 古いプロジェクトや古いAPIの互換性が必要      | 従来の方法 (checkSelfPermission など)              |
| パーミッションの事前説明や高度なロジック     | 従来の方法 (shouldShowRequestPermissionRationale)  |
| 簡潔に書きたい / Jetpackを利用する場合       | registerForActivityResult + RequestPermission      |
| 複数のパーミッションを一括管理する必要がある | ActivityResultContracts.RequestMultiplePermissions |


## 新しい方法を Jetpack Compose で使用する方法（ Accompanist ライブラリを使用しない方法）

```kotlin
enum class PermissionState {  
    Checking,  
    Granted,  
    Denied,  
}  
  
@Composable  
private fun NeedPermissionScreen() {  
    var state by remember { mutableStateOf(PermissionState.Checking) }  
  
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {  
        state = if (it) PermissionState.Granted else PermissionState.Denied  
    }  
  
    val permission = Manifest.permission.CAMERA  
  
    val context = LocalContext.current  
    val lifecycleObserver = remember {  
        LifecycleEventObserver { _, event ->  
            if (event == Lifecycle.Event.ON_START) {  
                val result = context.checkSelfPermission(permission)  
                if (result != PackageManager.PERMISSION_GRANTED) {  
                    state = PermissionState.Checking  
                    launcher.launch(permission)  
                } else {  
                    state = PermissionState.Granted  
                }  
            }  
        }  
    }  
    val lifecycle = LocalLifecycleOwner.current.lifecycle  
    DisposableEffect(lifecycle, lifecycleObserver) {  
        lifecycle.addObserver(lifecycleObserver)  
        onDispose {  
            lifecycle.removeObserver(lifecycleObserver)  
        }  
    }  
  
    when (state) {  
        PermissionState.Checking -> {  
        }  
        PermissionState.Granted -> {  
            // TODO パーミッションが必要な機能を使う画面  
        }  
        PermissionState.Denied -> {  
            // TODO 拒否された時の画面  
        }  
    }  
}
```


### 引用元資料

- [Jetpack Compose で Runtime Permission をリクエストする](https://y-anz-m.blogspot.com/2021/06/jetpack-compose-runtime-permission.html)


## 新しい方法を Jetpack Compose で使用する方法（ Accompanist ライブラリを使用する方法１）

Accompanist ライブラリは、バージョンが 0.X なので、すべて Experimental な API だと思われます。

```kotlin
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AppScreen() {
    val permissionState = rememberPermissionState(Manifest.permission.READ_EXTERNAL_STORAGE)
    when {
        permissionState.hasPermission -> Text("Granted!")
        permissionState.shouldShowRationale -> PermissionRationaleDialog {
            permissionState.launchPermissionRequest()
        }
        permissionState.permissionRequested -> Text("Denied...")
        else -> SideEffect {
            permissionState.launchPermissionRequest()
        }
    }
}

@Composable
fun PermissionRationaleDialog(onDialogResult: ()->Unit) {
    AlertDialog(
        text = { Text("Rationale") },
        onDismissRequest = {},
        confirmButton = {
            TextButton(onClick = onDialogResult) {
                Text("OK")
            }
        }
    )
}
```

### 引用元資料

- [Jetpack ComposeでPermissionを取得したい](https://engawapg.net/jetpack-compose/1715/jetpack-compose-permission/)


## 新しい方法を Jetpack Compose で使用する方法（ Accompanist ライブラリを使用する方法２）

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RequestPermissionUsingAccompanist()
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun RequestPermissionUsingAccompanist() {
    // Permission Request を実行する Permission を定義する
    val permission = Manifest.permission.READ_EXTERNAL_STORAGE

    // Permission Request の実行を制御する State クラス
    val permissionState = rememberPermissionState(permission)
    PermissionRequired(
        permissionState = permissionState,
        permissionNotAvailableContent = {
            // Permission を拒否し、表示しないを押したときの View
            Text("Permission Denied.")
        }, permissionNotGrantedContent = {
            // Permission の許可を促すときの View
            Button(onClick = { permissionState.launchPermissionRequest() }) {
                Text("Request permission.")
            }
        }, content = {
            // Permission が許可されたときの View
            Text("Permission Granted.")
        }
    )
}
```

### 引用元資料

- [Jetpack Compose で Permission を要求する方法](https://zenn.dev/kaleidot725/articles/2021-11-13-jc-permission#%E7%89%B9%E5%AE%9A%E3%81%AE%E3%83%9C%E3%82%BF%E3%83%B3%E3%82%92%E6%8A%BC%E3%81%97%E3%81%9F%E3%82%89-permission-%E3%82%92%E8%A6%81%E6%B1%82%E3%81%99%E3%82%8B)




