<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [include したレイアウトへの ViewBinding](#include-したレイアウトへの-viewbinding)
  - [include されているレイアウトファイルが merge でない場合](#include-されているレイアウトファイルが-merge-でない場合)
  - [include されているレイアウトファイルが merge である場合](#include-されているレイアウトファイルが-merge-である場合)
<!-- TOC END -->


# include したレイアウトへの ViewBinding

## include されているレイアウトファイルが merge でない場合

**include する側**

```xml
<include
    android:id="@+id/your_id"
    layout="@layout/some_layout" />
```

**include される側**

```xml
<ViewGroup>
  <TextView
    android:id="@+id/a_text_view" />
</ViewGroup>
```

**Activity or Fragment 側**

```java
TextView textView = binding.yourId.aTextView;
```


## include されているレイアウトファイルが merge である場合

以下を参照。まだ読んでいないので、どうやるかわからない。  
一番評価の高い回答の 2 番の説明を参照。

[how to get binding for included layouts?](https://stackoverflow.com/questions/58730127/viewbinding-how-to-get-binding-for-included-layouts)
