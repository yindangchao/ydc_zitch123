package com.vanpro.data.core.http;

import java.util.HashMap;

/**
 * Created by Administrator on 2015/3/26.
 */
public abstract class BaseModule implements XrkDataListener{

    public XrkService mService = null;

    public HashMap<String, String> mHeaders = new HashMap<String, String>();

    public void addHeader(String key, String value){
        mHeaders.put(key,value);
    }

    public HashMap<String,String> getHeaders(){
        return mHeaders;
    }

}
