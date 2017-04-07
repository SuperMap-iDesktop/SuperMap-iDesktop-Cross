package com.supermap.desktop.process.parameter.interfaces.datas;

/**
 * Created by highsad on 2017/4/5.
 */
public class InputData implements IDataDescription, IValueProvider {
	private String name;
	private String tips;
	private int dataType;
	private IValueProvider valueProvider;

	public InputData(String name, int dataType) {
		this(name, null, dataType);
	}

	public InputData(String name, String tips, int dataType) {
		this.name = name;
		this.tips = tips;
		this.dataType = dataType;
	}

	public boolean isBinded() {
		return this.valueProvider != null;
	}

	public boolean isBind(IValueProvider valueProvider) {
		return this.valueProvider == valueProvider;
	}

	public void bind(IValueProvider valueProvider) {
		if (valueProvider != null && this.valueProvider != valueProvider) {
			this.valueProvider = valueProvider;
		}
	}

	public void unbind() {
		this.valueProvider = null;
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

	@Override
	public Object getValue() {
		return this.valueProvider == null ? null : this.valueProvider.getValue();
	}
}
