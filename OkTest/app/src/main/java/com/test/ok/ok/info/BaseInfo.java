package com.test.ok.ok.info;

import com.test.ok.ok.err.OkErr;

public abstract class BaseInfo<T> {
    public abstract void response(T t);
    public abstract void failure(OkErr e);
}
