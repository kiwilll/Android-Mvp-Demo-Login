package com.hw.mvpbase.baseview.mvp;

import android.support.annotation.NonNull;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


/**
 * Created by hw on 5/11/17.<br>
 */

public abstract class AbstractPresenter<T extends BaseView> implements BasePresenter<T> {

    private T mMvpView;

    protected CompositeDisposable mCompositeDisposable;

    @Override
    public void attachView(@NonNull T view) {
        mMvpView = view;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void detachView() {
        if(mCompositeDisposable != null && !mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.clear();
            mCompositeDisposable = null;
        }
        mMvpView = null;
    }

    protected void addDisposable(Disposable disposable) {
        if(disposable == null || mCompositeDisposable == null)
            return;
        mCompositeDisposable.add(disposable);
    }

    protected void removeDisposable(Disposable disposable) {
        if(disposable == null || mCompositeDisposable == null)
            return;
        mCompositeDisposable.remove(disposable);
    }

    protected void dispose(Disposable disposable) {
        if(disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    public T getView() {
        return mMvpView;
    }

}
