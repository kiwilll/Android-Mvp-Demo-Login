package com.hw.mvpbase.uikit;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.hw.mvpbase.R;
import com.rengwuxian.materialedittext.MaterialEditText;


/**
 * Created by hw on 18/3/12.<br>
 */

public class BwtMaterialEditText extends MaterialEditText implements View.OnFocusChangeListener, TextWatcher {

    private Drawable mClearDrawable;

    //触摸叉叉的左右的范围加大
    private int mTouchDelta;

    public BwtMaterialEditText(Context context) {
        super(context);
        init();
    }

    public BwtMaterialEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BwtMaterialEditText(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);
        init();
    }

    private void init() {
        mClearDrawable = getCompoundDrawables()[2];
        if (mClearDrawable == null) {
            mClearDrawable = getResources().getDrawable(R.mipmap.bwt_ic_edittext_clear);
        }
        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(), mClearDrawable.getIntrinsicHeight());
        mTouchDelta = (int) (getResources().getDisplayMetrics().density * 3);

        setClearIconVisible(false);
        setOnFocusChangeListener(this);
        addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (isFocused()) {
            setClearIconVisible(s.length() > 0);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP && getCompoundDrawables()[2] != null) {
            boolean touchable = (event.getX() > (getWidth() - getTotalPaddingRight() - mTouchDelta)) &&
                    (event.getX() < (getWidth() - getPaddingRight() + mTouchDelta));
            if (touchable) {
                setText("", TextView.BufferType.EDITABLE);
            }
        }
        return super.onTouchEvent(event);
    }

    private void setClearIconVisible(boolean visible) {
        Drawable right = visible ? mClearDrawable : null;
        Drawable[] drawables = getCompoundDrawables();
        setCompoundDrawables(drawables[0], drawables[1], right, drawables[3]);
    }

}
