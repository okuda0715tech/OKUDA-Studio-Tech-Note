<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [AlarmManager経由でIntentにExtraを入れて渡す方法](#alarmmanager経由intentextra入渡方法)
	- [概要](#概要)
	- [サンプルコード](#)

<!-- /TOC -->


# AlarmManager経由でIntentにExtraを入れて渡す方法

## 概要

Intentに動的なデータを入れて渡したい場合にはIntentのExtraにデータを入れて渡します。  
特に`AlarmManager`を経由してExtraのデータを渡したい場合には、プロセス間を越える通信であるため、  
データは`Serializable`ではなく、`Parcerable`である必要があります。  
しかし、なんらかの不具合なのか仕様なのかわかりませんが、`Parcerable`では渡せないことがわかっています。  
そのため、バイトデータに変換し、それをExtraに詰め込む方法でデータの受け渡しをすることになります。  


## サンプルコード

**バイトデータとオブジェクトデータの変換を行うユーティリティクラス**

```java
public class ByteTranslator {

    public static byte[] toByteFrom(Serializable data) throws IOException {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try (ObjectOutputStream out = new ObjectOutputStream(bos);) {
            out.writeObject(data);
            out.flush();
            return bos.toByteArray();
        }
    }

    public static <T> T toObjectFrom(byte[] bytes, Class<T> clazz) throws ClassNotFoundException, IOException {

        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        T translatedObject = null;

        try (ObjectInput in = new ObjectInputStream(bis)) {
            Object o = in.readObject();
            if (clazz.isAssignableFrom(o.getClass())) {
                translatedObject = clazz.cast(o);
            }
        }

        return translatedObject;
    }
}
```

**BroadcastReceiverを起動するIntentを生成する側**

```java
Alarm alarm = new Alarm(...);
Intent alarmReceiverIntent = new Intent(context, AlarmReceiver.class);
try {
    alarmReceiverIntent.putExtra("alarm", ByteTranslator.toByteFrom(alarm));
} catch (IOException e) {
    // TODO
}
```

**AlarmManagerによって起動されるBroadcastReceiver側**

```java
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        byte[] bytes = intent.getByteArrayExtra("alarm");
        Alarm alarm = null;
        try {
            alarm = ByteTranslator.toObjectFrom(bytes, Alarm.class);
        } catch (ClassNotFoundException | IOException e) {
            // TODO
        }
    }
}
```

**受け渡しされるデータオブジェクト**

受け渡しされるデータオブジェクトは`Serializable`インターフェースを実装している必要があります。  
`Serializable`インターフェースは抽象メソッドを持たないマーカーインターフェースのため、  
オーバーライドするメソッドはありません。

```java
public class Alarm implements Serializable {

...

}
```
