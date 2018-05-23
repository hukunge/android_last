package com.test.ok.ok;

/**
 * Created by Kellan on 2017/8/9.
 */
public class OkErr {
    public int code;
    public String err;
    public Exception excp;

    public OkErr(int code, String err){
        this.code = code;
        this.err = err;
    }
}
