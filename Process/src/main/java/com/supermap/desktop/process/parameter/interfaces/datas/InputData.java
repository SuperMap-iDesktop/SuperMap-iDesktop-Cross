package com.supermap.desktop.process.parameter.interfaces.datas;

import com.supermap.desktop.process.parameter.interfaces.datas.types.DataType;

/**
 * Created by highsad on 2017/4/5.
 */
public class InputData implements IDataDescription, IValueProvider {
	private String name;
	private String tips;
	private DataType dataType;
	private IValueProvider valueProvider;

	public InputData(String name, DataType dataType) {
		this(name, null, dataType);
	}

	public InputData(String name, String tips, DataType dataType) {
		this.name = name;
		this.tips = tips;
		this.dataType = dataType;
	}

	public void bind(IValueProvider valueProvider) {
		if (valueProvider != null && this.valueProvider != valueProvider) {
			this.valueProvider = valueProvider;
		}
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

	@Override
	public Object getValue() {
		return this.valueProvider == null ? null : this.valueProvider.getValue();
	}
}
