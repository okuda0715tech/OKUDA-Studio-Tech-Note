<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [ViewModel](#viewmodel)
  - [概要](#概要)
  - [データの取得と保持をViewModelに任せる](#データの取得と保持をviewmodelに任せる)
  - [Contextを参照したい場合は、AndroidViewModelを使用する。](#contextを参照したい場合はandroidviewmodelを使用する)
  - [ViewModelの破棄タイミング](#viewmodelの破棄タイミング)
    - [ViewModelが破棄される時にクリーンアップを行う](#viewmodelが破棄される時にクリーンアップを行う)

<!-- /TOC -->


# ViewModel

## 概要

ViewModelはデータモデルのライフサイクル管理のためのコンポーネントである。

ViewModelコンポーネントは、MVVMアーキテクチャのVMとは異なる物を指す。
厳密には、MVVMのVMを実装するコンポーネントとして、ViewModelコンポーネントが使用できるという関係である。

＜主な役割＞

- UIコントローラーに連動したデータモデルのライフサイクル管理を行う。
  - ViewModelの役割は、OSによるUIコントローラーの一時的な破棄と再生成において、データを破棄せずに維持することである。
  - また、アプリによるUIコントローラーの永続的な破棄の際には、データモデルを破棄することである。
- 構成変更に伴うデータの再取得が不要
  - ViewModel(またはAndroidViewModel)を継承したクラス内のデータ（フィールド）は、画面回転や言語変更で構成が変わっても保持されます。
  - そのため、onSavedInstanceStateでデータを保持するコードを記述する必要がなくなります。
  - また、今まではonSavedInstanceStateで保存できなかった大容量データもViewModelを使用すれば保持できるようになります。
  - これらの機能によって、構成変更などの都度、サーバ等からデータの再取得する必要が無くなります。

＜その他の効果＞

- メモリリーク予防
  - UIコントローラーが非同期通信を開始して、リターンを受け取る前にコントローラーを破棄する場合、コントローラーへの参照が残っていると破棄できずにメモリリークしてしまう。しかし、ViewModelを使うとそれが防げる。（そもそもデータからUIコントローラーへの参照を持たないため）
- Fat Activityの防止
  - ViewModelは、UIコントローラーから様々な責務を切り離し、肥大化を防ぐのにも役立つ。
    - 主にビューデータの所有権とビューデータの取得処理の切り離し


## データの取得と保持をViewModelに任せる

データの取得と保持をViewModelに任せると、アプリの構成変更が発生して、UIコントローラーが一度破棄されて再生成されても、以前生成したViewModelを再取得してくれる。（ViewModelは破棄されない）

**MyViewModel.java**

```java
public class MyViewModel extends ViewModel {
    private MutableLiveData<List<User>> users;
    public LiveData<List<User>> getUsers() {
        if (users == null) {
            users = new MutableLiveData<List<User>>();
            loadUsers();
        }
        return users;
    }

    private void loadUsers() {
        // 非同期処理でUsersデータを取得する。
    }
}
```

**MyActivity.java**

```java
public class MyActivity extends AppCompatActivity {
    public void onCreate(Bundle savedInstanceState) {
        // 非推奨
        MyViewModel model = ViewModelProviders.of(this).get(MyViewModel.class);
        // 推奨
        MyViewModel model = new ViewModelProvider(
                getViewModelStore(), getDefaultViewModelProviderFactory()).get(MyViewModel.class);
        model.getUsers().observe(this, users -> {
            // update UI
        });
    }
}
```

`ViewModelProviders`や`getDefaultViewModelProviderFactory`を使用するためには、`build.gradle`に以下の記載が必要です。

**build.gradle**

```
// 昔(Javaの頃？)はこっちだったけど、
implementation("androidx.lifecycle:lifecycle-extensions:2.1.0")

// 今(Kotlinの場合？)はこっちかな？
implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
implementation("androidx.activity:activity-ktx:1.8.0")
implementation("androidx.fragment:fragment-ktx:1.6.2")
```


**注意**

ViewModelは、ビュー、ライフサイクル、またはアクティビティコンテキストへの参照を保持する可能性のあるクラスを参照してはなりません。

ViewModelは、LiveDataがラップしている値の変更を観察してはいけません。？？


## Contextを参照したい場合は、AndroidViewModelを使用する。

ViewModelは、LiveDataのようなライフサイクルを観察することはできません。そのため、テストコードを記述することが簡単になります。

Contextを使用したい場合は、`ViewModel`ではなく、`AndroidViewModel`を使用してください。

そして、コンストラクタで、`ApplicationContext`を渡すようにしてください。ActivityはContextを継承していますが、渡してはいけません。

なぜなら、ViewModelの生存期間はActivityよりも長くなることが一般的だからです。


## ViewModelの破棄タイミング

ViewModelは、UIコントローラーが"アプリによって"破棄される時に一緒に破棄されます。

Activityなら、finishするとき、Fragmentならdetachされるときに破棄されます。


### ViewModelが破棄される時にクリーンアップを行う

ViewModelが破棄される時にクリーンアップを行うには、`ViewModel`の`onCleared()`コールバックをオーバーライドして、  
その中でクリーンアップ処理を実行します。
