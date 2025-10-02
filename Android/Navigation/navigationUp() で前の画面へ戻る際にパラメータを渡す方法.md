- [navigationUp() で前の画面へ戻る際にパラメータを渡す方法](#navigationup-で前の画面へ戻る際にパラメータを渡す方法)
  - [例](#例)


# navigationUp() で前の画面へ戻る際にパラメータを渡す方法

## 例

**パラメータを渡す側の実装**

navigateUp() メソッドを呼び出す前に、 NavBackStackEntry を取得し、  
その中にパラメータを格納して渡すことができます。

```kotlin
val navController = findNavController()
val navBackStackEntry = navController.previousBackStackEntry
navBackStackEntry?.savedStateHandle?.set("key", value)

navController.navigateUp()
```

**パラメータを受け取る側の実装**

onViewCreated() などのメソッド内で NavBackStackEntry を取得し、  
パラメータを取り出します。

```kotlin
val navBackStackEntry = findNavController().currentBackStackEntry
val value = navBackStackEntry?.savedStateHandle?.get("key")
```
