package jp.glossom.sample.samplekotlin

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import jp.tjkapp.adfurikunsdk.moviereward.*

/**
 * アプリ起動時広告用サンプル
 */
class SimpleAppOpenAd : Activity() {

    /** アプリ起動時広告インスタンス */
    private var mAppOpenAd: AdfurikunAppOpenAd? = null

    /** 広告リスナー */
    private val mListener = object : AdfurikunAppOpenAdListener {
        override fun onPrepareSuccess(appId: String?) {
            Toast.makeText(applicationContext, "広告の準備が完了しました。", Toast.LENGTH_LONG).show()
        }

        override fun onPrepareFailure(appId: String?, error: AdfurikunMovieError?) {
            Toast.makeText(applicationContext, "広告の準備が失敗しました。(${error?.errorCode})", Toast.LENGTH_LONG).show()
        }

        override fun onStartPlaying(data: AppOpenAdData) {
            Toast.makeText(applicationContext, "広告の再生を開始しました。(${data.adnetworkKey}:${data.adnetworkName})", Toast.LENGTH_LONG).show()
        }

        override fun onFailedPlaying(data: AppOpenAdData, error: AdfurikunMovieError?) {
            Toast.makeText(applicationContext, "広告の再生が中断しました。(${data.adnetworkKey}:${data.adnetworkName})", Toast.LENGTH_LONG).show()
        }

        override fun onAdClose(data: AppOpenAdData) {
            Toast.makeText(applicationContext, "広告を閉じました。(${data.adnetworkKey}:${data.adnetworkName})", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_open_ad)
        title = intent.getStringExtra("title")

        // ボタンを押すと広告を読み込む
        findViewById<Button>(R.id.load_ad_btn).setOnClickListener {
            mAppOpenAd?.load(3000)
        }

        // ボタンを押すと広告を表示する
        findViewById<Button>(R.id.show_ad_btn).setOnClickListener {
            mAppOpenAd?.let {
                if (it.isPrepared) {
                    it.play(this)
                } else {
                    Toast.makeText(applicationContext, "広告の準備中です。", Toast.LENGTH_LONG).show()
                }
            }
        }

        // アプリ起動時広告のインスタンスを生成する
        mAppOpenAd = AdfurikunAppOpenAd(AdfurikunConst.ADFURIKUN_APP_OPEN_AD_APPID, this)
        mAppOpenAd?.setAdfurikunAppOpenAdListener(mListener)
    }

    override fun onDestroy() {
        mAppOpenAd?.destroy()
        super.onDestroy()
    }
}
