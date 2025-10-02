<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [Preference の変更を監視する LiveData](#preference-の変更を監視する-livedata)
  - [ユーティリティ](#ユーティリティ)
  - [使用上の注意点](#使用上の注意点)
  - [トラブルシューティング](#トラブルシューティング)
    - [プリファレンスの値は更新されるが、 LiveData の値が更新されない場合](#プリファレンスの値は更新されるが-livedata-の値が更新されない場合)
    - [SharedPreferenceLiveData の onActive() が呼ばれない場合](#sharedpreferencelivedata-の-onactive-が呼ばれない場合)
<!-- TOC END -->


# Preference の変更を監視する LiveData

## ユーティリティ

**SharedPreferenceLiveData**

```java
abstract class SharedPreferenceLiveData<T> extends MutableLiveData<T> {

    SharedPreferences sharedPrefs;
    private String key;
    private T defValue;

    SharedPreferenceLiveData(SharedPreferences prefs, String key, T defValue) {
        this.sharedPrefs = prefs;
        this.key = key;
        this.defValue = defValue;
    }

    private SharedPreferences.OnSharedPreferenceChangeListener listener
            = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (SharedPreferenceLiveData.this.key.equals(key)) {
                setValue(getValueFromPreferences(key, defValue));
            }
        }
    };

    abstract T getValueFromPreferences(String key, T defValue);

    @Override
    protected void onActive() {
        super.onActive();
        setValue(getValueFromPreferences(key, defValue));
        sharedPrefs.registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    protected void onInactive() {
        sharedPrefs.unregisterOnSharedPreferenceChangeListener(listener);
        super.onInactive();
    }

}
```


**SharedPreferenceBooleanLiveData**

```java
public class SharedPreferenceBooleanLiveData extends SharedPreferenceLiveData<Boolean> {

    public SharedPreferenceBooleanLiveData(SharedPreferences prefs, String key, Boolean defValue) {
        super(prefs, key, defValue);
    }

    @Override
    Boolean getValueFromPreferences(String key, Boolean defValue) {
        return sharedPrefs.getBoolean(key, defValue);
    }
}
```


**SharedPreferenceIntegerLiveData**

```java
public class SharedPreferenceIntegerLiveData extends SharedPreferenceLiveData<Integer> {

    public SharedPreferenceIntegerLiveData(SharedPreferences prefs, String key, Integer defValue) {
        super(prefs, key, defValue);
    }

    @Override
    Integer getValueFromPreferences(String key, Integer defValue) {
        return sharedPrefs.getInt(key, defValue);
    }
}
```


**SharedPreferenceStringLiveData**

```java
public class SharedPreferenceStringLiveData extends SharedPreferenceLiveData<String> {

    public SharedPreferenceStringLiveData(SharedPreferences prefs, String key, String defValue) {
        super(prefs, key, defValue);
    }

    @Override
    String getValueFromPreferences(String key, String defValue) {
        return sharedPrefs.getString(key, defValue);
    }
}
```


**ViewModel**

```java
private SharedPreferenceBooleanLiveData isShopStaffAuthenticated;

public SharedPreferenceBooleanLiveData getIsShopStaffAuthenticated() {
    if (isShopStaffAuthenticated == null) {
        isShopStaffAuthenticated = new SharedPreferenceBooleanLiveData(
                preferences, PrefConst.IS_SHOP_STAFF_AUTHENTICATED, false);
    }
    return isShopStaffAuthenticated;
}
```


## 使用上の注意点

**必ず、定義した LiveData を observe() メソッドで監視するか、 layout.xml から参照することで監視してください。**

監視を開始することで、 `SharedPreferenceLiveData` クラスの `onActive()`  
メソッドが呼ばれ、プリファレンスに値がまだセットされていない状況でも、  
`LiveData` にだけ、初期値がセットされます。

**監視を開始しなくても、 LiveData に初期値がセットされる仕組みに変更したほうが良いかもしれない。**


## トラブルシューティング

### プリファレンスの値は更新されるが、 LiveData の値が更新されない場合

`OnSharedPreferenceChangeListener` の `onSharedPreferenceChanged()` が呼ばれていない  
可能性があるります。

その原因は、 `SharedPreferenceLiveData` の `onActive()` が呼ばれないために、  
リスナーが登録されていない可能性があるため、次項の `onActive() が呼ばれない場合` を  
参照してみてください。


### SharedPreferenceLiveData の onActive() が呼ばれない場合

以下のように、データバインディング式で LiveData を監視している場合は、  
データバインディングのライフサイクルオーナーの設定が間違っている場合があります。

```xml
<View
  android:enabled="@{viewModel.isShown ? false : true}" />
```

結論としては、 Fragment 内の `setLifecycleOwner()` メソッドに渡すパラメータを変更してみると  
解決する可能性があります。

```java
public View onCreateView(@NonNull LayoutInflater inflater,
                         @Nullable ViewGroup container,
                         @Nullable Bundle savedInstanceState) {
    FragmentWinLoseBinding binding =
            DataBindingUtil.inflate(
                    inflater,
                    R.layout.fragment_win_lose,
                    container,
                    false);
    // パターン１
    binding.setLifecycleOwner(backstackEntry);
    // パターン２
    binding.setLifecycleOwner(getViewLifecycleOwner());
    return binding.getRoot();
}
```

**原因**

上記の「パターン１」のように、 `setLifecycleOwner()` メソッドに、 `backstackEntry`  
(すなわち、 Fragment のライフサイクル) を渡してしまうと、 `View` が破棄されても、  
データバインディング式のみ、残り続けてしまうことがあります。

結果として、 `View` が再生成された際に、 `LiveData` の更新が、新しい `View` には伝わらず、  
既に破棄された古い `View` に伝わってしまうことがあります。

そのため、新しい View が表示されている画面上では、何も見た目が更新されない、  
という事象が起こることがあります。
