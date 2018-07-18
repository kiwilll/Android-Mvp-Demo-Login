package com.bwton.metro.usermanager.api;

import android.text.TextUtils;

import com.bwton.metro.sharedata.model.BaseResponse;
import com.bwton.metro.network.BaseApi;
import com.bwton.metro.network.utils.HttpReqParamUtil;
import com.bwton.metro.usermanager.entity.LoginResultInfo;
import com.hjy.encryption.Aes;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hw on 7/12/17.<br>
 */

public class UserApi extends BaseApi {

    //0-快速登录，1-注册，2-修改密码，4-修改手机号码，5-邀请验证码 8-忘记支付密码
    public static final String CODEFLAG_LOGIN = "0";
    public static final String CODEFLAG_REGIST = "1";
    public static final String CODEFLAG_FINDPWD = "2";
    public static final String CODEFLAG_CHANGEPHONE = "4";
    public static final String CODEFLAG_SHARE = "5";
    public static final String CODEFLAG_RESETPWD = "8";


    private static UserService getUserService() {
        return getService(UserService.class);
    }

    /**
     * 使用密码登录
     *
     * @param mobile    手机号
     * @param pwd       密码, 明文
     * @param termToken 设备id
     * @param pushToken 推送token
     * @param bundleId
     * @return
     */
    public static Observable<BaseResponse<LoginResultInfo>> doLoginByPwd(String mobile, String pwd, String termToken, String pushToken, String bundleId) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("mobile_phone", mobile);
        String aesKey = HttpReqParamUtil.genAesKey();
        map.put("password", Aes.encryptByECBPKCS5(pwd, aesKey));
        map.put("term_type", "0");
        map.put("term_token", termToken);
        map.put("push_token", pushToken);
        map.put("device_id", termToken);
        map.put("bundle_id", bundleId);

        String bodyJson = strMapToJson(map);
        Map<String, String> headerMap = getHeaderMap(bodyJson, aesKey);

        Observable<BaseResponse<LoginResultInfo>> observable = getUserService().loginByPwd(headerMap, bodyJson)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    /**
     * 发送短信验证码
     *
     * @param userId
     * @param mobile 手机号
     * @param codeFlag
     * @param idNo
     * @return
     */
    public static Observable<BaseResponse> sendVerifyCode(String userId, String mobile, String codeFlag, String idNo) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", mobile);
        map.put("type", codeFlag);
        if(!TextUtils.isEmpty(userId)) {
            map.put("userid", userId);
        }
        if(!TextUtils.isEmpty(idNo)) {
            map.put("id_no", idNo);
        }
        String bodyJson = strMapToJson(map);
        Map<String, String> headerMap = getHeaderMap(bodyJson, null);
        Observable<BaseResponse> observable = getUserService().sendCode(headerMap, bodyJson)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    /**
     * 使用验证码登录
     *
     * @param mobile    手机号
     * @param code      验证码
     * @param termToken 设备id
     * @param pushToken 推送token
     * @param bundleId
     * @return
     */
    public static Observable<BaseResponse<LoginResultInfo>> doLoginByCode(String mobile, String code, String termToken, String pushToken, String bundleId) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("mobile_phone", mobile);
        map.put("code", code);
        map.put("term_type", "0");
        map.put("term_token", termToken);
        map.put("push_token", pushToken);
        map.put("device_id", termToken);
        map.put("bundle_id", bundleId);

        String bodyJson = strMapToJson(map);
        Map<String, String> headerMap = getHeaderMap(bodyJson, null);

        Observable<BaseResponse<LoginResultInfo>> observable = getUserService().loginByCode(headerMap, bodyJson)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    /**
     * 注册
     *
     * @param mobile        手机号
     * @param pwd           密码
     * @param code          验证码
     * @param termToken     设备id
     * @param pushToken     推送token
     * @param bundleId
     * @return
     */
    public static Observable<BaseResponse<LoginResultInfo>> doRegister(String mobile, String pwd, String code, String termToken, String pushToken, String bundleId) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("mobile_phone", mobile);
        String aesKey = HttpReqParamUtil.genAesKey();
        map.put("password", Aes.encryptByECBPKCS5(pwd, aesKey));
        map.put("code", code);
        map.put("term_type", "0");
        map.put("term_token", termToken);
        map.put("push_token", pushToken);
        map.put("device_id", termToken);
        map.put("bundle_id", bundleId);

        String bodyJson = strMapToJson(map);
        Map<String, String> headerMap = getHeaderMap(bodyJson, aesKey);

        Observable<BaseResponse<LoginResultInfo>> observable = getUserService().register(headerMap, bodyJson)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    /**
     * 修改密码
     *
     * @param mobile 手机号
     * @param pwd    密码
     * @param code   验证码
     * @return
     */
    public static Observable<BaseResponse> changePwd(String mobile, String pwd, String code) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("mobile_phone", mobile);
        String aesKey = HttpReqParamUtil.genAesKey();
        map.put("password", Aes.encryptByECBPKCS5(pwd, aesKey));
        map.put("code", code);
        String bodyJson = strMapToJson(map);
        Map<String, String> headerMap = getHeaderMap(bodyJson, aesKey);

        Observable<BaseResponse> observable = getUserService().resetPwd(headerMap, bodyJson)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    /**
     * 修改用户信息，能修改的有
     * sex
     * birthday
     * city
     * profession
     * nickname
     * userImagePath
     * mobile_phone
     * code
     *
     * @param map
     * @return
     */
    public static Observable<BaseResponse> modifyUserInfo(Map<String, String> map) {
        String bodyJson = strMapToJson(map);
        Map<String, String> headerMap = getHeaderMap(bodyJson, null);
        return getUserService().modifyUserInfo(headerMap, bodyJson).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 用户登出
     *
     * @return
     */
    public static Observable<BaseResponse> doLogout() {
        Map<String, String> map = new HashMap<String, String>();
        String bodyJson = strMapToJson(map);
        Map<String, String> headerMap = getHeaderMap(bodyJson, null);

        Observable<BaseResponse> observable = getUserService().logout(headerMap, bodyJson).subscribeOn(Schedulers.io());
        return observable;
    }

}