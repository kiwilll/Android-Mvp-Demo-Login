package com.hw.mvpbase.uikit.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.TextView;

import com.hw.mvpbase.R;


/**
 * Created by hw on 17/12/1.<br>
 */

public class BwtAlertDialog extends BaseDialog {

    private TextView mTvTitle;
    private TextView mTvMsg;
    private Button mBtnLeft;

    private ViewStub mViewStubRight;
    private ViewStub mViewStubCenter;

    private Button mBtnRight;
    private Button mBtnCenter;

    public BwtAlertDialog(Context context) {
        super(context);
        setContentView(R.layout.bwt_dialog_alert);
        mTvTitle = (TextView) findViewById(R.id.tv_dialog_title);
        mTvMsg = (TextView) findViewById(R.id.tv_dialog_msg);
        mBtnLeft = (Button) findViewById(R.id.btn_dialog_left);     //默认layout里只显示一个button
        mViewStubCenter = (ViewStub) findViewById(R.id.vs_dialog_center);
        mViewStubRight = (ViewStub) findViewById(R.id.vs_dialog_right);
    }

    private void applyBuildInfo(final BuildInfo buildInfo) {
        if(!TextUtils.isEmpty(buildInfo.title)) {
            mTvTitle.setText(buildInfo.title);
            mTvMsg.setTextSize(14);
        } else {// 当标题为空时，内容控件字号增加到18
            mTvTitle.setText("");
            mTvMsg.setTextSize(18);
        }
        mTvMsg.setText(buildInfo.message);
        CharSequence[] buttons = buildInfo.buttons;
        if(buttons == null || buttons.length == 0) {
            //如果没有设置过button, 默认显示"确定"
            mBtnLeft.setText(R.string.bwt_confirm);
        } else if(buttons.length == 1) {
            mBtnLeft.setText(buttons[0]);
        }
        mBtnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(buildInfo.listener != null) {
                    buildInfo.listener.onClick(BwtAlertDialog.this, 0);
                }
            }
        });
        if(buttons != null && buttons.length == 2) {
            View view = mViewStubRight.inflate();
            mBtnRight = (Button)view.findViewById(R.id.btn_dialog_right);
            mBtnLeft.setText(buttons[0]);
            mBtnRight.setText(buttons[1]);
            mBtnRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    if(buildInfo.listener != null) {
                        buildInfo.listener.onClick(BwtAlertDialog.this, 1);
                    }
                }
            });
        } else if(buttons != null && buttons.length == 3) {
            View viewCenter = mViewStubCenter.inflate();
            View viewRight = mViewStubRight.inflate();
            mBtnCenter = (Button) viewCenter.findViewById(R.id.btn_dialog_center);
            mBtnRight = (Button) viewRight.findViewById(R.id.btn_dialog_right);
            mBtnLeft.setText(buttons[0]);
            mBtnCenter.setText(buttons[1]);
            mBtnRight.setText(buttons[2]);
            mBtnCenter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    if(buildInfo.listener != null) {
                        buildInfo.listener.onClick(BwtAlertDialog.this, 1);
                    }
                }
            });
            mBtnRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    if(buildInfo.listener != null) {
                        buildInfo.listener.onClick(BwtAlertDialog.this, 2);
                    }
                }
            });
        }
    }

    public static class Builder {

        private Context mContext;
        private CharSequence mTitle;
        private CharSequence mMessage;
        private CharSequence[] mButtons;
        private OnClickListener mListener;

        public Builder(Context context) {
            mContext = context;
        }

        /**
         * 设置dialog标题
         *
         * @param title
         * @return
         */
        public Builder setTitle(CharSequence title) {
            mTitle = title;
            return this;
        }

        /**
         * 设置dialog内容
         *
         * @param msg
         * @return
         */
        public Builder setMessage(CharSequence msg) {
            mMessage = msg;
            return this;
        }

        /**
         * 设置dialog底部显示button, 按从左往右的顺序排列<br/>
         *
         * @param buttons 最多显示3个, 如果不传默认显示一个"确认"button
         * @param listener
         * @return
         */
        public Builder setButtons(CharSequence[] buttons, OnClickListener listener) {
            mButtons = buttons;
            mListener = listener;
            return this;
        }

        public BwtAlertDialog create() {
            BwtAlertDialog dialog = new BwtAlertDialog(mContext);
            BuildInfo buildInfo = new BuildInfo();
            buildInfo.title = mTitle;
            buildInfo.message = mMessage;
            buildInfo.buttons = mButtons;
            buildInfo.listener = mListener;
            dialog.applyBuildInfo(buildInfo);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            return dialog;
        }
    }

    private static class BuildInfo {
        CharSequence title;
        CharSequence message;
        CharSequence[] buttons;
        OnClickListener listener;
    }


}
