package com.supermap.desktop.process.parameter.interfaces.datas;

import com.supermap.desktop.process.parameter.interfaces.datas.types.Type;

/**
 * Created by highsad on 2017/4/5.
 */
public class OutputData implements IDataDescription, IValueProvider {
	private Object value;
	private String name;
	private String tips;
	private Type dataType;

	public OutputData(String name, Type dataType) {
		this(name, null, dataType);
	}

	public OutputData(String name, String tips, Type dataType) {
		this.name = name;
		this.tips = tips;
		this.dataType = dataType;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public Object getValue() {
		return this.value;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getTips() {
		return this.tips;
	}

	@Override
	public Type getType() {
		return this.dataType;
	}

	public String toString() {
		if (tips == null) {
			tips = "";
		}
		return name + "," + tips + "," + dataType;
	}
}
