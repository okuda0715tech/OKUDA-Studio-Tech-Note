# thisの使い方

## 自分自身のコンストラクタの呼び出し

```Java
public class Sample {

    private static final String val = "aaa";

    public Sample() {
        this(val);
        // do something
    }

    private Sample(String val) {
        // do something
    }
}
```

上記は、外部からは引数なしのコンストラクタを呼び出し、内部で引数ありの非公開なコンストラクタを呼び出す例です。
