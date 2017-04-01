package com.supermap.desktop.process.parameter.interfaces.datas;

import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DataType;
import com.supermap.desktop.utilities.StringUtilities;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by highsad on 2017/3/22.
 */
public class Outputs {
	private IProcess process;
	private ConcurrentHashMap<String, Data> datas;

	public Outputs(IProcess process) {
		this.process = process;
	}

	public IProcess getProcess() {
		return this.process;
	}

	public Data getData(String name) {
		if (StringUtilities.isNullOrEmpty(name) || !this.datas.containsKey(name)) {
			return null;
		}

		return this.datas.get(name);
	}

	public void add(Data data) {
		if (data == null) {
			return;
		}

		if (!this.datas.containsKey(data.getName())) {
			this.datas.put(data.getName(), data);
		}
	}

	public Data[] getDatas() {
		ArrayList<Data> result = new ArrayList<>();

		for (String name : this.datas.keySet()) {
			result.add(this.datas.get(name));
		}
		return result.toArray(new Data[this.datas.size()]);
	}

	public Data[] getDatas(DataType type) {
		if (type == null) {
			return null;
		}

		ArrayList<Data> result = new ArrayList<>();
		for (String name : this.datas.keySet()) {
			Data data = this.datas.get(name);
			if (data.getType().equals(type)) {
				result.add(data);
			}
		}
		return result.toArray(new Data[result.size()]);
	}

	public boolean isContains(DataType type) {
		if (type == null) {
			return false;
		}

		boolean result = false;
		for (String name : this.datas.keySet()) {
			Data data = this.datas.get(name);
			if (data.getType().equals(type)) {
				result = true;
				break;
			}
		}
		return result;
	}

	public int getCount() {
		return this.datas.size();
	}
}
