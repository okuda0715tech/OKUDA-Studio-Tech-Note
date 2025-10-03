<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [Delete](#delete)
	- [コード](#コード)

<!-- /TOC -->


# Delete

## コード

**AppDatabase.java**

```java
@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}
```


**UserDao.java**

`@Delete` アノテーションを付与したメソッドは、引数に与えられたオブジェクトと  
主キーがマッチするレコードを削除します。

```java
@Dao
public interface UserDao {
    @Delete
    void deleteOne(User users);

    // リターンのintは削除したレコード件数を返します。
    @Delete
    int deleteOneAndReturn(User users);

    @Delete
    void deleteArray(User... users);

    @Delete
    int deleteArrayAndReturn(User... users);

    @Delete
    void deleteList(List<User> users);

    @Delete
    int deleteListAndReturn(List<User> users);
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
