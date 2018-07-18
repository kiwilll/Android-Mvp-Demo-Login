package com.hw.mvpbase.uikit.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.StyleRes;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.hw.mvpbase.R;


/**
 * Created by hw on 17/12/1.<br>
 */
public class BaseDialog extends Dialog {

    public BaseDialog(Context context) {
        super(context, R.style.BwtAlertDialogStyle);
        configWindowAttr(Gravity.CENTER, R.style.BwtCenterAlphaAnimation, getMargin());
    }


    /**
     * 设置window属性
     *
     * @param gravity
     * @param animResId
     * @param margin    小于0时不设置宽度
     */
    protected void configWindowAttr(int gravity, @StyleRes int animResId, int margin) {
        Window window = getWindow();
        window.setWindowAnimations(animResId);
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams wl = window.getAttributes();
        DisplayMetrics displayParams = getContext().getResources().getDisplayMetrics();
        int mwidth = displayParams.widthPixels;

        if (margin > 0) {
            wl.width = mwidth - (int) (displayParams.density * margin);
        } else if (margin == 0)
            wl.width = mwidth;

        wl.gravity = gravity;
        window.setAttributes(wl);
    }

    protected int getMargin() {
        return 70;
    }

}
