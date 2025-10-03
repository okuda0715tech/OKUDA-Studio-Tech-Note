<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [DialogFragmentのDataBinding](#dialogfragmentのdatabinding)
	- [コード](#コード)

<!-- /TOC -->


# DialogFragmentのDataBinding

## コード

**CreateDirDialog.java**

```Java
public class CreateDirDialog extends AppCompatDialogFragment {

    private EditText mDirName;
    private TextView mSysMessage;
    private Listener mListener;
    public final ObservableField<String> mDirName2 = new ObservableField<>("");
    public final ObservableField<String> mSysMessage2 = new ObservableField<>("");
    String path;

    /**
     * ダイアログの生成
     *
     * @param savedInstanceState savedInstanceState
     * @return AlertDialog
     * @throws IllegalStateException アタッチするアクティビティが取得できない時に発生する例外
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) throws IllegalStateException {

        LayoutInflater inflater = requireActivity().getLayoutInflater();

        //**********************
        // DataBindingなしの場合
        //**********************
        View view = inflater.inflate(R.layout.dialog_create_new_directory, null);

        mDirName = view.findViewById(R.id.dir_name_edit);
        mSysMessage = view.findViewById(R.id.message);

        return new AlertDialog.Builder(requireActivity())
                .setView(view)
                .setTitle(R.string.create_new_dialog_title_regist)
                .create();

        //**********************
        // DataBindingありの場合
        //**********************
        DialogCreateNewDirectoryBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.dialog_create_new_directory, null, false);
        binding.setListener(this);
        binding.setDirName(mDirName2);
        binding.setSysMessage(mSysMessage2);
        View view = binding.getRoot();

        return new AlertDialog.Builder(requireActivity())
                .setView(view)
                .setTitle(R.string.create_new_dialog_title_regist)
                .create();
    }
}
```
