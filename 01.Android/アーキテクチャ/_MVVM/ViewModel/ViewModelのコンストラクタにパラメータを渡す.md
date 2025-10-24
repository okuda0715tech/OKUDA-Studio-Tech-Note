<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [ViewModelのコンストラクタにパラメータを渡す](#viewmodelのコンストラクタにハラメータを渡す)
	- [コード](#コード)
<!-- TOC END -->


# ViewModelのコンストラクタにパラメータを渡す

## コード

**MyViewModelFactory.java**

```java
public class MemoListVMFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private String mParam;


    public MemoListVMFactory(Application application, String param) {
        mApplication = application;
        mParam = param;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new MemoListViewModel(mApplication, mParam);
    }
}
```


**MainActivity.java**

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    binding.setLifecycleOwner(this);
    memoListViewModel = new ViewModelProvider(
        getViewModelStore(),
        new MemoListVMFactory(
                getApplication(),
                getCurrentDir())) // ここがパラメータ
        .get(MemoListViewModel.class);
    binding.setListViewModel(memoListViewModel);
    setUpRecyclerView(binding);
    setContentView(binding.getRoot());
}
```
