<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [Retrofit](#retrofit)
  - [build.gradle](#buildgradle)
  - [リクエストを構築する](#リクエストを構築する)
    - [Call でラップしない方法](#call-でラップしない方法)
  - [リクエストを送信する](#リクエストを送信する)
  - [ボディで JSON データを送信する](#ボディで-json-データを送信する)
  - [errorBody を取得する](#errorbody-を取得する)
<!-- TOC END -->


# Retrofit

## build.gradle

**kotlin**

```
val retrofitVersion = "2.9.0"
implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")
implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")
```

**groovy**

```
def retrofit_version = "2.9.0"
implementation "com.squareup.retrofit2:retrofit:$retrofit_version"
implementation "com.squareup.retrofit2:converter-gson:$retrofit_version"
implementation "com.squareup.okhttp3:logging-interceptor:4.9.0"
```


## リクエストを構築する

```java
public interface ApiInterface {
    @POST("/oauth/{username}/request_token")
    Call<Data> requestToken(
            // メッセージ・ヘッダーの付与
            @Header("Authorization") String authorization,
            // パスパラメーターの付与
            @Path("username") username: String,
            // クエリストリングの付与
            @Query("oauth_callback") String oauthCallback,
            // ボディーの付与
            @Body DataClass data);
}
```


### Call でラップしない方法

retrofit の場合は、リクエストの戻り値の型を Call クラスでラップする場合と、そうではなく、 Response 型で、直接データを受け取る方法があります。

Call を使用する場合は、 execute() 関数や enqueue() 関数でリクエストを送信しますが、 Response を使用する場合はそれらの呼び出しが必要なく、リクエストが定義された関数を呼び出すだけで、リクエストが送信されます。


## リクエストを送信する

**ViewModel 等での処理**

```java
private final String TwitterApiBaseUrl = "https://api.twitter.com";

Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(TwitterApiBaseUrl)
        .addConverterFactory(ScalarsConverterFactory.create()) // ボディを String 型で扱う場合
        .addConverterFactory(GsonConverterFactory.create()) // ボディを JSON フォーマットで扱う場合
        .build();

// ApiInterface は、上記「リクエストを構築する」の項目を参照
ApiInterface service = retrofit.create(ApiInterface.class);

String header = "xxx";
String queryParam = "yyy";
String body = "zzz";

Call<Data> data = service.requestToken(header, queryParam, body);

data.enqueue(new Callback<Data>() {
  final int STATUS_OK = 200;

  @Override
  public void onResponse(Call<Data> call, Response<Data> response) {
    if (STATUS_OK == response.code()) {
      String body = response.body();
    } else {
      // errorBody の取得方法
      String error = response.errorBody().string();
    }
  }

  @Override
  public void onFailure(Call<Data> call, Throwable t) {
  }
});
```


## ボディで JSON データを送信する

**データクラス**

```java
public class ManageTweetRequest {

    @SerializedName("reply_settings") // JSON ファイル内でのキーの名前
    private String replySettings;

    public ManageTweetRequest(String replySettings) {
        this.replySettings = replySettings;
    }

    public String getReplySettings() {
        return replySettings;
    }

    public void setReplySettings(String replySettings) {
        this.replySettings = replySettings;
    }
}
```


## errorBody を取得する

```java
@Override
public void onResponse(Call<Data> call, Response<Data> response) {
  if (STATUS_OK == response.code()) {
    String body = response.body();
  } else {
    // errorBody の取得
    String error = response.errorBody().string();
  }
}
```
