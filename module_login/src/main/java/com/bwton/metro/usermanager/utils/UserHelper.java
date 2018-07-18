package com.bwton.metro.usermanager.utils;

import android.content.Context;

import com.bwton.metro.sharedata.UserManager;
import com.bwton.metro.sharedata.model.BwtUserInfo;
import com.bwton.metro.usermanager.entity.LoginResultInfo;

/**
 * Created by hw on 17/12/7.<br>
 */

public class UserHelper {

    /**
     * 保存用户信息
     *
     * @param context context
     * @param resultInfo 用户信息
     * @param isLogin true表示登录后返回， false表示刷新个人用户信息时返回
     * @return 是否保存成功
     */
    public static boolean saveUserInfo(Context context, LoginResultInfo resultInfo, boolean isLogin) {
        LoginResultInfo.UserInfo loginUser = resultInfo.getUser();
        BwtUserInfo userInfo = new BwtUserInfo();
        userInfo.setUserId(loginUser.getUser_id());
        userInfo.setRegDate(loginUser.getReg_date());
        userInfo.setMobile(loginUser.getMobile_phone());
        userInfo.setIdType(loginUser.getId_type());
        userInfo.setIdNo(loginUser.getId_no());
        userInfo.setSex(loginUser.getSex());
        userInfo.setBirthday(loginUser.getBirthday());
        userInfo.setUserName(loginUser.getUser_name());
        userInfo.setImagePath(loginUser.getUser_image_path());
        userInfo.setProfession(loginUser.getProfession());
        userInfo.setCityId(loginUser.getCity_id());
        userInfo.setCityName(loginUser.getCity());
        userInfo.setNickname(loginUser.getNickname());
        userInfo.setRealNameAuth(loginUser.getReal_name_auth() == 1);
        userInfo.setRealNameChk(loginUser.getReal_name_chk() == 1);
        userInfo.setRealNameOpen(loginUser.getReal_name_open() == 1);
        userInfo.setRealNameReg(loginUser.getReal_name_reg() == 1);

        if (!isLogin) {
            userInfo.setShareId(resultInfo.getShareId());
            userInfo.setCouponNum(resultInfo.getCoupon_num());
            //次卡次数
            userInfo.setUserMeter(resultInfo.getRemain_card());
            //余额
            userInfo.setUserMoney(resultInfo.getRemain_charge());
            userInfo.setIntelligentFee(resultInfo.getPay_type() == 2);

        }
        return UserManager.getInstance(context).updateOrSaveUserInfo(userInfo);
    }

}