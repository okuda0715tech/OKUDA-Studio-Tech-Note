- [Jetpack Compose チュートリアル](#jetpack-compose-チュートリアル)
  - [レッスン１：コンポーズ可能な関数](#レッスン１コンポーズ可能な関数)
    - [setContent](#setcontent)
    - [コンポーズ可能な関数を定義する](#コンポーズ可能な関数を定義する)
    - [プレビューする](#プレビューする)
  - [レッスン２：レイアウト](#レッスン２レイアウト)
    - [列を定義する](#列を定義する)
    - [修飾子を使用してサイズやマージンを調整する](#修飾子を使用してサイズやマージンを調整する)
  - [レッスン３：マテリアルデザイン](#レッスン３マテリアルデザイン)
    - [テリアルデザイン（テーマ）を使用する](#テリアルデザインテーマを使用する)
    - [色](#色)
    - [タイポグラフィ](#タイポグラフィ)
    - [形状](#形状)
    - [ダークテーマ](#ダークテーマ)
  - [レッスン４：リストとアニメーション](#レッスン４リストとアニメーション)
    - [リストを作成する](#リストを作成する)
    - [リストアイテムの展開・折り畳み](#リストアイテムの展開折り畳み)
    - [アニメーションをつける](#アニメーションをつける)


# Jetpack Compose チュートリアル

## レッスン１：コンポーズ可能な関数

### setContent

`setContent` ブロックでは、コンポーズ可能な関数が呼び出されます。このブロック内で、アクティビティのレイアウトを定義します。

```kotlin
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Text("Hello world!")
        }
    }
}
```


### コンポーズ可能な関数を定義する

コンポーズ可能な関数は、関数名に `@Composable` アノテーションを追加するだけで作成できます。

コンポーズ可能な関数は、他のコンポーズ可能な関数からのみ呼び出すことができます。

```kotlin
// ...
import androidx.compose.runtime.Composable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MessageCard("Android")
        }
    }
}

@Composable
fun MessageCard(name: String) {
    Text(text = "Hello $name!")
}
```


### プレビューする

`@Preview` アノテーションを使用すると、アプリを Android デバイスやエミュレータにインストールしなくても、 Android Studio 内でコンポーズ可能な関数をプレビューできます。

@Preview アノテーションは、パラメータを受け取らないコンポーズ可能な関数に付与する必要があります。


## レッスン２：レイアウト

UI 要素は階層状で、要素が他の要素に含まれます。 Compose 内で、他のコンポーズ可能な関数からコンポーズ可能な関数を呼び出すことで、 UI 階層を作成します。


### 列を定義する

`Column` 関数を使用すると列を定義することができます。 Column 内に定義された Composable オブジェクトは、一つの列内に並んで表示されます。

同様に、 `Row` は一つの行を定義します。 `Box` は、 Column や Row では定義できない要素同士の重なりを定義できます。


### 修飾子を使用してサイズやマージンを調整する

各 Composable 要素に `androidx.compose.ui.Modifier` クラスを使用して要素のサイズやマージンなどを指定します。

```kotlin
@Composable
fun MessageCard(msg: Message) {
    // Add padding around our message
    Row(modifier = Modifier.padding(all = 8.dp)) {
        Image(
            painter = painterResource(R.drawable.profile_picture),
            contentDescription = "Contact profile picture",
            modifier = Modifier
                // Set image size to 40 dp
                .size(40.dp)
                // Clip image to be shaped as a circle
                .clip(CircleShape)
        )

        // Add a horizontal space between the image and the column
        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(text = msg.author)
            // Add a vertical space between the author and message texts
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = msg.body)
        }
    }
}
```


## レッスン３：マテリアルデザイン

Compose はマテリアルデザインの原則をサポートするために作られています。 UI 要素の多くは、最初からマテリアルデザインを実装しています。このレッスンでは、マテリアルデザインウィジェットを使用してアプリのスタイルを設定します。


### テリアルデザイン（テーマ）を使用する

`themes.xml` ファイル内で定義されたテーマの名称の末尾に Theme が付与された `XxxTheme` という名前でテーマを指定することが可能です。

```xml
<resources>

    <style name="Theme.MyFirstComposable" parent="android:Theme.Material.Light.NoActionBar" />
</resources>
```

この例の場合、 xml では、 "MyFirstComposable" という名前でテーマが定義されているため、 Kotlin では、 "MyFirstComposableTheme" という名前でテーマを参照できます。 Surface など、他のテーマ属性も参照できます。

```kotlin
// ...

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyFirstComposableTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MessageCard(Message("Android", "Jetpack Compose"))
                }
            }
        }
    }
}
```


### 色

```kotlin
import androidx.compose.foundation.border
import androidx.compose.material3.MaterialTheme

@Composable
fun MessageCard(msg: Message) {
    Row(modifier = Modifier.padding(all = 8.dp)) {
        Image(
            painter = painterResource(R.drawable. profile_picture),
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                // 変更箇所
                .border(1.5.dp, MaterialTheme.colorScheme. primary, CircleShape)
        )
 
        Spacer(modifier = Modifier.width(8.dp))
 
        Column {
            Text(
                text = msg.author,
                // 変更箇所
                color = MaterialTheme.colorScheme.secondary
            )
 
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = msg.body)
        }
    }
}
```


### タイポグラフィ

```kotlin
// ...

@Composable
fun MessageCard(msg: Message) {
    Row(modifier = Modifier.padding(all = 8.dp)) {
        Image(
            painter = painterResource(R.drawable. profile_picture),
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colorScheme. primary, CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
 
        Column {
            Text(
                text = msg.author,
                color = MaterialTheme.colorScheme.secondary,
                // 変更箇所
                style = MaterialTheme.typography.titleSmall
            )
 
            Spacer(modifier = Modifier.height(4.dp))
 
            Text(
                text = msg.body,
                // 変更箇所
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
```


### 形状

```kotlin
// ...
import androidx.compose.material3.Surface

@Composable
fun MessageCard(msg: Message) {
    Row(modifier = Modifier.padding(all = 8.dp)) {
        Image(
            painter = painterResource(R.drawable.profile_picture),
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(
                text = msg.author,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleSmall
            )
 
            Spacer(modifier = Modifier.height(4.dp))
 
            // 変更箇所
            Surface(shape = MaterialTheme.shapes.medium,  shadowElevation = 1.dp) {
                Text(
                    text = msg.body,
                    // 変更箇所
                    modifier = Modifier.padding(all = 4.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
```


### ダークテーマ

Jetpack Compose は、マテリアルデザインをサポートしているため、 デフォルトでダークテーマを処理できます。 Composable にテーマを設定していれば、ダークモードはデフォルトで実装されるものと思われます。ただし、プレビューするためには、別途ダークテーマのプレビューを設定する必要があるようです。

```kotlin
// ...
import android.content.res.Configuration

@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun PreviewMessageCard() {
    ComposeTutorialTheme {
        Surface {
            MessageCard(
                msg = Message("Lexi", "Hey, take a look at Jetpack Compose, it's great!")
            )
        }
    }
}
```


## レッスン４：リストとアニメーション

### リストを作成する

リストを作成するには、 `LazyColumn` クラス、もしくは、 `LazyRow` クラスを使用します。各クラスの内部には、 `items` 関数を定義して、その引数に List を渡します。 items 関数のラムダ式には、 List の要素が渡されます。

```kotlin
// ...
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

@Composable
fun Conversation(messages: List<Message>) {
    LazyColumn {
        items(messages) { message ->
            MessageCard(message)
        }
    }
}

@Preview
@Composable
fun PreviewConversation() {
    ComposeTutorialTheme {
        Conversation(SampleData.conversationSample)
    }
}
```


### リストアイテムの展開・折り畳み

リストアイテムが展開されているかどうかの状態を保存する変数を isExpanded という名前で定義します。 remember 関数は、そのラムダ式の結果を返します。つまり、 isExpanded 変数は、 MutableState 型のオブジェクトになります。

`Modifier.clickable` のラムダ式には、クリック時の処理を記述します。

isExpanded の状態に応じて、 Text の最大行数を変更します。 remember 関数と MutableState オブジェクトを使用して生成された isExpanded 変数は監視されており、値が変更される度に、その変数を参照しているコンポーザブルオブジェクトが再描画されます。この仕組みを [再コンポジション](https://developer.android.com/develop/ui/compose/mental-model?hl=ja&_gl=1*zcco5k*_up*MQ..*_ga*MTU2MTIyOTE0MS4xNzE5MjM5MTIx*_ga_6HH9YJMN9M*MTcxOTI4NDIxMC4yLjAuMTcxOTI4NDIxMC4wLjAuMA..#recomposition) と呼びます。

```kotlin
@Composable
fun MessageCard(msg: Message) {
    Row(modifier = Modifier.padding(all = 8.dp)) {
        Image(
            painter = painterResource(R.drawable.profile_picture),
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))

        // 変更箇所
        var isExpanded by remember { mutableStateOf(false) }

        // 変更箇所
        Column(modifier = Modifier.clickable { isExpanded = !isExpanded }) {
            Text(
                text = msg.author,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(modifier = Modifier.height(4.dp))

            Surface(
                shape = MaterialTheme.shapes.medium,
                shadowElevation = 1.dp,
            ) {
                Text(
                    text = msg.body,
                    modifier = Modifier.padding(all = 4.dp),
                    // 変更箇所
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
```


### アニメーションをつける

`animateColorAsState` 関数は、色を徐々に変更するアニメーションを行う関数です。

`animateContentSize` 関数は、コンテンツのサイズを徐々に変更するアニメーションを行う関数です。

```kotlin
// ...
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize

@Composable
fun MessageCard(msg: Message) {
    Row(modifier = Modifier.padding(all = 8.dp)) {
        Image(
            painter = painterResource(R.drawable.profile_picture),
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colorScheme.secondary, CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))

        var isExpanded by remember { mutableStateOf(false) }
        // 変更箇所
        val surfaceColor by animateColorAsState(
            if (isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
        )

        Column(modifier = Modifier.clickable { isExpanded = !isExpanded }) {
            Text(
                text = msg.author,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(modifier = Modifier.height(4.dp))

            Surface(
                shape = MaterialTheme.shapes.medium,
                shadowElevation = 1.dp,
                // 変更箇所
                color = surfaceColor,
                // 変更箇所
                modifier = Modifier.animateContentSize().padding(1.dp)
            ) {
                Text(
                    text = msg.body,
                    modifier = Modifier.padding(all = 4.dp),
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
```






