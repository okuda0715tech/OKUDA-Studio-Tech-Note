<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [adbコマンドでpermissionを変更する方法](#adbコマントてpermissionを変更する方法)

<!-- /TOC -->


# adbコマンドでpermissionを変更する方法

```
adb shell pm [grant|revoke] <package-name> <permission-name>
```

例

```
adb shell pm grant com.example.myapp android.permission.CAMERA
```


