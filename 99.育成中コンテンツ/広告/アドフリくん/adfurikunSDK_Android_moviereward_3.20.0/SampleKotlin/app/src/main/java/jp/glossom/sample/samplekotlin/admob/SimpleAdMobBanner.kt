package jp.glossom.sample.samplekotlin.admob

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.admanager.AdManagerAdView
import jp.glossom.sample.samplekotlin.R

class SimpleAdMobBanner : Activity() {

    private lateinit var mAdContainer: FrameLayout

    private val AD_UNIT_ID = ""
    private var mAdView: AdManagerAdView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admob_banner)

        mAdContainer = findViewById(R.id.ad_container)

        findViewById<Button>(R.id.load).setOnClickListener {
            val adRequest = AdManagerAdRequest.Builder().build()
            mAdView?.loadAd(adRequest)
        }

        // AdMob初期化
        MobileAds.initialize(this)

        // AdMobのバナー表示用ビューを生成
        mAdView = AdManagerAdView(this).apply {
            setAdSizes(AdSize.BANNER)
            adUnitId = AD_UNIT_ID
            adListener = mAdListener
        }
        mAdContainer.addView(mAdView)
    }

    /**
     * 広告読み込み・表示用リスナー
     */
    private val mAdListener = object : AdListener() {
        override fun onAdLoaded() {
            // 広告読み込み完了時に呼ばれる。
            Toast.makeText(this@SimpleAdMobBanner, "読み込み成功", Toast.LENGTH_SHORT).show()
        }

        override fun onAdFailedToLoad(adError: LoadAdError) {
            // 広告読み込み失敗時に呼ばれる。
            Toast.makeText(this@SimpleAdMobBanner, "読み込み失敗", Toast.LENGTH_SHORT).show()
        }

        override fun onAdImpression() {
            super.onAdImpression()
            // 広告表示時に呼ばれる
            Toast.makeText(this@SimpleAdMobBanner, "広告表示", Toast.LENGTH_SHORT).show()
        }

        override fun onAdClicked() {
            super.onAdClicked()
            // 広告をクリックした時に呼ばれる。
            Toast.makeText(this@SimpleAdMobBanner, "広告クリック", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        // 広告を再開
        mAdView?.resume()
    }

    override fun onPause() {
        mAdView?.pause()
        // 広告を一時停止
        super.onPause()
    }

    override fun onDestroy() {
        // 広告を破棄
        mAdView?.destroy()
        super.onDestroy()
    }
}