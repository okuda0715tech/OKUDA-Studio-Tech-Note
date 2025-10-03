package sample.adfurikunsdk.jp.samplejava;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import jp.tjkapp.adfurikunsdk.moviereward.AdfurikunInter;
import jp.tjkapp.adfurikunsdk.moviereward.AdfurikunInterListener;
import jp.tjkapp.adfurikunsdk.moviereward.AdfurikunMovieError;
import jp.tjkapp.adfurikunsdk.moviereward.InterData;

/**
 * インタースティシャル広告用サンプル
 */
public class SimpleInterstitial extends Activity {

    private static final String TEST_MODE_TEXT = "テストモードで配信しています<br/>詳しくは<a href=\"http://adfurikun.jp/adfurikun/adfully/\">こちら</a>をご覧ください";
    private SimpleDateFormat mLogDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.JAPAN);

    private LinearLayout mTestModeLayout;
    private TextView mMessageTxt;

    /**
     * インタースティシャル広告インスタンス
     */
    private AdfurikunInter mInterstitial;

    /**
     * 広告リスナー
     */
    private AdfurikunInterListener mListener = new AdfurikunInterListener() {
        @Override
        public void onPrepareSuccess(boolean isManualMode) {
            // 広告の準備完了直後に実行する処理を記述してください
            addLog("インタースティシャル広告の準備が完了しました。");
            // テストモードを通知
            mTestModeLayout.setVisibility(mInterstitial.isTestMode() ? View.VISIBLE : View.GONE);
        }

        @Override
        public void onPrepareFailure(AdfurikunMovieError error) {
            // 広告の準備失敗直後に実行する処理を記述してください
            addLog("インタースティシャル広告の準備が失敗しました。");
        }

        @Override
        public void onStartShowing(@NotNull InterData data) {
            // 静止画広告の再生開始直後に実行する処理を記述してください
            Toast.makeText(SimpleInterstitial.this, "インタースティシャル広告の再生を開始しました。" +
                    "(" + data.getAdnetworkKey() + ":" + data.getAdnetworkName() + ")", Toast.LENGTH_LONG).show();
            addLog("インタースティシャル広告の再生を開始しました。(" + data.getAdnetworkKey() + ":" + data.getAdnetworkName() + ")");
        }

        @Override
        public void onStartPlaying(InterData data) {
            // 動画広告の再生開始直後に実行する処理を記述してください
            Toast.makeText(SimpleInterstitial.this, "インタースティシャル広告の再生を開始しました。" +
                    "(" + data.getAdnetworkKey() + ":" + data.getAdnetworkName() + ")", Toast.LENGTH_LONG).show();
            addLog("インタースティシャル広告の再生を開始しました。(" + data.getAdnetworkKey() + ":" + data.getAdnetworkName() + ")");
        }

        @Override
        public void onFinishedPlaying(InterData data) {
            // 動画広告の再生が完了した時に実行する処理を記述してください
            addLog("インタースティシャル広告の再生が完了しました。(" + data.getAdnetworkKey() + ":" + data.getAdnetworkName() + ")");
        }

        @Override
        public void onFailedPlaying(InterData data, AdfurikunMovieError error) {
            // 広告の再生に失敗した時に実行する処理を記述してください
            addLog("インタースティシャル広告の再生が中断しました。(" + data.getAdnetworkKey() + ":" + data.getAdnetworkName() + ")");
        }

        @Override
        public void onAdClose(InterData data) {
            // 広告の画面を閉じた時に実行する処理を記述してください
            addLog("インタースティシャル広告を閉じました。(" + data.getAdnetworkKey() + ":" + data.getAdnetworkName() + ")");
        }

        @Override
        public void onClick(@NotNull InterData data) {
            // 静止画広告をクリックした時に実行する処理を記述してください
            addLog("インタースティシャル広告クリックを閉じました。(" + data.getAdnetworkKey() + ":" + data.getAdnetworkName() + ")");
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_interstitial);
        setTitle(getIntent().getStringExtra("title"));

        // テストモードの通知を準備
        mTestModeLayout = findViewById(R.id.test_mode_layout);
        Spanned text = Html.fromHtml(TEST_MODE_TEXT);
        TextView testMode = findViewById(R.id.test_mode_text);
        testMode.setText(text);
        testMode.setMovementMethod(LinkMovementMethod.getInstance());
        mMessageTxt = findViewById(R.id.message);

        // ボタンを押すと広告を読み込む
        findViewById(R.id.load_ad_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addLog("インタースティシャル広告の読み込みを開始します。");
                if (mInterstitial != null) {
                    mInterstitial.load();
                }
            }
        });

        // ボタンを押すと動画広告を表示する
        findViewById(R.id.show_ad_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMovieInterstitial();
            }
        });

        // インタースティシャル広告のインスタンスを生成する
        // 広告枠IDとActivityを引数で渡してください
        mInterstitial = new AdfurikunInter(AdfurikunConst.ADFURIKUN_INTER_APPID, this, true);
        mInterstitial.setAdfurikunInterListener(mListener);

        addLog("インタースティシャル広告の準備を開始します。");
    }

    /**
     * 広告を表示
     */
    private void showMovieInterstitial() {
        // テストモードを通知
        mTestModeLayout.setVisibility(mInterstitial.isTestMode() ? View.VISIBLE : View.GONE);
        //		mTestModeDescription.setText(mReward.getTestModeDescription());

        // 再生の前に広告の読み込みが完了しているか確認してください
        if (mInterstitial.isPrepared()) {
            new AlertDialog.Builder(this)
                    .setTitle("広告の再生確認")
                    .setMessage("広告を再生しますか？")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (mInterstitial != null) {
                                mInterstitial.play();
                            }
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            addLog("インタースティシャル広告の再生をキャンセルしました。");
                        }
                    })
                    .show();
        } else {
            addLog("インタースティシャル広告の準備中です。");
        }
    }

    @Override
    protected void onDestroy() {
        if (mInterstitial != null) {
            mInterstitial.onDestroy();
        }
        super.onDestroy();
    }

    private synchronized void addLog(String msg) {
        String log = mLogDateFormat.format(new Date()) + ":" + msg;
        mMessageTxt.setText(log + "\n" + mMessageTxt.getText());
    }
}
