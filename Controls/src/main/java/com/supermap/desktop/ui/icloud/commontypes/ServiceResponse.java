package com.supermap.desktop.ui.icloud.commontypes;

/**
 * Created by xie on 2016/12/24.
 */
public class ServiceResponse<T> {
    public int code;
    public String des;
    public T data;

    public ServiceResponse() {
    }

    public interface Code {
        int SUCCESS = 0;
        int NOT_LOGGED_IN = 2;
        int REMAIN_DAYS_LESS_THEN_ONE = 1455;
        int NO_PERMISSION_TO_RETURN = 1456;
        int LOGININ_OTHERPLAINT = 1425;
    }
}

