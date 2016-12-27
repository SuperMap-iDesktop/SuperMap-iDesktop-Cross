package com.supermap.desktop.icloud.online;

/**
 * Created by xie on 2016/12/24.
 */
public class AuthenticationException extends Exception {

    public AuthenticationException(Throwable cause) {
        super(cause);
    }

    public AuthenticationException(String msg) {
        super(msg);
    }
}
