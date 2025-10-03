<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [onActivityCreated() メソッドの非推奨対応](#onactivitycreated-メソッドの非推奨対応)
  - [概要](#概要)
    - [公式ドキュメントの引用](#公式ドキュメントの引用)
  - [対応方法](#対応方法)
    - [View を操作するコードを実装したい場合](#view-を操作するコードを実装したい場合)
    - [View を操作しないコードを実装したい場合](#view-を操作しないコードを実装したい場合)
    - [Activity の onCreate() が「呼ばれた時だけ」 or 「呼ばれた後に」コールバックを取得する方法](#activity-の-oncreate-が呼ばれた時だけ-or-呼ばれた後にコールバックを取得する方法)
  - [LiveData の observe() は、 onCreateDialog() or onViewCreated() で行う](#livedata-の-observe-は-oncreatedialog-or-onviewcreated-で行う)
    - [【注意点】第一引数には、 getViewLifecycleOwner() を渡す](#注意点第一引数には-getviewlifecycleowner-を渡す)
    - [observe() メソッドの第一引数の役割](#observe-メソッドの第一引数の役割)
    - [getViewLifecycleOwner() で取得されるのは何か？](#getviewlifecycleowner-で取得されるのは何か)
    - [Fragment のライフサイクルとその View のライフサイクルの違い](#fragment-のライフサイクルとその-view-のライフサイクルの違い)
    - [二重に observe してしまう原因](#二重に-observe-してしまう原因)
    - [【補足】 onCreate() で observe するケース](#補足-oncreate-で-observe-するケース)
<!-- TOC END -->


# onActivityCreated() メソッドの非推奨対応

## 概要

onActivityCreated() メソッドの利用は、 Fragment のバージョン 1.3.0 以降は非推奨となりました。


### 公式ドキュメントの引用

(日本語訳)

```
onCreateDialog によって作成されたダイアログに触れるコードには onCreateDialog() を使用します。
onCreateView によって作成されたビューに触れるコードには onViewCreated() を使用します。
その他の初期化には onCreate() を使用します。

Activity の onCreate() が呼ばれた時だけコールバックを取得するには、 onAttach() で
Activity の Lifecycle に androidx.lifecycle.LifecycleObserver を登録して、
CREATED コールバックを受け取ったらそれを削除してください。
```

## 対応方法


### View を操作するコードを実装したい場合

`DialogFragment` の View を操作するコードは `onCreateDialog()` を使用します。  
`Fragment` の View を操作するコードは `onViewCreated()` を使用します。


### View を操作しないコードを実装したい場合

`DialogFragment` であろうと `Fragment` であろうと、その中に表示する View を操作しない初期化処理は、
`onCreate()` を使用します。

**ただし、 `Fragment` の `onCreate()` が呼ばれる時点で、 `Activity` の `onCreate()` が完了しているとは限りません。**

そのため、確実に `Activity` の `onCreate()` が完了した後に処理を行いたい場合は、  
`onViewCreated()` に実装します。


### Activity の onCreate() が「呼ばれた時だけ」 or 「呼ばれた後に」コールバックを取得する方法


```java
public class SampleFragment extends Fragment {

    LifecycleObserver lifecycleObserver;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        lifecycleObserver = new DefaultLifecycleObserver() {
            @Override
            public void onCreate(@NonNull LifecycleOwner owner) {
              // Activity の onCreate() が呼ばれた時 or 呼ばれた後に実行したい処理を記述する。
            }
        };

        requireActivity().getLifecycle().addObserver(lifecycleObserver);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        requireActivity().getLifecycle().removeObserver(lifecycleObserver);
    }
}
```


## LiveData の observe() は、 onCreateDialog() or onViewCreated() で行う

見出しの通り、 `onCreateDialog()` or `onViewCreated()` で行ってください。


### 【注意点】第一引数には、 getViewLifecycleOwner() を渡す

`observe()` メソッドの第一引数には、 `getViewLifecycleOwner()` を渡してください。
`Fragment` や `NavBackStackEntry` を渡さないようにしてください。  
もし、 `Fragment` や `NavBackStackEntry` を渡してしまうと、二重三重...に observe してしまいます。


### observe() メソッドの第一引数の役割

`observe()` メソッドの第一引数には、以下の二つの役割があります。

1. どのコンポーネントがアクティブになった場合に LiveData の更新を通知するのかを指定する。
2. どのコンポーネントが破棄された場合に、 observe を中止するのかを指定する。


### getViewLifecycleOwner() で取得されるのは何か？

 `getViewLifecycleOwner()` で取得されるのは、 Fragment に紐づけられた View のライフサイクルオーナーです。


### Fragment のライフサイクルとその View のライフサイクルの違い

Fragment のライフサイクルは、  
`onPause() -> onStop() -> onDestroyView() -> onDestroy()` の順番で呼ばれます。

`Fragment` は、 `onDestroy()` まで呼ばれた時に破棄されるのに対して、  
`Fragment` に紐づく `View` は、 `onDestroyView()` まで呼ばれた時に破棄されます。

例えば、画面遷移した際などは、 `onDestroyView()` までしか呼ばれず、  
`View` だけが破棄され、 `Fragment` は破棄されない場合が多いです。


### 二重に observe してしまう原因

二重に observe してしまう原因は、適切に observe を中止できていないためです。

observe の開始を、 `onCreateDialog()` or `onViewCreated()` で行うので、  
observe の中止は、その対のコールバックである `onDestroyView()` で行う必要があります。

しかし、 `Fragment` or `NavBackStackEntry` を `observe()` メソッドの第一引数に渡した場合は、  
`onDestroy()` が呼ばれた場合に、 observe を中止しています。

これにより、 observe を中止するタイミングに対して、
observe を開始するタイミングが多くなってしまい、 observe が重複して行われてしまいます。

observe の中止を `onDestroyView()` で行うには、 `observe()` の第一引数に、  
`getViewLifecycleOwner()` で取得できる `View` のライフサイクルを渡します。


### 【補足】 onCreate() で observe するケース

サンプルコードによっては、 `onCreate()` で observe しているコードもあります。  
その場合は、 `observe()` メソッドの第一引数に `Fragment` or `NavBackStackEntry`  
を渡すのが適切でしょう。

ただし、その場合は、別の問題が発生するらしいのですが、別の問題が何かがよくわからないため、  
基本的には、 `onCreateDialog()` or `onViewCreated()` で observe するようにしてください。
