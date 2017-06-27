package com.ramraj.work.utils;

import android.content.res.Resources;
import android.util.TypedValue;

import com.ramraj.work.App;

/**
 * Created by ramraj on 27/6/17.
 */

public class Utils {
    /**
     * Converting dp to pixel
     */
    public static int dpToPx(int dp) {
        Resources r = App.getAppContext().getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
