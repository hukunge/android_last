package com.test.ok.ok.err;

/**
 * Created by Kellan on 2017/8/9.
 */
public class OkErr {
    //只要发生异常，肯定不为空
    public int code;
    public String err;

    //可能为空
    public Exception excp;

    public OkErr(int code, String err){
        this.code = code;
        this.err = err;
    }

    public OkErr(int code, String err, Exception excp){
        this.code = code;
        this.err = err;
        this.excp = excp;
    }
}
