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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import jp.tjkapp.adfurikunsdk.moviereward.AdfurikunMovieError;
import jp.tjkapp.adfurikunsdk.moviereward.AdfurikunMovieReward;
import jp.tjkapp.adfurikunsdk.moviereward.AdfurikunMovieRewardListener;
import jp.tjkapp.adfurikunsdk.moviereward.MovieRewardData;

/**
 * 動画リワード広告用サンプル
 */
public class SimpleMovieReward extends Activity {

	private static final String TEST_MODE_TEXT = "テストモードで配信しています<br/>詳しくは<a href=\"http://adfurikun.jp/adfurikun/adfully/\">こちら</a>をご覧ください";
	private SimpleDateFormat mLogDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.JAPAN);

	private LinearLayout mTestModeLayout;
	private TextView mMessageTxt;

	/**
	 * 動画リワード広告インスタンス
	 */
	private AdfurikunMovieReward mMovieReward;

	/**
	 * 動画広告リスナー
	 */
	private AdfurikunMovieRewardListener mListener = new AdfurikunMovieRewardListener() {
		@Override
		public void onPrepareSuccess(boolean isManualMode) {
			// 広告の準備完了直後に実行する処理を記述してください
			addLog("動画リワード広告の準備が完了しました。");
			// テストモードを通知
			mTestModeLayout.setVisibility(mMovieReward.isTestMode() ? View.VISIBLE : View.GONE);
		}

		@Override
		public void onPrepareFailure(AdfurikunMovieError error) {
			// 広告の準備失敗直後に実行する処理を記述してください
			addLog("動画リワード広告の準備が失敗しました。");
		}

		@Override
		public void onStartPlaying(MovieRewardData data) {
			// 広告の再生開始直後に実行する処理を記述してください
			Toast.makeText(SimpleMovieReward.this, "動画リワード広告の再生を開始しました。" +
					"(" + data.getAdnetworkKey() + ":" + data.getAdnetworkName() + ")", Toast.LENGTH_LONG).show();
			addLog("動画リワード広告の再生を開始しました。(" + data.getAdnetworkKey() + ":" + data.getAdnetworkName() + ")");
		}

		@Override
		public void onFinishedPlaying(MovieRewardData data) {
			// 広告の再生が完了した時に実行する処理を記述してください
			addLog("動画リワード広告の再生が完了しました。(" + data.getAdnetworkKey() + ":" + data.getAdnetworkName() + ")");
		}

		@Override
		public void onFailedPlaying(MovieRewardData data, AdfurikunMovieError error) {
			// 広告の再生に失敗した時に実行する処理を記述してください
			addLog("動画リワード広告の再生が中断しました。(" + data.getAdnetworkKey() + ":" + data.getAdnetworkName() + ")");
		}

		@Override
		public void onAdClose(MovieRewardData data) {
			// 広告の画面を閉じた時に実行する処理を記述してください
			addLog("動画リワード広告を閉じました。(" + data.getAdnetworkKey() + ":" + data.getAdnetworkName() + ")");
		}
	};


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simple_movie_reward);
		setTitle(getIntent().getStringExtra("title"));

		// テストモードの通知を準備
		mTestModeLayout = findViewById(R.id.test_mode_layout);
		Spanned text = Html.fromHtml(TEST_MODE_TEXT);
		TextView testMode = findViewById(R.id.test_mode_text);
		testMode.setText(text);
		testMode.setMovementMethod(LinkMovementMethod.getInstance());
		mMessageTxt = findViewById(R.id.message);

		// ボタンを押すと動画広告を読み込む
		findViewById(R.id.load_ad_btn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				addLog("動画リワード広告の読み込みを開始します。");
				if (mMovieReward != null) {
					mMovieReward.load();
				}
			}
		});

		// ボタンを押すと動画広告を表示する
		findViewById(R.id.show_ad_btn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showMovieReward();
			}
		});

		// 動画リワード広告のインスタンスを生成する
		// 広告枠IDとActivityを引数で渡してください
		mMovieReward = new AdfurikunMovieReward(AdfurikunConst.ADFURIKUN_REWARD_APPID, this, true);
		mMovieReward.setAdfurikunMovieRewardListener(mListener);

		addLog("動画リワード広告の準備を開始します。");
	}

	/**
	 * 広告を表示
	 */
	private void showMovieReward() {
		// テストモードを通知
		mTestModeLayout.setVisibility(mMovieReward.isTestMode() ? View.VISIBLE : View.GONE);
		//		mTestModeDescription.setText(mReward.getTestModeDescription());

		// 再生の前に動画の読み込みが完了しているか確認してください
		if (mMovieReward.isPrepared()) {
			new AlertDialog.Builder(this)
					.setTitle("広告の再生確認")
					.setMessage("広告を再生しますか？")
					.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (mMovieReward != null) {
								mMovieReward.play();
							}
						}
					})
					.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							addLog("動画リワード広告の再生をキャンセルしました。");
						}
					})
					.show();
		} else {
			addLog("動画リワード広告の準備中です。");
		}
	}

	@Override
	protected void onDestroy() {
		if (mMovieReward != null) {
			mMovieReward.onDestroy();
		}
		super.onDestroy();
	}

	private synchronized void addLog(String msg) {
		String log = mLogDateFormat.format(new Date()) + ":" + msg;
		mMessageTxt.setText(log + "\n" + mMessageTxt.getText());
	}
}
