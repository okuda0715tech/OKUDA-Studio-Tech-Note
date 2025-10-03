- [Jetpack Compose の Surface コンポーザブル](#jetpack-compose-の-surface-コンポーザブル)
  - [主な機能](#主な機能)
  - [使用するシチュエーション](#使用するシチュエーション)
  - [M3 Surface のパラメータの color と contentColor の違い](#m3-surface-のパラメータの-color-と-contentcolor-の違い)


# Jetpack Compose の Surface コンポーザブル

`Surface` コンポーザブルは、 Jetpack Compose で UI コンポーネントの背景やカードやボタン等のスタイルをカスタマイズするために使用されます。 `Surface` は、色、形、影、枠線などを簡単に設定できるように設計されたコンポーネントで、これにより UI の一貫性を保ちながらデザインのカスタマイズが可能になります。


## 主な機能

- **背景色の設定**: `color` パラメータを使って背景色を設定できます。
- **形状のカスタマイズ**: `shape` パラメータを使用して角丸やその他の形状を設定できます。
- **影の追加**: `elevation` パラメータで影の深さを設定し、立体感を持たせることができます。
  - Material Design 3 の Surface には、トーンで深さを設定する `tonalElevation` と、影で深さを表す `shadowElevation` が存在します。
- **コンテンツのクリック反応**: `onClick` パラメータを使って、`Surface` 全体がクリック可能な領域として機能させることができます。
  - Button 同様に、タップ時にリップルエフェクトが発生します。
  - ただし、 M3 Surface には、 onClick パラメータはついていません。 onClick を使用したい場合は、代わりに、 SurfaceButton コンポーザブルを使用してください。


## 使用するシチュエーション

- **カードやボタン**: 背景色や角丸、影を追加してカードやボタンをデザインする際に使用されます。
- **コンポーネントのレイアウト**: 視覚的なグループ化を行い、UI コンポーネントをわかりやすく分離するために利用されます。
- **テーマに基づいたデザインの適用**: アプリのテーマに従った色やスタイルを適用する際に役立ちます。

例えば、以下のように `Surface` を使用して、ボタン状の UI コンポーネントを作成できます。

```kotlin
Surface(
    color = MaterialTheme.colorScheme.primary,
    shape = RoundedCornerShape(8.dp),
    shadowElevation = 4.dp,
    modifier = Modifier.padding(8.dp)
) {
    Text(text = "Hello, Surface!", modifier = Modifier.padding(16.dp))
}
```

実行結果は以下のようになります。

<img src="./画像/Surface の実装例.png" width="300">


## M3 Surface のパラメータの color と contentColor の違い

- color : Surface の背景色となります。
- contentColor : CompositionLocal を介して、子コンポーザブルのテキストやアイコンの色として使用されます。





