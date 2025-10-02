<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [API Key Secretを安全に保管する](#api-key-secretを安全に保管する)
  - [難読化する](#難読化する)
    - [難読化](#難読化)
    - [難読化した値をアプリ側にセットする](#難読化した値をアプリ側にセットする)
    - [復号化](#復号化)
  - [デバッグビルドとリリースビルドで異なる API Key と API Key Secret を用意する。](#デバッグビルドとリリースビルドで異なる-api-key-と-api-key-secret-を用意する)
  - [リリースビルドの秘密情報は、開発を行う PC のローカルファイルに保存し、そのファイルを Git 管理の対象外とする。](#リリースビルドの秘密情報は開発を行う-pc-のローカルファイルに保存しそのファイルを-git-管理の対象外とする)
  - [もし漏洩した場合は、すぐにアプリを更新するが、その際、サーバのパラメータを変更して、アプリを更新しないと機能が使用できない旨を表示するロジックを実装する。](#もし漏洩した場合はすぐにアプリを更新するがその際サーバのパラメータを変更してアプリを更新しないと機能が使用できない旨を表示するロジックを実装する)
<!-- TOC END -->


# API Key Secretを安全に保管する

API Key と API Key Secret の絶対に安全な管理方法はない。

管理上できることは以下の４つのみ

- 難読化する。
- デバッグビルドとリリースビルドで異なる API Key と API Key Secret を用意する。
  - 複数人のチームで開発を行っている場合に、権限のある人だけが秘密情報を管理できるようになる。
- リリースビルドの秘密情報は、開発を行う PC のローカルファイルに保存し、そのファイルを Git 管理の対象外とする。
  - 他の開発者が対象プロジェクトを Android Studio で開いても見れないようになる。
  - Github 等のサーバが、万が一、のぞかれても情報流出を防ぐことができる。
- もし漏洩した場合は、すぐにアプリを更新するが、その際、サーバのパラメータを変更して、  
  アプリを更新しないと機能が使用できない旨を表示するロジックを実装する。


## 難読化する

Github 上にアップしている `MyStringObfuscator` リポジトリで以下のソースコードが動かせるアプリがあります。


### 難読化

以下のメソッドのインプットに、難読化したい文字列を入れて、処理をします。  
メソッドの返値が難読化された値になります。  
難読化後の文字列は `int[]` 型となります。

```java
/**
 * 難読化するメソッド
 *
 * @param original 難読化したい文字列
 * @return 難読化された文字列(正確には文字列を int[] に変換したもの)
 */
public static int[] obfuscate(String original) {

  int[] result = new int[original.length()];

  for (int i = 0; i < original.length(); i++) {
    // Unicode コードポイントという文字コードを取得する。
    // (文字コードの種類については、複雑なので、時間があるときにでも調べるとする。)
    int numericValue = original.codePointAt(i);
    // 3 ビット左シフト
    result[i] = numericValue << 3;
  }

  return result;
}
```


### 難読化した値をアプリ側にセットする

上記で難読化された値は `int[]` 型である。この値をアプリに組み込むためには、 `int[]`  
の中身をすべて確認する必要がある。デバッグして中身を確認しても良いし、以下のメソッドで  
中身を String 型で書き出しても良い。

```java
/**
 * 難読化後の int[] の中身を String 型で書き出す。
 *
 * @param obfuscated 難読化された値
 * @return {1, 2, 3, ...} 形式で、難読化後の値を書き出した文字列
 */
public static String buildStringForApplication(int[] obfuscated) {

  StringBuilder result = new StringBuilder();

  result.append("{");
  for (int i = 0; i < obfuscated.length; i++) {
    result.append(obfuscated[i]);
    if (i < obfuscated.length - 1) {
      result.append(", ");
    }
  }
  result.append("}");

  return result.toString();
}
```

以下のような `int[]` 型で、情報を隠したいアプリ側で、難読化済みの値を取り込む。

```java
// こんな感じで難読化された値をセットする。
int[] apiKeySecret = {1, 2, 3, 4};
```


### 復号化

`int[]` 型の値を元の文字列に戻す処理を行う。  
以下のメソッドを使用して、 `int[]` 型の値を `String` 型の元の文字列に復号する。

難読化した時と同じ分量だけシフトする必要がある点に注意すること。

```java
/**
 * 難読化された int[] 配列を復元し、元の String 型の文字列を取得する。
 *
 * @param obfuscated 難読化された int[] 配列
 * @return 復号された元の文字列
 */
public static String getOriginalString(int[] obfuscated) {

  StringBuilder result = new StringBuilder();

  for (int shiftedInt : obfuscated) {
    int origInt = shiftedInt >> 3;
    result.append((char) origInt);
  }

  return result.toString();
}
```


## デバッグビルドとリリースビルドで異なる API Key と API Key Secret を用意する。

複数人のチームで開発を行っている場合に、権限のある人だけが秘密情報を管理できるようになる。

今後、対応を行ったときに詳細を記載する。


## リリースビルドの秘密情報は、開発を行う PC のローカルファイルに保存し、そのファイルを Git 管理の対象外とする。

他の開発者が対象プロジェクトを Android Studio で開いても見れないようになる。  
Github 等のサーバが、万が一、のぞかれても情報流出を防ぐことができる。

今後、対応を行ったときに詳細を記載する。


## もし漏洩した場合は、すぐにアプリを更新するが、その際、サーバのパラメータを変更して、アプリを更新しないと機能が使用できない旨を表示するロジックを実装する。

ユーザーに該当の機能が使用できなくなった旨をいち早く伝えるための手段として用意すると良い。

今後、対応を行ったときに詳細を記載する。
