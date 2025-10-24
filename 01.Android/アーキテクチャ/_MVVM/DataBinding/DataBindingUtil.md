<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [DataBindingUtil](#databindingutil)
	- [\<T extends ViewDataBinding> setContentView(Activity activity, int layoutId)](#t-extends-viewdatabinding-setcontentviewactivity-activity-int-layoutid)

<!-- /TOC -->

# DataBindingUtil

## \<T extends ViewDataBinding> setContentView(Activity activity, int layoutId)

**MainActivity.java**

```Java
private ActivityMainBinding binding;

@Override
protected void onCreate(Bundle savedInstanceState) {
  binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
}
```

`ActivityMainBinding`クラスは、`activity_main.xml`ファイルから自動生成される。xmlファイルのルートに`<layout>`タグが含まれるxmlファイルがあれば生成する。



