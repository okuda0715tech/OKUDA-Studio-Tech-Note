# Butter Knife

## 概要

ButterKnifeはレイアウトXMLで生成したViewオブジェクトをJavaで生成したオブジェクトに紐付けるDIツールである。

## 利用目的

- Activity, ViewのfindViewByIdを楽に書く
- ViewのonClickListenerなどを楽に書く
- onCreateメソッド内にあったfindViewByIdやonClickListenerを外に出すことでコードがスッキリする。

## 使い方

### Build Gradle

モジュールレベルbuild.gradleに以下を追加する。

```Java
dependencies {
    implementation 'com.jakewharton:butterknife:10.1.0'
    annotationProcessor 'com.jakewharton:butterknife-compiler:10.1.0'
}
```

古い記事では、aptを使った以下のような方法が解説されているが、最近は上記の2行を設定すればOK。

[ButterKnifeを使ってみたまとめ - ButterKnifeの導入方法](https://qiita.com/takenoki/items/42d3c8c0bb3809fff1a0#butterknife%E3%81%AE%E5%B0%8E%E5%85%A5%E6%96%B9%E6%B3%95)

### メソッドの説明

```Java
static Unbinder bind(@NonNull Object target, @NonNull View source)
```

target・・・ViewのR.id.xxxとJavaクラスの変数の対応を記載したクラスのインスタンスを指定する
source・・・R.id.xxxが書かれているxmlファイルのルートViewのインスタンスを指定する


## 参考

公式サイト
[Butter Knife](http://jakewharton.github.io/butterknife/)


