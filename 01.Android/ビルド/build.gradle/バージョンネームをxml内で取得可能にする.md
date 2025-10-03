<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [バージョンネームをxml内で取得可能にする](#バージョンネームをxml内で取得可能にする)
<!-- TOC END -->


# バージョンネームをxml内で取得可能にする

```java
android {
    compileSdkVersion 30
    defaultConfig {
        applicationId "com.kurodai0715.mymemomvvm"
        minSdkVersion 21
        targetSdkVersion 30
        versionCode 3
        versionName "1.1.1"
    }
    // ビルド時に、バージョンネームの値を <string> リソースに定義する
    applicationVariants.all { variant ->
        variant.resValue "string", "versionName", variant.versionName
    }
```


**applicationVariants.all の部分**

`applicationVariants.all { variant -> ... }` の部分は、全ての Build Variant  
（ debug / release といった Build Type と Product Flavor の組み合わせ）に対して  
処理を行うという意味です。


**variant.resValue の部分**

`variant.resValue "string", "versionName", variant.versionName` の部分は、  
内部的には、  
`com.android.build.gradle.api.ApkVariant` 型の変数 `variant` の  
`void resValue(String type, String name, String value)` メソッドを呼び出しています。

このメソッドを呼び出すことによって、例えば、  
`<string name="versionName">1.1.1</string>` というリソースを定義することができます。

メソッドのパラメータは、それぞれ、  
type = リソースの種類 、 name = name属性 、 value = 要素の値 を示しています。
