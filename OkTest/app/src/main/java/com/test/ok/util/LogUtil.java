package com.test.ok.util;

import android.text.TextUtils;

import com.test.ok.BuildConfig;

public class LogUtil {
    public static final boolean isTest = BuildConfig.DEBUG;

    public static void e(String key, String value) {
        if (!TextUtils.isEmpty(value)) {
            android.util.Log.e("kellan" + key, value);
        }
    }
}
