- [キーボード表示時にViewが隠れないようにする](#キーボード表示時にviewが隠れないようにする)
  - [基本的には、Viewがキーボードで隠れないようになっている](#基本的にはviewがキーボードで隠れないようになっている)
  - [windowSoftInputMode 属性](#windowsoftinputmode-属性)
  - [stateXXX と adjustXXX](#statexxx-と-adjustxxx)
  - [adjustXXX は、キーボード表示時の画面の伸縮有無の指定](#adjustxxx-はキーボード表示時の画面の伸縮有無の指定)
    - [adjustResize](#adjustresize)
    - [adjustPan](#adjustpan)
    - [adjustUnspecified](#adjustunspecified)
  - [stateXXX は、キーボードの表示/非表示の指定](#statexxx-はキーボードの表示非表示の指定)
    - [まとめの表](#まとめの表)


# キーボード表示時にViewが隠れないようにする

## 基本的には、Viewがキーボードで隠れないようになっている

基本的には、タップされた EditText は、キーボードで隠れないようになっています。  
なぜなら、フォーカスを得た EditText が隠れないように、自動的に画面がスクロール or パンされるためです。  
パンについては [後述](#adjustPan) します。

ただし、スクロールさせるためには、 ScrollView などのスクロール可能な View を実装している必要があります。

```
(補足)
スクロール可能な View を実装していない場合は、パンされるため、フォーカスを得ていない View は、  
その時点で、画面からはみ出していれば、キーボードを非表示にするまで表示できなくなります。
```

そして、 ScrollView を実装していたとしても、実装方法によっては、  
View が隠れてしまう可能性や、 View のレイアウトが崩れてしまう可能性を秘めています。

以下に示す `windowSoftInputMode` 属性について、特に `adjustResize` 指定について、  
正しく理解することで、レイアウトの崩れや、 View が隠れてしまうことを防ぐことができます。

一言でいうと、 `adjustResize` を指定した場合は、キーボードが表示された場合に、  
Activity のウィンドウサイズが、キーボードで覆い隠されていない部分の大きさに縮小されます。


## windowSoftInputMode 属性

キーボード表示時にViewが隠れないようにするには、  
`Manifest.xml` ファイルの `<activity>` タグに `android:windowSoftInputMode` 属性を定義します。

```xml
<activity android:windowSoftInputMode="stateVisible|adjustResize" />
```


## stateXXX と adjustXXX

`windowSoftInputMode` 属性には、 `stateXXX` と `adjustXXX` のどちらか一つ、  
または、 `stateXXX` と `adjustXXX` を一つずつ指定することができます。


## adjustXXX は、キーボード表示時の画面の伸縮有無の指定

`adjustXXX` 系には、 `adjustResize` 、 `adjustPan` 、 `adjustUnspecified` の三種類が存在します。


### adjustResize

キーボードの表示/非表示の切り替え時に、 Activity のメインウィンドウのサイズを変更します。  
つまり、キーボードが表示されると、 Activity の描画領域が小さくなり、  
逆に、キーボードが閉じられると、 Activity の描画領域が大きくなります。

`adjustResize` は `ScrollView` などのスクロール可能な View と合わせて使用します。

ユーザーは、キーボード表示中であっても、スクロールすることで、
画面上のすべてのコンテンツを見ることが出来ます。


### adjustPan

```
通常、 adjustPan の使用は推奨されません。
adjustPan を使用した場合、キーボードを閉じなければ、
画面上の見えていない View にアクセスすることができないためです。

スクロール可能な View と合わせて使用したとしても、どうしても見えない領域が出てきてしまいます。
なぜなら、 Activity のウィンドウの一部が、画面外に確保されてしまっているためです。
```

**パン ( Pan )** とは、写真の専門用語で、「カメラの首を振って、レンズに収まる領域を変更する」という意味があります。

アプリ開発においては、 Activity のメインウィンドウのサイズを変更せずに、  
Activity のメインウィンドウが、画面の枠外に、はみ出すことを示します。


### adjustUnspecified

`windowSoftInputMode` 属性に、 `adjustXXX` 系の値が指定されていない場合のデフォルト値は、  
`adjustUnspecified` になります。

ウィンドウのコンテンツをスクロールできるビューがあるかどうかに応じて、  
システムが自動的に `adjustResize` もしくは `adjustPan` を選択します。

スクロール可能なビューがある場合は、 `adjustResize` が選択されます。


## stateXXX は、キーボードの表示/非表示の指定

詳しい内容は、このサイト ( [android:windowSoftInputMode](https://developer.android.com/guide/topics/manifest/activity-element?hl=ja#wsoft) ) に書かれているが、ちょっとよくわからないので、  
自分で以下に記載している「まとめの表」を見たほうが良い。


### まとめの表

**【前提条件】**

**1.キーボードが表示されるためには、 EditText にフォーカスが当たっていること。**  
フォーカスが当たっていないと、たとえ `stateAlwaysVisible` が設定されていたとしても、  
キーボードは表示されません。

フォーカスをあてるには、 `onResume()` メソッド内などで、 `editText.requestFocus()` を実行します。

**2.キーボードが非表示の場合でもタップすれば表示される**  
以下の表で、 「非表示」 と書かれている場合でも、 `EditText` をタップすれば、キーボードは表示されます。

**表：Visible 系の場合**

| -                      | stateVisible            | stateAlwaysVisible |
| ---------------------- | ----------------------- | ------------------ |
| **アプリ起動時**       | 表示                    | 表示               |
| **画面生成時**         | 表示                    | 表示               |
| **次画面からの戻り時** | 次画面の状態と同じ (※1) | 表示               |
| **バックフォア**       | 表示                    | 表示               |

**表：Hidden 系の場合**

| -                      | stateHidden             | stateAlwaysHidden |
| ---------------------- | ----------------------- | ----------------- |
| **アプリ起動時**       | 非表示                  | 非表示            |
| **画面生成時**         | 非表示                  | 非表示            |
| **次画面からの戻り時** | 次画面の状態と同じ (※1) | 非表示            |
| **バックフォア**       | 非表示                  | 非表示            |

(※1)  
次画面でキーボードが表示された状態で、自分の画面に戻ったときは、自分の画面でもキーボードが表示される。  
次画面でキーボードが閉じられた状態で、自分の画面に戻ったときは、自分の画面でもキーボードが閉じられた状態となる。


上記のパラメータ以外にも `stateUnspecified` と `stateUnchanged` が存在するが、調べてはいない。  
時間があれば、 `stateUnchanged` くらいは調べてもいいと思う。



