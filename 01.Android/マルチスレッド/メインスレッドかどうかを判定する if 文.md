<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [メインスレッドかどうかを判定する if 文](#メインスレッドかどうかを判定する-if-文)
<!-- TOC END -->


# メインスレッドかどうかを判定する if 文

```java
private boolean isCurrentThreadMainThread (){
     return Thread.currentThread().equals(getContext().getMainLooper().getThread());
}
```
