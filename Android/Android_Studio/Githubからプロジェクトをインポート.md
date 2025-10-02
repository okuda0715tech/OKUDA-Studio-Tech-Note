

# Githubからプロジェクトをインポート

## エラー対応

### クローン実行時のエラー

以下のエラーの場合の対処方法を記載します。

```
Invocation failed Unexpected end of file from server java.lang.RuntimeException:
        Invocation failed Unexpected end of file from server at git4idea.GitAppUtil.sendXmlRequest(GitAppUtil.java:30) at git4idea.http.GitAskPassApp.main(GitAskPassApp.java:58)
        Caused by: java.net.SocketException: Unexpected end of file from server at
        java.base/sun.net.www.http.HttpClient.parseHTTPHeader(HttpClient.java:866) at
        java.base/sun.net.www.http.HttpClient.parseHTTP(HttpClient.java:689) at
        java.base/sun.net.www.http.HttpClient.parseHTTPHeader(HttpClient.java:863)
        at java.base/sun.net.www.http.HttpClient.parseHTTP(HttpClient.java:689)
        at java.base/sun.net.www.protocol.http.HttpURLConnection.getInputStream0(HttpURLConnection.java:1615)
        at java.base/sun.net.www.protocol.http.HttpURLConnection.getInputStream(HttpURLConnection.java:1520)
        at org.apache.xmlrpc.DefaultXmlRpcTransport.sendXmlRpc(DefaultXmlRpcTransport.java:87)
        at org.apache.xmlrpc.XmlRpcClientWorker.execute(XmlRpcClientWorker.java:72)
        at org.apache.xmlrpc.XmlRpcClient.execute(XmlRpcClient.java:194)
        at org.apache.xmlrpc.XmlRpcClient.execute(XmlRpcClient.java:185)
        at org.apache.xmlrpc.XmlRpcClient.execute(XmlRpcClient.java:178)
        at git4idea.GitAppUtil.sendXmlRequest(GitAppUtil.java:27) ... 1 more unable to read askpass response from
        '/Users/okuda0715/Library/Caches/Google/AndroidStudio2021.2/tmp/intellij-git-askpass-local.sh'
        could not read Username for 'https://github.com': Device not configured
```

`File` -> `Invalidate Caches...` でキャッシュクリアすると上記のエラーが解消した。


### Github アカウントと連携していてもリポジトリ内のプロジェクトが参照できない場合

`File` -> `New` -> `Project from Version Control...` で、  
左側に表示される自分の Github アカウントを選択しても、右側にプロジェクトの一覧が表示されない場合は、  
アカウントの連携有効期限が切れている可能性があるため、ログインし直す必要がある。  

ログインのし直しは、色々な画面からできるが、自分の場合、おそらく、  
`File` -> `New` -> `Project from Version Control...` で、  
`Git from Version Control` の画面の右上にあるアカウントアイコンをクリックして、  
`Remove account` 的なメニューからアカウントを削除し、  
同じ画面から、 Token を使用してログインしなければならなかった。

Token は、 Windows と Github を連携した時と同じものを使用した。