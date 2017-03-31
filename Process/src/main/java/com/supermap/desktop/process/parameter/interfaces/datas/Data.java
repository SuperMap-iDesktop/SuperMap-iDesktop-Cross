package com.supermap.desktop.process.parameter.interfaces.datas;

import com.supermap.desktop.process.parameter.interfaces.datas.types.DataType;

/**
 * description
 * Created by highsad on 2017/3/22.
 */
public class Data {

	private Object value;

	public Data(String name, DataType type) {

	}

	public String getName() {
		return null;
	}

	public DataType getType() {
		return null;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Object getValue() {
		return this.value;
	}
}


