package com.test.ok.ok;


import android.text.TextUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * Created by Kellan on 2017/9/27.
 */
public class Params extends HashMap<String, String> {

    public Params() {
    }

    public RequestBody build(){
        FormBody.Builder b = new FormBody.Builder();
        Iterator<Entry<String, String>> entries = entrySet().iterator();

        while (entries.hasNext()) {
            Map.Entry<String, String> entry = entries.next();

            b.add(entry.getKey(), entry.getValue());
        }

        return b.build();
    }

    @Override
    public String put(String key, String value) {
        if (!TextUtils.isEmpty(value)) {
            return super.put(key, value);
        } else {
            return "";
        }
    }
}

