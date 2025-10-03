<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [Entity](#entity)
	- [概要](#概要)
	- [基本形](#基本形)
	- [テーブル名を変更する](#テーブル名を変更する)
	- [列名を変更する](#列名を変更する)
	- [複数のフィールドを主キーに指定する](#複数のフィールドを主キーに指定する)
	- [ID 等の自動採番](#id-等の自動採番)
	- [テーブルに含めたくないフィールドを除外する](#テーブルに含めたくないフィールドを除外する)
	- [外部キーを指定する（一対多）](#外部キーを指定する一対多)
		- [オプション](#オプション)
	- [外部キーを指定する（多対多）](#外部キーを指定する多対多)
	- [インデックスを登録する](#インデックスを登録する)
	- [unique制約を付与する](#unique制約を付与する)
	- [Not Null制約を付与する](#not-null制約を付与する)
	- [クラスがネストしたEntity](#クラスがネストしたentity)
	- [デフォルト値の指定](#デフォルト値の指定)
	- [注意事項](#注意事項)
		- [abstract class や interface は、 Entity として定義できない。](#abstract-class-や-interface-は-entity-として定義できない)
<!-- TOC END -->


# Entity

## 概要

Entityはテーブルに匹敵するデータモデルクラスです。


## 基本形

```Java
@Entity
public class User {
		// Entitiyのフィールドには、intなどのプリミティブ型よりもIntegerなどのラッパークラスを使用する方が良い。
		// なぜなら、デフォルト値を設定した場合に、プリミティブ型だと初期値（intなら0など）が設定されているために、
		// デフォルト値がセットされない状況になるため。

		@PrimaryKey
		public int id;

		public String firstName;
		public String lastName;
}
```

- クラス名がテーブル名になります。
- フィールド名が列名になります。
- プライマリーキーは最低でも一つ必要です。例え、フィールドが一つだけのテーブルの場合でも必要です。
- getterメソッドやsetterメソッドも定義できます。


## テーブル名を変更する

```Java
// SQLite のテーブル名では、大文字と小文字が区別されません。
@Entity(tableName = "users")
public class User {
		// ...
}
```


## 列名を変更する

```Java
@Entity(tableName = "users")
public class User {
		@PrimaryKey
		public int id;

		@ColumnInfo(name = "first_name")
		public String firstName;

		@ColumnInfo(name = "last_name")
		public String lastName;
}
```


## 複数のフィールドを主キーに指定する

```Java
@Entity(primaryKeys = {"firstName", "lastName"})
public class User {
		public String firstName;
		public String lastName;
}
```


## ID 等の自動採番

`@PrimaryKey(autoGenerate = true)` を記述すると自動採番されます。  
`autoGenerate` パラメータは、デフォルトで `true` となっているため、  
`@PrimaryKey` と記述するだけでも自動採番となります。

```Java
@Entity
public class User {
		@PrimaryKey(autoGenerate = true)
		public int id;

		public String firstName;
		public String lastName;
}
```

`Insert` 処理を実行したときに、IDに何らかの値がセットされているとその値を使用してSQLが実行されるが、  
何もセットされていないと自動採番される。  
何らかの値がセットされているときに、重複するデータが上書きされるか、更新が中止されるかは、  
`Insert` 処理のオプションに従う。


## テーブルに含めたくないフィールドを除外する

デフォルトでは、全てのフィールドがテーブルの列に含まれます。
含めたくないフィールドがある場合は以下のように`@Ignore`アノテーションで除外します。

```Java
@Entity
public class User {
	@PrimaryKey
	public int id;

	public String firstName;
	public String lastName;

	@Ignore
	Bitmap picture;
}
```

親エンティティークラスから継承したフィールドを除外する場合は、以下のようにします。

```Java
@Entity(ignoredColumns = "picture")
public class RemoteUser extends User {
	@PrimaryKey
	public int id;

	public boolean hasVpn;
}
```


## 外部キーを指定する（一対多）

以下の例は、`Book`テーブルの`user_id`列を外部キーとして、`User`テーブルの`id`列に紐付ける例です。

本の貸し出し管理などで、ある本を今、誰に貸し出しているかを管理するような場面をイメージしています。

```Java
@Entity(foreignKeys = @ForeignKey(entity = User.class,
                                  onDelete = CASCADE,
                                  parentColumns = "id",
                                  childColumns = "user_id"))
public class Book {
    @PrimaryKey
    public int bookId;
    public String title;
    @ColumnInfo(name = "user_id")
    public int userId;
}
```


### オプション

- onDelete
	- CASCADE
		- 親テーブルのレコードを削除した時に、子テーブルの関連レコードが削除される。
	- RESTRICT
		- 子テーブルに関連レコードが存在する場合は、親テーブルのレコードを削除することができない。
	- NO_ACTION または onDelete自体を未記載
		- 基本的には RESTRICT と同じ
		- RESTRICTは、親テーブルのレコードを削除しようとした時で即エラーとなる。（必ずエラーとなります。例外なし。）
		- 一方、NO_ACTIONは、親テーブルのレコードを削除しようとした時点で即エラーとはならない。
		- その後、同一トランザクション内で子テーブルの関連レコードが削除されていれば親のレコードも削除することができる。
	- SET_NULL
		- 親テーブルのレコードが削除された場合、子テーブルの関連レコードにはNULLがセットされます。
	- SET_NULL
		- 親テーブルのレコードが削除された場合、子テーブルの関連レコードにはデフォルト指定した値がセットされます。
- onUpdate
	- onDeleteと同じであるため、onDeleteの説明文を参照。（説明文の「削除」が「更新」に変わるだけ）


## 外部キーを指定する（多対多）

多対多のリレーションを構築する場合は、中間テーブルを作成するのがDB設計の定石です。

以下の例では、音楽再生アプリで、ユーザーが好きな名前のプレイリストを複数作成でき、そのプレイリストに複数の音楽を登録できる場合を考えます。


**プレイリストエンティティー**

```Java
@Entity
public class Playlist {
		@PrimaryKey public int id;

		public String name;
		public String description;
}
```


**楽曲エンティティー**

```Java
@Entity
public class Song {
		@PrimaryKey public int id;

		public String songName;
		public String artistName;
}
```


**中間テーブルエンティティー**

中間テーブルエンティティーは、各テーブルの主キーに対して外部キー連携を持ったテーブルです。

```Java
@Entity(tableName = "playlist_song_join",
				primaryKeys = { "playlistId", "songId" },
				foreignKeys = {
								@ForeignKey(entity = Playlist.class,
														parentColumns = "id",
														childColumns = "playlistId"),
								@ForeignKey(entity = Song.class,
														parentColumns = "id",
														childColumns = "songId")
								})
public class PlaylistSongJoin {
		public int playlistId;
		public int songId;
}
```


参考までに、データ取得コードも以下に掲載しておきます。

```Java
@Dao
public interface PlaylistSongJoinDao {
		@Insert
		void insert(PlaylistSongJoin playlistSongJoin);

		@Query("SELECT * FROM playlist " +
					 "INNER JOIN playlist_song_join " +
					 "ON playlist.id=playlist_song_join.playlistId " +
					 "WHERE playlist_song_join.songId=:songId")
		List<Playlist> getPlaylistsForSong(final int songId);

		@Query("SELECT * FROM song " +
					 "INNER JOIN playlist_song_join " +
					 "ON song.id=playlist_song_join.songId " +
					 "WHERE playlist_song_join.playlistId=:playlistId")
		List<Song> getSongsForPlaylist(final int playlistId);
}
```


## インデックスを登録する

```Java
// @Index("インデックス名")
// @Index(value = {"列名1", "列名2"})}
@Entity(indices = {@Index("name"), @Index(value = {"last_name", "address"})})
public class User {
		@PrimaryKey
		public int id;

		public String firstName;
		public String address;

		@ColumnInfo(name = "last_name")
		public String lastName;

		@Ignore
		Bitmap picture;
}
```


**インデックス作成のポイント**

検索（where句）や並べ替え（Order by句）に使用する列を登録すると効果的である。

where句やOrder by句に単一の列が使用される場合は、インデックスにも単一の列を登録し、複数列が使用される場合は複数列を登録すると効果的である。


**インデックスの使用方法**

インデックスを使用するかどうか、どのインデックスを使用するかは自動的に決まるため、通常のSQLを記述すれば問題ない。


## unique制約を付与する

ユニーク制約は、インデックス経由でしかつけることができないため、以下のように定義します。

```Java
@Entity(indices = {@Index(value = {"first_name", "last_name"},
				unique = true)})
public class User {
		@PrimaryKey
		public int id;

		@ColumnInfo(name = "first_name")
		public String firstName;

		@ColumnInfo(name = "last_name")
		public String lastName;

		@Ignore
		Bitmap picture;
}
```

ユニーク制約とプライマリーキー制約の違いはNullが許可されるかどうかです。

ユニーク制約ではNullが許可され、プライマリーキー制約では許可されていません。

例えば、住所マスタテーブルで、プライマリーキーにはIDを設定し、住所にはユニーク制約を付与します。

多くの場合、「主キーではないけれど、ビジネスロジック的に重複は発生しない」という列がユニーク制約の対象になります。


## Not Null制約を付与する

```Java
@NonNull
```

Javaの通常の`@NonNull`アノテーションを使用します。


## クラスがネストしたEntity

ネストは、共通の列群がある場合に再利用することができ、便利です。

```Java
public class Address {
		public String street;
		public String state;
		public String city;

		@ColumnInfo(name = "post_code") public int postCode;
}

@Entity
public class User {
		@PrimaryKey public int id;

		public String firstName;

		@Embedded(prefix = "address_")
		public Address address;
}
```

`User`オブジェクトを示すテーブルには、`id`、`firstName`、`street`、`state`、`city`、`post_code`という名前の列が格納されています。

`(prefix = "address_")`の指定は任意です。

指定した場合には、列名が、`address_id`、`address_firstName`、・・・のように先頭にプレフィックスがつきます。

共通の名前がある場合に区別するのに便利です。


## デフォルト値の指定

```java
@Entity(tableName = "users")
public class User {
		@PrimaryKey
		public int id;

		@ColumnInfo(name = "last_name", defaultValue = "NO_NAME")
		public String lastName;

		@ColumnInfo(name = "first_name", defaultValue = "1") // int型のフィールドでもダブルクォーテーションで囲む
		public int firstName;

		// @ColumnInfo アノテーションの defaultValue は、あくまでテーブル定義上のデフォルト値
		@ColumnInfo(name = "address", defaultValue = "")
		// クラスのフィールドへ代入する初期値は、DB へデータを書き込む前のメモリ上の初期値
		public String address = "";
}
```


## 注意事項

### abstract class や interface は、 Entity として定義できない。

フィールドが定数になってしまう interface が Entity として定義できないのは、当然である。

abstract class のような共通クラスを Entity として定義したい場合には、  
abstract を取り除いて具象クラスにする必要がある。  
その際、 abstract メソッドは、 abstract を外して、空のインスタンスメソッドとして定義  
するなどの対策が必要である。
