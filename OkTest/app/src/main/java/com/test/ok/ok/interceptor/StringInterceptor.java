package com.test.ok.ok.interceptor;

import android.text.TextUtils;

import com.test.ok.util.LogUtil;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * Created by M4500 on 2017/7/12.
 */

public class StringInterceptor implements Interceptor {
    private static String TAG = StringInterceptor.class.getSimpleName();

    private static String logBody(RequestBody body) {
        try {
            Buffer buffer = new Buffer();
            body.writeTo(buffer);
            return buffer.readUtf8();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void printUrl(Request request) {
        if (request.body() != null && request.body().contentType() != null && "multipart".equals(request.body().contentType().type())) {
            LogUtil.e(TAG, "request url : " + request.url());
        } else {
            String logBody = logBody(request.body());
            String body = getDecoder(logBody);

            String params = TextUtils.isEmpty(body) ? "" : "?" + body;
            LogUtil.e(TAG, "request url : " + request.url() + params);
        }
    }

    private static String getDecoder(String str){
        String body = "";
        try {
            body = URLDecoder.decode(str, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return body;
    }

    public static void printResult(final Response response, final String result) {
        if (LogUtil.isTest) {
            ExecutorService pool = Executors.newCachedThreadPool();
            pool.execute(() -> {
                String s = unicodeToUTF_8(result);
                printAll(response, s);
            });
        }
    }

    private static synchronized void printAll(Response response, String result) {
        //        printHeaders("request headers : ", response.request().headers());
        printUrl(response.request());
        LogUtil.e(TAG, "response code : " + response.code() + " || response msg : " + response.message());
        eSub(TAG, "response result : " + result);
        LogUtil.e(TAG, "-------------------------------------------------------------------------------------------------------");
    }

    private static void printHeaders(String tag, Headers headers) {
        StringBuilder sb = new StringBuilder();
        int headersLength = headers.size();
        for (int i = 0; i < headersLength; i++) {
            String headerName = headers.name(i);
            String headerValue = headers.get(headerName);
            sb.append("||name:" + headerName + "||value:" + headerValue);
        }
        LogUtil.e("foryou_test", tag + sb.toString());
    }

    /**
     * 截断输出日志
     *
     * @param msg
     */
    public static void eSub(String tag, String msg) {
        int LOG_MAXLENGTH = 2000;
        int strLength = msg.length();
        int start = 0;
        int end = LOG_MAXLENGTH;
        for (int i = 0; i < 100; i++) {
            if (strLength > end) {
                LogUtil.e(tag + i, msg.substring(start, end));
                start = end;
                end = end + LOG_MAXLENGTH;
            } else {
                LogUtil.e(tag, msg.substring(start, strLength));
                break;
            }
        }
    }

    public static String unicodeToUTF_8(String src) {
        if (TextUtils.isEmpty(src)) {
            return null;
        }
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < src.length(); ) {
            char c = src.charAt(i);
            if (i + 6 < src.length() && c == '\\' && src.charAt(i + 1) == 'u') {
                String hex = src.substring(i + 2, i + 6);
                try {
                    out.append((char) Integer.parseInt(hex, 16));
                } catch (NumberFormatException nfe) {
                    nfe.fillInStackTrace();
                }
                i = i + 6;
            } else {
                out.append(src.charAt(i));
                ++i;
            }
        }
        return out.toString();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder b = chain.request().newBuilder();
        //Request request = Apn.getRequestBuilder(b).build();
        Response response = chain.proceed(b.build());
        if (!response.isSuccessful()) {
            printResult(response, "");
            return response;
        }

        ResponseBody body = response.body();
        if (body == null) {
            printResult(response, "");
            return response;
        }

        MediaType mediaType = body.contentType();
        String result = body.string();
        int code = response.code();
        String msg = response.message();

        printResult(response, result);

        return response.newBuilder().body(ResponseBody.create(mediaType, result)).code(code).headers(response.headers()).message(msg).build();
    }
}
