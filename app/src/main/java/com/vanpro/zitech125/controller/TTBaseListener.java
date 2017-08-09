package com.vanpro.zitech125.controller;


import com.vanpro.data.core.http.XrkDataListener;
import com.vanpro.data.core.http.response.HttpError;
import com.vanpro.zitech125.event.DataEvent;

import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * 请求回调
 *
 * Created by Jinsen on 2016/3/23.
 */
public class TTBaseListener implements XrkDataListener {
    public int mTaskId = 0;

    @Override
    public void setId(int id) {
        this.mTaskId = id;
    }

    @Override
    public void header(Map<String, String> headers){

    }

    @Override
    public void success(Object data){

    }

    @Override
    public void fail(HttpError error){
        DataEvent e = new DataEvent();
        e.state = DataEvent.FAIL;
        e.error = error.getCode();
        e.id = mTaskId;
        e.msg = error.getMessage();
        EventBus.getDefault().post(e);
    }
}
