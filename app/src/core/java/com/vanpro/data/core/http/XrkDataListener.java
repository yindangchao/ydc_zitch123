package com.vanpro.data.core.http;

import com.vanpro.data.core.http.response.HttpError;

import java.util.Map;

/**
 * Created by Administrator on 2015/3/26.
 */
public interface XrkDataListener {
    void setId(int id);
    void header(Map<String, String> headers);
    void success(Object data);
    void fail(HttpError error);
}
