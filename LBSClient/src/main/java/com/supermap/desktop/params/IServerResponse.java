package com.supermap.desktop.params;

import com.supermap.desktop.Interface.IResponse;

/**
 * Created by xie on 2017/1/6.
 * 登录成功后返回结果类
 */
public class IServerResponse implements IResponse{
    public String referer;
    public String  reason;
    public String succeed;
}
