package jp.glossom.sample.samplekotlin.admob

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.ads.*
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import jp.tjkapp.adfurikunsdk.moviereward.AdfurikunAdMobNativeAd
import jp.glossom.sample.samplekotlin.R
import jp.glossom.sample.samplekotlin.Utils

class SimpleAdMobNativeAd : Activity() {

    private lateinit var mAdContainer: FrameLayout
    private lateinit var mAdLoader: AdLoader

    private val AD_UNIT_ID = ""
    private var mNativeAd: NativeAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admob_native_ad)

        mAdContainer = findViewById(R.id.ad_container)

        findViewById<Button>(R.id.load).setOnClickListener {
            load()
        }

        // AdMob初期化
        MobileAds.initialize(this)

        // AdMobの広告読み込み用インスタンスを生成
        mAdLoader = AdLoader.Builder(this, AD_UNIT_ID)
                .forNativeAd(onNativeAdLoadedListener)
                .withAdListener(adListener)
                .build()
    }

    /**
     * 広告を読み込み
     */
    private fun load() {
        if (!mAdLoader.isLoading) {
            val width = Utils.convertDpToPx(this@SimpleAdMobNativeAd, 320)
            val height = Utils.convertDpToPx(this@SimpleAdMobNativeAd, 180)
            val customEventExtras = Bundle()
            customEventExtras.putInt(AdfurikunAdMobNativeAd.CUSTOM_EVENT_KEY_WIDTH, width)
            customEventExtras.putInt(AdfurikunAdMobNativeAd.CUSTOM_EVENT_KEY_HEIGHT, height)
            val adRequest = AdRequest.Builder()
                    .addCustomEventExtrasBundle(AdfurikunAdMobNativeAd::class.java, customEventExtras)
                    .build()
            mAdLoader.loadAd(adRequest)
        }
    }

    /**
     * 広告読み込み用リスナー
     */

    private val onNativeAdLoadedListener = NativeAd.OnNativeAdLoadedListener { nativeAd ->
        // 広告読み込み完了時に呼ばれる。
        // NativeAd利用して広告を組み立てる。
        // https://developers.google.com/admob/android/native/templates
        Toast.makeText(this@SimpleAdMobNativeAd, "読み込み成功", Toast.LENGTH_SHORT).show()
        setupAdView(nativeAd)
    }

    /**
     * 広告ビューをセットアップ
     *
     * @param nativeAd
     */
    private fun setupAdView(nativeAd: NativeAd) {
        mNativeAd?.destroy()
        mNativeAd = nativeAd

        val adView = LayoutInflater.from(this@SimpleAdMobNativeAd).inflate(R.layout.admob_custom_view, null) as NativeAdView

        adView.mediaView = adView.findViewById(R.id.ad_media)
        adView.headlineView = adView.findViewById(R.id.ad_headline)
        adView.bodyView = adView.findViewById(R.id.ad_body)
        adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
        adView.iconView = adView.findViewById(R.id.ad_app_icon)
        adView.priceView = adView.findViewById(R.id.ad_price)
        adView.starRatingView = adView.findViewById(R.id.ad_stars)
        adView.storeView = adView.findViewById(R.id.ad_store)
        adView.advertiserView = adView.findViewById(R.id.ad_advertiser)

        // メディアサイズを設定する
        val width = Utils.convertDpToPx(this@SimpleAdMobNativeAd, 320)
        val height = Utils.convertDpToPx(this@SimpleAdMobNativeAd, 180)
        adView.mediaView?.layoutParams = LinearLayout.LayoutParams(width, height)

        (adView.headlineView as TextView).text = nativeAd.headline ?: ""
        (adView.bodyView as TextView).text = nativeAd.body ?: ""

        nativeAd.callToAction?.let { callToAction ->
            (adView.callToActionView as TextView).text = callToAction
            adView.callToActionView?.visibility = View.VISIBLE
        } ?: run {
            adView.callToActionView?.visibility = View.GONE
        }

        nativeAd.icon?.drawable?.let { icon ->
            (adView.iconView as ImageView).setImageDrawable(icon)
            adView.iconView?.visibility = View.VISIBLE
        } ?: run {
            adView.iconView?.visibility = View.GONE
        }

        (adView.priceView as TextView).text = nativeAd.price ?: ""

        nativeAd.starRating?.let { starRating ->
            (adView.starRatingView as RatingBar).rating = starRating.toFloat()
            adView.starRatingView?.visibility = View.VISIBLE
        } ?: run {
            adView.starRatingView?.visibility = View.GONE
        }

        (adView.storeView as TextView).text = nativeAd.store ?: ""

        (adView.advertiserView as TextView).text = nativeAd.advertiser ?: ""

        adView.setNativeAd(nativeAd)

        nativeAd.mediaContent?.videoController?.let { videoController ->
            if (videoController.hasVideoContent()) {
                videoController.videoLifecycleCallbacks = object : VideoController.VideoLifecycleCallbacks() {
                    override fun onVideoStart() {
                        super.onVideoStart()
                        // 広告再生開始時に呼ばれる。
                        Toast.makeText(this@SimpleAdMobNativeAd, "再生開始", Toast.LENGTH_SHORT).show()
                    }

                    override fun onVideoEnd() {
                        super.onVideoEnd()
                        // 広告再生終了時に呼ばれる。
                        Toast.makeText(this@SimpleAdMobNativeAd, "再生完了", Toast.LENGTH_SHORT).show()
                    }

                    override fun onVideoPlay() {
                        super.onVideoPlay()
                    }

                    override fun onVideoPause() {
                        super.onVideoPause()
                    }
                }
            }
        }

        mAdContainer.removeAllViews()
        mAdContainer.addView(adView)
    }

    /**
     * 広告表示用リスナー
     */
    private val adListener = object : AdListener() {
        override fun onAdLoaded() {
            // 広告読み込み完了時に呼ばれる。
            Toast.makeText(this@SimpleAdMobNativeAd, "読み込み成功", Toast.LENGTH_SHORT).show()
        }

        override fun onAdFailedToLoad(adError: LoadAdError) {
            // 広告読み込み失敗時に呼ばれる。
            Toast.makeText(this@SimpleAdMobNativeAd, "読み込み失敗", Toast.LENGTH_SHORT).show()
        }

        override fun onAdImpression() {
            super.onAdImpression()
            // 広告表示時に呼ばれる
            Toast.makeText(this@SimpleAdMobNativeAd, "広告表示", Toast.LENGTH_SHORT).show()
        }

        override fun onAdClicked() {
            super.onAdClicked()
            // 広告をクリックした時に呼ばれる。
            Toast.makeText(this@SimpleAdMobNativeAd, "広告クリック", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        // 広告を破棄
        mNativeAd?.destroy()
        super.onDestroy()
    }

    companion object {

    }
}