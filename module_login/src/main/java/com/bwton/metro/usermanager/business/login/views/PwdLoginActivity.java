package com.bwton.metro.usermanager.business.login.views;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bwton.metro.base.BaseActivity;
import com.bwton.metro.base.trace.TraceManager;
import com.bwton.metro.uikit.BwtTopBarView;
import com.bwton.metro.usermanager.R;
import com.bwton.metro.usermanager.R2;
import com.bwton.metro.usermanager.business.login.LoginContract;
import com.bwton.metro.usermanager.business.login.presenter.LoginPresenter;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by hw on 17/12/6.<br>
 */

public class PwdLoginActivity extends BaseActivity implements LoginContract.View {

    @BindView(R2.id.topbar_header)
    BwtTopBarView mTopBar;
    @BindView(R2.id.dl_et_phone)
    MaterialEditText mEtPhone;
    @BindView(R2.id.dl_et_pwd)
    MaterialEditText mEtPwd;
    @BindView(R2.id.dl_btn_login)
    Button mBtnLogin;

    private LoginContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mEtPhone.requestFocus();
        mTopBar.setOnTopBarListener(new BwtTopBarView.OnTopBarListener() {
            @Override
            public void onClickBack() {
                finish();
            }

            @Override
            public void onClickRight() {
            }
        });

        mPresenter = new LoginPresenter(this);
        mPresenter.attachView(this);
        mPresenter.clearUserInfoBeforeLogin();
        mPresenter.getLastLoginMobile();
    }

    @Override
    public int getLayoutId() {
        return R.layout.user_activity_pwd_login;
    }

    @Override
    public String getPageTitle() {
        return "密码登录";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @OnClick({R2.id.dl_btn_login, R2.id.dl_tv_regist, R2.id.dl_tv_find_pwd})
    public void onClick(View v) {
        if (v.getId() == R.id.dl_btn_login) {
            String phone = mEtPhone.getText().toString();
            String pwd = mEtPwd.getText().toString();
            TraceManager.getInstance().onEvent("login_submit");
            mPresenter.doLoginByPwd(phone, pwd);
        } else if (v.getId() == R.id.dl_tv_regist) {
            mPresenter.clickRegister();
        } else if (v.getId() == R.id.dl_tv_find_pwd) {
            mPresenter.clickForgetPwd();
        }
    }

    @OnTextChanged(value = {R2.id.dl_et_phone, R2.id.dl_et_pwd}, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void textChanged(Editable s) {
        mPresenter.onPwdLoginContentChanged(mEtPhone.getText().toString(), mEtPwd.getText().toString());
    }

    @Override
    public void showLastLoginMobile(String mobile) {
        mEtPhone.setText(mobile);
    }

    @Override
    public void updateAgreementStatus(boolean checked) {

    }

    @Override
    public void updateSubmitButtonStatus(boolean enabled) {
        mBtnLogin.setEnabled(enabled);
    }
}
