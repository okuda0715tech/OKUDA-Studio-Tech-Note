<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [xmlの属性に指定する@androidなどの意味](#xmlの属性に指定するandroidなどの意味)
  - [レイアウトの 「 @android:\*/\* 」 , 「 ?attr/\* 」 , 「 ?\* 」 , 「 ?android:attr/\* 」 の違い](#レイアウトの--android----attr--------androidattr--の違い)
    - [「android」 は SDK に定義されているリソースを指す](#android-は-sdk-に定義されているリソースを指す)
    - [「?」 はテーマに定義されている item を指す](#-はテーマに定義されている-item-を指す)
    - [「attr」 は 「?」 の場合にだけ使用しますが、あってもなくても同じです。](#attr-は--の場合にだけ使用しますがあってもなくても同じです)
  - [名前空間 「android」 と 「app」 の違い](#名前空間-android-と-app-の違い)
  - [カスタムViewを作成した場合の独自属性の名前空間](#カスタムviewを作成した場合の独自属性の名前空間)
  - [独自属性の定義、参照方法](#独自属性の定義参照方法)
  - [xmlファイルからリソースを参照する構文](#xmlファイルからリソースを参照する構文)
  - [xmlファイルから現在のテーマに設定されているリソースを参照する構文](#xmlファイルから現在のテーマに設定されているリソースを参照する構文)

<!-- /TOC -->


# xmlの属性に指定する@androidなどの意味

## レイアウトの 「 @android:\*/\* 」 , 「 ?attr/* 」 , 「 ?* 」 , 「 ?android:attr/* 」 の違い

### 「android」 は SDK に定義されているリソースを指す



### 「?」 はテーマに定義されている item を指す

（例）

**activity_main.xml**

```Xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"  
    android:background="?android:attr/selectableItemBackground"  
    android:layout_width="match_parent"  
    android:layout_height="wrap_content" />  
```

**styles.xml**

```Xml
<style name="Theme">  
    <item name="selectableItemBackground">@android:drawable/item_background</item>  
    ...  
</style>
```

### 「attr」 は 「?」 の場合にだけ使用しますが、あってもなくても同じです。

`attr` はあってもなくても同じである為、省略しても大丈夫です。


## 名前空間 「android」 と 「app」 の違い

`android` と `app` はほとんどの場合、以下の URI を示している。

```
xmlns:android="http://schemas.android.com/apk/res/android"
xmlns:app="http://schemas.android.com/apk/res-auto"
```

上記の場合、 `android` は `platform API` を指し、 `app` は `Androidx` を指している。


## カスタムViewを作成した場合の独自属性の名前空間

```
xmlns:custom="http://schemas.android.com/apk/res/アプリのパッケージ名"
```

詳細は以下を参照

[カスタム属性を定義する](https://developer.android.com/training/custom-views/create-view.html#customattr)


## 独自属性の定義、参照方法

**res/value/attrs.xml**

```xml
<resources>
  <declare-styleable name="DynamicHeightView">
    <attr name="aspectRatio" format="float"/>
  </declare-styleable>
</resources>
```

```java
public DynamicHeightView extends View{
  private float mAspectRatio;

  public DynamicHeightView(Context context, AttributeSet attrs){
    super(context, attrs);

    TypedArray a = context.getTheme().obtainStyledAttributes(
      attrs,
      R.stylable.DynamicHeightView,
      0, 0
    );

    try{
      mAspectRatio = a.getFloat(R.styleable.DynamicHeightView_aspectRatio, 0);
    }finally{
      a.recycle();
    }
  }
}
```

**layout.xml**

```xml
<パッケージ名.フォルダパス.DynamicHeightView
  custom:aspectRatio="0.75"
  />
```


## xmlファイルからリソースを参照する構文

```xml
@[<package_name>:]<resource_type>/<resource_name>
```

- `<package_name>`
  - リソースが配置されるパッケージの名前です（同一のパッケージからリソースを参照する場合は省略可能です）。
- `<resource_type>`
  - タグで定義したリソースを参照する場合は、タグ名を記載します。（例：`<string name="xxx">`というリソースなら、`string`の部分）
  - ファイルで定義したリソースを参照する場合は、フォルダ名を記載します。（例：`res/color/custom_blue.xml`というリソースなら、`color`の部分）
    - Androidアプリ開発で`res`フォルダ直下に定義できるファイル名は以下のみです。
    - `animator`, `anim`, `color`, `drawable`, `mipmap`, `layout`, `menu`, `raw`, `values`, `xml`, `font`
    - 詳しくは、次のリンクを参照 -> [リソースタイプをグループ化する](https://developer.android.com/guide/topics/resources/providing-resources?hl=ja#ResourceTypes)
  - 上記のいづれの場合も、`R`クラスでは、リソースタイプごとにサブクラス化されて管理されています。
- `<resource_name>`
  - タグで定義したリソースを参照する場合は、タグのname属性の値を記載します。（例：`<string name="xxx">`というリソースなら、`xxx`の部分）
  - ファイルで定義したリソースを参照する場合は、拡張子を除くファイル名を記載します。（例：`res/color/custom_blue.xml`というリソースなら、`custom_blue`の部分）


 (使用例)

```xml
<Button
    android:layout_width="fill_parent"
    android:layout_height="wrap_content"
    android:text="@string/submit" />
```

```xml
<!-- platformAPI(sdk)のリソースを参照する場合は、固定値(android:)とすること。
".../apk/res/android"の名前空間の名前に合わせても動作はするが、エディタ上赤色で警告が出るため避けること。 -->
<TextView
  android:background="@android:color/holo_orange_light"/>
```

なお、 Java コードで platformAPI (sdk) のリソースにアクセスする場合は、  
`android.R.layout.simple_list_item_1` のように、 `R` の前に `android.` を付与します。


## xmlファイルから現在のテーマに設定されているリソースを参照する構文

```xml
<!-- リソース参照との違いは "@" で始まるか "?" で始まるかと、
リソースタイプが省略可能かどうかです。
"?" で始まる時は、常に "attr" 属性を参照するため、 <resource_type> は省略可能です。 -->
?[<package_name>:][<resource_type>/]<resource_name>
```

（参考）テーマ属性の値の設定元を探すには、 Theme クラスの内部コードを読まないといけないので、  
探すのは困難かもしれない。

実際の開発で、上記のフォーマットのリンクをクリックして設定元にジャンプすると、 `<attr>` タグに  
ジャンプするため、 `?` マークは `<attr>` タグを参照する構文かな？と思ってしまうが、  
`<attr>` タグは、単に属性名とその型の定義にすぎません。  
実際に属性の値をセットしているところは他のところにあります。




