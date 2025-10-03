package jp.glossom.sample.samplekotlin.admob

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.ads.*
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import jp.glossom.sample.samplekotlin.R

class SimpleAdMobInterstitial : Activity() {

    private val AD_UNIT_ID = ""
    private var mInterstitialAd: InterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admob_interstitial)

        findViewById<Button>(R.id.load).setOnClickListener {
            load()
        }

        findViewById<Button>(R.id.show).setOnClickListener {
            show()
        }

        // AdMob初期化
        MobileAds.initialize(this)
    }

    /**
     * 広告を読み込み
     */
    private fun load() {
        if (mInterstitialAd == null) {
            InterstitialAd.load(this, AD_UNIT_ID, AdManagerAdRequest.Builder().build(), loadListener)
        }
    }

    /**
     * 広告を表示
     */
    private fun show() {
        mInterstitialAd?.let {
            it.fullScreenContentCallback = playListener
            it.show(this)
        }
    }

    /**
     * 広告読み込み用リスナー
     */
    private val loadListener = object : InterstitialAdLoadCallback() {
        override fun onAdLoaded(interstitialAd: InterstitialAd) {
            // 広告読み込み完了時に呼ばれる。
            Toast.makeText(this@SimpleAdMobInterstitial, "読み込み成功", Toast.LENGTH_SHORT).show()
            mInterstitialAd = interstitialAd
        }

        override fun onAdFailedToLoad(adError: LoadAdError) {
            // 広告読み込み失敗時に呼ばれる。
            Toast.makeText(this@SimpleAdMobInterstitial, "読み込み失敗", Toast.LENGTH_SHORT).show()
            mInterstitialAd = null
        }
    }

    /**
     * 広告表示用リスナー
     */
    private val playListener = object : FullScreenContentCallback() {
        override fun onAdShowedFullScreenContent() {
            super.onAdShowedFullScreenContent()
            // 広告再生開始時に呼ばれる。
            Toast.makeText(this@SimpleAdMobInterstitial, "再生開始", Toast.LENGTH_SHORT).show()
        }

        override fun onAdDismissedFullScreenContent() {
            super.onAdDismissedFullScreenContent()
            // 広告を閉じた時に呼ばれる。
            Toast.makeText(this@SimpleAdMobInterstitial, "広告閉じる", Toast.LENGTH_SHORT).show()
            mInterstitialAd = null
        }

        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
            super.onAdFailedToShowFullScreenContent(adError)
            // 広告再生失敗時に呼ばれる。
            Toast.makeText(this@SimpleAdMobInterstitial, "再生失敗", Toast.LENGTH_SHORT).show()
            mInterstitialAd = null
        }

        override fun onAdImpression() {
            super.onAdImpression()
        }
    }
}