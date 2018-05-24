package com.test.ok.ok;

import android.os.Handler;
import android.os.Looper;

import com.test.ok.ok.info.UpInfo;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

/**
 * Created by Kellan on 2017/12/5.
 */

public class UpLoadFileRequestBody extends RequestBody {
    private static final int SEGMENT_SIZE = 1024; // okio.Segment.SIZE

    private final File file;
    private final UpInfo listener;
    private final MediaType contentType;
    private Handler h;

    public UpLoadFileRequestBody(File file, MediaType contentType, UpInfo listener) {
        this.file = file;
        this.contentType = contentType;
        this.listener = listener;
        this.h = new Handler(Looper.getMainLooper());
    }

    @Override
    public long contentLength() {
        return file.length();
    }

    @Override
    public MediaType contentType() {
        return contentType;
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        Source source = null;
        try {
            source = Okio.source(file);
            long total = 0;
            long read;

            while ((read = source.read(sink.buffer(), SEGMENT_SIZE)) != -1) {
                total += read;
                sink.flush();

                final double p = (total * 100 / contentLength());
                if (listener != null) {
                    h.post(() -> listener.update((int) p));
                }
            }
        } finally {
            Util.closeQuietly(source);

            if (listener != null) {
                h.post(() -> listener.done());
            }
        }
    }
}
