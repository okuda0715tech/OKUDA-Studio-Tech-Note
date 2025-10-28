- [MutableInteractionSource の使用方法](#mutableinteractionsource-の使用方法)
  - [基本的な使い方の手順](#基本的な使い方の手順)
  - [使用例](#使用例)
  - [この例のポイント](#この例のポイント)
  - [clickable 関数の `indication` パラメータについて](#clickable-関数の-indication-パラメータについて)
  - [`indication` の使い方](#indication-の使い方)
  - [例: リップルエフェクトを無効にする](#例-リップルエフェクトを無効にする)
  - [カスタムリップルの例](#カスタムリップルの例)
    - [`rememberRipple()` の主なパラメータ](#rememberripple-の主なパラメータ)
  - [まとめ](#まとめ)


# MutableInteractionSource の使用方法

MutableInteractionSource は、Jetpack Compose の中でユーザーの操作やインタラクション（タップ、ホバー、フォーカスなど）をトラッキングするために使用されるクラスです。このクラスを利用することで、コンポーネントが受け取るユーザーのインタラクション状態を監視したり、UI の見た目を変更したりすることができます。

MutableInteractionSource の一般的な使用方法としては、ボタンやテキストフィールドなどのコンポーネントに対して、タップやクリックなどのインタラクションを監視し、その状態に応じてスタイルや動作を変更する場合が考えられます。


## 基本的な使い方の手順

1. remember { MutableInteractionSource() } を使って MutableInteractionSource のインスタンスを作成します。
2. 作成した MutableInteractionSource を UI コンポーネントに渡し、そのコンポーネントのインタラクションをキャプチャします。
3. interactionSource.interactions.collect() を使って、インタラクションの状態に基づいた処理を行います。


## 使用例

以下の例は、ボタンをタップしたときに、そのインタラクションを監視し、UI に変化を与える例です。

```kotlin
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressedInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@Composable
fun InteractiveCircle() {
    // InteractionSourceを作成
    val interactionSource = remember { MutableInteractionSource() }
    
    // 現在押されているかどうかを管理するための状態
    var isPressed by remember { mutableStateOf(false) }
    
    // コルーチンでインタラクションの変更を監視
    LaunchedEffect(interactionSource) {
        launch {
            interactionSource.interactions.collect { interaction ->
                when (interaction) {
                    is PressedInteraction.Press -> isPressed = true
                    is PressedInteraction.Release -> isPressed = false
                }
            }
        }
    }

    // UIの表示
    Box(
        modifier = Modifier
            .size(100.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null // インジケーターをオフにする（デフォルトのリップルを無効化）
            ) {}
    ) {
        Surface(
            shape = CircleShape,
            color = if (isPressed) Color.Red else Color.Green,
            modifier = Modifier.size(100.dp)
        ) {}
    }
}

@Preview
@Composable
fun PreviewInteractiveCircle() {
    InteractiveCircle()
}
```


## この例のポイント

1. MutableInteractionSource の作成: val interactionSource = remember { MutableInteractionSource() } を使って、インタラクションをキャプチャするためのインスタンスを作成しています。

2. コルーチンを使ってインタラクションを監視: LaunchedEffect ブロックの中で、interactionSource.interactions.collect {} を使用して、押下やリリースのイベントを監視し、それに応じて isPressed の状態を変更しています。

3. clickable の使用: Modifier.clickable() に interactionSource を渡すことで、クリック（タップ）イベントをキャプチャし、インタラクションを追跡できるようにしています。

このように、MutableInteractionSource を使うことで、ユーザーのインタラクションに応じた状態管理が容易にでき、よりインタラクティブなUIを作成できます。


## clickable 関数の `indication` パラメータについて

`indication` パラメータは、`clickable` や `selectable` などのコンポーネントで、ユーザーがコンポーネントをタップやクリックした際に、視覚的なフィードバック（インジケーター）を表示するために使用されるパラメータです。典型的な例は、リップルエフェクト（波紋のような効果）です。

Jetpack Compose では、`indication` パラメータを使うことで、デフォルトのクリックエフェクトをカスタマイズしたり、完全に無効化したりすることができます。


## `indication` の使い方

- **デフォルトのリップルエフェクトを使う**: `indication` パラメータを省略すると、デフォルトでリップルエフェクトが適用されます。
- **インジケーションを無効化する**: `indication = null` とすることで、視覚的なフィードバックをオフにすることができます。
- **カスタムのインジケーションを作成する**: `indication` に自作のエフェクトを指定することで、独自のビジュアルフィードバックを適用することができます。


## 例: リップルエフェクトを無効にする

```kotlin
Box(
    modifier = Modifier
        .size(100.dp)
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null // リップルエフェクトを無効化
        ) {}
)
```


## カスタムリップルの例

リップルの色や形状をカスタマイズする場合は、`rememberRipple()` を使用できます。例えば、リップルの色を変更する場合:

```kotlin
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.ui.graphics.Color

Box(
    modifier = Modifier
        .size(100.dp)
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = rememberRipple(color = Color.Red) // カスタムリップル（赤色）
        ) {}
)
```


### `rememberRipple()` の主なパラメータ

- **color**: リップルの色を指定できます。デフォルトはシステムテーマに依存します。
- **bounded**: `true` でボタンの境界内にリップルが収まるようにします。`false` で境界を超えて広がります。
- **radius**: リップルのサイズを指定できます。デフォルトはコンポーネントに応じた自動設定です。


## まとめ

- `indication` パラメータは、ユーザー操作時の視覚的フィードバック（リップルなど）を制御するために使います。
- `indication = null` でエフェクトを無効にできます。
- `rememberRipple()` を使って、リップルの色やサイズなどをカスタマイズすることが可能です。
