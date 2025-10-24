<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [ID](#id)
	- [`view.setId()/getId()`と`android:id="xxx"`の違い](#viewsetidgetidandroididxxx違)
	- [動的にViewを生成する場合は、どのようにIDを生成すればよいのか](#動的view生成場合id生成)
	- [使い方によっては、IDにViewの型情報をセットして、型の識別に使用することもできる。](#使方idview型情報型識別使用)

<!-- /TOC -->


# ID

## `view.setId()/getId()`と`android:id="xxx"`の違い

- どちらも同じオブジェクト（ViewクラスのmIdフィールド）を参照している。
- ID自体は「アプリ内でユニークにしなければいけない」という制約はない
- ただし、そのIDをRクラスにも登録する場合は、「Rクラス内ではユニークにしなければいけない」という制約が存在しているはず。
- `findViewById(xxx)`などでViewを取得する場合は、同一IDのViewが複数存在していると取得したいViewとは別のViewが取得される恐れがあるため、アプリ内でユニークなIDにしなければいけない。（そのため、IDをRクラスで管理する）


## 動的にViewを生成する場合は、どのようにIDを生成すればよいのか

### そもそもIDを生成する必要はないはず

動的に生成したViewは、そのViewへの参照をホストのActivity/Fragmentがフィールドに保持しているはず。  
そのViewへアクセスする場合は、そのフィールドを参照すれば良いため、わざわざIDからViewを取り出す必要はないはず。

そもそもIDは、レイアウトXMLで生成したViewへの参照をホストのActivity/Fragmentが取得するために使用するものであるという前提を理解していれば、上記の理屈がわかるはず。


### どうしてもやりたい場合は以下のようにする

リストのアイテムなど、動的にViewを生成する場合は、`view.setId()/getId()`を使用して、IDのセットとゲットを行うことになると思う。

IDの生成は、アプリ内でユニークなIDにしたい場合は`view.generateViewId()`メソッドで取得する。この方法だとRクラス内に登録される。  
ユニークにする必要がない場合は、自分で好きな値をセットする。  
ただし、`setId()`に渡すパラメータは`@IdRes`というアノテーションでRクラスへの登録をチェックしているため、  
単にint型の変数を生成してパラメータに渡すとコンパイルエラーとなる。

Rクラスへ登録されたint型を生成するには、例えばxmlファイルで以下のようにすれば良い。

```xml
<!-- res/values/ids.xml(ファイル名は任意。慣例としは、タグ名の複数形だが、idの場合はitemsよりidsの方が良さそう) -->
<?xml version="1.0" encoding="utf-8"?>
<resources>
  <item type="id" name="xxxxx" />
</resources>
<!-- R.id.xxxxxで参照できる -->
```

```Java
textView = new TextView(this);
textView.setId(10); // これはエラーとなる。
textView.setId(R.id.xxxxx); // これは問題なし。
```


## 使い方によっては、IDにViewの型情報をセットして、型の識別に使用することもできる。が、やってはいけない。

例えば、あるViewGroupにTextViewやButtonなど複数の型のViewが格納されているとします。  
そして、その中からTextViewのものだけを取り出したいとします。
その場合、findViewByIdなどでViewGroupを取得してから、for文とview.getChildAt(index)で子Viewを順番に取り出します。  
取り出したViewのview.getId()メソッドでIDを取り出すとします。  
あらかじめ、View生成時に、IDにR.id.text_viewなど、型を区別できる情報を詰め込んでおけば、それがTextViewなのかどうかを判定することができ、結果として、TextViewのみを取り出すことができます。

**やってはいけない理由**

1. そもそもフィールド名がIDなのに型情報を入れるとバグの原因になりうる。
2. Javaの型と異なり、誤って使用される危険がある
   1. 上記の例の場合、TextViewのみにR.id.text_viewをセットする必要があるが、Buttonの実装者が気が付かないと知らないうちにセット処理を通過してしまい、ButtonにもR.id.text_viewがセットされる恐れがある。

例のようなことをやりたい場合、`view.setTag(@ResId int id, Object Object)`を使用すること。  
そして、誤ってセット処理を通過しないように、インスタンス生成メソッドを用意して、その中でTagをセットすること。


## JavaコードでIDリソースを取得する方法

```Java
// これでID（自動的に生成された数値）が取得できる

// リソースから取得する場合はこう
R.id.xxx

// Viewから取得する場合はこう
view.getId();

// 上記でイコール検査が可能
if(R.id.xxx == view.getId()){
	...
}
```
