package com.uqiansoft.gateway.constant;


/**
 * 和shiro认证中心通信的一些配置
 * @author xutao
 * @date 2018-12-06 16:21
 */
public class ShiroConfig {

    public static final String AUTH_TOKEN = "usoft-session-id";
    public static final String USER_INFO = "usoft-user-info";

    public static final String SHIRO_SERVICE_ID = "DATAQUALITYAUTHMANAGE-SERVICE";

    public static final String QUALITY_CHECK_AUTH_URI = "/DATAQUALITYAUTHMANAGE-SERVICE/auth/checkAuth";
    public static final String QUALITY_LOGIN_URI = "/DATAQUALITYAUTHMANAGE-SERVICE/auth/login";
    public static final String QUALITY_LOGOUT_URI = "/DATAQUALITYAUTHMANAGE-SERVICE/auth/logout";
    public static final String QUALITY_ADD_ADMIN_URI = "/DATAQUALITYAUTHMANAGE-SERVICE/user/addUsoftAdmin";

    public static final String QUALITY_POLY_WEBSOCKET = "/QUALITY-POLY-SERVICE/webSocket/poly";


}
