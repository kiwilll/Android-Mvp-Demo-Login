package com.bwton.login.demo;

import android.app.Application;

import com.bwton.metro.network.HttpReqManager;
import com.bwton.metro.sharedata.CityManager;
import com.bwton.modulemanager.BwtModuleManager;

/**
 * Created by hw on 18/1/2.<br>
 */

public class LoginApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        BwtModuleManager.getInstance().initModules(this);

        CityManager.setCurrentCity(3202, "无锡");
        HttpReqManager.getInstance().setCityId("3202");
    }
}
