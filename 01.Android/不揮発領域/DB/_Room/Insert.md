<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [Insert](#insert)
	- [コードと解説](#コードと解説)
<!-- TOC END -->


# Insert

## コードと解説

**AppDatabase.java**

```Java
@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}
```


**AppDatabaseSingleton.java**

```java
public class AppDatabaseSingleton {

    private static AppDatabase database;

    private AppDatabaseSingleton() {
        // do nothing.
    }

    public static AppDatabase getInstance(Context context){
        if(database == null){
            database = Room.databaseBuilder(context,
                    AppDatabase.class, "database-name").build();
        }
        return database;
    }
}
```


**MyViewModel.java**

```java
public class MyViewModel extends AndroidViewModel {

    private AppDatabase db;

    public MyViewModel(@NonNull Application application) {
        super(application);
        db = AppDatabaseSingleton.getInstance(application);
    }
}
```


**UserDao.java**

```Java
@Dao
public interface UserDao {
    // @Insertアノテーションをつけます。
    // メソッド名は自由です。
    // 引数は、単一のオブジェクト、配列、Listが指定可能です。

    @Insert
    void insertOne(User users);

    // 返値としてlong型でrowIdを受け取ることができる。
    // rowIdはAutoIncrementみたいな非表示の列。詳細はSqliteのメモを参照。
    @Insert
    long insertOne(User users);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(User... users);

    // 返値としてlong型の配列でrowIdを受け取ることができる。
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(User... users);

    @Insert
    void insertAll(List<User> users);

    // 返値としてLong型のListでrowIdを受け取ることができる。
    @Insert
    List<Long> insertAll(List<User> users);
}
```

- onConflictのオプションについて
  - ABORT もしくは 未指定
    - トランザクションを中止し、ロールバックする。
  - FAIL
    - deprecated
  - IGNORE
    - 重複するデータは更新されません。
    - トランザクションは（エラー行を無視しているため）エラーにならず続行します。
    - そのため、複数行のインサートの場合は、重複していない行はインサートに成功します。
    - IGNOREの場合でも、外部キー制約に違反した場合は、ABORTのルールで処理します。
  - REPLACE
    - 古いデータは新しいデータで更新される。
  - ROLLBACK
    - deprecated

- トランザクション
	- パラメータが一つの場合でも複数の場合でも、`@Insert`アノテーション一つにつき、一つのトランザクションが形成され、実行される。


**User.java**

```Java
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
