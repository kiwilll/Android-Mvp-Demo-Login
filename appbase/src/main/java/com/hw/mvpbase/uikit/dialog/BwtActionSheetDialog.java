package com.hw.mvpbase.uikit.dialog;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hw.mvpbase.R;


/**
 * Created by hasee on 2017/12/26.
 */
//------------------------------调用范例---------------------------------
//       BwtActionSheetDialog.Builder builder = new BwtActionSheetDialog.Builder(this)
//                        .setTitle("")//传空或不传则没有标题
//                        .setOptions(new String[]{"拍照", "相册"}, new DialogInterface.OnClickListener() {
//                          @Override
//                              public void onClick(DialogInterface dialog, int which) {
//                               }
//                       });
//        builder.create().show();
public class BwtActionSheetDialog extends BaseDialog {

    private TextView mTvTitleView; //标题
    private Button mBtnCancelView; //取消按钮
    private LinearLayout mLlOptionLayout;

    public BwtActionSheetDialog(Context context) {
        super(context);
        setContentView(R.layout.bwt_dialog_actionsheet);
        mTvTitleView = (TextView) findViewById(R.id.tv_dialog_title);
        mLlOptionLayout = (LinearLayout) findViewById(R.id.ll_option_layout);
        mBtnCancelView = (Button) findViewById(R.id.btn_dialog_cancel);

        configWindowAttr(Gravity.BOTTOM, R.style.BwtBottomMenuAnimation, 12);
    }

    private void applyBuildInfo(final BuildInfo buildInfo) {
        LinearLayout.LayoutParams layoutParams;
        int optionLen = buildInfo.options.length;
        boolean showtitle = !TextUtils.isEmpty(buildInfo.title);
        if (showtitle) {
            mTvTitleView.setText(buildInfo.title);
            mTvTitleView.setVisibility(View.VISIBLE);
        }

        for (int i = 0; i < optionLen; i++) {

            if (i == 0 && showtitle) {
                TextView line = new TextView(getContext());
                layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
                line.setBackgroundColor(Color.parseColor("#e0e0e0"));
                line.setLayoutParams(layoutParams);
                mLlOptionLayout.addView(line);
            }

            final int position = i;
            Button button = getMenuOption(getContext(), buildInfo.options[i]);
            if (i == 0) {
                if (optionLen > 1) {
                    if (showtitle)
                        button.setBackground(getContext().getResources().getDrawable(R.drawable.bwt_bg_dialog_actionsheet_middle_selector));
                    else
                        button.setBackground(getContext().getResources().getDrawable(R.drawable.bwt_bg_dialog_actionsheet_top_selector));
                } else
                    button.setBackground(getContext().getResources().getDrawable(R.drawable.bwt_bg_dialog_actionsheet_selector));
            }
            if (i > 0 && i == optionLen - 1) {
                button.setBackground(getContext().getResources().getDrawable(R.drawable.bwt_bg_dialog_actionsheet_down_selector));
            }

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    buildInfo.listener.onClick(BwtActionSheetDialog.this, position);
                }
            });

            mLlOptionLayout.addView(button);
            if (i < optionLen - 1) {
                TextView line = new TextView(getContext());
                layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2);
                line.setBackgroundColor(Color.parseColor("#e0e0e0"));
                line.setLayoutParams(layoutParams);
                mLlOptionLayout.addView(line);
            }
        }

        mBtnCancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    private Button getMenuOption(Context context, CharSequence s) {
        Button button = new Button(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp2px(context, 45));
        button.setGravity(Gravity.CENTER);
        button.setTextColor(Color.DKGRAY);
        button.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        button.setBackground(context.getResources().getDrawable(R.drawable.bwt_bg_dialog_actionsheet_middle_selector));
        button.setText(s);
        button.setLayoutParams(layoutParams);
        return button;
    }

    private int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    public static class Builder {

        private Context mContext;
        private CharSequence mTitle;
        private CharSequence[] mOptions;
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
         * 设置dialog底部显示button, 按从上往下的顺序排列<br/>
         *
         * @param options
         * @param listener
         * @return
         */
        public Builder setOptions(CharSequence[] options, OnClickListener listener) {
            mOptions = options;
            mListener = listener;
            return this;
        }

        public BwtActionSheetDialog create() {
            BwtActionSheetDialog dialog = new BwtActionSheetDialog(mContext);
            BuildInfo buildInfo = new BuildInfo();
            buildInfo.title = mTitle;
            buildInfo.options = mOptions;
            buildInfo.listener = mListener;
            dialog.applyBuildInfo(buildInfo);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
            return dialog;
        }
    }

    private static class BuildInfo {
        CharSequence title;
        CharSequence[] options;
        OnClickListener listener;
    }
}
