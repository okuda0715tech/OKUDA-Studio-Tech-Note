<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [Fragmentの利用](#fragmentの利用)
	- [画面回転対応パート1(Fragmentの再生成時にパラメータをきちんと渡す)](#画面回転対応パート1fragmentの再生成時にパラメータをきちんと渡す)
		- [Fragmentの生成メソッドを用意するとよりシンプルに実装ができる(newInstanceメソッドの使用)](#fragmentの生成メソッドを用意するとよりシンプルに実装ができるnewinstanceメソッドの使用)
		- [引数ありのコンストラクタは使用せず、Bundle経由でパラメータを渡す。](#引数ありのコンストラクタは使用せずbundle経由でパラメータを渡す)
			- [サンプルコード](#サンプルコード)
	- [画面回転対応パート2(状態の保存と復元)](#画面回転対応パート2状態の保存と復元)
		- [Fragmentの途中の状態を保存するには、onSaveInstanceState内で状態を保存する](#fragmentの途中の状態を保存するにはonsaveinstancestate内で状態を保存する)
		- [getIntentの呼び出し方（FragmentからActivityのメソッドを呼び出す方法）](#getintentの呼び出し方fragmentからactivityのメソッドを呼び出す方法)
		- [注意点](#注意点)
	- [FragmentManagerで管理しているリストの取得](#fragmentmanagerで管理しているリストの取得)

<!-- /TOC -->


# Fragmentの利用

## 画面回転対応パート1(Fragmentの再生成時にパラメータをきちんと渡す)

### Fragmentの生成メソッドを用意するとよりシンプルに実装ができる(newInstanceメソッドの使用)

上記のサンプルコードは、Activity側でパラメータの格納処理を実施していた。
しかし、パラメータの格納処理は、5stepほどあり、複数箇所から同一のFragmentを生成する際には、実装漏れが発生しやすくなる。
そのため、Fragmentクラス内にFragmentインスタンスの生成とパラメータの格納を行う処理を実装し、Activity側では、その処理(メソッド)を呼び出すだけにすると処理を共通化することができる。


### 引数ありのコンストラクタは使用せず、Bundle経由でパラメータを渡す。

**Fragmentの生成手順**

1. Fragment生成用の`static`メソッド`(newInstanceなど)`を用意する。
2. そのメソッド内でBundleを生成する。
3. Bundleにパラメータをセットする。
4. Fragmentを生成し、そのFragmentにBundleをセットする。
5. Fragmentの`onCreate()`メソッドでBundleから値を取り出して、フィールドにセットする。

**なぜこうする必要があるのか**

画面回転などによるFragmentの再生成はOS起因のため、引数なしのコンストラクタで初期化される。
したがって、引数ありのコンストラクタで再生成することができないため。

一方、初回Fragment生成時にBundle経由でパラメータを渡した場合、Fragmentの再生成時に呼ばれる`onCreate()`メソッドでBundleからパラメータを取り出してフィールドにセットすることで、引数ありのコンストラクタを呼び出すのと同等の処理をすることができる。


#### サンプルコード

**MyDialogFragment.java**

```java
public class MyDialogFragment extends Fragment{
    /**
     * Fragmentのインスタンス生成とパラメータのセットを行います。
     */
    public static final MyDialogFragment newInstance(int flags) {
        Bundle args = new Bundle();
        args.putInt("FLAGS", flags);// 渡すデータの型によってメソッド名が変わります。
        MyDialogFragment fragment = new MyDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Bundleからパラメータを取り出し、フィールドにセットします。
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
				// onCreateのパラメータのBundleではなく、getArgumentsで取得したBundleから取り出すので、注意が必要です。
				// パラメータのsavedInstanceStateは、onSaveInstanceStateメソッドで保存したデータのみが入っています。
				// 一方、getArgumentsで取得したBundleは、Fragmentの親Activity側でセットしたデータのみが入っています。
        Bundle bundle = getArguments();
        Person person = (Person)bundle.getSerializable("PERSON");
    }
}
```

- Bundleにオブジェクトを渡したい場合は「putSerializable」を使用します。
- その際は、そのクラスが「Serializable」マーカーインターフェースを実装している必要があります。
- なお、List<E>とMap<K,V>はSerialableでありません。それらを保存したい場合には、代わりにArrayList<E>か、もしくはHashMap<K,V>を使用します。
- 一度シリアライズしたオブジェクトは、元のメモリとは別のメモリアドレスに復元されるため、元のオブジェクトを参照している変数と新しいオブジェクトを参照している変数のアドレスはイコールにはなりません。


## 画面回転対応パート2(状態の保存と復元)

### Fragmentの途中の状態を保存するには、onSaveInstanceState内で状態を保存する

ここで紹介するonSaveInstanceStateメソッドは、Fragmentが破棄されるタイミングで状態を保存する為、Fragmentでなんらかの処理をした最後の状態を保存したい場合に使用します。

**StoreSample.java**

```Java
@Override
public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean("isLogin", isLogin );
}
```

**RestoreSample.java**

```Java
@Override
public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// 再生成時にはsavedInstanceStateがnullではない
		if (savedInstanceState != null) {
				isLogin = savedInstanceState.getBoolean("isLogin");
		}
}
```

Fragmentの場合は`onViewCreated()`メソッド内で復元すること。

DialogFragmentの場合は`onCreateDialog()`メソッド内で復元すること。

### getIntentの呼び出し方（FragmentからActivityのメソッドを呼び出す方法）

Fragmentから`getActivity()`メソッドを呼び出すと、そのFragmentのホストになっているActivityが取得できる。そのメソッドを使えば、本来Activityでしか呼び出せないメソッドをFragmentから呼び出すことができる。

例えば、`getIntent()`を呼び出したい場合は以下のようにする。

```Java
getActivity().getIntent()
```


### 注意点

DialogFragmetの表示処理(初期化を含む)をActivityのOnCreate()に記載していたのですが、
回転処理で異常終了が発生する不具合がありました。

DialogFragmetの実装クラスがインナークラスとして宣言されている場合、
Publicな標準コンストラクタが呼び出せず例外が発生するようです。
そのためDialogFragmetは、Publicなクラスとして使用するのが一般的なようです。


## FragmentManagerで管理しているリストの取得

**v4.app.FragmentManager.java**

```java
List<Fragment> getFragments()
```

**MainActivity.java**

```Java
List<Fragment> fragments = getSupportFragmentManager().getFragments();
```


