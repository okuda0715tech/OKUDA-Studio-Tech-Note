- [BuildGradle の具体的な使用方法](#buildgradle-の具体的な使用方法)
  - [リファレンス](#リファレンス)
  - [モジュールレベルの build.gradle の例](#モジュールレベルの-buildgradle-の例)
  - [プロダクトフレーバーごとに versionName を変える方法](#プロダクトフレーバーごとに-versionname-を変える方法)
  - [Javaからプロダクトフレーバーを参照する方法](#javaからプロダクトフレーバーを参照する方法)
    - [dimension が一つだけの場合](#dimension-が一つだけの場合)
    - [dimension が二つ以上の場合](#dimension-が二つ以上の場合)
      - [全ての dimension を結合したプロダクトフレーバーを取得したい場合](#全ての-dimension-を結合したプロダクトフレーバーを取得したい場合)
      - [指定した単一の dimension のプロダクトフレーバーを取得したい場合](#指定した単一の-dimension-のプロダクトフレーバーを取得したい場合)
  - [ビルドタイプ、プロダクトフレーバーごとに applicationId を変更する方法](#ビルドタイププロダクトフレーバーごとに-applicationid-を変更する方法)
  - [dependencies ブロックのコンフィギュレーションの説明](#dependencies-ブロックのコンフィギュレーションの説明)
    - [implementation と api の違い](#implementation-と-api-の違い)
      - [implementation](#implementation)
      - [api](#api)
    - [compileOnly](#compileonly)
      - [使用例１：javax.annotation:javax.annotation-api](#使用例１javaxannotationjavaxannotation-api)
      - [使用例２：androidx.annotation:annotation](#使用例２androidxannotationannotation)
      - [使用例３：Android AAR ライブラリ作成時：依存をバンドルしたくない場合](#使用例３android-aar-ライブラリ作成時依存をバンドルしたくない場合)
    - [runtimeOnly](#runtimeonly)
      - [使用例：プラグイン方式のモジュール（実行時に動的ロード）](#使用例プラグイン方式のモジュール実行時に動的ロード)
    - [参考](#参考)


# BuildGradle の具体的な使用方法

## リファレンス

Android 用の公式リファレンスは以下にあります。その時点で最新のドキュメントを参照してください。

[Android Plugin DSL Reference](https://developer.android.com/reference/tools/gradle-api)


## モジュールレベルの build.gradle の例

```java
/**
 * androidブロックを使用可能にします。
 * androidブロックを使用することで、android独自のビルドオプションが設定できるようになります。
 */

apply plugin: 'com.android.application'

android {

  /**
   * コンパイル時のAPIレベルを指定します。
   * アプリはここで指定したAPIレベル以下のAPI機能を使用できます。
   */

  compileSdkVersion 28

  /**
   * ビルドツールのバージョンを指定します。
   * 公式ドキュメントによると常に最新のバージョンを指定するように書かれています。
   *
   * 最新バージョンの確認は以下のリリースノートを参照。
   * https://developer.android.com/studio/releases/build-tools
   *
   * ダウンロード及びインストールはSDK Managerから行います。
   * 「Android SDK Build-tools」という名前で一覧に存在しています。
   *
   * このパラメータは書かなくても良いです。
   * 書かない場合は、デフォルトで推奨バージョンが使用されます。
   * 書いた場合は、古いバージョンを書いているとワーニングを出してくれるので気がつくとができる。
   */

  buildToolsVersion "28.0.3"

  /**
   * 全てのビルドバリアントに共通の内容を記述します。
   *
   * ビルドシステムから main / AndroidManifest.xml 内の一部の属性を動的にオーバーライドできます。
   */

  defaultConfig {

    /**
     * アプリケーションIDはPlay Storeや端末内でアプリを一意に識別するためのIDです。
     * アプリ公開後は変更することができません。
     * パッケージ名と異なるアプリケーションIDを設定することは可能だが、やらない方が良い。
     * たとえば、Context.getPackageName() メソッドではアプリケーション ID が返されます。
     * ただし、AndroidのAPIではパッケージ名を使用すると記載されていても実際にはアプリケーションIDを使用していて、問題ないこともある。
     * また、WebView を使用している場合は、アプリケーション ID のプレフィックスとしてパッケージ名を使用することをお勧めします。使用しないと、issue 211768 で説明している問題が発生する可能性があります。
     *
     * 命名規則
     * ・2 個以上のセグメント（1 個以上のドット）が必要です
     * ・各セグメントは文字で始まる必要があります
     * ・すべての文字は英数字または下線である必要があります [a-zA-Z0-9_]
     */

    applicationId 'com.example.myapp'

    // アプリを動作させるのに最低限必要なAPIレベルを記述します。
    // 条件を満たさない端末ではアプリがインストールできなくなります。
    // 記載がない場合は、全ての端末でインストールができます。
    minSdkVersion 15

    /*
     * ここで指定したAPIレベルより古いAPIレベルの端末で動作させた時に、
     * targetSdkVersion の動作に合わせた動作ができるように Support Library などが
     * 機能するようになっているみたい。
     */
    targetSdkVersion 28

    /*
     * 内部バージョン番号として使用する整数。（1〜21億）
     * この番号は、バージョンを比較してどちらが新しいか判別するために使われます。
     * このバージョン番号はユーザーには表示されません。
     * ユーザーに表示されるのは、 versionName 設定で定義した番号です。
     * リリースごとにそのリリースがメジャー リリースかマイナー リリースかに関係なく単純に値を増やしていきます。
     * つまり、versionCode 値は、ユーザーに表示されるアプリのリリース バージョンと必ずしも類似している必要はありません
     */
    versionCode 1

    /*
     * フォーマットは XX.XX.XX が一般的だが、自由に設定して良い。
     * versionName はユーザーに表示することのみを目的としています。
     */
    versionName "1.0"
  }

  buildTypes {

    /**
     * By default, Android Studio configures the release build type to enable code
     * shrinking, using minifyEnabled, and specifies the Proguard settings file.
     */

    release {
        // コード圧縮が有効かどうか。
        // true : 有効, false : 無効
        minifyEnabled true
        /*
         * 以下の記述をすることでProGuardが有効になる。
         * 'proguard-rules.pro' には、難読化の対象外にするクラス名を指定する。
         * 'proguard-rules.pro' は、app / proguard-rules.pro に存在している。
         * getDefaultProguardFile('proguard-android.txt') では、ProGuard構成ファイルのパスを取得しています。
         */
        proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
    }
  }

  flavorDimensions "payment", "usage"
  productFlavors {
    free {
      /*
       * プロダクトフレーバー内の分類を表す。
       * 同一dimension内でのみフレーバーが切り分けられる。
       * 異なるdimension間では、組合せたビルドバリアントが生成される。
       *
       * この例の場合は、以下のビルドバリアントが生成される。
       * freeDevDebug
       * freeDevRelease
       * freeProdDebug
       * freeProdRelease
       * paidDevDebug
       * paidDevRelease
       * paidProdDebug
       * paidProdRelease
       *
       * dimensionをきることによって、新しいプロダクトフレーバーを追加する場合に最小限の記述で
       * 全ての組合せのビルドバリアントを作ることができる。また、build.gradleの可読性も上がる。
       *
       * 未指定の場合は、AndroidManifest.xmlのpackage属性の値がapplicationIdに設定される。
       */
      dimension "payment"
      applicationId 'com.example.myapp.free'
    }

    paid {
      dimension "payment"
      applicationId 'com.example.myapp.paid'
    }

    dev {
      dimension "usage"
      // SDKバージョン21からmultidex環境でのビルドが最適化され、高速にビルドができるため、
      // 開発時のみ minSdkVersion を21にする。
      minSdkVersion 21
    }

    prod {
      dimension "usage"
      minSdkVersion 15
    }
  }

  /**
   * The splits block is where you can configure different APK builds that
   * each contain only code and resources for a supported screen density or
   * ABI. You'll also need to configure your build so that each APK has a
   * different versionCode.
   */

  splits {
    // Settings to build multiple APKs based on screen density.
    density {

      // Enable or disable building multiple APKs.
      enable false

      // Exclude these densities when building multiple APKs.
      exclude "ldpi", "tvdpi", "xxxhdpi", "400dpi", "560dpi"
    }
  }
}

/**
 * The dependencies block in the module-level build configuration file
 * specifies dependencies required to build only the module itself.
 * To learn more, go to Add build dependencies.
 */

dependencies {
    /*
     * ローカルライブラリモジュールへの依存関係
     */
    implementation project(":lib")

    /*
     * ローカルバイナリへの依存関係
     */
    // module_name/libs/ ディレクトリ内の JAR ファイルへの依存関係を宣言します
    // module_nameはこのモジュールレベルのbuild.gradleの親ディレクトリを指す。
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    // module_name/libs/ ディレクトリ内の JAR ファイルへの依存関係を個別に指定することもできます。
    implementation files('libs/foo.jar', 'libs/bar.jar')

    /*
     * リモートバイナリへの依存関係
     */
    // 省略した書き方
    implementation 'com.android.support:appcompat-v7:28.0.0'
    // 省略しない書き方
    implementation group: 'com.android.support', name: 'appcompat-v7', version: '28.0.0'

    /*
     * 特定のソースセットにのみ依存関係を構築したい場合
     *
     * たとえば、"free" プロダクトフレーバーのみに implementation 依存関係を追加する場合は、次のようにします。
     */
     freeImplementation 'com.google.firebase:firebase-ads:9.8.0'

}
```


## プロダクトフレーバーごとに versionName を変える方法

（例）

```java
android {
  ...
  defaultConfig {
    ...
    versionCode 2
    versionName "1.1"
  }
  productFlavors {
    demo {
      ...
      versionName "1.1-demo"
    }
    full {
      ...
    }
  }
}
```

この例では、defaultConfig {} ブロックの versionCode 値は、既存の APK にアプリの 2 回目のリリースが含まれていることを示しています。また versionName 文字列で、ユーザーにバージョン 1.1 と表示するように指定しています。この build.gradle ファイルでは、"demo" と "full" の 2 つのプロダクト フレーバーも定義しています。"demo" プロダクト フレーバー ブロックで versionName を "1.1-demo" と定義しているため、"demo" ビルドではデフォルト値ではなくこの versionName を使用します。"full" プロダクト フレーバー ブロックでは versionName を定義していないため、デフォルト値の "1.1" を使用します。

targetSdkVersion、minSdkVersion なども、プロダクトフレーバーごとにオーバーライドさせることができる。


## Javaからプロダクトフレーバーを参照する方法

### dimension が一つだけの場合

```Java
switch (BuildConfig.FLAVOR) {
    case "paid":
        // paid flavorの時の処理
        break;
    case "free":
        // free flavorの時の処理
        break;
}
```

### dimension が二つ以上の場合

#### 全ての dimension を結合したプロダクトフレーバーを取得したい場合

```Java
switch (BuildConfig.FLAVOR) {
    case "paidProd":
    case "paidDev":
        // paid flavorの時の処理
        break;
    case "freeProd":
    case "freeDev":
        // free flavorの時の処理
        break;
}
```

#### 指定した単一の dimension のプロダクトフレーバーを取得したい場合

```Java
switch (BuildConfig.FLAVOR_payment) {
    case "paid":
        // paid flavorの時の処理
        break;
    case "free":
        // free flavorの時の処理
        break;
}
```

## ビルドタイプ、プロダクトフレーバーごとに applicationId を変更する方法

各ビルドタイプ、各プロダクトフレーバーで applicationId をオーバーライドするか
もしくは、 applicationIdSuffix 属性を指定します。

（例）

```Java
android {
    defaultConfig {
        applicationId "com.example.myapp"
    }
    productFlavors {
        free {
            applicationIdSuffix ".free"
        }
        pro {
            applicationIdSuffix ".pro"
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix ".debug"
        }
    }
}
```

上記の例で、

- プロダクトフレーバーのみ指定した場合
  - com.example.myapp.free
- ビルドバリアントのみ指定した場合
  - com.example.myapp.debug
- 両方指定した場合
  - Gradle ではプロダクト フレーバーの後にビルドタイプの設定を適用するため
  - com.example.myapp.free.debug

となります。


## dependencies ブロックのコンフィギュレーションの説明

| 新コンフィギュレーション | 旧コンフィギュレーション | 説明                                               |
| ------------------------ | ------------------------ | -------------------------------------------------- |
| implementation           | compile                  | 参照元モジュールの再コンパイルを行わない           |
| api                      | compile                  | 参照元モジュールの再コンパイルを行う               |
| compileOnly              | provided                 | コンパイル時だけ必要で、実行時には不要なライブラリ |
| runtimeOnly              | apk                      | 実行時だけ必要なライブラリ                         |


### implementation と api の違い

#### implementation

以下のような app と libraryA と retrofit の依存関係の場合について考えます。

```scss
app ── depends ──> libraryA ── implementation ──> retrofit
```

- 使う場面：
  - 他のモジュールに依存ライブラリを隠したいとき（基本的にはこちらを使用する）
- 特徴：
  - 自分のモジュール内では使える
  - 依存先モジュール（このモジュールを使う側）にはそのライブラリは見えない（再エクスポートされない）
- メリット：
  - ビルドが速くなる（依存グラフが小さくなる）
    - retrofit のバージョンが変更された場合、 app からは retrofit が参照できないため app を再コンパイルする必要はなく、 libraryA のみを再コンパイルすれば良い。

```groovy
implementation 'com.squareup.retrofit2:retrofit:2.9.0'
```


#### api

以下のような app と libraryA と retrofit の依存関係の場合について考えます。

```scss
app ── depends ──> libraryA ── api ──> retrofit
```

- 使う場面
  - 自分のモジュールが公開ライブラリや SDK で、その依存も一緒に公開したいとき
  - 公開ライブラリや SDK を作っている場合でも、依存を公開する必要がない場合は、 implementation を使用した方が良いです。その方が、ライブラリのバージョン競合が発生するのを避けられるためです。
- 特徴：
  - 自分のモジュールでも使える
  - 依存先モジュール（このモジュールを使う側）にも見える
- 例：
  - ViewModel ライブラリを開発しており、外部にも LiveData を使わせたいとき
- メリット：
  - 依存関係の二重定義をなくせる
    - app 側で改めて同じライブラリ (この場合は retrofit ) を定義しなくても良い。

```groovy
api 'androidx.lifecycle:lifecycle-livedata-ktx:2.6.2'
```


### compileOnly

- 使う場面
  - コンパイル時にしか必要ない依存関係を定義したい場合
    - アノテーションを使用するための依存関係を定義したい場合
  - コンパイル時と実行時の両方で必要だが、アプリによっては不要な依存関係となる場合
    - implementation や api で定義されたライブラリの依存関係は、ライブラリの使用側に自動的に解決されます。
    - つまり、ライブラリの該当の機能を使用していなかったとしても、アプリにバイトコードが含まれてしまいます。
    - 該当の機能を使用していないなら、そのクラスへの依存も不要になるため、ビルドを通すためだけにコンパイル時だけ依存関係を構築すると、アプリサイズを小さくすることが可能です。
    - 例えば、広告表示用ライブラリが複数の広告配信会社のライブラリに依存しており、そのうち、特定の配信会社のライブラリしか使用しない場合
- 特徴
  - 依存関係が .pom に含まれないため、 Gradle はライブラリの依存関係を自動的に解決できない。
  - よって、ライブラリの利用側の build.gradle.kts で、ライブラリの依存関係を利用側の依存関係として定義する必要がある。
  - または、 Service Loader や SPI を使用して、実行時に動的に依存関係をロードする必要がある。 

#### 使用例１：javax.annotation:javax.annotation-api

```kotlin
dependencies {
    compileOnly("javax.annotation:javax.annotation-api:1.3.2")
}
```

- Java の @Nullable / @Nonnull などのアノテーション
- 実行時には使われないので compileOnly が最適


#### 使用例２：androidx.annotation:annotation

```kotlin
dependencies {
    compileOnly("androidx.annotation:annotation:1.8.0")
}
```

- @NonNull, @Keep, @RequiresApi など
- 実行時には Android Framework 側の同等機能があるため runtime には不要


#### 使用例３：Android AAR ライブラリ作成時：依存をバンドルしたくない場合

ライブラリ作者が

- 「他のプロジェクトが同じ依存を用意すべき」
- 「自分の AAR に依存ライブラリを含めたくない」

という場合は、その依存を compileOnly にして、 AAR に混入しないようにすることがあります。

例えば、 Logging ライブラリが、 Timber を「存在すれば使う」場合は compileOnly を使います。

```kotlin
compileOnly("com.jakewharton.timber:timber:5.0.1")
```


### runtimeOnly

- 使う場面
  - 実行時にのみ参照する必要のあるクラスへの依存関係を定義する場合
    - 使用するかどうか任意の機能に対して、依存関係の定義を必須にしない。
    - アプリサイズが小さくなる。
    - ビルド時間が短くなる。
- 特徴
  - .pom ファイルには (ランタイムスコープとして) 自動的に追加される。
  - アプリユーザーが画面を指で操作している最中などに、トリガーが実行されるとクラスのロードが開始される。
  - プログラム内で該当クラスが直接参照されない。
  - リフレクション、プラグイン方式、実行時ロードなどの仕組みを使用して、実行時に初めて該当のクラスが参照される場合に使用できる。


#### 使用例：プラグイン方式のモジュール（実行時に動的ロード）

- 画像デコーダ
- 圧縮/解凍ライブラリ

など、アプリ起動後に ServiceLoader / SPI (Service Provider Interface) で読み込むもの

```kotlin
// 追加コーデックのみ runtimeOnly として登録
runtimeOnly("com.github.bumptech.glide:gifdecoder:4.16.0")
```

SPI とは、 Java 標準のプラグイン機構で、「ある機能を実装したモジュールを後から差し替えられる仕組み」です。

ServiceLoader は、SPI を使って「実装クラス」を実行時に探索するクラスです。


### 参考

- implementation と api の違いにつてい解説しているドキュメント
  - [Speeding up your Android Gradle builds](https://speakerdeck.com/ctake0005/speeding-up-your-android-gradle-builds)
- [ビルド依存関係を追加する - Android Developers](https://developer.android.com/build/dependencies?hl=ja)


