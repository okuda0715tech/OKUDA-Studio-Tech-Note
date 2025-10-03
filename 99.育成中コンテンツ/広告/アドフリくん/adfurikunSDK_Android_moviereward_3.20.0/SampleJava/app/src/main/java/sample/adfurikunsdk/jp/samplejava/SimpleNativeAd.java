package sample.adfurikunsdk.jp.samplejava;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import jp.tjkapp.adfurikunsdk.moviereward.AdfurikunMovieError;
import jp.tjkapp.adfurikunsdk.moviereward.AdfurikunNativeAd;
import jp.tjkapp.adfurikunsdk.moviereward.AdfurikunNativeAdInfo;
import jp.tjkapp.adfurikunsdk.moviereward.AdfurikunNativeAdLoadListener;
import jp.tjkapp.adfurikunsdk.moviereward.AdfurikunNativeAdVideoListener;

/**
 * ネイティブ広告用サンプル
 */
public class SimpleNativeAd extends Activity {

    private TextView mTitle;
    private TextView mDescription;
    private FrameLayout mNativeAdViewFrame;
    private Button mLoad;
    private Button mPlay;

    /**
     * ネイティブ広告インスタンス
     */
    private AdfurikunNativeAd mNativeAd;
    /**
     * ネイティブ広告情報
     */
    private AdfurikunNativeAdInfo mNativeAdInfo;

    /**
     * 広告読み込みリスナー
     */
    private AdfurikunNativeAdLoadListener mLoadListener = new AdfurikunNativeAdLoadListener() {
        @Override
        public void onNativeAdLoadFinish(AdfurikunNativeAdInfo adInfo, String appId) {
            // 広告の準備完了直後に実行する処理を記述してください
            Utils.log("onNativeAdLoadFinish appId = " + appId);
            if (adInfo != null) {
                mNativeAdInfo = adInfo;
                String title = adInfo.getTitle();
                String description = adInfo.getDescription();
                mTitle.setText(title);
                mDescription.setText(description);
                mPlay.setEnabled(true);
            }
        }

        @Override
        public void onNativeAdLoadError(AdfurikunMovieError error, String appId) {
            // 広告の準備失敗直後に実行する処理を記述してください
            String errorType = "";
            if (error != null) {
                errorType = error.getErrorType().name();
            }
            Utils.log("onNativeAdLoadError appId = " + appId + ", errorType = " + errorType);
        }
    };

    /**
     * 広告再生リスナー
     */
    private AdfurikunNativeAdVideoListener mVideoListener = new AdfurikunNativeAdVideoListener() {
        @Override
        public void onNativeAdViewPlayStart(String appId) {
            // 広告の再生開始直後に実行する処理を記述してください
            Utils.log("onNativeAdViewPlayStart appId = " + appId);
        }

        @Override
        public void onNativeAdViewPlayFinish(String appId, boolean isVideoAd) {
            // 広告の再生が完了したら実行する処理を記述してください
            Utils.log("onNativeAdViewPlayFinish appId = " + appId + ", isVideoAd = " + isVideoAd);
        }

        @Override
        public void onNativeAdViewPlayFail(String appId, AdfurikunMovieError error) {
            // 広告の再生に失敗した時に実行する処理を記述してください
            String errorType = "";
            if (error != null) {
                errorType = error.getErrorType().name();
            }
            Utils.log("onNativeAdViewPlayFail appId = " + appId + ", errorType = " + errorType);
        }

        @Override
        public void onNativeAdViewClicked(String appId) {
            // 広告の画面をクリックした時に実行する処理を記述してください
            Utils.log("onNativeAdViewClicked appId = " + appId);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_ad);
        setTitle(getIntent().getStringExtra("title"));

        mDescription = findViewById(R.id.description);
        mTitle = findViewById(R.id.title);
        mNativeAdViewFrame = findViewById(R.id.native_ad_view_frame);
        mLoad = findViewById(R.id.load);
        mPlay = findViewById(R.id.play);

        // ボタンを押すと広告を読み込む
        mLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mNativeAd != null) {
                    mNativeAd.load();
                }
            }
        });

        // ボタンを押すと広告を表示する
        mPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mNativeAd != null) {
                    mNativeAd.play();
                }
            }
        });

        // ネイティブ広告のインスタンスを生成する
        // 広告枠IDとActivityを引数で渡してください
        mNativeAd = new AdfurikunNativeAd(this, AdfurikunConst.ADFURIKUN_NATIVE_AD_APPID,
                Utils.convertDpToPx(this, 320),
                Utils.convertDpToPx(this, 180));
        mNativeAd.setAdfurikunNativeAdLoadListener(mLoadListener);
        mNativeAd.setAdfurikunNativeAdVideoListener(mVideoListener);

        mNativeAdViewFrame.addView(mNativeAd.getNativeAdView());
    }

    @Override
    protected void onDestroy() {
        if (mNativeAd != null) {
            mNativeAd.onDestroy();
        }
        super.onDestroy();
    }
}
