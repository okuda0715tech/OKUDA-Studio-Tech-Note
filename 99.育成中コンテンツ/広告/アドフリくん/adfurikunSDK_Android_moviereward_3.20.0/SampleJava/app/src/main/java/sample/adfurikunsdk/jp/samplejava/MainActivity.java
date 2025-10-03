package sample.adfurikunsdk.jp.samplejava;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import sample.adfurikunsdk.jp.samplejava.admob.SimpleAdMobBanner;
import sample.adfurikunsdk.jp.samplejava.admob.SimpleAdMobInterstitial;
import sample.adfurikunsdk.jp.samplejava.admob.SimpleAdMobNativeAd;
import sample.adfurikunsdk.jp.samplejava.admob.SimpleAdMobRectangle;
import sample.adfurikunsdk.jp.samplejava.admob.SimpleAdMobReward;

public class MainActivity extends ListActivity {
    private final String[] titles = {
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
    };

    private final Class<?>[] class_names = {
            SimpleAppOpenAd.class,
            SimpleMovieReward.class,
            SimpleInterstitial.class,
            SimpleNativeAd.class,
            SimpleRectangle.class,
            SimpleBanner.class,
            SimpleBannerAutoRefresh.class,
            SimpleAdMobReward.class,
            SimpleAdMobInterstitial.class,
            SimpleAdMobNativeAd.class,
            SimpleAdMobRectangle.class,
            SimpleAdMobBanner.class
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titles);
        setListAdapter(adapter);

        if (AdfurikunConst.ADFURIKUN_REWARD_APPID.startsWith("*") &&
                AdfurikunConst.ADFURIKUN_INTER_APPID.startsWith("*") &&
                AdfurikunConst.ADFURIKUN_NATIVE_AD_APPID.startsWith("*") &&
                AdfurikunConst.ADFURIKUN_RECTANGLE_APPID.startsWith("*") &&
                AdfurikunConst.ADFURIKUN_BANNER_APPID.startsWith("*") && 
                AdfurikunConst.ADFURIKUN_APP_OPEN_AD_APPID.startsWith("*")) {
            Utils.popupAlert("please set all the APPID in AdfurikunConst", this);
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent intent = new Intent(MainActivity.this, class_names[position]);
        intent.putExtra("title", titles[position]);
        startActivity(intent);
    }

}
