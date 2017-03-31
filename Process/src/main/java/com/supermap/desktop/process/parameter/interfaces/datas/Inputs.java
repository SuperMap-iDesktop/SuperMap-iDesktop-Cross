package com.supermap.desktop.process.parameter.interfaces.datas;

import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DataType;
import com.supermap.desktop.utilities.StringUtilities;

import java.util.List;

/**
 * Created by highsad on 2017/3/1.
 */
public class Inputs {
	private IProcess process;
	private List<Data> datas;

	public Inputs() {

	}

	public Data getData(String name) {
		if (StringUtilities.isNullOrEmpty(name)) {
			return null;
		}

		Data result = null;

		for (int i = 0, size = this.datas.size(); i < size; i++) {
			Data data = this.datas.get(i);
			if (data.getName().equals(name)) {
				result = data;
				break;
			}
		}
		return result;
	}

	public Object getData() {
		if (this.process != null && this.process.getOutputs() != null && this.process.getOutputs().size() > 0) {
			return this.process.getOutputs().get(0).getData();
		} else {
			return null;
		}
	}

	public void addData(Data data) {
		if (!this.datas.contains(data)) {
			this.datas.add(data);
		}
	}

	public void addData(String name, DataType type) {
		this.datas.add(new Data(name, type));
	}
}
