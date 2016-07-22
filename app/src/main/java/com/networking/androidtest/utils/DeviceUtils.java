package com.networking.androidtest.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by rafalwesolowski on 21/07/2016.
 */
public class DeviceUtils {

    /**
     * Get Screen width.
     *
     * @param context context of the caller
     * @return screen width
     */
    public static int getDisplayWidth(Context context) {
        int result = 0;
        if (context != null) {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            // Returns the size of the entire window, including status bar and title.
            DisplayMetrics dm = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(dm);
            result = dm.widthPixels;
        }
        return result;
    }
}
