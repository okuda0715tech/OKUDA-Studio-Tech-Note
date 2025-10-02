autotoc<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [onDestroyViewでバインディングクラスをクリーンアップする理由](#ondestroyviewでバインディングクラスをクリーンアップする理由)
<!-- TOC END -->


# onDestroyViewでバインディングクラスをクリーンアップする理由

ViewBinding では、以下のサンプルのように、 `onDestroyView()` メソッド内で、  
バインディングクラスのインスタンスへの参照を解放するのがベストです。  
理由はサンプルコード内のコメントを参照。

```Java
public class AnnouncementsFragment extends Fragment {

    private FragmentAnnouncementsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAnnouncementsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // バインディングクラスのインスタンスを null でクリーンアップしたほうが良いです。
        // onDestroyView() が呼ばれて、 onDestroy() が呼ばれない場合は、次は onCreateView() が呼ばれます。
        // onCreateView() が呼ばれると、新たなインスタンスがセットされるため、それ以降にガベージコレクションが
        // 実行されますが、 onDestroyView() が呼ばれてから onCreateView() が呼ばれるまでの間に
        // 既存のインスタンスは既に再利用されないことが確定しているため、 onDestroyView() が呼ばれた時点で
        // クリーンアップするのがメモリ開放のベストタイミングです。
        binding = null;
    }
}
```
