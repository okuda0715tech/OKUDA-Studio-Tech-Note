<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [Fragmentへの参照を取得する方法](#fragmentへの参照を取得する方法)
<!-- TOC END -->


# Fragmentへの参照を取得する方法


```java
//in Java
FragmentManager.findFragment(view)

//in Kotlin there is an extension function
view.findFragment()
```

指定されたViewに関連づけられているFragmentを返します。  
Viewは、Fragmentの `onCreateView()` で返されたView、もしくはその子孫Viewを指定することができます。  
関連づけられているFragmentを見つけられなかった場合は、 `IllegalStateException` が返されます。

```java
// カスタムViewの中で実装する場合は、以下のように onAttachedToWindow 内で実装するのが良さそうです。
@Override
protected void onAttachedToWindow() {
    super.onAttachedToWindow();

    Fragment fragment = FragmentManager.findFragment(this);
}
```
