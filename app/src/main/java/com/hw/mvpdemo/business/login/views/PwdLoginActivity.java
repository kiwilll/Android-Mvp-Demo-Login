package com.hw.mvpdemo.business.login.views;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;

import com.hw.mvpbase.baseview.BaseActivity;
import com.hw.mvpbase.uikit.BwtTopBarView;
import com.hw.mvpdemo.R;
import com.hw.mvpdemo.business.login.LoginContract;
import com.hw.mvpdemo.business.login.presenter.LoginPresenter;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;


/**
 * Created by hw on 17/12/6.<br>
 */

public class PwdLoginActivity extends BaseActivity implements LoginContract.View {

    @BindView(R.id.topbar_header)
    BwtTopBarView mTopBar;
    @BindView(R.id.dl_et_phone)
    MaterialEditText mEtPhone;
    @BindView(R.id.dl_et_pwd)
    MaterialEditText mEtPwd;
    @BindView(R.id.dl_btn_login)
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

    @OnClick({R.id.dl_btn_login, R.id.dl_tv_regist, R.id.dl_tv_find_pwd})
    public void onClick(View v) {
        if (v.getId() == R.id.dl_btn_login) {
            String phone = mEtPhone.getText().toString();
            String pwd = mEtPwd.getText().toString();
            mPresenter.doLoginByPwd(phone, pwd);
        } else if (v.getId() == R.id.dl_tv_regist) {
            mPresenter.clickRegister();
        } else if (v.getId() == R.id.dl_tv_find_pwd) {
            mPresenter.clickForgetPwd();
        }
    }

    @OnTextChanged(value = {R.id.dl_et_phone, R.id.dl_et_pwd}, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
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
