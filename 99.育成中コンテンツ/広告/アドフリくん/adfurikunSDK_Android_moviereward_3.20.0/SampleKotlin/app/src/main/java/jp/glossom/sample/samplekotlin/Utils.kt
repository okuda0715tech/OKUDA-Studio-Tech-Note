package jp.glossom.sample.samplekotlin

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log

object Utils {

    /**
     * dpをpixelに変換
     *
     * @param context
     * @param dp
     * @return
     */
    fun convertDpToPx(context: Context, dp: Int): Int {
        val metrics = context.resources.displayMetrics
        return (dp * metrics.density).toInt()
    }

    fun log(text: String) {
        Log.d("AdfurikunSample", text)
    }

    fun popupAlert(message: String, activity: Activity) {
        if (!activity.isFinishing) {
            activity.runOnUiThread {
                val builder = AlertDialog.Builder(activity)
                builder.setTitle("Error")
                builder.setMessage(message)
                builder.setPositiveButton("Close") { dialog, which ->
                    dialog.dismiss()
                    val intent = Intent(activity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    activity.startActivity(intent)
                }
                builder.create().show()
            }
        }
    }
}
