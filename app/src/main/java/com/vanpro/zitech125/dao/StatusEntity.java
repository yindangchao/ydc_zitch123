package com.vanpro.zitech125.dao;

/**
 * Created by Jinsen on 16/3/24.
 */
public class StatusEntity {

    String ver;
    String status;

    int error_code;
    String error_msg;

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static final String SUCC = "ok";
    public static final String ERROR = "error";
}
