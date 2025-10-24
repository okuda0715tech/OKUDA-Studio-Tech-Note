- [基本はwrap_contentにしたいが、最大長は制約で指定したい場合](#基本はwrap_contentにしたいが最大長は制約で指定したい場合)
  - [実装方法](#実装方法)
  - [参考資料](#参考資料)


# 基本はwrap_contentにしたいが、最大長は制約で指定したい場合

## 実装方法

```xml
<androidx.constraintlayout.widget.ConstraintLayout>

    <!-- 注意：解説に必要な属性のみ残しているため、このままではビルドエラーになります。 -->
    <TextView
        android:layout_width="0dp"
        android:layout_marginStart="40dp"
        android:layout_marginEnd="40dp"
        android:text="aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintWidth_default="wrap" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

上記のようにすると、 `TextView` 内部の文字数が少ない場合は、 `TextView` の幅が `wrap_content` になるが、  
文字が増えていき、 `TextView` の左右制約 (`End_toEndOf="parent"` や `marginEnd="40dp"`) が満たせなくなると  
`wrap_content` の設定は無効になり、制約にしたがって `TextView` の幅が決定される。


## 参考資料

[ConstraintLayout max width percentage - Stack Overflow](https://stackoverflow.com/questions/49255147/constraintlayout-max-width-percentage)
