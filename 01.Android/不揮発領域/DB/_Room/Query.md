<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [Query](#query)
	- [基本形](#基本形)
	- [パラメータを渡す](#パラメータを渡す)
	- [複数のパラメータを渡す](#複数のパラメータを渡す)
	- [一つのパラメータを複数回使用する](#一つのパラメータを複数回使用する)
	- [テーブルの一部の列だけを格納するオブジェクトを作成する](#テーブルの一部の列だけを格納するオブジェクトを作成する)
	- [実行時までパラメータの数がわからないSQL](#実行時までパラメータの数がわからないsql)
	- [取得したデータをLiveData型の変数で受け取り、UI連携する](#取得したデータをlivedata型の変数で受け取りui連携する)
	- [テーブルを結合したSQL](#テーブルを結合したsql)

<!-- /TOC -->


# Query

`@Select`アノテーションは存在しないため、SELECT文を記載したい場合は、`@Query`アノテーションを使用します。

また、SELECT文以外のSQLでも、複雑なSQLを記述したい場合は、`@Query`アノテーションを使用すると便利です。


## 基本形

`@Query`アノテーションを使用すると任意のSQLを実行することができます。

```Java
// SELECT文
@Dao
public interface MyDao {
    @Query("SELECT * FROM user")
    public User[] loadAllUsers();
}

// UPDATE文
@Query("UPDATE user SET name = :name WHERE id = :id")
void update(String name, String id);

// DELETE文
@Query("DELETE FROM user WHERE id = :id")
void delete(String id);

// count
@Query("SELECT COUNT(*) FROM user WHERE name LIKE :name")
long count(String name);

// SELECT文 LIKE検索
// ※
// LIKE 検索は、「完全一致」の場合のみを抽出するため、「部分一致」検索を行いたい場合は
// 以下のように、「 % 」を指定します。
@Query("SELECT * FROM WinLose WHERE column1 LIKE ('%'||:column1Condition||'%')")
List<WinLose> searchWithOr(String column1Condition);
```


## パラメータを渡す

パラメータは名前でマッチングします。

`@Query`アノテーション内のコロンから始まる変数名とメソッドパラメータの変数名が一致するとSQLのパラメータにメソッドのパラメータが代入されます。

```Java
@Dao
public interface MyDao {
    @Query("SELECT * FROM user WHERE age > :minAge")
    public User[] loadAllUsersOlderThan(int minAge);
}
```


## 複数のパラメータを渡す

複数のパラメータを渡す方法を以下に記載します。

```Java
@Dao
public interface MyDao {
    @Query("SELECT * FROM user WHERE age BETWEEN :minAge AND :maxAge")
    public User[] loadAllUsersBetweenAges(int minAge, int maxAge);
}
```


## 一つのパラメータを複数回使用する

一つのメソッドパラメータを一つのクエリの中で複数回使用することができます。

```Java
@Dao
public interface MyDao {
    @Query("SELECT * FROM user WHERE first_name LIKE :search " +
           "OR last_name LIKE :search")
    // 返し値の型は、ArrayListにすることはできない。ListならOK。
    public List<User> findUserWithName(String search);
}
```


## テーブルの一部の列だけを格納するオブジェクトを作成する

`@Entity`アノテーションでテーブルと同じ列を持つオブジェクトを作成することができますが、ある処理では、全ての列が必要ない場合があります。

そんな時は、必要な列だけを格納するオブジェクトを使用して、リソースを節約することができます。

```Java
// Tupleはレコードと同じ意味
// @Entityアノテーションは不要
public class NameTuple {
    @ColumnInfo(name = "first_name")
    public String firstName;

    @ColumnInfo(name = "last_name")
    @NonNull
    public String lastName;
}
```

```Java
@Dao
public interface MyDao {
    @Query("SELECT first_name, last_name FROM user")
    public List<NameTuple> loadFullName();
}
```


## 実行時までパラメータの数がわからないSQL

`IN`句のパラメータなどは、実行時までパラメータの数がわかりません。その場合は、メソッドのパラメータの型をコレクションにすることでマシンはパラメータの数が可変であることを認識します。

```Java
@Dao
public interface MyDao {
    @Query("SELECT first_name, last_name FROM user WHERE region IN (:regions)")
    public List<NameTuple> loadUsersFromRegions(List<String> regions);
}
```


## 取得したデータをLiveData型の変数で受け取り、UI連携する

```Java
@Dao
public interface MyDao {
    @Query("SELECT first_name, last_name FROM user WHERE region IN (:regions)")
    public LiveData<List<User>> loadUsersFromRegionsSync(List<String> regions);
}
```

- 自動的にワーカースレッドでSQLが実行される。
	- メソッドの返値がLiveData型の場合は、自動的にワーカースレッドでSQLが実行される。
	- それ以外の場合は、メインスレッドで実行される。
- MutableLiveData型にすることはできない。
	- メソッドの返値はMutableLiveData型にすることはできない。LiveData型である必要がある。
	- MutableLiveData型にするとコンパイルエラーとなる。


## テーブルを結合したSQL

```Java
@Dao
public interface MyDao {
   @Query("SELECT * FROM book " +
           "INNER JOIN loan ON loan.book_id = book.id " +
           "INNER JOIN user ON user.id = loan.user_id " +
           "WHERE user.name LIKE :userName")
   public List<Book> findBooksBorrowedByNameSync(String userName);
}
```

以下のように返値の型をPOJOで返すこともできます。

```Java
@Dao
public interface MyDao {
   @Query("SELECT user.name AS userName, pet.name AS petName " +
          "FROM user, pet " +
          "WHERE user.id = pet.user_id")
   public LiveData<List<UserPet>> loadUserAndPetNames();

   // Daoクラスの外部に個別のクラスとしてUserPetクラスを定義することもできます。
   // その場合は、publicなクラスとしてください。
   static class UserPet {
       public String userName;
       public String petName;
   }
}
```


## Not sure how to convert a Cursor to this method's return type

Select文の結果の型が、 `ArrayList<>` などの具体的なクラス名の場合に発生するエラーである。  
`List<>` などの `abstract` なクラスを指定する必要がある。


## WHERE 句に Boolean 型の条件を指定する場合

Room で、 `Boolean` 型のデータを保存すると、数値として保存されます。  
`true` は `1` 、 `false` は `0` の値がテーブルに保存されます。

そのため、 `@Query` アノテーションで `SELECT` 文を記述する際に、  
`WHERE` 句で `Boolean` 型のパラメータを指定する場合には、  
`true` なら `1` 、 `false` なら `0` を指定します。

```Java
@Query("SELECT * FROM TableName WHERE booleanColumn = 1")
List<TableName> loadTrueDataOnly();
```
