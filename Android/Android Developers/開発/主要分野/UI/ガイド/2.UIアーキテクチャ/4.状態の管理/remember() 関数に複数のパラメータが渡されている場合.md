- [remember() 関数に複数のパラメータが渡されている場合](#remember-関数に複数のパラメータが渡されている場合)
  - [参考：ラムダ式内で参照しているプロパティはどこのものか？](#参考ラムダ式内で参照しているプロパティはどこのものか)


# remember() 関数に複数のパラメータが渡されている場合

Jetpack Compose の remember 関数に複数のパラメータ（キー）が指定されている場合、それらのいずれかが変更されると、remember 関数のラムダ式が再実行されます。

remember 関数は、指定されたキー（key1, key2, など）に基づいて状態を保持します。これらのキーのいずれかが変更されると、再評価がトリガーされ、ラムダ式が再度呼び出され、結果として新しい値が計算されて保存されます。キーが変更されなければ、保存された値がそのまま使用されます。

```kotlin
val myState = remember(key1, key2) {
    // ラムダ式の内容
    "Calculated Value"
}
```

この場合、key1 または key2 のいずれかが変更されたときに、ラムダ式の内容が再度実行され、myState に新しい値が保存されます。

つまり、remember の引数として指定されたキーがどれか一つでも変更されれば、ラムダ式が再評価されます。


## 参考：ラムダ式内で参照しているプロパティはどこのものか？

remember() 関数のラムダ式内の MyAppState() コンストラクタのパラメータに指定されている drawerState は、 rememberMyAppState 関数のパラメータ drawerState を参照しています。 ( remember 関数のパラメータを参照しているわけではありません。結果的に同じものを参照してはいますが、一応、文法的な違いとして、参考までに。)

```kotlin
@Stable
class MyAppState(
    private val drawerState: DrawerState,
    private val navController: NavHostController
) { /* ... */ }

@Composable
fun rememberMyAppState(
    drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed),
    navController: NavHostController = rememberNavController()
): MyAppState = remember(drawerState, navController) {
    MyAppState(drawerState, navController)
}
```


