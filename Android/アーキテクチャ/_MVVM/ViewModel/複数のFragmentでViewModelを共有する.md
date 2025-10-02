<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [複数のFragmentでViewModelを共有する](#複数のfragmentてviewmodelを共有する)
	- [概要](#概要)
	- [コード（ポイントのみ）](#コードポイントのみ)
	- [コード（全体）](#コード全体)

<!-- /TOC -->


# 複数のFragmentでViewModelを共有する

## 概要

複数のFragmentが同一のActivityを親に持っている場合、ViewModelを共有することができます。

ViewModelを共有するので、当然内部のLiveDataなどのデータも共有されます。

ポイントは、 `ViewModelProvider` のインスタンスを生成する場合に、パラメータに親アクティビティのインスタンスを渡すことです。  
これにより、アクティビティの保有する `ViewModelStore` を複数のフラグメントが共有します。


## コード（ポイントのみ）

**SharedViewModel.java**

```java
public class SharedViewModel extends ViewModel {
    private final MutableLiveData<Item> selected = new MutableLiveData<Item>();

    public void select(Item item) {
        selected.setValue(item);
    }

    public LiveData<Item> getSelected() {
        return selected;
    }
}
```


**MasterFragment.java**

```java
public class MasterFragment extends Fragment {
    private SharedViewModel viewModel;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 第二引数 getDefaultViewModelProviderFactory() を省略したコンストラクタも使用可能です。
        viewModel = new ViewModelProvider( 
                requireActivity(), getDefaultViewModelProviderFactory()).get(SharedViewModel.class); 
        itemSelector.setOnClickListener(item -> {
            model.select(item);
        });
    }
}
```


**DetailFragment.java**

```java
public class DetailFragment extends Fragment {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 第二引数 getDefaultViewModelProviderFactory() を省略したコンストラクタも使用可能です。
        viewModel = new ViewModelProvider( 
                requireActivity(), getDefaultViewModelProviderFactory()).get(SharedViewModel.class); 
        model.getSelected().observe(this, { item ->
           // Update the UI.
        });
    }
}
```


## コード（全体）

以下のコードは、次のようなアプリです。

- Activityは二つのFragmentで共通
- FirstFragmentでは、乱数を生成し、画面に表示する。
- 乱数はViewModelに格納する。
- FirstFragmentのボタンタップでSecondFragmentに入れ替わる。
- SecondFragmentのボタンタップで乱数を再生成し、ViewModelのデータを更新する。
- 更新後の値をSecondFragmenの画面に表示する。
- 端末のバックキータップでFirstFragmentに戻る。
- 戻ったときに画面に表示されている値がSecondFragmentと共通のViewModelなので同じ値になっている。


**MainActivity.java**

```java
public class MainActivity extends AppCompatActivity implements FirstFragment.Listener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.activity_container);
        FirstFragment firstFragment = null;
        if (fragment instanceof FirstFragment) {
            firstFragment = (FirstFragment) fragment;
        }

        if (firstFragment == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.activity_container, FirstFragment.getInstance(), "FIRST_FRAGMENT");
            transaction.commit();
        }
    }

    @Override
    public void onAction() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.activity_container, SecondFragment.getInstance(), "SECOND_FRAGMENT");
        transaction.addToBackStack("REMOVE_FIRST_ADD_SECOND");
        transaction.commit();
    }
}
```


**activity_main.xml**

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout>

    <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:tools="http://schemas.android.com/tools"
        android:id="@+id/activity_container"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical"
        tools:context=".MainActivity">

    </LinearLayout>

</layout>
```


**FirstFragment.java**

```java
public class FirstFragment extends Fragment {

    private SharedViewModel viewModel;
    private Listener mListener;

    static FirstFragment getInstance() {
        return new FirstFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof Listener) {
            mListener = (Listener) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // requireActivityはgetActivityにnullチェックを付けたもの
        viewModel = new ViewModelProvider(
                requireActivity(), getDefaultViewModelProviderFactory()).get(SharedViewModel.class);
        viewModel.getRandomNumLiveData();
        viewModel.getRandomNumLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                // do something.
            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FirstFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.first_fragment, container, false);
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);
        binding.setListener(this);

        return binding.getRoot();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void onClickButton(View view) {
        mListener.onAction();
    }

    interface Listener {
        void onAction();
    }
}
```


**first_fragment.xml**

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout>
    <data>
        <variable
            name="viewModel"
            type="com.example.mysharedviewmodel.SharedViewModel" />

        <variable
            name="listener"
            type="com.example.mysharedviewmodel.FirstFragment" />
    </data>

    <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical"
        android:id="@+id/first_fragment_container">

        <TextView
            android:layout_width="match_parent"
            android:textSize="50sp"
            android:layout_height="wrap_content"
            android:text="First Fragment"/>

        <TextView
            android:layout_width="match_parent"
            android:textSize="50sp"
            android:layout_height="wrap_content"
            android:background="@color/colorLightGreen"
            android:text="@{viewModel.randomNumLiveData}"/>

        <Button
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:textSize="20sp"
            android:text="Go to the Next Fragment"
            android:onClick="@{listener::onClickButton}"/>

    </LinearLayout>

</layout>
```


**SecondFragment.java**

```java
public class SecondFragment extends Fragment {

    private SharedViewModel viewModel;

    static SecondFragment getInstance() {
        return new SecondFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // requireActivityはgetActivityにnullチェックを付けたもの
        viewModel = new ViewModelProvider(
                requireActivity(), getDefaultViewModelProviderFactory()).get(SharedViewModel.class);
        viewModel.getRandomNumLiveData();
        viewModel.getRandomNumLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                // do something.
                int a = 1;
            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        SecondFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.second_fragment, container, false);
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);
        binding.setListener(this);

        return binding.getRoot();
    }

    public void onClickButton(View view){
        viewModel.updateRandomNumLiveData();
    }
}
```


**second_fragment.xml**

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout>
    <data>
        <variable
            name="viewModel"
            type="com.example.mysharedviewmodel.SharedViewModel" />

        <variable
            name="listener"
            type="com.example.mysharedviewmodel.SecondFragment" />
    </data>

    <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical">

        <TextView
            android:layout_width="match_parent"
            android:textSize="50sp"
            android:layout_height="wrap_content"
            android:text="Second Fragment"/>

        <TextView
            android:layout_width="match_parent"
            android:textSize="50sp"
            android:layout_height="wrap_content"
            android:background="@color/colorYellow"
            android:text="@{viewModel.randomNumLiveData}"/>

        <Button
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:textSize="20sp"
            android:text="Update Value"
            android:onClick="@{listener::onClickButton}"/>

    </LinearLayout>

</layout>
```


**SharedViewModel.java**

```java
public class SharedViewModel extends ViewModel implements GetDataTask.Listener{

    private MutableLiveData<String> randomNumLiveData;

    public MutableLiveData<String> getRandomNumLiveData() {
        if (randomNumLiveData == null) {
            randomNumLiveData = new MutableLiveData<>();
            loadData();
        }
        return randomNumLiveData;
    }

    void updateRandomNumLiveData(){
        loadData();
    }

    private void loadData() {
        GetDataTask task = new GetDataTask();
        task.setListener(new WeakReference<GetDataTask.Listener>(this));
        task.execute();
    }

    @Override
    public void onFinish(String num) {
        randomNumLiveData.setValue(num);
    }
}
```


**GetDataTask.java**

```java
public class GetDataTask extends AsyncTask<Object, Object, String> {

    private WeakReference<Listener> mListener;

    @Override
    protected String doInBackground(Object[] objects) {
        Random random = new Random();
        Integer randomNum = random.nextInt(100);
        return String.valueOf(randomNum);
    }

    @Override
    protected void onPostExecute(String num) {
        super.onPostExecute(num);
        mListener.get().onFinish(num);
    }

    interface Listener {
        void onFinish(String num);
    }

    void setListener(WeakReference<Listener> listener) {
        mListener = listener;
    }
}
```
