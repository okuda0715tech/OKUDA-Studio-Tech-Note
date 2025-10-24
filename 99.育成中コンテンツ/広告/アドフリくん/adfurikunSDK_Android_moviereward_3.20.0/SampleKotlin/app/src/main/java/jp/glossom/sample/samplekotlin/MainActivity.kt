package jp.glossom.sample.samplekotlin

import android.R
import android.app.ListActivity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import jp.glossom.sample.samplekotlin.admob.*
import kotlin.reflect.KClass

class MainActivity : ListActivity() {

    private val titles = arrayOf(
        "SimpleAppOpenAd",
        "SimpleMovieReward",
        "SimpleInterstitial",
        "SimpleNativeAd",
        "SimpleRectangle",
        "SimpleBanner",
        "SimpleBannerAutoRefresh",
        "SimpleAdMobReward",
        "SimpleAdMobInterstitial",
        "SimpleAdMobNativeAd",
        "SimpleAdMobRectangle",
        "SimpleAdMobBanner"
    )

    private var class_names = arrayOf<KClass<*>>(
        SimpleAppOpenAd::class,
        SimpleMovieReward::class,
        SimpleInterstitial::class,
        SimpleNativeAd::class,
        SimpleRectangle::class,
        SimpleBanner::class,
        SimpleBannerAutoRefresh::class,
        SimpleAdMobReward::class,
        SimpleAdMobInterstitial::class,
        SimpleAdMobNativeAd::class,
        SimpleAdMobRectangle::class,
        SimpleAdMobBanner::class
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val adapter = ArrayAdapter(this, R.layout.simple_list_item_1, titles)
        listAdapter = adapter

        if (AdfurikunConst.ADFURIKUN_REWARD_APPID.startsWith("*") &&
            AdfurikunConst.ADFURIKUN_INTER_APPID.startsWith("*") &&
            AdfurikunConst.ADFURIKUN_NATIVE_AD_APPID.startsWith("*") &&
            AdfurikunConst.ADFURIKUN_RECTANGLE_APPID.startsWith("*") &&
            AdfurikunConst.ADFURIKUN_BANNER_APPID.startsWith("*") &&
            AdfurikunConst.ADFURIKUN_APP_OPEN_AD_APPID.startsWith("*")
        ) {
            Utils.popupAlert("please set all the APPID in AdfurikunConst", this)
        }
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)
        val intent = Intent(this, class_names[position].java)
        intent.putExtra("title", titles[position])
        startActivity(intent)
    }
}
