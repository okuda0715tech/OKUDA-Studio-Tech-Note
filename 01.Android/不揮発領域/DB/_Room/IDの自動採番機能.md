- [IDの自動採番機能](#idの自動採番機能)
  - [エンティティクラスの定義](#エンティティクラスの定義)
  - [使用例](#使用例)


# IDの自動採番機能

Android の Room ライブラリでは、プライマリキーの ID を自動的に採番する機能があります。それを実現するには、エンティティのプライマリキーに @PrimaryKey アノテーションと `autoGenerate = true` を設定します。 `@Insert` アノテーションでも、 `@Upsert` アノテーションでも、自動採番が可能です。 ( `@Query` でも可能だとは思いますが、試していないため正確なところはわかりません。)

以下はその実装例です：


## エンティティクラスの定義

```kotlin
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // 自動採番
    val name: String,
    val age: Int
)
```

Room の autoGenerate では、項目が 0 もしくは null の場合に、自動採番を行います。


## 使用例

```kotlin
import androidx.room.Dao
import androidx.room.Insert

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User): Long
}

class MyRepository() {

    val userDao = // Dao を取得

    suspend fun insertUser() {
        val user = User(name = "John Doe", age = 30)
        val newId = userDao.insertUser(user)
        println("New User ID: $newId")
    }
}
```
