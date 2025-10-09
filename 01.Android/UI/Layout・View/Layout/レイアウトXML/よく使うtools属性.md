<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [よく使うtools属性](#使tools属性)
	- [tools:context=".MainActivity"](#toolscontextmainactivity)

<!-- /TOC -->


# よく使うtools属性

## tools:context=".MainActivity"

`tools:context=".MainActivity"`をレイアウトXMLファイルのルートViewに設定しておくと、  
`Button`ウィジェットなどの`android:onClick=""`のリスナーとして、指定したコンテキストを参照してくれる。

（例）

```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="16dp"
    tools:context=".MainActivity">

    <Button
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Schedule Job"
        android:onClick="scheduleJob"   <--- MainActivityのscheduleJob(View view)メソッドを参照してくれる。
        android:layout_gravity="center_horizontal"
        android:layout_margin="4dp"/>

</LinearLayout>
```

```java
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // tools:context=".MainActivity" を記述することで android:onClick="scheduleJob"
    // の参照先がこのMainActivityになり、クリックイベントがこのメソッドに紐づく。
    public void scheduleJob(View view){

    }
}
```
