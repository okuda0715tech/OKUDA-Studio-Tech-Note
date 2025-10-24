<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [BindingAdapter2](#bindingadapter2)
  - [前提知識](#前提知識)
    - [セッターメソッドの自動選択](#セッターメソッドの自動選択)
    - [セッターメソッドの選択方法をカスタマイズする](#セッターメソッドの選択方法をカスタマイズする)
  - [BindingAdapter](#bindingadapter)
    - [BindingAdapterを定義する場所](#bindingadapterを定義する場所)
    - [複数のパラメータを渡したい場合](#複数のパラメータを渡したい場合)
    - [requireAll属性について](#requireall属性について)
    - [古い値を受け取りたい場合](#古い値を受け取りたい場合)
    - [イベントハンドラのサンプル](#イベントハンドラのサンプル)
  - [InverseBindingAdapter](#inversebindingadapter)
<!-- TOC END -->


# BindingAdapter2

## 前提知識

### セッターメソッドの自動選択

データバインディングライブラリは、あるViewクラス内に、レイアウトxmlで指定した属性（フィールド）  
が存在しない場合、そのフィールドのセッターメソッドを自動的に探しに行きます。

例えば、次にようなレイアウトxmlが存在する場合

```xml
<android.support.v4.widget.DrawerLayout
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:scrimColor="@{@color/scrim}"
        app:drawerListener="@{fragment.drawerListener}">
```

`app:scrimColor="@{@color/scrim}"` の部分を参照して、 `DrawerLayout` クラス内から自動的に `setScrimColor(int param)`  
というセッターメソッドを探します。探すメソッドの名前は、 `app:scrimColor` の先頭に `set` が  
付与されたものになります。探すメソッドのパラメータの型は、 `@color/scrim` の部分の型から決定されます。


### セッターメソッドの選択方法をカスタマイズする

前項「セッターメソッドの自動選択」で記載した通り、通常は、レイアウトxmlファイル内のViewの  
属性名の先頭に `set` を付与したセッターメソッドが検索されます。ただし、別のメソッドを検索するように  
カスタマイズすることができます。

以下の例では、通常ならば、 `setTint()` メソッドを検索するところ、  
`android.widget.ImageView` クラスの  
`setImageTintList()` メソッドを検索するようにカスタマイズしています。

```xml
<ImageView
    android:tint="@{xxxx}" />
```

```java
// この記述はどのクラス内に書いても問題ありません。
// クラスの{}の外側に書くことも可能であり、むしろそのほうが普通かもしれません。
@BindingMethods({
       @BindingMethod(type = "android.widget.ImageView",
                      attribute = "android:tint",
                      method = "setImageTintList"),
})
// type には、 type = ImageView.class と記述する方法もあるみたい。
```

`@BindingMethods({})` 内に複数の `@BindingMethod()` を記述することができます。  
`@BindingMethod()` は、一つの属性につき一つ定義します。


## BindingAdapter

セッターメソッドが存在しない場合、または、存在するが処理内容をカスタマイズしたい場合に、  
`@BindingAdapter` の出番になります。  
ただし、カスタムViewにセッターメソッドを作成したい場合は、 `@BindingAdapter` を使用しなくても、  
カスタムViewに `setXxx` メソッドを定義するだけで `@BindingAdapter` を使用するのと  
同じ役割をしてくれます。

例えば、Viewクラスに `setPaddingLeft()` メソッドは存在していませんが、以下のように  
`@BindingAdapter` を使用することで `android:paddingLeft` 属性に対応したメソッドを定義することができます。

```java
// 第一引数の view の型には、このセッターメソッドを適用するクラスを制限する役割があります。
// View を指定した場合は、すべての View において、このメソッドが適用されますが、
// 例えば、 ImageView を指定した場合には、 ImageView とその子孫Viewにのみ、このメソッドが適用されます。
//
// 「@BindingAdapter(xxx)」の「xxx」の部分で属性名を定義します。
// ここで定義した属性名を使用して、xmlでは「app:xxx="@{実パラメータ}"」のようにパラメータをセットします。
// 「@BindingAdapter(xxx)」の「xxx」の部分には、名前空間を指定する必要はありません。
// ただし、名前空間が「android:」の場合は指定する必要があります。
//
// java側の例：@BindingAdapter({"customTranslationY"})
// xml側の例：app:customTranslationY="@{param}"
//
// メソッド名は任意です。
// staticメソッドにする必要があります。
// パラメータは、第一引数がView、第二引数以降がxmlで渡したものです。
@BindingAdapter({"android:paddingLeft"}) // <- パラメータが一つの場合は、中カッコを省略可能
public static void setPaddingLeft(View view, int padding) {
  view.setPadding(padding,
                  view.getPaddingTop(),
                  view.getPaddingRight(),
                  view.getPaddingBottom());
}
```

**@BindingAdapterの役割**

`@BindingAdapter` には、以下の二つの役割があります。

- レイアウトxmlでバインド式を受け取る独自の属性（ `app:xxx="@{yyy}"` など）を定義する役割
- レイアウトxmlで定義した独自の属性に与えられたパラメータを受け取り、その View の属性（Javaのフィールド）を更新する役割


### BindingAdapterを定義する場所

`@BindingAdapter` アノテーションを付与したメソッドはどこに定義することも可能であるようだ。  
フレームワークでは、 `TextView` クラスなどの BindingAdapter は `TextViewBindingAdapter`  
というクラスを定義して、その中にメソッドを定義しているようである。  
自分で作成したカスタムViewの BindingAdapter も同様の命名で定義して、その内部にメソッドを  
定義するのが良さそうである。

[TextViewBindingAdapterのサンプル](https://android.googlesource.com/platform/frameworks/data-binding/+/refs/heads/studio-master-dev/extensions/baseAdapters/src/main/java/androidx/databinding/adapters/TextViewBindingAdapter.java)


### 複数のパラメータを渡したい場合

```java
@BindingAdapter({"imageUrl", "error"})
public static void loadImage(ImageView view, String url, Drawable error) {
  Picasso.get().load(url).error(error).into(view);
}
```

```xml
<ImageView app:imageUrl="@{venue.imageUrl}" app:error="@{@drawable/venueError}" />
```

上記の@BindingAdapterメソッドは、  
レイアウトxml上の `<ImageView>` で `app:imageUrl` 属性と `app:error` 属性の両方に  
データバインディング式（`@{}`）が使用されており、かつ、その属性に渡されるパラメータの型が一致する場合  
（`app:imageUrl` が `String` 型で、 `app:error` が `Drawable` 型の場合）にのみ呼び出されます。

**補足**

`@BindingAdapter` アノテーションを付与されたメソッドのメソッド名は任意である。  
そのため、上記の例では、 `loadImage()` という名前のメソッドが定義されているが、  
レイアウトxml内には、 `loadImage` という属性があるわけではない。


### requireAll属性について

複数のパラメータを渡す場合、 `requireAll` 属性を指定することができます。  
`requireAll` 属性はデフォルトでは `true` です。

`true` の場合、すべてのパラメータが存在しており、かつ、そのパラメータにデータバインディング式（`@{}`）が  
割り当てられている場合にのみ@BindingAdapterメソッドが呼び出されます。

`false` の場合、一つ以上のパラメータが存在しており、かつ、そのパラメータにデータバインディング式（`@{}`）が  
割り当てられている場合に@BindingAdapterメソッドが呼び出されます。

`false` の状態でメソッドが呼び出される場合には、存在しないパラメータ、または、  
データバインディング式でないパラメータには、（パラメータに通常の値が入っていたとしても）  
Javaのデフォルト値が渡されて、メソッドが呼び出されます。

以下はサンプルのコードです。

```java
@BindingAdapter(value={"imageUrl", "placeholder"}, requireAll=false)
public static void setImageUrl(ImageView imageView, String url, Drawable placeHolder) {
  if (url == null) {
    imageView.setImageDrawable(placeholder);
  } else {
    MyImageLoader.loadInto(imageView, url, placeholder);
  }
}
```


### 古い値を受け取りたい場合

バインディング アダプターのメソッドは、必要に応じてハンドラの古い値を取得できます。  
古い値と新しい値を取得するメソッドでは、次の例に示すように、最初に属性の古い値をすべて宣言し、  
その後で新しい値を宣言する必要があります。

```java
@BindingAdapter("android:paddingLeft")
public static void setPaddingLeft(View view, int oldPadding, int newPadding) {
  if (oldPadding != newPadding) {
      view.setPadding(newPadding,
                      view.getPaddingTop(),
                      view.getPaddingRight(),
                      view.getPaddingBottom());
   }
}
```


### イベントハンドラのサンプル

**抽象メソッドを一つのみ持っているイベントハンドラのサンプル**

```java
@BindingAdapter("android:onLayoutChange")
public static void setOnLayoutChangeListener(View view, View.OnLayoutChangeListener oldValue,
       View.OnLayoutChangeListener newValue) {
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
    if (oldValue != null) {
      view.removeOnLayoutChangeListener(oldValue);
    }
    if (newValue != null) {
      view.addOnLayoutChangeListener(newValue);
    }
  }
}
```

```xml
<!-- バインド式内はラムダ式で記載する -->
<View android:onLayoutChange="@{() -> handler.layoutChanged()}"/>
```


**複数の抽象メソッドを持っているイベントハンドラのサンプル**

リスナーに複数のメソッドがある場合、複数のリスナーに分割する必要があります。  
たとえば、 `View.OnAttachStateChangeListener` には `onViewAttachedToWindow(View)` と  
 `onViewDetachedFromWindow(View)` の 2 つのメソッドがあります。  

```java
// リスナーの分割例
@TargetApi(VERSION_CODES.HONEYCOMB_MR1)
public interface OnViewDetachedFromWindow {
  void onViewDetachedFromWindow(View v);
}

@TargetApi(VERSION_CODES.HONEYCOMB_MR1)
public interface OnViewAttachedToWindow {
  void onViewAttachedToWindow(View v);
}
```

```java
// requireAll=false に設定することで、どちらかのリスナーが未設定でも動作するようにしています。
// ListenerUtil で古いリスナーへの参照を取得できるため、 @BindingAdapter メソッドのパラメータとして受け取る必要はありません。
@BindingAdapter({"android:onViewDetachedFromWindow", "android:onViewAttachedToWindow"}, requireAll=false)
public static void setListener(View view, OnViewDetachedFromWindow detach, OnViewAttachedToWindow attach) {
    if (VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB_MR1) {
        OnAttachStateChangeListener newListener;
        if (detach == null && attach == null) {
            newListener = null;
        } else {
            newListener = new OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View v) {
                    if (attach != null) {
                        attach.onViewAttachedToWindow(v);
                    }
                }
                @Override
                public void onViewDetachedFromWindow(View v) {
                    if (detach != null) {
                        detach.onViewDetachedFromWindow(v);
                    }
                }
            };
        }

        // trackListener() メソッドは、新しいリスナーを「最新の使用中のリスナー」として登録しつつ、
        // 一つ前の古いリスナーを返します。
        OnAttachStateChangeListener oldListener = ListenerUtil.trackListener(view, newListener,
                R.id.onAttachStateChangeListener);
        if (oldListener != null) {
            view.removeOnAttachStateChangeListener(oldListener);
        }
        if (newListener != null) {
            view.addOnAttachStateChangeListener(newListener);
        }
    }
}
```


## InverseBindingAdapter

`@InverseBindingAdapter` アノテーションは、Viewの属性（Javaのフィールド）から値を取り出し、  
その値をモデルにセットする役割を持つメソッドに付与します。

`@InverseBindingAdapter` が付与されたメソッドを呼び出すタイミングは、別途、  
`@BindingAdapter` を付与したメソッドを定義することで、自分で指定します。
