# AndroidXへの移行

## build.radle の対応

### Gradleのdependenciesの対応一覧

一覧（公式サイト）

[アーティファクトのマッピング - Android Developer](https://developer.android.com/jetpack/androidx/migrate/artifact-mappings)

ここを見れば、implementationの引数に何を記載すれば良いかがわかる。
新旧（Support LibraryとAndroidX）で対比になっているので、調べられる。


### よく使うimplementation,importの対応一覧

<implementation\>

Support Library | AndroidX
----------------|---------
com.android.support:appcompat-v7 | androidx.appcompat:appcompat:1.0.2
com.android.support.constraint:constraint-layout | androidx.constraintlayout:constraintlayout:1.1.3
com.android.support.test:runner  |  androidx.test:runner:1.2.0
com.android.support.test.espresso:espresso-core  |  androidx.test.espresso:espresso-core:3.2.0
com.android.support:design  |  com.google.android.material:material:1.0.0
com.android.support:recyclerview-v7  |  androidx.recyclerview:recyclerview:1.1.0

`com.google.android.material:material`は、FABなどのウィジェットで必要となるライブラリである。

<import、および、layoutファイルのクラス\>

Support Library | AndroidX
----------------|---------
android.support.v7.app.AppCompatActivity  | androidx.appcompat.app.AppCompatActivity
android.support.constraint.ConstraintLayout | androidx.constraintlayout.widget.ConstraintLayout


## gradle.properties の対応

以下の二行を追加する。

```Java
android.useAndroidX=true
android.enableJetifier=true
```
