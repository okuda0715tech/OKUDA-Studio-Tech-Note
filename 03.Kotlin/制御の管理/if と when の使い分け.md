- [if と when の使い分け](#if-と-when-の使い分け)
  - [when を使うと良いケース](#when-を使うと良いケース)
  - [if を使うと良いケース](#if-を使うと良いケース)


# if と when の使い分け

## when を使うと良いケース

3 つ以上の明確な条件分岐があり、かつ、一つの値について判定する場合は、 when を使うと簡潔に記述できます。

```kotlin
when (status) {
    Status.Loading -> showLoading()
    Status.Success -> showContent()
    Status.Error -> showError()
}
```


## if を使うと良いケース

条件分岐が二つしかなく、かつ、二つ目が else の場合は、 if を使うと簡潔に記述できます。

```kotlin
_uiState.update {
    if (resultFailure) {
        // 削除に失敗した場合
        it.copy(userMessage = R.string.common_delete_failed)
    } else {
        // 削除に成功した場合
        it.copy(showDelCompDialog = true)
    }
}
```

これをもし、 when で記述すると以下のようになります。

```kotlin
_uiState.update {
    when (resultFilure) {
        true -> {
            // 削除に失敗した場合
            it.copy(userMessage = R.string.common_delete_failed)
        }

        false -> {
            // 削除に成功した場合
            it.copy(showDelCompDialog = true)        
        }
    }
}
```

when の場合は、ネストが一段深くなる可能性が高いため、 when の内側にさらに別の when が入る場合などは、 if に比べてどんどんとネストが深くなっていく可能性があります。
