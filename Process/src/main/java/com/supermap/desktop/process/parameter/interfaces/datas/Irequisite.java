package com.supermap.desktop.process.parameter.interfaces.datas;

/**
 * Created by yuanR on 2017/7/14 .
 * 尝试创建此接口，负责管理参数是否必填
 */
public interface Irequisite {
	void setRequisite(boolean value);

	boolean isRequisite();

	boolean isReady();
}
