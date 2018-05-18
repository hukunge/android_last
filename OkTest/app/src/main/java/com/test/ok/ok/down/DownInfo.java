package com.test.ok.ok.down;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class DownInfo implements Observer<Integer> {
    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(Integer t) {
        update(t);
    }

    @Override
    public void onError(Throwable e) {
        error(e);
        done();
    }

    @Override
    public void onComplete() {
        done();
    }

    public abstract void update(Integer t);

    public abstract void error(Throwable e);

    public abstract void done();
}
