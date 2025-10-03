package sample.adfurikunsdk.jp.samplejava;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import jp.tjkapp.adfurikunsdk.moviereward.AdfurikunBanner;
import jp.tjkapp.adfurikunsdk.moviereward.AdfurikunBannerAdInfo;
import jp.tjkapp.adfurikunsdk.moviereward.AdfurikunBannerLoadListener;
import jp.tjkapp.adfurikunsdk.moviereward.AdfurikunBannerVideoListener;
import jp.tjkapp.adfurikunsdk.moviereward.AdfurikunMovieError;

/**
 * バナー広告自動切り替え用サンプル
 */
public class SimpleBannerAutoRefresh extends Activity {

    private static final long AUTO_REFRESH_INTERVAL = 5 * 1000L;   // 60秒
    private static final int LOAD_RETRY_COUNT = 5;  // リトライ回数

    private TextView mTitle;
    private TextView mDescription;
    private FrameLayout mBannerViewFrame;
    private Button mAutoRefresh;

    /**
     * バナー広告インスタンス
     */
    private AdfurikunBanner mBanner;

    /**
     * リトライ回数
     */
    private int mLoadRetryCount = 0;

    /**
     * 広告読み込みリスナー
     */
    private AdfurikunBannerLoadListener mLoadListener = new AdfurikunBannerLoadListener() {
        @Override
        public void onBannerLoadFinish(AdfurikunBannerAdInfo adInfo, String appId) {
            // 広告の準備完了直後に実行する処理を記述してください
            Utils.log("onBannerLoadFinish appId = " + appId);
            if (adInfo != null) {
                Toast.makeText(SimpleBannerAutoRefresh.this, "広告を切り替えました", Toast.LENGTH_SHORT).show();
                String title = adInfo.getTitle();
                String description = adInfo.getDescription();
                mTitle.setText(title);
                mDescription.setText(description);
                mBannerViewFrame.setVisibility(View.VISIBLE);
                // バナーを表示する
                if (mBanner != null) {
                    mBanner.play();
                }
                // 次の広告読み込み開始する
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mBanner != null) {
                            mBanner.load();
                        }
                    }
                }, AUTO_REFRESH_INTERVAL);
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
            // 読み込み中ではなくエラータイプの場合にリトライ処理を行う
            if (error != null && error.getErrorType() != AdfurikunMovieError.MovieErrorType.LOADING) {
                if (mLoadRetryCount < LOAD_RETRY_COUNT) {
                    mBanner.load();
                }
                mLoadRetryCount++;
            }
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
        setContentView(R.layout.activity_banner_auto_refresh);
        setTitle(getIntent().getStringExtra("title"));

        mDescription = findViewById(R.id.description);
        mTitle = findViewById(R.id.title);
        mBannerViewFrame = findViewById(R.id.banner_view_frame);
        mAutoRefresh = findViewById(R.id.auto_refresh);

        // ボタンを押すと広告の自動切り替え開始
        mAutoRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBanner != null) {
                    mBanner.load();
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
