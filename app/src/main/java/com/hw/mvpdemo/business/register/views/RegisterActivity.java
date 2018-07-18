package com.hw.mvpdemo.business.register.views;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.hw.mvpbase.baseview.BaseActivity;
import com.hw.mvpbase.uikit.BwtTopBarView;
import com.hw.mvpbase.util.DensityUtil;
import com.hw.mvpdemo.R;
import com.hw.mvpdemo.api.UserApi;
import com.hw.mvpdemo.business.code.VerifyCodeContract;
import com.hw.mvpdemo.business.code.VerifyCodePresenter;
import com.hw.mvpdemo.business.register.RegisterContract;
import com.hw.mvpdemo.business.register.presenter.RegisterPresenter;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by hw on 17/12/7.<br>
 */

public class RegisterActivity extends BaseActivity implements RegisterContract.View, VerifyCodeContract.View {

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
    @BindView(R.id.cbAgreement)
    CheckBox mCbAgreement;

    private RegisterContract.Presenter mPresenter;
    private VerifyCodeContract.Presenter mVerifyCodePresenter;

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
        mEtPhone.requestFocus();
        disableSendCodeBtn();
        if (null != mCbAgreement && Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
            mCbAgreement.setPadding(DensityUtil.dp2px(this, 20), 4, 4, 4);
        }

        mPresenter = new RegisterPresenter(this);
        mPresenter.attachView(this);
        mVerifyCodePresenter = new VerifyCodePresenter(this);
        mVerifyCodePresenter.attachView(this);

        mPresenter.getAgreementStatus();
    }

    @Override
    public int getLayoutId() {
        return R.layout.user_activity_register;
    }

    @Override
    public String getPageTitle() {
        return "注册";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
        mVerifyCodePresenter.detachView();
    }

    @OnClick({R.id.btn_get_code, R.id.btn_login, R.id.ll_agreenment_quicklogin})
    public void onClick(View v) {
        if (v.getId() == R.id.btn_get_code) {
            String mobile = mEtPhone.getText().toString();
            mVerifyCodePresenter.sendVerifyCode(UserApi.CODEFLAG_REGIST, mobile);
        } else if (v.getId() == R.id.btn_login) {
            String phone = mEtPhone.getText().toString();
            String pwd = mEtPwd1.getText().toString();
            String pwd1 = mEtPwd2.getText().toString();
            String code = mEtCode.getText().toString();
            mPresenter.doRegister(phone, pwd, pwd1, code, mCbAgreement.isChecked());
        } else if (v.getId() == R.id.ll_agreenment_quicklogin) {
            mPresenter.clickUserAgreement();
        }
    }

    @OnTextChanged(value = {R.id.et_phone}, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void phoneTextChanged(Editable s) {
        mVerifyCodePresenter.onMobileTextChanged(s.toString());
        mPresenter.onInputContentChanged(s.toString(), mEtCode.getText().toString(), mEtPwd1.getText().toString(), mEtPwd2.getText().toString());
    }

    @OnTextChanged(value = {R.id.et_code, R.id.ed_pwd_1, R.id.ed_pwd_2}, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void textChanged(Editable s) {
        mPresenter.onInputContentChanged(mEtPhone.getText().toString(), mEtCode.getText().toString(), mEtPwd1.getText().toString(), mEtPwd2.getText().toString());
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
    public void updateAgreementStatus(boolean checked) {
        mCbAgreement.setChecked(checked);
    }

    @Override
    public void updateSubmitButtonStatus(boolean enabled) {
        mBtnLogin.setEnabled(enabled);
    }
}
