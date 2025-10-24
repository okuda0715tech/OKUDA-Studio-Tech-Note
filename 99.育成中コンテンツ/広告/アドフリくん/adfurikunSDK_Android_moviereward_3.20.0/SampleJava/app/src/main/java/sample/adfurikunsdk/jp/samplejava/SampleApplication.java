package sample.adfurikunsdk.jp.samplejava;

import android.app.Application;
import jp.tjkapp.adfurikunsdk.moviereward.AdfurikunSdk;

public class SampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AdfurikunSdk.init(this);
    }
}