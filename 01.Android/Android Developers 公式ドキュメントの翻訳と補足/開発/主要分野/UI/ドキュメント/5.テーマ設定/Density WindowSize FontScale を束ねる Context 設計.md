- [Density / WindowSize / FontScale を束ねる Context 設計](#density--windowsize--fontscale-を束ねる-context-設計)
  - [統合された UI Context](#統合された-ui-context)
  - [CompositionLocal](#compositionlocal)
  - [提供側（App Root）](#提供側app-root)
  - [使う側](#使う側)
  - [currentFontScaleMode() 関数の中身](#currentfontscalemode-関数の中身)
  - [Typography Token（FontScale 完全連動）](#typography-tokenfontscale-完全連動)
  - [Layout Token（Column / Row の標準余白）](#layout-tokencolumn--row-の標準余白)
  - [Interaction Token（Hover / Focus / Press）](#interaction-tokenhover--focus--press)


# Density / WindowSize / FontScale を束ねる Context 設計

## 統合された UI Context

```kotlin
data class UiEnvironment(
    val widthSizeClass: WindowWidthSizeClass,
    val densityMode: DensityMode,
    val fontScaleMode: FontScaleMode
)
```

`DensityMode` の実装については、 [スペーストークン.md](./スペーストークン.md) を参照してください。


## CompositionLocal

```kotlin
// ここは static じゃないほうが良い気がする。
// なぜなら、値が変わったら画面を対象の画面だけを更新すればいい気がするので。
val LocalUiEnvironment = staticCompositionLocalOf<UiEnvironment> {
    error("UiEnvironment not provided")
}
```


## 提供側（App Root）

```kotlin
@Composable
fun ProvideUiEnvironment(content: @Composable () -> Unit) {
    // ここで取得される画面の幅と高さは、画面回転 ( Landscape / Portrait ) に応じて、入れ変わります。
    val windowSizeClass =
        calculateWindowSizeClass(LocalContext.current as Activity)

    // currentFontScaleMode() 関数の定義は後述します。
    val fontScaleMode = currentFontScaleMode()

    val densityMode = when {
        // スマホ + 大きな文字 → Spacious
        windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact
            && fontScaleMode >= FontScaleMode.ExtraLarge ->
                DensityMode.Spacious

        // タブレットは基本 Dense
        windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded ->
            DensityMode.Dense

        else ->
            DensityMode.Normal
    }

    CompositionLocalProvider(
        LocalUiEnvironment provides UiEnvironment(
            widthSizeClass = windowSizeClass.widthSizeClass,
            densityMode = densityMode,
            fontScaleMode = fontScaleMode
        )
    ) {
        content()
    }
}
```


## 使う側

```kotlin
@Composable
fun Example() {
    val env = LocalUiEnvironment.current

    if (env.widthSizeClass == WindowWidthSizeClass.Expanded) {
        MultiPaneScreen()
    } else {
        SinglePaneScreen()
    }
}
```


## currentFontScaleMode() 関数の中身

```kotlin
enum class FontScaleMode {
    Normal,
    Large,
    ExtraLarge,
    Accessibility
}
```

```kotlin
@Composable
fun currentFontScaleMode(): FontScaleMode {
    val fontScale = LocalDensity.current.fontScale
    return when {
        fontScale <= 1.0f -> FontScaleMode.Normal
        fontScale <= 1.3f -> FontScaleMode.Large
        fontScale <= 1.6f -> FontScaleMode.ExtraLarge
        else -> FontScaleMode.Accessibility
    }
}
```


## Typography Token（FontScale 完全連動）

- ポイント
  - sp を直接使わない
  - fontScale × density を吸収

```kotlin
object TypographyTokens {
    val body: TextStyle
        @Composable get() = when (currentFontScaleMode()) {
            FontScaleMode.Normal ->
                TextStyle(fontSize = 14.sp, lineHeight = 20.sp)
            FontScaleMode.Large ->
                TextStyle(fontSize = 16.sp, lineHeight = 24.sp)
            FontScaleMode.Accessibility ->
                TextStyle(fontSize = 18.sp, lineHeight = 28.sp)
        }
}
```


## Layout Token（Column / Row の標準余白）

- ポイント
  - Column のたびに padding を考えない

```kotlin
object LayoutTokens {
    val screenPadding: Dp
        @Composable get() = Spacing.l

    val sectionSpacing: Dp
        @Composable get() = Spacing.m

    val itemSpacing: Dp
        @Composable get() = Spacing.s
}
```


## Interaction Token（Hover / Focus / Press）

```kotlin
object InteractionTokens {
    val hoverAlpha = 0.08f
    val pressAlpha = 0.12f
    val focusAlpha = 0.16f
}
```


```kotlin
Modifier
    .background(
        color.copy(alpha = InteractionTokens.hoverAlpha)
    )
```



