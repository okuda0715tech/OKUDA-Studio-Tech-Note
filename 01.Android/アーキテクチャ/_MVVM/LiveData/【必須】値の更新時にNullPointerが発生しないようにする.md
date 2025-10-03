<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [【必須】値の更新時に NullPointerException が発生しないようにする](#必須値の更新時に-nullpointerexception-が発生しないようにする)
<!-- TOC END -->


# 【必須】値の更新時に NullPointerException が発生しないようにする

MutableLiveData の値の更新方法を間違えると NullPointerException が発生する可能性があるため、  
必ず、この実装方法を真似するようにしてください。

まずは、以下のサンプルコードを確認してください。

```java
public class TweetExeResultViewModel extends ViewModel {

    private MutableLiveData<Integer> listSize;

    public MutableLiveData<Integer> getListSize() {
        if (listSize == null) {
            listSize = new MutableLiveData<>(1);
        }
        return listSize;
    }

    public void setListSize(int size) {
        // 良い例
        getListSize().setValue(size);
        // 悪い例
        listSize.setValue(size);
    }
}
```

ポイントは、 `MutableLiveData` で定義されている `listSize` の更新方法です。

`setListSize()` メソッドの内部に、良い例と悪い例を記載しました。  
悪い例の場合は、 `listSize` 変数が初期化されていない可能性があるため、  
`NullPointerException` が発生する場合があります。  
一方で、良い例の場合は、必ず `listSize` 変数が初期化されてから値が更新されるため、  
`NullPointerException` は発生しません。
