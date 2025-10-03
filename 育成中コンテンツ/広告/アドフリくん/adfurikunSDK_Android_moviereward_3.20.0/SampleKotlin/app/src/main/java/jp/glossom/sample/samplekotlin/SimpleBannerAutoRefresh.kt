package jp.glossom.sample.samplekotlin

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import jp.tjkapp.adfurikunsdk.moviereward.*

/**
 * バナー広告自動切り替え用サンプル
 */
class SimpleBannerAutoRefresh : Activity() {

    private lateinit var mTitle: TextView
    private lateinit var mDescription: TextView
    private lateinit var mBannerViewFrame: FrameLayout
    private lateinit var mAutoRefresh: Button

    /** バナー広告インスタンス */
    private var mBanner: AdfurikunBanner? = null

    /** リトライ回数 */
    private var mLoadRetryCount = 0

    /** 広告読み込みリスナー */
    private val mLoadListener = object : AdfurikunBannerLoadListener {
        override fun onBannerLoadFinish(adInfo: AdfurikunBannerAdInfo?, appId: String?) {
            // 広告の準備完了直後に実行する処理を記述してください
            Utils.log("onBannerLoadFinish appId = $appId")
            adInfo?.let {
                Toast.makeText(this@SimpleBannerAutoRefresh, "広告を切り替えました", Toast.LENGTH_SHORT).show()
                mTitle.text = it.title
                mDescription.text = it.description
                mBannerViewFrame.visibility = View.VISIBLE
                // バナーを表示する
                mBanner?.play()
                // 次の広告読み込み開始する
                Handler(Looper.getMainLooper()).postDelayed({
                    mBanner?.load()
                }, AUTO_REFRESH_INTERVAL)
                mLoadRetryCount = 0
            }
        }

        override fun onBannerLoadError(error: AdfurikunMovieError?, appId: String?) {
            // 広告の準備失敗直後に実行する処理を記述してください
            Utils.log("onBannerLoadError appId = $appId, errorType = ${error?.errorType?.name}")
            // 読み込み中ではなくエラータイプの場合にリトライ処理を行う
            if (error?.errorType != AdfurikunMovieError.MovieErrorType.LOADING) {
                if (mLoadRetryCount < LOAD_RETRY_COUNT) {
                    mBanner?.load()
                }
                mLoadRetryCount++
            }
        }
    }

    /** 広告再生リスナー */
    private val mVideoListener = object : AdfurikunBannerVideoListener {
        override fun onBannerViewPlayStart(appId: String?) {
            // 広告の再生開始直後に実行する処理を記述してください
            Utils.log("onBannerViewPlayStart appId = $appId")
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
        setContentView(R.layout.activity_banner_auto_refresh)
        title = intent.getStringExtra("title")

        mTitle = findViewById(R.id.title)
        mDescription = findViewById(R.id.description)
        mBannerViewFrame = findViewById(R.id.banner_view_frame)
        mAutoRefresh = findViewById(R.id.auto_refresh)

        // ボタンを押すと広告の自動切り替え開始
        mAutoRefresh.setOnClickListener {
            mBanner?.load()
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

    companion object {
        private const val AUTO_REFRESH_INTERVAL = 5 * 1000L  // 60秒
        private const val LOAD_RETRY_COUNT = 5  // リトライ回数
    }
}