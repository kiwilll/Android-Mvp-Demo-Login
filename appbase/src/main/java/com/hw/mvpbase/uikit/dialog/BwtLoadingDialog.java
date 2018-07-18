package com.hw.mvpbase.uikit.dialog;

import android.content.Context;
import android.widget.TextView;

import com.hw.mvpbase.R;


/**
 * Created by hw on 17/12/1.<br>
 */

public class BwtLoadingDialog extends BaseDialog {

    private TextView mTvMsg;

    public BwtLoadingDialog(Context context) {
        super(context);
        setContentView(R.layout.bwt_dialog_loading);
        mTvMsg = (TextView) findViewById(R.id.tv_dialog_msg);

        setCanceledOnTouchOutside(false);
    }

    public BwtLoadingDialog setLoadingMessage(CharSequence msg) {
        mTvMsg.setText(msg);
        return this;
    }

    @Override
    protected int getMargin() {
        return -1;
    }
}