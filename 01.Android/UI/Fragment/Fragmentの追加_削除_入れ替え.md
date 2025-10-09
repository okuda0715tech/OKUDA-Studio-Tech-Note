<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [Fragmentの追加_削除_入れ替え](#fragmentの追加_削除_入れ替え)
	- [追加](#追加)
	- [削除](#削除)
	- [入れ替え](#入れ替え)
	- [Fragmentの保存と復元](#fragmentの保存と復元)
		- [保存](#保存)
		- [復元](#復元)
	- [バックスタックに積まれたトランザクションの数を取得](#バックスタックに積まれたトランザクションの数を取得)
<!-- TOC END -->


# Fragmentの追加_削除_入れ替え

## 追加

フラグメントを追加するには `FragmentTransaction.add()` メソッドを使用します。  
add できるフラグメントの数は一つだけです。  

複数のフラグメントを重ねたい時は、フラグメントの入れ子を作成します。  
入れ子を作るためには、フラグメント内で `getChildFragmentManager()` で  
`FragmentTransaction` を取得し、それの `add` メソッドを使用して、フラグメントを追加します。

引数は以下の通りです。

```Java
public final @NonNull FragmentTransaction add(
  // フラグメントを設置するコンテナビューのリソース ID
  @IdRes int containerViewId,
  // 設置したいフラグメントの Class クラス
  @NonNull Class<Fragment> fragmentClass,
  // フラグメントにセットされるパラメータ
  @Nullable Bundle args,
  // 後から第二引数で渡したフラグメントのインスタンスを取得する場合に必要なタグ
  @Nullable String tag
)
```


## 削除

`Fragment` を削除するには `FragmentTransaction.remove()` メソッドを使用します。

引数は以下の通りです。

```Java
public @NonNull FragmentTransaction remove(
  // 取り除きたいフラグメント
  @NonNull Fragment fragment
)
```


## 入れ替え

`Fragment` を入れ替えるには `FragmentTransaction.replace()` メソッドを使用します。

入れ替えを実行するとそれまでコンテナに格納されていた `Fragment` が取り除かれて、  
新たな `Fragment` が追加されます。

引数は以下の通りです。

```Java
public final @NonNull FragmentTransaction replace(
  // フラグメントを設置するコンテナビューのリソース ID
  @IdRes int containerViewId,
  // 設置したいフラグメントの Class クラス
  @NonNull Class<Fragment> fragmentClass,
  // フラグメントにセットされるパラメータ
  @Nullable Bundle args,
  // 後から第二引数で渡したフラグメントのインスタンスを取得する場合に必要なタグ
  @Nullable String tag
)
```


## Fragmentの保存と復元

- Fragmentの追加・削除・入れ替えをおこなった後、端末のバックキーで元の状態に戻すことができます。
- 元の状態に戻すには`FragmentTransaction.addToBackStack`メソッドを使用して、変更を保存しておきます。
- 保存するのは、変更そのものであり、復元する際には、変更内容を逆から復元することで、状態を復元します。


### 保存

transactionを開始してからcommitするまでの処理内容を保存します。

`addToBackStack`メソッドは、transactionを開始してから全ての操作を終えた後（commitする直前で）、呼び出します。


**MainActivity.java**

```Java
FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
transaction.replace(R.id.activity_container, SecondFragment.getInstance(), "SECOND_FRAGMENT");
// 引数は、保存する状態に対するキーとして使用します。
// 保存する状態が一つだけの場合は引数にnullを指定することも可能です。
transaction.addToBackStack("REMOVE_FIRST_ADD_SECOND");
transaction.commit();
```


### 復元

`addToBackStack`で変更内容を保存した場合、何も実装しなくてもバックキーで1つ前のフラグメントに戻ることができます。

任意のイベントに戻る動作を実装したい場合は、`FragmentTransaction.popBackStack`メソッドを使用します。


**MainActivity.java**

```java
FragmentManager fm = getSupportFragmentManager();
// 引数は、保存時に指定したキーを指定します。
fm.popBackStack("REMOVE_FIRST_ADD_SECOND");
```


#### 指定したFragmentまで戻る場合

```java
// トランザクション１
FragmentManager fm = getSupportFragmentManager();
FragmentTransaction transaction = fm.beginTransaction();
transaction.add(R.id.contaier, FirstFragment.newInstance(), FirstFragment.TAG);
transaction.addToBackStack("ADD_FIRST");
transaction.commit();

// トランザクション２
FragmentTransaction transaction = fm.beginTransaction();
transaction.replace(R.id.contaier, SecondFragment.newInstance(), SecondFragment.TAG);
transaction.addToBackStack("REPLACE_SECOND");
transaction.commit();

// トランザクション３
FragmentTransaction transaction = fm.beginTransaction();
transaction.add(R.id.contaier, ThirdFragment.newInstance(), ThirdFragment.TAG);
transaction.addToBackStack("REPLACE_THIRD");
transaction.commit();
```

```java
FragmentManager fm = getSupportFragmentManager();

// 方法１　（addToBackStackのタグで戻す位置を指定）
// 指定したトランザクションのコミット後の状態に戻す
fm.popBackStack("REPLACE_SECOND", 0);
// 指定したトランザクションのコミット前の状態に戻す
fm.popBackStack("REPLACE_SECOND", FragmentManager.POP_BACK_STACK_INCLUSIVE);

// 方法２　（トランザクションの要素番号で戻す位置を指定。バックスタックを配列のように考え、一番下を要素をindex=0とする考え方。）
// バックスタックに積まれたトランザクションから指定した要素番号のトランザクションを取得する。
FragmentManager.BackStackEntry entry = fm.getBackStackEntryAt(1);
// コミット後の状態に戻す
FragmentManager.popBackStack(entry.getId(), 0);
// コミット前の状態に戻す
FragmentManager.popBackStack(entry.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
```


## バックスタックに積まれたトランザクションの数を取得

```java
FragmentManager fm = getSupportFragmentManager();
int size = fm.getBackStackEntryCount();
```
