<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [SharedPreference](#sharedpreference)
	- [用途](#用途)
	- [書き込み（新規、上書き）](#書き込み新規上書き)
	- [読み込み](#読み込み)
	- [削除](#削除)
	- [コミット](#コミット)
		- [apply](#apply)
		- [commit](#commit)
		- [applyとcommitの違い](#applyとcommitの違い)
	- [実ファイルの保存先、ファイル名](#実ファイルの保存先ファイル名)

<!-- /TOC -->


# SharedPreference

## 用途

頻繁に更新せず、データフォーマットがシンプルな設定データなどの保存のために使用します。

以下のような用途には**向いていません**。

- 大量のデータの保存、参照
- 頻繁なデータの保存、参照
- 高速な処理を要求し、データの一貫性の保証が不要な場合


## 書き込み（新規、上書き）

```Java
// 操作用インスタンスの取得
// 第一引数 : 保存ファイル名
// 第二引数 : アクセスレベル（MODE_PRIVATE 固定。それ以外は現在deprecated）
SharedPreferences preferences = getSharedPreferences("file_name", MODE_PRIVATE);
SharedPreferences.Editor editor = preferences.edit();

// 要素の追加・変更
// 第一引数：キー
// 第二引数：値
// 書き込み可能な型は以下の通り
editor.putInt("int", 1);
editor.putString("String", "a");
editor.putBoolean("boolean", true);
editor.putFloat("float", 1.1f);
editor.putLong("long", 1L);
Set<String> mySet = new HashSet<>();
mySet.add("a");
mySet.add("b");
mySet.add("c");
editor.putStringSet("StringSet", mySet);

// 書き込み開始
editor.apply(); // もしくは editor.commit();
```


## 読み込み

読み込みは二段階で実行される。

まず、`getSharedPreferences`などで、プリファレンスのオブジェクトを取得した時点で、xmlファイルからメモリへとデータが展開される。

その後、`getInt()`メソッド等で、実際にデータを取得する時は、メモリからデータが取得される。


```java
// 操作用インスタンスの取得
// 第一引数 : 保存ファイル名
// 第二引数 : アクセスレベル（MODE_PRIVATE 固定）
SharedPreferences preferences = getSharedPreferences("file_name", MODE_PRIVATE);

// 値の取得
// 第一引数：キー
// 第二引数：デフォルト値（ファイル名やキーが存在しないなどの理由で取得できなかった場合）
int a = preferences.getInt("int", 0);
String b = preferences.getString("String", "");
boolean c = preferences.getBoolean("boolean", false);
float d = preferences.getFloat("float", 0.1f);
long e = preferences.getLong("long", 2L);
Set<String> f = preferences.getStringSet("StringSet", new HashSet<String>());
```


## 削除

```java
// 指定した要素の削除
editor.remove("int");
editor.apply();

// 全要素の削除
editor.clear();
editor.commit(); // applyもcommitも可。違いは以下を参照。
```

`remove()` も `clear()` も、プリファレンスのキーとバリューのセット自体が削除される。  
バリューだけが削除されるのではない。


## コミット

要素の書き込み、削除の際には、applyメソッドかcommitメソッドを呼び出して、初めてファイルに反映される。


### apply

データを書き込むためのメソッドである。

スレッドセーフである。

複数のスレッドから同時にapplyが呼ばれた時は、最後にapplyを呼んだ処理の結果が書き込まれる。

ライフサイクルコンポーネントの状況を気にする必要はない。
なぜなら、ライフサイクルコンポーネントが終了する際には、全てのapplyの書き込みが完了していることを確認してから終了するようになっているためである。


### commit

データを書き込むためのメソッドである。

スレッドセーフである。

複数のスレッドから同時にcommitが呼ばれた時は、最後にcommitを呼んだ処理の結果が書き込まれる。


### applyとcommitの違い

- apply
	- 非同期で書き込む
	- メインスレッドから実行可能
	- 書き込みが成功したかどうかを返さない
- commit
	- 同期で書き込む
	- メインスレッドから実行不可
	- 書き込みが成功したかどうかをBooleanで返す（true:成功、false:失敗）

書き込み結果を必要としない場合は、applyを使用すれば問題ない。


## 実ファイルの保存先、ファイル名

**保存先**

`/data/data/[パッケージ名]/shared_prefs/`


**保存ファイル名**

```java
// Activityから取得する方法
// この場合は、file_name.xmlというファイル名になる。
SharedPreferences preferences = getSharedPreferences("file_name", MODE_PRIVATE);
```

```java
// Activityから取得する方法
// この場合は、[Activityのクラス名].xmlというファイル名になる。
SharedPreferences preferences = getPreferences(MODE_PRIVATE);
```

```java
// ActivityでもFragmentでもどこからでも取得できる方法
// PreferenceFragmentを使用して、プリファレンスのファイル名を変更せずにデータを保存した場合にはこのファイル名となる。
// この場合は、[パッケージ名]_preferences.xmlというファイル名になる。
SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(アクティビティのコンテキスト);
```

```java
// PreferenceFragmentにひもづくプリファレンスファイルは、以下の方法で取得できる。
// PreferenceFragmentは、どれか一つのプリファレンスファイルを自分に紐付けている。
SharedPreferences preferences = getPreferenceManager().getSharedPreferences();

// PreferenceFragmentにひもづくSharedPreferencesの名前を変更するには、以下のようにすればOK
public class MyPrefFragment extends PreferencFragmentCompat{
	@Override
	public void onCreatePreferences(Bundle savedInstanceState, String rootKey){
		getPreferenceManager().setSharedPreferencesName("new_name");
		setPreferencesFromResource(R.xml.xxx, rootKey);

		Preference preference = findPreference("key1");
		...
	}
}
```
