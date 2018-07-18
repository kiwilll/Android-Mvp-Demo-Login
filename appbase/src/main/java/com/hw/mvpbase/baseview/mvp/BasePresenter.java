package com.hw.mvpbase.baseview.mvp;

import android.support.annotation.NonNull;

/**
 * Created by hw on 1/20/17.<br>
 */

public interface BasePresenter<T extends BaseView>{

    void attachView(@NonNull T view);

    void detachView();

}
