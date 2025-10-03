<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [フォアグラウンドサービスのNotificationがスワイプしても消せない](#フォアグラウンドサービスのnotificationがスワイプしても消せない)
<!-- TOC END -->


# フォアグラウンドサービスのNotificationがスワイプしても消せない

フォアグラウンドで開始したサービスは、 `stopSelf()` や `stopService()` でサービスが停止されられ、  
`stopForeground()` でフォアグラウンドが終了するまで、 `Notification` をスワイプしても  
`Notification` を削除することはできません。

コーディングミスにより、サービスを停止する処理まで到達していないか、もしくは、  
フォアグラウンドを停止する処理まで到達していない可能性が高いです。
