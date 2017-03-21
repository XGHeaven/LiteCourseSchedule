package com.xgheaven.litecourseschedule;


import android.content.res.Resources;

public class Utils {
    public static int px2Dp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static int dp2Px(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}
