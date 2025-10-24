- [Duplicate class kotlin.collections.jdk8.CollectionsJDK8Kt found in modules kotlin-stdlib-1.8.22](#duplicate-class-kotlincollectionsjdk8collectionsjdk8kt-found-in-modules-kotlin-stdlib-1822)
  - [エラーの全文](#エラーの全文)
  - [対処方法](#対処方法)


# Duplicate class kotlin.collections.jdk8.CollectionsJDK8Kt found in modules kotlin-stdlib-1.8.22

## エラーの全文

```
Duplicate class kotlin.collections.jdk8.CollectionsJDK8Kt found in modules kotlin-stdlib-1.8.22 (org.jetbrains.kotlin:kotlin-stdlib:1.8.22) and kotlin-stdlib-jdk8-1.6.21 (org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.21)
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

Go to the documentation to learn how to Fix dependency resolution errors.

```


## 対処方法

app レベルの `build.gradle` に以下を追加する。

```
configurations.implementation {
    exclude group: 'org.jetbrains.kotlin', module: 'kotlin-stdlib-jdk8'
    exclude group: 'org.jetbrains.kotlin', module: 'kotlin-stdlib-jdk7'
}
```

jdk のバージョンが上がった場合は追記すれば OK らしい。

