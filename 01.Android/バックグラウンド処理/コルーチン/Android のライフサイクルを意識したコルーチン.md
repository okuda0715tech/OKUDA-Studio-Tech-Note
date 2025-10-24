- [Android のライフサイクルを意識したコルーチン](#android-のライフサイクルを意識したコルーチン)
  - [一覧](#一覧)
  - [Fragment の View のライフサイクルに連動したコルーチン](#fragment-の-view-のライフサイクルに連動したコルーチン)
  - [Fragment のライフサイクルに連動したコルーチン](#fragment-のライフサイクルに連動したコルーチン)


# Android のライフサイクルを意識したコルーチン

## 一覧

| 連動元           | コルーチンがキャンセルされるタイミング |
| ---------------- | -------------------------------------- |
| Fragment の View | Fragment の onDestroyView              |
| Fragment         | onDestroy                              |


## Fragment の View のライフサイクルに連動したコルーチン

viewLifecycleOwner.lifecycleScope を使ってコルーチンを起動すると、 Fragment の View が破棄されるときにコルーチンがキャンセルされます。これは Fragment の onDestroyView メソッドが呼ばれるタイミングです。

```kotlin
viewLifecycleOwner.lifecycleScope.launch {
    // コルーチンの処理
}
```

この場合、コルーチンは Fragment の onDestroyView が呼び出されるときにキャンセルされます。つまり、 Fragment の View のライフサイクルが終わるタイミングです。


## Fragment のライフサイクルに連動したコルーチン

lifecycleScope を使ってコルーチンを起動する場合 ( Fragment 自身のライフサイクルにバインドされる) 、コルーチンは Fragment の onDestroy が呼び出されるときにキャンセルされます：

```kotlin
lifecycleScope.launch {
    // コルーチンの処理
}
```

この場合、Fragment 全体のライフサイクルが終わるタイミングでコルーチンがキャンセルされます。


