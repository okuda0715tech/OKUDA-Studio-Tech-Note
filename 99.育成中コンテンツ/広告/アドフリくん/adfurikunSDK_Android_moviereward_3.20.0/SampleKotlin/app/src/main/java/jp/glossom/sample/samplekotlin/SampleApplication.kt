package jp.glossom.sample.samplekotlin

import android.app.Application
import jp.tjkapp.adfurikunsdk.moviereward.AdfurikunSdk

open class SampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        AdfurikunSdk.init(this)
    }
}