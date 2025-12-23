- [Compose のマルチペイン](#compose-のマルチペイン)
  - [Window Size Class でレイアウト構造を切り替えるサンプル](#window-size-class-でレイアウト構造を切り替えるサンプル)
    - [WindowSizeClass の取得](#windowsizeclass-の取得)
    - [Single Pane（スマホ）](#single-paneスマホ)
    - [Two Pane（小型タブレット / Fold）](#two-pane小型タブレット--fold)
    - [Expanded（タブレット）](#expandedタブレット)


# Compose のマルチペイン

## Window Size Class でレイアウト構造を切り替えるサンプル

### WindowSizeClass の取得

```kotlin
@Composable
fun AppRoot() {
    val windowSizeClass = calculateWindowSizeClass(LocalContext.current as Activity)

    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            SinglePaneScreen()
        }
        WindowWidthSizeClass.Medium -> {
            TwoPaneScreen()
        }
        WindowWidthSizeClass.Expanded -> {
            MultiPaneScreen()
        }
    }
}
```


### Single Pane（スマホ）

```kotlin
@Composable
fun SinglePaneScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Spacing.l)
    ) {
        ListContent()
    }
}
```

### Two Pane（小型タブレット / Fold）

```kotlin
@Composable
fun TwoPaneScreen() {
    Row(Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(Spacing.m)
        ) {
            ListContent()
        }

        Box(
            modifier = Modifier
                .weight(2f)
                .padding(Spacing.l)
        ) {
            DetailContent()
        }
    }
}
```


### Expanded（タブレット）

内部 Dense / 境界 Spacious の Multi Pane 実装

---

DensityMode 定義 :

```kotlin
enum class DensityMode {
    Dense,
    Normal,
    Spacious
}
```

---

Density Context :

```kotlin
val LocalDensityMode = staticCompositionLocalOf { DensityMode.Normal }
```

---

Multi Pane Screen : 

```kotlin
@Composable
fun MultiPaneScreen() {
    CompositionLocalProvider(
        LocalDensityMode provides DensityMode.Dense
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.xl) // ← 境界は Spacious
        ) {
            ListPane()

            Spacer(modifier = Modifier.width(Spacing.xl))

            DetailPane()
        }
    }
}
```

---

ListPane（Dense） : 

```kotlin
@Composable
fun ListPane() {
    Column(
        modifier = Modifier
            .width(320.dp)
            .fillMaxHeight()
    ) {
        repeat(30) {
            ListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Spacing.touchTarget)
            )
        }
    }
}
```

---

DetailPane（Dense 内部） : 

```kotlin
@Composable
fun DetailPane() {
    Column(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
    ) {
        Text(
            text = "Detail",
            modifier = Modifier.padding(Spacing.m)
        )
    }
}
```



