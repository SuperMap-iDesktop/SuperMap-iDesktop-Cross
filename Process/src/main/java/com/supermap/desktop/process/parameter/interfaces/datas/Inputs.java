package com.supermap.desktop.process.parameter.interfaces.datas;

import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DataType;
import com.supermap.desktop.utilities.StringUtilities;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by highsad on 2017/3/1.
 */
public class Inputs {
	private IProcess process;
	private ConcurrentHashMap<String, InputData> datas = new ConcurrentHashMap<>();

	public Inputs(IProcess process) {
		this.process = process;
	}

	public IProcess getProcess() {
		return this.process;
	}

	public Object getData() {
		return null;
	}

	public InputData getData(String name) {
		if (StringUtilities.isNullOrEmpty(name) || !this.datas.containsKey(name)) {
			return null;
		}

		return this.datas.get(name);
	}


	public void add(InputData data) {
		if (data == null) {
			return;
		}

		if (!this.datas.containsKey(data.getName())) {
			this.datas.put(data.getName(), data);
		}
	}

	public void addData(String name, DataType type) {
		if (StringUtilities.isNullOrEmpty(name)) {
			return;
		}

		addData(new InputData(name, type));
	}

	public void addData(InputData data) {
		if (data == null) {
			return;
		}

		if (!this.datas.containsKey(data.getName())) {
			this.datas.put(data.getName(), data);
		}
	}

	public InputData[] getDatas() {
		ArrayList<InputData> result = new ArrayList<>();

		for (String name : this.datas.keySet()) {
			result.add(this.datas.get(name));
		}
		return result.toArray(new InputData[this.datas.size()]);
	}

	public InputData[] getDatas(DataType type) {
		if (type == null) {
			return null;
		}

		ArrayList<InputData> result = new ArrayList<>();
		for (String name : this.datas.keySet()) {
			InputData data = this.datas.get(name);
			if (data.getType().equals(type)) {
				result.add(data);
			}
		}
		return result.toArray(new InputData[result.size()]);
	}

	public boolean isContains(DataType type) {
		if (type == null) {
			return false;
		}

		boolean result = false;
		for (String name : this.datas.keySet()) {
			InputData data = this.datas.get(name);
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
