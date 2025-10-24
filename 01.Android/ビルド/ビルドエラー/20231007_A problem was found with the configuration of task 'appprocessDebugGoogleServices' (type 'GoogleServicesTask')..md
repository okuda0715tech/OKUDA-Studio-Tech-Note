- [A problem was found with the configuration of task 'appprocessDebugGoogleServices' (type 'GoogleServicesTask').](#a-problem-was-found-with-the-configuration-of-task-appprocessdebuggoogleservices-type-googleservicestask)
  - [対処法](#対処法)


# A problem was found with the configuration of task 'appprocessDebugGoogleServices' (type 'GoogleServicesTask').

```
FAILURE: Build completed with 2 failures.

1: Task failed with an exception.
-----------
* What went wrong:
A problem was found with the configuration of task ':app:processDebugGoogleServices' (type 'GoogleServicesTask').
  - Gradle detected a problem with the following location: 'E:\AndroidProject2\_StoreReleasedProject\AppName\app\build\generated\res\google-services\debug'.
    
    Reason: Task ':app:mergeDebugResources' uses this output of task ':app:processDebugGoogleServices' without declaring an explicit or implicit dependency. This can lead to incorrect results being produced, depending on what order the tasks are executed.
    
    Possible solutions:
      1. Declare task ':app:processDebugGoogleServices' as an input of ':app:mergeDebugResources'.
      2. Declare an explicit dependency on ':app:processDebugGoogleServices' from ':app:mergeDebugResources' using Task#dependsOn.
      3. Declare an explicit dependency on ':app:processDebugGoogleServices' from ':app:mergeDebugResources' using Task#mustRunAfter.
    
    Please refer to https://docs.gradle.org/8.0/userguide/validation_problems.html#implicit_dependency for more details about this problem.

* Try:
> Run with --stacktrace option to get the stack trace.
> Run with --info or --debug option to get more log output.
> Run with --scan to get full insights.
==============================================================================

2: Task failed with an exception.
-----------
* What went wrong:
Execution failed for task ':app:checkDebugDuplicateClasses'.
> A failure occurred while executing com.android.build.gradle.internal.tasks.CheckDuplicatesRunnable
   > Duplicate class kotlin.collections.jdk8.CollectionsJDK8Kt found in modules kotlin-stdlib-1.8.22 (org.jetbrains.kotlin:kotlin-stdlib:1.8.22) and kotlin-stdlib-jdk8-1.6.21 (org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.21)
     Duplicate class kotlin.internal.jdk7.JDK7PlatformImplementations found in modules kotlin-stdlib-1.8.22 (org.jetbrains.kotlin:kotlin-stdlib:1.8.22) and kotlin-stdlib-jdk7-1.6.21 (org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.6.21)
     Duplicate class kotlin.internal.jdk7.JDK7PlatformImplementations$ReflectSdkVersion found in modules kotlin-stdlib-1.8.22 (org.jetbrains.kotlin:kotlin-stdlib:1.8.22) and kotlin-stdlib-jdk7-1.6.21 (org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.6.21)
     Duplicate class kotlin.internal.jdk8.JDK8PlatformImplementations found in modules kotlin-stdlib-1.8.22 (org.jetbrains.kotlin:kotlin-stdlib:1.8.22) and kotlin-stdlib-jdk8-1.6.21 (org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.21)
     Duplicate class kotlin.internal.jdk8.JDK8PlatformImplementations$ReflectSdkVersion found in modules kotlin-stdlib-1.8.22 (org.jetbrains.kotlin:kotlin-stdlib:1.8.22) and kotlin-stdlib-jdk8-1.6.21 (org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.21)
     Duplicate class kotlin.io.path.ExperimentalPathApi found in modules kotlin-stdlib-1.8.22 (org.jetbrains.kotlin:kotlin-stdlib:1.8.22) and kotlin-stdlib-jdk7-1.6.21 (org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.6.21)
     Duplicate class kotlin.io.path.PathRelativizer found in modules kotlin-stdlib-1.8.22 (org.jetbrains.kotlin:kotlin-stdlib:1.8.22) and kotlin-stdlib-jdk7-1.6.21 (org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.6.21)
     Duplicate class kotlin.io.path.PathsKt found in modules kotlin-stdlib-1.8.22 (org.jetbrains.kotlin:kotlin-stdlib:1.8.22) and kotlin-stdlib-jdk7-1.6.21 (org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.6.21)
     Duplicate class kotlin.io.path.PathsKt__PathReadWriteKt found in modules kotlin-stdlib-1.8.22 (org.jetbrains.kotlin:kotlin-stdlib:1.8.22) and kotlin-stdlib-jdk7-1.6.21 (org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.6.21)
     Duplicate class kotlin.io.path.PathsKt__PathUtilsKt found in modules kotlin-stdlib-1.8.22 (org.jetbrains.kotlin:kotlin-stdlib:1.8.22) and kotlin-stdlib-jdk7-1.6.21 (org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.6.21)
     Duplicate class kotlin.jdk7.AutoCloseableKt found in modules kotlin-stdlib-1.8.22 (org.jetbrains.kotlin:kotlin-stdlib:1.8.22) and kotlin-stdlib-jdk7-1.6.21 (org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.6.21)
     Duplicate class kotlin.jvm.jdk8.JvmRepeatableKt found in modules kotlin-stdlib-1.8.22 (org.jetbrains.kotlin:kotlin-stdlib:1.8.22) and kotlin-stdlib-jdk8-1.6.21 (org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.21)
     Duplicate class kotlin.random.jdk8.PlatformThreadLocalRandom found in modules kotlin-stdlib-1.8.22 (org.jetbrains.kotlin:kotlin-stdlib:1.8.22) and kotlin-stdlib-jdk8-1.6.21 (org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.21)
     Duplicate class kotlin.streams.jdk8.StreamsKt found in modules kotlin-stdlib-1.8.22 (org.jetbrains.kotlin:kotlin-stdlib:1.8.22) and kotlin-stdlib-jdk8-1.6.21 (org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.21)
     Duplicate class kotlin.streams.jdk8.StreamsKt$asSequence$$inlined$Sequence$1 found in modules kotlin-stdlib-1.8.22 (org.jetbrains.kotlin:kotlin-stdlib:1.8.22) and kotlin-stdlib-jdk8-1.6.21 (org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.21)
     Duplicate class kotlin.streams.jdk8.StreamsKt$asSequence$$inlined$Sequence$2 found in modules kotlin-stdlib-1.8.22 (org.jetbrains.kotlin:kotlin-stdlib:1.8.22) and kotlin-stdlib-jdk8-1.6.21 (org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.21)
     Duplicate class kotlin.streams.jdk8.StreamsKt$asSequence$$inlined$Sequence$3 found in modules kotlin-stdlib-1.8.22 (org.jetbrains.kotlin:kotlin-stdlib:1.8.22) and kotlin-stdlib-jdk8-1.6.21 (org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.21)
     Duplicate class kotlin.streams.jdk8.StreamsKt$asSequence$$inlined$Sequence$4 found in modules kotlin-stdlib-1.8.22 (org.jetbrains.kotlin:kotlin-stdlib:1.8.22) and kotlin-stdlib-jdk8-1.6.21 (org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.21)
     Duplicate class kotlin.text.jdk8.RegexExtensionsJDK8Kt found in modules kotlin-stdlib-1.8.22 (org.jetbrains.kotlin:kotlin-stdlib:1.8.22) and kotlin-stdlib-jdk8-1.6.21 (org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.21)
     Duplicate class kotlin.time.jdk8.DurationConversionsJDK8Kt found in modules kotlin-stdlib-1.8.22 (org.jetbrains.kotlin:kotlin-stdlib:1.8.22) and kotlin-stdlib-jdk8-1.6.21 (org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.21)
     
     Go to the documentation to learn how to <a href="d.android.com/r/tools/classpath-sync-errors">Fix dependency resolution errors</a>.

* Try:
> Run with --stacktrace option to get the stack trace.
> Run with --info or --debug option to get more log output.
> Run with --scan to get full insights.
==============================================================================

* Get more help at https://help.gradle.org

BUILD FAILED in 17s
```


## 対処法

アプリレベルの `build.gradle` に定義されている以下の部分から

```
plugins {
    id 'com.android.application'
    id 'androidx.navigation.safeargs'
    id 'com.google.gms.google-services'
}
```

```
    id 'com.google.gms.google-services'
```

の一行を削除する。**と思ったが...**

実は、プロジェクトレベルの `build.gradle` に定義されている  
`'com.google.gms.google-services'` のバージョンが古いだけだったみたいで、  
最新化したらエラーが解消されました。

