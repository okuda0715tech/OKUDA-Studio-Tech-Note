<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [include_merge](#include_merge)
  - [include タグ](#include-タグ)
  - [merge タグ](#merge-タグ)
  - [tools:showIn 属性を指定すると子レイアウトを親レイアウトに組み込んだプレビューを行う](#toolsshowin-属性を指定すると子レイアウトを親レイアウトに組み込んだプレビューを行う)
<!-- TOC END -->


# include_merge

## include タグ

`<include>` タグは、親となるレイアウト xml 内に定義します。  
子となるレイアウト xml を読み込むために使用します。


## merge タグ

`<merge>` タグは、子となるレイアウト xml のルート ViewGroup として定義することで  
ViewGroup のネストが深くなるのを防ぐために使用します。

例えば、 parent.xml と child.xml があったとします。

**parent.xml**

```xml
<ConstraintLayout>
  <View />
  <include />
  <View />
</ConstraintLayout>
```

**child.xml**

```xml
<!--
merge タグを使用しない場合、 ConstraintLayout など、何らかの ViewGroup を定義する必要がある。
もし、 child.xml のルート ViewGroup に ConstraintLayout を定義した場合、
arent.xml の ConstraintLayout と child.xml の ConstraintLayout でネストが生じてしまう。
 -->
<merge>
  <View />
  <View />
  <View />
</merge>
```

`<merge>` タグは、親となるレイアウト xml が子となるレイアウト xml を読み込んだ時に削除されます。


## tools:showIn 属性を指定すると子レイアウトを親レイアウトに組み込んだプレビューを行う

子レイアウト xml のルート ViewGroup に対して `tools:showIn="@layout/parent_layout"`  
属性を指定すると、 Android Studio で子レイアウト xml を開いたときに、親レイアウトに  
組み込んだ状態のプレビュー表示を行ってくれます。

（例）

**parent.xml**

```xml
<ConstraintLayout>
  <include layout="@layout/child" />
</ConstraintLayout>
```

**child.xml**

```xml
<merge xmlns:tools="http://schemas.android.com/tools"
  tools:showIn="@layout/parent">
  <View />
</merge>
```

上記の例のようにすると child.xml ファイルを Android Studio で開いた場合に、  
プレビュー画面では、 parent.xml に組み込まれた child.xml のレイアウトを表示してくれます。
