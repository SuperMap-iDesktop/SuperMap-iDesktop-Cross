package com.supermap.desktop.process.parameter.implement;

import com.supermap.desktop.process.parameter.interfaces.IParameterObjects;

import java.util.Objects;

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
	public void setParameterObject(Objects value) {
	}

	@Override
	public Object getParameterObject() {
		if (valueNumber != null) {
			return this.valueNumber;
		} else if (valueString != null) {
			return this.valueString;
		} else if (valueBoolean != null) {
			return this.valueBoolean;
		} else if (valueObject != null) {
			return this.valueObject;
		} else {
			return null;
		}
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

	public AbstractParameterObjects(String name, Object valueObject, Boolean isRequisite) {
		this.name = name;
		this.valueObject = valueObject;
		this.isRequisite = isRequisite;
	}

	public AbstractParameterObjects(String name, Number valueNumber, Boolean isRequisite) {
		this.name = name;
		this.valueNumber = valueNumber;
		this.isRequisite = isRequisite;
	}

	public AbstractParameterObjects(String name, String valueString, Boolean isRequisite) {
		this.name = name;
		this.valueString = valueString;
		this.isRequisite = isRequisite;
	}

	public AbstractParameterObjects(String name, Boolean valueBoolean, Boolean isRequisite) {
		this.name = name;
		this.valueBoolean = valueBoolean;
		this.isRequisite = isRequisite;
	}

}
