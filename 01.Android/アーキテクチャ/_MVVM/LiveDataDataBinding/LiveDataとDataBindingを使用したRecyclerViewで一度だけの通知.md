<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [LiveDataとDataBindingを使用したRecyclerViewで一度だけの通知](#livedataとdatabindingを使用したrecyclerviewて一度たけの通知)
	- [コード](#コード)

<!-- /TOC -->


# LiveDataとDataBindingを使用したRecyclerViewで一度だけの通知

## コード

**activity_main.xml**

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout>

    <data>

    </data>

    <androidx.appcompat.widget.LinearLayoutCompat xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:tools="http://schemas.android.com/tools"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical"
        tools:context=".MainActivity">

        <androidx.recyclerview.widget.RecyclerView
            android:id="@+id/recycler"
            android:layout_width="match_parent"
            android:layout_height="match_parent" />

    </androidx.appcompat.widget.LinearLayoutCompat>
</layout>
```


**row.xml**

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout>
    <data>

        <variable
            name="rowData"
            type="com.kurodai0715.myeventoncerecycler.RowData" />
    </data>

    <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:tools="http://schemas.android.com/tools"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal">

        <ImageView
            android:layout_width="60dp"
            android:layout_height="60dp"
            android:scaleType="fitCenter"
            android:src="@mipmap/ic_launcher" />

        <LinearLayout
            android:layout_width="0dp"
            android:layout_height="match_parent"
            android:layout_weight="1"
            android:orientation="vertical">

            <TextView
                android:id="@+id/title"
                android:layout_width="match_parent"
                android:layout_height="0dp"
                android:layout_weight="1"
                android:text="@{rowData.title}"
                tools:text="title" />

            <TextView
                android:id="@+id/detail"
                android:layout_width="match_parent"
                android:layout_height="0dp"
                android:layout_weight="1"
                android:text="@{rowData.detail}"
                tools:text="detail" />

        </LinearLayout>

    </LinearLayout>
</layout>
```


**MainActivity.java**

```Java
public class MainActivity extends AppCompatActivity {

    ListViewModel listViewModel;
    DetailViewModel detailViewModel;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        binding.setLifecycleOwner(this);
        setContentView(binding.getRoot());

        recyclerView = binding.recycler;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        // コメント1（コメント1とコメント2を復活させると個別アイテムタップ時のLiveData伝搬が可能になる。）
//        detailViewModel = new ViewModelProvider(getViewModelStore(), getDefaultViewModelProviderFactory())
//                .get(DetailViewModel.class);
//        detailViewModel.getEmptyDetailLiveData().observe(MainActivity.this, new EventObserver<>(
//                new EventObserver.OnEventChanged<DetailData>() {
//                    @Override
//                    public void onUnhandledContent(DetailData data) {
//                        Snackbar.make(getRootView(), data.getAddress(), Snackbar.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onChangedContent(DetailData data) {
//                    }
//                }));
        listViewModel = new ViewModelProvider(getViewModelStore(), getDefaultViewModelProviderFactory())
                .get(ListViewModel.class);
        listViewModel.getListLiveData().observe(this, new EventObserver<>(
                new EventObserver.OnEventChanged<RowDataList>() {
                    @Override
                    public void onUnhandledContent(RowDataList items) {
                    }

                    @Override
                    public void onChangedContent(RowDataList items) {
                        MyAdapter adapter = new MyAdapter(items) {
                            @Override
                            public void onRowDataClicked(@NonNull RowData rowData) {
                                // コメント2
//                                detailViewModel.getDetailData();

                                Snackbar.make(getRootView(), rowData.getTitle(), Snackbar.LENGTH_SHORT).show();

                            }
                        };
                        recyclerView.setAdapter(adapter);
                    }
                }));

    }

    private View getRootView() {
        return findViewById(android.R.id.content);
    }
}
```


**MyAdapter.java**

```Java
public abstract class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<RowData> mRowDataList;

    public abstract void onRowDataClicked(RowData rowData);

    MyAdapter(List<RowData> rowDataList) {
        mRowDataList = rowDataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RowBinding binding = DataBindingUtil.inflate(inflater, R.layout.row, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int position) {
        myViewHolder.binding.setRowData(mRowDataList.get(position));
        myViewHolder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRowDataClicked(mRowDataList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRowDataList.size();
    }

    /**
     * ViewHolder
     */
    static class MyViewHolder extends RecyclerView.ViewHolder {

        private final RowBinding binding;

        MyViewHolder(final RowBinding itemBinding) {
            super(itemBinding.getRoot());
            binding = itemBinding;
        }
    }
}
```


**RowData.java**

```Java
public class RowData {
    private String mTitle;
    private String mDetail;

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setDetail(String mDetail) {
        this.mDetail = mDetail;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDetail() {
        return mDetail;
    }
}
```


**RowDataList.java**

```Java
public class RowDataList extends ArrayList<RowData> {
  // 空っぽのクラス
}
```


**Event.java**

```Java
public class Event<T> {
    private boolean hasBeenHandled = false;
    private T content;

    public Event(T content) {
        this.content = content;
    }

    /**
     * まだコンテンツの変更を(Activityに)一度も通知していない場合はコンテンツを返す。
     */
    public T getContentIfNotHandled() {
        if (hasBeenHandled) {
            return null;
        } else {
            hasBeenHandled = true;
            return content;
        }
    }

    /**
     * 既にコンテンツの変更を一度(Activityに)通知済みでもコンテンツを返す。
     */
    public T getContent() {
        return content;
    }

    public boolean isHandled() {
        return hasBeenHandled;
    }

    public boolean isNotHandled() {
        return !hasBeenHandled;
    }

}
```


**EventObserver.java**

```Java
public class EventObserver<T1> implements Observer<Event<T1>> {
    private OnEventChanged<T1> onEventChanged;

    public EventObserver(OnEventChanged<T1> onEventChanged) {
        this.onEventChanged = onEventChanged;
    }

    @Override
    public void onChanged(@Nullable Event<T1> tEvent) {
        if (tEvent != null && onEventChanged != null){
            if(tEvent.isNotHandled()){
                onEventChanged.onUnhandledContent(tEvent.getContentIfNotHandled());
            }
            onEventChanged.onChangedContent(tEvent.getContent());
        }
    }

    interface OnEventChanged<T2> {
        // データが未処理の場合のみ呼び出されるメソッド
        void onUnhandledContent(T2 data);

        // データ変更時に常に呼び出されるメソッド
        void onChangedContent(T2 data);
    }
}
```


**ListViewModel.java**

```Java
public class ListViewModel extends AndroidViewModel implements GetListDataTask.Listener {

    private MutableLiveData<Event<RowDataList>> listLiveData;

    public ListViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Event<RowDataList>> getListLiveData() {
        if (listLiveData == null) {
            listLiveData = new MutableLiveData<>();
            getListData();
        }
        return listLiveData;
    }

    private void getListData() {
        GetListDataTask task = new GetListDataTask();
        task.setLister(new WeakReference<GetListDataTask.Listener>(this));
        task.execute();
    }

    @Override
    public void onFinish(RowDataList list) {
        listLiveData.setValue(new Event<>(list));
    }
}
```


**GetListDataTask.java**

```Java
public class GetListDataTask extends AsyncTask<Object, Object, RowDataList> {

    private WeakReference<Listener> mLister;

    @Override
    protected RowDataList doInBackground(Object... objects) {
        RowDataList list = new RowDataList();
        for (int i = 1; i <= 10 ;i++){
            RowData rowData = new RowData();
            rowData.setTitle("title " + i);
            rowData.setDetail("detail " + i);
            list.add(rowData);
        }

        try {
            Thread.sleep(300);
        }catch (InterruptedException e){

        }

        return list;
    }

    @Override
    protected void onPostExecute(RowDataList list) {
        super.onPostExecute(list);
        mLister.get().onFinish(list);
    }

    interface Listener {
        void onFinish(RowDataList list);
    }

    void setLister(WeakReference<Listener> lister) {
        mLister = lister;
    }
}
```


**DetailViewModel.java**

```Java
public class DetailViewModel extends AndroidViewModel implements GetDetailDataTask.Listener {

    private MutableLiveData<Event<DetailData>> detailLiveData;

    public DetailViewModel(@NonNull Application application) {
        super(application);
    }

    MutableLiveData<Event<DetailData>> getDetailLiveData(){
        if(detailLiveData == null){
            detailLiveData = new MutableLiveData<>();
            getDetailData();
        }
        return detailLiveData;
    }

    public MutableLiveData<Event<DetailData>> getEmptyDetailLiveData(){
        if(detailLiveData == null){
            detailLiveData = new MutableLiveData<>();
        }
        return detailLiveData;
    }

    public void getDetailData(){
        GetDetailDataTask task = new GetDetailDataTask();
        task.setListener(new WeakReference<GetDetailDataTask.Listener>(this));
        task.execute();
    }

    @Override
    public void onFinish(DetailData data) {
        detailLiveData.setValue(new Event<>(data));
    }
}
```


**GetDetailDataTask.java**

```Java
public class GetDetailDataTask extends AsyncTask<Object, Object, DetailData> {

    private WeakReference<Listener> mListener;

    @Override
    protected DetailData doInBackground(Object... objects) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {

        }
        return new DetailData("横浜市");
    }

    @Override
    protected void onPostExecute(DetailData detailData) {
        super.onPostExecute(detailData);
        mListener.get().onFinish(detailData);
    }

    interface Listener {
        void onFinish(DetailData data);
    }

    void setListener(WeakReference<Listener> listener) {
        mListener = listener;
    }
}
```


**DetailData.java**

```Java
public class DetailData {

    private String mAddress;

    public DetailData(String address) {
        this.mAddress = address;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        this.mAddress = address;
    }
}
```


