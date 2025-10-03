<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [LiveDataとDataBindingを使用したRecyclerView](#livedataとdatabindingを使用したrecyclerview)
	- [コード](#コード)

<!-- /TOC -->


# LiveDataとDataBindingを使用したRecyclerView

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
            type="com.kurodai0715.myrecycler.RowData" />
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

    MyViewModel viewModel;
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

        viewModel = new ViewModelProvider(getViewModelStore(), getDefaultViewModelProviderFactory())
                .get(MyViewModel.class);
        viewModel.getItemsLiveData().observe(this, new Observer<ArrayList<RowData>>() {
            @Override
            public void onChanged(ArrayList<RowData> items) {
                MyAdapter adapter = new MyAdapter(items) {
                    @Override
                    public void onRowDataClicked(@NonNull RowData rowData) {
                        Toast.makeText(getApplicationContext(), rowData.getTitle(), Toast.LENGTH_SHORT).show();
                    }
                };
                recyclerView.setAdapter(adapter);
            }
        });
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


**MyViewModel.java**

```Java
public class MyViewModel extends AndroidViewModel implements MyTask.Listener {

    private MutableLiveData<ArrayList<RowData>> itemsLiveData;

    public MyViewModel(@NonNull Application application) {
        super(application);
    }

    MutableLiveData<ArrayList<RowData>> getItemsLiveData() {
        if (itemsLiveData == null) {
            itemsLiveData = new MutableLiveData<>();
            loadData();
        }
        return itemsLiveData;
    }

    private void loadData() {
        MyTask task = new MyTask();
        task.setLister(new WeakReference<MyTask.Listener>(this));
        task.execute();
    }

    @Override
    public void onFinish(ArrayList<RowData> list) {
        itemsLiveData.setValue(list);
    }
}
```


**MyTask.java**

```Java
public class MyTask extends AsyncTask<Object, Object, ArrayList<RowData>> {

    private WeakReference<Listener> mLister;

    @Override
    protected ArrayList<RowData> doInBackground(Object... objects) {
        ArrayList<RowData> list = new ArrayList<>();
        for (int i = 1; i <= 10 ;i++){
            RowData rowData = new RowData();
            rowData.setTitle("title " + i);
            rowData.setDetail("detail " + i);
            list.add(rowData);
        }

        try {
            Thread.sleep(3000);
        }catch (InterruptedException e){

        }

        return list;
    }

    @Override
    protected void onPostExecute(ArrayList<RowData> list) {
        super.onPostExecute(list);
        mLister.get().onFinish(list);
    }

    interface Listener {
        void onFinish(ArrayList<RowData> list);
    }

    void setLister(WeakReference<Listener> lister) {
        mLister = lister;
    }
}
```


