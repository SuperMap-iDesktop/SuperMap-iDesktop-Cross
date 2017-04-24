package com.supermap.desktop.process.parameter.events;

import com.supermap.desktop.process.parameter.interfaces.datas.InputData;

/**
 * Created by SillyB on 2017/4/23.
 */
public class ValueProviderBindEvent {
	private InputData inputData;
	private int type = 0;
	public static final int BIND = 0;
	public static final int UNBIND = 1;

	public ValueProviderBindEvent(InputData inputData) {
		this.inputData = inputData;
	}

	public InputData getInputData() {
		return inputData;
	}

	public void setInputData(InputData inputData) {
		this.inputData = inputData;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
