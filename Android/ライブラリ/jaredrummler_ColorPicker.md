# jaredrummler_ColorPicker

## 公式HP

[jaredrummler / ColorPicker - Github](https://github.com/jaredrummler/ColorPicker)

## 実際の画面イメージ

画面イメージは上記の公式ページを参照

## 使い方

**MainActivity.java**

```Java
public class MainActivity extends AppCompatActivity implements ColorPickerDialogListener {

    View view = null;

    ColorPickerDialog.Builder builder = ColorPickerDialog.newBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        builder.setDialogType(ColorPickerDialog.TYPE_CUSTOM)    // プリセット型のダイアログではなく、カスタム型のダイアログを表示する
                .setAllowPresets(false)                         // プリセット画面への遷移ボタンを表示しない
                .setShowAlphaSlider(true)                       // 透明度選択スライダーを表示する
                .setColor(0xFFF0FF00);                          // ダイアログ表示時の初期色をセットする
        builder.create().setColorPickerDialogListener(MainActivity.this);

        view = findViewById(R.id.circle);
        view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                builder.show(MainActivity.this);
            }
        });
    }

    @Override
    public void onColorSelected(int dialogId, int color) {
        view.setBackgroundColor(color);
    }

    @Override
    public void onDialogDismissed(int dialogId) {

    }
}
```


