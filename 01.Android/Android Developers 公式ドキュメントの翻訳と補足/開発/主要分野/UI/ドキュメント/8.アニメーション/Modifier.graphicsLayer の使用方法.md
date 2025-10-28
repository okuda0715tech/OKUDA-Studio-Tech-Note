- [Modifier.graphicsLayer の使用方法](#modifiergraphicslayer-の使用方法)
  - [基本的な使い方](#基本的な使い方)
    - [パラメータの説明](#パラメータの説明)
  - [応用例：アニメーションで使用](#応用例アニメーションで使用)
  - [graphicsLayer の注意点](#graphicslayer-の注意点)
    - [パフォーマンスの観点](#パフォーマンスの観点)
    - [drawXxxx 関数は使用できない](#drawxxxx-関数は使用できない)


# Modifier.graphicsLayer の使用方法

Modifier.graphicsLayer は Jetpack Compose で UI 要素に対してグラフィックエフェクトを適用するために使用されます。具体的には、回転、スケーリング、透明度、影などのグラフィック変換を簡単に行うことができます。graphicsLayer のオプションを使うことで、カスタマイズされたアニメーションや視覚効果を作成できます。


## 基本的な使い方

Modifier.graphicsLayer は Modifier にチェーンとして追加して使います。以下に基本的なパラメータとその使い方を説明します。

```kotlin
Box(
    modifier = Modifier
        .size(200.dp)
        .graphicsLayer(
            rotationZ = 45f, // Z軸の回転（ここでは45度）
            scaleX = 1.5f,   // X軸方向の拡大率
            scaleY = 1.5f,   // Y軸方向の拡大率
            alpha = 0.8f,    // 透明度
            shadowElevation = 8.dp.toPx() // 影の高さ
        )
        .background(Color.Blue)
)
```

### パラメータの説明

- alpha: UI 要素の透明度（0f は完全透明、1f は不透明）。
- scaleX / scaleY: X軸とY軸方向のスケール（1f で元のサイズ）。
- rotationX / rotationY / rotationZ: X、Y、Z軸方向への回転。
- shadowElevation: 要素に対する影の高さ。dp を toPx() メソッドでピクセルに変換して使用します。
- translationX / translationY: X軸・Y軸の方向への平行移動。


## 応用例：アニメーションで使用

Modifier.graphicsLayer を使ったアニメーションも簡単に作成できます。たとえば、animateFloatAsState を使って回転アニメーションを行う例です。

```kotlin
val rotation by animateFloatAsState(targetValue = if (isRotating) 360f else 0f)

Box(
    modifier = Modifier
        .size(200.dp)
        .graphicsLayer(
            rotationZ = rotation
        )
        .background(Color.Green)
        .clickable { isRotating = !isRotating }
)
```

この例では、ボックスをクリックするたびに回転アニメーションがトリガーされます。


## graphicsLayer の注意点

### パフォーマンスの観点

graphicsLayer は直接ビットマップとしてレンダリングされるため、Modifier.scale や Modifier.rotate などの他のModifierに比べてパフォーマンスがやや低下する可能性があります。グラフィックの効果を統合的に管理したい場合に使いますが、単純な操作の場合は他の専用の Modifier を使う方が効率的です。

このように、graphicsLayer を使うと、さまざまなグラフィック変換やアニメーションをCompose内で簡単に実現することができます。


### drawXxxx 関数は使用できない

Modifier.graphicsLayer ブロック内では、drawRect 関数を使用することはできません。graphicsLayer は、レイヤーに対する視覚効果（回転、スケール、アルファブレンディングなど）を適用するための修飾子であり、カスタム描画のための API ではありません。

drawRect やその他の描画関数を使用するには、Canvas コンポーザブル内で行う必要があります。 graphicsLayer はあくまでも描画結果に変換やエフェクトを適用するためのものです。

**要するに、 drawXxxx 関数は、存在しないものを描画して生成する関数であるのに対し、 graphicsLayer は、既に存在するものに対して、 (アニメーションを含む) 効果を適用するものです。**

例えば、Canvas で Rect を描画した後、Modifier.graphicsLayer を使ってその描画に回転や拡大縮小を適用することは可能です。以下はその例です。

