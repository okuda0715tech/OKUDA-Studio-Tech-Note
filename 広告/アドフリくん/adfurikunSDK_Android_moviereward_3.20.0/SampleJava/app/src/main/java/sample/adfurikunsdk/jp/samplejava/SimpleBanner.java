package sample.adfurikunsdk.jp.samplejava;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import jp.tjkapp.adfurikunsdk.moviereward.AdfurikunMovieError;
import jp.tjkapp.adfurikunsdk.moviereward.AdfurikunBanner;
import jp.tjkapp.adfurikunsdk.moviereward.AdfurikunBannerAdInfo;
import jp.tjkapp.adfurikunsdk.moviereward.AdfurikunBannerLoadListener;
import jp.tjkapp.adfurikunsdk.moviereward.AdfurikunBannerVideoListener;

/**
 * バナー広告用サンプル
 */
public class SimpleBanner extends Activity {

    private TextView mTitle;
    private TextView mDescription;
    private FrameLayout mBannerViewFrame;
    private Button mLoad;
    private Button mPlay;

    /**
     * バナー広告インスタンス
     */
    private AdfurikunBanner mBanner;
    /**
     * バナー広告情報
     */
    private AdfurikunBannerAdInfo mBannerInfo;

    /**
     * 広告読み込みリスナー
     */
    private AdfurikunBannerLoadListener mLoadListener = new AdfurikunBannerLoadListener() {
        @Override
        public void onBannerLoadFinish(AdfurikunBannerAdInfo adInfo, String appId) {
            // 広告の準備完了直後に実行する処理を記述してください
            Utils.log("onBannerLoadFinish appId = " + appId);
            if (adInfo != null) {
                mBannerInfo = adInfo;
                String title = adInfo.getTitle();
                String description = adInfo.getDescription();
                mTitle.setText(title);
                mDescription.setText(description);
                mPlay.setEnabled(true);
            }
        }

        @Override
        public void onBannerLoadError(AdfurikunMovieError error, String appId) {
            // 広告の準備失敗直後に実行する処理を記述してください
            String errorType = "";
            if (error != null) {
                errorType = error.getErrorType().name();
            }
            Utils.log("onBannerLoadError appId = " + appId + ", errorType = " + errorType);
        }
    };

    /**
     * 広告再生リスナー
     */
    private AdfurikunBannerVideoListener mVideoListener = new AdfurikunBannerVideoListener() {
        @Override
        public void onBannerViewPlayStart(String appId) {
            // 広告の再生開始直後に実行する処理を記述してください
            Utils.log("onBannerViewPlayStart appId = " + appId);
        }

        @Override
        public void onBannerViewPlayFinish(String appId, boolean isVideoAd) {
            // 広告の再生が完了したら実行する処理を記述してください
            Utils.log("onBannerViewPlayFinish appId = " + appId + ", isVideoAd = " + isVideoAd);
        }

        @Override
        public void onBannerViewPlayFail(String appId, AdfurikunMovieError error) {
            // 広告の再生に失敗した時に実行する処理を記述してください
            String errorType = "";
            if (error != null) {
                errorType = error.getErrorType().name();
            }
            Utils.log("onBannerViewPlayFail appId = " + appId + ", errorType = " + errorType);
        }

        @Override
        public void onBannerViewClicked(String appId) {
            // 広告の画面をクリックした時に実行する処理を記述してください
            Utils.log("onBannerViewClicked appId = " + appId);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        setTitle(getIntent().getStringExtra("title"));

        mDescription = findViewById(R.id.description);
        mTitle = findViewById(R.id.title);
        mBannerViewFrame = findViewById(R.id.banner_view_frame);
        mLoad = findViewById(R.id.load);
        mPlay = findViewById(R.id.play);

        // ボタンを押すと広告を読み込む
        mLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBanner != null) {
                    mBanner.load();
                }
            }
        });

        // ボタンを押すと広告を表示する
        mPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBanner != null) {
                    mBanner.play();
                }
            }
        });

        // バナー広告のインスタンスを生成する
        // 広告枠IDとActivityを引数で渡してください
        mBanner = new AdfurikunBanner(this, AdfurikunConst.ADFURIKUN_BANNER_APPID,
                Utils.convertDpToPx(this, 320),
                Utils.convertDpToPx(this, 50));
        mBanner.setAdfurikunBannerLoadListener(mLoadListener);
        mBanner.setAdfurikunBannerVideoListener(mVideoListener);

        mBannerViewFrame.addView(mBanner.getBannerView());
    }

    @Override
    protected void onDestroy() {
        if (mBanner != null) {
            mBanner.onDestroy();
        }
        super.onDestroy();
    }
}
