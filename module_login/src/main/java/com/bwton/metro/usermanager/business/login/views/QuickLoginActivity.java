package com.bwton.metro.usermanager.business.login.views;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bwton.metro.base.BaseActivity;
import com.bwton.metro.base.trace.TraceManager;
import com.bwton.metro.tools.DensityUtil;
import com.bwton.metro.uikit.BwtTopBarView;
import com.bwton.metro.usermanager.R;
import com.bwton.metro.usermanager.R2;
import com.bwton.metro.usermanager.api.UserApi;
import com.bwton.metro.usermanager.business.code.VerifyCodeContract;
import com.bwton.metro.usermanager.business.code.VerifyCodePresenter;
import com.bwton.metro.usermanager.business.login.LoginContract;
import com.bwton.metro.usermanager.business.login.presenter.LoginPresenter;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by hw on 17/12/7.<br>
 */

public class QuickLoginActivity extends BaseActivity implements LoginContract.View, VerifyCodeContract.View {

    @BindView(R2.id.topbar_header)
    BwtTopBarView mTopBar;
    @BindView(R2.id.ql_et_phone)
    MaterialEditText mEtPhone;
    @BindView(R2.id.ql_et_code)
    MaterialEditText mEtCode;
    @BindView(R2.id.ql_btn_get_code)
    Button mBtnGetCode;
    @BindView(R2.id.ql_btn_login)
    Button mBtnLogin;
    @BindView(R2.id.cbAgreement)
    CheckBox mCbAgreement;

    private LoginContract.Presenter mPresenter;
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
        if (null != mCbAgreement && Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
            mCbAgreement.setPadding(DensityUtil.dp2px(this, 20), 4, 4, 4);
        }

        mPresenter = new LoginPresenter(this);
        mPresenter.attachView(this);
        mVerifyCodePresenter = new VerifyCodePresenter(this);
        mVerifyCodePresenter.attachView(this);

        mPresenter.clearUserInfoBeforeLogin();
        mPresenter.getLastLoginMobile();

        mPresenter.getAgreementStatus();
    }

    @Override
    public int getLayoutId() {
        return R.layout.user_activity_quick_login;
    }

    @Override
    public String getPageTitle() {
        return "快速登录";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
        mVerifyCodePresenter.detachView();
    }

    @OnClick({R2.id.ql_btn_get_code, R2.id.ql_btn_login, R2.id.ql_tv_regist, R2.id.ql_tv_login_pwd, R2.id.tv_quicklogin_agreenment})
    public void onClick(View v) {
        if (v.getId() == R.id.ql_btn_get_code) {
            String mobile = mEtPhone.getText().toString();
            TraceManager.getInstance().onEvent("huoquyanzhenma");
            mVerifyCodePresenter.sendVerifyCode(UserApi.CODEFLAG_LOGIN, mobile);
        } else if (v.getId() == R.id.ql_btn_login) {
            String mobile = mEtPhone.getText().toString();
            String code = mEtCode.getText().toString();
            boolean isChecked = mCbAgreement.isChecked();
            TraceManager.getInstance().onEvent("login_quick_submit");
            mPresenter.doQuickLogin(mobile, code, isChecked);
        } else if (v.getId() == R.id.ql_tv_regist) {
            mPresenter.clickRegister();
        } else if (v.getId() == R.id.ql_tv_login_pwd) {
            mPresenter.clickPwdLogin();
        } else if (v.getId() == R.id.tv_quicklogin_agreenment) {
            mPresenter.clickUserAgreement();
        }
    }

    @OnTextChanged(value = {R2.id.ql_et_phone}, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void phoneTextChanged(Editable s) {
        mVerifyCodePresenter.onMobileTextChanged(s.toString());
        mPresenter.onQuickLoginContentChanged(s.toString(), mEtCode.getText().toString());
    }

    @OnTextChanged(value = {R2.id.ql_et_code}, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void codeTextChanged(Editable s) {
        mPresenter.onQuickLoginContentChanged(mEtPhone.getText().toString(), mEtCode.getText().toString());
    }

    @Override
    public void showLastLoginMobile(String mobile) {
        mEtPhone.setText(mobile);
    }

    @Override
    public void updateAgreementStatus(boolean checked) {
        mCbAgreement.setChecked(checked);
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
