package com.vanpro.zitech125.event;

/**
 * Created by Jinsen on 16/7/6.
 */
public class PhotoMenuActionEvent {
    public final static int ACTION_TIPS = 0;
    public final static int ACTION_RETAKE = 1;
    public final static int ACTION_DELETE = 2;

    public int action ;

    public PhotoMenuActionEvent(int action){
        this.action = action;
    }
}
