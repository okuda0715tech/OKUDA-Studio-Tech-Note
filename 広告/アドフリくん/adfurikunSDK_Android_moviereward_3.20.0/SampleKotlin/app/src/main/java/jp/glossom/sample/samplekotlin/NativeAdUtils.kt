package jp.glossom.sample.samplekotlin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.google.android.gms.ads.nativead.NativeAdView
import jp.tjkapp.adfurikunsdk.moviereward.AdMobParts
import jp.tjkapp.adfurikunsdk.moviereward.AdfurikunNativeAd

object NativeAdUtils {

    fun adMobRegisterViewFromNativeAd(context: Context, adMobParts: AdMobParts?, adfurikunNativeAd: AdfurikunNativeAd? = null): View? {
        adMobParts?.let { parts ->
            (parts.detail as? com.google.android.gms.ads.nativead.NativeAd)?.let { nativeAd ->
                val adView = LayoutInflater.from(context).inflate(R.layout.admob_custom_view, null) as NativeAdView

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
                val width = Utils.convertDpToPx(context, 320)
                val height = Utils.convertDpToPx(context, 180)
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

                nativeAd.mediaContent?.let { mediaContent ->
                    adView.mediaView?.setMediaContent(mediaContent)
                }

                adView.setNativeAd(nativeAd)
                // アドフリくんのViewable Impressionを発生させるための広告ビューを設定する
                parts.setVimpTargetView(adView)
                parts.prepareVideoListener(adfurikunNativeAd)
                return adView
            }
        }
        return null
    }
}
