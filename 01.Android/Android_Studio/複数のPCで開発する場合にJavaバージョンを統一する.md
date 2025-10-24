<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [複数の PC で開発する場合に Java バージョンを統一する](#複数の-pc-で開発する場合に-java-バージョンを統一する)
<!-- TOC END -->


# 複数の PC で開発する場合に Java バージョンを統一する

`File -> Settings -> Build, Execution, Deployment -> Build Tools -> Gradle` の  
`Gradle projects` セクション内にある `Gradle JDK:` の部分を  
`Embedded JDK version` に設定すると、 Android Studio に内包された Java を使用するため、  
Android Studio のバージョンが統一されていれば Java のバージョンも統一されると思われます。
