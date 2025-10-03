- [Retrofit 公式ドキュメント](#retrofit-公式ドキュメント)
  - [イントロ](#イントロ)
  - [API 定義](#api-定義)
    - [リクエストメソッド](#リクエストメソッド)
    - [URL 操作](#url-操作)
      - [パスを動的に設定する方法](#パスを動的に設定する方法)
      - [クエリパラメータを追加する方法](#クエリパラメータを追加する方法)
    - [リクエストボディ](#リクエストボディ)
    - [フォーム エンコードおよびマルチパート](#フォーム-エンコードおよびマルチパート)
      - [フォームエンコード](#フォームエンコード)
    - [ヘッダー操作](#ヘッダー操作)
      - [静的なヘッダーの設定方法](#静的なヘッダーの設定方法)
      - [動的なヘッダーの設定方法](#動的なヘッダーの設定方法)
    - [同期と非同期](#同期と非同期)
    - [Kotlin サポート](#kotlin-サポート)
      - [結果を Response オブジェクトでラップして返す場合](#結果を-response-オブジェクトでラップして返す場合)
      - [結果を任意のオブジェクトで直接返す場合](#結果を任意のオブジェクトで直接返す場合)
      - [エラー処理](#エラー処理)
  - [Retrofit の構成](#retrofit-の構成)
    - [コンバーター](#コンバーター)
    - [カスタム コンバーター](#カスタム-コンバーター)


# Retrofit 公式ドキュメント

## イントロ

Retrofit は HTTP API を Java インターフェースに変換します。

```Java
public interface GitHubService {
    @GET("users/{user}/repos")
    Call<List<Repo>> listRepos(@Path("user") String user);
}
```

Retrofit クラスは、 GitHubService インターフェースの実装を生成します。

```Java
Retrofit retrofit = new Retrofit.Builder()
    .baseUrl("https://api.github.com/")
    .build();

GitHubService service = retrofit.create(GitHubService.class);
```

作成された GitHubService からの各呼び出しは、リモート Web サーバーに同期、または、非同期の HTTP 要求を送信できます。

```Java
Call<List<Repo>> repos = service.listRepos("octocat");
```

HTTP リクエストを記述するには、アノテーションを使用します:

- URL パラメータの置換とクエリ パラメータのサポート
- リクエスト本文へのオブジェクト変換 (例: JSON、プロトコル バッファ)
- マルチパート リクエスト本文とファイルのアップロード


## API 定義

インターフェース メソッドとそのパラメータの注釈は、リクエストの処理方法を示します。


### リクエストメソッド

すべてのメソッドには、リクエスト メソッドと相対 URL を提供する HTTP 注釈が必要です。組み込みの注釈は、HTTP、GET、POST、PUT、PATCH、DELETE、OPTIONS、HEAD の 8 つです。リソースの相対 URL は注釈で指定されます。

```java
@GET("users/list")
```

URL にクエリ パラメータを指定することもできます。

```java
@GET("users/list?sort=desc")
```


### URL 操作

#### パスを動的に設定する方法

メソッドの置換ブロックとパラメータを使用して、リクエスト URL を動的に更新できます。置換ブロックは、 `{}` で囲まれた英数字の文字列です。対応するパラメータには、同じ文字列を使用して @Path の注釈を付ける必要があります。

```java
@GET("group/{id}/users")
Call<List<User>> groupList(@Path("id") int groupId);
```


#### クエリパラメータを追加する方法

クエリパラメータを追加することもできます。

```java
@GET("group/{id}/users")
Call<List<User>> groupList(@Path("id") int groupId, @Query("sort") String sort);

// URL は、こんな感じになります。
// group/groupId/users?sort=desc
```

複雑なクエリ パラメータの組み合わせには、マップを使用できます。

```java
@GET("group/{id}/users")
Call<List<User>> groupList(@Path("id") int groupId, @QueryMap Map<String, String> options);
```


### リクエストボディ

@Body アノテーションを使用して、HTTP リクエスト ボディとして使用するオブジェクトを指定できます。

```java
@POST("users/new")
Call<User> createUser(@Body User user);
```

オブジェクトは、Retrofit インスタンスで指定されたコンバーターを使用して変換されます。コンバーターが追加されていない場合は、RequestBody のみを使用できます。


### フォーム エンコードおよびマルチパート

フォーム エンコードおよびマルチパート データを送信するメソッドも宣言できます。


#### フォームエンコード

フォームエンコードとは、クライアントとサーバーがデータのやり取りをする際のデータ形式 ( Content-Type ) の一種で、 application/x-www-form-urlencoded 形式のことを示します。

この形式は、キーと値のペアでデータを保持し、特殊文字やスペースは URL エンコードされます。

例えば、次のようなデータがあった場合、

```
name=John Doe
age=30
```

データ転送時には、次のようなデータになっています。 (スペースは、 + になります。)

```
name=John+Doe&age=30
```

より詳しい情報は、 [application/x-www-form-urlencoded](../../../ネットワーク/HTTP/Content-Type%20一覧.md/#applicationx-www-form-urlencoded) を参照してください。

フォーム エンコード データは、メソッドに @FormUrlEncoded が存在する場合に送信されます。各キーと値のペアには、名前と値を提供するオブジェクトを含む @Field の注釈が付けられます。

```java
@FormUrlEncoded
@POST("user/edit")
Call<User> updateUser(@Field("first_name") String first, @Field("last_name") String last);
```

[マルチパートリクエスト](../../../ネットワーク/HTTP/Content-Type%20一覧.md/#multipartform-data) は、メソッドに @Multipart が存在する場合に使用されます。パートは @Part アノテーションを使用して宣言されます。

```java
@Multipart
@PUT("user/photo")
Call<User> updateUser(@Part("photo") RequestBody photo, @Part("description") RequestBody description);
```

マルチパート Part は Retrofit のコンバーターの 1 つを使用するか、独自のシリアル化を処理するために RequestBody を実装できます。


### ヘッダー操作

#### 静的なヘッダーの設定方法

@Headers アノテーションを使用してメソッドの静的ヘッダーを設定できます。

```java
@Headers("Cache-Control: max-age=640000")
@GET("widget/list")
Call<List<Widget>> widgetList();
```

```java
@Headers({
    "Accept: application/vnd.github.v3.full+json",
    "User-Agent: Retrofit-Sample-App"
})
@GET("users/{username}")
Call<User> getUser(@Path("username") String username);
```

ヘッダーは互いに上書きされないことに注意してください。同じ名前のすべてのヘッダーがリクエストに含まれます。


#### 動的なヘッダーの設定方法

リクエスト ヘッダーは、@Header アノテーションを使用して動的に更新できます。対応するパラメータを @Header に提供する必要があります。値が null の場合、ヘッダーは省略されます。それ以外の場合は、値に対して toString が呼び出され、結果が使用されます。

```java
@GET("user")
Call<User> getUser(@Header("Authorization") String authorization)

// この場合
// "Authorization: authorization"
// というヘッダーが追加されます。
```

クエリ パラメータと同様に、複雑なヘッダーの組み合わせにはマップを使用できます。

```java
@GET("user")
Call<User> getUser(@HeaderMap Map<String, String> headers)
```

すべてのリクエストに追加する必要があるヘッダーは、 [OkHttp インターセプター](https://square.github.io/okhttp/features/interceptors/) を使用して指定できます。


### 同期と非同期

呼び出しインスタンスは、同期または非同期で実行できます。各インスタンスは 1 回しか使用できませんが、clone() を呼び出すと、使用可能な新しいインスタンスが作成されます。

Android では、コールバックはメイン スレッドで実行されます。JVM では、コールバックは HTTP リクエストを実行した同じスレッドで発生します。


### Kotlin サポート

#### 結果を Response オブジェクトでラップして返す場合

インターフェース メソッドは、Response オブジェクトを直接返す Kotlin の suspend 関数をサポートし、現在の関数を一時停止しながら呼び出しを作成して非同期で実行します。

```kotlin
@GET("users")
suspend fun getUser(): Response<User>
```


#### 結果を任意のオブジェクトで直接返す場合

suspend メソッドは、直接本文を返すこともできます。 200 番台以外のステータスが返された場合は、応答を含む HttpException がスローされます。

```kotlin
@GET("users")
suspend fun getUser(): User
```


#### エラー処理

エラー処理の方法は、サーバーからのレスポンスを Response オブジェクトでラップするかどうかによって、動作が変わってきます。

- Response でラップしたオブジェクトを返す場合
  - レスポンスコードが 200 番台以外の場合でも、 HttpException はスローされません。
  - `if(response.isSuccessful)` で、レスポンスコードが 200 番台かどうかを自分でチェックすることができるため、これが false を返す場合には、サーバーからエラーが返されたと判断して、エラー処理を実装してください。
- Response でラップせず、任意のオブジェクトを返す場合
  - レスポンスコードが 200 番台以外の場合は、 HttpException をスローします。
  - そのため、上記のような `if(response.isSuccessful)` の判定を行うのではなく、 `catch(e: HttpException){}` ブロックで、エラー処理を実装してください。

ただし、いずれの場合も、タイムアウトやネットワーク障害でエラーレスポンスすら受け取れなかった場合に備えて、 `catch(e: Exception){}` で、キャッチするのを忘れないでください。

```kotlin
//******************************
// Response でラップしている場合
//******************************
try {
    val responseObject = GithubApi.retrofitService.getProfile(auth = auth)
    if (responseObject.isSuccessful) {
        // 200 番台の場合の処理
    } else {
        // 200 番台以外の場合の処理
    }
} catch (e: Exception) {
    // サーバーから結果が返ってこなかった場合の処理
}

//******************************
// Response でラップしていない場合
//******************************
try {
    val myObject = GithubApi.retrofitService.getProfile(auth = auth)
    // 200 番台の場合の処理
} catch (e: HttpException) {
    // 200 番台以外の場合の処理
} catch (e: Exception) {
    // サーバーから結果が返ってこなかった場合の処理
}
```

参考 : サーバーからのレスポンスを Response オブジェクトでラップしていない場合は、レスポンスコードが 200 番台の場合に、レスポンスコードを取得するすべがなさそうなので、レスポンスコードを常に取得したい場合は、 Response オブジェクトでラップした結果を受け取るほうが良いと思います。


## Retrofit の構成

Retrofit は、API インターフェースを呼び出し可能なオブジェクトに変換するクラスです。デフォルトでは、Retrofit はプラットフォームに適したデフォルトを提供しますが、カスタマイズも可能です。


### コンバーター

デフォルトでは、Retrofit は HTTP 本文を OkHttp の ResponseBody 型にデシリアライズすることしかできず、@Body には RequestBody 型しか受け入れることができません。

コンバーターを追加して他の型をサポートすることもできます。兄弟モジュールは、一般的なシリアル化ライブラリをユーザーの利便性のために適応させます。

- [Gson](https://github.com/google/gson): com.squareup.retrofit2:converter-gson
- [Jackson](https://github.com/FasterXML/jackson): com.squareup.retrofit2:converter-jackson
- [Moshi](https://github.com/square/moshi/): com.squareup.retrofit2:converter-moshi
- [Protobuf](https://developers.google.com/protocol-buffers/): com.squareup.retrofit2:converter-protobuf
- [Wire](https://github.com/square/wire): com.squareup.retrofit2:converter-wire
- [Simple XML](http://simple.sourceforge.net/): com.squareup.retrofit2:converter-simplexml
- [JAXB](https://docs.oracle.com/javase/tutorial/jaxb/intro/index.html): com.squareup.retrofit2:converter-jaxb
- Scalars (プリミティブ、ボックス化、および String ): com.squareup.retrofit2:converter-scalars

GsonConverterFactory クラスを使用して、デシリアライズに Gson を使用する GitHubService インターフェースの実装を生成する例を次に示します。

```java
Retrofit retrofit = new Retrofit.Builder()
    .baseUrl("https://api.github.com/")
    .addConverterFactory(GsonConverterFactory.create())
    .build();

GitHubService service = retrofit.create(GitHubService.class);
```


### カスタム コンバーター

Retrofit がすぐにサポートしないコンテンツ形式 (YAML、txt、カスタム形式など) を使用する API と通信する必要がある場合、または別のライブラリを使用して既存の形式を実装する場合は、独自のコンバーターを簡単に作成できます。 [Converter.Factory クラス](https://github.com/square/retrofit/blob/master/retrofit/src/main/java/retrofit2/Converter.java) を拡張するクラスを作成し、アダプターの構築時にインスタンスを渡します。



