<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [base64エンコーディング](#base64エンコーディング)
  - [Apache Commons Codec を使用した例](#apache-commons-codec-を使用した例)
<!-- TOC END -->


# base64エンコーディング

## Apache Commons Codec を使用した例

`java.util.Base64` パッケージは、 API レベル 26 以上からしか使用できないため、  
それ未満の API レベルをサポートするには、 `Apache Commons Codec` ライブラリを使用する。

**build.gradle**

```
// Apache Commons Codec
implementation "commons-codec:commons-codec:1.15"
```

**javaの処理**

```java
SecureRandom random = new SecureRandom();
byte[] bytes = new byte[20];
random.nextBytes(bytes);
// 20 バイトの乱数を Base64 でエンコードして、 String 型で返す。
String encodedValue = Base64.encodeBase64String(bytes)
```
