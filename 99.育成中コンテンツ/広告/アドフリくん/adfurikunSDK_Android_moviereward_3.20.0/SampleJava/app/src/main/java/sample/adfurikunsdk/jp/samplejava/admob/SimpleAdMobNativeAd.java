package sample.adfurikunsdk.jp.samplejava.admob;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;

import jp.tjkapp.adfurikunsdk.moviereward.AdfurikunAdMobNativeAd;
import sample.adfurikunsdk.jp.samplejava.R;
import sample.adfurikunsdk.jp.samplejava.Utils;

public class SimpleAdMobNativeAd extends Activity {

    private static final String AD_UNIT_ID = "";

    private FrameLayout mAdContainer;
    private AdLoader mAdLoader;
    private NativeAd mNativeAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admob_native_ad);

        mAdContainer = findViewById(R.id.ad_container);

        findViewById(R.id.load).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load();
            }
        });

        // AdMob初期化
        MobileAds.initialize(this);

        // AdMobの広告読み込み用インスタンスを生成
        mAdLoader = new AdLoader.Builder(this, AD_UNIT_ID)
                .forNativeAd(onNativeAdLoadedListener)
                .withAdListener(adListener)
                .build();
    }

    /**
     * 広告を読み込み
     */
    private void load() {
        if (mAdLoader != null && !mAdLoader.isLoading()) {
            int width = Utils.convertDpToPx(this, 320);
            int height = Utils.convertDpToPx(this, 180);
            Bundle customEventExtras = new Bundle();
            customEventExtras.putInt(AdfurikunAdMobNativeAd.CUSTOM_EVENT_KEY_WIDTH, width);
            customEventExtras.putInt(AdfurikunAdMobNativeAd.CUSTOM_EVENT_KEY_HEIGHT, height);
            AdRequest adRequest = new AdRequest.Builder()
                    .addCustomEventExtrasBundle(AdfurikunAdMobNativeAd.class, customEventExtras)
                    .build();
            mAdLoader.loadAd(adRequest);
        }
    }

    private NativeAd.OnNativeAdLoadedListener onNativeAdLoadedListener = new NativeAd.OnNativeAdLoadedListener() {
        @Override
        public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {
            // 広告読み込み完了時に呼ばれる。
            // NativeAd利用して広告を組み立てる。
            // https://developers.google.com/admob/android/native/templates
            Toast.makeText(SimpleAdMobNativeAd.this, "読み込み成功", Toast.LENGTH_SHORT).show();
            setupAdView(nativeAd);
        }
    };

    /**
     * 広告ビューをセットアップ
     *
     * @param nativeAd
     */
    private void setupAdView(NativeAd nativeAd) {
        if (mNativeAd != null) {
            mNativeAd.destroy();
            mNativeAd = nativeAd;
        }

        NativeAdView adView = (NativeAdView) LayoutInflater.from(this).inflate(R.layout.admob_custom_view, null);

        adView.setMediaView(adView.findViewById(R.id.ad_media));
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // メディアサイズを設定する
        int width = Utils.convertDpToPx(this, 320);
        int height = Utils.convertDpToPx(this, 180);
        adView.getMediaView().setLayoutParams(new LinearLayout.LayoutParams(width, height));

        String headline = nativeAd.getHeadline();
        if (headline != null) {
            ((TextView) adView.getHeadlineView()).setText(headline);
        }
        String body = nativeAd.getBody();
        if (body != null) {
            ((TextView) adView.getBodyView()).setText(body);
        }

        String callToAction = nativeAd.getCallToAction();
        if (callToAction != null) {
            ((TextView) adView.getCallToActionView()).setText(callToAction);
            adView.getCallToActionView().setVisibility(View.VISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.GONE);
        }

        Drawable icon = nativeAd.getIcon().getDrawable();
        if (icon != null) {
            ((ImageView) adView.getIconView()).setImageDrawable(icon);
            adView.getIconView().setVisibility(View.VISIBLE);
        } else {
            adView.getIconView().setVisibility(View.GONE);
        }

        String price = nativeAd.getPrice();
        if (price != null) {
            ((TextView) adView.getPriceView()).setText(price);
        }

        Double starRating = nativeAd.getStarRating();
        if (starRating != null) {
            ((RatingBar) adView.getStarRatingView()).setRating(starRating.floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        } else {
            adView.getStarRatingView().setVisibility(View.GONE);
        }

        String store = nativeAd.getStore();
        if (store != null) {
            ((TextView) adView.getStoreView()).setText(store);
        }

        String advertiser = nativeAd.getAdvertiser();
        if (advertiser != null) {
            ((TextView) adView.getAdvertiserView()).setText(advertiser);
        }

        adView.setNativeAd(nativeAd);

        VideoController videoController = nativeAd.getMediaContent().getVideoController();
        if (videoController != null && videoController.hasVideoContent()) {
            videoController.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoStart() {
                    super.onVideoStart();
                    // 広告再生開始時に呼ばれる。
                    Toast.makeText(SimpleAdMobNativeAd.this, "再生開始", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onVideoEnd() {
                    super.onVideoEnd();
                    // 広告再生終了時に呼ばれる。
                    Toast.makeText(SimpleAdMobNativeAd.this, "再生完了", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onVideoPlay() {
                    super.onVideoPlay();
                }

                @Override
                public void onVideoPause() {
                    super.onVideoPause();
                }
            });
        }

        mAdContainer.removeAllViews();
        mAdContainer.addView(adView);
    }

    private AdListener adListener = new AdListener() {
        @Override
        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
            super.onAdFailedToLoad(loadAdError);
            // 広告読み込み失敗時に呼ばれる。
            Toast.makeText(SimpleAdMobNativeAd.this, "読み込み失敗", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAdOpened() {
            super.onAdOpened();
            // 広告をクリックした時に呼ばれる。
            Toast.makeText(SimpleAdMobNativeAd.this, "広告クリック", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 広告を破棄
        if (mNativeAd != null) {
            mNativeAd.destroy();
        }
    }
}
