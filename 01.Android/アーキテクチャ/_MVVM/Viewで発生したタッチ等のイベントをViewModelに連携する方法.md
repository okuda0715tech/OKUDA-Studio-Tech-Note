<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [Viewで発生したタッチ等のイベントをViewModelに連携する方法](#viewで発生したタッチ等のイベントをviewmodelに連携する方法)
  - [サンプル](#サンプル)
<!-- TOC END -->


# Viewで発生したタッチ等のイベントをViewModelに連携する方法

## サンプル

**CustomView.java**

```Java
public class CustomView implements View.OnClickListener {

  OnUpdateListener listener;

  public void setOnUpdate(OnUpdateListener listener) {
    this.listener = listener;
  }

  public interface OnUpdateListener {
    void onUpdate(ViewData viewData);
  }

  ViewData viewData;

  // 例えば、 View をクリックした場合に、クリックイベントを ViewModel へ連動したい場合
  @Override
  public void onClick(View view) {
    listener.onUpdate(viewData);
  }
}
```

**レイアウト.xml**

```xml
<CustomView
  app:onUpdate="@{(viewData) -> viewModel.update(viewData)}"
  />
```

**MyViewModel.java**

```Java
public class MyViewModel extends ViewModel {

  public void update(ViewData viewData) {
    // do something.
  }

}
```
