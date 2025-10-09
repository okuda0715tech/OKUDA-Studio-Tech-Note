<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [Activityへの参照を取得する方法](#activityへの参照を取得する方法)
<!-- TOC END -->


# Activityへの参照を取得する方法

```java
private Activity getActivity() {
    Context context = getContext();
    while (context instanceof ContextWrapper) {
        if (context instanceof Activity) {
            return (Activity)context;
        }
        context = ((ContextWrapper)context).getBaseContext();
    }
    return null;
}
```

[参考にしたサイト - How to get hosting Activity from a view?](https://stackoverflow.com/questions/8276634/how-to-get-hosting-activity-from-a-view)
