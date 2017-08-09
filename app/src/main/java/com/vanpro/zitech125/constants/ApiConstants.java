package com.vanpro.zitech125.constants;

/**
 * Created by Jinsen on 16/3/24.
 */
public class ApiConstants {
    /**
     * 开发环境 HOST
     **/
    private final static String URL_APP_DEV = "http://zainayo.com";

    /**
     * 生产环境 API
     **/
    private final static String URL_APP_PRUD = "http://zainayo.com";

    public static final String LOGIN_TOKEN_NAME = "Token";

    public static final String TOKEN_HEADER_TAG = "Bearer ";

    private static final String BASE_URL = Constants.DEV_MODE ? URL_APP_DEV : URL_APP_PRUD;

    /**
     * 获取最新版本信息
     */
    public static final String UPDATE_URL = BASE_URL + "/index.php/Index/CheckVersionCodeALL";

    /**
     * 上传位置信息
     */
    public static final String UPLOAD_LOCATION_URL = BASE_URL + "/index.php/Dwb/zitech117";

}
