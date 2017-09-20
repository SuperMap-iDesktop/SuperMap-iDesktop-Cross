package com.supermap.desktop.process.parameter.interfaces.datas;

import com.supermap.data.Dataset;
import com.supermap.data.Datasource;
import com.supermap.data.Workspace;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.datas.types.Type;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by highsad on 2017/4/5.
 */
public class InputData implements IDataDescription, IValueProvider {
	private String name;
	private String text;
	private String tips;
	private Type dataType;
	private IValueProvider valueProvider;
	private ArrayList<IParameter> parameters = new ArrayList<>();

	public InputData(String name, Type dataType) {
		this(name, name, dataType);
	}

	public InputData(String name, String text, Type dataType) {
		this(name, text, null, dataType);
	}

	public InputData(String name, String text, String tips, Type dataType) {
		this.name = name;
		this.text = text;
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
	public String getText() {
		return this.text;
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
		Object result = this.valueProvider == null ? null : this.valueProvider.getValue();
		return getUnClosedResult(result);
	}

	private Object getUnClosedResult(Object result) {
		try {
			if (result instanceof Dataset) {
				((Dataset) result).getName();
			} else if (result instanceof Datasource) {
				((Datasource) result).getAlias();
			} else if (result instanceof Workspace) {
				((Workspace) result).getCaption();
			}
		} catch (Exception e) {
			return null;
		}
		return result;
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
