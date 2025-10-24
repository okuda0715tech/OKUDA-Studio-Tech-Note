<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [RecyclerViewのDataBinding](#recyclerviewのdatabinding)

<!-- /TOC -->


# RecyclerViewのDataBinding

**UserAdapter.java**

```java
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> mUserList;

    public void onRowDataClicked(User user){}

    UserAdapter(List<User> userList) {
        this.mUserList = userList;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder{
        // **************************
        // DataBindingを使用する場合
        // **************************
        // 「RowItemViewBinding」はrow_item_view.xmlのように一行分のレイアウトxmlファイルの名称から決定する。
        private final RowItemViewBinding binding;

        public UserViewHolder(final RowItemViewBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }

        // **************************
        // DataBindingを使用しない場合
        // **************************
        TextView name;
        TextView address;

        public UserViewHolder(View viewHolder){
            super(viewHolder);
            name = itemView.findViewById(R.id.name_text);
            address = itemView.findViewById(R.id.address_text);
        }
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // DataBindingを使用する場合
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RowItemViewBinding binding = DataBindingUtil.inflate(layoutInflater,     R.layout.row_item_view, parent, false);

        return new UserViewHolder(binding);

        // DataBindingを使用しない場合
        View userView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_view, parent, false);
        final UserViewHolder userViewHolder = new UserViewHolder(userView);

        userViewHolder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                final int position = userViewHolder.getAdapterPosition();
                User user = mUserList.get(position);
                onRowDataClicked(user);
            }
        });

        return userViewHolder;
    }

    @Override
    public void onBindViewHolder(final @NonNull UserViewHolder viewHolder, int position) {
        // DataBindingを使用する場合(方法1)
        viewHolder.binding.setUser(mUserList.get(position));
        viewHolder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRowDataClicked(mUserList.get(viewHolder.getAdapterPosition()));
            }
        });

        // DataBindingを使用する場合(方法2)
        final T item = items.get(position);
        viewHolder.getBinding().setVariable(BR.item, item);
        viewHolder.getBinding().executePendingBindings();

        // DataBindingを使用しない場合
        viewHolder.name.setText(mUserList.get(position).mName);
        viewHolder.address.setText(mUserList.get(position).mAddress);
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

}
```

\<UserViewHolderについて>

DataBindingを使用しない場合は、一行分のデータの中に保有するViewの数だけフィールドを定義しなければいけなかったが、DataBindingを使用すると、Viewをいくつ保有してもバインド情報を一つ定義するだけで済むのですっきりとする。

\<onCreateViewHolderについて>

DataBindingを使用する場合は、クリック時のリスナーの定義はしない。
クリックしたポジションを取得することができないためである。

\<onBindViewHolderについて>

RecyclerViewでは、スクロール時にカクカクする場合があるので、`executePendingBindings`を呼ぶと良い。こうすることで、即時にバインドしてくれる。


**MainActivity.java**

ポイントとなるところは、`findViewById(R.id.user_recycler)`の代わりに`binding.userRecycler`を使用しているところである。

```java
public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initRecycler(binding);
    }

    private void initRecycler(ActivityMainBinding binding) {
        mRecyclerView = binding.userRecycler;
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.setAdapter(getAdapter());
    }

    private UserAdapter getAdapter() {
        return new UserAdapter(createUsers()) {
                @Override
                public void onRowDataClicked(@NonNull User user) {
                    super.onRowDataClicked(user);
                    Toast.makeText(getApplicationContext(), user.mName, Toast.LENGTH_LONG).show();
                }
            };
    }

    private List<User> createUsers() {
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            User User = new User();
            User.mName = "name" + i;
            User.mAddress = "Detail" + i;
            userList.add(User);
        }
        return userList;
    }
}
```


**activity_main.xml**

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout>

    <data>

    </data>

    <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical">

        <android.support.v7.widget.RecyclerView
            android:id="@+id/user_recycler"
            android:layout_width="match_parent"
            android:layout_height="wrap_content">

        </android.support.v7.widget.RecyclerView>

    </LinearLayout>
</layout>
```


**row_item_view.xml**

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout>

    <data>

        <variable
            name="user"
            type="com.kurodai0715.mydatabindingrecycler.User" />
    </data>

    <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:tools="http://schemas.android.com/tools"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_margin="2dp"
        android:background="@color/colorBlue"
        android:orientation="vertical"
        android:padding="4dp">

        <TextView
            android:id="@+id/name_text"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@{user.mName}"
            tools:text="名前" />

        <TextView
            android:id="@+id/address_text"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@{user.mAddress}"
            tools:text="住所" />

    </LinearLayout>
</layout>
```
