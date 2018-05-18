package com.test.ok.util;

import android.annotation.SuppressLint;
import android.app.Activity;

/**
 * Created by Kellan on 2017/8/9.
 */
public class MyUtil {

    @SuppressLint("NewApi")
    public static boolean isDead(Activity act) {
        return act == null || act.isFinishing() || act.isDestroyed();
    }
}
