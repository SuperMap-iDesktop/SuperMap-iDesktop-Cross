package com.supermap.desktop.process.parameter.interfaces.datas;

/**
 * Created by highsad on 2017/4/5.
 */
public class OutputData implements IDataDescription, IValueProvider {
	private Object value;
	private String name;
	private String tips;
	private int dataType;

	public OutputData(String name, int dataType) {
		this(name, null, dataType);
	}

	public OutputData(String name, String tips, int dataType) {
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
	public int getType() {
		return this.dataType;
	}
}
