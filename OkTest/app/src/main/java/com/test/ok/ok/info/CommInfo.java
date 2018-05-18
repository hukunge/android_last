package com.test.ok.ok.info;

import android.os.Handler;

import com.google.gson.Gson;
import com.test.ok.ok.OkErr;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by Kellan on 2017/8/9.
 */
public abstract class CommInfo<T> extends BaseInfo<T> implements Callback {
    protected static Handler handler = new Handler();//只有一个实例
    public CommInfo() {
    }

    @Override
    public void onFailure(Call call, IOException e) {
        failure(new OkErr(-100, "网络异常，请稍后重试"));
    }

    @Override
    public void onResponse(Call call, Response response) {
        try {
            ResponseBody responseBody = response.body();
            if (!response.isSuccessful()) {
                failure(new OkErr(response.code(), response.message()));
                return;
            }

            T t = new Gson().fromJson(responseBody.string(), ((ParameterizedType) (getClass().getGenericSuperclass())).getActualTypeArguments()[0]);
            response(t);
        } catch (Exception e) {
            failure(new OkErr(-300, "服务器异常"));
        }
    }

    @Override
    public void failure(OkErr e) {
        handler.post(() -> fail(e));
    }

    @Override
    public void response(T t) {
        handler.post(() -> succ(t));
    }

    public abstract void succ(T t);

    public abstract void fail(OkErr e);
}

