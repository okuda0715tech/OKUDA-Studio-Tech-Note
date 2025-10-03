<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [ReceiverManager](#receivermanager)

<!-- /TOC -->


# ReceiverManager

```Java
public class ReceiverManager {

    /**
     * BroadcastReceiverを有効化する。
     * @param context
     * @param receiver 有効化するBroadcastReceiver
     */
    public static void enable(Context context, Class<?> receiver){
        ComponentName bootReceiver = new ComponentName(context, receiver);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(bootReceiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    /**
     * BroadcastReceiverを無効化する。
     * @param context
     * @param receiver 無効化するBroadcastReceiver
     */
    public static void disabled(Context context, Class<?> receiver){
        ComponentName bootReceiver = new ComponentName(context, receiver);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(bootReceiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
}
```
