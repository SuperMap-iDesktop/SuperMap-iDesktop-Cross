package com.supermap.desktop.process.parameter.implement;

import com.supermap.desktop.process.parameter.interfaces.IParameterObjects;

/**
 * Created by yuanR on 2017/7/14
 * 参数管理对象
 */
public class AbstractParameterObjects implements IParameterObjects {
	private String name = "";
	private Boolean isRequisite = true;


	private Object valueObject = null;
	private Number valueNumber = null;
	private String valueString = null;
	private Boolean valueBoolean = null;

	@Override
	public void setParameterObject(Object value) {
		this.valueObject = value;
	}

	@Override
	public void setParameterObject(Number value) {
		this.valueObject = value;
	}

	@Override
	public void setParameterObject(String value) {
		this.valueObject = value;
	}

	@Override
	public void setParameterObject(Boolean value) {
		this.valueObject = value;
	}

	@Override
	public Object getParameterObject() {
		return this.valueObject;
	}

	@Override
	public void setParameterObjectDescribe(String name) {
		this.name = name;
	}

	@Override
	public String getParameterObjectDescribe() {
		return this.name;
	}

	@Override
	public Boolean isRequisite() {
		return this.isRequisite;
	}

	@Override
	public void setRequisite(Boolean isRequisite) {
		this.isRequisite = isRequisite;
	}

	/**
	 * 默认构造函数
	 *
	 * @param name
	 * @param isRequisite
	 */
	public AbstractParameterObjects(String name, Boolean isRequisite) {
		this.name = name;
		this.isRequisite = isRequisite;
	}
}
