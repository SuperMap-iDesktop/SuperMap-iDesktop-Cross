package com.supermap.desktop.process.parameter.interfaces.datas;

import com.supermap.desktop.process.parameter.interfaces.datas.types.DataType;

/**
 * Created by highsad on 2017/4/5.
 */
public class OutputData implements IDataDescription, IValueProvider {
	private Object value;
	private String name;
	private String tips;
	private DataType dataType;

	public OutputData(String name, DataType dataType) {
		this(name, null, dataType);
	}

	public OutputData(String name, String tips, DataType dataType) {
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
	public DataType getType() {
		return this.dataType;
	}
}
