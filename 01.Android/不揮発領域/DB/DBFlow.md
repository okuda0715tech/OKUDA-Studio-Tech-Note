<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [DBFlow](#dbflow)
	- [Gradle](#gradle)
	- [DBの作成](#dbの作成)
	- [テーブル定義](#テーブル定義)
	- [SAVE文](#save文)
	- [INSERT文](#insert文)
	- [UPDATE文](#update文)
	- [DELETE文](#delete文)
	- [SELECT文](#select文)

<!-- /TOC -->

# DBFlow

## Gradle

**プロジェクトのbuild.gradle**

```Java
allprojects {
    repositories {
        jcenter()
        maven { url "https://jitpack.io" }
    }
}
```

**アプリのbuild.gradle**

```Java
def dbflow_version = "4.0.3"

dependencies {
    // annotationProcessor now supported in Android Gradle plugin 2.2
    // See https://bitbucket.org/hvisser/android-apt/wiki/Migration
    annotationProcessor "com.github.Raizlabs.DBFlow:dbflow-processor:${dbflow_version}"
    implementation "com.github.Raizlabs.DBFlow:dbflow-core:${dbflow_version}"
    implementation "com.github.Raizlabs.DBFlow:dbflow:${dbflow_version}"

    // sql-cipher database encryption (optional)
    // implementation "com.github.Raizlabs.DBFlow:dbflow-sqlcipher:${dbflow_version}"
}
```


## DBの作成

DBクラスを作成します。
以下のサンプルを使いまわすことができます。
以下の`NAME`フィールドはDB名であり、Table名ではありません。

**AppDatabase.java**

```Java
@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public class AppDatabase {

    public static final String NAME = "AppDatabase";

    public static final int VERSION = 1;
}
```

## テーブル定義

Entityクラスから直接テーブルが作成されるため、Entityクラス＝テーブル定義となる。
Entityクラスのサンプルを以下に示します。

**SampleEntity.java**

```Java
@Table(database = AppDatabase.class)
public class SimulationCondition extends BaseModel {

    @PrimaryKey(autoincrement = true)
    public long id;

    @Column(defaultValue = "0")
    public int simulationYears;

    @Column
    public int rent;
}
```

- クラス名がテーブル名になります。
- `@PrimaryKey`は必ず一つ必要です。
- フィールドを`private`にする場合は、`public`な`getter/setter`が必要です。

**注意**

バグなのかなんなのかわからないが、`@Column(defaultValue = "1")`としても`int`型の初期値`0`が入っていた。`defaultValue`は使えないようなので、注意。

## SAVE文

主キーに一致するデータが存在する場合はUPDATE文を実行し、存在しない場合はINSERT文を実行するのがSAVEメソッドである。

Entityの生成後、insertメソッドを呼ぶだけです。

**SaveSample.java**

```Java
SimulationCondition simulationCondition = new SimulationCondition();
simulationCondition.rent = 70000;
simulationCondition.simulationYears = 35;
simulationCondition.save();
```


## INSERT文

Entityの生成後、insertメソッドを呼ぶだけです。

**InsertSample.java**

```Java
SimulationCondition simulationCondition = new SimulationCondition();
simulationCondition.rent = 70000;
simulationCondition.simulationYears = 35;
simulationCondition.insert();
```

## UPDATE文

**UpdateSample.java**

```Java
SimulationCondition simulationCondition = new SimulationCondition();
simulationCondition.rent = 70000;
simulationCondition.simulationYears = 35;
simulationCondition.insert();
```

## DELETE文

**DeleteSample.java**

```Java
SimulationCondition simulationCondition = new SimulationCondition();
simulationCondition.rent = 70000;
simulationCondition.simulationYears = 35;
simulationCondition.insert();
```

## SELECT文

**SelectSample.java**

```Java
SimulationCondition simulationCondition = new SimulationCondition();
simulationCondition.rent = 70000;
simulationCondition.simulationYears = 35;
simulationCondition.insert();
List<User> users = SQLite.select()
                    .from(SimulationCondition.class)
                    .where(SimulationCondition_Table.rent.greaterThan(100000))
                    .queryList();
```

## COUNT文

**CountSample.java**

```Java
int count = (int)new Select(count(SimulationCondition_Table.id))
				.from(SimulationCondition.class)
				.count();
```

`SimulationCondition`はテーブル名
`id`はテーブルのプライマリーキーに設定してある列名
返却値が`long`型で返ってくるので`int`に変換した方が扱いやすい場合も多いだろう。


