package com.supermap.desktop.process.parameter.interfaces.datas;

import com.supermap.desktop.process.parameter.interfaces.datas.types.DataType;

/**
 * description
 * Created by highsad on 2017/3/22.
 */
public class Data {

	private String name;
	private String description;
	private DataType type;
	private Object value;

	public Data(String name, DataType type) {
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return this.name;
	}

	public DataType getType() {
		return this.type;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Object getValue() {
		return this.value;
	}

	public String getDescription() {
		return this.description;
	}
}


