package jp.glossom.sample.samplekotlin

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import jp.tjkapp.adfurikunsdk.moviereward.*

/**
 * バナー広告用サンプル
 */
class SimpleBanner : Activity() {

    private lateinit var mTitle: TextView
    private lateinit var mDescription: TextView
    private lateinit var mBannerViewFrame: FrameLayout
    private lateinit var mLoad: Button
    private lateinit var mPlay: Button

    /** バナー広告インスタンス */
    private var mBanner: AdfurikunBanner? = null
    /** バナー広告情報 */
    private var mBannerInfo: AdfurikunBannerAdInfo? = null

    /** 広告読み込みリスナー */
    private val mLoadListener = object : AdfurikunBannerLoadListener {
        override fun onBannerLoadFinish(adInfo: AdfurikunBannerAdInfo?, appId: String?) {
            // 広告の準備完了直後に実行する処理を記述してください
            Utils.log("onBannerLoadFinish appId = $appId")
            adInfo?.let {
                mBannerInfo = it
                mTitle.text = it.title
                mDescription.text = it.description
                mPlay.isEnabled = true
            }
        }

        override fun onBannerLoadError(error: AdfurikunMovieError?, appId: String?) {
            // 広告の準備失敗直後に実行する処理を記述してください
            Utils.log("onBannerLoadError appId = $appId, errorType = ${error?.errorType?.name}")
        }
    }

    /** 広告再生リスナー */
    private val mVideoListener = object : AdfurikunBannerVideoListener {
        override fun onBannerViewPlayStart(appId: String?) {
            // 広告の再生開始直後に実行する処理を記述してください
            Utils.log("onBannerViewPlayStart appId = $appId")
            mPlay.isEnabled = false
        }

        override fun onBannerViewPlayFinish(appId: String?, isVideoAd: Boolean) {
            // 広告の再生に失敗した時に実行する処理を記述してください
            Utils.log("onBannerViewPlayFinish appId = $appId, isVideoAd = $isVideoAd")
        }

        override fun onBannerViewPlayFail(appId: String?, error: AdfurikunMovieError?) {
            // 広告の再生が完了したら実行する処理を記述してください
            Utils.log("onBannerViewPlayFail appId = $appId, errorType = ${error?.errorType?.name}")
        }

        override fun onBannerViewClicked(appId: String?) {
            // 広告の画面をクリックした時に実行する処理を記述してください
            Utils.log("onBannerViewClicked appId = $appId")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_banner)
        title = intent.getStringExtra("title")

        mTitle = findViewById(R.id.title)
        mDescription = findViewById(R.id.description)
        mBannerViewFrame = findViewById(R.id.banner_view_frame)
        mLoad = findViewById(R.id.load)
        mPlay = findViewById(R.id.play)

        // ボタンを押すと広告を読み込む
        mLoad.setOnClickListener {
            mBanner?.load()
        }

        // ボタンを押すと広告を表示する
        mPlay.setOnClickListener {
            mBanner?.play()
        }

        // バナー広告のインスタンスを生成する
        // 広告枠IDとActivityを引数で渡してください
        mBanner = AdfurikunBanner(
            this, AdfurikunConst.ADFURIKUN_BANNER_APPID,
            Utils.convertDpToPx(this, 320),
            Utils.convertDpToPx(this, 50)
        )
        mBanner?.setAdfurikunBannerLoadListener(mLoadListener)
        mBanner?.setAdfurikunBannerVideoListener(mVideoListener)

        mBannerViewFrame.addView(mBanner?.bannerView)
    }

    override fun onDestroy() {
        mBanner?.onDestroy()
        super.onDestroy()
    }
}
