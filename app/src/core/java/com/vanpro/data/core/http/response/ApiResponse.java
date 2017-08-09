package com.vanpro.data.core.http.response;

import java.io.Serializable;


/**
 * 接口数据
 * <p/>
 * Created by tony on 8/24/14.
 */
public class ApiResponse implements Serializable {

    private int code;
    private String response;

    public ApiResponse() {}

    public ApiResponse(int code, String response) {
        this.code = code;
        this.response = response;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "code=" + code +
                ", response='" + response + '\'' +
                '}';
    }
}
