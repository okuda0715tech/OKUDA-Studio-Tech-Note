<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [SavedStateHandler](#savedstatehandler)
  - [概説](#概説)
  - [準備](#準備)
  - [コード](#コード)
    - [ViewModel側](#viewmodel側)
    - [Activity側](#activity側)
    - [ViewModelではなく、AndroidViewModelを継承しているカスタムViewModelの場合](#viewmodelではなくandroidviewmodelを継承しているカスタムviewmodelの場合)
<!-- TOC END -->


# SavedStateHandler

## 概説

`SavedStateHandler` は、メモリ不足の状況になったとき、OSによる強制プロセス終了直前に、  
データを永続化する仕組みである。 `Activity` の `savedInstanceState` と同じ役割を担う。

`savedInstanceState` と異なる点は、 `ViewModel` , `LiveData` との組み合わせで使用することが  
想定されている点である。これらを組み合わせて  使用することにより、構成変更時には `ViewModel` が、  
強制プロセス終了時には `SavedStateHandler` が必要なデータを保持する実装が簡単にできる。


## 準備

`build.gradle` へ以下を追加

```java
implementation 'androidx.lifecycle:lifecycle-viewmodel-savedstate:2.2.0'
// ViewModel生成時の getDefaultViewModelProviderFactory() 呼び出しのために必要
implementation 'androidx.preference:preference:1.1.1'
```


## コード

### ViewModel側

```java
public class MainViewModel extends ViewModel {

    SavedStateHandle state;

    MutableLiveData<String> userNameLiveData;
    int userAge;
    User user;

    // ポイント：コンストラクタの引数で SavedStateHandle を渡す。
    public MainViewModel(SavedStateHandle state) {
        // ポイント
        // getLiveData(キー) で MutableLiveData を取得してメンバ変数に代入する。
        // 強制プロセス終了時に保存したいデータが MutableLiveData の場合は、ViewModel 内に必要な
        // 実装は、この一行だけで良い。
        // こうするだけで、プロセス再生成時には自動的にメンバ変数の MutableLiveData に
        // データが復元される。それによって、オブザーバーの onChanged() コールバックも呼ばれる。
        // また、 MutableLiveData の中にセットした実データは、自動的に SavedStateHandle に保存される。
        userNameLiveData = state.getLiveData("USER_NAME");

        // ポイント
        // SavedStateHandle に保存したいデータの型が MutableLiveData ではない場合は、
        // メンバ変数として、 SavedStateHandle を保持する必要がある。（書き込み時に使用するため。）
        this.state = state;

        // ポイント
        // 強制プロセス終了後にプロセスが再生成されるときには ViewModel のコンストラクタが呼ばれる。
        // ViewModel の初回生成時のコンストラクタ処理では、 get() は null を返す。
        // 二回目以降の生成時に値がセットされていれば、その値が取得できる。
        // 保存したいデータの型が MutableLiveData ではない場合に、 get() メソッドを使用する。
        Integer userAge = state.get("USER_AGE");

        // ポイント
        // 独自クラスの保存・取得も可能。ただし、 Serializable などを実装している必要がある。
        User user = state.get("USER");
    }

    // 普通のセッター
    public void setUserName(String userName) {
        // ポイント
        // SavedStateHandle.getLiveData() メソッドで取得した MutableLiveData にデータをセットすると、
        // SavedStateHandle 内にデータが書き込まれるため、プロセスが破棄されても値を保持している。
        userNameLiveData.setValue(userName);
    }

    // ポイント
    // 保存したいデータ型が MutableLiveData ではない場合は、 set() メソッドで、
    // SavedStateHandle への明示的な書き込みが必要である。
    public void persistUserAge() {
        state.set("USER_AGE", userAge);
    }

    // ポイント
    // 保存したいデータの型が MutableLiveData ではない場合、
    // メンバ変数へのデータセットと、 SavedStateHandle へのデータセットは別ものなので、
    // それぞれへセットする必要がある。
    public void setUserAge(int age) {
        userAge = age;
    }

    public void persistUser() {
        state.set("USER", user);
    }
}
```


### Activity側

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // 普通に ViewModel を取得する
    mainViewModel = new ViewModelProvider(
            getViewModelStore(), getDefaultViewModelProviderFactory()
    ).get(MainViewModel.class);

    // ポイント
    // Activity側では、普通に LiveData を観察するだけで、プロセス再生成後に onChanged
    // コールバックで SavedStateHandle へ保存したデータを受け取ることができる。
    mainViewModel.userNameLiveData.observe(this, new Observer<String>() {
        @Override
        public void onChanged(String s) {
            userName.setText(s);
        }
    });

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {

        // ポイント
        // MutableLiveData 以外のデータを保存するため、 SavedStateHandle.set() メソッドを
        // 含むメソッドを呼んでいる。
        // 保存処理は、 super.onSaveInstanceState(outState); より前に実行しなければ、
        // 何らかの理由で保存できません。
        // onSaveInstanceState() メソッドが呼ばれたタイミングで保存処理をする必要はなく、
        // ボタンタップ時などに保存しても良い。
        mainViewModel.persistUserAge();
        mainViewModel.persistUser();

        super.onSaveInstanceState(outState);
    }
}
```


### ViewModelではなく、AndroidViewModelを継承しているカスタムViewModelの場合

```java
public class MainAndroidViewModel extends AndroidViewModel {

    // 基本は同じだが、コンストラクタの引数に Application が加わるため、
    // SavedStateHandle は第二引数に指定することになる。
    // 第一引数に指定した場合、クラッシュする。
    public MainAndroidViewModel(@NonNull Application application, SavedStateHandle state) {
        super(application);

        // do something.
    }
}
```
