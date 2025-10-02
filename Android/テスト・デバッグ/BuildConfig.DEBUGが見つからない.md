- [BuildConfig.DEBUG が見つからない](#buildconfigdebug-が見つからない)
  - [原因](#原因)
  - [対策](#対策)


# BuildConfig.DEBUG が見つからない

## 原因

2023 年の 5 月くらいから、 Gradle の最新バージョンでは、  
追加の記述がないと、 BuildConfig.DEBUG が見つからないっぽい。


## 対策

Module:app の build.gradle に以下を追加してください。

```
buildFeatures { buildConfig = true } 
```

