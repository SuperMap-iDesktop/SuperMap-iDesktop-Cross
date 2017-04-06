package com.supermap.desktop.process.parameter.interfaces.datas;

import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.utilities.StringUtilities;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by highsad on 2017/3/22.
 */
public class Outputs {
	private IProcess process;
	private ConcurrentHashMap<String, OutputData> datas = new ConcurrentHashMap<>();

	public Outputs(IProcess process) {
		this.process = process;
	}

	public IProcess getProcess() {
		return this.process;
	}

	public OutputData getData(String name) {
		if (StringUtilities.isNullOrEmpty(name) || !this.datas.containsKey(name)) {
			return null;
		}

		return this.datas.get(name);
	}

	public void addData(OutputData data) {
		if (data == null) {
			return;
		}

		if (!this.datas.containsKey(data.getName())) {
			this.datas.put(data.getName(), data);
		}
	}

	public void addData(String name, int type) {
		if (StringUtilities.isNullOrEmpty(name)) {
			return;
		}

		addData(new OutputData(name, type));
	}

	public OutputData[] getDatas() {
		ArrayList<OutputData> result = new ArrayList<>();

		for (String name : this.datas.keySet()) {
			result.add(this.datas.get(name));
		}
		return result.toArray(new OutputData[this.datas.size()]);
	}

	public OutputData[] getDatas(int type) {
		ArrayList<OutputData> result = new ArrayList<>();
		for (String name : this.datas.keySet()) {
			OutputData data = this.datas.get(name);
			if (data.getType() == type) {
				result.add(data);
			}
		}
		return result.toArray(new OutputData[result.size()]);
	}

	public boolean isContains(int type) {
		boolean result = false;
		for (String name : this.datas.keySet()) {
			OutputData data = this.datas.get(name);
			if (data.getType() == type) {
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
