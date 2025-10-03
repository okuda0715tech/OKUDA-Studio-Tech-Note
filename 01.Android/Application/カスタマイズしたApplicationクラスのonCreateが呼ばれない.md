- [カスタマイズした Application クラスの onCreate が呼ばれない](#カスタマイズした-application-クラスの-oncreate-が呼ばれない)


# カスタマイズした Application クラスの onCreate が呼ばれない

`android.app.Application` クラスを継承したクラスを作成しただけでは、  
その独自のアプリケーションクラスの `onCreate()` メソッドは呼ばれません。

マニフェストファイルに以下の様に、カスタマイズしたクラスを指定する必要があります。

```xml
<application
    android:name="MyApplication" >
</application>
```



