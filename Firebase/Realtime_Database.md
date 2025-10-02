<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [Realtime Database](#realtime-database)
	- [参考にしたページ](#参考にしたページ)

<!-- /TOC -->

# Realtime Database

## 参考にしたページ

[Android でのデータの読み取りと書き込み - Firebase](https://firebase.google.com/docs/database/android/read-and-write?hl=ja)

**FirebaseRealtimeDatabase.java**

```Java
public class FirebaseRealtimeDatabase {

    private DatabaseReference mDatabase;

    public FirebaseRealtimeDatabase() {
        // DatabaseReference を取得する
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);

        mDatabase.child("users").child(userId).setValue(user);
    }
}
```

RealtimeDatabaseは、連想配列の構造でデータツリーを作成する。

`child(XXX)`の`XXX`の部分には、連想配列のキーを指定し、
`setValue(YYY)`の`YYY`の部分には、連想配列のバリューを指定する。

`child("XXX")`のXXXに該当するノードが存在している場合は、既存のノードを辿り、存在していない場合は、新たにノードを作成する。

既存のキーに対して`setValue()`した場合には、新しいデータで既存のデータが上書きされる。

上記の通り、RealtimeDatabaseは連想配列の構造をしているが、DBとして使用したい場合は、`DatabaseReference`インスタンスの直後の`child(XXX)`の`XXX`の部分を`テーブル名`として捉えると良いでしょう。


**User.java**

```Java
@IgnoreExtraProperties
public class User {

    public String username;
    public String email;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

}
```




