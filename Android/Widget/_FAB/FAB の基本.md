- [FAB の基本](#fab-の基本)
  - [サイズを変更する](#サイズを変更する)
    - [`app:fabSize="mini/normal"`を使用した方法](#appfabsizemininormalを使用した方法)
    - [`app:fabCustomSize="xxdp"`を使用した方法](#appfabcustomsizexxdpを使用した方法)
  - [大小のFABを並べて表示する場合](#大小のfabを並べて表示する場合)
  - [FABのデフォルトのテーマ](#fabのデフォルトのテーマ)
  - [リップルエフェクトの色の変更](#リップルエフェクトの色の変更)


# FAB の基本

## サイズを変更する

### `app:fabSize="mini/normal"`を使用した方法

アプリ全体のテーマとしてFABのサイズを指定したい場合は、この方法しかない。この方法だと、大小二種類までしかテーマとしてサイズを指定できないため、もし、それ以上の種類が存在する場合は、テーマとして適用できないため、各FABごとに、`app:fabCustomSize="xxdp"`で指定する必要がある。

`res/values/dimens.xml`などに以下を定義し、サイズを指定する。

```xml
<!-- カスタマイズは可能だが画面密度の多様性に対応するためには、規定値が良いのだろう。
 もしカスタマイズするなら、8の倍数、無理ならせめて4の倍数がよさそう。4の倍数ならdp->px変換後に必ず
 整数値になるため。 -->
<!-- 規定値だとマージンも迷わなくて済むためおすすめ。カスタマイズ値だと、normalサイズとminiサイズの
 FABを並べるときに少し考えてマージンを決める必要が出てくるかもしれない。 -->
<!-- 既定値はnormalが56dp、miniが40dpである。 -->
<dimen name="design_fab_size_normal" tools:override="true">50dp</dimen>
<dimen name="design_fab_size_mini" tools:override="true">40dp</dimen>
```

**レイアウトxml**

```Java
<!-- layout_width,layout_heightはwrap_contentにする -->
<!-- app:fabSizeはmini,normal,autoがある。mini,normalはdimens.xmlで指定したサイズになる。 -->
<!-- miniは少しpaddingができるので注意すること。 -->
<!-- app:fabCustomSize="xxdp"でもサイズ指定が可能である。これだと第三のサイズも作成できる。 -->
<com.google.android.material.floatingactionbutton.FloatingActionButton
    android:id="@+id/parent_fab"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_marginEnd="8dp"
    android:layout_marginBottom="8dp"
    android:onClick="@{listener::onClickParentFab}"
    app:customRotation="@{fabState.getValue() * @integer/rotation_135}"
    app:fabSize="mini"
    app:layout_constraintBottom_toBottomOf="parent"
    app:layout_constraintEnd_toEndOf="parent"
    app:rippleColor="@color/colorRed"
    app:srcCompat="@drawable/ic_open_fap_menu_24dp" />
```

`app:srcCompat="xxx"`は背景に表示するアイコンを指定する。

`app:fabSize`属性を指定しなかった場合は、`normal`がデフォルト値となる。


### `app:fabCustomSize="xxdp"`を使用した方法

`app:fabCustomSize="xxdp"`を指定した場合は、`app:fabSize="mini/normal"`が指定してあったとしても、その指定を無視してfabCustomSizeを適用する。

```xml
<com.google.android.material.floatingactionbutton.FloatingActionButton
    android:id="@+id/parent_fab"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_marginEnd="8dp"
    android:layout_marginBottom="8dp"
    android:onClick="@{listener::onClickParentFab}"
    app:customRotation="@{fabState.getValue() * @integer/rotation_135}"
    app:fabCustomSize="70dp"
    app:layout_constraintBottom_toBottomOf="parent"
    app:layout_constraintEnd_toEndOf="parent"
    app:rippleColor="@color/colorRed"
    app:srcCompat="@drawable/ic_open_fap_menu_24dp" />
```


## 大小のFABを並べて表示する場合

- 大小のFABを並べて表示したい場合
  - Layoutが`ConstraintLayout`の場合
    - 小さい方のFABの上下左右の制約を大きいほうのFABの上下左右につけてあげる。
  - Layoutが`LinearLayout`などの場合
    - 大小のFABに`android:layout_gravity="center"`を指定してあげると、親ViewGroupの中の中心に配置され、そろえることができる。
    - Marginにも注意が必要かもしれない。
      - デフォルトのFABサイズなら、大の直径56dp/半径28dp、小の直径40dp/半径20dpであるため、大のMarginを8dp、小のMarginを16dpにするとちょうどMarginを含めたlayout_heightとlayout_widthが同じになるため、FABをきれいに並べられるはず。


以下はLinearLayoutの場合のサンプル

```xml
<LinearLayout
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:padding="8dp"
    android:clipToPadding="false"
    android:orientation="horizontal">
    <!-- 大 -->
  <com.google.android.material.floatingactionbutton.FloatingActionButton
      android:layout_width="wrap_content"
      android:layout_height="wrap_content"
      android:layout_margin="8dp"
      android:layout_gravity="center"
      android:contentDescription="@string/cat_theme_fab_content_desc"
      app:srcCompat="@drawable/ic_add_24px"/>
      <!-- 小 -->
  <com.google.android.material.floatingactionbutton.FloatingActionButton
      android:layout_width="wrap_content"
      android:layout_height="wrap_content"
      android:layout_margin="16dp"
      android:layout_gravity="center"
      android:contentDescription="@string/cat_theme_fab_mini_content_desc"
      app:srcCompat="@drawable/ic_add_24px"
      app:fabSize="mini"/>
</LinearLayout>
```


## FABのデフォルトのテーマ

```xml
<style name="Base.Theme.MemoMVVM" parent="Theme.MaterialComponents.DayNight.NoActionBar">
  <item name="floatingActionButtonStyle">@style/Widget.MaterialComponents.FloatingActionButton</item>
</style>
```

デフォルトのテーマの中には、FABのサイズを変更する属性は存在していない。そのため、アプリ全体のテーマとしてFABのサイズを指定したい場合は、「`app:fabSize="mini/normal"`を使用した方法」で説明した方法を使用するしかない。


## リップルエフェクトの色の変更

```xml
app:rippleColor
```

```Java
setRippleColor()
```




