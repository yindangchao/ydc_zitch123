package com.vanpro.data.core.http;

/**
 * Created by Administrator on 2015/3/31.
 */
public class Global {

    public static final String ENCODING = "UTF-8";

    public static final String POST_CONTENTTYPE = "application/json; charset=UTF-8";

    public static final String DEFAULT_CONTENTTYPE = "application/x-www-form-urlencoded; charset=UTF-8";

    /**
     * 请求超时时间
     */
    public static final int REQUEST_TIMEOUT_MS = 8000;

    /**
     * 连接超时时间
     */
    public static final int CONNECT_TIMEOUT = 8000;

    /**
     * 读取超时时间
     */
    public static final int READ_TIMEOUT = 8000;

    /**
     * 请求重试次数
     */
    public static final int REQUEST_MAX_RETRIES = 3;

    /**
     * 参数默认编码
     */
    public static final String DEFAULT_PARAMS_ENCODING = "UTF-8";


    public final static String APPLICATION_OCTET_STREAM = "application/octet-stream";

    public final static String APPLICATION_JSON = "application/json";

    public final static String HEAD_SUCCESS = "200 OK";
}
