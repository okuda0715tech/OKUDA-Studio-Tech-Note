<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [Work にインプットデータを渡す](#work-にインプットデータを渡す)
  - [概要](#概要)
  - [サンプル](#サンプル)
<!-- TOC END -->


# Work にインプットデータを渡す

## 概要

Work にインプットデータを渡す場合は、 `androidx.work.Data` クラスに、 「キー & バリュー」  
形式でデータを格納して渡します。  

格納できるデータ容量の上限は、約 10 KB です。

渡せるデータの種類は、プリミティブ型だけだと思われます。  
それ以外のデータを渡したい場合には、バイト型に変換して渡すことが可能です。


## サンプル

以下のサンプルは、画像をアップロードする Work に、画像の URI を渡しています。

```Java
public class UploadWork extends Worker {

   public UploadWork(Context appContext, WorkerParameters workerParams) {
       super(appContext, workerParams);
   }

   @NonNull
   @Override
   public Result doWork() {
       String imageUriInput = getInputData().getString("IMAGE_URI");
       if(imageUriInput == null) {
           return Result.failure();
       }

       uploadFile(imageUriInput);
       return Result.success();
   }

}
```

```Java
WorkRequest myUploadWork =
      new OneTimeWorkRequest.Builder(UploadWork.class)
           .setInputData(
               new Data.Builder()
                   .putString("IMAGE_URI", "http://...")
                   .build()
           )
           .build();
```
