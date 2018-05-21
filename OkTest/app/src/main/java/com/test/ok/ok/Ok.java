package com.test.ok.ok;

import com.test.ok.ok.info.OkInfo;
import com.test.ok.ok.up.UpInfo;
import com.test.ok.ok.up.UpLoadFileRequestBody;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Kellan on 2017/8/9.
 */
public class Ok {
    public static final MediaType MEDIA_TYPE_ALL = MediaType.parse("image/*");

    public static void get(String url, OkInfo info, boolean b){
        if(b) info.show();

        Request request = new Request.Builder()
                .url(url)
                .build();

        ClientHelper.get().getClientString().newCall(request).enqueue(info);
    }

    public static void post(String url, RequestBody body, OkInfo info, boolean b){
        if(b) info.show();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        ClientHelper.get().getClientString().newCall(request).enqueue(info);
    }

    public static void upload(String url, File f, UpInfo p, OkInfo info){
        UpLoadFileRequestBody pBody = new UpLoadFileRequestBody(f, MEDIA_TYPE_ALL, p);
        Request request = new Request.Builder()
                .url(url)
                .post(pBody)
                .build();

        ClientHelper.get().getClientString().newCall(request).enqueue(info);
    }
}
