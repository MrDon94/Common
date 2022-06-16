package com.github.common.base;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class BasePresenter<T> implements IPresenter<T> {
    private CompositeDisposable mCompositeDisposable;
    protected T mView;

    public BasePresenter(T mView) {
        this.mView = mView;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override public void onDestroy() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
            mCompositeDisposable = null;
        }
        mView = null;
    }

    protected final void addDispose(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }
}
