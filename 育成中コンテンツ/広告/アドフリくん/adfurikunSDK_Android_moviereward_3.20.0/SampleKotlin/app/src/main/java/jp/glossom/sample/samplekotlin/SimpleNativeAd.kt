package jp.glossom.sample.samplekotlin

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import jp.tjkapp.adfurikunsdk.moviereward.*

/**
 * ネイティブ広告用サンプル
 */
class SimpleNativeAd : Activity() {

    private lateinit var mTitle: TextView
    private lateinit var mDescription: TextView
    private lateinit var mNativeAdViewFrame: FrameLayout
    private lateinit var mLoad: Button
    private lateinit var mPlay: Button

    /** ネイティブ広告インスタンス */
    private var mNativeAd: AdfurikunNativeAd? = null
    /** ネイティブ広告情報 */
    private var mNativeAdInfo: AdfurikunNativeAdInfo? = null

    /** 広告読み込みリスナー */
    private val mLoadListener = object : AdfurikunNativeAdLoadListener {
        override fun onNativeAdLoadFinish(adInfo: AdfurikunNativeAdInfo?, appId: String?) {
            // 広告の準備完了直後に実行する処理を記述してください
            Utils.log("onNativeAdLoadFinish appId = $appId")
            adInfo?.let {
                mNativeAdInfo = it
                mTitle.text = it.title
                mDescription.text = it.description
                mPlay.isEnabled = true
            }
        }

        override fun onNativeAdLoadError(error: AdfurikunMovieError?, appId: String?) {
            // 広告の準備失敗直後に実行する処理を記述してください
            Utils.log("onNativeAdLoadError appId = $appId, errorType = ${error?.errorType?.name}")
        }
    }

    /** 広告再生リスナー */
    private val mVideoListener = object : AdfurikunNativeAdVideoListener {
        override fun onNativeAdViewPlayStart(appId: String?) {
            // 広告の再生開始直後に実行する処理を記述してください
            Utils.log("onNativeAdViewPlayStart appId = $appId")
            mPlay.isEnabled = false
        }

        override fun onNativeAdViewPlayFinish(appId: String?, isVideoAd: Boolean) {
            // 広告の再生に失敗した時に実行する処理を記述してください
            Utils.log("onNativeAdViewPlayFinish appId = $appId, isVideoAd = $isVideoAd")
        }

        override fun onNativeAdViewPlayFail(appId: String?, error: AdfurikunMovieError?) {
            // 広告の再生が完了したら実行する処理を記述してください
            Utils.log("onNativeAdViewPlayFail appId = $appId, errorType = ${error?.errorType?.name}")
        }

        override fun onNativeAdViewClicked(appId: String?) {
            // 広告の画面をクリックした時に実行する処理を記述してください
            Utils.log("onNativeAdViewClicked appId = $appId")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_native_ad)
        title = intent.getStringExtra("title")

        mTitle = findViewById(R.id.title)
        mDescription = findViewById(R.id.description)
        mNativeAdViewFrame = findViewById(R.id.native_ad_view_frame)
        mLoad = findViewById(R.id.load)
        mPlay = findViewById(R.id.play)

        // ボタンを押すと広告を読み込む
        mLoad.setOnClickListener {
            mNativeAd?.load()
        }

        // ボタンを押すと広告を表示する
        mPlay.setOnClickListener {
            mNativeAd?.play()
        }

        // ネイティブ広告のインスタンスを生成する
        // 広告枠IDとActivityを引数で渡してください
        mNativeAd = AdfurikunNativeAd(
            this, AdfurikunConst.ADFURIKUN_NATIVE_AD_APPID,
            Utils.convertDpToPx(this, 320),
            Utils.convertDpToPx(this, 180)
        )
        mNativeAd?.setAdfurikunNativeAdLoadListener(mLoadListener)
        mNativeAd?.setAdfurikunNativeAdVideoListener(mVideoListener)

        mNativeAdViewFrame.addView(mNativeAd?.nativeAdView)
    }

    override fun onDestroy() {
        mNativeAd?.onDestroy()
        super.onDestroy()
    }
}
