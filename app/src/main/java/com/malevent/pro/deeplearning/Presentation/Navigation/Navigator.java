package com.malevent.pro.deeplearning.Presentation.Navigation;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.malevent.pro.deeplearning.Presentation.Activity.MainActivity;

/**
 * Created by OCCOYANC on 3/12/2018.
 */

public class Navigator {

    private static final String TAG = Navigator.class.getSimpleName();

    private static final String PLAY_STORE_APP_URI = "market://details?id=";


    /**
     * Links a user to an app
     */
    public static void redirectToApp(Context context, String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent != null) {
            // We found the activity now start the activity
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            redirectToMarket(context, packageName);
        }
    }


    public static void redirectToMarket(Context context, String packageName) {
        // Bring user to the market or let them choose an app?
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse(PLAY_STORE_APP_URI + packageName));
        context.startActivity(intent);
    }

    public static void navigateToMain(Context context) {
        if (context != null) {
            Intent intentToLaunch = MainActivity.getCallingIntent(context);
            context.startActivity(intentToLaunch);
        }
    }
}
