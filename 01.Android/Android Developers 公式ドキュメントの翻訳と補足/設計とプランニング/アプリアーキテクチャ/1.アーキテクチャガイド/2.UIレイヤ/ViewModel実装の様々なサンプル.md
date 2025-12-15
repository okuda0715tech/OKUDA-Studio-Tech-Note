- [Flow を使った様々なサンプル](#flow-を使った様々なサンプル)


# ViewModel 実装の様々なサンプル

## UI 状態は 3 種類に分類する

UI 状態は、 data class で表現されますが、その元となるデータは Flow で管理します。

データの取得元ごとに Flow を複数定義して、それらを Flow.combine() 関数で一つの Flow に合成し、 UI は合成後の一つの Flow を監視するのが良いアーキテクチャです。

ただし、 Flow が増えすぎると、可読性が落ちるため、基本の Flow の定義方法として、以下の 3 種類の Flow を定義することを推奨します。

- PersistedUiState
  - Repository から取得したデータを保持する Flow
- FormUiState / EditableUiState
  - 画面からユーザーによって入力されたデータで、これから Repository 経由で永続化するデータを保持する Flow
- UiLocalState
  - 折り畳み可能な UI など、永続化する必要のないデータを保持する Flow

これら三つの UI 状態を合成した UI 状態を UI 側で監視します。

これら三つの UI 状態はさらに細かい UI 状態に分割することも可能です。


## サンプル

```kotlin
data class PersistedState(
    val dest: Dest? = null,
    val sources: List<Source> = emptyList(),
    val stats: Stats? = null
)

data class FormState(
    val name: String = "",
    val amount: String = ""
)

data class LocalState(
    val isDialogOpen: Boolean = false,
    val tempSelection: Int? = null
)

data class UiState(
    val persisted: PersistedState = PersistedState(),
    val form: FormState = FormState(),
    val local: LocalState = LocalState()
)
```

```kotlin
@HiltViewModel
class DestEditViewModel @Inject constructor(
    private val repo: DestRepository
) : ViewModel() {

    // -------------------------------
    // ① Repository の Flow を combine して 1つの persistedState にする
    // -------------------------------
    private fun buildPersistedState(destId: Int): StateFlow<PersistedState> {
        return combine(
            repo.getDest(destId),      // Flow<Dest>
            repo.getSources(destId),   // Flow<List<Source>>
            repo.getStats(destId)      // Flow<Stats>
        ) { dest, sources, stats ->
            PersistedState(
                dest = dest,
                sources = sources,
                stats = stats
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            PersistedState() // ← 初期値（空 or ローディング）
        )
    }

    // persistedState は 1 Flow に統合された
    private val persistedState = buildPersistedState(destId = 123)


    // -------------------------------
    // ② ユーザー入力（DB保存する form の状態）
    // -------------------------------
    private val _formState = MutableStateFlow(FormState())
    val formState: StateFlow<FormState> = _formState


    // -------------------------------
    // ③ UI 内限定の一時状態（dialog open など）
    // -------------------------------
    private val _localState = MutableStateFlow(LocalState())
    val localState: StateFlow<LocalState> = _localState


    // -------------------------------
    // ④ これら３つを combine して、UI が監視する 1つの UiState にする
    // -------------------------------
    val uiState: StateFlow<UiState> =
        combine(
            persistedState,
            formState,
            localState
        ) { persisted, form, local ->
            UiState(
                persisted = persisted,
                form = form,
                local = local
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            UiState() // 初期値
        )
}
```


### どうしてもたくさんの種類以上の UI 状態を合成したい場合

とは言え、どうしてもたくさんの UI 状態 ( Flow ) を ( `Flow.combine()` 関数で) 合成したい場合もあるかもしれません。 combine 関数は、その関数シグネチャでは、引数を 5 つまでしか受け取ることができないため、 6 個以上の Flow を合成することができません。

その場合は、 `Flow.combineTransform()` 関数を使うことで、無制限の Flow を合成することが可能です。

combineTransform() について、詳しくは、 [ combine 関数の引数は 5 個までだが](./4.UI%20状態生成.md/#combine-関数の引数は-5-個までだが) を参照してください。


## Repository からデータを取得する際にエラーをハンドリングする

```kotlin
data class PersistedState(
    val dest: Dest,
    val sources: List<Source>
)
```

Repository から複数の Flow を取得して、それらのうちのどれかでエラーが発生したらエラー状態を返す。

```kotlin
@HiltViewModel
class DestEditViewModel @Inject constructor(
    private val repo: DestRepository
) : ViewModel() {

    private val persistedAsync: StateFlow<Async<PersistedState>> =
        combine(
            repo.getDest(destId = 123),
            repo.getSources(destId = 123)
        ) { dest, sources ->
            PersistedState(
                dest = dest,
                sources = sources
            )
        }
            .map<PersistedState, Async<PersistedState>> { state ->
                Async.Success(state)
            }
            .catch {
                emit(Async.Error(R.string.error_load_failed))
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                Async.Loading
            )
```

```kotlin
private val _formState = MutableStateFlow(FormState())
val formState: StateFlow<FormState> = _formState

private val _localState = MutableStateFlow(LocalState())
val localState: StateFlow<LocalState> = _localState
```

```kotlin
data class UiState(
    val persisted: Async<PersistedState> = Async.Loading,
    val form: FormState = FormState(),
    val local: LocalState = LocalState()
)
```

UI が監視する最終的な UI 状態を生成

```kotlin
val uiState: StateFlow<UiState> =
    combine(
        persistedAsync,
        formState,
        localState
    ) { persisted, form, local ->
        UiState(
            persisted = persisted,
            form = form,
            local = local
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        UiState()
    )
```

UI 側

```kotlin
when (val state = uiState.persisted) {
    Async.Loading -> {
        CircularProgressIndicator()
    }

    is Async.Error -> {
        Text(stringResource(state.messageRes))
    }

    is Async.Success -> {
        val data = state.data
        DestContent(
            dest = data.dest,
            sources = data.sources
        )
    }
}
```


## キーが変わるたびに、そのキーを使って Repository から再取得

キーを Flow として定義することで、宣言的な実装になっているところがポイント。

```kotlin
private val destIdFlow = MutableStateFlow<Int?>(null)

private val persistedAsync: StateFlow<Async<PersistedState>> =
    destIdFlow
        .filterNotNull()
        .flatMapLatest { destId ->aa
            combine(
                repo.getDest(destId),
                repo.getSources(destId)
            ) { dest, sources ->
                PersistedState(dest, sources)
            }
                .map<PersistedState, Async<PersistedState>> {
                    Async.Success(it)
                }
                .onStart { emit(Async.Loading) }
                .catch { emit(Async.Error(R.string.error_load_failed)) }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            Async.Loading
        )
```


## イベントを UI 状態とは別に Coroutine で管理する

イベントを UI 状態として管理すると、イベントを消費した後に状態を元に戻す処理が必要になります。戻す処理を忘れたり、もし状態がグローバルな状態だと、同時実行された場合の予期せぬ結果につながる可能性があります。そのため、一度きりのイベントは、 UI 状態として管理するより、 Coroutine や SharedState として、一度消費されたらもう一度イベントを受信することができないような仕組みにすると、より良い実装になります。

イベントを表すクラスの定義

```kotlin
sealed class UiEvent {
    data class ShowSnackbar(val messageRes: Int) : UiEvent()
}
```

ViewModel から Channel へイベントをディスパッチ

```kotlin
@HiltViewModel
class SampleViewModel @Inject constructor(
    private val repo: SampleRepository
) : ViewModel() {

    private val _eventChannel = Channel<UiEvent>(Channel.BUFFERED)
    val eventFlow = _eventChannel.receiveAsFlow()

    fun onSaveClicked() {
        viewModelScope.launch {
            runCatching {
                repo.save()
            }.onSuccess {
                _eventChannel.send(
                    UiEvent.ShowSnackbar(R.string.save_success)
                )
            }.onFailure {
                _eventChannel.send(
                    UiEvent.ShowSnackbar(R.string.save_failed)
                )
            }
        }
    }
}
```

UI 側でイベントを監視

```kotlin
@Composable
fun SampleScreen(
    viewModel: SampleViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = stringResource(event.messageRes)
                    )
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {
        // UI 本体
    }
}
```
