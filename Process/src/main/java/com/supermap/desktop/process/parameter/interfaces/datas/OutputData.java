package com.supermap.desktop.process.parameter.interfaces.datas;

import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.parameter.interfaces.datas.types.Type;

/**
 * Created by highsad on 2017/4/5.
 */
public class OutputData implements IDataDescription, IValueProvider {
	private IProcess process;
	private Object value;
	private String name;
	private String tips;
	private Type dataType;

	public OutputData(IProcess process, String name, Type dataType) {
		this(process, name, null, dataType);
	}

	public OutputData(IProcess process, String name, String tips, Type dataType) {
		this.process = process;
		this.name = name;
		this.tips = tips;
		this.dataType = dataType;
	}

	public IProcess getProcess() {
		return process;
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
