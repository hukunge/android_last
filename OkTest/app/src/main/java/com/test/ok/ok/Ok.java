package com.test.ok.ok;

import android.support.annotation.NonNull;

import com.test.ok.ok.info.DownInfo;
import com.test.ok.ok.info.OkInfo;
import com.test.ok.ok.interceptor.StringInterceptor;
import com.test.ok.ok.info.UpInfo;

import java.io.File;
import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

/**
 * Created by Kellan on 2017/8/9.
 */
public class Ok {
    private static final int BufferSize = 8 * 1024;
    private static final MediaType MEDIA_TYPE_ALL = MediaType.parse("image/*");

    public static void get(String url, OkInfo info, boolean b){
        if(b) info.show();

        Request request = new Request.Builder()
                .url(url)
                .build();

        ClientHelper.get().getClientString().newCall(request).enqueue(info);
    }

    public static void post(String url, Params map, OkInfo info, boolean b){
        if(b) info.show();

        Request request = new Request.Builder()
                .url(url)
                .post(map.build())
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

    public static void download(@NonNull final String url, @NonNull final File destFile, DownInfo info) {
        if (destFile.exists()) destFile.delete();
        if (!destFile.getParentFile().exists()) destFile.mkdirs();

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<Integer> subscriber) throws Exception {
                BufferedSink sink = null;
                BufferedSource source = null;
                try {
                    Request request = new Request.Builder().url(url).build();
                    StringInterceptor.printUrl(request);

                    Response response = ClientHelper.get().getClientClean().newCall(request).execute();
                    ResponseBody body = response.body();
                    long contentLength = body.contentLength();
                    source = body.source();

                    sink = Okio.buffer(Okio.sink(destFile));
                    Buffer sinkBuffer = sink.buffer();
                    long totalBytesRead = 0;
                    long bytesRead;

                    while ((bytesRead = source.read(sinkBuffer, BufferSize)) != -1) {
                        sink.emit();
                        totalBytesRead += bytesRead;
                        int progress = (int) ((totalBytesRead * 100) / contentLength);
                        subscriber.onNext(progress);
                    }

                    sink.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    Util.closeQuietly(sink);
                    Util.closeQuietly(source);
                }

                subscriber.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(info);
    }
}
