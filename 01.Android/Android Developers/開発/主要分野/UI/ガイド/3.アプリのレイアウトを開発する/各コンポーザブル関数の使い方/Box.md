- [Box のよくある使用例](#box-のよくある使用例)
  - [1. 子要素どうしを重ね合わせる](#1-子要素どうしを重ね合わせる)
  - [2. 子要素の位置指定（寄せ）](#2-子要素の位置指定寄せ)
  - [3. 背景色や形状の設定](#3-背景色や形状の設定)
  - [4. スクロール可能な領域の作成](#4-スクロール可能な領域の作成)
  - [5. Box 内でのレイアウトのカスタマイズ](#5-box-内でのレイアウトのカスタマイズ)
  - [6. Box のサイズと同じサイズの子要素を生成する](#6-box-のサイズと同じサイズの子要素を生成する)


# Box のよくある使用例

`Box` は、Jetpack Compose においてシンプルで柔軟なレイアウトを提供するコンポーザブル関数です。子要素を重ねたり、特定の位置に配置するのに役立ちます。以下に `Box` コンポーザブル関数のよくある使用方法を紹介します。

## 1. 子要素どうしを重ね合わせる

`Box` を使うと、複数の子要素を重ねて配置できます。例えば、テキストの上に画像を重ねるといったことができます。

```kotlin
Box(
    modifier = Modifier.size(200.dp)
) {
    Image(
        painter = painterResource(id = R.drawable.sample_image),
        contentDescription = "Sample Image",
        modifier = Modifier.fillMaxSize()
    )
    Text(
        text = "Overlay Text",
        color = Color.White,
        modifier = Modifier.align(Alignment.Center)
    )
}
```


## 2. 子要素の位置指定（寄せ）

`Box` 内で `Modifier.align()` と `Alignment` を使って、各子要素を Box 内のどこ寄せにするかを指定できます。中央、左上、右下などの位置に配置可能です。

```kotlin
Box(
    modifier = Modifier.size(200.dp)
) {
    Text(
        text = "Top Start",
        modifier = Modifier.align(Alignment.TopStart)
    )
    Text(
        text = "Bottom End",
        modifier = Modifier.align(Alignment.BottomEnd)
    )
}
```


## 3. 背景色や形状の設定

`Box` は、背景色や形状を設定するのにも使われます。`Modifier.background()` や `Modifier.clip()` を使用して、背景をカスタマイズできます。

```kotlin
Box(
    modifier = Modifier
        .size(100.dp)
        // 角丸を指定します。
        .clip(RoundedCornerShape(16.dp))
        .background(Color.Blue)
) {
    Text(
        text = "Rounded Box",
        color = Color.White,
        modifier = Modifier.align(Alignment.Center)
    )
}
```

**注意** : Modifier を指定する際に、 clip を指定してから background を指定する必要があります。もし background を先に指定すると青色の塗りつぶしを実行した後に角丸が実行されるため、 Box の端まできっちりと青色になってしまいます。


## 4. スクロール可能な領域の作成

`Box` を使用して、スクロール可能な領域を作成することも可能です。例えば、縦方向にスクロールできるリストを `Box` 内に配置できます。

```kotlin
Box(
    modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
) {
    Column {
        for (i in 1..50) {
            Text(text = "Item $i", modifier = Modifier.padding(8.dp))
        }
    }
}
```

## 5. Box 内でのレイアウトのカスタマイズ

`BoxWithConstraints` を使用すると、`Box` 内のレイアウトを親のサイズに応じてカスタマイズすることができます。 BoxWithConstraints のサイズに基づいて子要素の配置やスタイルを動的に変更できます。

```kotlin
BoxWithConstraints(
    modifier = Modifier.size(200.dp)
) {
    if (maxWidth < 300.dp) {
        Text("Small width", Modifier.align(Alignment.Center))
    } else {
        Text("Large width", Modifier.align(Alignment.Center))
    }
}
```

`Box` は非常に柔軟で、さまざまなレイアウトシナリオに対応できるため、特に重ね合わせやカスタムレイアウトが必要な場合によく使用されます。


## 6. Box のサイズと同じサイズの子要素を生成する

`Modifier.matchParentSize()` を使用すると、それを設定した Box の子要素が Box と同じサイズになります。

**注意** : matchParentSize() は、 Box の子要素でしか使用できない関数です。

```kotlin
@Composable
fun MyScreen() {
    Box {
        Spacer(
            Modifier
                .matchParentSize()
                .background(Color.Green)
        )
        Row {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "image",
                modifier = Modifier.size(40.dp)
            )
            Text(
                "abc",
                color = Color.White,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }
}
```

**注意** : 上記の例では、 Spacer 自身にはサイズの指定がありません。そのため、 Box 内の要素が Spacer だけの場合は、 Box のサイズが 0 になってしまい、画面に何も表示されません。 Box 内に実際にサイズを持つ他の要素を配置することで、 Spacer が Box のサイズに連動するようになります。





