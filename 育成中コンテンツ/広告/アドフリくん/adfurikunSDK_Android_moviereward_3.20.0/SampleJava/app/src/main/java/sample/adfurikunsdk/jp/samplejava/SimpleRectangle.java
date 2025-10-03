package sample.adfurikunsdk.jp.samplejava;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import jp.tjkapp.adfurikunsdk.moviereward.AdfurikunMovieError;
import jp.tjkapp.adfurikunsdk.moviereward.AdfurikunRectangle;
import jp.tjkapp.adfurikunsdk.moviereward.AdfurikunRectangleAdInfo;
import jp.tjkapp.adfurikunsdk.moviereward.AdfurikunRectangleLoadListener;
import jp.tjkapp.adfurikunsdk.moviereward.AdfurikunRectangleVideoListener;

/**
 * レクタングル広告用サンプル
 */
public class SimpleRectangle extends Activity {

    private TextView mTitle;
    private TextView mDescription;
    private FrameLayout mRectangleViewFrame;
    private Button mLoad;
    private Button mPlay;

    /**
     * レクタングル広告インスタンス
     */
    private AdfurikunRectangle mRectangle;
    /**
     * レクタングル広告情報
     */
    private AdfurikunRectangleAdInfo mRectangleInfo;

    /**
     * 広告読み込みリスナー
     */
    private AdfurikunRectangleLoadListener mLoadListener = new AdfurikunRectangleLoadListener() {
        @Override
        public void onRectangleLoadFinish(AdfurikunRectangleAdInfo adInfo, String appId) {
            // 広告の準備完了直後に実行する処理を記述してください
            Utils.log("onRectangleLoadFinish appId = " + appId);
            if (adInfo != null) {
                mRectangleInfo = adInfo;
                String title = adInfo.getTitle();
                String description = adInfo.getDescription();
                mTitle.setText(title);
                mDescription.setText(description);
                mPlay.setEnabled(true);
            }
        }

        @Override
        public void onRectangleLoadError(AdfurikunMovieError error, String appId) {
            // 広告の準備失敗直後に実行する処理を記述してください
            String errorType = "";
            if (error != null) {
                errorType = error.getErrorType().name();
            }
            Utils.log("onRectangleLoadError appId = " + appId + ", errorType = " + errorType);
        }
    };

    /**
     * 広告再生リスナー
     */
    private AdfurikunRectangleVideoListener mVideoListener = new AdfurikunRectangleVideoListener() {
        @Override
        public void onRectangleViewPlayStart(String appId) {
            // 広告の再生開始直後に実行する処理を記述してください
            Utils.log("onRectangleViewPlayStart appId = " + appId);
        }

        @Override
        public void onRectangleViewPlayFinish(String appId, boolean isVideoAd) {
            // 広告の再生が完了したら実行する処理を記述してください
            Utils.log("onRectangleViewPlayFinish appId = " + appId + ", isVideoAd = " + isVideoAd);
        }

        @Override
        public void onRectangleViewPlayFail(String appId, AdfurikunMovieError error) {
            // 広告の再生に失敗した時に実行する処理を記述してください
            String errorType = "";
            if (error != null) {
                errorType = error.getErrorType().name();
            }
            Utils.log("onRectangleViewPlayFail appId = " + appId + ", errorType = " + errorType);
        }

        @Override
        public void onRectangleViewClicked(String appId) {
            // 広告の画面をクリックした時に実行する処理を記述してください
            Utils.log("onRectangleViewClicked appId = " + appId);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rectangle);
        setTitle(getIntent().getStringExtra("title"));

        mDescription = findViewById(R.id.description);
        mTitle = findViewById(R.id.title);
        mRectangleViewFrame = findViewById(R.id.rectangle_view_frame);
        mLoad = findViewById(R.id.load);
        mPlay = findViewById(R.id.play);

        // ボタンを押すと広告を読み込む
        mLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRectangle != null) {
                    mRectangle.load();
                }
            }
        });

        // ボタンを押すと広告を表示する
        mPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRectangle != null) {
                    mRectangle.play();
                }
            }
        });

        // レクタングル広告のインスタンスを生成する
        // 広告枠IDとActivityを引数で渡してください
        mRectangle = new AdfurikunRectangle(this, AdfurikunConst.ADFURIKUN_RECTANGLE_APPID,
                Utils.convertDpToPx(this, 300),
                Utils.convertDpToPx(this, 250));
        mRectangle.setAdfurikunRectangleLoadListener(mLoadListener);
        mRectangle.setAdfurikunRectangleVideoListener(mVideoListener);

        mRectangleViewFrame.addView(mRectangle.getRectangleView());
    }

    @Override
    protected void onDestroy() {
        if (mRectangle != null) {
            mRectangle.onDestroy();
        }
        super.onDestroy();
    }
}
