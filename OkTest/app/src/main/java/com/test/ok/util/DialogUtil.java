package com.test.ok.util;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;

/**
 * Created by Kellan on 2017/8/9.
 */

public class DialogUtil {

    private DialogUtil() {
    }

    public static Dialog get(Activity activity) {
        Dialog d = new Dialog(activity);
        d.getWindow().getAttributes().gravity = Gravity.CENTER;
        d.setCancelable(false);

        return d;
    }
}
