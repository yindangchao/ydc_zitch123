package com.vanpro.zitech125.dao;

/**
 * Created by Jinsen on 16/5/24.
 */
public class TBaseDTO<T> extends BaseDTO {

    T body;

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
}
