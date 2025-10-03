package jp.glossom.sample.samplekotlin

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import jp.tjkapp.adfurikunsdk.moviereward.*

/**
 * レクタングル広告用サンプル
 */
class SimpleRectangle : Activity() {

    private lateinit var mTitle: TextView
    private lateinit var mDescription: TextView
    private lateinit var mRectangleViewFrame: FrameLayout
    private lateinit var mLoad: Button
    private lateinit var mPlay: Button

    /** レクタングル広告インスタンス */
    private var mRectangle: AdfurikunRectangle? = null
    /** レクタングル広告情報 */
    private var mRectangleInfo: AdfurikunRectangleAdInfo? = null

    /** 広告読み込みリスナー */
    private val mLoadListener = object : AdfurikunRectangleLoadListener {
        override fun onRectangleLoadFinish(adInfo: AdfurikunRectangleAdInfo?, appId: String?) {
            // 広告の準備完了直後に実行する処理を記述してください
            Utils.log("onRectangleLoadFinish appId = $appId")
            adInfo?.let {
                mRectangleInfo = it
                mTitle.text = it.title
                mDescription.text = it.description
                mPlay.isEnabled = true
            }
        }

        override fun onRectangleLoadError(error: AdfurikunMovieError?, appId: String?) {
            // 広告の準備失敗直後に実行する処理を記述してください
            Utils.log("onRectangleLoadError appId = $appId, errorType = ${error?.errorType?.name}")
        }
    }

    /** 広告再生リスナー */
    private val mVideoListener = object : AdfurikunRectangleVideoListener {
        override fun onRectangleViewPlayStart(appId: String?) {
            // 広告の再生開始直後に実行する処理を記述してください
            Utils.log("onRectangleViewPlayStart appId = $appId")
            mPlay.isEnabled = false
        }

        override fun onRectangleViewPlayFinish(appId: String?, isVideoAd: Boolean) {
            // 広告の再生に失敗した時に実行する処理を記述してください
            Utils.log("onRectangleViewPlayFinish appId = $appId, isVideoAd = $isVideoAd")
        }

        override fun onRectangleViewPlayFail(appId: String?, error: AdfurikunMovieError?) {
            // 広告の再生が完了したら実行する処理を記述してください
            Utils.log("onRectangleViewPlayFail appId = $appId, errorType = ${error?.errorType?.name}")
        }

        override fun onRectangleViewClicked(appId: String?) {
            // 広告の画面をクリックした時に実行する処理を記述してください
            Utils.log("onRectangleViewClicked appId = $appId")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rectangle)
        title = intent.getStringExtra("title")

        mTitle = findViewById(R.id.title)
        mDescription = findViewById(R.id.description)
        mRectangleViewFrame = findViewById(R.id.rectangle_view_frame)
        mLoad = findViewById(R.id.load)
        mPlay = findViewById(R.id.play)

        // ボタンを押すと広告を読み込む
        mLoad.setOnClickListener {
            mRectangle?.load()
        }

        // ボタンを押すと広告を表示する
        mPlay.setOnClickListener {
            mRectangle?.play()
        }

        // レクタングル広告のインスタンスを生成する
        // 広告枠IDとActivityを引数で渡してください
        mRectangle = AdfurikunRectangle(
            this, AdfurikunConst.ADFURIKUN_RECTANGLE_APPID,
            Utils.convertDpToPx(this, 300),
            Utils.convertDpToPx(this, 250)
        )
        mRectangle?.setAdfurikunRectangleLoadListener(mLoadListener)
        mRectangle?.setAdfurikunRectangleVideoListener(mVideoListener)

        mRectangleViewFrame.addView(mRectangle?.rectangleView)
    }

    override fun onDestroy() {
        mRectangle?.onDestroy()
        super.onDestroy()
    }
}
