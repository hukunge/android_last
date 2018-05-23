package com.test.ok.ok.info;

import android.app.Activity;
import android.app.Dialog;

import com.google.gson.Gson;
import com.test.ok.util.DialogUtil;
import com.test.ok.ok.OkErr;
import com.test.ok.util.MyUtil;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.ParameterizedType;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by Kellan on 2017/8/9.
 */
public abstract class OkInfo<T> extends BaseInfo<T> implements Callback {
    private WeakReference<Dialog> weakDialog;
    private WeakReference<Activity> weakAct;

    public OkInfo(Activity activity) {
        weakAct = new WeakReference<>(activity);
    }

    @Override
    public void onFailure(Call call, IOException e) {
        failure(new OkErr(1000, e.getMessage()));
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
            failure(new OkErr(1001, e.getMessage()));
        }
    }

    @Override
    public void response(T t) {
        diss();

        Activity act = weakAct.get();
        if(MyUtil.isDead(act))
            return;

        act.runOnUiThread(() -> succ(t));
    }

    @Override
    public void failure(OkErr e) {
        diss();

        Activity act = weakAct.get();
        if(MyUtil.isDead(act))
            return;

        act.runOnUiThread(() -> fail(e));
    }

    public void show() {
        if (weakAct == null || MyUtil.isDead(weakAct.get()))
            return;

        weakDialog = new WeakReference<>(DialogUtil.get(weakAct.get()));
        weakDialog.get().show();
    }

    private void diss() {
        if(weakDialog == null || weakDialog.get() == null)
            return;

        if (weakDialog.get().isShowing()) {
            weakDialog.get().dismiss();
        }
    }

    public abstract void succ(T t);

    public abstract void fail(OkErr e);
}

