package com.hw.mvpdemo.business.resetpwd.views;

import android.os.Bundle;

/**
 * Created by hw on 17/12/25.<br>
 */

public class ModifyPwdActivity extends ResetPwdActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAction = ACTION_MODIFY_PWD;
        super.onCreate(savedInstanceState);
    }
}
