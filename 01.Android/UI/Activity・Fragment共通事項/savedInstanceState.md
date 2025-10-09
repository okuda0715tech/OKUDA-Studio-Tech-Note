<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [savedInstanceState](#savedinstancestate)
	- [ポイント](#ポイント)
	- [対応方法](#対応方法)
		- [保存方法](#保存方法)
		- [復元方法](#復元方法)
	- [テスト方法](#テスト方法)

<!-- /TOC -->

# savedInstanceState

## ポイント

- OSがメモリ不足などでプロセスを破棄することがある。
  - プロセスを破棄するのであって、Activityを直接破棄することはない。
  - [原文](https://developer.android.com/guide/components/activities/activity-lifecycle?hl=ja#asem)
  - 原文抜粋　`システムがメモリを解放するためにアクティビティを直接強制終了することはありません。代わりに、アクティビティが実行されているプロセスを強制終了します。`
- Viewの状態はOS側で保存・復元してくれるため、開発者が気にする必要はない。
  - ただし、ViewにIDを設定していない場合は保持しないため、注意すること。
- Activityのメンバ変数についてはOSが保存・復元してくれないため、開発者が保存・復元する必要がある。
- アプリがフォアグラウンドの状態で電源をOFFにした時も`onSaveInstanceState()`は呼ばれる。
- デバイス再起動時はsavedInstanceStateは使用不可である。
  - デバイス再起動後は、シャットダウン前に実行していたプロセスが自動的に再開するわけではないため、新規Activity生成の扱いになってしまう。


## 対応方法

### 保存方法

```Java
public class LoginActivity extends Activity {
    private boolean mTest = true;
    private int     mNum  = 0;

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean("Test", mTest);
        savedInstanceState.putBoolean("Num", mNum);
    }
}
```


### 復元方法

savedInstanceStateから値を取り出し、メンバ変数にセットする。

**SampleActivity.java**

```Java
public class LoginActivity extends Activity {
    private boolean mTest = true;
    private int     mNum  = 0;

		@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        // 構成変更やメモリ不足などシステム起因で破棄されたアクティビティが再生成されるときのみ
        // savedInstanceStateがnullではなくなる。
        if (null != savedInstanceState) {
            mTest = savedInstanceState.getBoolean("Test");
            mNum  = savedInstanceState.getInt("Num");
        }
    }
}
```

保存されたデータは、破棄からの復元時に `onCreate` と `onRestoreInstanceState` に渡ってきます。  
`onCreate` は `Activity` の復元時以外にも呼ばれるため、 `savedInstanceState` のnullチェックが必要ですが、  
`onRestoreInstanceState` は復元時にしか呼ばれないため、nullチェックは不要です。

`onCreate` で復元するか `onRestoreInstanceState` で復元するかは、どちらのタイミングで復元したいかによります。  
`onRestoreInstanceState` は、 `onStart` と `onPostCreate` の間で呼ばれます。


## テスト方法

以下のいずれかの設定をすることでテストをすることができます。

1. **[設定アプリ] > [開発者オプション] > [アプリ]タブ > [アクティビティを保持しない]**

これを設定すると、バックグラウンドに回った時点でアクティビティが破棄されます。
プロセスは破棄されません。

2. **[設定アプリ] > [開発者オプション] > [アプリ]タブ > [バックグラウンドプロセスの上限]**

これを設定すると、バックグラウンドに回った時点でアクティビティが破棄されます。
プロセスも破棄されます。つまり、static変数も初期化されます。
