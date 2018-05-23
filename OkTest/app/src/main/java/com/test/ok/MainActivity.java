package com.test.ok;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.test.ok.bean.TestBean;
import com.test.ok.ok.Ok;
import com.test.ok.ok.err.OkErr;
import com.test.ok.ok.down.DownInfo;
import com.test.ok.ok.info.OkInfo;
import com.test.ok.ok.up.UpInfo;
import com.test.ok.permission.EasyPermissions;
import com.test.ok.permission.my.PermissionInfo;
import com.test.ok.util.LogUtil;

import java.io.File;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * Created by Kellan on 2017/8/9.
 */
public class MainActivity extends AppCompatActivity {
    private String[] ALL = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,//存储
            Manifest.permission.READ_EXTERNAL_STORAGE,//存储
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EasyPermissions.of(MainActivity.this)
                .reqCode(111)
                .perms(ALL)
                .callBack(new PermissionInfo() {
                    @Override
                    public void onGranted(List<String> perms) {
                    }

                    @Override
                    public void onDenied(List<String> perms) {
                    }
                });
    }

    public void onClickGet(View view) {
        String url = "https://api.weibo.com/2/search/topics.json";

        OkInfo info = new OkInfo<TestBean>(MainActivity.this) {
            @Override
            public void succ(TestBean b) {
                Toast.makeText(MainActivity.this, "succ code : " + b.error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void fail(OkErr e) {
                Toast.makeText(MainActivity.this, "err code : " + e.code + "\nerr msg:" + e.err, Toast.LENGTH_SHORT).show();
            }
        };

        Ok.get(url, info, true);
    }

    public void onClickPost(View view) {
        String url = "https://api.weibo.com/2/comments/create.json";
        RequestBody b = new FormBody.Builder()
                .add("search", "Jurassic Park")
                .build();

        OkInfo info = new OkInfo<TestBean>(MainActivity.this) {
            @Override
            public void succ(TestBean b) {
                Toast.makeText(MainActivity.this, "succ code : " + b.error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void fail(OkErr e) {
                Toast.makeText(MainActivity.this, "err code : " + e.code + "\nerr msg:" + e.err, Toast.LENGTH_SHORT).show();
            }
        };

        Ok.post(url, b, info, true);
    }

    public void onClickUpload(View view) {
        String url = "http://test.fykc.com/common/uploadImage/";
        File f = new File(Environment.getExternalStorageDirectory(), "test.jpg");

        UpInfo p = new UpInfo() {
            @Override
            public void update(int per) {
                LogUtil.e("kunge.hu", "per : " + per);
            }

            @Override
            public void done() {
                LogUtil.e("kunge.hu", "done");
            }
        };

        OkInfo info = new OkInfo<TestBean>(MainActivity.this) {
            @Override
            public void succ(TestBean b) {
                Toast.makeText(MainActivity.this, "succ code : " + b.error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void fail(OkErr e) {
                Toast.makeText(MainActivity.this, "err code : " + e.code + "\nerr msg:" + e.err, Toast.LENGTH_SHORT).show();
            }
        };

        Ok.upload(url, f, p, info);
    }

    public void onClickDownLoad(View view) {
        String url = "http://pic.58pic.com/58pic/15/57/84/70H58PICCJt_1024.jpg";
        File f = new File(Environment.getExternalStorageDirectory(), "test.jpg");

        DownInfo info = new DownInfo() {
            @Override
            public void update(Integer t) {
                LogUtil.e("", "update : " + t);
            }

            @Override
            public void error(Throwable e) {
                LogUtil.e("", "onError : " + e.getMessage());
            }

            @Override
            public void done() {
                LogUtil.e("", "onComplete " + isMain());
            }
        };
        Ok.download(url, f, info);
    }

    private static boolean isMain() {
        return Looper.getMainLooper() == Looper.myLooper();
    }
}
