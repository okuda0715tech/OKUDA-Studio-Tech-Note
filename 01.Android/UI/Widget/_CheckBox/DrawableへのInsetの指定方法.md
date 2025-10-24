<!-- TOC START min:1 max:3 link:true asterisk:false update:true -->
- [DrawableへのInsetの指定方法](#drawableへのinsetの指定方法)
<!-- TOC END -->


# DrawableへのInsetの指定方法

```Java
import com.google.android.material.checkbox.MaterialCheckBox;

public class CustomCheckBox extends MaterialCheckBox {

    public CustomCheckBox(@NonNull Context context) {
        super(context);
    }

    public CustomCheckBox(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        // 【単位変換】dp -> px 変換
        float density = getResources().getDisplayMetrics().density;
        int insetRightLeftPx = (int) (20 * density);

        // Insetの設定
        // 【注意】左右のinset値は同じにしてあげないと、リップルエフェクトが中心から少しずれてしまう。
        Drawable drawable = getButtonDrawable();
        InsetDrawable insetDrawable = new InsetDrawable(drawable, insetRightLeftPx, 0, insetRightLeftPx, 0);
        setButtonDrawable(insetDrawable);
    }

    public CustomCheckBox(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
```
