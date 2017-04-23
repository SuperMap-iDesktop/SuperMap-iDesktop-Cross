package com.supermap.desktop.process.parameter.interfaces.datas;

import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.datas.types.Type;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by highsad on 2017/4/5.
 */
public class InputData implements IDataDescription, IValueProvider {
	private String name;
	private String tips;
	private Type dataType;
	private IValueProvider valueProvider;
	private ArrayList<IParameter> parameters;

	public InputData(String name, Type dataType) {
		this(name, null, dataType);
	}

	public InputData(String name, String tips, Type dataType) {
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
	public Type getType() {
		return this.dataType;
	}

	@Override
	public Object getValue() {
		return this.valueProvider == null ? null : this.valueProvider.getValue();
	}

	public void removeParameters(IParameter... parameters) {
		for (IParameter parameter : parameters) {
			this.parameters.remove(parameter);
		}
	}

	public void addParameters(IParameter... parameters) {
		Collections.addAll(this.parameters, parameters);
	}

	public ArrayList<IParameter> getParameters() {
		return parameters;
	}
}
