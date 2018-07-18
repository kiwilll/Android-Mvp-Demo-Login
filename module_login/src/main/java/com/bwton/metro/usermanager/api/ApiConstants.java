package com.bwton.metro.usermanager.api;

/**
 * Created by hw on 17/12/7.<br>
 */

public class ApiConstants {

    // 0100 用户不存在
    public static final String HAVE_NO_USER = "0100";
    // 0101 终端限制
    public static final String DEVICE_LIMIT = "0101";
    // 0102 黑名单用户  您的账户存在风险，暂时无法使用，请与客服联系
    public static final String BLACK_LIST_USER = "0102";
    // 0103 用户状态不正确（注销或停用等）  您的账户状态异常，暂时无法使用，请与客服联系
    public static final String USER_STATE_ERROR = "0103";

    public static final String URL_USER_AGREEMENT = "https://api.msx.bwton.com/api_h5/protocol/yhProtocol.html";
    //用户协议
    public static String getYongHuXieYi(String cityId) {
        return "https://api.msx.bwton.com/api_h5/" + String.format("helpCenter/hextechController.html?cityId=%s&typeCode=yhxy", cityId);
    }
    public static final String URL_LOGIN_BY_CODE = "msx://m.bwton.com/login/code";
    public static final String URL_LOGIN_BY_PWD = "msx://m.bwton.com/login/password";
    public static final String URL_REGISTER = "msx://m.bwton.com/login/register";
    public static final String URL_FIND_PWD = "msx://m.bwton.com/login/findpwd";
    public static final String URL_WEBVIEW = "msx://m.bwton.com/webview/ui";
}
