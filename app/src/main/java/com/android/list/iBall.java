package com.android.list;

import android.content.Context;

/**
 * Created by isaiah on 2018-05-19.
 */

public class iBall {
    public static Context context;

    public static int dpToPx(int dp) {
        float density = context.getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }
}
