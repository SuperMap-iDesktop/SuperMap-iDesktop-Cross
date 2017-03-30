package com.supermap.desktop.process.parameter.interfaces.datas;

import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.utilities.StringUtilities;

import java.util.List;

/**
 * Created by highsad on 2017/3/1.
 */
public class Inputs {
	private IProcess process;
	private List<IData> datas;

	public Inputs() {

	}

	public IData getData(String name) {
		if (StringUtilities.isNullOrEmpty(name)) {
			return null;
		}

		IData result = null;

		for (int i = 0, size = this.datas.size(); i < size; i++) {
			IData data = this.datas.get(i);
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
}
