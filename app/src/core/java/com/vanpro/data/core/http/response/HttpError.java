package com.vanpro.data.core.http.response;

/**
 * Created by Administrator on 2015/3/25.
 */
public class HttpError {
    private int code;

    private String message;

    public HttpError(){

    }

    public HttpError(int code, String message){
        this.code = code;
        this.message = message;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public enum Error {

        DEFUALT_ERROR(0, "网络不给力，请检查网络"),

        /**
         * TimeoutError
         */
        TIMEOUT_ERROR(1, "网络不给力，请检查网络"),

        /**
         * NoConnectionError
         */
        NO_CONNECTION_ERROR(2, "网络异常，未连接成功"),

        /**
         * AuthFailureError
         */
        AUTH_FAILURE_ERROR(3, "登录信息无效，请重新登录"),

        /**
         * ServerError
         */
        SERVER_ERROR(4, "网络异常，服务器错误"),

        /**
         * NetworkError
         */
        NETWORK_ERROR(5, "网络不可用"),

        /**
         * ParseError
         */
        PARSE_ERROR(6, "数据解析错误"),

        /**
         * 请求数据为空
         */
        RESPONSE_NULL(100, "请求数据为空");

        private int code;
        private String message;

        Error(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

}
