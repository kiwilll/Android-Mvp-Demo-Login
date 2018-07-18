package com.hw.mvpdemo.business.resetpwd.views;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;


import com.hw.mvpbase.baseview.BaseActivity;
import com.hw.mvpbase.uikit.BwtTopBarView;
import com.hw.mvpdemo.R;
import com.hw.mvpdemo.api.UserApi;
import com.hw.mvpdemo.business.code.VerifyCodeContract;
import com.hw.mvpdemo.business.code.VerifyCodePresenter;
import com.hw.mvpdemo.business.resetpwd.ResetPwdContract;
import com.hw.mvpdemo.business.resetpwd.presenter.ResetPwdPresenter;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by hw on 17/12/8.<br>
 */

public class ResetPwdActivity extends BaseActivity implements ResetPwdContract.View, VerifyCodeContract.View {

    public static final int ACTION_RESET_PWD = 0;
    public static final int ACTION_MODIFY_PWD = 1;

    @BindView(R.id.topbar_header)
    BwtTopBarView mTopBar;
    @BindView(R.id.et_phone)
    MaterialEditText mEtPhone;
    @BindView(R.id.et_code)
    MaterialEditText mEtCode;
    @BindView(R.id.btn_get_code)
    Button mBtnGetCode;
    @BindView(R.id.ed_pwd_1)
    MaterialEditText mEtPwd1;
    @BindView(R.id.ed_pwd_2)
    MaterialEditText mEtPwd2;
    @BindView(R.id.btn_login)
    Button mBtnLogin;
    @BindView(R.id.rl_edit_phone)
    RelativeLayout mRlPhone;

    private ResetPwdContract.Presenter mPresenter;
    private VerifyCodeContract.Presenter mVerifyCodePresenter;

    //0-找回密码 1-修改密码
    protected int mAction = ACTION_RESET_PWD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTopBar.setOnTopBarListener(new BwtTopBarView.OnTopBarListener() {
            @Override
            public void onClickBack() {
                finish();
            }

            @Override
            public void onClickRight() {
            }
        });

        mPresenter = new ResetPwdPresenter(this);
        mPresenter.attachView(this);
        mVerifyCodePresenter = new VerifyCodePresenter(this);
        mVerifyCodePresenter.attachView(this);

        mPresenter.init(mAction);
    }

    @Override
    public int getLayoutId() {
        return R.layout.user_activity_reset_pwd;
    }

    @Override
    public String getPageTitle() {
        return "重置密码";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
        mVerifyCodePresenter.detachView();
    }

    @OnClick(value = {R.id.btn_get_code, R.id.btn_login})
    public void onClick(View v) {
        if (v.getId() == R.id.btn_get_code) {
            String mobile = mEtPhone.getText().toString();
            mVerifyCodePresenter.sendVerifyCode(UserApi.CODEFLAG_FINDPWD, mobile);
        } else if (v.getId() == R.id.btn_login) {
            String mobile = mEtPhone.getText().toString();
            String code = mEtCode.getText().toString();
            String pwd1 = mEtPwd1.getText().toString();
            String pwd2 = mEtPwd2.getText().toString();
            mPresenter.doResetPwd(mobile, code, pwd1, pwd2);
        }
    }

    @OnTextChanged(value = R.id.et_phone, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void phoneTextChanged(Editable s) {
        mVerifyCodePresenter.onMobileTextChanged(s.toString());
        mPresenter.onInputContentChanged(s.toString(), mEtCode.getText().toString(), mEtPwd1.getText().toString(), mEtPwd2.getText().toString());
    }

    @OnTextChanged(value = {R.id.et_code, R.id.ed_pwd_1, R.id.ed_pwd_2}, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void textChanged(Editable s) {
        mPresenter.onInputContentChanged(mEtPhone.getText().toString(), mEtCode.getText().toString(), mEtPwd1.getText().toString(), mEtPwd2.getText().toString());
    }

    @Override
    public void showTopbarTitle(String title) {
        mTopBar.setTitle(title);
    }

    @Override
    public void hideMobileEditText() {
        mRlPhone.setVisibility(View.GONE);
    }

    @Override
    public void showMobile(String mobile) {
        mEtPhone.setText(mobile);
    }

    @Override
    public void enableSendCodeBtn() {
        mBtnGetCode.setTextColor(getResources().getColor(R.color.bwt_primary_ui_color));
        mBtnGetCode.setEnabled(true);
    }

    @Override
    public void disableSendCodeBtn() {
        mBtnGetCode.setTextColor(0xffbbbbbb);
        mBtnGetCode.setEnabled(false);
    }

    @Override
    public void focusCodeEditText() {
        mEtCode.requestFocus();
    }

    @Override
    public void showSendCodeBtnTickText(String tickText) {
        mBtnGetCode.setText(tickText);
    }

    @Override
    public void updateSubmitButtonStatus(boolean enabled) {
        mBtnLogin.setEnabled(enabled);
    }
}
