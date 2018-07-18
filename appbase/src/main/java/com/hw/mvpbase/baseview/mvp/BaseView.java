package com.hw.mvpbase.baseview.mvp;

import android.content.Context;

/**
 * Created by hw on 1/20/17.<br>
 */

public interface BaseView {

    interface OnAlertDialogCallback {
        void onClick(Context context, int index);
    }

    /**
     * 显示加载dialog, 不可取消
     *
     * @param title 标题, 可以为空
     * @param msg 内容
     */
    void showLoadingDialog(CharSequence title, CharSequence msg);

    /**
     * 显示加载dialog
     *
     * @param title 标题
     * @param msg 内容
     * @param cancelable 是否可取消
     */
    void showLoadingDialog(CharSequence title, CharSequence msg, boolean cancelable);

    /**
     * 取消加载dialog
     */
    void dismissLoadingDialog();

    /**
     * 显示Toast信息
     *
     * @param msg
     */
    void toastMessage(String msg);

    /**
     * 显示alert对话框
     *
     * @param title 标题
     * @param msg 内容
     * @param buttons 对话框button, 根据数组长度从左到右排列, 为空时默认显示"取消"、"确定"
     * @param callback button点击回调处理事件, 按从左到右的顺序排列, 索引值从0开始
     */
    void showAlertDialog(CharSequence title, CharSequence msg, CharSequence[] buttons, OnAlertDialogCallback callback);

    /**
     * 隐藏软键盘
     */
    void hideSoftKeyboard();

    /**
     * 显示软键盘
     */
    void showSoftKeyboard();

    /**
     * 关闭当前页面
     */
    void closeCurrPage();

}