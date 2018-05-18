package com.test.ok.ok;

import com.test.ok.ok.interceptor.StringInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by Kellan on 2017/8/4.
 */

public class ClientHelper {
    private static final ClientHelper ch = new ClientHelper();
    private OkHttpClient clientCrypt;
    private OkHttpClient clientClean;
    public final static int CONNECT_TIMEOUT = 60;
    public final static int READ_TIMEOUT = 60;
    public final static int WRITE_TIMEOUT = 60;

    private ClientHelper() {
        init();
    }

    public static ClientHelper get() {
        return ch;
    }

    private void init() {
        clientCrypt = new OkHttpClient.Builder()
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)//设置连接超时时间
                .addInterceptor(new StringInterceptor())
                .build();
        clientClean = new OkHttpClient.Builder()
                .build();
    }

    public OkHttpClient getClientString() {
        return clientCrypt;
    }

    public OkHttpClient getClientClean() {
        return clientClean;
    }
}
