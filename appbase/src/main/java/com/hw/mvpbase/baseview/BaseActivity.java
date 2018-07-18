package com.hw.mvpbase.baseview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.hw.mvpbase.baseview.mvp.BaseView;


/**
 * Created by hw on 5/17/17.<br>
 */

public abstract class BaseActivity extends FragmentActivity implements BaseView {

//    private Dialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        if (isTranslucentStatusBar()) {
            initStatusBar();
        }

//        ButterKnife.bind(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(getPageTitle()))
            Log.w("p", getPageTitle());
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissLoadingDialog();
    }

    /**
     * 当前页面所用的layout id
     *
     * @return
     */
    public abstract int getLayoutId();

    /**
     * 返回当前页面的title, 用来做日志记录时使用
     *
     * @return
     */
    public abstract String getPageTitle();

    @Override
    public void showLoadingDialog(CharSequence title, CharSequence msg) {
        showLoadingDialog(title, msg, false);
    }

    @Override
    public void showLoadingDialog(CharSequence title, CharSequence msg, boolean cancelable) {
        dismissLoadingDialog();
//        mLoadingDialog = new BwtLoadingDialog(this).setLoadingMessage(msg);
//        mLoadingDialog.setCancelable(cancelable);
//        mLoadingDialog.show();
    }

    @Override
    public void dismissLoadingDialog() {
//        if (mLoadingDialog != null) {
//            mLoadingDialog.dismiss();
//            mLoadingDialog = null;
//        }
    }

    @Override
    public void toastMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showAlertDialog(CharSequence title, CharSequence msg, CharSequence[] buttons, final OnAlertDialogCallback callback) {
//        BwtAlertDialog.Builder builder = new BwtAlertDialog.Builder(this)
//                .setTitle(title)
//                .setMessage(msg)
//                .setButtons(buttons, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (callback != null) {
//                            callback.onClick(BaseActivity.this, which);
//                        }
//                    }
//                });
//        builder.create().show();
    }

    @Override
    public void closeCurrPage() {
        finish();
    }

    @Override
    public void hideSoftKeyboard() {
//        KeyBoardUtil.hideKeyboard(this);
    }

    @Override
    public void showSoftKeyboard() {
//        KeyBoardUtil.openKeyboard(this);
    }

    private void initStatusBar() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            ViewGroup rootView = getWindow().getDecorView().findViewById(android.R.id.content);
//            rootView.setPadding(0, ScreenUtil.getStatusBarHeight(this), 0, 0);
//            getWindow().setStatusBarColor(getStatusBarColor());
//        }
    }

    /**
     * 是否设置设置沉浸式状态栏，在整个根布局加上paddingTop，其值为状态栏高度。
     * 默认从6.0开始设置
     *
     * @return
     */
    protected boolean isTranslucentStatusBar() {
    /*    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return true;
        }*/
        return false;
    }

    protected int getStatusBarColor() {
        return Color.WHITE;
    }

}
