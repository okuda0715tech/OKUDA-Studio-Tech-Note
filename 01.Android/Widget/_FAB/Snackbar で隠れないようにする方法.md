- [Snackbar で隠れないようにする方法](#snackbar-で隠れないようにする方法)
  - [基本形](#基本形)
  - [邪道形](#邪道形)


# Snackbar で隠れないようにする方法

## 基本形

`FAB` を `CoordinatorLayout` で囲み、 `Snackbar` の第一引数には、 CoordinatorLayout か、その配下の View を渡す。

```xml
<?xml version="1.0" encoding="utf-8"?><!--
CoordinatorLayout の直下に、右下寄せで FAB を設置すると、
Snackbar が表示された時に FAB が自動的に Snackbar と
重ならない位置に移動してくれる。 -->
<androidx.coordinatorlayout.widget.CoordinatorLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/coordinatorLayout"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <androidx.constraintlayout.widget.ConstraintLayout
        android:id="@+id/content_root_cl"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@string/snackbar_sample_tv"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent" />

    </androidx.constraintlayout.widget.ConstraintLayout>

    <com.google.android.material.floatingactionbutton.FloatingActionButton
        android:id="@+id/fab"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="end|bottom"
        android:layout_margin="16dp"
        android:contentDescription="@string/fab_description"
        android:src="@drawable/ic_launcher_foreground" />

</androidx.coordinatorlayout.widget.CoordinatorLayout>
```

```kotlin
class MainActivity : AppCompatActivity() {

    private lateinit var coordinatorLayout: CoordinatorLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        coordinatorLayout = findViewById(R.id.coordinatorLayout)
        val fab: FloatingActionButton = findViewById(R.id.fab)

        fab.setOnClickListener {
            // 第一引数の View には、 CoordinatorLayout か、その配下の View を渡す必要がある。
            Snackbar.make(it, "Snackbar with FAB", Snackbar.LENGTH_LONG).show()
        }
    }
}
```


## 邪道形

Snackbar は、自分から (階層的に) 一番近くにある CoordinatorLayout の中にインフレートされるため、 Snackbar を表示したい部分に CoordinatorLayout を配置し、 Snackbar の make 関数の第一引数に、その CoordinatorLayout を渡します。その際に、 FAB の Bottom が、 CoordinatorLayout の Top に対して、制約をかけている必要があります。こうすることで、 Snackbar と FAB の表示が重なるのを防ぐことが可能です。

ただし、この方法は公式ドキュメントでは紹介されていないため、邪道な方法です。

```xml
<androidx.constraintlayout.widget.ConstraintLayout
    android:id="@+id/memo_list_act_root"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <com.google.android.material.floatingactionbutton.loatingActionButton
        android:id="@+id/parent_fab"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginEnd="@dimen/interval_10dp"
        android:layout_marginBottom="@dimen/interval_10dp"
        android:onClick="@{listener::onClickParentFab}"
        app:customRotation="@{fabState.getValue() * integer/rotation_135}"
        app:layout_constraintBottom_toTopOf="@id/nack_bar_area_cl"
        app:layout_constraintEnd_toEndOf="parent"
        app:srcCompat="@drawable/ic_open_fap_menu_24dp" />
            
    <androidx.coordinatorlayout.widget.CoordinatorLayout
        android:id="@+id/snack_bar_area_cl"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent" />

</androidx.constraintlayout.widget.ConstraintLayout>
```
