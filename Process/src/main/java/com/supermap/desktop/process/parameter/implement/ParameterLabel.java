package com.supermap.desktop.process.parameter.implement;

import com.supermap.desktop.process.enums.ParameterType;

/**
 * @author XiaJT
 */
public class ParameterLabel extends AbstractParameter {
	private String describe;

	@Override
	public String getType() {
		return ParameterType.LABEL;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	@Override
	public String getDescribe() {
		return describe;
	}
}
