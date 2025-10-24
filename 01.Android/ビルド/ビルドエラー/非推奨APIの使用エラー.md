<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [非推奨 API の使用エラー](#非推奨-api-の使用エラー)
  - [エラーメッセージ](#エラーメッセージ)
  - [対応方法](#対応方法)
    - [エラー部分の確認方法](#エラー部分の確認方法)
<!-- TOC END -->


# 非推奨 API の使用エラー

## エラーメッセージ

```
> Task :app:compileDebugJavaWithJavac
注意:一部の入力ファイルは推奨されないAPIを使用またはオーバーライドしています。
注意:詳細は、-Xlint:deprecationオプションを指定して再コンパイルしてください。
```


## 対応方法

このエラーに対応するには、まず、プログラムのどの部分で非推奨 API を使用しているのかを  
確認する必要があります。確認するする方法は以下に記載します。  
非推奨 API の使用箇所が確認ができたら、その API にあった個別の方法で具体的な対応を行います。


### エラー部分の確認方法

プロジェクト側の `build.gradle` に以下の記述を追加して、再度ビルドします。

```
allprojects {
    gradle.projectsEvaluated {
        tasks.withType(JavaCompile) {
            options.compilerArgs << "-Xlint:unchecked" << "-Xlint:deprecation"
        }
    }
}
```
