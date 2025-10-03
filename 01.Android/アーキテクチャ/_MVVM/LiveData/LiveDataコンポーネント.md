<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [LiveData](#livedata)
	- [概要](#概要)
	- [メリット](#メリット)
	- [使い方](#使い方)
		- [LiveDataオブジェクトの生成](#livedataオブジェクトの生成)
		- [LiveDataオブジェクトを観察する](#livedataオブジェクトを観察する)
		- [LiveData内のデータの更新](#livedata内のデータの更新)
		- [コレクションの要素の更新時にObserverに通知する方法](#コレクションの要素の更新時にobserverに通知する方法)
	- [LiveDataとMutableLiveDataの違い](#livedataとmutablelivedataの違い)
	- [LiveDataクラスを拡張して、独自LiveDataクラスを作成する場合](#livedataクラスを拡張して独自livedataクラスを作成する場合)
	- [ある LiveData の値が変更された時に、別の LiveData の値を変更する](#ある-livedata-の値が変更された時に別の-livedata-の値を変更する)
	- [LiveDataの実型がListなどの入れ物の場合、入れ物内部の一部の要素の参照だけが変わった場合にObserverへ通知する方法](#livedataの実型がlistなどの入れ物の場合入れ物内部の一部の要素の参照だけが変わった場合にobserverへ通知する方法)
	- [複数のホストから一つのLiveDataにアクセスするには](#複数のホストから一つのlivedataにアクセスするには)
	- [Room と併用することで DB の値の変更も観察できる](#room-と併用することで-db-の値の変更も観察できる)
		- [Select文の返値を LiveData 型にすることで DB の更新時にデータを再取得する](#select文の返値を-livedata-型にすることで-db-の更新時にデータを再取得する)
		- [場合によっては、 Transformations の switchMap() を使用する](#場合によっては-transformations-の-switchmap-を使用する)
<!-- TOC END -->


# LiveData

## 概要

LiveDataは、データモデルからホストへの通知を自動化するコンポーネントである。

Activityなどの観察クラスがLiveDataにラップされたデータモデルクラスを観察する。
LiveDataは、データモデルに変更があった場合、観察側クラスがアクティブならば、観察側クラスに通知する。アクティブでなければ通知しない。


## メリット

- データモデルからViewへの通知をライブラリがやってくれるため、開発者がコードを書く必要がない。
	- また、ViewModelに変更があった場合に、Viewへの通知を行うコードを記述せずともライブラリが通知してくれるため、ViewModelからViewへの依存関係を作らなくて済む。
- データ取得中に画面回転等の構成変更が発生しても、再生成したホストに通知してくれる。
	- 画面回転などの構成変更が発生した時にも、受け取れなかったデータがあったとしても最新のデータを受け取ってくれるっぽい。
- データモデル観察の開始、終了の自動化
	- LiveDataは、ホストするActivityなどのライフサイクルを把握(観察)しているため、LiveData自身が(View -> データモデルの)観察の開始や終了を行ってくれる。そのため、ホストするActivityなどの観察側で、観察の開始や終了のコードを書く必要はない。
- 通知先のホストクラス(ActivityやFragment)が非アクティブな時にデータモデルに変更が発生しても通知を保留してくれる。
	- LiveDataでラップしたデータモデルは、LiveDataをホストするActivityやFragmentのライフサイクルに同期したライフサイクルを持つことができる。
	- そのため、ActivityやFragmentがアクティブな時のみUIを更新する通知を行うことができる。そのため、それに起因するクラッシュが回避できる。
- データモデルの破棄の自動化
	- ホストしているActivityが`Destroy`した時に、LiveDataでラップしているデータモデルも破棄する機能を備えている。
- データ取得中にホストが非アクティブになっても、再度アクティブになった時に最新のデータを取得し、ホストに通知してくれる？？？
	- Activityなどがバックグラウドからフォアグラウンドに変更になった場合、フォアグラウンドになったら、すぐに最新のデータを取得する。（要するにバックグラウンドにいた時に受け取れなかった変更の通知をフォアグラウンドになった直後に受け取れるということである。バックグラウンドにいる時に変更がなければ通知は送られない。ただし、初回は値なしから値ありになるため、変更ありと見なされ送られる。）
- 複数の観察クラスへの通知
	- 観察側クラスが複数登録されている場合は、観察側クラスがアクティブな状態ならば、それら全てのクラスに通知が行われます。
- データモデルの生成クラスとデータモデルの利用クラスを完全に分離することができる。
	- 上記の通りなので詳細は省略する。
-  Activity,Fragment,Serviceなどのライフサイクル
	- Activity,Fragment,Serviceなどのライフサイクル管理をライブラリが行ってくれるため、開発者がホストクラスへの依存を作る必要がなく、ホストの破棄失敗によるメモリリークが防げる。
	- LiveDataは、観察する側(Activityなど)の`onDestroy`などが呼ばれると、観察される側(LiveDataでラップしたデータモデル)から、観察する側を切り離します。
- その他
	- LiveDataが存在していなかった頃は、画面を持たないFragmentをLiveDataの代わりに使用していた。


## 使い方

1. ViewModel内でModelをラップしたLiveDataフィールドを定義する。
2. ホストActivityやFragmentに`onChanged`コールバックメソッドを実装した`Observer`オブジェクトを生成します。これは、LiveDataに変更が発生した場合に、変更を通知するコールバックです。
3. `observe`メソッドを使用して、観察する側と観察される側のオブジェクトを接続します。

`observe`メソッドの使い方

`{観察されるオブジェクトをLiveDataでラップしたオブジェクト}.observe({観察するオブジェクト},{onChangedコールバックを実装したObserverオブジェクト})`

補足

`observeForever(Observer)`メソッドを使用すると、観察側クラスがライフサイクルを持ってないクラスでも観察側クラスとして登録することができます。
この場合は、観察側は常にアクティブであると見なされ、値に変更が生じた場合には、常に観察側に通知されます。
観察を解除したい場合は、`removeObserver(Observer)`メソッドを呼び出します。


### LiveDataオブジェクトの生成

**SampleViewModel.java**

```Java
public class NameViewModel extends ViewModel {

    // ViewModelではなくAndroidViewModelを継承する場合は、コンストラクタを定義するが、コンストラクタはpublicでなければいけない。
    // publicでないと、ViewModelProviderでインスタンスを生成することができない。

    // String型のデータを格納するLiveDataフィールド
    // フィールドは一度代入したら後から再代入すると、データ更新時に更新通知をしてくれないことがあるので、再代入はしないこと。
    // ただし、finalで定義するなら、定義時にインスタンスを生成し、代入しておく必要がある。
    private final MutableLiveData<String> currentName;

    public MutableLiveData<String> getCurrentName() {
        if (currentName == null) {
            currentName = new MutableLiveData<String>();
        }
        return currentName;
    }

    // Rest of the ViewModel...
}
```

1. ViewModelクラス内にLiveDataオブジェクトを定義する。
2. 保持したいデータをLiveDataでラップする。
3. getterで取得できるようにgetterを定義する。


### LiveDataオブジェクトを観察する

ほとんどの場合、ActivityやFragmentの`onCreate`メソッド内で観察を開始するのが良いです。それ以降だと、重複して観察を開始してしまう恐れがあります。

**SampleActivity.java**

```Java
public class NameActivity extends AppCompatActivity {

    private NameViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Activityをセットアップするその他のコード

        // ViewModelを取得する。
        // 非推奨
        NameViewModel model = ViewModelProviders.of(this).get(NameViewModel.class);
        // 推奨
        NameViewModel model = new ViewModelProvider(
                getViewModelStore(), getDefaultViewModelProviderFactory()).get(NameViewModel.class);

        // UIを更新するオブザーバーを生成します。
        final Observer<String> nameObserver = new Observer<String>() {
            // 「ViewModelのsetValueメソッド」が呼ばれた場合に呼ばれるコールバックメソッドです。基本的には「observeメソッド」で観察を開始しただけでは呼ばれません。
            // observeメソッドで観察を開始した際に、既にLiveDataフィールドに値がセットされていれば、即座にonChangedメソッドが呼び出されます。
            // LiveDataフィールドの実型がListなどの入れ物の場合、既にセットされている入れ物が空であってもonChangedメソッドは呼ばれます。
            @Override
            public void onChanged(@Nullable final String newName) {
                // UIの更新などを行います。
                nameTextView.setText(newName);
            }
        };

        // ViewModel,LiveData,Activity,Activityのコールバックを紐付けて、観察を開始します。
        model.getCurrentName().observe(this, nameObserver);
    }
}
```

`ViewModelProviders`や`getDefaultViewModelProviderFactory`を使用するためには、`build.gradle`に以下の記載が必要です。

**build.gradle**

```
implementation 'androidx.lifecycle:lifecycle-extensions:2.1.0'
```


### LiveData内のデータの更新

LiveDataの中身のデータを更新するには、`setValue`メソッド、もしくは、`postValue`メソッドを使用します。

**SampleActivity.java**

```java
button.setOnClickListener(new OnClickListener() {
    @Override
    public void onClick(View v) {
        String anotherName = "John Doe";
        // メインスレッドから実行する場合
        model.getCurrentName().setValue(anotherName);
        // ワーカースレッドから実行する場合
        model.getCurrentName().postValue(anotherName);
    }
});
```

`setValue`や`postValue`メソッドで、LiveDataの値が更新されれば、`Observer`クラスの`onChanged`メソッドが呼ばれるので、そこでViewを更新する処理を行ましょう。


### コレクションの要素の更新時にObserverに通知する方法

コレクションの要素が更新されただけでは、LiveDataはObserverへの通知を行いません。

通知を行うのは、`setValue`や`postValue`を実行した時のみです。

そのため、リスト更新時には、以下のようにしてObserverへ通知するのが一般的です。

```java
livedata.setValue(livedata.getValue());
```


## LiveDataとMutableLiveDataの違い

MutableLiveDataはLiveDataを継承しているクラスである。MutableLiveDataとLiveDataの違いは、  
MutableLiveDataは`setValue`メソッドと`postValue`メソッドを外部に公開している点で異なる。  
それ以外の違いはない。

そのため、自分で`setValue`や`postValue`メソッドを呼び出す時はもちろん、  
双方向データバインディングでLiveDataの値を更新する場合もMutableLiveDataを使用する必要がある。


## LiveDataクラスを拡張して、独自LiveDataクラスを作成する場合

**MyLiveData.java**

```java
public class StockLiveData extends LiveData<BigDecimal> {
    private StockManager stockManager;

    private SimplePriceListener listener = new SimplePriceListener() {
        @Override
        public void onPriceChanged(BigDecimal price) {
            setValue(price);
        }
    };

    public StockLiveData(String symbol) {
        stockManager = new StockManager(symbol);
    }

    @Override
    protected void onActive() {
        stockManager.requestPriceUpdates(listener);
    }

    @Override
    protected void onInactive() {
        stockManager.removeUpdates(listener);
    }
}
```

`onActive`メソッドは、LiveDataにアクティブなオブザーバーが一つもいない状態から一ついる状態になった時に呼ばれる。
`onInactive`メソッドは、全てのObserverがアクティブではなくなった場合に呼ばれる。
`onInactive`メソッド内で、`removeUpdates`メソッドの引数にlistenerを渡しているが、`removeUpdates`メソッド内では、特にlistenerは参照せず、`StockManager`内の`listener`に`null`をセットするだけで良い。


**HostFragment.java**

```java
public class MyFragment extends Fragment {
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LiveData<BigDecimal> myPriceListener = ...;
        myPriceListener.observe(this, price -> {
            // Update the UI.
        });
    }
}
```

LiveDataを継承したカスタムLiveDataクラスを作成した場合も、観察登録の仕方は同じである。
ただし、ViewModel内にLiveDataを定義せず、ActivityやFragment内にLiveData変数を定義することになると思われる。


## ある LiveData の値が変更された時に、別の LiveData の値を変更する

`MediatorLiveData` もしくは、 `Transformations` クラスの `map()` メソッド or `switchMap()` メソッドを使用します。

ある `LiveData` の値をそのまま別の `LiveData` にセットする場合は、 `MediatorLiveData` を使用します。  
ある `LiveData` の値を受け取って、何らかの処理を行い、その結果を `LiveData` にセットする場合は、  
`Transformations` クラスを使用します。

それぞれの使用方法は、別紙を参照してください。


## LiveDataの実型がListなどの入れ物の場合、入れ物内部の一部の要素の参照だけが変わった場合にObserverへ通知する方法

```java
MutableLiveData<List<String>> stringLiveData;

public void addItem(String stringData){
	stringLiveData.getValue().add(stringData);
	// 少し奇妙な実装に見えるが、setValueやpostValueを呼ばないとObserverへ通知されないため、
	// 以下のようにgetしてからsetする方法がとられる。
	stringLiveData.setValue(stringLiveData.getValue());
}
```


## 複数のホストから一つのLiveDataにアクセスするには

`MediatorLiveData`を使用すると複数のホストから一つのLiveDataにアクセスすることができます。


## Room と併用することで DB の値の変更も観察できる

### Select文の返値を LiveData 型にすることで DB の更新時にデータを再取得する

`Room` の `Dao` クラスで、Selectメソッドの返値の型に `LiveData` 型を指定することによって、  
テーブルの更新を監視し、テーブルに更新が発生した場合に自動的に Select メソッドを実行してくれる。  
Select 文の返値( `LiveData` 型)を `observe()` メソッド等で監視することにより、  
テーブルの更新をトリガーとした処理を実装することができる。

### 場合によっては、 Transformations の switchMap() を使用する

例えば、リアルタイム検索機能のような、検索キーワードを一文字変更するたびに、  
即座に DB のデータを再取得する機能を実装するとする。  

この場合、検索キーワードの値の変更を監視し、変更が発生した場合に、 DB から Select 文で  
データを再取得する Function を実行する。  
その Function では、 `Dao` の Select 文を実行し、その結果は LiveData 型で受け取る。

実際のコードは、上記の **switchMap() のサンプル** を参照。
