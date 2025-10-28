- [トップレベルの build.gradle とモジュールレベルの build.gradle](#トップレベルの-buildgradle-とモジュールレベルの-buildgradle)
  - [トップレベルの build.gradle](#トップレベルの-buildgradle)
  - [モジュールレベルの build.gradle](#モジュールレベルの-buildgradle)


# トップレベルの build.gradle とモジュールレベルの build.gradle

## トップレベルの build.gradle

ルートプロジェクトディレクトリにあるトップレベルの build.gradle ファイルにより、プロジェクトのすべてのモジュールに適用されるビルド設定が定義されます。

デフォルトでは、このトップレベルのビルドファイルは buildscript ブロックを使用して、プロジェクトのすべてのモジュールに共通の Gradle リポジトリと依存関係を定義します。

複数のモジュールを含む Android プロジェクトで、全てのモジュールで共通のプロパティがある場合には、トップレベルの build.gradle に追記します。その場合は、 ext ブロック内に記述します。

（例）

```java
buildscript {...}

allprojects {...}

// This block encapsulates custom properties and makes them available to all
// modules in the project.
ext {
    // The following are only a few examples of the types of properties you can define.
    compileSdkVersion = 28
    // You can also create properties to specify versions for dependencies.
    // Having consistent versions between modules can avoid conflicts with behavior.
    supportLibVersion = "28.0.0"
}
```

モジュールレベルの build.gradle ファイルから、トップレベルの build.gradle ファイルにアクセスするには、以下のように記述します。

```java
android {
  // Use the following syntax to access properties you defined at the project level:
  // rootProject.ext.property_name
  compileSdkVersion rootProject.ext.compileSdkVersion
}

dependencies {
    implementation "com.android.support:appcompat-v7:${rootProject.ext.supportLibVersion}"
}
```

## モジュールレベルの build.gradle

各 project/module/ ディレクトリにあるモジュールレベルの build.gradle ファイルを使用すると、そのファイルがある特定のモジュールのビルド設定を行うことができます。

これらのビルド設定を行うと、追加のビルドタイプやプロダクトフレーバーなどのカスタムパッケージオプションを指定し、 main/ アプリマニフェストやトップレベルの build.gradle ファイルの設定を *オーバーライド* することができます。

