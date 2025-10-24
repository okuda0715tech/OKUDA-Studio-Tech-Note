<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [Viewの表示_非表示](#viewの表示_非表示)
	- [円形表示/非表示アニメーション](#円形表示非表示アニメーション)

<!-- /TOC -->

# Viewの表示_非表示

## 円形表示/非表示アニメーション

**AnimationUtils.java**

```Java
public static void showAnimation(View view) {
    // get the center for the clipping circle
    int cx = view.getWidth() / 2;
    int cy = view.getHeight() / 2;

    // get the final radius for the clipping circle
    float finalRadius = (float) Math.hypot(cx, cy);

    // create the animator for this view (the start radius is zero)
    Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0f, finalRadius);
    // make the view visible and start the animation
    view.setVisibility(View.VISIBLE);

    anim.start();
}

public static void hideAnimation(final View view){
    // get the center for the clipping circle
    int cx = view.getWidth() / 2;
    int cy = view.getHeight() / 2;

    // get the initial radius for the clipping circle
    float initialRadius = (float) Math.hypot(cx, cy);

    // create the animation (the final radius is zero)
    Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, 0f);

    // make the view invisible when the animation is done
    anim.addListener(new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            view.setVisibility(View.GONE);
        }
    });

    // start the animation
    anim.start();
}
```

**SampleInvoker.java**

```Java
AnimationUtils.showAnimation(mRepaymentPeriodTextView);
AnimationUtils.hideAnimation(mRepaymentPeriodTextView);
```




