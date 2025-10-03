package jp.glossom.sample.samplekotlin

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import jp.tjkapp.adfurikunsdk.moviereward.AdfurikunMovieError
import jp.tjkapp.adfurikunsdk.moviereward.AdfurikunMovieReward
import jp.tjkapp.adfurikunsdk.moviereward.AdfurikunMovieRewardListener
import jp.tjkapp.adfurikunsdk.moviereward.MovieRewardData

/**
 * 動画リワード広告用サンプル
 */
class SimpleMovieReward : Activity() {

    private lateinit var mTestModeLayout: LinearLayout
    private lateinit var mMessageTxt: TextView

    /** 動画リワード広告インスタンス */
    private var mMovieReward: AdfurikunMovieReward? = null

    /** 動画広告リスナー */
    private val mListener = object : AdfurikunMovieRewardListener {
        override fun onPrepareSuccess(isManualMode: Boolean) {
            // 広告の準備完了直後に実行する処理を記述してください
            addLog("動画リワード広告の準備が完了しました。")
            // テストモードを通知
            mTestModeLayout.visibility = if (mMovieReward?.isTestMode == true) View.VISIBLE else View.GONE
        }

        override fun onPrepareFailure(error: AdfurikunMovieError?) {
            // 広告の準備失敗直後に実行する処理を記述してください
            addLog("動画リワード広告の準備が失敗しました。")
        }

        override fun onStartPlaying(data: MovieRewardData) {
            // 動画の再生開始直後に実行する処理を記述してください
            Toast.makeText(applicationContext, "動画リワード広告の再生を開始しました。(${data.adnetworkKey}:${data.adnetworkName})", Toast.LENGTH_LONG).show()
            addLog("動画リワード広告の再生を開始しました。(${data.adnetworkKey}:${data.adnetworkName})")
        }

        override fun onFinishedPlaying(data: MovieRewardData) {
            // 広告の再生が完了した時に実行する処理を記述してください
            addLog("動画リワード広告の再生が完了しました。(${data.adnetworkKey}:${data.adnetworkName})")
        }

        override fun onFailedPlaying(data: MovieRewardData, error: AdfurikunMovieError?) {
            // 動画の再生に失敗した時に実行する処理を記述してください
            addLog("動画リワード広告の再生が中断しました。(${data.adnetworkKey}:${data.adnetworkName})")
        }

        override fun onAdClose(data: MovieRewardData) {
            // 動画の画面を閉じた時に実行する処理を記述してください
            addLog("動画リワード広告を閉じました。(${data.adnetworkKey}:${data.adnetworkName})")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.simple_movie_reward)
        title = intent.getStringExtra("title")

        // テストモードの通知を準備
        mTestModeLayout = findViewById(R.id.test_mode_layout)
        val text = Html.fromHtml(TEST_MODE_TEXT)
        val testMode = findViewById<TextView>(R.id.test_mode_text)
        testMode.text = text
        testMode.movementMethod = LinkMovementMethod.getInstance()
        mMessageTxt = findViewById(R.id.message)

        // ボタンを押すと動画広告を読み込む
        findViewById<Button>(R.id.load_ad_btn).setOnClickListener {
            addLog("動画リワード広告の読み込みを開始します。")
            mMovieReward?.load()
        }

        // ボタンを押すと動画広告を表示する
        findViewById<Button>(R.id.show_ad_btn).setOnClickListener {
            showMovieReward()
        }

        // 動画リワード広告のインスタンスを生成する
        // 広告枠IDとActivityを引数で渡してください
        mMovieReward = AdfurikunMovieReward(AdfurikunConst.ADFURIKUN_REWARD_APPID, this)
        mMovieReward?.setAdfurikunMovieRewardListener(mListener)

        addLog("動画リワード広告の準備を開始します。")
    }

    /**
     * 動画広告を表示
     */
    private fun showMovieReward() {
        // テストモードを通知
        mTestModeLayout.visibility = if (mMovieReward?.isTestMode == true) View.VISIBLE else View.GONE
//        mTestModeDescription.setText(mReward.getTestModeDescription());

        // 再生の前に動画の読み込みが完了しているか確認してください
        if (mMovieReward?.isPrepared == true) {
            AlertDialog.Builder(this@SimpleMovieReward)
                    .setTitle("動画の再生確認")
                    .setMessage("動画を再生しますか？")
                    .setPositiveButton(android.R.string.ok) { dialog, which ->
                        mMovieReward?.play()
                    }
                    .setNegativeButton(android.R.string.cancel) { dialog, which ->
                        addLog("動画リワード広告の再生をキャンセルしました。")
                    }
                    .show()
        } else {
            addLog("動画リワード広告の準備中です。")
        }
    }

    override fun onDestroy() {
        mMovieReward?.onDestroy()
        super.onDestroy()
    }

    @Synchronized
    private fun addLog(msg: String) {
        val log = "${mLogDateFormat.format(Date())}:$msg"
        mMessageTxt.text = "$log\n${mMessageTxt.text}"
    }

    companion object {
        private const val TEST_MODE_TEXT = "テストモードで配信しています<br/>詳しくは<a href=\"http://adfurikun.jp/adfurikun/adfully/\">こちら</a>をご覧ください"
        private val mLogDateFormat = SimpleDateFormat("HH:mm:ss", Locale.JAPAN)
    }
}
