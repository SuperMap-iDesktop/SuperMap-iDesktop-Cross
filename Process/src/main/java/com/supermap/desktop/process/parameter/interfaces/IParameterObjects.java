package com.supermap.desktop.process.parameter.interfaces;

import com.supermap.desktop.process.parameter.interfaces.datas.Irequisite;

import java.util.Objects;

/**
 * Created by yuanR on 2017/7/14  .
 * 参数对象接口，负责参数管理
 * 例如：控制参数是否为必填字段等
 */
public interface IParameterObjects extends Irequisite {

	String getParameterObjectDescribe();

	void setParameterObjectDescribe(String value);

	Object getParameterObject();

	void setParameterObject(Objects value);

}
