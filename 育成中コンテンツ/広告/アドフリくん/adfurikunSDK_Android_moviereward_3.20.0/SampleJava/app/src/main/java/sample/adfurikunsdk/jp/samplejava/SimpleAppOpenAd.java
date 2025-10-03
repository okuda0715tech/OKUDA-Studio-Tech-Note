package sample.adfurikunsdk.jp.samplejava;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import jp.tjkapp.adfurikunsdk.moviereward.AdfurikunAppOpenAd;
import jp.tjkapp.adfurikunsdk.moviereward.AdfurikunAppOpenAdListener;
import jp.tjkapp.adfurikunsdk.moviereward.AdfurikunMovieError;
import jp.tjkapp.adfurikunsdk.moviereward.AppOpenAdData;

public class SimpleAppOpenAd extends Activity {

    /* アプリ起動時広告インスタンス */
    private AdfurikunAppOpenAd mAppOpenAd;

    /* 広告リスナー */
    private AdfurikunAppOpenAdListener mListener = new AdfurikunAppOpenAdListener() {
        @Override
        public void onPrepareSuccess(@Nullable String appId) {
            Toast.makeText(getApplicationContext(),
                    "広告の準備が完了しました。", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onPrepareFailure(@Nullable String appId, @Nullable AdfurikunMovieError error) {
            int errorCode = error != null ? error.getErrorCode() : 0;
            Toast.makeText(getApplicationContext(),
                    "広告の準備が失敗しました。(" + errorCode + ")", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onStartPlaying(@NonNull AppOpenAdData data) {
            Toast.makeText(getApplicationContext(),
                    "広告の再生を開始しました。(" + data.getAdnetworkKey() + ":" + data.getAdnetworkName() + ")", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailedPlaying(@NonNull AppOpenAdData data, @Nullable AdfurikunMovieError error) {
            Toast.makeText(getApplicationContext(),
                    "広告の再生が中断しました。(" + data.getAdnetworkKey() + ":" + data.getAdnetworkName() + ")", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onAdClose(@NonNull AppOpenAdData data) {
            Toast.makeText(getApplicationContext(),
                    "広告を閉じました。(" + data.getAdnetworkKey() + ":" + data.getAdnetworkName() + ")", Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_open_ad);
        setTitle(getIntent().getStringExtra("title"));

        // ボタンを押すと広告を読み込む
        findViewById(R.id.load_ad_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAppOpenAd != null) {
                    mAppOpenAd.load(3000);
                }
            }
        });

        // ボタンを押すと広告を表示する
        findViewById(R.id.show_ad_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAppOpenAd != null) {
                    if (mAppOpenAd.isPrepared()) {
                        mAppOpenAd.play(SimpleAppOpenAd.this);
                    } else {
                        Toast.makeText(getApplicationContext(), "広告の準備中です。", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        mAppOpenAd = new AdfurikunAppOpenAd(AdfurikunConst.ADFURIKUN_APP_OPEN_AD_APPID, this);
        mAppOpenAd.setAdfurikunAppOpenAdListener(mListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAppOpenAd != null) {
            mAppOpenAd.destroy();
        }
    }
}
