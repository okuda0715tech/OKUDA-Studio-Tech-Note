<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [Update](#update)
	- [@Update と @Insert の違い](#update-と-insert-の違い)
	- [コード](#コード)
	- [テーブル内の特定の列だけを UPDATE する。](#テーブル内の特定の列だけを-update-する)
		- [方法 1 : 更新対象の列だけを抜き出したクラス (POJO) を作成する方法](#方法-1--更新対象の列だけを抜き出したクラス-pojo-を作成する方法)
		- [方法 2 : @Query アノテーションで UPDATE 文を記述する方法](#方法-2--query-アノテーションで-update-文を記述する方法)
<!-- TOC END -->


# Update

## @Update と @Insert の違い

`@Update` は、キーが一致するレコードがテーブルに存在しない場合は、 Insert を行わない。  
`@Insert` は、キーが一致するレコードがテーブルに存在しない場合は、 Insert を行う。


## コード

**AppDatabase.java**

```java
@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}
```


**UserDao.java**

```java
@Dao
public interface UserDao {
    // 主キーがマッチするレコードを更新します。

    @Update
    void updateOne(User users);

    // リターンのintは更新したレコード件数を返します。
    @Update
    int updateOneAndReturn(User users);

    @Update
    void updateArray(User... users);

    @Update
    int updateArrayAndReturn(User... users);

    @Update
    void updateList(List<User> users);

    @Update
    int updateListAndReturn(List<User> users);
}
```


**User.java**

```java
@Entity
public class User {
    @PrimaryKey
    public int uid;

    @ColumnInfo(name = "first_name")
    public String firstName;

    @ColumnInfo(name = "last_name")
    public String lastName;
}
```


## テーブル内の特定の列だけを UPDATE する。

テーブル内の特定の列だけを UPDATE する方法は 2 つあります。  
更新対象の列がたくさんある場合は、方法 1 の方が簡潔に実装することができます。  
更新対象の列が少ない場合は、方法 2 の方が簡潔に実装することができます。


### 方法 1 : 更新対象の列だけを抜き出したクラス (POJO) を作成する方法

**テーブルの本体**

```java
// テーブル本体を表すクラスは何も変更する必要はありません。
@Entity
public class WholeTable {

    @PrimaryKey
    public Integer id;

    @ColumnInfo(name = "score")
    public Integer score;

    @ColumnInfo(name = "firstName")
    public String firstName;

}
```

**更新したい列のみのクラス (POJO)**

```java
// アノテーション @Entity や @ColumnInfo などは全く必要ありません。
// 更新するレコードを特定するために、プライマリーキーを保持します。
// プライマリーキー以外は、更新したい列情報のみ保持します。
public class PartialTable {

    public Integer id;

    public Integer score;

}
```

**MyTableNameDao.java**

```java
@Dao
public interface WholeTableDao {

    @Update(entity = WholeTable.class) // <--- テーブル全体を表すクラス
    void update(PartialTable partialTable); // <--- 更新したい列のみを表すクラス

}
```


### 方法 2 : @Query アノテーションで UPDATE 文を記述する方法

```java
@Query("UPDATE TableName SET score = :score WHERE id = :id")
int updateScore(Integer id, Integer score);
```
