- [buildscript\_plugins\_dependenciesの違い](#buildscript_plugins_dependenciesの違い)
  - [dependencies の使い所](#dependencies-の使い所)
  - [buildscript plugins の使い所](#buildscript-plugins-の使い所)


# buildscript_plugins_dependenciesの違い

## dependencies の使い所

dependencies は、アプリケーションコード内で使いたいライブラリをインストールするために使用します。

例えば、アプリケーションコード内でAPIにアクセスしたい場合は、  
dependencies を使用してHTTPリクエスト用のライブラリをインストールする、といった形です。

（例）

```gradle
allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    // 必要になる部分で定義
    dependencies {
        implementation "org.apache.httpcomponents:httpclient:4.5.13"
    }
}
```


## buildscript plugins の使い所

buildscript と plugins は、ともに build.gradle 内で使いたいライブラリをインストールするために使用します。

例えば、コードフォーマットを gradle のタスクとして定義したい、という場合は、  
build.gradle 内でコードフォーマット用のライブラリが必要になるので、  
buildscript や plugins でコードフォーマット用ライブラリをインストールする、といった形です。

なお、buildscript と plugins の使い分けですが、基本的には plugins で使用できるように  
ライブラリが開発されていたら plugins を使用するのが良いようです。

plugins の方が新しく出来たインストール方法であり、 buildscript に比べて簡単に記述できるようになっています。

（ plugins の使用例）

```gradle
// 可能な限りファイルの最初の方で定義
plugins {
    id 'checkstyle'
}
```

（ buildscript の使用例）

```gradle
// 可能な限りファイルの最初の方で定義
buildscript {
    repositories {
        mavenCentral()
    }

    // buildscript内で使用するdependenciesは、あくまでbuildscript内で効力を持つものであり、
    // 通常のdependenciesの様にアプリケーションコード内で使用できるようにはならない
    dependencies {
        classpath platform('software.amazon.awssdk:bom:2.17.160')
        classpath 'software.amazon.awssdk:s3'
    }
}
```



