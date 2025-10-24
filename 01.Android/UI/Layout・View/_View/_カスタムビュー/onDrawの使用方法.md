<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [カスタムビューの作り方](#カスタムヒューの作り方)
	- [コード](#コード)

<!-- /TOC -->


# onDrawの使用方法

以下のサンプルは、カスタムViewの中にテキストを表示するサンプルアプリである。

画面内のボタンをタップすると、テキストがViewの中で左寄せになったり右寄せになったりを繰り返す。


## コード

**MainActivity.java**

```Java
public class MainActivity extends AppCompatActivity {

    Button button;
    MyCustomView myCustomView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getResources();
        button = findViewById(R.id.button);
        myCustomView = findViewById(R.id.my_custom_view);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyCustomView.TextPosition textPos = myCustomView.getTextPosition();
                if (textPos == MyCustomView.TextPosition.LEFT) {
                    myCustomView.setTextPosition(MyCustomView.TextPosition.RIGHT);
                } else if (textPos == MyCustomView.TextPosition.RIGHT) {
                    myCustomView.setTextPosition(MyCustomView.TextPosition.LEFT);
                }
            }
        });

    }
}
```


**MyCustomView.java**

```Java
class MyCustomView extends View {

    // 属性を読み取ることができるのはビューが初期化されたときだけです。
    // 動的な動作を実現するために、各属性のゲッター/セッターを定義します。
    private boolean showText;
    private TextPosition textPosition;
    public Paint textPaint;

    // ＜必須コンストラクタ＞
    // このコンストラクタがあることで Android Studio のレイアウトエディターで
    // レイアウトを表示/編集することが可能となります。
    public MyCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // カスタム属性のセットを取得する
        TypedArray attributes = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.MyCustomView, // declare-styleable name="xxx"の値
                0, 0);

        try {
            // カスタム属性をフィールドにマッピングする
            // 第一引数
            //  R.styleableには、<declare-styleable name="xxx"> の値と <attr name="yyy"> の値の連結
            // 第二引数
            //  デフォルト値
            showText = attributes.getBoolean(R.styleable.MyCustomView_showText, false);
            textPosition = TextPosition.getBy(
                    attributes.getInteger(R.styleable.MyCustomView_textPosition, 0));
        } finally {
            // TypedArray オブジェクトは共有リソースであり、使用後にリサイクルする必要があります。
            attributes.recycle();
        }

        init();
    }

    private void init() {
        // Paintの生成などの描画の準備処理はここで行うのが良い。
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.parseColor("#42F5EF"));
        float textHeight = 48; // 単位:sp
        textPaint.setTextSize(UnitConverter.spToPx(textHeight, getResources()));
    }

    public boolean getShowText() {
        return showText;
    }

    /**
     * セッターは属性値を更新した場合、invalidateとrequestLayoutを呼ぶ
     *
     * @param isShown
     */
    public void setShowText(boolean isShown) {
        this.showText = isShown;
        // ビューを再描画する必要があることをシステムに伝えるためにビューを無効化する。
        // invalidate()を呼び出すと、対象のViewを最初から再描画するため、
        // canvas.drawXXX()等で描画した内容もいったんすべてクリアされる。
        invalidate();
        // ビューのサイズまたは形状に影響する可能性がある属性値を変更したときは、
        // 新しいレイアウトをリクエストする。
        requestLayout();
    }

    public TextPosition getTextPosition() {
        return textPosition;
    }

    public void setTextPosition(TextPosition textPosition) {
        this.textPosition = textPosition;
        invalidate();
        requestLayout();
    }

    /**
     * Viewが描画されるときに呼ばれる
     * <p>
     * カスタム属性の値が変更された場合の処理を行う
     *
     * @param canvas 描画対象のキャンバス
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (showText) {
            float x = UnitConverter.dpToPx(0, getResources());
            if (textPosition == TextPosition.LEFT) {
                textPaint.setTextAlign(Paint.Align.LEFT);
                canvas.drawText("Myt", x, 100, textPaint);
            } else if (textPosition == TextPosition.RIGHT) {
                textPaint.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText("Myt", this.getWidth(), 100, textPaint);
            }
        }
    }

    public enum TextPosition {

        LEFT(0),
        RIGHT(1);

        int resourceId;

        TextPosition(int resourceId) {
            this.resourceId = resourceId;
        }

        public static TextPosition getBy(int resourceId) {
            for (TextPosition position : values()) {
                if (position.resourceId == resourceId) return position;
            }
            throw new IllegalArgumentException(
                    "IllegalArgument 'resourceId' was passed to the method" +
                            " 'TextPosition.getBy(int resourceId)'.");
        }
    }
}
```


**UnitConverter.java**

```Java
public class UnitConverter {
    private static float getDensity(Resources resources){
        return resources.getDisplayMetrics().density;
    }

    public static int dpToPx(float dp, Resources resources) {
        return (int) (dp * getDensity(resources) + 0.5f);
    }

    public static int pxToDp(float px, Resources resources){
        return (int) (px / getDensity(resources) + 0.5f);
    }

    private static float getScaleDensity(Resources resources){
        return resources.getDisplayMetrics().scaledDensity;
    }

    public static int spToPx(float sp, Resources resources){
        return (int)(sp * getScaleDensity(resources) + 0.5f);
    }

    public static int pxToSp(float px, Resources resources){
        return (int)(px / getScaleDensity(resources) + 0.5f);
    }

    // spとdpを相互に変換する場合は、いったんpxを経由して変換する。
}
```


**activity_main.xml**

```xml
<?xml version="1.0" encoding="utf-8"?><!--
カスタムViewに定義した独自の属性を使用するには、正しくは
xmlns:app="http://schemas.android.com/apk/res-auto"
の名前空間の定義が必要である。
公式ドキュメントには、独自のパッケージ名の名前空間の定義が必要だと記載されているが
最新の作成方法ではres-autoが正しい。
-->
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    tools:context=".MainActivity">

    <!-- カスタムViewが内部クラスの場合は
     com.example.customviews.charting.PieChart$PieView のように「外部クラス$内部クラス」と記載する  -->
    <com.example.myfullcustomview.MyCustomView
        android:id="@+id/my_custom_view"
        android:layout_width="100dp"
        android:layout_height="100dp"
        android:layout_margin="5dp"
        android:background="@color/colorPrimary"
        app:showText="true"
        app:textPosition="right" />

    <Button
        android:id="@+id/button"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="toggle" />

</LinearLayout>
```


**attrs.xml**

```xml
<?xml version="1.0" encoding="utf-8"?><!--ファイル名「attrs.xml」は慣例として通常この名前で作成される-->
<resources>
    <!--  属性の定義  -->
    <!--  nameはカスタムViewのクラス名と同じが良い。  -->
    <declare-styleable name="MyCustomView">
        <attr name="showText" format="boolean" />
        <attr name="textPosition" format="enum">
            <enum name="left" value="0" />
            <enum name="right" value="1" />
        </attr>
    </declare-styleable>
</resources>
```
