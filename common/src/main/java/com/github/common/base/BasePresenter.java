package com.github.common.base;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * company 重庆庆云石油工程技术有限责任公司
 * FileName BasePresenter
 * Package com.kingyun.common.base
 * Description ${DESCRIPTION}
 * author jiexiaoqiang
 * create 2019-01-15 10:43
 * version V1.0
 */
public class BasePresenter<T> implements IPresenter<T> {
    private CompositeDisposable mCompositeDisposable;
    protected T mView;

    public BasePresenter(T mView) {
        this.mView = mView;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void onDestroy() {
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
