package sample.adfurikunsdk.jp.samplejava.admob;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import sample.adfurikunsdk.jp.samplejava.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.admanager.AdManagerAdView;

public class SimpleAdMobBanner extends Activity {

    private static final String AD_UNIT_ID = "";

    private FrameLayout mAdContainer;
    private AdManagerAdView mAdView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admob_banner);

        mAdContainer = findViewById(R.id.ad_container);

        findViewById(R.id.load).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAdView != null) {
                    mAdView.loadAd(new AdManagerAdRequest.Builder().build());
                }
            }
        });

        // AdMob初期化
        MobileAds.initialize(this);

        // AdMobのバナー表示用ビューを生成
        mAdView = new AdManagerAdView(this);
        mAdView.setAdSizes(AdSize.BANNER);
        mAdView.setAdUnitId(AD_UNIT_ID);
        mAdView.setAdListener(mAdListener);
        mAdContainer.addView(mAdView);
    }

    /**
     * 広告読み込み・表示用リスナー
     */
    private AdListener mAdListener = new AdListener() {
        @Override
        public void onAdLoaded() {
            super.onAdLoaded();
            // 広告読み込み完了時に呼ばれる。
            Toast.makeText(SimpleAdMobBanner.this, "読み込み成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
            super.onAdFailedToLoad(loadAdError);
            // 広告読み込み失敗時に呼ばれる。
            Toast.makeText(SimpleAdMobBanner.this, "読み込み失敗", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAdImpression() {
            super.onAdImpression();
            // 広告表示時に呼ばれる。
            Toast.makeText(SimpleAdMobBanner.this, "広告表示", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAdOpened() {
            super.onAdOpened();
            // 広告をクリックした時に呼ばれる。
            Toast.makeText(SimpleAdMobBanner.this, "広告クリック", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        // 広告を再開
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    protected void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        // 広告を一時停止
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        // 広告を破棄
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
}
