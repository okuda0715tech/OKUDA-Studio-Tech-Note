- [Column Row Box](#column-row-box)
  - [Column と Row の子の位置を設定する](#column-と-row-の子の位置を設定する)
  - [Box の子の位置を設定する](#box-の子の位置を設定する)


# Column Row Box

## Column と Row の子の位置を設定する

`horizontalArrangement` と `verticalAlignment` 引数を設定します。

```kotlin
@Composable
fun ArtistCardArrangement(artist: Artist) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        Image(bitmap = artist.image, contentDescription = "Artist image")
        Column { /*...*/ }
    }
}
```


## Box の子の位置を設定する

```kotlin
@Composable
fun CenteredBoxSample() {
    Box(
        modifier = Modifier
            .fillMaxSize() // 親のサイズいっぱいに広げる
    ) {
        Text(
            text = "中央に表示",
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
```










