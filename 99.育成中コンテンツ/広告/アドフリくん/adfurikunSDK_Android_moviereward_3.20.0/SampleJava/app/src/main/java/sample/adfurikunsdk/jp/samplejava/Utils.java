package sample.adfurikunsdk.jp.samplejava;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;

public class Utils {

    /**
     * dpをpixelに変換
     *
     * @param context
     * @param dp
     * @return
     */
    public static int convertDpToPx(Context context, int dp){
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) (dp * metrics.density);
    }

    public static void log(String text) {
        Log.d("AdfurikunSample", text);
    }

    public static void popupAlert(final String message, final Activity activity) {
        if (!activity.isFinishing()) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("Error");
                    builder.setMessage(message);
                    builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intent = new Intent(activity, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            activity.startActivity(intent);
                        }
                    });
                    builder.create().show();
                }
            });
        }
    }
}
