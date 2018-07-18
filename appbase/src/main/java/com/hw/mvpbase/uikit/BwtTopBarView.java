package com.hw.mvpbase.uikit;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hw.mvpbase.R;


/**
 * Created by hw on 17/12/6.<br>
 */

public class BwtTopBarView extends RelativeLayout implements View.OnClickListener {

    public static interface OnTopBarListener {
        void onClickBack();

        void onClickRight();
    }

    private String mTitleTextStr;
    private int mTitleTextSize;
    private int mTitleTextColor;
    private Drawable mBackDrawable;
    private String mRightText;
    private int mRightTextSize;
    private ColorStateList mRightTextColor;
    private Drawable mBgDrawable;

    private TextView mTvTitle;
    private ImageView mIvBack;
    private TextView mTvRight;
    private LinearLayout mLayoutRightContainer;
    private View mViewDivider;

    private OnTopBarListener mListener;

    public BwtTopBarView(Context context) {
        this(context, null);
    }

    public BwtTopBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BwtTopBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BwtTopBar);

        mTitleTextStr = ta.getString(R.styleable.BwtTopBar_titleText);
        mTitleTextSize = ta.getDimensionPixelSize(R.styleable.BwtTopBar_titleSize,
                (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 18, getResources().getDisplayMetrics())));
        mTitleTextColor = ta.getColor(R.styleable.BwtTopBar_titleColor, getResources().getColor(R.color.bwt_title_center_text));
        mBackDrawable = ta.getDrawable(R.styleable.BwtTopBar_backIcon);

        mRightText = ta.getString(R.styleable.BwtTopBar_rightText);
        mRightTextSize = ta.getDimensionPixelSize(R.styleable.BwtTopBar_rightTextSize,
                (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 15, getResources().getDisplayMetrics())));
        mRightTextColor = ta.getColorStateList(R.styleable.BwtTopBar_rightTextColor);
        if (mRightTextColor == null) {
            mRightTextColor = ContextCompat.getColorStateList(context, R.color.bwt_title_right_text);
        }

        mBgDrawable = ta.getDrawable(R.styleable.BwtTopBar_titleBackground);
        if(mBgDrawable == null) {
            mBgDrawable = new ColorDrawable(context.getResources().getColor(R.color.bwt_title_bg_color));
        }
        initView(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w = getMeasuredWidth();
        //设置固定高度为48dip
        int h = (int)(getResources().getDisplayMetrics().density * 48);
        setMeasuredDimension(w, h);
    }

    private void initView(Context context) {
        View layout = LayoutInflater.from(context).inflate(R.layout.bwt_top_bar, null);
        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                (int)(context.getResources().getDisplayMetrics().density * 48)
        );
        addView(layout, params);

        mTvTitle = layout.findViewById(R.id.tv_topbar_title);
        mIvBack = layout.findViewById(R.id.iv_topbar_back);
        mLayoutRightContainer = layout.findViewById(R.id.ll_topbar_rightcontianer);
        mTvRight = layout.findViewById(R.id.tv_topbar_right);
        mViewDivider = layout.findViewById(R.id.view_topbar_divider);

        if (!TextUtils.isEmpty(mTitleTextStr))
            mTvTitle.setText(mTitleTextStr);
        mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleTextSize);
        mTvTitle.setTextColor(mTitleTextColor);

        if (mBackDrawable != null)
            mIvBack.setImageDrawable(mBackDrawable);

        if (TextUtils.isEmpty(mRightText)) {
            mTvRight.setVisibility(View.GONE);
        } else {
            mTvRight.setVisibility(View.VISIBLE);
            mTvRight.setText(mRightText);
        }
        mTvRight.setTextSize(TypedValue.COMPLEX_UNIT_PX, mRightTextSize);
        mTvRight.setTextColor(mRightTextColor);

        if(mBgDrawable != null) {
            layout.setBackground(mBgDrawable);
        }

        mIvBack.setOnClickListener(this);
        mTvRight.setOnClickListener(this);
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(CharSequence title) {
        mTvTitle.setText(title);
    }

    /**
     * 获取标题
     *
     * @return
     */
    public CharSequence getTitle() {
        return mTvTitle.getText();
    }

    /**
     * 设置右边显示操作item文字
     *
     * @param rightText 为空则隐藏
     */
    public void setRightText(CharSequence rightText) {
        mTvRight.setText(rightText);
        if (TextUtils.isEmpty(rightText)) {
            mTvRight.setVisibility(View.GONE);
        } else {
            mTvRight.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 返回按钮显示成叉叉
     */
    public void showLeftCloseButton() {
        mIvBack.setVisibility(VISIBLE);
        mIvBack.setImageResource(R.drawable.bwt_ic_header_close);
    }

    public void setOnTopBarListener(OnTopBarListener listener) {
        mListener = listener;
    }

    /**
     * 隐藏返回图标
     */
    public void hideBackIcon() {
        mIvBack.setVisibility(View.GONE);
    }

    /**
     * 显示返回按钮
     */
    public void showBackIcon() {
        mIvBack.setVisibility(View.VISIBLE);
    }

    /**
     * 是否显示标题左边的loading
     *
     * @param showLoading
     */
    public void showLoading(boolean showLoading) {
//        mLoadingView.setVisibility(showLoading ? View.VISIBLE : View.GONE);
    }

    /**
     * 右边添加更多菜单按钮
     *
     * @param view
     * @param listener
     */
    public void addRightMenu(View view, OnClickListener listener) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        view.setOnClickListener(listener);
        mLayoutRightContainer.addView(view, 0, params);
    }

    public View getDividerView() {
        return mViewDivider;
    }

    @Override
    public void onClick(View v) {
        if (v == mIvBack) {
            if (mListener != null) {
                mListener.onClickBack();
            }
        } else if (v == mTvRight) {
            if (mListener != null) {
                mListener.onClickRight();
            }
        }
    }
}