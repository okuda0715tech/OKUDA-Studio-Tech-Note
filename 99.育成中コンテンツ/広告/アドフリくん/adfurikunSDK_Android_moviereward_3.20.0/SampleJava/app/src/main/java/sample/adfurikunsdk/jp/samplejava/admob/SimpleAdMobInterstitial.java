package sample.adfurikunsdk.jp.samplejava.admob;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import sample.adfurikunsdk.jp.samplejava.R;

public class SimpleAdMobInterstitial extends Activity {

    private static final String AD_UNIT_ID = "";

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admob_interstitial);

        findViewById(R.id.load).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load();
            }
        });

        findViewById(R.id.show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show();
            }
        });

        // AdMob初期化
        MobileAds.initialize(this);
    }

    /**
     * 広告を読み込み
     */
    private void load() {
        if (mInterstitialAd == null) {
            InterstitialAd.load(this, AD_UNIT_ID, new AdManagerAdRequest.Builder().build(), loadListener);
        }
    }

    /**
     * 広告を表示
     */
    private void show() {
        if (mInterstitialAd != null) {
            mInterstitialAd.setFullScreenContentCallback(playListener);
            mInterstitialAd.show(this);
        }
    }

    /**
     * 広告読み込み用リスナー
     */
    private InterstitialAdLoadCallback loadListener = new InterstitialAdLoadCallback() {
        @Override
        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
            super.onAdLoaded(interstitialAd);
            // 広告読み込み完了時に呼ばれる。
            Toast.makeText(SimpleAdMobInterstitial.this, "読み込み成功", Toast.LENGTH_SHORT).show();
            mInterstitialAd = interstitialAd;
        }

        @Override
        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
            super.onAdFailedToLoad(loadAdError);
            // 広告読み込み失敗時に呼ばれる。
            Toast.makeText(SimpleAdMobInterstitial.this, "読み込み失敗", Toast.LENGTH_SHORT).show();
            mInterstitialAd = null;
        }
    };

    /**
     * 広告表示用リスナー
     */
    private FullScreenContentCallback playListener = new FullScreenContentCallback() {
        @Override
        public void onAdShowedFullScreenContent() {
            super.onAdShowedFullScreenContent();
            // 広告再生開始時に呼ばれる。
            Toast.makeText(SimpleAdMobInterstitial.this, "再生開始", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAdDismissedFullScreenContent() {
            super.onAdDismissedFullScreenContent();
            // 広告を閉じた時に呼ばれる。
            Toast.makeText(SimpleAdMobInterstitial.this, "広告閉じる", Toast.LENGTH_SHORT).show();
            mInterstitialAd = null;
        }

        @Override
        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
            super.onAdFailedToShowFullScreenContent(adError);
            // 広告再生失敗時に呼ばれる。
            Toast.makeText(SimpleAdMobInterstitial.this, "再生失敗", Toast.LENGTH_SHORT).show();
            mInterstitialAd = null;
        }

        @Override
        public void onAdImpression() {
            super.onAdImpression();
        }
    };
}
