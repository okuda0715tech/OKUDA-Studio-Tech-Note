<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [LiveData連携](#livedata連携)
	- [概要](#概要)
	- [非同期で実行されるのはどこまでか](#非同期で実行されるのはどこまでか)
	- [Room で取得した LiveData を `getValue()` した時に null が返ってくる場合](#room-で取得した-livedata-を-getvalue-した時に-null-が返ってくる場合)
	- [SELCTC 文の結果を LiveData 型で返していいものといけないもの](#selctc-文の結果を-livedata-型で返していいものといけないもの)
<!-- TOC END -->


# LiveData連携

## 概要

```Java
@Dao
public interface MyDao {
    @Query("SELECT first_name, last_name FROM user WHERE region IN (:regions)")
    public LiveData<List<User>> loadUsersFromRegionsSync(List<String> regions);
}
```

- データベースのデータが更新されると、その都度、`Observer`の`onChanged`メソッドが呼び出されるため、`onChanged`メソッド内で、画面を更新する処理を行えば、**画面に表示されるデータは常に最新のデータベースの内容になります。**
	- データ更新後にSELECT文で最新データを取得し直す必要はありません。（`onChanged`メソッドで自動的に渡されるため）
- 自動的にワーカースレッドでSQLが実行される。
	- **メソッドの返値がLiveData型の場合は、自動的にワーカースレッドでSQLが実行される。**
	- それ以外の場合は、メインスレッドで実行される。
- MutableLiveData型にすることはできない。
	- メソッドの返値はMutableLiveData型にすることはできない。LiveData型である必要がある。
	- MutableLiveData型にするとコンパイルエラーとなる。


## 非同期で実行されるのはどこまでか

非同期で実行されるのは、DaoのSELECT文を実装したメソッドの呼び出し行の一行のみである。  
そのため、SELECT文の呼び出しの直後にLiveDataの値を取得していると思って、実装すると、  
実際には取得が完了しておらず、エラーとなってしまう。  
そのため、取得後に行いたい処理は、observeメソッドなど、LiveDataの値の変更を検知する仕組みを  
利用して実装する必要がある。


ポイント１：取得したデータは `ViewModel` 内の `LiveData` フィールドで受け取ること。

```Java
public class MyViewModel{
	LiveData<String> myLiveData;

	public void myMethod(){
		myLiveData = myDb.MyDao.getLiveData();	// 非同期で実行されるのはこの一行のみ。後続処理は同期で実行される。
	}
}
```


ポイント２： `LiveData` の更新を検知する仕組みを利用して、後続処理を実施すること。

例えば、 Activity / Fragment 内の observe メソッドや、データバインディング済みの  
レイアウトxml内で後続処理を実施すること

```Java
public class MyActivity {
	viewModel.myLiveData.observe(this, new Observer<String>() {
		void onChanged (String data) {
			// do something.
		}
	});
}
```


ポイント３：SQLの返値として `LiveData` 型を指定する。

```Java
public class MyDao{
	@Query(SELECT ...)
	LiveData<List<String>> getLiveData(); // この一行のみが非同期で実行される。
}
```


## Room で取得した LiveData を `getValue()` した時に null が返ってくる場合

Roomで取得したLiveDataは、Activity/Fragmentでobserve()メソッドを呼んであげないと、  
LiveDataをgetValue()してもnullが返ってくる。

要点をまとめると、以下の例の通りです。

（例）

```Java
public class MyActivity{

	void onCreate(){
		viewModel.getMyLiveData() // NG
		viewModel.getMyLiveData().observe(this, ...) // OK
	}
}
```

```Java
public class MyViewModel{
	LiveData<List<String>> myLiveData;

	LiveData<String> getMyLiveData(){
		if(myLiveData == null){
			myLiveData = mDatabase.myDao.getAll();
		}
		return myLiveData;
	}

	void someMethod(){
		// MyActivity内でNGのようにobserve()メソッドを実装していない場合、ここのgetValue()はnullを返します。
		myLiveData.getValue();
	}
}
```

```Java
public class MyDao{
	@Query(SELECT * FROM ...)
	LiveData<List<String>> getAll();
}
```


## SELCTC 文の結果を LiveData 型で返していいものといけないもの

- いいもの
  - 参照のみの画面が保持するデータ


- いけないもの
	- 更新が可能な画面が保持するデータ


- 理由
	- DB の値の更新を監視し、リアルタイムで画面に反映する一方通行の行為である。
	- 画面の内容を DB にリアルタイムで反映する逆方向の機能ではない。
	- Select メソッドの返値には、 `MutableLiveData` 型が定義できないため、そのデータは更新できないため。
