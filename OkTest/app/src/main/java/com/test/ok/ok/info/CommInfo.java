package com.test.ok.ok.info;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.test.ok.bean.BaseBean;
import com.test.ok.ok.err.Err;
import com.test.ok.ok.err.OkErr;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by Kellan on 2017/8/9.
 */
public abstract class CommInfo<T extends BaseBean> extends BaseInfo<T> implements Callback {
    protected static Handler handler = new Handler(Looper.getMainLooper());//只有一个实例

    public CommInfo() {
    }

    @Override
    public void onFailure(Call call, IOException e) {
        failure(new OkErr(Err.ERR_NET, Err.ERR_MSG_NET, e));
    }

    @Override
    public void onResponse(Call call, Response response) {
        ResponseBody responseBody = response.body();
        if (!response.isSuccessful()) {
            failure(new OkErr(response.code(), "服务器错误" + response.code()));
            return;
        }

        T t;
        try {
            t = new Gson().fromJson(responseBody.string(), ((ParameterizedType) (getClass().getGenericSuperclass())).getActualTypeArguments()[0]);
        } catch (Exception e) {
            failure(new OkErr(Err.ERR_PARSE, Err.ERR_MSG_PARSE, e));
            return;
        }

        if(t == null){
            failure(new OkErr(Err.ERR_PARSE, Err.ERR_MSG_PARSE));
            return;
        }

        if(t.code != 0){
            failure(new OkErr(Err.ERR_N, t.msg));
            return;
        }

        response(t);
    }

    @Override
    public void failure(OkErr err) {
        handler.post(() -> fail(err));
    }

    @Override
    public void response(T t) {
        handler.post(() -> succ(t));
    }

    public abstract void succ(T t);

    public abstract void fail(OkErr e);
}

