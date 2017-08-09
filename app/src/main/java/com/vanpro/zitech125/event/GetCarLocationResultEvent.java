package com.vanpro.zitech125.event;

/**
 * Created by Jinsen on 16/7/2.
 */
public class GetCarLocationResultEvent {
    public boolean locationSucc;

    public GetCarLocationResultEvent(boolean succ){
        locationSucc = succ;
    }
}
